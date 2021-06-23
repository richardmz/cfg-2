package fly.dream.cfg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Test
{
    public static void main(String[] args)
    {
        String filename = "config.cfg";
        Path file = Paths.get(System.getProperty("user.dir") + File.separator + "test" + File.separator + filename);
        try
        {
            Loader loader = new Loader(file);
            long start = System.currentTimeMillis();
            Config config = loader.load();
            long used = System.currentTimeMillis() - start;
            System.out.println(config);
            System.out.println("(Used " + used + " ms)");
        }
        catch (IOException | ConfigException e)
        {
            e.printStackTrace();
        }
    }
}
