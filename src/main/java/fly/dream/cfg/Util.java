package fly.dream.cfg;

public class Util
{
    public static String getIndent(int lvl)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < lvl; i ++)
        {
            stringBuilder.append("  ");
        }
        return stringBuilder.toString();
    }

    public static String toHex(char c)
    {
        String hex = String.format("%02x", (int) c);
        return "0x" + hex;
    }
}
