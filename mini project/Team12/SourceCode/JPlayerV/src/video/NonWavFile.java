package video;
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
import javax.sound.sampled.Clip;

public class NonWavFile extends SoundFile {
    public NonWavFile(String path) throws Exception {
        super(path);
                System.out.println("trying");
        setClip();
    }

    private void readObject(ObjectInputStream in) throws Exception {
        in.defaultReadObject();
        setClip();
    }

    private void setClip() throws Exception {
//        AudioInputStream stream = AudioSystem.getAudioInputStream(new File(path));
//        clip = AudioSystem.getClip();
//        clip.open(stream);
        System.out.println("setting video");
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
