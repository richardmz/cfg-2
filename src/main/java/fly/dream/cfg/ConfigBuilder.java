package fly.dream.cfg;

import java.util.ArrayDeque;
import java.util.Deque;

import static fly.dream.cfg.SemiItemType.*;

class ConfigBuilder
{
    /**
     * Only accept the following types of {@code SemiItem}:
     * <ul>
     *     <li>ENTRY</li>
     *     <li>MAP</li>
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


    void pushEntry(String key)
    {
        semiItemStack.push(new SemiItem(key));
    }

    void pushMap()
    {
        semiItemStack.push(new SemiItem(MAP));
    }

    void pushList()
    {
        semiItemStack.push(new SemiItem(LIST));
    }

    void finishEntry(String value)
    {
        SemiItem semiEntry = semiItemStack.pop();
        Entry entry = semiEntry.finishEntry(value);
        switch (peekType())
        {
            case MAP:
                SemiItem semiMap = semiItemStack.peek();
                assert semiMap != null : "Stack should not be empty";
                semiMap.put(semiEntry.getKey(), entry.getValue());
                break;
            case ENTRY:
            case LIST:
                throw new IllegalStateException("Entry value should only be in an 'MAP'");
        }
    }

    void finishMap()
    {
        SemiItem semiMap = semiItemStack.pop();
        Item map = semiMap.finishMap();
        switch (peekType())
        {
            case ENTRY:
                SemiItem semiEntry = semiItemStack.pop();
                Entry entry = semiEntry.finishEntry(map);
                SemiItem parentItem = semiItemStack.peek();
                assert parentItem != null : "Stack should not be empty";
                parentItem.put(semiEntry.getKey(), entry.getValue());
                break;
            case LIST:
                SemiItem semiList = semiItemStack.peek();
                assert semiList != null : "Stack should not be empty";
                semiList.add(map);
                break;
            case MAP:
                throw new IllegalStateException("MAP value should only be in either a 'ENTRY' or a 'LIST'");
        }
    }

    void finishList()
    {
        SemiItem semiList = semiItemStack.pop();
        Item list = semiList.finishList();
        switch (peekType())
        {
            case ENTRY:
                SemiItem semiEntry = semiItemStack.pop();
                Entry entry = semiEntry.finishEntry(list);
                SemiItem parentItem = semiItemStack.peek();
                assert parentItem != null : "Stack should not be empty";
                parentItem.put(semiEntry.getKey(), entry.getValue());
                break;
            case LIST:
                SemiItem parentList = semiItemStack.peek();
                assert parentList != null : "Stack should not be empty";
                parentList.add(list);
                break;
            case MAP:
                throw new IllegalStateException("List value should only be in either a 'ENTRY' or a 'LIST'");
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
        return new Config(root.getEntries());
    }

    /**
     * @return The current context of the {@code semiItemStack}. Possible types:
     * <ul>
     *     <li>ROOT</li>
     *     <li>ENTRY</li>
     *     <li>MAP</li>
     *     <li>LIST</li>
     *     <li>STRING_LIST</li>
     *     <li>MAP_LIST</li>
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
            SemiItemType type = topItem.getType();
            switch (type)
            {
                case ENTRY:
                    return Context.ENTRY;
                case MAP:
                    return Context.MAP;
                default: // LIST
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
                            default: // ENTRY
                                throw new IllegalStateException("Should not add any 'ENTRY' type semi-item to the element list");
                        }
                    }
            }
        }
    }

    SemiItemType peekType()
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
