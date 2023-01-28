package video;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PlayerPanel.java
 *
 * Created on 2014年3月15日, 上午01:19:30
 */
/**
 *
 * @author User
 */
//package org.jmf.example;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.CannotRealizeException;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.GainChangeEvent;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.RealizeCompleteEvent;
import javax.media.Time;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JPanel;

public class PlayerPanel extends javax.swing.JPanel  implements ActionListener, ControllerListener{

    /** Creates new form PlayerPanel */
    	private static final long serialVersionUID = 1L;
	
	private Component visualComponent;
	public static Player player;
        public static Player mediaPlayer;
        public static Time length;
        public static long duration;
        public static Clip videofile;

        public PlayerPanel(String path) throws CannotRealizeException, LineUnavailableException {
            initComponents();
            try{
//                System.out.println("new"+path);
		player = Manager.createPlayer(new URL(path));
                videofile = AudioSystem.getClip();
		player.addControllerListener(this);
/*                new javax.media.GainChangeListener() {
                    @Override
                    public void gainChange(GainChangeEvent gce) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                };*/
                mediaPlayer = Manager.createRealizedPlayer(new URL(path));
                visualComponent = mediaPlayer.getVisualComponent();
                Dimension size = visualComponent.getPreferredSize();
//                PlayerFrame.width = visualComponent.getWidth();
//                PlayerFrame.height = visualComponent.getHeight();

                PlayerFrame.width = size.width;
                PlayerFrame.height = size.height;
                mediaPlayer.close();
//                PlayerFrame.width = this.getWidth();
//                PlayerFrame.height = this.getHeight();
//                PlayerFrame.setSize(size.width,size.height);
                player.realize();
		player.start();
//                player.getGainControl().setDB((float)0.5);
                length = PlayerPanel.player.getDuration();
                duration = length.getNanoseconds();
//                System.out.println("Duration" + duration);
                
            }
            catch(NoPlayerException e){
		e.printStackTrace();
            }
            catch(MalformedURLException e){
		e.printStackTrace();
            }
            catch(IOException e){
		e.printStackTrace();
            }
        }
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}

	public void actionPerformed(ActionEvent e){

	}

	public void controllerUpdate(ControllerEvent c){
		if(player == null)
			return;
		if(c instanceof RealizeCompleteEvent){
			if((visualComponent = player.getVisualComponent()) != null)
				add(visualComponent);
		}
//                Component visual = player.getVisualComponent();
//                Dimension size = visual.getPreferredSize();
//                PlayerFrame.width = size.width;
//                PlayerFrame.height = size.height;
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}