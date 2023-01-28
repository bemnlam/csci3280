package video;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
    import java.awt.BorderLayout;
    import java.awt.Component;
    import java.awt.FileDialog;
    import java.awt.Toolkit;
    import java.awt.event.WindowAdapter;
    import java.awt.event.WindowEvent;
    import java.io.IOException;
    import java.io.Serializable;
    import java.net.URL;
    import javax.media.CannotRealizeException;
import javax.media.IncompatibleTimeBaseException;
    import javax.media.Manager;
    import javax.media.MediaLocator;
    import javax.media.NoPlayerException;
    import javax.media.Player;
    import javax.media.Time;
    import javax.media.Clock;
import javax.sound.sampled.FloatControl;
    import javax.swing.JDialog;
    import javax.swing.JPanel;
    import javax.swing.JFrame;
    import javax.swing.UIManager;
    import javax.swing.UnsupportedLookAndFeelException;
    import javax.swing.plaf.metal.MetalLookAndFeel;
   
    public class Clips{
        
//        Time pause_t;
/*        public abstract class VideoFile implements Serializable { 
            
        }
        public static VideoFile create(String path) throws Exception { 
            return null;
        }*/
        public void VideoFile(String path) throws CannotRealizeException { 
            JFrame.setDefaultLookAndFeelDecorated(true); 
	    JDialog.setDefaultLookAndFeelDecorated(true);
            try{
		UIManager.setLookAndFeel(new MetalLookAndFeel());
            }
            catch(UnsupportedLookAndFeelException e){
		e.printStackTrace();
            }
		PlayerFrame.main(path);
        }
        public Time getMicrosecondLength(){
            Time length = PlayerPanel.player.getDuration();
            return length;
        }
        public Time getMicrosecondPosition() { 
            long temp = PlayerFrame.sliderPlayBack.getValue();
            Time length = getMicrosecondLength();
            long length_i = length.getNanoseconds();
            length_i = (temp/100000) * length_i;
            Time value = new javax.media.Time((long)length_i);
            return value;
        }
        public void setMicrosecondPosition(long position) { 
            Time value = new javax.media.Time((long)position);
            PlayerPanel.player.stop();
            PlayerPanel.player.setMediaTime(value);
            PlayerPanel.player.start();
        }
        public float getMaxMasterGain() { 
            float dB = (float)(Math.log(1)/Math.log(10.0)*20.0);
            return dB;
        }
        public float getMinMasterGain() { 
            float dB = (float)(Math.log(0)/Math.log(10.0)*20.0);
            return dB;
        }
        public float getMasterGain() { 
            float temp = (float)PlayerFrame.sliderVol.getValue();
            float value = (float) (temp/100.0);
            float dB = (float)(Math.log(value)/Math.log(10.0)*20.0);
            return dB;
        }
        public void setMasterGain(float value) { 
            float dB = (float)PlayerFrame.sliderVol.getValue();
            if(dB == -35)//set Mute at minimum
                PlayerPanel.player.getGainControl().setMute(true);
            else
                PlayerPanel.player.getGainControl().setMute(false);
                PlayerPanel.player.getGainControl().setDB(dB);
        }
        public void play() {
            PlayerPanel.player.start();
        }
        public void stop() { 
            PlayerPanel.player.stop();
            PlayerPanel.player.setMediaTime(Clock.RESET);
//            PlayerPanel.player.close();
        }
        public void pause() throws IncompatibleTimeBaseException { 
            PlayerPanel.player.stop();
        }
        public void resume() { 
//            PlayerPanel.player.setMediaTime(PlayerPanel.player.getStopTime());
            PlayerPanel.player.start();
            
        }
        public void getUI(){
//          super(File(path).getName());
        }
    }