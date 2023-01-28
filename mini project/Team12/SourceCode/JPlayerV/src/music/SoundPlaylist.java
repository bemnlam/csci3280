package music;
import java.io.Serializable;
import java.util.ArrayList;

public class SoundPlaylist implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 660343598550306423L;
	private ArrayList<SoundFile> sounds;

    public SoundPlaylist() {
        sounds = new ArrayList<SoundFile>();
    }

    public void add(SoundFile sound) {
        sounds.add(sound);
    }

    public void remove(SoundFile sound) {
        sounds.remove(sound);
    }

    public ArrayList<SoundFile> list() {
        return new ArrayList<SoundFile>(sounds);
    }

    public ArrayList<SoundFile> list(String keyword) {
        ArrayList<SoundFile> result = new ArrayList<SoundFile>();

        for (SoundFile sound : sounds)
            if (sound.match(keyword))
                result.add(sound);

        return result;
    }
}
