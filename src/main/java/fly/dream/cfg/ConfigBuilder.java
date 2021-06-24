package fly.dream.cfg;

import java.util.ArrayDeque;
import java.util.Deque;

import static fly.dream.cfg.ItemType.LIST;
import static fly.dream.cfg.ItemType.MAP;

class ConfigBuilder
{
    /**
     * Only accept the following types of {@code SemiItem}:
     * <ul>
     *     <li>PROPERTY</li>
     *     <li>OBJECT</li>
     *     <li>LIST</li>
     * </ul>
     */
    private final Deque<SemiItem> semiItemStack;


    ConfigBuilder()
    {
        this.semiItemStack = new ArrayDeque<>();
        SemiItem root = new SemiItem(MAP);
        this.semiItemStack.push(root);
    }


    void pushProperty(String key)
    {
        semiItemStack.push(new SemiItem(key));
    }

    void pushObject()
    {
        semiItemStack.push(new SemiItem(MAP));
    }

    void pushList()
    {
        semiItemStack.push(new SemiItem(LIST));
    }

    void finishProperty(String value)
    {
        SemiItem semiProperty = semiItemStack.pop();
        Item property = semiProperty.finishProperty(value);
        switch (peekType())
        {
            case MAP:
                SemiItem semiObject = semiItemStack.peek();
                assert semiObject != null : "Stack should not be empty";
                semiObject.put(semiProperty.getKey(), ((Property) property).getValue());
                break;
            case PROPERTY:
            case LIST:
            default: // STRING
                throw new IllegalStateException("Property value should only be in an 'OBJECT'");
        }
    }

    void finishObject()
    {
        SemiItem semiObject = semiItemStack.pop();
        Item object = semiObject.finishObject();
        switch (peekType())
        {
            case PROPERTY:
                SemiItem semiProperty = semiItemStack.pop();
                Item property = semiProperty.finishProperty(object);
                SemiItem parentItem = semiItemStack.peek();
                assert parentItem != null : "Stack should not be empty";
                parentItem.put(semiProperty.getKey(), ((Property) property).getValue());
                break;
            case LIST:
                SemiItem semiList = semiItemStack.peek();
                assert semiList != null : "Stack should not be empty";
                semiList.add(object);
                break;
            case MAP:
            default: // STRING
                throw new IllegalStateException("Object value should only be in either a 'PROPERTY' or a 'LIST'");
        }
    }

    void finishList()
    {
        SemiItem semiList = semiItemStack.pop();
        Item list = semiList.finishList();
        switch (peekType())
        {
            case PROPERTY:
                SemiItem semiProperty = semiItemStack.pop();
                Item property = semiProperty.finishProperty(list);
                SemiItem parentItem = semiItemStack.peek();
                assert parentItem != null : "Stack should not be empty";
                parentItem.put(semiProperty.getKey(), ((Property) property).getValue());
                break;
            case LIST:
                SemiItem parentList = semiItemStack.peek();
                assert parentList != null : "Stack should not be empty";
                parentList.add(list);
                break;
            case MAP:
            default: // STRING
                throw new IllegalStateException("List value should only be in either a 'PROPERTY' or a 'LIST'");
        }
    }

    boolean addElement(String value)
    {
        SemiItem semiList = semiItemStack.peek();
        assert semiList != null : "Stack should not be empty";
        return semiList.add(new StringItem(value));
    }

    Config toConfig()
    {
        SemiItem root = semiItemStack.getFirst();
        return new Config(root.getProperties());
    }

    /**
     * @return The current context of the {@code semiItemStack}. Possible types:
     * <ul>
     *     <li>ROOT</li>
     *     <li>PROPERTY</li>
     *     <li>MAP</li>
     *     <li>LIST</li>
     *     <li>STRING_LIST</li>
     *     <li>OBJECT_LIST</li>
     *     <li>LIST_LIST</li>
     * </ul>
     */
    Context getContext()
    {
        int stackSize = semiItemStack.size();
        if (stackSize == 0)
        {
            throw new IllegalStateException("Semi-item stack should not be empty");
        }
        else if (stackSize == 1)
        {
            return Context.ROOT;
        }
        else
        {
            SemiItem topItem = semiItemStack.peek();
            assert topItem != null;
            ItemType type = topItem.getType();
            switch (type)
            {
                case PROPERTY:
                    return Context.PROPERTY;
                case MAP:
                    return Context.MAP;
                case LIST:
                    if (topItem.isEmpty())
                    {
                        return Context.LIST;
                    }
                    else
                    {
                        switch (topItem.firstElementType())
                        {
                            case STRING:
                                return Context.STRING_LIST;
                            case MAP:
                                return Context.MAP_LIST;
                            case LIST:
                                return Context.LIST_LIST;
                            default: // PROPERTY
                                throw new IllegalStateException("Should not add any 'PROPERTY' type semi-item to the element list");
                        }
                    }
                default: // STRING
                    throw new IllegalStateException("Should not push any 'STRING' type semi-item into the stack");
            }
        }
    }

    ItemType peekType()
    {
        if (semiItemStack.size() == 0)
        {
            throw new IllegalStateException("Semi-item stack should not be empty");
        }
        else
        {
            SemiItem item = semiItemStack.peek();
            return item.getType();
        }
    }
}
