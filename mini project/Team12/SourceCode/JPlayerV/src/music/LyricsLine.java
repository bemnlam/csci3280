package music;
public class LyricsLine {
    private long length;
    private String text;

    public LyricsLine(long length, String text) {
        this.length = length;
        this.text = text;
    }

    public long getMicrosecondLength() {
        return length;
    }

    public String getText() {
        return text;
    }
}
