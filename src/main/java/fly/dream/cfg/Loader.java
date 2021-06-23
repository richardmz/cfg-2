package fly.dream.cfg;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Loader
{
    private final Path path;

    public Loader(Path path)
    {
        this.path = path;
    }

    public Config load() throws IOException, ConfigException
    {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8))
        {
            Parser parser = new Parser(reader);
            return parser.parse();
        }
    }
}
