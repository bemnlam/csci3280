package video;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTable;

public class PlayBackController {
	private static boolean isPlaying, isPaused;
	private static String songpath;
	private static SoundFile sound;
	
	private static Timer timerPlay;
	
	public PlayBackController() {
		isPlaying = false;
		isPaused = false;
	}
	
	public static void playClicked(	JTable table, 
									JSlider sliderVol, 
									JSlider sliderPlayBack, 
									JLabel lblLyrics,
									JLabel lblSongInfo,
									JButton btnPlay, JButton btnPause, JButton btnStop, 
									SoundPlaylist playlist){

		btnPlay.setVisible(false);
		btnPause.setVisible(true);
		btnStop.setVisible(true);
		
		try {
			// table:get selected item
			if(isPaused) {
				VolumnController.adjustVolumn(sound, sliderVol.getValue());
				sound.resume();
				PlayBackTimerTask.resume();
				System.out.println("resume");
				
			} else {

				int songIndex = SongListController.getSelectedItemIndex(table);
				if(songIndex==-1) {
					// make default selection
					if(table.getRowCount()>0){
						songIndex = 0;
					} else {
						throw new FileNotFoundException();
					}
				}
				table.setRowSelectionInterval(songIndex, songIndex);
				System.out.println("name= "+playlist.list().get(songIndex).getAbsoluteFilename());
				//sound = SoundFile.create(playlist.list().get(songIndex).getAbsoluteFilename());
				sound = playlist.list().get(songIndex);
				
				VolumnController.adjustVolumn(sound, sliderVol.getValue());
				
				timerPlay = new Timer();
				PlayBackTimerTask ttPlay = new PlayBackTimerTask(sound, sliderPlayBack, lblLyrics, lblSongInfo);
				timerPlay.schedule(ttPlay, 0, 100);

				//SongInfoController.getLyrics(sound);
				sound.play();
				
				try {
					ArrayList<String> soundInfo = sound.getSoundInfoFromLyric();
					if(!soundInfo.isEmpty())
					{
						if(sound.getTitle().isEmpty()) {
							sound.setTitle(soundInfo.get(0));
						}
						if(sound.getAlbum().isEmpty()) {
							sound.setAlbum(soundInfo.get(1));
						}
						int curr = table.getSelectedRow();
						SongListController.initSongList(table, playlist);
						table.setRowSelectionInterval(curr, curr);
						System.out.println("info auto-loaded");
					}
				}catch (Exception e) {
					System.out.println("lrc file not exist");
				}
				System.out.println("play");
				lblSongInfo.setText(SongInfoController.getSongInfo(PlayBackController.getSound()));
				//LyricsTimerTask ttLyrics = new LyricsTimerTask(lblLyrics, sound.getLyricsLines()); 
				//ttLyrics.run();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			if(e1 instanceof FileNotFoundException) {
				System.out.println("file not found");
			}
			else {
				e1.printStackTrace();
				System.out.println("err on play");
			}
		}
		isPlaying = true;
		isPaused = false;
	}
	
	public static void pauseClicked(JButton btnPlay, JButton btnPause, JButton btnStop) {
		btnPlay.setVisible(true);
		btnPause.setVisible(false);
		btnStop.setVisible(true);
		
		try {
			sound.pause();
			PlayBackTimerTask.pause();
		} catch(Exception e2) {
			System.out.println("err on pause");
		}
		isPaused = true;
		
	}
	
	public static void stopClicked(JButton btnPlay, JSlider sliderPlayBack, JButton btnPause, JButton btnStop)
	{
		isPlaying = false;
		isPaused = false;
		
		btnPlay.setVisible(true);
		btnPause.setVisible(false);
		btnStop.setVisible(false);
		
		try{
			sound.stop();
			sliderPlayBack.setValue(0);
			
			timerPlay.cancel();
			LyricsTimerTask.cancelTask();
			
			
		} catch(Exception e3) {
			e3.getStackTrace();
			//System.out.println("err on stop");
		}
	}
	
	public static SoundFile getSound()
	{
		return sound;
	}

	public static float getPlayBackProgress(SoundFile s, JSlider slider){
		return ((float)s.getMicrosecondPosition() / (float)s.getMicrosecondLength() * slider.getMaximum());
	}

}
