package music;
import javax.swing.JButton;


public class MediaBtnController {
	public MediaBtnController() {
		System.out.println("mbc created");
	}
	
	private boolean isMusicPlay = false;
	public void musicPlay(JButton btn)
	{
		//System.out.println("play");
		isMusicPlay = true;
		btn.setVisible(!isMusicPlay);
	}
	public void musicPause(JButton btn)
	{
		//System.out.println("pause");
		isMusicPlay = false;
		btn.setVisible(!isMusicPlay);
	}
	public void misicStop(JButton btn)
	{
		//System.out.print("stop");
		isMusicPlay = false;
		btn.setVisible(false);
	}
}
