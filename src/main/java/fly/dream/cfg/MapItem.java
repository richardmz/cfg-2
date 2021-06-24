package fly.dream.cfg;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static fly.dream.cfg.ItemType.MAP;

public class MapItem implements Item, Printable
{
    private final Map<String, Item> properties;

    MapItem(Map<String, Item> properties)
    {
        this.properties = properties;
    }

    @Override
    public ItemType getType()
    {
        return MAP;
    }

    @Override
    public Map<String, Item> getMap()
    {
        return properties;
    }

    @Override
    public String getString()
    {
        throw new RuntimeException("Should not get string value from a " + MAP);
    }

    @Override
    public List<Item> getList()
    {
        throw new RuntimeException("Should not get list from a " + MAP);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        MapItem mapItem = (MapItem) o;

        return properties.equals(mapItem.properties);
    }

    @Override
    public int hashCode()
    {
        return properties.hashCode();
    }

    @Override
    public String toString(int lvl)
    {
        StringJoiner stringJoiner = new StringJoiner("\n", "\n" + Util.getIndent(lvl - 1) + "{\n", "\n" + Util.getIndent(lvl - 1) + "}");
        for (Map.Entry<String, Item> entry : properties.entrySet())
        {
            stringJoiner.add(Util.getIndent(lvl) + entry.getKey() + " " + ((Printable) entry.getValue()).toString(lvl + 1));
        }
        return stringJoiner.toString();
    }

    @Override
    public String toString()
    {
        StringJoiner stringJoiner = new StringJoiner("\n", "{\n", "\n}");
        for (Map.Entry<String, Item> entry : properties.entrySet())
        {
            stringJoiner.add(Util.getIndent(1) + entry.getKey() + " " + ((Printable) entry.getValue()).toString(2));
        }
        return stringJoiner.toString();
    }
}
