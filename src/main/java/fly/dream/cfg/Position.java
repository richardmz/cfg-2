package fly.dream.cfg;

public class Position
{
    private int line;
    private int column;

    public Position()
    {
        this.line = 1;
        this.column = 0;
    }

    /**
     * Also sets column to 0
     */
    public void incLine()
    {
        line ++;
        column = 0;
    }

    public void incColumn()
    {
        column ++;
    }

    public int getLine()
    {
        return line;
    }

    public int getColumn()
    {
        return column;
    }

    /**
     * Column zero means '\n' (LF, Line Feed) of the (line - 1)th line.
     */
    @Override
    public String toString()
    {
        return "(line " + line + ",  column " + column + ")";
    }
}
