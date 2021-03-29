package eu.franhakase.yada.kuromoji;

import java.util.HashMap;
import java.util.Map;
public class KanaToRomaji
{
    Map<String, String> m = new HashMap<String, String>();

    // Constructor
    public KanaToRomaji() {
        m.put("ア", "a");
        m.put("イ", "i");
        m.put("ウ", "u");
        m.put("エ", "e");
        m.put("オ", "o");
        m.put("カ", "ka");
        m.put("キ", "ki");
        m.put("ク", "ku");
        m.put("ケ", "ke");
        m.put("コ", "ko");
        m.put("サ", "sa");
        m.put("シ", "shi");
        m.put("ス", "su");
        m.put("セ", "se");
        m.put("ソ", "so");
        m.put("タ", "ta");
        m.put("チ", "chi");
        m.put("ツ", "tsu");
        m.put("テ", "te");
        m.put("ト", "to");
        m.put("ナ", "na");
        m.put("ニ", "ni");
        m.put("ヌ", "nu");
        m.put("ネ", "ne");
        m.put("ノ", "no");
        m.put("ハ", "ha");
        m.put("ヒ", "hi");
        m.put("フ", "fu");
        m.put("ヘ", "he");
        m.put("ホ", "ho");
        m.put("マ", "ma");
        m.put("ミ", "mi");
        m.put("ム", "mu");
        m.put("メ", "me");
        m.put("モ", "mo");
        m.put("ヤ", "ya");
        m.put("ユ", "yu");
        m.put("ヨ", "yo");
        m.put("ラ", "ra");
        m.put("リ", "ri");
        m.put("ル", "ru");
        m.put("レ", "re");
        m.put("ロ", "ro");
        m.put("ワ", "wa");
        m.put("ヲ", "wo");
        m.put("ン", "n");
        m.put("ガ", "ga");
        m.put("ギ", "gi");
        m.put("グ", "gu");
        m.put("ゲ", "ge");
        m.put("ゴ", "go");
        m.put("ザ", "za");
        m.put("ジ", "ji");
        m.put("ズ", "zu");
        m.put("ゼ", "ze");
        m.put("ゾ", "zo");
        m.put("ダ", "da");
        m.put("ヂ", "ji");
        m.put("ヅ", "zu");
        m.put("デ", "de");
        m.put("ド", "do");
        m.put("バ", "ba");
        m.put("ビ", "bi");
        m.put("ブ", "bu");
        m.put("ベ", "be");
        m.put("ボ", "bo");
        m.put("パ", "pa");
        m.put("ピ", "pi");
        m.put("プ", "pu");
        m.put("ペ", "pe");
        m.put("ポ", "po");
        m.put("キャ", "kya");
        m.put("キュ", "kyu");
        m.put("キョ", "kyo");
        m.put("シャ", "sha");
        m.put("シュ", "shu");
        m.put("ショ", "sho");
        m.put("チャ", "cha");
        m.put("チュ", "chu");
        m.put("チョ", "cho");
        m.put("ニャ", "nya");
        m.put("ニュ", "nyu");
        m.put("ニョ", "nyo");
        m.put("ヒャ", "hya");
        m.put("ヒュ", "hyu");
        m.put("ヒョ", "hyo");
        m.put("リャ", "rya");
        m.put("リュ", "ryu");
        m.put("リョ", "ryo");
        m.put("ギャ", "gya");
        m.put("ギュ", "gyu");
        m.put("ギョ", "gyo");
        m.put("ジャ", "ja");
        m.put("ジュ", "ju");
        m.put("ジョ", "jo");
        m.put("ヂャ", "dya");
        m.put("ヂュ", "dyu");
        m.put("ヂョ", "dyo");
        m.put("ビャ", "bya");
        m.put("ビュ", "byu");
        m.put("ビョ", "byo");
        m.put("ピャ", "pya");
        m.put("ピュ", "pyu");
        m.put("ピョ", "pyo");
        m.put("。", ".");

        //Casos especiais(?)
        m.put("ヴァ", "va");
        m.put("ファ", "fa");
        m.put("フィ", "fi");
        m.put("フェ", "fe");
        m.put("フォ", "fo");
        m.put("ウェ", "we");
        m.put("ウォ", "wo");
        m.put("ティ", "ti");
        m.put("ディ", "di");
        m.put("ツィ", "tsi");
        m.put("ァ", "a");
        m.put("ィ", "i");
        m.put("ゥ", "u");
        m.put("ェ", "e");
        m.put("ォ", "o");
        m.put("ャ", "ya");

        //ァィゥェォ

        //half-width
        m.put("ｱ", "a");
        m.put("ｲ", "i");
        m.put("ｳ", "u");
        m.put("ｴ", "e");
        m.put("ｵ", "o");
        m.put("ｶ", "ka");
        m.put("ｷ", "ki");
        m.put("ｸ", "ku");
        m.put("ｹ", "ke");
        m.put("ｺ", "ko");
        m.put("ｻ", "sa");
        m.put("ｼ", "shi");
        m.put("ｽ", "su");
        m.put("ｾ", "se");
        m.put("ｿ", "so");
        m.put("ﾀ", "ta");
        m.put("ﾁ", "chi");
        m.put("ﾂ", "tsu");
        m.put("ﾃ", "te");
        m.put("ﾄ", "to");
        m.put("ﾅ", "na");
        m.put("ﾆ", "ni");
        m.put("ﾇ", "nu");
        m.put("ﾈ", "ne");
        m.put("ﾉ", "no");
        m.put("ﾊ", "ha");
        m.put("ﾋ", "hi");
        m.put("ﾌ", "fu");
        m.put("ﾍ", "he");
        m.put("ﾎ", "ho");
        m.put("ﾏ", "ma");
        m.put("ﾐ", "mi");
        m.put("ﾑ", "mu");
        m.put("ﾒ", "me");
        m.put("ﾓ", "mo");
        m.put("ﾔ", "ya");
        m.put("ﾕ", "yu");
        m.put("ﾖ", "yo");
        m.put("ﾗ", "ra");
        m.put("ﾘ", "ri");
        m.put("ﾙ", "ru");
        m.put("ﾚ", "re");
        m.put("ﾛ", "ro");
        m.put("ﾜ", "wa");
        m.put("ｦ", "wo");
        m.put("ﾝ", "n");
        m.put("ｶﾞ", "ga");
        m.put("ｷﾞ", "gi");
        m.put("ｸﾞ", "gu");
        m.put("ｹﾞ", "ge");
        m.put("ｺﾞ", "go");
        m.put("ｻﾞ", "za");
        m.put("ｼﾞ", "ji");
        m.put("ｽﾞ", "zu");
        m.put("ｾﾞ", "ze");
        m.put("ｿﾞ", "zo");
        m.put("ﾀﾞ", "da");
        m.put("ﾁﾞ", "ji");
        m.put("ﾂﾞ", "zu");
        m.put("ﾃﾞ", "de");
        m.put("ﾄﾞ", "do");
        m.put("ﾊﾞ", "ba");
        m.put("ﾋﾞ", "bi");
        m.put("ﾌﾞ", "bu");
        m.put("ﾍﾞ", "be");
        m.put("ﾎﾞ", "bo");
        m.put("ﾊﾟ", "pa");
        m.put("ﾋﾟ", "pi");
        m.put("ﾌﾟ", "pu");
        m.put("ﾍﾟ", "pe");
        m.put("ﾎﾟ", "po");
        m.put("ｷｬ", "kya");
        m.put("ｷｭ", "kyu");
        m.put("ｷｮ", "kyo");
        m.put("ｼｬ", "sha");
        m.put("ｼｭ", "shu");
        m.put("ｼｮ", "sho");
        m.put("ﾁｬ", "cha");
        m.put("ﾁｭ", "chu");
        m.put("ﾁｮ", "cho");
        m.put("ﾆｬ", "nya");
        m.put("ﾆｭ", "nyu");
        m.put("ﾆｮ", "nyo");
        m.put("ﾋｬ", "hya");
        m.put("ﾋｭ", "hyu");
        m.put("ﾋｮ", "hyo");
        m.put("ﾘｬ", "rya");
        m.put("ﾘｭ", "ryu");
        m.put("ﾘｮ", "ryo");
        m.put("ｷﾞｬ", "gya");
        m.put("ｷﾞｭ", "gyu");
        m.put("ｷﾞｮ", "gyo");
        m.put("ｼﾞｬ", "ja");
        m.put("ｼﾞｭ", "ju");
        m.put("ｼﾞｮ", "jo");
        m.put("ﾁﾞｬ", "dya");
        m.put("ﾁﾞｭ", "dyu");
        m.put("ﾁﾞｮ", "dyo");
        m.put("ﾋﾞｬ", "bya");
        m.put("ﾋﾞｭ", "byu");
        m.put("ﾋﾞｮ", "byo");
        m.put("ﾋﾟｬ", "pya");
        m.put("ﾋﾟｭ", "pyu");
        m.put("ﾋﾟｮ", "pyo");

        //Casos especiais(?)
        m.put("ｳﾞｧ", "va");
        m.put("ﾌｧ", "fa");
        m.put("ﾌｨ", "fi");
        m.put("ﾌｪ", "fe");
        m.put("ﾌｫ", "fo");
        m.put("ｳｪ", "we");
        m.put("ｳｫ", "wo");
        m.put("ﾃｨ", "ti");
        m.put("ﾃﾞｨ", "di");
        m.put("ﾂｨ", "tsi");

        //pontuação
        m.put("｡", ".");
        m.put("！", "!");
        m.put("？", "?");
        m.put("､", ",");
        m.put("、", ",");
        m.put("･", " ");
        m.put("・", " ");
        m.put("　", " ");

        //arcaicos
        m.put("ヰ", "wi");
        m.put("ヱ", "we");



    }
    public String convert(String s)
    {
        //hack feioso para corrigir a mania que o Kuromoji tem de separar o ッ(tsu pequeno) da sílaba seguinte...
        s = s.replace("ッ ", "ッ");
        StringBuilder t = new StringBuilder();
        for ( int i = 0; i < s.length(); i++ )
        {
            if ( i <= s.length() - 2 )
            {
                if ( m.containsKey(s.substring(i,i+2)))
                {
                    t.append(m.get(s.substring(i, i+2)));
                    i++;
                }
                else if (m.containsKey(s.substring(i, i+1)))
                {
                    t.append(m.get(s.substring(i, i+1)));
                }
                else if ( s.charAt(i) == 'ッ'  && KanaUtils.isKatakana(s.charAt(i+1)))
                {

                    t.append(m.get(s.substring(i+1, i+2)).charAt(0));
                }
                else if(s.charAt(i) == 'ッ' && !KanaUtils.isKatakana(s.charAt(i+1)))
                {
                    t.append("tsu");
                }
                else if(s.charAt(i) == 'ー' && KanaUtils.isKatakana(s.charAt(i-1)) || s.charAt(i) == 'ｰ' && KanaUtils.isKatakana(s.charAt(i-1)))
                {
                    t.append(t.charAt(t.length()-1));
                    //ー
                }
                else
                {
                    t.append(s.charAt(i));
                }
            }
            else
                {
                if (m.containsKey(s.substring(i, i+1)))
                {
                    t.append(m.get(s.substring(i, i+1)));
                } else
                    {
                    t.append(s.charAt(i));
                }
            }
        }
        return t.toString().replaceAll("[ ]{2,}", " ");
    }

}