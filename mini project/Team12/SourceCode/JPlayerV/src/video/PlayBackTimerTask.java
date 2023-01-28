package video;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JSlider;


public class PlayBackTimerTask extends TimerTask{
	private static SoundFile s;
	private static JSlider bar;
	private static JLabel l;
	private static JLabel songInfo;
	private static ArrayList<LyricsLine> lyrics;
	private static boolean isManualAdjust = false;
	
	private static boolean isLyricsExist = false;
	private static boolean isRefreshed = false;
	
	public PlayBackTimerTask(SoundFile sound, JSlider slider, JLabel label, JLabel lblSongInfo) {
		s = sound;
		bar = slider;
		l = label;
		l.setText("");
		songInfo = lblSongInfo;
		try {
			lyrics = sound.getLyricsLines();
			isRefreshed = true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			isLyricsExist = false;
			isRefreshed = false;
			
			System.out.println("Downloading lyrics...");

			//Runnable r = new ThreadFindSong(s);
			//new Thread(r).start();
			/*
			Thread one = new Thread() {
			    public void run() {
			        try {
			            System.out.println("Does it work?");

			            Thread.sleep(10000);

			            System.out.println("Nope, it doesnt...again.");
			        } catch(InterruptedException v) {
			            System.out.println(v);
			        }
			    }  
			};

			one.start();
			*/
			new ThreadFindSong(sound).start();

		}
		isManualAdjust = false;
	}

	
	@Override
	public void run() {
		if(!isManualAdjust) {
			float progress = PlayBackController.getPlayBackProgress(s, bar); 
			//((float)s.getMicrosecondPosition() / (float)s.getMicrosecondLength() ) * 100000;
			bar.setValue(Math.round(progress));
			
			float amp = 100*((float)(s.getLevel()-s.getMinLevel())/(float)(s.getMaxLevel()-s.getMinLevel()));
			HelloWorldBuild.progressBar.setValue((int)amp);
			//System.out.println(amp);
			
			try {
				//File f = new File("Sources/4.lrc");
				//if(f.exists()) { System.out.println("exist"); };
				if(!isLyricsExist) {
					lyrics = s.getLyricsLines();
					
					ArrayList<String> soundInfo = s.getSoundInfoFromLyric();
					if(!soundInfo.isEmpty())
					{
						if(s.getTitle().isEmpty()) {
							s.setTitle(soundInfo.get(0));
						}
						if(s.getAlbum().isEmpty()) {
							s.setAlbum(soundInfo.get(1));
						}
					}
					songInfo.setText(SongInfoController.getSongInfo(PlayBackController.getSound()));
				}
				isLyricsExist = true;
				if(!isRefreshed) {
					SongListController.refreshSongList();
					isRefreshed = true;
				}
				
				String currText = l.getText();
				String newText = LyricsController.getCurrentLyrics(lyrics, s.getMicrosecondPosition()); 
				if(!currText.equals(newText)) {
					l.setText(newText);
					System.out.println("lyrics updated");
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("get lyrics fail: " + s.getFilename());
				isLyricsExist = false;
			}
			
			
			/*
			if(lyrics.size() > 0) {
				
				
			}
			*/
			
		}
	}
	//skip auto setValue() to avoid conflict from Mouse drag input
	public static void pause() {
		//System.out.println("pause");
		isManualAdjust = true;
	}
	public static void resume() {
		//System.out.println("resume");
		isManualAdjust = false;
	}
}
