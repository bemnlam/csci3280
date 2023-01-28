package music;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class SongInfoController {
	public static String getSongInfo(SoundFile s)
	{
		String str = "";
		String tmp;
		
		//str = "<html><b>Now Playing:</b><br>";
		
		tmp = "";
		try{
			tmp = s.getFilename();
			str += "<html><b>"+ tmp + "</b><br>";
			//tmp = SongListController.getHumanReadableTime((long)s.getMicrosecondLength());
			//str += " (" + tmp + ")<br>";
		} catch(Exception e1) {
			str += "(no such file)<br>";
		}

		tmp = "";
		try{
			tmp = ""+s.getTitle() + "<br>";
			if(s.getTitle() =="") {
				tmp = "N/A"+ "<br>";
			}
			str += tmp;
		} catch(Exception e2) {
			str += "(no album)<br>";
		}
		
		tmp = "";
		try{
			tmp = ""+ s.getSinger() + "<br>";
			if(s.getSinger() =="") {
				tmp = "N/A"+ "<br>";
			}
			str += tmp;
		} catch(Exception e3) {
			str += "(no singer)<br>";
		}
		
		tmp = "";
		try{
			tmp = ""+s.getAlbum() + "<br>";
			if(s.getAlbum() =="") {
				tmp = "N/A"+ "<br>";
			}
			str += tmp;
		} catch(Exception e3) {
			str += "(no album)<br>";
		}
		
		str += "</html>";
		
		//getLyrics(s);
		return str;
	}
	
	public static void getLyrics(SoundFile s){
		ArrayList<LyricsLine> lines;
		try {
			lines = s.getLyricsLines();
			//for (LyricsLine line : lines) { 
			    //System.out.println(line.getMicrosecondLength()/1000 + " " + line.getText()); 
			//}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(e instanceof FileNotFoundException) {
				System.out.println("SongInfoController: no .lrc");
			}else {
				e.printStackTrace();
				System.out.println("SongInfoController: Exception");
			}
		} 
	}
}
