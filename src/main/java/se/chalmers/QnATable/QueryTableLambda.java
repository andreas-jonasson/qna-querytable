package se.chalmers.QnATable;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.GetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import se.chalmers.qna.fulfillment.model.QNABotFullfillmentRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class QueryTableLambda implements RequestHandler<QNABotFullfillmentRequest, String>
{
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private LambdaLogger logger;
    private static final String QNA_LIST_TABLE_NAME = System.getenv("QNA_LIST_TABLE_NAME");
    private static final String QNA_LIST_PRIMARY_KEY = System.getenv("QNA_LIST_PRIMARY_KEY");
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

        request = createItemRequest(topic, category);

        return "Done.";
    }

    private GetItemRequest createItemRequest(String primaryKey, String sortKey)
    {
        return new GetItemRequest();
    }

    private String getTopic(QNABotFullfillmentRequest event)
    {
        return "";
    }

    private String getCategory(QNABotFullfillmentRequest event)
    {
        return "";
    }

    private Set<TableRow> getList(String topic, String category)
    {
        Set<TableRow> result = new HashSet<>();

        HashMap<String,AttributeValue> key_to_get =
                new HashMap<String,AttributeValue>();

        //key_to_get.put(QNA_LIST_TABLE_NAME, new AttributeValue(name));

        return result;
    }

}
