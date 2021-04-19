package se.chalmers;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableProps;
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
        // sort key := "Sort"
        // Example: fruit:yellow -> lemon
        //=======================================================================

        Attribute primaryKeyAttribute = Attribute.builder()
                .name("Topic")
                .type(AttributeType.STRING).build();

        Attribute sortKeyAttribute = Attribute.builder()
                .name("Sort")
                .type(AttributeType.STRING).build();

        Table qnaListTable = new Table(this, "qna-list-table", TableProps.builder()
                .partitionKey(primaryKeyAttribute)
                .sortKey(sortKeyAttribute)
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

        /*


        //=======================================================================
        // Building our API Lambda function
        //=======================================================================
        Map<String, String> lambdaEnvMap = new HashMap<String, String>()
        {{
            put("QNA_LIST_TABLE_NAME", qnaListTable.getTableName());
            put("QNA_LIST_SINGLE_VALUE_KEY", "value");
        }};

        Function strengthLambda = new Function(qnaListTable, "qna-list-qna-lambda", FunctionProps.builder()
                .code(Code.fromAsset("./out/artifacts/lambda/qna.jar"))
                .handler("com.myorg.lambda.FindWeakness::handleRequest")
                .functionName("pokemon-weakness-lambda")
                .runtime(Runtime.JAVA_11)
                .role(lambdaApiRole)
                .environment(lambdaEnvMap)
                .timeout(Duration.seconds(30))
                .memorySize(1024)
                .build());

        qnaListTable.grantReadData(strengthLambda);

        CfnOutput.Builder.create(this, "lambda-weakness-output")
                .description("lambda-weakness-arn")
                .value(strengthLambda.getFunctionArn())
                .build();

        //=======================================================================
        // Role for seeder Lambda
        //=======================================================================
        Role lambdaSeederRole = new Role(this, "pokemon-lambda-seeder-role", RoleProps.builder()
                .assumedBy(lambdaPrincipal)
                .build());

        //=======================================================================
        // Policy for seeder Lambda that gives permission to asset bucket and
        // strength DynamoDB.
        //=======================================================================
        lambdaSeederRole.addToPolicy(new PolicyStatement(PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .actions(Arrays.asList("logs:CreateLogGroup", "logs:CreateLogStream", "logs:PutLogEvents"))
                .resources(Collections.singletonList("*"))
                .build() ));



        //=======================================================================
        // Building our Seeder Lambda function
        //=======================================================================
        Function seederLambda = new Function(qnaListTable, "pokemon-seeder-lambda", FunctionProps.builder()
                .code(Code.fromAsset("./out/artifacts/lambda/pokemon_api.jar"))
                .handler("com.myorg.lambda.SeedDynamoDB::handleRequest")
                .functionName("pokemon-seeder-lambda")
                .runtime(Runtime.JAVA_11)
                .role(lambdaSeederRole)
                .environment(lambdaEnvMap)
                .timeout(Duration.seconds(90))
                .memorySize(1024)
                .build());

        qnaListTable.grantReadWriteData(seederLambda);
        assetBucket.grantRead(lambdaSeederRole);

        //=======================================================================
        // Make the Seeder Lambda function trigger on created items in the
        // assetBucket.
        //=======================================================================

        List<EventType> s3Events = Arrays.asList(EventType.OBJECT_CREATED);

        seederLambda.addEventSource(new S3EventSource(assetBucket, S3EventSourceProps.builder()
                .events(s3Events)
                .build()));

        CfnOutput.Builder.create(this, "lambda-seeder-output")
                .description("lambda-seeder-arn")
                .value(seederLambda.getFunctionArn())
                .build();

         */
    }
}
