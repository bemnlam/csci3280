package video;
import java.util.Locale;

import javax.media.CannotRealizeException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

//import statements
//Check if window closes automatically. Otherwise add suitable code
public class VideoMain extends JFrame {
	public static void main(String args) throws CannotRealizeException {//throws CannotRealizeException, ClassNotFoundException, InstantiationException, IllegalAccessException {
//            Body.main(args);
//            PlayList.main(args);
           /* JFrame.setDefaultLookAndFeelDecorated(true); 
	    JDialog.setDefaultLookAndFeelDecorated(true);
            try{
		UIManager.setLookAndFeel(new MetalLookAndFeel());
            }
            catch(UnsupportedLookAndFeelException e){
		e.printStackTrace();
            }*/
//		new PlayerFrame("file:///C:/Users/User/Desktop/csci3280/lunar.mov");
//            new PlayerFrame("file:///F:/slide.wmv");
            args = "file:///"+args;
            args = args.replace("\\", "/");
            System.out.println(args);
            //PlayerFrame.main(args);
            fxengine.PlayerView.launchFX(args);
        }
}