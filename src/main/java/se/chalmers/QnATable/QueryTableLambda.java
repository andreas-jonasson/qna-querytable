package se.chalmers.QnATable;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import se.chalmers.qna.fulfillment.model.QNABotFullfillmentRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class QueryTableLambda implements RequestHandler<QNABotFullfillmentRequest, String>
{
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private static DynamoDB dynamoDB = new DynamoDB(client);
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private LambdaLogger logger;
    private static final String QNA_LIST_TABLE_NAME = System.getenv("QNA_LIST_TABLE_NAME");
    private static final String QNA_LIST_PARTITION_KEY = System.getenv("QNA_LIST_PARTITION_KEY");
    private static final String QNA_LIST_SORT_KEY =  System.getenv("QNA_LIST_SORT_KEY");
    private static final String QNA_LIST_VALUE_KEY =  System.getenv("QNA_LIST_VALUE_KEY");
    private static final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();

    @Override
    public String handleRequest(QNABotFullfillmentRequest event, Context context)
    {
        Set<TableRow> rows = new HashSet<>();
        String topic;
        String category;
        GetItemRequest request;

        if (event == null && context == null)
        {

            rows = UpdateTableLambda.parseLocalFile();
            for (TableRow row : rows)
                System.out.println(row);
            System.exit(0);
        }

        logger = context.getLogger();
        logger.log("Inside SeedDynamoDB::handleRequest");
        logger.log("Event:\n" + gson.toJson(event));

        topic = getTopic(event);
        topic = "fruit";

        category = getCategory(event);
        category = "yellow";

        QueryResult result = queryTable(topic, category);

        return result.toString();
    }

    private QueryResult queryTable(String partitionKey, String sortKey)
    {
        Table table = dynamoDB.getTable(QNA_LIST_TABLE_NAME);

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression(QNA_LIST_PARTITION_KEY + " = :t and begins_with(" + QNA_LIST_SORT_KEY + ", :q)")
                    .withValueMap(new ValueMap().withString(":t", partitionKey).withString(":q", sortKey));

        ItemCollection<QueryOutcome> items = table.query(querySpec);

        QueryResult result = new QueryResult();
        Iterator<Item> iterator = items.iterator();
        Item item = null;

        while (iterator.hasNext())
        {
            item = iterator.next();
            String category = item.getString(QNA_LIST_SORT_KEY);
            String value = item.getString(QNA_LIST_VALUE_KEY);

            if (category.endsWith("#title"))
                result.setTitle(value);
            else
                result.addItem(value);
        }

        return result;
    }

    private String getTopic(QNABotFullfillmentRequest event)
    {
        return "";
    }

    private String getCategory(QNABotFullfillmentRequest event)
    {
        return "";
    }
}
