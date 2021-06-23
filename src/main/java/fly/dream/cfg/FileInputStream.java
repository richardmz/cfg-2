package fly.dream.cfg;

import java.io.BufferedReader;
import java.io.IOException;

class FileInputStream
{
    private final BufferedReader reader;
    private final Position position;

    public FileInputStream(BufferedReader reader)
    {
        this.reader = reader;
        this.position = new Position();
    }

    public char read() throws EndOfStreamException, IOException
    {
        char c = readChar();
        updatePosition(c);
        return c;
    }

    public boolean endOfStream() throws IOException
    {
        reader.mark(1);
        int c = reader.read();
        reader.reset();
        return c == -1;
    }

    public char peek() throws EndOfStreamException, IOException
    {
        reader.mark(1);
        char c = readChar();
        reader.reset();
        return c;
    }

    public void skip() throws EndOfStreamException, IOException
    {
        char c = readChar();
        updatePosition(c);
    }

    public Position getPosition()
    {
        return position;
    }


    // Helper ----------------------------------------------------------------------------------------------------------

    private void updatePosition(char c)
    {
        if (c == '\n')
        {
            position.incLine();
        }
        else
        {
            position.incColumn();
        }
    }

    private char readChar() throws EndOfStreamException, IOException
    {
        return (char) readInt();
    }

    private int readInt() throws EndOfStreamException, IOException
    {
        int x = reader.read();
        if (x == -1)
        {
            throw new EndOfStreamException();
        }
        else
        {
            return x;
        }
    }
}
