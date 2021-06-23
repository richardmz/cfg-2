package fly.dream.cfg;

public class UnexpectedEndOfStream extends Exception
{
    public UnexpectedEndOfStream()
    {
        super("Unexpected end of stream");
    }
}
