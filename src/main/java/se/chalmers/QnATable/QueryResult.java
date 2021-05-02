package se.chalmers.QnATable;

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
            stringBuilder.append('\t').append(item).append('\n');

        return stringBuilder.toString();
    }

    public String toMarkdownString()
    {
        if (title == null && items.isEmpty())
            return "Ups! No result!";

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("#").append(title).append('\n');
        for (String item : items)
            stringBuilder.append(item).append("\\\n");

        return stringBuilder.toString();
    }
}
