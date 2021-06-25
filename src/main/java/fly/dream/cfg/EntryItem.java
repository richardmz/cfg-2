package fly.dream.cfg;

import java.util.List;
import java.util.Map;

import static fly.dream.cfg.ItemType.ENTRY;

public class EntryItem implements Item, Entry, Printable
{
    private final Item value;

    EntryItem(String str)
    {
        this.value = new StringItem(str);
    }

    EntryItem(Item value)
    {
        this.value = value;
    }

    @Override
    public ItemType getType()
    {
        return ENTRY;
    }

    @Override
    public Item getValue()
    {
        return value;
    }

    @Override
    public String getString()
    {
        throw new RuntimeException("Should not get string from a " + ENTRY);
    }

    @Override
    public Map<String, Item> getMap()
    {
        throw new RuntimeException("Should not get object from a " + ENTRY);
    }

    @Override
    public List<Item> getList()
    {
        throw new RuntimeException("Should not get list from a " + ENTRY);
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

        EntryItem propertyItem = (EntryItem) o;

        return value.equals(propertyItem.value);
    }

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

    @Override
    public String toString(int lvl)
    {
        return ((Printable) value).toString(lvl);
    }

    @Override
    public String toString()
    {
        return value.toString();
    }
}
