package fly.dream.cfg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class EntryEliminationTest
{
    public static void main(String[] args)
    {
        String filename = "config.cfg";
        Path file = Paths.get(System.getProperty("user.dir") + File.separator + "test" + File.separator + filename);
        try
        {
            Loader loader = new Loader(file);
            Config config = loader.load();
            Item mapItem = config.get("map");
            System.out.println("Type of map: " + mapItem.getType());
            Map<String, Item> map = mapItem.getMap();
            Item property = map.get("property");
            System.out.println("Type of property: " + property.getType());
        }
        catch (IOException | ConfigException e)
        {
            e.printStackTrace();
        }
    }
}
