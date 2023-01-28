package video;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Database implements Serializable {
    private SoundPlaylist sounds;
    private VideoPlaylist clips;

    private Database() {
        sounds = new SoundPlaylist();
        clips = new VideoPlaylist();
    }

    public static Database load() throws Exception {
        try {
            FileInputStream fileStream = new FileInputStream("databasev");
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            Database database = (Database) objectStream.readObject();
            objectStream.close();
            fileStream.close();
            return database;
        } catch (FileNotFoundException e) {
            return new Database();
        }
    }

    public void save() throws Exception {
        FileOutputStream fileStream = new FileOutputStream("databasev");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(this);
        objectStream.close();
        fileStream.close();
    }

    public SoundPlaylist getSounds() {
        return sounds;
    }
    public VideoPlaylist getVideo(){
        return clips;
    }
}
