package video;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;


public class LyricsTimerTask extends TimerTask{
	private static int counter = 0;
	private static JLabel label;
	private static Timer timer;
	private static ArrayList<LyricsLine> lyrics;
	
	private static boolean isCancelled = false;
	
	/* testing data struct
	private static ArrayList<Integer> lyricsTime = new ArrayList<Integer>();
	private static ArrayList<String> lyricsString = new ArrayList<String>();
	
	private static void initArrayList(){
		if(lyricsTime.isEmpty()) {
			lyricsTime.clear();
			lyricsString.clear();
			
			lyricsTime.add(1800);
			lyricsString.add(0, "你太猖狂");
			lyricsTime.add(22490);
			lyricsString.add(1, "----");
			lyricsTime.add(2810);
			lyricsString.add(2,"能約出來的人都約光");
			lyricsTime.add(6290);
			lyricsString.add(3,"能吃得下的早已吃光");
		}
	}
	*/
	public LyricsTimerTask(JLabel output, ArrayList<LyricsLine> input) {
		//counter = 0;
		//initArrayList();
		lyrics = input;
		label = output;
	}
	private LyricsTimerTask() {
		
	}
	
	@Override
	public void run() {
		if(counter == lyrics.size()+1) {
			label.setText("<end>");
			return;
		}
		//System.out.println("LyricsTimer: " + counter);
		//label.setText("LyricsTimer: " + counter);
		//label.setText(lyricsTime.get(counter).toString() + ":" + lyricsString.get(counter));
		
		if(counter==0) {
			try {
				label.setText(lyrics.get(counter).getText());
				Thread.sleep(lyrics.get(counter).getMicrosecondLength()/1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(timer==null || isCancelled==true) {
			timer = new Timer();
			isCancelled = false;
			counter = 0;
		}
		label.setText(lyrics.get(counter).getText());
		timer.schedule(new LyricsTimerTask(),lyrics.get(counter+1).getMicrosecondLength()/1000);
		counter++;
	}
	
	public static void cancelTask() {
		isCancelled = true;
		counter = 0;

		if(timer!=null) {
			timer.cancel();
			timer.purge();
		}
		label.setText("");
	}
	
}
