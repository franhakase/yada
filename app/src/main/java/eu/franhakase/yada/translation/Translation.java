package eu.franhakase.yada.translation;

import java.util.List;

public class Translation
{
    public List<Beam> getBeams() {
        return beams;
    }

    public void setBeams(List<Beam> beams) {
        this.beams = beams;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public List<Beam> beams;
    public String quality;
}
