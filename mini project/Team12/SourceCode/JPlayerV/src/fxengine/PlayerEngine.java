package fxengine;


import java.io.File;

import javax.swing.JFrame;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.media.Track;
import javafx.stage.Stage;
import javafx.embed.swing.JFXPanel;;
/**
 * A sample media player which loops indefinitely over the same video
 */
public class PlayerEngine extends Application {
private static final String MEDIA_URL = new File(System.getProperty("user.dir")+"/Video.mp4").toURI().toString();//System.getProperty("user.dir")+"/Video.mp4";
private static String arg1;

private static JFrame frame;

    @Override public void start(Stage stage) {
        stage.setTitle("Media Player");

// Create media player
        Media media = new Media((arg1 != null) ? arg1 : MEDIA_URL);
        javafx.scene.media.MediaPlayer mediaPlayer = new javafx.scene.media.MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(javafx.scene.media.MediaPlayer.INDEFINITE);

// Print track and metadata information
        media.getTracks().addListener(new ListChangeListener<Track>() {
public void onChanged(Change<? extends Track> change) {
                System.out.println("Track> "+change.getList());
            }
        });
        media.getMetadata().addListener(new MapChangeListener<String,Object>() {
public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> change) {
                System.out.println("Metadata> "+change.getKey()+" -> "+change.getValueAdded());
            }
        });

// Add media display node to the scene graph
        MediaView mediaView = new MediaView(mediaPlayer);
        Group root = new Group();
        Scene scene = new Scene(root,800,600);
        root.getChildren().add(mediaView);
        //frame.getContentPane().add(mediaView);
        stage.setScene(scene);
        stage.show();
        JFXPanel fxpanel = new JFXPanel();
        fxpanel.setScene(scene);
        frame.getContentPane().add(fxpanel);
        System.out.println("in scene");
    }
/*
public static void main(String[] args) {
	System.out.println(System.getProperty("user.dir"));
if (args.length > 0) {
            arg1 = args[0];
        }
        Application.launch(args);
    }
*/
    /*
public PlayerEngine(String filename, JFrame f) {
	if(!filename.isEmpty()) {
		frame = f;
		arg1 = new File(System.getProperty("user.dir")+"/"+filename).toURI().toString();
		System.out.println("launching:" +arg1);
		Application.launch(filename);
	}
	else
	{
		System.out.println("specify a file name please");
	}
}
*/
}