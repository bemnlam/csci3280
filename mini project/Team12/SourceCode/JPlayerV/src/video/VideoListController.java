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


public class VideoListController {

	private static JTable clipList = null;
        private static VideoPlaylist clipPlaylist = null;
	
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
	
	public static void addClip(JTable table, VideoPlaylist playlist, String keyword) {
		int index;

		if(keyword==null){
			System.out.println("no song selected");
			return;
		}try {	
			index = getSelectedItemIndex(table);
			playlist.add(keyword);
			table.setModel(redrawTable(table, playlist.list()));
			if(index == -1) {
				index = 0;
			}
            else
                index += 1;
            System.out.println("adding "+index);
			table.setRowSelectionInterval(index,index);
		} catch (Exception e) {
			System.out.println("err on addSong");
		}
		
	}
	
	public static void removeSong(JTable table, VideoPlaylist playlist, String keyword) {
		if(table.getRowCount() > 0) {
			int index = table.getSelectedRow();
			if(index!=-1) {
				String path = playlist.list(keyword).get(index);
				playlist.remove(path);
				
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
	
	public static void filtersong(JTable table, VideoPlaylist playlist, String keyword) {
		ArrayList<String> list;
		if(keyword.isEmpty()){
			list = playlist.list();
		} else {
			list = playlist.list(keyword);
		}
		table.setModel(redrawTable(table, list));
	}
	
	public static void initSongList(JTable table, VideoPlaylist playlist) {
		clipList = table;
		clipPlaylist = playlist;
		
		table.setModel(redrawTable(table, playlist));
		if(table.getRowCount()>0 && getSelectedItemIndex(table)==-1	) {
			// keep same row
			table.setRowSelectionInterval(0, 0);
		}
	}
	
	public static void refreshSongList() {
		if(clipList!=null && clipPlaylist!=null) {
			clipList.setModel(redrawTable(clipList,clipPlaylist));
		}
	}
	
	private static DefaultTableModel redrawTable(JTable table, VideoPlaylist playlist) {
		return redrawTable(table, playlist.list());
	}
	private static DefaultTableModel redrawTable(JTable table, ArrayList<String> playlist) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		for(String s: playlist){
			File video = new File(s);
			String title = (video.getName().isEmpty())? "N?A" : video.getName();
			String path = (s.isEmpty())? "N/A" : s;
			
			model.addRow(new Object[]{
				title,
                path,
                getHumanReadableTime((long)0),
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
}
