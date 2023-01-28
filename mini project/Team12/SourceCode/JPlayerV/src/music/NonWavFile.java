package music;
/*
import java.io.ObjectInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class NonWavFile extends SoundFile {
    public NonWavFile(String path) {
        super(path);
    }

    private void readObject(ObjectInputStream in) throws Exception {
        in.defaultReadObject();
        setClip();
    }

    private void setClip() throws Exception {
    }
}
*/

import java.io.File;
import java.io.ObjectInputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class NonWavFile extends SoundFile {
    /**
	 * 
	 */
	private static final long serialVersionUID = 165359654963778137L;
	public NonWavFile(String path) throws Exception {
        super(path);
        setClip();
    }

    private void readObject(ObjectInputStream in) throws Exception {
        in.defaultReadObject();
        setClip();
    }

    private void setClip() throws Exception {
        AudioInputStream stream = AudioSystem.getAudioInputStream(new File(path));
        clip = AudioSystem.getClip();
        clip.open(stream);
    }

    public int getMaxLevel() {
        return 0;
    }

    public int getMinLevel() {
        return 0;
    }
    public int getLevel() {
        return 0;
    }
}
