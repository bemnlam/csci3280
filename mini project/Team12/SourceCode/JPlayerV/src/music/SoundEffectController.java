package music;

public class SoundEffectController {
	public static void adjustVolumn(SoundFile s, int factor){
		if(s!=null) {	
			float scale = s.getMaxMasterGain() - s.getMinMasterGain();
			float vol = (float) (scale*((float) factor/100.0)+s.getMinMasterGain());
			s.setMasterGain(vol);
			System.out.println("Volumn= "+s.getMasterGain());
		}
	}
	
	public static void adjustPan(SoundFile s, int factor){
		if(s!=null) {
			float range = s.getMaxPan() - s.getMinPan();
			float pan = (float) (range * ((float) factor/100.0)) + s.getMinPan();
			s.setPan(pan);
			System.out.println("Pan = "+s.getPan());
		}
	}
	
	public static void adjustBalance(SoundFile s, int factor){
		if(s!=null) {
			
			float range = s.getMaxBalance() - s.getMinBalance();
			float balance = (float) (range * ((float) factor/100.0)) + s.getMinBalance();
			s.setBalance(balance);
			//System.out.println("MinBalance = "+s.getMinBalance());
			System.out.println("Balance = "+s.getBalance());
			//System.out.println("MaxBalance = "+s.getMaxBalance());
		}
	}
}
