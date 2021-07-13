package fly.dream.cfg;

import java.io.BufferedReader;
import java.io.IOException;

class FileInputStream
{
    private final BufferedReader reader;
    private final Position position;

    FileInputStream(BufferedReader reader)
    {
        this.reader = reader;
        this.position = new Position();
    }

    char read() throws EndOfStreamException, IOException
    {
        char c = readChar();
        updatePosition(c);
        return c;
    }

    boolean endOfStream() throws IOException
    {
        reader.mark(1);
        int c = reader.read();
        reader.reset();
        return c == -1;
    }

    char peek() throws EndOfStreamException, IOException
    {
        reader.mark(1);
        char c = readChar();
        reader.reset();
        return c;
    }

    void skip() throws EndOfStreamException, IOException
    {
        char c = readChar();
        updatePosition(c);
    }

    Position getPosition()
    {
        return position;
    }


    // Helpers ---------------------------------------------------------------------------------------------------------

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
