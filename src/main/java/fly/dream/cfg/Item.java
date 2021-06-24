package fly.dream.cfg;

import java.util.List;
import java.util.Map;

public interface Item
{
    ItemType getType();

    String getString();

    Map<String, Item> getMap();

    List<Item> getList();
}
