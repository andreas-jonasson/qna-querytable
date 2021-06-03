package se.chalmers.QnATable;


import software.amazon.awscdk.core.*;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.dynamodb.*;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.eventsources.S3EventSource;
import software.amazon.awscdk.services.lambda.eventsources.S3EventSourceProps;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.amazon.awscdk.services.s3.BucketProps;
import software.amazon.awscdk.services.s3.EventType;


import java.util.*;

import static se.chalmers.QnATable.Config.*;

public class QnaQuerytableStack extends Stack
{
    public QnaQuerytableStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public QnaQuerytableStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        //=======================================================================
        // Create a DynamoDB table for the QnA-bot lists
        // partition key := "Topic"
        // Example: fruit:yellow -> lemon
        //=======================================================================

        Attribute primaryKeyAttribute = Attribute.builder()
                .name(QNA_LIST_PARTITION_KEY)
                .type(AttributeType.STRING).build();

        Table qnaListTable = new Table(this, "qna-list-table", TableProps.builder()
                .tableName(QNA_LIST_TABLE_NAME)
                .partitionKey(primaryKeyAttribute)
                .build());

        CfnOutput.Builder.create(this, "qna-list-table-output")
                .description("qna-list-table-arn")
                .value(qnaListTable.getTableArn())
                .build();


        //=======================================================================
        // Building a bucket for assets and give the seeder lambda permission to
        // read it.
        //=======================================================================
        Bucket assetBucket = new Bucket(this, "qna-list-bucket", BucketProps.builder()
                .encryption(BucketEncryption.UNENCRYPTED)
                .bucketName("qna-list-bucket")
                .build());


        //=======================================================================
        // Role for AWS Lambda
        //=======================================================================
        IPrincipal lambdaPrincipal = new ServicePrincipal("lambda.amazonaws.com");

        Role lambdaApiRole = new Role(this, "qna-list-lambda-role", RoleProps.builder()
                .assumedBy(lambdaPrincipal)
                .build());


        //=======================================================================
        // Attaching a role that will allow AWS Lambda to access logs
        //=======================================================================
        lambdaApiRole.addToPolicy(new PolicyStatement(PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .actions(Arrays.asList("logs:CreateLogGroup", "logs:CreateLogStream", "logs:PutLogEvents"))
                .resources(Collections.singletonList("*"))
                .build() ));

        //=======================================================================
        // Building our API Lambda function
        //=======================================================================
        Map<String, String> lambdaEnvMap = new HashMap<String, String>()
        {{
            put("QNA_LIST_TABLE_NAME", QNA_LIST_TABLE_NAME);
            put("QNA_LIST_PARTITION_KEY", QNA_LIST_PARTITION_KEY);
            put("QNA_LIST_VALUE_KEY", QNA_LIST_VALUE_KEY);
        }};

        //=======================================================================
        // Role for seeder Lambda
        //=======================================================================
        Role lambdaUpdateRole = new Role(this, "qna-table-update-lambda-role", RoleProps.builder()
                .assumedBy(lambdaPrincipal)
                .build());

        //=======================================================================
        // Policy for seeder Lambda that gives permission to asset bucket and
        // strength DynamoDB.
        //=======================================================================
        lambdaUpdateRole.addToPolicy(new PolicyStatement(PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .actions(Arrays.asList("logs:CreateLogGroup", "logs:CreateLogStream", "logs:PutLogEvents"))
                .resources(Collections.singletonList("*"))
                .build() ));

        //=======================================================================
        // Building our update Lambda function
        //=======================================================================
        Function updateLambda = new Function(qnaListTable, "qna-table-update-lambda", FunctionProps.builder()
                .code(Code.fromAsset("./out/artifacts/lambda/qna-querytable.jar"))
                .handler("se.chalmers.QnATable.UpdateTableLambda::handleRequest")
                .functionName("qna-table-update-lambda")
                .runtime(Runtime.JAVA_11)
                .role(lambdaUpdateRole)
                .environment(lambdaEnvMap)
                .timeout(Duration.seconds(90))
                .memorySize(1024)
                .build());

        qnaListTable.grantReadWriteData(updateLambda);
        assetBucket.grantRead(lambdaUpdateRole);


        //=======================================================================
        // Make the update Lambda function trigger on created items in the
        // assetBucket.
        //=======================================================================

        List<EventType> s3Events = Collections.singletonList(EventType.OBJECT_CREATED);

        updateLambda.addEventSource(new S3EventSource(assetBucket, S3EventSourceProps.builder()
                .events(s3Events)
                .build()));

        CfnOutput.Builder.create(this, "lambda-update-output")
                .description("lambda-update-arn")
                .value(updateLambda.getFunctionArn())
                .build();

        //=======================================================================
        // Create the query table Lambda function that is triggered from the
        // QnA-bot
        //=======================================================================

        Function queryLambda = new Function(qnaListTable, "qna-table-query-lambda", FunctionProps.builder()
                .code(Code.fromAsset("./out/artifacts/lambda/qna-querytable.jar"))
                .handler("se.chalmers.QnATable.QueryTableLambda::handleRequest")
                .functionName("QNA-query-table-lambda")
                .runtime(Runtime.JAVA_11)
                .role(lambdaApiRole)
                .environment(lambdaEnvMap)
                .timeout(Duration.seconds(30))
                .memorySize(1024)
                .build());

        qnaListTable.grantReadData(queryLambda);

        CfnOutput.Builder.create(this, "qna-table-query-lambda-output")
                .description("qna-table-query-lambda-arn")
                .value(queryLambda.getFunctionArn())
                .build();
    }
}
