package fly.dream.cfg;

import java.util.List;
import java.util.Map;

public interface Item
{
    ItemType getType();

    Item getValue();

    String getString();

    Map<String, Item> getMap();

    List<Item> getList();

    String toString(int lvl);
}
