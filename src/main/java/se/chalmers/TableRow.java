package se.chalmers;

public class TableRow
{
    public String primaryKey;
    public String sortingKey;
    public String value;

    public TableRow(String primaryKey, String sortingKey, String value)
    {
        this.primaryKey = primaryKey;
        this.sortingKey = sortingKey;
        this.value = value;
    }

    public String toString()
    {
        return primaryKey + "\t" + sortingKey + "\t" + value;
    }
}
