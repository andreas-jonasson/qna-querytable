package se.chalmers.QnATable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashSet;

public class QueryResult
{
    private String title;
    private HashSet<String> items;

    public QueryResult()
    {
        items = new HashSet<>();
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void addItem(String item)
    {
        items.add(item);
    }

    public String toString()
    {
        if (title == null && items.isEmpty())
            return "Ups! No result!";

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(title).append('\n');

        for (String item : items)
        {
            if (item != null && item.length() > 1)
                stringBuilder.append('\t').append(item).append("\n");
        }

        return stringBuilder.toString();
    }

    public String toMarkdownString()
    {
        if (title == null && items.isEmpty())
            return "Ups! No result!";

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("# ").append(title).append("  \n");
        for (String item : items)
            stringBuilder.append(item).append("  \n");

        return stringBuilder.toString();
    }

    public String toJson()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
