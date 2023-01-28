package video;
import java.io.Serializable;
import java.util.ArrayList;

public class VideoPlaylist implements Serializable {
    private ArrayList<String> clips;

    public VideoPlaylist() {
        clips = new ArrayList<String>();
    }

  
    public void add(String path){
        clips.add(path);
    }

    public void remove(String path) {
        clips.remove(path);
    }

    public ArrayList<String> list() {
        return new ArrayList<String>(clips);
    }

    public ArrayList<String> list(String keyword) {
        ArrayList<String> result = new ArrayList<String>();

        for (String clip : clips)
            if (clip.contains(keyword))
                result.add(clip);
        return result;
    }
}
