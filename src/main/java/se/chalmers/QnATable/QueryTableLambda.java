package se.chalmers.QnATable;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;

public class QueryTableLambda implements RequestStreamHandler
{
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private static DynamoDB dynamoDB = new DynamoDB(client);
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private LambdaLogger logger;
    private static final String QNA_LIST_TABLE_NAME = System.getenv("QNA_LIST_TABLE_NAME");
    private static final String QNA_LIST_PARTITION_KEY = System.getenv("QNA_LIST_PARTITION_KEY");
    private static final String QNA_LIST_VALUE_KEY =  System.getenv("QNA_LIST_VALUE_KEY");

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException
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
        String event = getEventAsString(inputStream);

        logger.log("Inside SeedDynamoDB::handleRequest");
        logger.log("Event:\n" + event);

        QueryResult result = queryTable(getQuestionId(event));
        event = updateEvent(event, result);
        returnEventAsStream(event, outputStream);
    }

    private QueryResult queryTable(String partitionKey)
    {
        Table table = dynamoDB.getTable(QNA_LIST_TABLE_NAME);

        Item item = table.getItem(QNA_LIST_PARTITION_KEY, partitionKey);
        logger.log("Partition key name: " + QNA_LIST_PARTITION_KEY +  "\nParition key: " + partitionKey );
        logger.log("Item: " + item.toString());

        QueryResult result = gson.fromJson(item.getString(QNA_LIST_VALUE_KEY), QueryResult.class);

        return result;
    }

    private String getQuestionId(String event)
    {

        String qid;
        JsonElement element = gson.fromJson(event, JsonElement.class);
        qid = element.getAsJsonObject().getAsJsonObject("res").getAsJsonObject("session").get("qnabot_qid").getAsString();

        logger.log("getQuestionId: " + qid);

        return qid;

     }

    private String updateEvent(String event, QueryResult result)
    {
        JsonElement element = gson.fromJson(event, JsonElement.class);
        // res.session.appContext.altMessages.markdown = markupMessage;
        JsonObject alt = element.getAsJsonObject().getAsJsonObject("res").getAsJsonObject("session").getAsJsonObject("appContext").getAsJsonObject("altMessages");

        if(alt != null)
            alt.addProperty("markdown", result.toMarkdownString());
        else
        {
            element.getAsJsonObject().getAsJsonObject("res").addProperty("message", result.toString());
            element.getAsJsonObject().getAsJsonObject("res").addProperty("plainMessage", result.toString());
        }

        event = gson.toJson(element);
        logger.log("Processed event:\n" + event);

        return event;
    }

    private String getEventAsString(InputStream inputStream)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        StringBuilder stringBuilder = new StringBuilder();
        try
        {

            String line;
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line);
            }
            reader.close();
            inputStream.close();
        }
        catch (IOException e)
        {
            logger.log("Error reading event input stream: " + e);
            System.exit(-1);
        }

        return stringBuilder.toString();
    }

    private void returnEventAsStream(String event, OutputStream outputStream)
    {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8"))));

        try
        {
            writer.write(event);
            if (writer.checkError())
            {
                logger.log("WARNING: Writer encountered an error.");
            }
        }
        catch (IllegalStateException | JsonSyntaxException exception)
        {
            logger.log(exception.toString());
        }
        finally
        {
            writer.close();
        }
    }
}
