package fly.dream.cfg;

public enum Context
{
    /**
     * Accepts 'KEY'
     */
    ROOT,

    /**
     * Accepts 'STRING' (END), '{' (OBJECT) and '[' (LIST)
     */
    ENTRY,

    /**
     * Accepts 'KEY' and '}' (END)
     */
    MAP,

    /**
     * Accepts 'STRING', '{' (OBJECT), '[' (LIST) and ']' (END)
     */
    LIST,

    /**
     * Accepts 'STRING' and ']' (END)
     */
    STRING_LIST,

    /**
     * Accepts '{' (OBJECT) and ']' (END)
     */
    MAP_LIST,

    /**
     * Accepts '[' (LIST) and ']' (END)
     */
    LIST_LIST
}
