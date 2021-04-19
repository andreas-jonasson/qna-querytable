package se.chalmers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
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
    private static final String HEADER_ROW = "Pokemon";
    private String[] elements;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private LambdaLogger logger;
    private static final String STRENGTH_TABLE_NAME = System.getenv("STRENGTH_TABLE_NAME");
    private static final String STRENGTH_PRIMARY_KEY = System.getenv("STRENGTH_PRIMARY_KEY");
    private static final String STRENGTH_SINGLE_VALUE_KEY =  System.getenv("STRENGTH_SINGLE_VALUE_KEY");
    private static final String STRENGTH_ALL_VALUES_KEY =  System.getenv("STRENGTH_ALL_VALUES_KEY");

    private HashMap<String, String> attackEffects = new HashMap<>(50);

    @Override
    public String handleRequest(S3Event event, Context context)
    {
        if (event == null && context == null)
        {
            parseLocalFile();

            for (String key : attackEffects.keySet())
                System.out.println(key + "\t" + attackEffects.get(key));

            System.exit(0);
        }

        logger = context.getLogger();
        logger.log("Inside SeedDynamoDB::handleRequest");
        logger.log("Event:\n" + gson.toJson(event));

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
            parseFile(objectData);
            seedStrengthsTable();
        }
        catch (IOException e)
        {
            logger.log("Failed to read file from S3 bucket:\t\n" + e.getMessage());
        }

        return "Done.";
    }

    public static void main(String[] args)
    {
        new SeedDynamoDB().handleRequest(null, null);
    }

    private void parseLocalFile()
    {
        File startDir = new File(System.getProperty("user.dir"));
        File strengthTable = new File(startDir + "\\assets", "strength.csv");
        InputStream inputStream = null;

        try
        {
            inputStream = new FileInputStream(strengthTable);
        }
        catch (IOException e)
        {
            System.err.println("Error opening local file: " + strengthTable + "\n" + e);
            System.exit(1);
        }

        try
        {
            parseFile(inputStream);
        }
        catch (IOException e)
        {
            System.err.println("Error reading file: " + strengthTable + "\n" + e);
            System.exit(1);
        }
    }

    private void parseFile(InputStream inputStream) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while((line = reader.readLine()) != null)
        {
            parseLine(line);
        }

        // And the other way around...
        for (ElementTypes defenseElement : ElementTypes.values())
        {
            String values = ""; // "[ ";
            boolean first = true;

            for (ElementTypes attackElement : ElementTypes.values())
            {
                String thisValue = attackEffects.get(attackElement.toString() + "_" + defenseElement.toString());

                if (first)
                {
                    values += thisValue;
                    first = false;
                }
                else
                    values += " ," + thisValue;
            }
            //values += " ]";
            attackEffects.put(defenseElement.toString() + "_Defense_All", values);
        }
    }

    private void parseLine(String line)
    {
        if (line.startsWith(HEADER_ROW))
            getElements(line);
        else
        {
            String[] parts = line.split(";");
            String allEffects = ""; //""[ ";

            for (int i = 1;i < parts.length; i++)
            {
                attackEffects.put(parts[0] + "_" + elements[i], parts[i]);

                if (i != 1)
                    allEffects += ", ";
                allEffects += parts[i];
            }
            // allEffects += " ]";

            attackEffects.put(parts[0] + "_Attack_All", allEffects);
        }
    }

    private void seedStrengthsTable()
    {
        logger.log("seedDynamoTable()\nTable: " + STRENGTH_TABLE_NAME + "\nprimary key: " + STRENGTH_PRIMARY_KEY +
                "\nsingle value key: " + STRENGTH_SINGLE_VALUE_KEY + "\nall values key: " + STRENGTH_ALL_VALUES_KEY);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

        for (String attackKey : attackEffects.keySet())
        {
            HashMap<String, AttributeValue> itemValues = new HashMap<String, AttributeValue>();

            itemValues.put(STRENGTH_PRIMARY_KEY, new AttributeValue().withS(attackKey));

            if (attackKey.endsWith("_All"))
                itemValues.put(STRENGTH_ALL_VALUES_KEY, new AttributeValue().withS(attackEffects.get(attackKey)));
            else
                itemValues.put(STRENGTH_SINGLE_VALUE_KEY, new AttributeValue().withN(attackEffects.get(attackKey)));

            PutItemRequest putItemRequest = new PutItemRequest()
                    .withTableName(STRENGTH_TABLE_NAME)
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

    private void getElements(String line)
    {
        String[] parts = line.split(";");
        elements = Arrays.copyOfRange(parts, 0, parts.length);
    }
}
