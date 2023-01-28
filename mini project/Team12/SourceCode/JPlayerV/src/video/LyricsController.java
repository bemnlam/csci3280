package video;
import java.util.ArrayList;


public class LyricsController {
	//private static SoundFile s;
	//private static ArrayList<LyricsLine> lyrics;
	
	public static String getCurrentLyrics(ArrayList<LyricsLine> lyrics, float position) {
		/*TODO
		 * get current position
		 * calculate the corresponding lyrics
		 * return lyrics
		 */
		
		int currPos = (int) position;
		//System.out.println("currPos: " + currPos);
/*		
		for (LyricsLine line : lyrics) {
			if(line.getMicrosecondLength() > currPos) {
				System.out.println(line.getMicrosecondLength() + " " + line.getText());
				currentLyrics
				break;
			}
		} 
*/		
		String currentLyric = "";
		for(int i=0; i<=lyrics.size(); i++){
			// for last line
			if(i==lyrics.size()) {
				currentLyric = "";
				//System.out.println(currentLyric);
				break;
			}
			
			if(i<lyrics.size() && lyrics.get(i).getMicrosecondLength() > currPos) {
				if(i>0) {
					currentLyric = (lyrics.get(i-1).getText());
					//System.out.println(currentLyric);
				}
				break;
			}
		}
		return currentLyric;
	}
}
