package com.myorg;

import software.amazon.awscdk.core.CfnOutput;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableProps;

public class QnaQuerytableStack extends Stack {
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
    }
}
