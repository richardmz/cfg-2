package fly.dream.cfg;

import java.io.IOException;

import static fly.dream.cfg.Constants.*;
import static fly.dream.cfg.TokenType.KEY;
import static fly.dream.cfg.TokenType.STRING;
import static fly.dream.cfg.Util.toHex;

class TokenStream
{
    final static int STRING_LIMIT = 1024;

    private final FileInputStream inputStream;

    TokenStream(FileInputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    Token read() throws EndOfStreamException, IOException, TokenException, UnexpectedEndOfStreamException
    {
        skipSpaces();
        char c = inputStream.read();
        if (c == '#')
        {
            skipComment();
            return read();
        }
        else if (isPunctuation(c))
        {
            return new Token(c, inputStream.getPosition());
        }
        else if (c == '\"')
        {
            return readString();
        }
        else
        {
            return readKey(c);
        }
    }

    boolean endOfStream() throws IOException
    {
        return inputStream.endOfStream();
    }


    // Helpers ---------------------------------------------------------------------------------------------------------

    private Token readString() throws UnexpectedEndOfStreamException, IOException, TokenException
    {
        // Get position
        Position position = inputStream.getPosition();

        int i = 0;
        char[] chars = new char[STRING_LIMIT];

        try
        {
            char c = inputStream.read();
            while (c != '"' && i < STRING_LIMIT)
            {
                if (c == '\\')
                {
                    chars[i] = read_unicode_or_escaped();
                }
                else if (isControlCharacter(c))
                {
                    throw croak("String contains control character: " + toHex(c));
                }
                else
                {
                    chars[i] = c;
                }
                i ++;
                c = inputStream.read();
            }
        }
        catch (EndOfStreamException e)
        {
            throw new UnexpectedEndOfStreamException();
        }


        if (i == STRING_LIMIT)
        {
            throw croak("String too long");
        }

        String result = new String(chars, 0, i);
        return new Token(STRING, result, position);
    }

    private Token readKey(char first) throws IOException, UnexpectedEndOfStreamException
    {
        StringBuilder builder = new StringBuilder();
        builder.append(first);
        // Get position
        Position position = inputStream.getPosition();
        // Read the remaining characters if any
        try
        {
            while (!isSpace(inputStream.peek()))
            {
                builder.append(inputStream.read());
            }
        }
        catch (EndOfStreamException e)
        {
            throw new UnexpectedEndOfStreamException();
        }
        return new Token(KEY, builder.toString(), position);
    }

    private void skipSpaces() throws EndOfStreamException, IOException
    {
        while (isSpace(inputStream.peek()))
        {
            inputStream.skip();
        }
    }

    private void skipComment() throws EndOfStreamException, IOException
    {
        while (!isLineSeparator(inputStream.peek()))
        {
            inputStream.skip();
        }
    }

    private char read_unicode_or_escaped() throws EndOfStreamException, TokenException, IOException
    {
        char c = inputStream.read();
        if (c != 'u' && c != 'b' && c != 'f' &&
            c != 'n' && c != 'r' && c != 't' &&
            c != '"' && c != '\\')
        {
            throw croak("Unexpected character after '\\': " + c);
        }
        else if (c == 'u')
        {
            return readUnicode();
        }
        else
        {
            return readEscaped(c);
        }
    }

    private char readUnicode() throws TokenException, EndOfStreamException, IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < 4; j ++)
        {
            char c = inputStream.read();
            if (isHex(c))
            {
                stringBuilder.append(c);
            }
            else
            {
                throw croak("Hex character expected but got: " + c);
            }
        }
        String unicode = stringBuilder.toString();
        int integer = Integer.parseInt(unicode, 16);
        return (char) integer;
    }

    private char readEscaped(char c)
    {
        switch (c)
        {
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
            default:
                return c;
        }
    }

    private boolean isHex(char c)
    {
        return (48 <= c && c <= 57) || (65 <= c && c <= 70) || (97 <= c && c <= 102);
    }

    private boolean isSpace(char c)
    {
        return SPACES.indexOf(c) > -1;
    }

    private boolean isLineSeparator(char c)
    {
        return LINE_SEPARATORS.indexOf(c) > -1;
    }

    private boolean isPunctuation(char c)
    {
        return PUNCTUATIONS.indexOf(c) > -1;
    }

    private boolean isControlCharacter(char c)
    {
        return c < 32 || c == 127;
    }

    private TokenException croak(String msg)
    {
        String errMsg = toErrMsg(msg);
        System.err.println(errMsg);
        return new TokenException(errMsg);
    }

    private String toErrMsg(String msg)
    {
        return msg + " at " + inputStream.getPosition();
    }
}
