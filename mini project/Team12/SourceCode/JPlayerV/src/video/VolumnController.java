package video;

public class VolumnController {
	public static void adjustVolumn(SoundFile s, int factor){
		if(s!=null) {	
			float scale = s.getMaxMasterGain() - s.getMinMasterGain();
			float vol = (float) (scale*((float) factor/100.0)+s.getMinMasterGain());
			s.setMasterGain(vol);
			System.out.println("Volumn= "+s.getMasterGain());
		}
	}
}
