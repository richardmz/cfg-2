package fly.dream.cfg;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static fly.dream.cfg.ItemType.LIST;

public class ListItem implements Item, Printable
{
    private final List<Item> elements;

    ListItem(List<Item> elements)
    {
        this.elements = elements;
    }

    @Override
    public ItemType getType()
    {
        return LIST;
    }

    public List<Item> getList()
    {
        return elements;
    }

    @Override
    public String getString()
    {
        throw new RuntimeException("Should not get string value from a " + LIST);
    }

    @Override
    public Map<String, Item> getMap()
    {
        throw new RuntimeException("Should not get object from a " + LIST);
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

        ListItem listItem = (ListItem) o;

        return elements.equals(listItem.elements);
    }

    @Override
    public int hashCode()
    {
        return elements.hashCode();
    }

    @Override
    public String toString(int lvl)
    {
        StringJoiner stringJoiner;
        switch (elements.get(0).getType())
        {
            case STRING:
                stringJoiner = new StringJoiner("\n", "\n" + Util.getIndent(lvl - 1) + "[\n", "\n" + Util.getIndent(lvl - 1) + "]");
                break;
            case MAP:
            case LIST:
                stringJoiner = new StringJoiner("", "\n" + Util.getIndent(lvl - 1) + "[", "\n" + Util.getIndent(lvl - 1) + "]");
                break;
            default: // PROPERTY
                throw new IllegalStateException("ListItem should not has any 'PROPERTY' element");
        }
        for (Item element : elements)
        {
            stringJoiner.add(Util.getIndent(lvl) + ((Printable) element).toString(lvl + 1));
        }
        return stringJoiner.toString();
    }

    @Override
    public String toString()
    {
        StringJoiner stringJoiner;
        switch (elements.get(0).getType())
        {
            case STRING:
                stringJoiner = new StringJoiner("\n", "[\n", "\n]");
                break;
            case MAP:
            case LIST:
                stringJoiner = new StringJoiner("", "[", "\n]");
                break;
            default: // PROPERTY
                throw new IllegalStateException("ListItem should not has any 'PROPERTY' element");
        }
        for (Item element : elements)
        {
            stringJoiner.add(Util.getIndent(1) + ((Printable) element).toString(1));
        }
        return stringJoiner.toString();
    }
}
