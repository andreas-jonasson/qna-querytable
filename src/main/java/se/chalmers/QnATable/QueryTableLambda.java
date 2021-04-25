package se.chalmers.QnATable;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.LexEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashSet;
import java.util.Set;

public class QueryTableLambda implements RequestHandler<LexEvent, String>
{
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private LambdaLogger logger;
    private static final String QNA_LIST_TABLE_NAME = System.getenv("QNA_LIST_TABLE_NAME");
    private static final String QNA_LIST_PRIMARY_KEY = System.getenv("QNA_LIST_PRIMARY_KEY");
    private static final String QNA_LIST_SORT_KEY =  System.getenv("QNA_LIST_SORT_KEY");
    private static final String QNA_LIST_VALUE_KEY =  System.getenv("QNA_LIST_VALUE_KEY");
    private Set<TableRow> rows = new HashSet<>();


    @Override
    public String handleRequest(LexEvent event, Context context)
    {
        if (event == null && context == null) {
            rows = UpdateTableLambda.parseLocalFile();
            for (TableRow row : rows)
                System.out.println(row);
            System.exit(0);
        }

        logger = context.getLogger();
        logger.log("Inside SeedDynamoDB::handleRequest");
        logger.log("Event:\n" + gson.toJson(event));

        return "Done.";
    }
}
