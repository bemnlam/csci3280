package music;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

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

import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.JCheckBox;

public class JPlayer implements ActionListener, ChangeListener, DocumentListener, FocusListener, MouseListener, MouseMotionListener{
	
	
	private static JFrame frmJplayer;
	
	private JTable tableSongList;
	private JTextField txtSearch;
	private JButton btnPlay;
	private JButton btnStop;
	private JButton btnPause;
	private JLabel lblSongInfo;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnEdit;
	private JButton btnEditDone;
	private JSlider sliderVol;
	private JButton btnSearch;
	private JLabel lblLyrics;
	private JLabel lblAlbumArt;
	private JSlider sliderPlayBack;
	private JSlider sliderBalance;
	private JSlider sliderPan;
	
	private JPanel panel;
	private JPanel panelEffect;
	private JTextField txtNewTitle;
	private JTextField txtNewAlbum;
	private JTextArea txtaFileName;
	private JTextArea txtaFullpath;
	private JButton btnExit;
	private JButton btnEffect;
	private JScrollPane scrollPaneTable;
	public static JProgressBar progressBar;
	
	private static Point jFramePos;
	
	private JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
	private FileDialog fd = new FileDialog(frmJplayer,"Choose a wave file", FileDialog.LOAD);
	
	Database db;
	SoundPlaylist playlist;
	private JLabel lblL;
	private JLabel lblR;
	private JLabel label;
	private JLabel lblSound;
	
	ArrayList<ImageIcon> soundIcons = new ArrayList<ImageIcon>();
	private JLabel lblCurrTime;
	private JLabel labelBarIcon;
	private JLabel lblBackground;
	private JLabel lblBg;
	private JLabel lblEffectBg;
	private JButton btnNext;
	private JButton btnPrev;
	private JTextField txtNewSinger;

