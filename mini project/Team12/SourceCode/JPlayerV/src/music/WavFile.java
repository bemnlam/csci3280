package music;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class WavFile extends SoundFile {
    /**
	 * 
	 */
	private static final long serialVersionUID = -185514570373676206L;
	private transient byte[] data;
    private transient int sampleSizeInBits;

    public WavFile(String path) throws Exception {
        super(path);
        setClip();
    }

    private void readObject(ObjectInputStream in) throws Exception {
        in.defaultReadObject();
        setClip();
    }

    private void setClip() throws Exception {
        AudioFormat format = null;

        BinaryFile file = new BinaryFile(path);
        file.skip(12);

        while (format == null || data == null) {
            String tag = file.readString(4);
            int size = file.readInt32();

            if (tag.equals("fmt ")) {
                file.skip(2);
                int channels = file.readInt16();
                int sampleRate = file.readInt32();
                file.skip(6);
                sampleSizeInBits = file.readInt16();
                format = new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
                file.skip(size - 16);
            } else if (tag.equals("data")) {
                data = file.readBytes(size);
            } else {
                file.skip(size);
            }
        }

        file.close();
        int frameCount = data.length / 4;
        ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
        AudioInputStream inputStream = new AudioInputStream(dataStream, format, frameCount);
        clip = AudioSystem.getClip();
        clip.open(inputStream);
    }

    public int getMaxLevel() {
        int sampleSize = sampleSizeInBits / 8;

        switch (sampleSize) {
            case 1: return Byte.MAX_VALUE;
            case 2: return Short.MAX_VALUE;
            case 4: return Integer.MAX_VALUE;
            default: return 0;
        }
    }

    public int getMinLevel() {
        int sampleSize = sampleSizeInBits / 8;

        switch (sampleSize) {
            case 1: return Byte.MIN_VALUE;
            case 2: return Short.MIN_VALUE;
            case 4: return Integer.MIN_VALUE;
            default: return 0;
        }
    }

    public int getLevel() {
        int pos = clip.getFramePosition();
        int sampleSize = sampleSizeInBits / 8;
        ByteBuffer buffer = ByteBuffer.allocate(sampleSize);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < sampleSize; i++)
            buffer.put(data[i + pos]);

        switch (sampleSize) {
            case 1: return buffer.get(0);
            case 2: return buffer.getShort(0);
            case 4: return buffer.getInt(0);
            default: return 0;
        }
    }
}


/*import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class WavFile extends SoundFile {
    public WavFile(String path) throws Exception {
        super(path);
        setClip();
    }

    private void readObject(ObjectInputStream in) throws Exception {
        in.defaultReadObject();
        setClip();
    }

    private void setClip() throws Exception {
        AudioFormat format = null;
        byte[] data = null;

        BinaryFile file = new BinaryFile(path);
        file.skip(12);

        while (format == null || data == null) {
            String tag = file.readString(4);
            int size = file.readInt32();

            if (tag.equals("fmt ")) {
                file.skip(2);
                int channels = file.readInt16();
                int sampleRate = file.readInt32();
                file.skip(6);
                int sampleSizeInBits = file.readInt16();
                format = new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
                file.skip(size - 16);
            } else if (tag.equals("data")) {
                data = file.readBytes(size);
            } else {
                file.skip(size);
            }
        }

        file.close();
        int frameCount = data.length / 4;
        ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
        AudioInputStream inputStream = new AudioInputStream(dataStream, format, frameCount);
        clip = AudioSystem.getClip();
        clip.open(inputStream);
    }
}
*/