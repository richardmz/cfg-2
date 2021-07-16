package fly.dream.cfg;

public class UnexpectedEndOfStreamException extends Exception
{
    public UnexpectedEndOfStreamException()
    {
        super("Unexpected end of stream");
    }
}
