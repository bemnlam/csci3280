package music;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class ThreadFindSong extends Thread{
	private static SoundFile s;
	private static JLabel lArt;
	private static boolean includeLyrics;
	
	public ThreadFindSong(SoundFile sound, JLabel lblAlbumArt, boolean isIncludeLyrics) {
		s = sound;
		includeLyrics = isIncludeLyrics;
		lArt = lblAlbumArt;
		lArt.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/black-32390_150.png")));
	}
	
	public void run() {
		SoundInfoDownloader sid = new SoundInfoDownloader();
		String artLink;
		/*if(s.getAlbum().length()>0) {
			artLink = sid.searchAlbumArt(s.getAlbum());
			try {
				lArt.setIcon(new ImageIcon(new URL(artLink)));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				lArt.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/black-32390_150.png")));
			}
		}*/
		if(includeLyrics) {
			ArrayList<LyricsLine> lyrics;
			
			String queryStr;
			if(!s.getTitle().isEmpty()) {
				queryStr = s.getTitle() + "+" + s.getSinger() + s.getAlbum();
			} else {
				queryStr = s.getFilename().replace(".wav", "") + "+" + s.getAlbum();
				System.out.println("\nlrc key: " + queryStr);
			}
			System.out.println("search lrc: " + queryStr);
			String downloadedLyrics = sid.findLyrics(queryStr);
			sid.saveLyrics(downloadedLyrics, s.getAbsoluteFilename());
			//label.setText("done!");
			try {
				lyrics = s.getLyricsLines();
				//System.out.println("waked: " + lyrics.get(0).getText());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println("ThreadFindSong: .lrc not found");
				//lyrics.clear();
			}
		}
		if(s.getSinger().length()>0) {
			artLink = sid.searchAlbumArt(s.getAlbum() + "+" + s.getSinger());
			if(artLink.length()<=0)
				artLink = sid.searchAlbumArt(s.getAlbum());
			//System.out.println("\nart key: " + s.getAlbum() + "+" + s.getSinger());
		}else{
			artLink = sid.searchAlbumArt(s.getAlbum());
			System.out.println("\nart key: " + s.getAlbum());
		}
		try {
			lArt.setIcon(new ImageIcon(new URL(artLink)));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			lArt.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/black-32390_150.png")));
		}
	}
}
