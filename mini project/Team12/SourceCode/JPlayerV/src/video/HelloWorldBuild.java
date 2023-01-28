package video;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.CannotRealizeException;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JLabel;

import java.awt.Color;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.SystemColor;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.JTextPane;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JProgressBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;

import music.JPlayer;
import music.PlayBackTimerTask;
import music.SongListController;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;


//import sun.misc.IOUtils;
import org.apache.commons.io.*;

public class HelloWorldBuild implements ActionListener, ChangeListener, DocumentListener, FocusListener, MouseListener, MouseMotionListener{

        private class ExitListener extends WindowAdapter{
            @Override
            public void windowClosing(WindowEvent event){
                    System.exit(0);
            }
        }
	
	private static JFrame frmJplayer;
	
    private JTable tableVideoList;
	private JTextField txtSearch;
	public static JButton btnPlay;
	private JButton btnExit;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnSearch;
	private JLabel lblAlbumArt;
	private JLabel lblBackground;
	
	public static JProgressBar progressBar;
	
	private JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
	Database db;
    VideoPlaylist playlist2;
	private static Point jFramePos;
	
	/* event listener */
    @Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "play") {
                    System.out.println("play");
                    int videoIndex = VideoListController.getSelectedItemIndex(tableVideoList);
                    System.out.println(videoIndex);
                    if(videoIndex == -1) {
                            if(tableVideoList.getRowCount()>0){
                            videoIndex = 0;
                     }
                    else{
                        try {
                            throw new FileNotFoundException();
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(HelloWorldBuild.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    }
                    String path = null;
                    path = playlist2.list().get(videoIndex);
                    try {
                		System.out.println("byron");
                        VideoMain.main(path);
                    	//path = "file:///"+path;
                        //path = path.replace("\\", "/");
                        //System.out.println(path);
                        //PlayerFrame.main(args);
                        //new fxengine.PlayerView();//.launchFX(path);
                    } catch (CannotRealizeException ex) {
                    //    Logger.getLogger(HelloWorldBuild.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
		else if(cmd == "add"){
			System.out.println("add");
            String path = VideoListController.getFileFullPath(fc).getAbsolutePath();
            System.out.println(path);
            VideoListController.addClip(tableVideoList, playlist2, path);
		}
		else if(cmd == "remove")
		{
			System.out.println("remove");
			VideoListController.removeSong(tableVideoList, playlist2, txtSearch.getText());
		}
		else if(cmd == "search")
		{
            String keyword = txtSearch.getText();
            VideoListController.filtersong(tableVideoList, playlist2, keyword);
		}
		else if(cmd == "exit")
		{
			System.out.println("exit");
			new WindowEvent(frmJplayer, WindowEvent.WINDOW_CLOSING);
			try{
				db.save();
			} catch(Exception eSave) {
				// do nothing
			}
			frmJplayer.dispose();
			System.exit(0);
		}
	}
	

	/* DocumentListener */
    
	public void changedUpdate(DocumentEvent e) {
	    changed(btnSearch);
	}
	public void removeUpdate(DocumentEvent e) {
		changed(btnSearch);
	}
	public void insertUpdate(DocumentEvent e) {
		changed(btnSearch);
	}
	/* FocusListener */
	public void focusGained(FocusEvent e) {
	}
	public void focusLost(FocusEvent e) {
	}

	public void changed(JButton btn) {
		if (txtSearch.getText().length()==0){
    	 	//btn.setEnabled(false);
			VideoListController.filtersong(tableVideoList, playlist2, "");

     	}
     	else {
     		//btn.setEnabled(true);
			String keyword = txtSearch.getText();
			VideoListController.filtersong(tableVideoList, playlist2, keyword);
     	}
     
	}
	
	/* MouseListener */
	public void mouseClicked(MouseEvent e) {
		
	}
	public void mouseEntered(MouseEvent e) {
		
	}
	public void mouseExited(MouseEvent e) {
		
	}
	public void mousePressed(MouseEvent e) {
		if(e.getComponent().getName() == "jFrame") {
			jFramePos = e.getPoint();
		}else{
			PlayBackTimerTask.pause();
		}
	}
	public void mouseReleased(MouseEvent e){
		//System.out.println("MouseX = " + e.getX());
	}
	
	/* MouseMotionListener */
	public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    	if(e.getComponent().getName()=="jFrame") {
	        Point currCoords = e.getLocationOnScreen();
	        frmJplayer.setLocation(currCoords.x - jFramePos.x, currCoords.y - jFramePos.y);
    	}
    }
	
	
	/* init elements */
	private void init(){
		//fc.setFileFilter(new FileNameExtensionFilter("Windows media video (*.wmv)","wmv"));
		
		try {
            db = Database.load();
			playlist2 = db.getVideo();
			if(playlist2.list().isEmpty()) {
				System.out.println("empty list");
			} else {
				VideoListController.initSongList(tableVideoList, playlist2);
			}
		} catch (Exception e) {
			System.out.println("err on loading db");
			//e.printStackTrace();
		}
		
	}
	/**
	 * Launch the application.
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	//import org.apache.commons.httpclients.*;
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e1) {
				// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HelloWorldBuild window = new HelloWorldBuild();
					window.frmJplayer.setVisible(true);
					frmJplayer.setShape(new RoundRectangle2D.Double(1, 1, 325, 346, 15, 15));
					frmJplayer.setOpacity(0.90f);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// get trad content using regEx
	/**
	 * Create the application.
	 * @throws InterruptedException 
	 */
	public HelloWorldBuild() throws InterruptedException {
		fc.setDialogTitle("Choose a file to add...");
		draw();
		init();
		addListeners();
                frmJplayer.addWindowListener(new ExitListener());
	}
	/**
	 * resize the icons
	 */
	private ImageIcon makeImageIcon(String path, int width, int height) {
		ImageIcon icon = new ImageIcon(HelloWorldBuild.class.getResource(path));
		if(width<=0 || height <=0){
			return icon;
		}
		Image img = icon.getImage(); 
		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB); 
		Graphics g = bi.createGraphics(); 
		g.drawImage(img, 0, 0, width, height, null); 
		ImageIcon resizedIcon = new ImageIcon(bi);
		return resizedIcon;
	}
	
	private void addListeners() {
		
		frmJplayer.setName("jFrame");
		frmJplayer.addMouseListener(this);
		frmJplayer.addMouseMotionListener(this);
		
		btnPlay.addActionListener(this);
		btnPlay.setActionCommand("play");
		
		btnAdd.addActionListener(this);
		btnAdd.setActionCommand("add");

		btnRemove.addActionListener(this);
		btnRemove.setActionCommand("remove");

/*		btnEdit.addActionListener(this);
		btnEdit.setActionCommand("edit");
		
		btnEditDone.addActionListener(this);
		btnEditDone.setActionCommand("done");*/
		
		btnSearch.addActionListener(this);
		btnSearch.setActionCommand("search");
		
		btnExit.addActionListener(this);
		btnExit.setActionCommand("exit");
		
		txtSearch.addFocusListener(this);
		
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void draw() {
		//final MediaBtnController mbc = new MediaBtnController();
		
		frmJplayer = new JFrame();
//		frmJplayer.setName("JFrame");
		frmJplayer.setBackground(SystemColor.window);
		//TODO move out windowAdapter
		frmJplayer.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					db.save();
					System.out.println("db saved");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		frmJplayer.setForeground(Color.LIGHT_GRAY);
		frmJplayer.setTitle("JPlayer");
		frmJplayer.setResizable(false);
		frmJplayer.getContentPane().setBackground(SystemColor.window);
		frmJplayer.setBounds(100, 100, 325, 346);
		frmJplayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJplayer.getContentPane().setLayout(null);
		
		frmJplayer.setUndecorated(true); //make the title bar disappear
		
		JButton btnSong = new JButton("song mode");
		btnSong.setForeground(SystemColor.inactiveCaptionText);
		btnSong.setFont(btnSong.getFont().deriveFont(btnSong.getFont().getStyle() | Font.ITALIC));
		btnSong.setFocusable(false);
		btnSong.setHorizontalTextPosition(SwingConstants.CENTER);
		btnSong.setContentAreaFilled(false);
		btnSong.setBorder(null);
		btnSong.setOpaque(false);
		btnSong.setContentAreaFilled(false);
		btnSong.setBorderPainted(false);
		btnSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					music.JPlayer.main(null);
					frmJplayer.setVisible(false);
					try {
						db.save();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSong.setBounds(160, 4, 95, 20);
        btnSong.setVisible(true);
		frmJplayer.getContentPane().add(btnSong);
		
		btnPlay = new JButton(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Play.png")));
		btnPlay.setBounds(10, 50, 55, 55);
		frmJplayer.getContentPane().add(btnPlay);
		btnPlay.setFocusable(false);
		btnPlay.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/Play.png")));
		btnPlay.setBorder(null);
		btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
		btnPlay.setOpaque(false);
		btnPlay.setContentAreaFilled(false);
		btnPlay.setBorderPainted(false);
		
		btnExit = new JButton("");
		btnExit.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/exit-red.png")));
		btnExit.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/exit.png")));
		btnExit.setFont(new Font("Arial Unicode MS", Font.BOLD, 16));
		btnExit.setBounds(302, 4, 20, 20);
		frmJplayer.getContentPane().add(btnExit);
		btnExit.setFocusable(false);
		btnExit.setRequestFocusEnabled(false);
		btnExit.setBorder(null);	
		btnExit.setOpaque(false);
		btnExit.setContentAreaFilled(false);
		btnExit.setBorderPainted(false);
		btnExit.setForeground(SystemColor.inactiveCaptionText);
		
        String headers[] = { "Filename", "Path" };//,"Length" };
		tableVideoList = new JTable();
		tableVideoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TableModel tableModel = new DefaultTableModel(headers,0){
			public boolean isCellEditable(int row, int column)
		    {
		      return false;
		    }
		};
		tableVideoList.setModel(tableModel);
		tableVideoList.setBackground(Color.WHITE);
		tableVideoList.setBounds(14, 170, 290, 129);
		//frmJplayer.getContentPane().add(tableVideoList);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 113, 303, 12);
		frmJplayer.getContentPane().add(separator);
		
		btnAdd = new JButton("");
		btnAdd.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/Add.png")));
		btnAdd.setBounds(250, 315, 20, 20);
		frmJplayer.getContentPane().add(btnAdd);
		btnAdd.setFocusable(false);
		btnAdd.setBorderPainted(false);
		btnAdd.setBorder(null);
		btnAdd.setHorizontalTextPosition(SwingConstants.CENTER);
		btnAdd.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Add.png")));
		btnAdd.setOpaque(false);
		btnAdd.setContentAreaFilled(false);
		btnAdd.setBorderPainted(false);
		
		btnRemove = new JButton("");
		btnRemove.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/Remove.png")));
		btnRemove.setBounds(280, 315, 20, 20);
		frmJplayer.getContentPane().add(btnRemove);
		btnRemove.setFocusable(false);
		btnRemove.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Remove.png")));
		btnRemove.setBorder(null);
		btnRemove.setOpaque(false);
		btnRemove.setContentAreaFilled(false);
		btnRemove.setBorderPainted(false);
		
		txtSearch = new JTextField();
		txtSearch.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
		txtSearch.setBounds(10, 120, 276, 28);
		frmJplayer.getContentPane().add(txtSearch);
		txtSearch.setColumns(10);
		
		btnSearch = new JButton();
		btnSearch = new JButton(makeImageIcon("/iconsNormal/Search.png",20,20));
		btnSearch.setBounds(290, 124, 20, 20);
		txtSearch.getDocument().addDocumentListener(this);
//		btnSearch.addActionListener(this);
		btnSearch.setHorizontalAlignment(SwingConstants.LEFT);
		btnSearch.setVerticalAlignment(SwingConstants.TOP);
		btnSearch.setForeground(SystemColor.inactiveCaptionText);
		btnSearch.setContentAreaFilled(false);
		btnSearch.setBorder(null);
		btnSearch.setOpaque(false);
		btnSearch.setContentAreaFilled(false);
		btnSearch.setBorderPainted(false);
		btnSearch.setFocusable(false);

		frmJplayer.getContentPane().add(btnSearch);
//		btnSearch.setVisible(false);
		
		JScrollPane scrollPane = new JScrollPane(tableVideoList);
		scrollPane.setBounds(12, 150, 302, 156);
		frmJplayer.getContentPane().add(scrollPane);

		lblAlbumArt = new JLabel("");
//		lblAlbumArt = new JLabel(makeImageIcon("/iconsNormal/black-32390_150.png",50,50));
		ImageIcon tempicon = new ImageIcon(JPlayer.class.getResource("/iconsNormal/black-32390_150.png"));
		Image reicon = tempicon.getImage();
		reicon = reicon.getScaledInstance(100, 100, 0);
		tempicon.setImage(reicon);
		lblAlbumArt.setIcon(tempicon);
		lblAlbumArt.setBounds(220, 15, 100, 100);
		lblAlbumArt.setBorder(null);
		lblAlbumArt.setHorizontalTextPosition(SwingConstants.CENTER);
		lblAlbumArt.setHorizontalAlignment(SwingConstants.CENTER);
		frmJplayer.getContentPane().add(lblAlbumArt);
		
		lblBackground = new JLabel("");
		lblBackground.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/bg.jpg")));
		lblBackground.setBounds(0, 0, 325, 346);
		frmJplayer.getContentPane().add(lblBackground);
		
	}

    @Override
    public void stateChanged(ChangeEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
        
}
