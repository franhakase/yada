package eu.franhakase.yada.kuromoji;

public class KanaUtils
{

    /**
     * Determines if this character is one of the Japanese Hiragana.
     */
    public static boolean isHiragana(char c)
    {
        return (('\u3041' <= c) && (c <= '\u309e'));
    }

    /**
     * Determines if this character is a Half width Katakana.
     */

    public static String toKatakana(String entrada)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < entrada.length(); i++)
        {
            if(isHiragana(entrada.charAt(i)))
            {

                sb.append((char) (entrada.charAt(i) + 0x60));

            }
        }
        return sb.toString();
    }

    public static boolean isKatakana(char c) {
        return (isHalfWidthKatakana(c) || isFullWidthKatakana(c));
    }

    /**
     * Determines if this character is a Half width Katakana.
     */
    public static boolean isHalfWidthKatakana(char c) {
        return (('\uff66' <= c) && (c <= '\uff9d'));
    }

    /**
     * Determines if this character is a Full width Katakana.
     */
    public static boolean isFullWidthKatakana(char c) {
        return (('\u30a1' <= c) && (c <= '\u30fe'));
    }


}
