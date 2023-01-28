package music;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Database implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8190668702974064176L;
	private SoundPlaylist sounds;

    private Database() {
        sounds = new SoundPlaylist();
    }

    public static Database load() throws Exception {
        try {
            FileInputStream fileStream = new FileInputStream("database");
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
        FileOutputStream fileStream = new FileOutputStream("database");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(this);
        objectStream.close();
        fileStream.close();
    }

    public SoundPlaylist getSounds() {
        return sounds;
    }
}
