package eu.franhakase.yada.kuromoji;

import android.content.Context;

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

    public static String TokenizeKaraoke(Context c, String entrada)
    {
        StringBuilder sb = new StringBuilder();
        List<Token> tokens = t.tokenize(entrada);
        KanaToRomaji kn = new KanaToRomaji();
        for(Token to: tokens)
        {
            String raw = to.getPronunciation();
            String read = to.getSurface();
            String Result = "";
            if(raw.equals("*") && !isHiragana(read))
            {
                Result = String.format("%s", (to.getSurface()));
                //sb.append(Result);

            }
            else if(isHiragana(read))
            {
                Result = String.format("%s", (KanaUtils.toKatakana(read)));
                //sb.append(Result);
            }
            else
            {
                Result = String.format("%s", raw);
                //sb.append(Result);
            }
            //sb.append(String.format("%s ", (raw.equals("*") || !isKatakana(raw)) ? to.getSurface() : raw));
            sb.append(String.format("\r\n%s [%s]\r\n", to.getSurface(), to.getReading()));
            sb.append(String.format("%s\r\n", kn.convert(c, Result)));
        }
        return sb.toString();
    }

    public static boolean isHiragana(String entrada)
    {
        return entrada.matches("[\\u3040-\\u309Fー]+");
    }
}