	/* event listener */
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "play") {
			System.out.println("play");
			PlayBackController.playClicked(tableSongList, sliderVol, sliderPlayBack, lblLyrics, lblAlbumArt, lblSongInfo, btnPlay, btnPause, btnStop, txtSearch, playlist);
			
		} 
		else if(cmd == "pause"){
			System.out.println("pause");
			btnPlay.setVisible(true);
			btnPause.setVisible(false);
			PlayBackController.pauseClicked(btnPlay, btnPause, btnStop);

		}
		else if(cmd == "stop"){
			System.out.println("stop");
			btnPlay.setVisible(true);
			btnPause.setVisible(false);
			PlayBackController.stopClicked(btnPlay, sliderPlayBack, btnPause, btnStop);
		}
		else if(cmd == "add"){
			System.out.println("add");
			panelEffect.setVisible(false);
			SongListController.addSong(tableSongList, fd, playlist, txtSearch.getText());
		}
		else if(cmd == "remove")
		{
			System.out.println("remove");
			panelEffect.setVisible(false);
			SongListController.removeSong(tableSongList, playlist, txtSearch.getText());
		}
		else if(cmd == "edit")
		{
			System.out.println("edit");
			if(SongListController.getSelectedItemIndex(tableSongList) != -1) {
				panel.setVisible(true);
				panelEffect.setVisible(false);
				btnEdit.setVisible(false);
				btnAdd.setEnabled(false);
				btnRemove.setEnabled(false);
				btnEffect.setEnabled(false);
				tableSongList.setVisible(false);
				scrollPaneTable.setEnabled(false);
				scrollPaneTable.setVisible(false);
				btnEditDone.setVisible(true);
				SongListController.readSongInfoFromList(tableSongList, txtaFullpath, txtaFileName, txtNewTitle, txtNewSinger, txtNewAlbum, playlist, txtSearch.getText());
				
			}
		}
		else if(cmd == "done")
		{
			System.out.println("done");
			if(SongListController.getSelectedItemIndex(tableSongList) != -1) {
				panel.setVisible(false);
				panelEffect.setVisible(false);
				btnEdit.setVisible(true);
				btnAdd.setEnabled(true);
				btnRemove.setEnabled(true);
				btnEffect.setEnabled(true);
				tableSongList.setVisible(true);
				scrollPaneTable.setEnabled(true);
				scrollPaneTable.setVisible(true);
				btnEditDone.setVisible(false);
				SongListController.writeSongInfoToList(tableSongList, txtaFullpath, txtaFileName, txtNewTitle, txtNewSinger, txtNewAlbum, playlist, txtSearch.getText());
				try {
					db.save();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(PlayBackController.getSound() != null)
					lblSongInfo.setText(SongInfoController.getSongInfo(PlayBackController.getSound()));
			}
		}
		else if(cmd == "effect") {
			System.out.println("effect");
			panelEffect.setVisible(!panelEffect.isVisible());
			tableSongList.setEnabled(!panelEffect.isVisible());
			scrollPaneTable.setVisible(!panelEffect.isVisible());
			btnEdit.setEnabled(!panelEffect.isVisible());
			btnAdd.setEnabled(!panelEffect.isVisible());
			btnRemove.setEnabled(!panelEffect.isVisible());
			if(tableSongList.isEnabled())
			{
				scrollPaneTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scrollPaneTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			}else{
				scrollPaneTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				scrollPaneTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			}
		}
		else if(cmd == "search")
		{
			System.out.println("search");
			//System.out.println(txtSearch.getText());
			String keyword = txtSearch.getText();
			SongListController.filtersong(tableSongList, playlist, keyword);
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
		else if(cmd == "finished") //"next"
		{
			PlayBackController.playNextSong(btnPlay, btnPause, btnStop, lblSongInfo, lblLyrics, lblAlbumArt, sliderPlayBack, sliderVol, txtSearch, tableSongList, playlist);
		}
		else if(cmd == "prev")
		{
			PlayBackController.playPrevSong(btnPlay, btnPause, btnStop, lblSongInfo, lblLyrics, lblAlbumArt, sliderPlayBack, sliderVol, txtSearch, tableSongList, playlist);
		}
	}
	
	public void stateChanged(ChangeEvent e){
		JSlider source = (JSlider) e.getSource();
		
		if(source.getName() == "sliderVol") {
			int factor = (int) source.getValue();
			SoundEffectController.adjustVolumn(PlayBackController.getSound(), factor);
			
			int currVol = source.getValue();
			if(currVol==0) {
				lblSound.setIcon(soundIcons.get(0));
			}else if(currVol>0 && currVol<=33) {
				lblSound.setIcon(soundIcons.get(1));
			}else if(currVol>33 && currVol<=67) {
				lblSound.setIcon(soundIcons.get(2));
			}else if(currVol>68 && currVol<=100) {
				lblSound.setIcon(soundIcons.get(3));
			}
			
		}else if(source.getName() == "sliderPlayBack") {
			//System.out.println(source.getValue() + " vs " + source.getMaximum());
			if(PlayBackController.getSound()!=null){
				lblCurrTime.setText(SongListController.getHumanReadableTime((long) ((float)source.getValue()/(float)source.getMaximum()*(float)PlayBackController.getSound().getMicrosecondLength())));
			}
			//if(source.getValue() >= source.getMaximum()) {
			//if(PlayBackController.getSound().getMicrosecondPosition() >= PlayBackController.getSound().getMicrosecondLength()) {
			if(SongListController.getHumanReadableSecond(PlayBackController.getSound().getMicrosecondPosition()) >=
					SongListController.getHumanReadableSecond(PlayBackController.getSound().getMicrosecondLength()) ) {
				/*
				PlayBackTimerTask.pause();

				PlayBackController.stopClicked(btnPlay, sliderPlayBack, btnPause, btnStop);
				System.out.println("song Finished");
				int nextIndex = SongListController.getSelectedItemIndex(tableSongList)+1;
				if(nextIndex < tableSongList.getRowCount()) {
					tableSongList.setRowSelectionInterval(nextIndex, nextIndex);
					PlayBackController.playClicked(tableSongList, sliderVol, sliderPlayBack, lblLyrics, lblAlbumArt, lblSongInfo, btnPlay, btnPause, btnStop, playlist);
					sliderPlayBack.setValue(0);
					lblSongInfo.setText(SongInfoController.getSongInfo(PlayBackController.getSound()));
				}
				*/
				PlayBackController.playNextSong(btnPlay, btnPause, btnStop,
							lblSongInfo, lblLyrics, lblAlbumArt,
							sliderPlayBack, sliderVol,
							 txtSearch, tableSongList, playlist);
			}
		}else if(source.getName() == "sliderBalance") {
			SoundEffectController.adjustBalance(PlayBackController.getSound(),  (int) source.getValue());
		}else if(source.getName() == "sliderPan") {
			SoundEffectController.adjustPan(PlayBackController.getSound(), (int) source.getValue());
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
		//System.out.println("focusing...");
		//((JTextField) e.getSource()).setText("");
	}
	public void focusLost(FocusEvent e) {
		//((JTextField) e.getSource()).setText("(Type to Search...)");
	}

	public void changed(JButton btn) {
		if (txtSearch.getText().length()==0){
    	 	//btn.setEnabled(false);
			SongListController.filtersong(tableSongList, playlist, "");

     	}
     	else {
     		//btn.setEnabled(true);
			String keyword = txtSearch.getText();
			SongListController.filtersong(tableSongList, playlist, keyword);
     	}
     
	}
	
	/* MouseListener */
	public void mouseClicked(MouseEvent e) {
		int index = -1;
		if(e.getClickCount()==1 && e.getComponent().getName()=="table"){
			index = ((JTable) e.getSource()).getSelectedRow();
		}
		if(e.getClickCount()==2 && index!=1 && e.getComponent().getName()=="table"){
			//System.out.println("[[double event]]");
			PlayBackController.playSelectedSong(btnPlay, btnPause, btnStop, lblSongInfo, lblLyrics, lblAlbumArt, sliderPlayBack, sliderVol, txtSearch, tableSongList, playlist);
		}
	}
	public void mouseEntered(MouseEvent e) {
		
	}
	public void mouseExited(MouseEvent e) {
		
	}
	public void mousePressed(MouseEvent e) {
		//System.out.println(e.getComponent().getName());
		if(e.getComponent().getName() == "jFrame") {
			jFramePos = e.getPoint();
			//System.out.println("getting position");
		}else{
			PlayBackTimerTask.pause();
		}
	}
	public void mouseReleased(MouseEvent e){
		//System.out.println("MouseX = " + e.getX());
		
		if(e.getComponent().getName()=="sliderPlayBack") {
			JSlider source = (JSlider) e.getSource();
			SoundFile s = PlayBackController.getSound();
			if(s!=null) {
				long position = (long) ( (float)source.getValue()/(float)source.getMaximum() * s.getMicrosecondLength());
				//System.out.println("onChanged: "+source.getValue() + ";" + position);
				if(source.getValue() >= source.getMaximum()) {
					//position = s.getMicrosecondLength();
					PlayBackController.playNextSong(btnPlay, btnPause, btnStop,
							lblSongInfo, lblLyrics, lblAlbumArt,
							sliderPlayBack, sliderVol,
							txtSearch, tableSongList, playlist);
					//System.out.println("playNext");
				}else {
					s.setMicrosecondPosition(position);
				}
			} else {
				source.setValue(0);
			}
		}
		PlayBackTimerTask.resume();
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

		soundIcons.add(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Sound0.png")));
		soundIcons.add(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Sound33.png")));
		soundIcons.add(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Sound66.png")));
		soundIcons.add(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Sound100.png")));
		int currVol = sliderVol.getValue();
		if(currVol==0) {
			lblSound.setIcon(soundIcons.get(0));
		}else if(currVol>0 && currVol<=33) {
			lblSound.setIcon(soundIcons.get(1));
		}else if(currVol>33 && currVol<=67) {
			lblSound.setIcon(soundIcons.get(2));
		}else if(currVol>68 && currVol<=100) {
			lblSound.setIcon(soundIcons.get(3));
		}
		
		fc.setFileFilter(new FileNameExtensionFilter("wave file (*.wav)", "wav"));
		
		try {
			db = Database.load();
			playlist = db.getSounds();
			if(playlist.list().isEmpty()) {
				System.out.println("empty list");
			} else {
				SongListController.initSongList(tableSongList, playlist, txtSearch);
			}
		} catch (Exception e) {
			System.out.println("JPlayer.java: err on loading db");
			//e.printStackTrace();
		}
		
	}
	/**
	 * Launch the application.
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	//import org.apache.commons.httpclients.*;
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JPlayer window = new JPlayer();
					window.frmJplayer.setVisible(true);
					frmJplayer.setShape(new RoundRectangle2D.Double(1, 1, 445, 395, 15, 15));
					frmJplayer.setOpacity(0.90f);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
		});
		
		/*get lyrics example
		
		System.out.println("lyrics:\n" + l);
		
		PrintWriter writer = new PrintWriter("downloaded.lrc", "UTF-8");
		writer.write(l);
		writer.close();
		*/
	}
	
	// get trad content using regEx
	private String translateLyrics(String input){
		String tradStr = "";
		String magic = "(.+)/></head>(.*?)</td>";
		Pattern pat = Pattern.compile(magic, Pattern.DOTALL);
		Matcher mat = pat.matcher(input);
		
		if(mat.find()) {
			//System.out.println(mat.group(2));
			tradStr = mat.group(2);
		} else {
			System.out.println("translateLyrics: pattern not match");
		}
		return tradStr;
	}
	/* httpclient test
	private String sim2tradLyrics(String url){
		String lyrics = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://cdict.info/convert/g2b_url.php");
		ArrayList <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("url", url));//"music.baidu.com/data2/lrc/13839727/13839727.lrc"));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("httpEntity err");
		}
		
        try {
        	CloseableHttpResponse response2 = httpclient.execute(httpPost);
        	System.out.println("post");
        	System.out.println(response2.getStatusLine());
        	//response2.getEntity().getContent()
        	lyrics = translateLyrics(IOUtils.toString(response2.getEntity().getContent(), "UTF-8"));
			
        	response2.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return lyrics;
	}
	
	// get lrc link
	private String getLrcFile(String input) {
		String site = "http://music.baidu.com/";
		String link = "";
		String magic = "(.*?)down-lrc-btn(.*?)\'href\':\'(.*?)\'";//\\s{\\s\'href\':\'(.*?)\'\\s}";
		Pattern pat = Pattern.compile(magic, Pattern.DOTALL);
		Matcher mat = pat.matcher(input);

		if(mat.find()){
			//System.out.println(mat.group(3));
			link = site+mat.group(3);
		}else{
			System.out.println("not match");
		}
		return link;
	}
	
	//search lrc file
	private String searchLrcFile(String keyword) {
		String link="";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		keyword = keyword.replace(" ", "+");
		String site = "http://music.baidu.com/search/lrc?key=" + keyword;
		HttpGet httpGet = new HttpGet(site);
        try {
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			System.out.println(response1.getStatusLine());
			String content = IOUtils.toString(response1.getEntity().getContent());
            // do something useful with the response body
            // and ensure it is fully consumed
            //EntityUtils.consume(entity1);
			link = getLrcFile(content);
            response1.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return link;
	}
	*/
	/**
	 * Create the application.
	 * @throws InterruptedException 
	 */
	public JPlayer() throws InterruptedException {
		//fc.setDialogTitle("Choose a file to add...");
		fd.setDirectory(System.getProperty("user.dir"));
		
		
		draw();
		init();
		addListeners();

		//Thread.sleep(1500);
		//t1.schedule(tt1, 2000);
		//t1.schedule(tt1, 3000);
/*
		int cnt = 1;
		final ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //...Perform a task...
                System.out.println("a task...");
            }
            };
        Timer timer = new Timer( 0 , taskPerformer);
        timer.setRepeats(false);
        timer.start();
        //Thread.sleep(100);

        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/		
	}
	/**
	 * resize the icons
	 */
	private ImageIcon makeImageIcon(String path, int width, int height) {
		ImageIcon icon = new ImageIcon(JPlayer.class.getResource(path));
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
		btnPlay.addActionListener(this);
		btnPlay.setActionCommand("play");
		
		btnPause.addActionListener(this);
		btnPause.setActionCommand("pause");
		
		btnStop.addActionListener(this);
		btnStop.setActionCommand("stop");
		
		btnAdd.addActionListener(this);
		btnAdd.setActionCommand("add");

		btnRemove.addActionListener(this);
		btnRemove.setActionCommand("remove");

		btnEdit.addActionListener(this);
		btnEdit.setActionCommand("edit");
		
		btnEditDone.addActionListener(this);
		btnEditDone.setActionCommand("done");
		
		btnEffect.addActionListener(this);
		btnEffect.setActionCommand("effect");
		
		btnSearch.addActionListener(this);
		btnSearch.setActionCommand("search");
		
		txtSearch.getDocument().addDocumentListener(this);
		
		txtSearch.addFocusListener(this);
		
		sliderVol.setName("sliderVol");
		sliderVol.addChangeListener(this);
		
		sliderPlayBack.setName("sliderPlayBack");
		sliderPlayBack.addChangeListener(this);
		sliderPlayBack.addMouseListener(this);
		
		sliderBalance.setName("sliderBalance");
		sliderBalance.addChangeListener(this);
		
		sliderPan.setName("sliderPan");
		sliderPan.addChangeListener(this);
		
		frmJplayer.setName("jFrame");
		frmJplayer.addMouseListener(this);
		frmJplayer.addMouseMotionListener(this);
		
		btnExit.setName("exit");
		btnExit.addActionListener(this);
		btnExit.setActionCommand("exit");
		
		btnNext.setName("finished");
		btnNext.addActionListener(this);
		btnNext.setActionCommand("finished");
		
		btnPrev.setName("prev");
		btnPrev.addActionListener(this);
		btnPrev.setActionCommand("prev");
		
		tableSongList.setName("table");
		tableSongList.addMouseListener(this);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void draw() {
		//final MediaBtnController mbc = new MediaBtnController();
		
		frmJplayer = new JFrame();
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
		frmJplayer.setForeground(Color.WHITE);
		frmJplayer.setTitle("JPlayer");
		frmJplayer.setResizable(false);
		frmJplayer.getContentPane().setBackground(SystemColor.window);
		frmJplayer.setBounds(100, 100, 445, 395);
		frmJplayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJplayer.getContentPane().setLayout(null);
		
		//frmJplayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
		//frmJplayer.setExtendedState(JFrame.MAXIMIZED_BOTH); //make it maximize
		frmJplayer.setUndecorated(true); //make the title bar disappear
		
		String headers[] = { "Filename", "Title", "Singer", "Album", "Length" };
		TableModel tableModel = new DefaultTableModel(headers,0){
			public boolean isCellEditable(int row, int column)
		    {
		      return false;
		    }
		};
		
		JLabel lblSound0 = new JLabel(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Sound0.png")));
		//lblNewLabel.setIcon(makeImageIcon("/resources/sound0.png",32,32));//new ImageIcon(HelloWorldBuild.class.getResource("/resources/sound0.png")));
		lblSound0.setBounds(518, 112, 20, 20);
		frmJplayer.getContentPane().add(lblSound0);
		
		JLabel lblSound100 = new JLabel(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Sound100.png")));
		lblSound100.setHorizontalAlignment(SwingConstants.LEFT);
		lblSound0.setHorizontalAlignment(SwingConstants.LEFT);
		lblSound100.setBounds(550, 116, 20, 20);
		frmJplayer.getContentPane().add(lblSound100);
		
		JPanel panelAll = new JPanel();
		panelAll.setBounds(0, 0, 445, 395);
		frmJplayer.getContentPane().add(panelAll);
		panelAll.setLayout(null);
		
		JButton btnVideo = new JButton("video mode");
		btnVideo.setForeground(SystemColor.inactiveCaptionText);
		btnVideo.setFont(btnVideo.getFont().deriveFont(btnVideo.getFont().getStyle() | Font.ITALIC));
		btnVideo.setFocusable(false);
		btnVideo.setHorizontalTextPosition(SwingConstants.CENTER);
		btnVideo.setContentAreaFilled(false);
		btnVideo.setBorder(null);
		btnVideo.setOpaque(false);
		btnVideo.setContentAreaFilled(false);
		btnVideo.setBorderPainted(false);
		btnVideo.setOpaque(false);
		btnVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					video.HelloWorldBuild.main(null);
					frmJplayer.setVisible(false);
					try {
						db.save();
						System.out.println("database saved");
						PlayBackController.stopClicked(btnPlay, sliderPlayBack, btnPause, btnStop);
						frmJplayer.dispose();
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
				}
			}
		});
		btnVideo.setBounds(319, 2, 95, 20);
		panelAll.add(btnVideo);
		
		lblCurrTime = new JLabel("00:00");
		lblCurrTime.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
		lblCurrTime.setBounds(245, 145, 42, 16);
		panelAll.add(lblCurrTime);
		lblCurrTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrTime.setForeground(SystemColor.textText);
		
		sliderPlayBack = new JSlider();
		sliderPlayBack.setOpaque(false);
		sliderPlayBack.setBounds(8, 145, 240, 20);
		panelAll.add(sliderPlayBack);
		sliderPlayBack.setFocusable(false);
		sliderPlayBack.setMaximum(100001);
		sliderPlayBack.setValue(0);
		
		lblSongInfo = new JLabel("");
		lblSongInfo.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
		lblSongInfo.setBounds(10, 16, 272, 64);
		panelAll.add(lblSongInfo);
		lblSongInfo.setBackground(SystemColor.window);
		
		btnStop = new JButton(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Stop.png")));
		btnStop.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/Stop.png")));
		btnStop.setBounds(70, 112, 30, 30);
		panelAll.add(btnStop);
		btnStop.setFocusable(false);
		btnStop.setHorizontalTextPosition(SwingConstants.CENTER);
		btnStop.setContentAreaFilled(false);
		btnStop.setBorder(null);
		btnStop.setOpaque(false);
		btnStop.setContentAreaFilled(false);
		btnStop.setBorderPainted(false);
		
		btnPlay = new JButton(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Play.png")));
		btnPlay.setBounds(40, 110, 35, 35);
		panelAll.add(btnPlay);
		btnPlay.setFocusable(false);
		btnPlay.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/Play.png")));
		btnPlay.setBorder(null);
		btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
		btnPlay.setOpaque(false);
		btnPlay.setContentAreaFilled(false);
		btnPlay.setBorderPainted(false);
		
		sliderVol = new JSlider(JSlider.HORIZONTAL,0,100,100);
		sliderVol.setOpaque(false);
		sliderVol.setBounds(155, 115, 107, 29);
		panelAll.add(sliderVol);
		sliderVol.setFocusable(false);
		
		lblSound = new JLabel("");
		lblSound.setBounds(260, 119, 20, 20);
		panelAll.add(lblSound);
		lblSound.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Sound66.png")));
		
		btnExit = new JButton("");
		btnExit.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/exit-red.png")));
		btnExit.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/exit.png")));
		btnExit.setFont(new Font("Arial Unicode MS", Font.BOLD, 16));
		btnExit.setBounds(424, 2, 20, 20);
		panelAll.add(btnExit);
		btnExit.setFocusable(false);
		btnExit.setRequestFocusEnabled(false);
		btnExit.setBorder(null);	
		btnExit.setOpaque(false);
		btnExit.setContentAreaFilled(false);
		btnExit.setBorderPainted(false);
		btnExit.setForeground(SystemColor.inactiveCaptionText);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(8, 80, 270, 12);
		panelAll.add(separator);
		
		lblLyrics = new JLabel("");
		lblLyrics.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
		lblLyrics.setBounds(10, 90, 270, 20);
		panelAll.add(lblLyrics);
		lblLyrics.setForeground(SystemColor.textText);
		lblLyrics.setHorizontalAlignment(SwingConstants.CENTER);
		
		progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 40 , 60);
		progressBar.setBorderPainted(false);
		progressBar.setOpaque(false);
		progressBar.setBounds(10, 360, 329, 20);
		panelAll.add(progressBar);
		progressBar.setBorder(null);
		
		labelBarIcon = new JLabel("");
		labelBarIcon.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Bar.png")));
		labelBarIcon.setBounds(319, 360, 20, 20);
		panelAll.add(labelBarIcon);
		
		btnAdd = new JButton("");
		btnAdd.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/Add.png")));
		btnAdd.setBounds(350, 360, 20, 20);
		panelAll.add(btnAdd);
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
		btnRemove.setBounds(370, 360, 20, 20);
		panelAll.add(btnRemove);
		btnRemove.setFocusable(false);
		btnRemove.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Remove.png")));
		btnRemove.setBorder(null);
		btnRemove.setOpaque(false);
		btnRemove.setContentAreaFilled(false);
		btnRemove.setBorderPainted(false);
		
		btnEffect = new JButton("");
		btnEffect.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/Settings.png")));
		btnEffect.setBounds(390, 360, 20, 20);
		panelAll.add(btnEffect);
		btnEffect.setFocusable(false);
		btnEffect.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Settings.png")));
		btnEffect.setBorder(null);
		btnEffect.setOpaque(false);
		btnEffect.setContentAreaFilled(false);
		btnEffect.setBorderPainted(false);
		
		btnEdit = new JButton("");
		btnEdit.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/Edit.png")));
		btnEdit.setBounds(410, 360, 20, 20);
		panelAll.add(btnEdit);
		btnEdit.setFocusable(false);
		btnEdit.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Edit.png")));
		btnEdit.setBorder(null);
		btnEdit.setOpaque(false);
		btnEdit.setContentAreaFilled(false);
		btnEdit.setBorderPainted(false);
		
		btnEditDone = new JButton("");
		btnEditDone.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/List.png")));
		btnEditDone.setBounds(410, 360, 20, 20);
		panelAll.add(btnEditDone);
		btnEditDone.setFocusable(false);
		btnEditDone.setBorderPainted(false);
		btnEditDone.setBorder(null);
		btnEditDone.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/List.png")));
		btnEditDone.setActionCommand("done");
		btnEditDone.setOpaque(false);
		btnEditDone.setContentAreaFilled(false);
		btnEditDone.setBorderPainted(false);

		
		
		tableSongList = new JTable();
		tableSongList.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
		tableSongList.setBorder(null);
		tableSongList.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		tableSongList.setFocusable(false);
		tableSongList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSongList.setModel(tableModel);
		tableSongList.setBackground(Color.WHITE);
		tableSongList.setBounds(10, 443, 270, 129);
		//frmJplayer.getContentPane().add(tableSongList);
		//panelAll.add(tableSongList);
		
		panel = new JPanel();
		panel.setBounds(10, 168, 425, 187);
		panelAll.add(panel);
		panel.setBorder(new LineBorder(new Color(154, 154, 154)));
		panel.setBackground(SystemColor.control);
		panel.setVisible(false);
		panel.setLayout(null);
		
				txtNewTitle = new JTextField();
				txtNewTitle.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
				txtNewTitle.setBounds(104, 46, 285, 28);
				panel.add(txtNewTitle);
				txtNewTitle.setColumns(10);
				
				JLabel lblTitle = new JLabel("Title:");
				lblTitle.setFont(new Font("Arial Unicode MS", Font.BOLD, 14));
				lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
				lblTitle.setBounds(31, 50, 61, 16);
				panel.add(lblTitle);
				
				JLabel lblFilename = new JLabel("Filename:");
				lblFilename.setFont(new Font("Arial Unicode MS", Font.BOLD, 14));
				lblFilename.setForeground(SystemColor.inactiveCaptionText);
				lblFilename.setHorizontalAlignment(SwingConstants.RIGHT);
				lblFilename.setBounds(6, 12, 86, 16);
				panel.add(lblFilename);
				
				txtaFileName = new JTextArea();
				txtaFileName.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
				txtaFileName.setOpaque(false);
				txtaFileName.setBorder(null);
				txtaFileName.setBackground(SystemColor.window);
				txtaFileName.setEditable(false);
				txtaFileName.setForeground(SystemColor.inactiveCaptionText);
				txtaFileName.setWrapStyleWord(false);
				txtaFileName.setLineWrap(false);
				txtaFileName.setBounds(104, 12, 285, 18);
				panel.add(txtaFileName);
				
				txtNewAlbum = new JTextField();
				txtNewAlbum.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
				txtNewAlbum.setColumns(10);
				txtNewAlbum.setBounds(104, 115, 285, 28);
				panel.add(txtNewAlbum);
				
				JLabel lblSinger = new JLabel("Singer:");
				lblSinger.setHorizontalAlignment(SwingConstants.RIGHT);
				lblSinger.setFont(new Font("Arial Unicode MS", Font.BOLD, 14));
				lblSinger.setBounds(31, 83, 61, 23);
				panel.add(lblSinger);
				
				txtNewSinger = new JTextField();
				txtNewSinger.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
				txtNewSinger.setColumns(10);
				txtNewSinger.setBounds(104, 81, 285, 28);
				panel.add(txtNewSinger);
				
				JSeparator separator_1 = new JSeparator();
				separator_1.setBounds(6, 30, 413, 12);
				panel.add(separator_1);
				
				txtaFullpath = new JTextArea();
				txtaFullpath.setOpaque(false);
				txtaFullpath.setForeground(SystemColor.inactiveCaptionText);
				txtaFullpath.setEditable(false);
				txtaFullpath.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
				txtaFullpath.setBackground(SystemColor.window);
				txtaFullpath.setWrapStyleWord(true);
				txtaFullpath.setLineWrap(true);
				txtaFullpath.setBounds(33, 148, 356, 27);
				panel.add(txtaFullpath);
				
				JLabel labelAlbum = new JLabel("Album:");
				labelAlbum.setFont(new Font("Arial Unicode MS", Font.BOLD, 14));
				labelAlbum.setHorizontalAlignment(SwingConstants.RIGHT);
				labelAlbum.setBounds(31, 121, 61, 16);
				panel.add(labelAlbum);
				
				lblBg = new JLabel("bg");
				lblBg.setDoubleBuffered(true);
				lblBg.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/panel-bg.jpg")));
				lblBg.setBounds(0, 0, 425, 187);
				panel.add(lblBg);
				

				
				
		
		panelEffect = new JPanel();
		panelEffect.setBounds(10, 272, 425, 82);
		panelAll.add(panelEffect);
		panelEffect.setVisible(false);
		panelEffect.setBorder(new LineBorder(SystemColor.windowBorder));
		panelEffect.setBackground( SystemColor.control );
		panelEffect.setLayout(null);
		
		
		
		scrollPaneTable = new JScrollPane(tableSongList);
		scrollPaneTable.setBounds(10, 200, 425, 154);
		panelAll.add(scrollPaneTable);
		scrollPaneTable.setFocusable(false);
		
		JLabel lblSoundEffects = new JLabel("Effects");
		lblSoundEffects.setFont(new Font("Arial Unicode MS", Font.BOLD, 12));
		lblSoundEffects.setForeground(SystemColor.inactiveCaptionText);
		lblSoundEffects.setHorizontalTextPosition(SwingConstants.LEADING);
		lblSoundEffects.setBounds(20, 6, 51, 16);
		panelEffect.add(lblSoundEffects);
		
		JLabel lblBalance = new JLabel("Balance");
		lblBalance.setFont(new Font("Arial Unicode MS", Font.BOLD, 12));
		lblBalance.setBounds(20, 30, 61, 16);
		panelEffect.add(lblBalance);
		
		JLabel lblPan = new JLabel("Pan");
		lblPan.setFont(new Font("Arial Unicode MS", Font.BOLD, 12));
		lblPan.setBounds(20, 50, 61, 16);
		panelEffect.add(lblPan);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(6, 22, 413, 12);
		panelEffect.add(separator_2);
		
		sliderBalance = new JSlider();
		sliderBalance.setFocusable(false);
		sliderBalance.setBounds(74, 24, 345, 29);
		sliderBalance.setMinorTickSpacing(5);
		sliderBalance.setMajorTickSpacing(50);
		sliderBalance.setPaintTicks(false);
		sliderBalance.setSnapToTicks(true);
		panelEffect.add(sliderBalance);
		
		sliderPan = new JSlider();
		sliderPan.setFocusable(false);
		sliderPan.setBounds(74, 48, 345, 29);
		sliderPan.setMinorTickSpacing(5);
		sliderPan.setMajorTickSpacing(50);
		sliderPan.setPaintTicks(false);
		sliderPan.setSnapToTicks(true);
		panelEffect.add(sliderPan);
		
		lblL = new JLabel("L");
		lblL.setFont(new Font("Arial Unicode MS", Font.BOLD, 14));
		lblL.setForeground(SystemColor.inactiveCaptionText);
		lblL.setBounds(88, 6, 61, 16);
		panelEffect.add(lblL);
		
		lblR = new JLabel("R");
		lblR.setFont(new Font("Arial Unicode MS", Font.BOLD, 14));
		lblR.setBounds(397, 6, 61, 16);
		panelEffect.add(lblR);
		lblR.setForeground(SystemColor.inactiveCaptionText);
		
		label = new JLabel("|");
		label.setFont(new Font("Arial Unicode MS", Font.BOLD, 14));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(SystemColor.inactiveCaptionText);
		label.setBounds(217, 6, 60, 16);
		panelEffect.add(label);
		

				
				//btnPause = new JButton("Pause");
				btnPause = new JButton(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Pause.png")));
				btnPause.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/Pause.png")));
				btnPause.setBounds(40, 110, 35, 35);
				panelAll.add(btnPause);
				btnPause.setFocusable(false);
				btnPause.setVisible(false);
				btnPause.setBorder(null);
				btnPause.setHorizontalTextPosition(SwingConstants.CENTER);
				btnPause.setOpaque(false);
				btnPause.setContentAreaFilled(false);
				btnPause.setBorderPainted(false);
				
				btnNext = new JButton("");
				btnNext.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/Next.png")));
				btnNext.setFocusable(false);
				btnNext.setContentAreaFilled(false);
				btnNext.setBorder(null);
				btnNext.setBorderPainted(false);
				btnNext.setOpaque(false);
				btnNext.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Next.png")));
				btnNext.setBounds(105, 112, 30, 30);
				panelAll.add(btnNext);
				
				btnPrev = new JButton("");
				btnPrev.setRolloverIcon(new ImageIcon(JPlayer.class.getResource("/iconsOver/Prev.png")));
				btnPrev.setFocusable(false);
				btnPrev.setBounds(10, 112, 30, 30);
				btnPrev.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/Prev.png")));
				btnPrev.setContentAreaFilled(false);
				btnPrev.setBorder(null);
				btnPrev.setBorderPainted(false);
				btnPrev.setOpaque(false);
				panelAll.add(btnPrev);
				
				lblAlbumArt = new JLabel("");
				lblAlbumArt.setBounds(293, 21, 140, 140);
				panelAll.add(lblAlbumArt);
				lblAlbumArt.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/black-32390_150.png")));
				//lblAlbumArt.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/cd.gif")));
				lblAlbumArt.setBorder(null);
				lblAlbumArt.setHorizontalTextPosition(SwingConstants.CENTER);
				lblAlbumArt.setHorizontalAlignment(SwingConstants.CENTER);
		
				txtSearch = new JTextField();
				txtSearch.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
				txtSearch.setBounds(10, 170, 425, 28);
				panelAll.add(txtSearch);
				txtSearch.setColumns(10);
				
				btnSearch = new JButton(makeImageIcon("/iconsNormal/Search.png",20,20));
				btnSearch.setBounds(500, 133, 40, 28);
				panelAll.add(btnSearch);
				
				btnSearch.addActionListener(this);
				btnSearch.setHorizontalAlignment(SwingConstants.LEFT);
				btnSearch.setVerticalAlignment(SwingConstants.TOP);
				
				lblBackground = new JLabel("");
				lblBackground.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/bg.jpg")));
				lblBackground.setBounds(0, 0, 445, 395);
				panelAll.add(lblBackground);
				

		btnEditDone.setVisible(false);
		//System.out.println(sliderVol.getValue());
		//btnStop.setVisible(false);
		btnStop.setEnabled(false);
		
		
		lblEffectBg = new JLabel("bg");
		lblEffectBg.setIcon(new ImageIcon(JPlayer.class.getResource("/iconsNormal/panel-bg.jpg")));
		lblEffectBg.setBounds(1, 0, 423, 82);
		panelEffect.add(lblEffectBg);
		

		


		
		

	}
}
