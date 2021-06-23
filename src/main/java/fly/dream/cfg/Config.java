package fly.dream.cfg;

import java.util.Map;
import java.util.StringJoiner;

public class Config
{
    private final Map<String, Item> properties;

    public Config(Map<String, Item> properties)
    {
        this.properties = properties;
    }

    public boolean contains(String key)
    {
        return properties.containsKey(key);
    }

    public Item get(String key)
    {
        return properties.get(key);
    }

    @Override
    public String toString()
    {
        String delimiter = "\n\n";
        String prefix = "Start ---------------------------------------------------\n";
        String suffix = "\n----------------------------------------------------- end";
        StringJoiner stringJoiner = new StringJoiner(delimiter, prefix, suffix);

        for (Map.Entry<String, Item> entry : properties.entrySet())
        {
            stringJoiner.add(entry.getKey() + " " + entry.getValue().toString(1));
        }

        return stringJoiner.toString();
    }
}
