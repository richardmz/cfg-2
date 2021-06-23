package fly.dream.cfg;

public class ConfigSyntaxException extends Exception
{
    public ConfigSyntaxException()
    {
        super("The config file having some syntax error");
    }
}
