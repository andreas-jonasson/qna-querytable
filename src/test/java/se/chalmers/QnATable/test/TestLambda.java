package se.chalmers.QnATable.test;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import se.chalmers.QnATable.QueryTableLambda;

import java.io.*;

public class TestLambda
{
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testStack() throws IOException
    {
        FileInputStream inputStream = new FileInputStream(new File("src/test/resources/qna-request-event.json"));
        OutputStream outputStream = new ByteArrayOutputStream();
        new QueryTableLambda().handleRequest(inputStream, outputStream, null);

        System.out.println(outputStream);
    }
}
