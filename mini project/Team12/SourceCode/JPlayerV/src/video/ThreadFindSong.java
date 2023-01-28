package video;
import java.util.ArrayList;


public class ThreadFindSong extends Thread{
	private static SoundFile s;
	
	public ThreadFindSong(SoundFile sound) {
		s = sound;
	}
	
	public void run() {
		ArrayList<LyricsLine> lyrics;
		SoundInfoDownloader sid = new SoundInfoDownloader();
		String queryStr;
		if(!s.getTitle().isEmpty()) {
			queryStr = s.getTitle() + "+" + s.getAlbum();
		} else {
			queryStr = s.getFilename().replace(".wav", "") + "+" + s.getAlbum();
		}
		System.out.println("searching: " + queryStr);
		String downloadedLyrics = sid.findLyrics(queryStr);
		sid.saveLyrics(downloadedLyrics, s.getAbsoluteFilename());
		//label.setText("done!");
		try {
			lyrics = s.getLyricsLines();
			
			System.out.println("waked: " + lyrics.get(0).getText());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("@PLayBackTimerTask: no lyrics found");
			//lyrics.clear();
		}

	}
}
