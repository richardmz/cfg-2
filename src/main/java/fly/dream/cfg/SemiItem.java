package fly.dream.cfg;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fly.dream.cfg.ItemType.*;

class SemiItem
{
    private final ItemType type;

    private final String key;

    private final List<Item> elements;

    private final Map<String, Item> properties;


    SemiItem(@NotNull ItemType type)
    {
        this.type = type;
        this.key = null;
        switch (type)
        {
            case MAP:
                this.properties = new HashMap<>();
                this.elements = null;
                break;
            case LIST:
                this.elements = new ArrayList<>();
                this.properties = null;
                break;
            default: // PROPERTY
                this.elements = null;
                this.properties = null;
                break;
        }
    }

    /**
     * Constructs a semi-item with type value equals 'PROPERTY'
     * @param key The key of the property
     */
    SemiItem(String key)
    {
        this.key = key;
        this.type = PROPERTY;
        this.elements = null;
        this.properties = null;
    }


    ItemType getType()
    {
        return type;
    }

    Map<String, Item> getProperties()
    {
        return properties;
    }

    void put(String key, Item property)
    {
        assert type == MAP;
        assert properties != null;
        properties.put(key, property);
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

    Item finishProperty(String value)
    {
        assert type == PROPERTY;
        return new PropertyItem(value);
    }

    Item finishProperty(Item value)
    {
        assert type == PROPERTY;
        return new PropertyItem(value);
    }

    Item finishObject()
    {
        assert type == MAP;
        return new MapItem(properties);
    }

    Item finishList()
    {
        assert type == LIST;
        return new ListItem(elements);
    }

    String getKey()
    {
        assert type == PROPERTY;
        return key;
    }
}
