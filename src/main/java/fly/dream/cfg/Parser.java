package fly.dream.cfg;

import java.io.BufferedReader;
import java.io.IOException;

class Parser
{
    private final TokenStream tokenStream;
    private final ConfigBuilder builder;

    /**
     * Since throwing an exception will halt the parsing, use it
     * to mark if there is any syntax error during the parsing.
     */
    private boolean hasError;

    Parser(BufferedReader reader)
    {
        FileInputStream fileInputStream = new FileInputStream(reader);
        this.tokenStream = new TokenStream(fileInputStream);
        this.builder = new ConfigBuilder();
        this.hasError = false;
    }

    Config parse() throws IOException, ConfigException
    {
        try
        {
            while (!tokenStream.endOfStream())
            {
                try
                {
                    handle(tokenStream.read());
                }
                catch (EndOfStreamException e)
                {
                    break;
                }
            }
            if (!hasError)
            {
                return builder.toConfig();
            }
            else
            {
                throw new ConfigSyntaxException();
            }
        }
        catch (TokenException | ConfigSyntaxException | UnexpectedEndOfStreamException e)
        {
            throw new ConfigException(e);
        }
    }


    // Helpers ---------------------------------------------------------------------------------------------------------

    private void handle(Token token)
    {
        switch (token.getType())
        {
            case KEY:
                handleKey(token);
                break;
            case STRING:
                handleString(token);
                break;
            case PUNCTUATION:
                handlePunctuation(token);
                break;
        }
    }

    private void handleKey(Token token)
    {
        String key = token.getString();
        switch (builder.getContext())
        {
            case MAP_LIST:
                croak("'{' or ']' expected but got: " + token);
                break;
            case LIST_LIST:
                croak("'[' or ']' expected but got: " + token);
                break;
            case ENTRY:
                croak("'STRING', '{' or '[' expected but got: " + token);
                break;
            case LIST:
                croak("'STRING', '{', '[' or ']' expected but got: " + token);
                break;
            case STRING_LIST:
                croak("'STRING' or ']' expected but got: " + token);
                break;
            case MAP:
            case ROOT:
                builder.pushEntry(key);
                break;
        }
    }

    private void handleString(Token token)
    {
        String str = token.getString();
        switch (builder.getContext())
        {
            case ENTRY:
                builder.finishEntry(str);
                break;
            case LIST:
            case STRING_LIST:
                addListElement(str, token);
                break;
            case MAP_LIST:
                croak("'{' or ']' expected but got: " + token);
                break;
            case LIST_LIST:
                croak("'[' or ']' expected but got: " + token);
                break;
            case MAP:
                croak("'KEY' or '}' expected but got: " + token);
                break;
            case ROOT:
                croak("'KEY' expected but got: " + token);
                break;
        }
    }

    private void handlePunctuation(Token token)
    {
        switch (token.getChar())
        {
            case '{':
                handleOpeningBrace(token);
                break;
            case '}':
                handleClosingBrace(token);
                break;
            case '[':
                handleOpeningBracket(token);
                break;
            case ']':
                handleClosingBracket(token);
                break;
        }
    }

    private void handleOpeningBrace(Token token)
    {
        switch (builder.getContext())
        {
            case ENTRY:
            case LIST:
            case MAP_LIST:
                builder.pushMap();
                break;
            case LIST_LIST:
                croak("'[' or ']' expected but got: " + token);
                break;
            case STRING_LIST:
                croak("'STRING' or ']' expected but got: " + token);
                break;
            case MAP:
                croak("'KEY' or '}' expected but got: " + token);
                break;
            case ROOT:
                croak("'KEY' expected but got: " + token);
                break;
        }
    }

    private void handleClosingBrace(Token token)
    {
        switch (builder.getContext())
        {
            case MAP:
                builder.finishMap();
                break;
            case ENTRY:
                croak("'STRING', '{' or '[' expected but got: " + token);
                break;
            case LIST:
                croak("'STRING', '{', '[' or ']' expected but got: " + token);
                break;
            case STRING_LIST:
                croak("'STRING' or ']' expected but got: " + token);
                break;
            case MAP_LIST:
                croak("'{' or ']' expected but got: " + token);
                break;
            case LIST_LIST:
                croak("'[' or ']' expected but got: " + token);
                break;
            case ROOT:
                croak("'KEY' expected but got: " + token);
                break;
        }
    }

    private void handleOpeningBracket(Token token)
    {
        switch (builder.getContext())
        {
            case ENTRY:
            case LIST:
            case LIST_LIST:
                builder.pushList();
                break;
            case STRING_LIST:
                croak("'STRING' or ']' expected but got: " + token);
                break;
            case MAP_LIST:
                croak("'{' or ']' expected but got: " + token);
                break;
            case MAP:
                croak("'KEY' or '}' expected but got: " + token);
                break;
            case ROOT:
                croak("'KEY' expected but got: " + token);
                break;
        }
    }

    private void handleClosingBracket(Token token)
    {
        switch (builder.getContext())
        {
            case LIST:
            case STRING_LIST:
            case MAP_LIST:
            case LIST_LIST:
                builder.finishList();
                break;
            case ENTRY:
                croak("'STRING', '{' or '[' expected but got: " + token);
                break;
            case MAP:
                croak("'KEY' or '}' expected but got: " + token);
                break;
            case ROOT:
                croak("'KEY' expected but got: " + token);
                break;
        }
    }

    private void addListElement(String str, Token token)
    {
        boolean success = builder.addElement(str);
        if (!success)
        {
            tip("Duplicated value: " + token);
        }
    }

    private void tip(String msg)
    {
        System.out.println(msg);
    }

    private void croak(String msg)
    {
        hasError = true;
        System.err.println(msg);
//        System.out.println("[ERROR] " + msg); // For debugging
    }
}
