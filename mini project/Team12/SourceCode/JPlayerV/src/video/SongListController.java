package video;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


public class SongListController {
	/*
	private static File getFilePath(JFileChooser fc){
		File file = null;
		String filepath = "";
		// open file chooser
		int result = fc.showOpenDialog(null);
		if( result == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			filepath = file.getAbsolutePath();
			System.out.println("filepath: " + file.getAbsolutePath());
		}
		return file;
	}
	
	private static void addSongToList(JTable table, File file){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[]{file.getName(), "<>", "<>", "<>"});
	}
	
	public static void addSong(JTable table, JFileChooser fc){
		addSongToList(table,getFilePath(fc));
		System.out.println(getSelectedSongName(table));
	}
	
	public static String getSelectedSongName(JTable table){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		if(table.getRowCount()>0){
			return model.getValueAt(table.getSelectedRow(), 0).toString();
		}else
			return "";
		
	}
	*/
	private static JTable songList = null;
	private static SoundPlaylist soundPlaylist = null;
	
	public static File getFileFullPath(JFileChooser fc){
		File file = null;
		String filepath = "";
		// open file chooser
		int result = fc.showOpenDialog(null);
		if( result == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			filepath = file.getAbsolutePath();
			System.out.println("filepath: " + file.getAbsolutePath());
		}
		return file;
	}
	
	public static void addSong(JTable table, JFileChooser fc, SoundPlaylist playlist, String keyword) {
		File file;
		String filepath;
		int index;
		
		file = getFileFullPath(fc);
		if(file==null){
			System.out.println("no song selected");
			return;
		}else{
			filepath = file.getAbsolutePath();
		}
		try {	
			//SoundFile.create(filepath);
			index = getSelectedItemIndex(table);
//                        if(filepath.endsWith(".wav"))
			playlist.add(SoundFile.create(filepath));
			table.setModel(redrawTable(table, playlist.list(keyword)));
			if(index == -1) {
				index = 0;
			}
			table.setRowSelectionInterval(index,index);
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("err on addSong");
		}
		
	}
	
	public static void removeSong(JTable table, SoundPlaylist playlist, String keyword) {
		if(table.getRowCount() > 0) {
			int index = table.getSelectedRow();
			if(index!=-1) {
				SoundFile sound = playlist.list(keyword).get(index);
				playlist.remove(sound);
				
				table.setModel(redrawTable(table, playlist.list(keyword)));
				
				if(table.getRowCount()>index) {
					// keep same row
					table.setRowSelectionInterval(index, index);
				}else{
					// shift up 1 row if the most bottom row is removed
					if(table.getRowCount()>=1)
						table.setRowSelectionInterval(index-1, index-1);
				}
			}
		}
	}
	
	public static void filtersong(JTable table, SoundPlaylist playlist, String keyword) {
		
		ArrayList<SoundFile> list;
		if(keyword.isEmpty()){
			list = playlist.list();
		} else {
			list = playlist.list(keyword);
		}
		table.setModel(redrawTable(table, list));
		
		/*
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(sorter);
		
		RowFilter<DefaultTableModel, Object> rf = null;
	    //If current expression doesn't parse, don't update.
	    try {
	        rf = RowFilter.regexFilter(keyword, 0);
	    } catch (java.util.regex.PatternSyntaxException e) {
	        return;
	    }
	    sorter.setRowFilter(rf);
	    */
	}
	
	public static void initSongList(JTable table, SoundPlaylist playlist) {
		songList = table;
		soundPlaylist = playlist;
		
		table.setModel(redrawTable(table, playlist));
		if(table.getRowCount()>0 && getSelectedItemIndex(table)==-1	) {
			// keep same row
			table.setRowSelectionInterval(0, 0);
		}
	}
	
	public static void refreshSongList() {
		if(songList!=null && soundPlaylist!=null) {
			songList.setModel(redrawTable(songList,soundPlaylist));
		}
	}
	
	private static DefaultTableModel redrawTable(JTable table, SoundPlaylist playlist) {
		/*
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		for(SoundFile s: playlist.list()){
			//String filename = (s.getFilename().isEmpty())? "N/A" : s.getFilename();
			String title = (s.getTitle().isEmpty())? "N/A" : s.getTitle();
			String album = (s.getAlbum().isEmpty())? "N/A" : s.getAlbum();
			
			model.addRow(new Object[]{
				s.getFilename(),
				title,
				album,
				getHumanReadableTime((long) (s.getMicrosecondLength())),
			});
		}
		
		return model;
		*/
		return redrawTable(table, playlist.list());
	}
	private static DefaultTableModel redrawTable(JTable table, ArrayList<SoundFile> playlist) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		for(SoundFile s: playlist){
			//String filename = (s.getFilename().isEmpty())? "N/A" : s.getFilename();
			String title = (s.getTitle().isEmpty())? "N/A" : s.getTitle();
			String album = (s.getAlbum().isEmpty())? "N/A" : s.getAlbum();
			
			model.addRow(new Object[]{
				s.getFilename(),
				title,
				album,
				getHumanReadableTime((long) (s.getMicrosecondLength())),
			});
		}
		
		return model;
	}
	
	public static String getHumanReadableTime(long t) {
		return String.format("%02d:%02d", 
				TimeUnit.MICROSECONDS.toMinutes(t),
				TimeUnit.MICROSECONDS.toSeconds(t) - TimeUnit.MINUTES.toSeconds(TimeUnit.MICROSECONDS.toMinutes(t))
			);
	}
	
	public static int getSelectedItemIndex(JTable table) {
		return table.getSelectedRow();
	}
	
	public static void readSongInfoFromList(JTable table, JTextArea fullpath, JTextArea filename, JTextField title, JTextField album, SoundPlaylist playlist, String keyword) {
		ArrayList<SoundFile> sounds;
		SoundFile s;
		int index;
		
		if(keyword.length()>0) {
			sounds = playlist.list(keyword);
		}else{
			sounds = playlist.list();
		}
		
		index = getSelectedItemIndex(table);
		
		if(index >= 0) {
			System.out.println(sounds.get(index).getFilename() +";"+
					sounds.get(index).getTitle() +";"+
					sounds.get(index).getAlbum());
			filename.setText(sounds.get(index).getFilename());
			title.setText(sounds.get(index).getTitle());
			album.setText(sounds.get(index).getAlbum());
			fullpath.setText(sounds.get(index).getAbsoluteFilename());
			//sounds.get(index).getAbsoluteFilename();
		}
	}
	
	public static void writeSongInfoToList(JTable table, JTextArea fullpath, JTextArea filename, JTextField title, JTextField album, SoundPlaylist playlist, String keyword) {
		ArrayList<SoundFile> sounds;
		SoundFile s;
		int index;
		
		if(keyword.length()>0) {
			sounds = playlist.list(keyword);
		}else{
			sounds = playlist.list();
		}
		
		index = getSelectedItemIndex(table);
		
		if(index >= 0) {
			sounds.get(index).setTitle(title.getText());
			sounds.get(index).setAlbum(album.getText());
			redrawTable(table, sounds);
			
			table.setRowSelectionInterval(index, index);
			
			System.out.println(sounds.get(index).getFilename() +";"+
					sounds.get(index).getTitle() +";"+
					sounds.get(index).getAlbum());
			
			fullpath.setText("");
			filename.setText("");
			title.setText("");
			album.setText("");
			
		}
	}
}
