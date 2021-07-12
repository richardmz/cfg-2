package fly.dream.cfg;

class Entry
{
    private final Item value;

    Entry(String str)
    {
        this.value = new StringItem(str);
    }

    Entry(Item value)
    {
        this.value = value;
    }

    public Item getValue()
    {
        return value;
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

        Entry propertyItem = (Entry) o;

        return value.equals(propertyItem.value);
    }

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

    @Override
    public String toString()
    {
        return value.toString();
    }
}
