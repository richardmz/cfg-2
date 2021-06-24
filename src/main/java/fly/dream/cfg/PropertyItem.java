package fly.dream.cfg;

import java.util.List;
import java.util.Map;

import static fly.dream.cfg.ItemType.PROPERTY;

public class PropertyItem implements Item, Property, Printable
{
    private final Item value;

    PropertyItem(String str)
    {
        this.value = new StringItem(str);
    }

    PropertyItem(Item value)
    {
        this.value = value;
    }

    @Override
    public ItemType getType()
    {
        return PROPERTY;
    }

    @Override
    public Item getValue()
    {
        return value;
    }

    @Override
    public String getString()
    {
        throw new RuntimeException("Should not get string from a " + PROPERTY);
    }

    @Override
    public Map<String, Item> getMap()
    {
        throw new RuntimeException("Should not get object from a " + PROPERTY);
    }

    @Override
    public List<Item> getList()
    {
        throw new RuntimeException("Should not get list from a " + PROPERTY);
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

        PropertyItem propertyItem = (PropertyItem) o;

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
