package se.chalmers.QnATable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;


public class UpdateTableLambda implements RequestHandler<S3Event, String>
{
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private LambdaLogger logger;
    private static final String QNA_LIST_TABLE_NAME = System.getenv("QNA_LIST_TABLE_NAME");
    private static final String QNA_LIST_PRIMARY_KEY = System.getenv("QNA_LIST_PARTITION_KEY");
    private static final String QNA_LIST_VALUE_KEY =  System.getenv("QNA_LIST_VALUE_KEY");


    @Override
    public String handleRequest(S3Event event, Context context)
    {
        HashMap<String, QueryResult> topics = new HashMap<>();

        if (event == null && context == null)
        {
            topics = parseLocalFile();
            for (String qid : topics.keySet())
            {
                System.out.println(topics.get(qid));
            }

            System.exit(0);
        }

        logger = context.getLogger();
        logger.log("Inside SeedDynamoDB::handleRequest");
        logger.log("Event:\n" + gson.toJson(event));

        String topic = event.getRecords().get(0).getS3().getObject().getKey().toLowerCase();
        topic = topic.substring(0, topic.lastIndexOf('.'));

        S3EventNotification.S3EventNotificationRecord record = event.getRecords().get(0);
        String srcBucket = record.getS3().getBucket().getName();
        String srcKey = record.getS3().getObject().getUrlDecodedKey();

        // Download the content from S3 into a stream
        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(
             srcBucket, srcKey));
        InputStream objectData = s3Object.getObjectContent();

        try
        {
             // Change to insert all the values at once!
             parseFile(objectData, topics);
             updateQnaTable(topics);
        }
        catch (IOException e)
        {
            logger.log("Failed to read file from S3 bucket:\t\n" + e.getMessage());
        }

        return "Done.";
    }

    public static void main(String[] args)
    {
        new UpdateTableLambda().handleRequest(null, null);
    }

    public HashMap<String, QueryResult> parseLocalFile()
    {
        File startDir = new File(System.getProperty("user.dir"));
        File testFile = new File(startDir + "\\src\\test\\resources\\", "fruit.csv");
        InputStream inputStream = null;
        HashMap<String, QueryResult> topics = new HashMap<>();

        try
        {
            inputStream = new FileInputStream(testFile);
        }
        catch (IOException e)
        {
            System.err.println("Error opening local file: " + testFile + "\n" + e);
            System.exit(1);
        }

        try
        {
            parseFile(inputStream, topics);
        }
        catch (IOException e)
        {
            System.err.println("Error reading file: " + testFile + "\n" + e);
            System.exit(1);
        }

        return topics;
    }

    private static void parseFile(InputStream inputStream, HashMap<String, QueryResult> topics) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int rownumber = 0;

        while((line = reader.readLine()) != null)
        {
            parseLine(line, rownumber, topics);
            rownumber++;
        }
    }

    private static void parseLine(String line, int rownumber, HashMap<String, QueryResult> topics)
    {
        String[] parts = line.split(";");

        if (parts == null || parts.length < 2)
        {
            System.err.println("Discarding bad input on line#: " + rownumber + "\t" + line);
            return;
        }

        String sortKey = parts[0].toLowerCase();
        String value = parts[1];
        String qid;

        if (sortKey.endsWith("#title"))
            qid = sortKey.substring(0, sortKey.lastIndexOf('#'));
        else
            qid = sortKey;


        QueryResult currentResult = topics.get(qid);

        if (currentResult == null)
            currentResult = new QueryResult();


        // Pad a number to entries avoid collisions. Order in csv-file becomes the sort order in output.
        if (sortKey.endsWith("#title"))
            currentResult.setTitle(value);
        else
            currentResult.addItem(value);

        topics.put(qid, currentResult);
    }

    private void updateQnaTable(HashMap<String, QueryResult> topics)
    {
        logger.log("updateQnaTable()\nTable: " + QNA_LIST_TABLE_NAME +
                "\nprimary key: " + QNA_LIST_PRIMARY_KEY +
                "\nvalue key: " + QNA_LIST_VALUE_KEY);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

        for (String qid : topics.keySet())
        {
            HashMap<String, AttributeValue> itemValues = new HashMap<String, AttributeValue>();

            itemValues.put(QNA_LIST_PRIMARY_KEY, new AttributeValue().withS(qid));
            itemValues.put(QNA_LIST_VALUE_KEY, new AttributeValue().withS(topics.get(qid).toJson()));

            PutItemRequest putItemRequest = new PutItemRequest()
                    .withTableName(QNA_LIST_TABLE_NAME)
                    .withItem(itemValues);

            try
            {
                PutItemResult result = client.putItem(putItemRequest);
            }
            catch (AmazonServiceException e)
            {
                StringBuilder output = new StringBuilder();

                output.append("Attempting to insert:\n");

                for (String key : itemValues.keySet())
                    output.append(key + "\t" + itemValues.get(key) + "\n");

                output.append(e.getMessage());
                logger.log(output.toString());
            }
        }
    }
}
