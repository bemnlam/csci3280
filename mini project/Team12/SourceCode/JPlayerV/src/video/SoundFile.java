package video;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public abstract class SoundFile implements Serializable {
    protected String path;
    protected String title;
    protected String album;
    protected transient Clip clip;

    public static SoundFile create(String path) throws Exception {
        if (path.endsWith(".wav"))
            return new WavFile(path);
        else
            return new NonWavFile(path);
    }

    public SoundFile(String path) {
        this.path = path;
        this.title = "";
        this.album = "";
    }

    /* public abstract int getLevel(); */
    public abstract int getMaxLevel();
    public abstract int getMinLevel();
    public abstract int getLevel();

    public String getFilename() {
        return new File(path).getName();
    }
    
    /* added by ben */
    public String getAbsoluteFilename() {
        return new File(path).getAbsolutePath();
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getMicrosecondLength() {
        return clip.getMicrosecondLength();
    }

    public long getMicrosecondPosition() {
        return clip.getMicrosecondPosition();
    }

    public void setMicrosecondPosition(long position) {
        clip.setMicrosecondPosition(position);
    }

    private FloatControl getControl(FloatControl.Type type) {
        return (FloatControl) clip.getControl(type);
    }

    public float getMaxMasterGain() {
        return getControl(FloatControl.Type.MASTER_GAIN).getMaximum();
    }

    public float getMinMasterGain() {
        return getControl(FloatControl.Type.MASTER_GAIN).getMinimum();
    }

    public float getMasterGain() {
        return getControl(FloatControl.Type.MASTER_GAIN).getValue();
    }

    public void setMasterGain(float value) {
        getControl(FloatControl.Type.MASTER_GAIN).setValue(value);
    }

    public float getMaxBalance() {
        return getControl(FloatControl.Type.BALANCE).getMaximum();
    }

    public float getMinBalance() {
        return getControl(FloatControl.Type.BALANCE).getMinimum();
    }

    public float getBalance() {
        return getControl(FloatControl.Type.BALANCE).getValue();
    }

    public void setBalance(float value) {
        getControl(FloatControl.Type.BALANCE).setValue(value);
    }

    public float getMaxPan() {
        return getControl(FloatControl.Type.PAN).getMaximum();
    }

    public float getMinPan() {
        return getControl(FloatControl.Type.PAN).getMinimum();
    }

    public float getPan() {
        return getControl(FloatControl.Type.PAN).getValue();
    }

    public void setPan(float value) {
        getControl(FloatControl.Type.PAN).setValue(value);
    }
    
    /* added by ben */
    public ArrayList<String> getSoundInfoFromLyric() throws IOException {
    	BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream(path.replace(".wav", ".lrc")), "UTF-8"));
    	Pattern pattern = Pattern.compile("\\[(.+):(.+)\\]");
    	ArrayList<String> soundInfo = new ArrayList<String>(); //[title, album]
    	String line;
    	soundInfo.add("");
    	soundInfo.add("");
    	
    	while( (line = stream.readLine()) != null) {
    		Matcher matcher = pattern.matcher(line);
    		
    		if(matcher.matches()) {
    			String key = matcher.group(1);
    			String value = matcher.group(2);
    			if("ti".equals(key)) { soundInfo.set(0, value); System.out.println("key= "+key+" ;value=" + value);};
    			if("al".equals(key)) { soundInfo.set(1, value); System.out.println("key= "+key+" ;value=" + value);};
    		}
    		
    	}
    	
    	for(String str: soundInfo) {
			System.out.println("value=" + str);
		}
    	
    	stream.close();
		return soundInfo;
    }
    
    public ArrayList<LyricsLine> getLyricsLines() throws Exception {
        //BufferedReader stream = new BufferedReader(new FileReader(path.replace(".wav", ".lrc")));
        /* fixed: allow opening Unicode lrc file*/
    	BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream(path.replace(".wav", ".lrc")), "UTF-8"));
        
    	Pattern pattern = Pattern.compile("\\[(.+):(.+)\\.(.+)\\](.?|.+)"); // fixed: handle empty line
        ArrayList<LyricsLine> lyricsLines = new ArrayList<LyricsLine>();
        String line;
        long prevTime = 0;

        while ((line = stream.readLine()) != null) {
        	// debug
        	//System.out.println("{" + line + "}");
        	
            Matcher matcher = pattern.matcher(line);

            if (matcher.matches()) {
                String mm = matcher.group(1);
                String ss = matcher.group(2);
                String xx = matcher.group(3);
                
                //fixed: handle emptyline
                String text = null;
                try {
                	text = matcher.group(4);
                } catch(Exception e) {
                	//System.out.println("emptyLine");
                	text = "";
                }
                
                long time = 0;

                /* in microsecond */
                time += Long.parseLong(mm) * 60 * 1000000;
                time += Long.parseLong(ss) * 1000000;
                time += Long.parseLong(xx) * 10000;
                
                /* in millisecond
                time += Long.parseLong(mm) * 60 * 1000;
                time += Long.parseLong(ss) * 1000;
                time += Long.parseLong(xx) * 10;
				*/
				
                // lyricsLines.add(new LyricsLine(time - prevTime, text));
                lyricsLines.add(new LyricsLine(time, text)); //convert into microsec only
                prevTime = time;
            }
        }

        stream.close();
        return lyricsLines;
    }

    public void play() {
        clip.start();
    }

    public void stop() {
        clip.stop();
        clip.setMicrosecondPosition(0);
    }

    public void pause() {
        clip.stop();
    }

    public void resume() {
        clip.start();
    }

    public boolean match(String keyword) {
        return getFilename().contains(keyword) || title.contains(keyword) || album.contains(keyword);
    }
}
