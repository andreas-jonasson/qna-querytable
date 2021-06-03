package se.chalmers.QnATable.test;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import se.chalmers.QnATable.QueryTableLambda;
import se.chalmers.qna.fulfillment.model.QNABotFullfillmentRequest;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestLambda
{
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testStack() throws IOException
    {
        FileReader reader = new FileReader(new File("src/test/resources/qna-request-event.json"));
        QNABotFullfillmentRequest qnaEvent = gson.fromJson(reader, QNABotFullfillmentRequest.class);
        new QueryTableLambda().handleRequest(qnaEvent, null);

    }
}
