package eu.franhakase.yada.kuromoji;

import com.atilika.kuromoji.TokenizerBase;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import java.util.List;

public class Kuromoji
{
    private static Tokenizer t;

    public static void setup()
    {
        t = new Tokenizer.Builder().mode(TokenizerBase.Mode.NORMAL).build();
    }

    public static String Tokenize(String entrada)
    {
        StringBuilder sb = new StringBuilder();
        List<Token> tokens = t.tokenize(entrada);
        for(Token to: tokens)
        {
            String raw = to.getPronunciation();
            String read = to.getSurface();
            if(raw.equals("*") && !isHiragana(read))
            {
                sb.append(String.format("%s ", (to.getSurface())));
            }
            else if(isHiragana(read))
            {
                sb.append(String.format("%s ", (KanaUtils.toKatakana(read))));
            }
            else
            {
                sb.append(String.format("%s ", raw));
            }
            //sb.append(String.format("%s ", (raw.equals("*") || !isKatakana(raw)) ? to.getSurface() : raw));
        }
        return sb.toString();
    }

    public static boolean isHiragana(String entrada)
    {
        return entrada.matches("[\\u3040-\\u309Fー]+");
    }
}
