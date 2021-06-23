package fly.dream.cfg;

import org.jetbrains.annotations.NotNull;

import static fly.dream.cfg.TokenType.*;

class Token
{
    private final @NotNull TokenType type;
    private final char character;
    private final String string;
    private final Position position;

    Token(char character, Position position)
    {
        this.type = PUNCTUATION;
        this.character = character;
        this.string = null;
        this.position = position;
    }

    Token(@NotNull TokenType type, String string, Position position)
    {
        this.type = type;
        this.string = string;
        this.character = '\u0000';
        this.position = position;
    }

    @NotNull TokenType getType()
    {
        return type;
    }

    String getString()
    {
        assert type == STRING || type == KEY : "Should not get char from " + type;
        return string;
    }

    char getChar()
    {
        assert type == PUNCTUATION : "Should not get char from " + type;
        return character;
    }

    @Override
    public String toString()
    {
        switch (type)
        {
            case KEY:
                return "\"" + string + "\" (KEY) at " + position;
            case STRING:
                return "\"" + string + "\" at " + position;
            default: // PUNCTUATION
                return "'" + character + "' at " + position;
        }
    }
}
