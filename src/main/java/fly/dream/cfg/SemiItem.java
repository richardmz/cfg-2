package fly.dream.cfg;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fly.dream.cfg.SemiItemType.*;

class SemiItem
{
    private final SemiItemType type;

    private final String key;

    private final List<Item> elements;

    private final Map<String, Item> entries;


    SemiItem(@NotNull SemiItemType type)
    {
        this.type = type;
        this.key = null;
        switch (type)
        {
            case MAP:
                this.entries = new HashMap<>();
                this.elements = null;
                break;
            case LIST:
                this.elements = new ArrayList<>();
                this.entries = null;
                break;
            default: // ENTRY
                this.elements = null;
                this.entries = null;
                break;
        }
    }

    /**
     * Constructs a semi-item with type value equals 'ENTRY'
     * @param key The key of the entry
     */
    SemiItem(String key)
    {
        this.key = key;
        this.type = ENTRY;
        this.elements = null;
        this.entries = null;
    }


    SemiItemType getType()
    {
        return type;
    }

    Map<String, Item> getEntries()
    {
        return entries;
    }

    void put(String key, Item value)
    {
        assert type == MAP;
        assert entries != null;
        entries.put(key, value);
    }

    boolean isEmpty()
    {
        assert type == LIST;
        assert elements != null;
        return elements.isEmpty();
    }

    ItemType firstElementType()
    {
        assert type == LIST;
        assert elements != null;
        return elements.get(0).getType();
    }

    boolean add(Item element)
    {
        assert type == LIST;
        assert elements != null;
        return elements.add(element);
    }

    Entry finishEntry(String value)
    {
        assert type == ENTRY;
        return new Entry(value);
    }

    Entry finishEntry(Item value)
    {
        assert type == ENTRY;
        return new Entry(value);
    }

    Item finishMap()
    {
        assert type == MAP;
        return new MapItem(entries);
    }

    Item finishList()
    {
        assert type == LIST;
        return new ListItem(elements);
    }

    String getKey()
    {
        assert type == ENTRY;
        return key;
    }
}
