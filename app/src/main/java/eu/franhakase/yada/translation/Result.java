package eu.franhakase.yada.translation;

import java.util.List;

public class Result
{
    public List<Translation> translations;
    public String target_lang;
    public String source_lang;
    public boolean source_lang_is_confident;
    public DetectedLanguages detectedLanguages;
    public int timestamp;
    public String date;
}
