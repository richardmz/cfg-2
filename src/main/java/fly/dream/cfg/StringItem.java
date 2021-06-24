package fly.dream.cfg;

import java.util.List;
import java.util.Map;

import static fly.dream.cfg.ItemType.STRING;

public class StringItem implements Item, Printable
{
    private final String value;

    StringItem(String value)
    {
        this.value = value;
    }

    @Override
    public ItemType getType()
    {
        return STRING;
    }

    @Override
    public String getString()
    {
        return value;
    }

    @Override
    public Map<String, Item> getMap()
    {
        throw new RuntimeException("Should not get object from a " + STRING);
    }

    @Override
    public List<Item> getList()
    {
        throw new RuntimeException("Should not get list from a " + STRING);
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

        StringItem that = (StringItem) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

    @Override
    public String toString(int lvl)
    {
        return "\"" + handleEscape(value) + "\"";
    }

    @Override
    public String toString()
    {
        return "\"" + handleEscape(value) + "\"";
    }

    private String handleEscape(String value)
    {
        StringBuilder stringBuilder = new StringBuilder();
        int length = value.length();
        for (int i = 0; i < length; i ++)
        {
            char c = value.charAt(i);
            switch (c)
            {
                case '\b':
                    stringBuilder.append("\\b");
                    break;
                case '\f':
                    stringBuilder.append("\\f");
                    break;
                case '\n':
                    stringBuilder.append("\\n");
                    break;
                case '\r':
                    stringBuilder.append("\\r");
                    break;
                case '\t':
                    stringBuilder.append("\\t");
                    break;
                case '\"':
                    stringBuilder.append("\\\"");
                    break;
                case '\\':
                    stringBuilder.append("\\\\");
                    break;
                default:
                    stringBuilder.append(c);
                    break;
            }
        }
        return stringBuilder.toString();
    }
}
