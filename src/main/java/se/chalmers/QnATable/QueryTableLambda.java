package se.chalmers.QnATable;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import se.chalmers.qna.fulfillment.model.QNABotFullfillmentRequest;

import javax.sound.midi.SysexMessage;
import java.util.HashMap;

public class QueryTableLambda implements RequestHandler<QNABotFullfillmentRequest, QNABotFullfillmentRequest>
{
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private static DynamoDB dynamoDB = new DynamoDB(client);
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private LambdaLogger logger;
    private static final String QNA_LIST_TABLE_NAME = System.getenv("QNA_LIST_TABLE_NAME");
    private static final String QNA_LIST_PARTITION_KEY = System.getenv("QNA_LIST_PARTITION_KEY");
    private static final String QNA_LIST_VALUE_KEY =  System.getenv("QNA_LIST_VALUE_KEY");
    private static final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();

    @Override
    public QNABotFullfillmentRequest handleRequest(QNABotFullfillmentRequest event, Context context)
    {
        HashMap<String, QueryResult> topics = new HashMap<>();

        if (context == null)
        {
            topics = new UpdateTableLambda().parseLocalFile();
            for (String qid : topics.keySet())
            {
                System.out.println(topics.get(qid));
            }

            System.exit(0);
        }

        logger = context.getLogger();
        logger.log("Inside SeedDynamoDB::handleRequest");
        logger.log("Event:\n" + gson.toJson(event));

        QueryResult result = queryTable(getQuestionId(event));

        return updateEvent(event, result);
    }

    private QueryResult queryTable(String partitionKey)
    {
        Table table = dynamoDB.getTable(QNA_LIST_TABLE_NAME);

        Item item = table.getItem(QNA_LIST_PARTITION_KEY, partitionKey);
        QueryResult result = gson.fromJson(item.getString(QNA_LIST_VALUE_KEY), QueryResult.class);

        return result;
    }

    private String getQuestionId(QNABotFullfillmentRequest event)
    {
        return event.res.session.qnabot_qid;
    }

    private QNABotFullfillmentRequest updateEvent(QNABotFullfillmentRequest event, QueryResult result)
    {
        event.res.plainMessage = result.toMarkdownString();

        return event;
    }
}
