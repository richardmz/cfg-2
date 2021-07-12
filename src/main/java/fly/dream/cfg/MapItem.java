package fly.dream.cfg;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static fly.dream.cfg.ItemType.MAP;

public class MapItem implements Item, Printable
{
    private final Map<String, Item> entries;

    MapItem(Map<String, Item> entries)
    {
        this.entries = entries;
    }

    @Override
    public ItemType getType()
    {
        return MAP;
    }

    @Override
    public Map<String, Item> getMap()
    {
        return entries;
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

        return entries.equals(mapItem.entries);
    }

    @Override
    public int hashCode()
    {
        return entries.hashCode();
    }

    @Override
    public String toString(int lvl)
    {
        if (entries.size() == 0)
        {
            return "\n" + Util.getIndent(lvl - 1) + "{\n" + Util.getIndent(lvl - 1) + "}";
        }
        else
        {
            StringJoiner stringJoiner = new StringJoiner("\n", "\n" + Util.getIndent(lvl - 1) + "{\n", "\n" + Util.getIndent(lvl - 1) + "}");
            for (Map.Entry<String, Item> entry : entries.entrySet())
            {
                stringJoiner.add(Util.getIndent(lvl) + entry.getKey() + " " + ((Printable) entry.getValue()).toString(lvl + 1));
            }
            return stringJoiner.toString();
        }
    }

    @Override
    public String toString()
    {
        StringJoiner stringJoiner = new StringJoiner("\n", "{\n", "\n}");
        for (Map.Entry<String, Item> entry : entries.entrySet())
        {
            stringJoiner.add(Util.getIndent(1) + entry.getKey() + " " + ((Printable) entry.getValue()).toString(2));
        }
        return stringJoiner.toString();
    }
}
