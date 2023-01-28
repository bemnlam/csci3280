package fxengine;

import java.awt.EventQueue;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.BoxLayout;

import net.miginfocom.swing.MigLayout;

public class PlayerView{

	private JFrame frame;
	private JTextField txtFileName;
	MediaPlayer mediaPlayer;
	private JPanel panelVideoContainer;
	private JPanel panelControls;
	private JButton btnPlay;

	private static String dest = "";
	private JButton btnExit;
	private boolean isPause = false; 	
	/**
	 * Launch the application.
	 */
	public static void launchFX(String args) {
		dest = args;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
					PlayerView window = new PlayerView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PlayerView() {
		initialize();
		Platform.setImplicitExit(false);
	}

	/*listeners*/
	
	
	private void initJXPanel(JFXPanel panel, String path, JFrame frame) {
	    	final String MEDIA_URL = "http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv";
	        Group root = new Group();
	        //Scene scene = new Scene(root, 540, 210);
	         
	        if(!path.isEmpty()) {
	        	//path = new File(System.getProperty("user.dir")+"/"+path).toURI().toString();
	        } else {
	        	path = MEDIA_URL;
	        }
	        // create media player
			System.out.println("byron");
	        Media media = new Media(path);
	        if(mediaPlayer!=null) {
	        	mediaPlayer.stop();
	        }
	        mediaPlayer = new MediaPlayer(media);
	        MediaView mediaView = new MediaView(mediaPlayer);
	        System.out.println(media.getSource());
	        Scene scene = new Scene(root, 800, 500);
	        mediaPlayer.setAutoPlay(true);
	        
	        ((Group)scene.getRoot()).getChildren().add(mediaView);
			panel.setScene(scene);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	
	/*addWindowListener(new WindowAdapter(){
		public void windowClosing(WindowEvent evt){
			dispose();
		}
	});*/
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 510, 480);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent evt){
				//dispose();
				System.out.println("dispose");
				mediaPlayer.stop();
			}
		});
		
		JPanel panel = new JPanel();
		panel.setBounds(19, 33, 449, 272);
		frame.getContentPane().add(panel);
						panel.setLayout(new BorderLayout(0, 0));
						
						panelControls = new JPanel();
						panel.add(panelControls, BorderLayout.NORTH);
						
		btnPlay = new JButton("pause");
		panelControls.add(btnPlay);
		
				
				txtFileName = new JTextField(dest);
				panelControls.add(txtFileName);
				txtFileName.setColumns(10);
				
				btnExit = new JButton("exit");
				
				btnExit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String cmd = e.getActionCommand();
						if(cmd == "exit")
						{
							System.out.println("exit PlayView");
							new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
							try{
								//db.save();
							} catch(Exception eSave) {
								// do nothing
							}
							mediaPlayer.stop();
							frame.dispose();
							
						}
					}
				});
				panelControls.add(btnExit);
				
				panelVideoContainer = new JPanel();
				panelVideoContainer.setBackground(Color.GRAY);
				panel.add(panelVideoContainer, BorderLayout.CENTER);
				panelVideoContainer.setLayout(new BoxLayout(panelVideoContainer, BoxLayout.X_AXIS));
		
				final JFXPanel jFXPanel = new JFXPanel();
				jFXPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
				jFXPanel.setLocation(65, 175);
				jFXPanel.setSize(500, 400);
				//frame.getContentPane().setLayout(null);
				panelVideoContainer.add(jFXPanel, BorderLayout.CENTER);
				
			
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isPause){
					mediaPlayer.play();
					btnPlay.setText("pause");
				}else{
					mediaPlayer.pause();
					btnPlay.setText("resume");
				}
				isPause = !isPause;
			}	
		});
		
		final String path = txtFileName.getText();
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
            	initJXPanel(jFXPanel, path, frame);
            }
        });			
        
	/*	
	Platform.runLater(new Runnable(){
		@Override
		public void run() {
		Group root = new Group();
		Scene scene = new Scene(root, 600, 500);

		Media media = new Media(path);
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setAutoPlay(true);

		MediaView mediaView = new MediaView(mediaPlayer);
		((Group)scene.getRoot()).getChildren().add(mediaView);

		jFXPanel.setScene(scene);
		}
		});*/
}

}
