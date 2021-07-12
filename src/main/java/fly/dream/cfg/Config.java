package fly.dream.cfg;

import java.util.Map;
import java.util.StringJoiner;

public class Config
{
    private final Map<String, Item> entries;

    Config(Map<String, Item> entries)
    {
        this.entries = entries;
    }

    public boolean contains(String key)
    {
        return entries.containsKey(key);
    }

    public Item get(String key)
    {
        return entries.get(key);
    }

    @Override
    public String toString()
    {
        String delimiter = "\n\n";
        String prefix = "Start ---------------------------------------------------\n";
        String suffix = "\n----------------------------------------------------- end";
        StringJoiner stringJoiner = new StringJoiner(delimiter, prefix, suffix);

        for (Map.Entry<String, Item> entry : entries.entrySet())
        {
            stringJoiner.add(entry.getKey() + " " + ((Printable) entry.getValue()).toString(1));
        }

        return stringJoiner.toString();
    }
}
