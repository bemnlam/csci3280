package music;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;

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
									JLabel lblAlbumArt,
									JLabel lblSongInfo,
									JButton btnPlay, JButton btnPause, JButton btnStop,
									JTextField txtSearch,
									SoundPlaylist playlist
									){

		btnPlay.setVisible(false);
		btnPause.setVisible(true);
		//btnStop.setVisible(true);
		btnStop.setEnabled(true);
		
		try {
			// table:get selected item
			if(isPaused) {
				SoundEffectController.adjustVolumn(sound, sliderVol.getValue());
				sound.resume();
				PlayBackTimerTask.resume();
				//System.out.println("resume");
				
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
				System.out.println("file name = "+playlist.list(txtSearch.getText()).get(songIndex).getAbsoluteFilename());
				//sound = SoundFile.create(playlist.list().get(songIndex).getAbsoluteFilename());
				sound = playlist.list(txtSearch.getText()).get(songIndex);
				
				SoundEffectController.adjustVolumn(sound, sliderVol.getValue());
				
				timerPlay = new Timer();
				//SongInfoController.getLyrics(sound);
				sound.play();
				PlayBackTimerTask ttPlay = new PlayBackTimerTask(sound, sliderPlayBack, lblLyrics, lblAlbumArt, lblSongInfo);
				//lblAlbumArt.setText("grab");
				timerPlay.schedule(ttPlay, 0, 100);
				
				
				try {
					ArrayList<String> soundInfo = sound.getSoundInfoFromLyric();
					if(!soundInfo.isEmpty())
					{
						if(sound.getTitle().isEmpty()) {
							sound.setTitle(soundInfo.get(0));
						}
						
						if(sound.getSinger().isEmpty()) {
							sound.setSinger(soundInfo.get(1));
						}
						if(sound.getAlbum().isEmpty()) {
							sound.setAlbum(soundInfo.get(2));
						}
						int curr = table.getSelectedRow();
						SongListController.initSongList(table, playlist, txtSearch);
						table.setRowSelectionInterval(curr, curr);
						//System.out.println("info auto-loaded");
					}
					// force to find album art again
					//ThreadFindSong tfs = new ThreadFindSong(sound, lblAlbumArt, false);
					//tfs.run();
					
				}catch (Exception e) {
					System.out.println("PlayBackController: no .lrc");
				}
				
				
				//System.out.println("play");
				lblSongInfo.setText(SongInfoController.getSongInfo(PlayBackController.getSound()));
				//LyricsTimerTask ttLyrics = new LyricsTimerTask(lblLyrics, sound.getLyricsLines()); 
				//ttLyrics.run();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			if(e1 instanceof FileNotFoundException) {
				System.out.println("PlayBackController: FileNotFoundException");
			}
			else {
				e1.printStackTrace();
				System.out.println("PlayBackController: unknown Exception");
			}
		}
		isPlaying = true;
		isPaused = false;
	}
	
	public static void pauseClicked(JButton btnPlay, JButton btnPause, JButton btnStop) {
		btnPlay.setVisible(true);
		btnPause.setVisible(false);
		//btnStop.setVisible(true);
		btnStop.setEnabled(true);
		
		try {
			sound.pause();
			PlayBackTimerTask.pause();
		} catch(Exception e2) {
			System.out.println("PlayBackController: can't pause");
		}
		isPaused = true;
		
	}
	
	public static void stopClicked(JButton btnPlay, JSlider sliderPlayBack, JButton btnPause, JButton btnStop)
	{
		isPlaying = false;
		isPaused = false;
		
		btnPlay.setVisible(true);
		btnPause.setVisible(false);
		//btnStop.setVisible(false);
		btnStop.setEnabled(false);
		
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
		return (float) Math.ceil((float)s.getMicrosecondPosition() / (float)s.getMicrosecondLength() * (float)slider.getMaximum());
	}
	
	public static void playNextSong(JButton btnPlay,
									JButton btnPause,
									JButton btnStop,
									JLabel lblSongInfo,
									JLabel lblLyrics,
									JLabel lblAlbumArt,
									JSlider sliderPlayBack,
									JSlider sliderVol,
									JTextField txtSearch,
									JTable tableSongList,
									SoundPlaylist playlist
									) {
		int indicator = 1;
		playSong(btnPlay, btnPause, btnStop, lblSongInfo, lblLyrics,
		lblAlbumArt, sliderPlayBack, sliderVol, tableSongList, txtSearch, playlist, playlist.list().indexOf(sound)+indicator);
	}
	
	public static void playPrevSong(JButton btnPlay,
									JButton btnPause,
									JButton btnStop,
									JLabel lblSongInfo,
									JLabel lblLyrics,
									JLabel lblAlbumArt,
									JSlider sliderPlayBack,
									JSlider sliderVol,
									JTextField txtSearch,
									JTable tableSongList,
									SoundPlaylist playlist
									) {
		int indicator = -1;
		playSong(btnPlay, btnPause, btnStop, lblSongInfo, lblLyrics,
		lblAlbumArt, sliderPlayBack, sliderVol, tableSongList, txtSearch, playlist, playlist.list().indexOf(sound)+indicator);
	}
	
	public static void playSelectedSong(JButton btnPlay,
			JButton btnPause,
			JButton btnStop,
			JLabel lblSongInfo,
			JLabel lblLyrics,
			JLabel lblAlbumArt,
			JSlider sliderPlayBack,
			JSlider sliderVol,
			JTextField txtSearch,
			JTable tableSongList,
			SoundPlaylist playlist
			) {
		int indicator = tableSongList.getSelectedRow();
		playSong(btnPlay, btnPause, btnStop, lblSongInfo, lblLyrics,
				lblAlbumArt, sliderPlayBack, sliderVol, tableSongList, txtSearch, playlist, indicator);
	}
	
	public static void playSong(JButton btnPlay,
								JButton btnPause,
								JButton btnStop,
								JLabel lblSongInfo,
								JLabel lblLyrics,
								JLabel lblAlbumArt,
								JSlider sliderPlayBack,
								JSlider sliderVol,
								JTable tableSongList,
								JTextField txtSearch,
								SoundPlaylist playlist,
								int indicator
								) {
	PlayBackTimerTask.pause();
	
	PlayBackController.stopClicked(btnPlay, sliderPlayBack, btnPause, btnStop);
	//System.out.println("song Finished");
	int nextIndex = indicator; //SongListController.getSelectedItemIndex(tableSongList)+indicator;
	if(nextIndex < tableSongList.getRowCount() && nextIndex>=0) {
		tableSongList.setRowSelectionInterval(nextIndex, nextIndex);
		PlayBackController.playClicked(tableSongList, sliderVol, sliderPlayBack, lblLyrics, lblAlbumArt, lblSongInfo, btnPlay, btnPause, btnStop, txtSearch, playlist);
		sliderPlayBack.setValue(0);
		lblSongInfo.setText(SongInfoController.getSongInfo(PlayBackController.getSound()));
	}
	
	}
}
