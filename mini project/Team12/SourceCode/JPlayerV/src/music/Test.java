package music;


public class Test {
    public static void main(String[] args) throws Exception {
        SoundFile sound = SoundFile.create(args[0]);
        sound.play();
        /* Thread.sleep(2000); */
        /* sound.pause(); */
        /* Thread.sleep(2000); */
        /* sound.resume(); */
        /* Thread.sleep(2000); */
        /* sound.stop(); */
        /* Thread.sleep(2000); */
        /* sound.play(); */
        /* System.out.println("Filename: " + sound.getFilename()); */
        /* System.out.println("Title: " + sound.getTitle()); */
        /* sound.setTitle("title"); */
        /* System.out.println("Title Set: " + sound.getTitle()); */
        /* System.out.println("Album: " + sound.getAlbum()); */
        /* sound.setAlbum("album"); */
        /* System.out.println("Album Set: " + sound.getAlbum()); */
        /* System.out.println("Microsecond length: " + sound.getMicrosecondLength()); */
        /* Thread.sleep(2000); */
        /* System.out.println("Microsecond position: " + sound.getMicrosecondPosition()); */
        /* sound.setMicrosecondPosition(0); */
        /* System.out.println("Microsecond position Set: " + sound.getMicrosecondPosition()); */
        /* System.out.println("Level: " + sound.getLevel()); */
        /* System.out.println("MaxMasterGain: " + sound.getMaxMasterGain()); */
        /* System.out.println("MinMasterGain: " + sound.getMinMasterGain()); */
        /* System.out.println("MasterGain: " + sound.getMasterGain()); */
        /* Thread.sleep(3000); */
        /* sound.setMasterGain(6); */
        /* System.out.println("MasterGain Set: " + sound.getMasterGain()); */
        /* System.out.println("Max Balance: " + sound.getMaxBalance()); */
        /* System.out.println("Min Balance: " + sound.getMinBalance()); */
        /* System.out.println("Balance: " + sound.getBalance()); */
        /* Thread.sleep(3000); */
        /* sound.setBalance(1); */
        /* System.out.println("Balance Set: " + sound.getBalance()); */
        /* System.out.println("Max Pan: " + sound.getMaxPan()); */
        /* System.out.println("Min Pan: " + sound.getMinPan()); */
        /* System.out.println("Pan: " + sound.getPan()); */
        /* Thread.sleep(3000); */
        /* sound.setPan(-1); */
        /* System.out.println("Pan Set: " + sound.getPan()); */
        /* ArrayList<LyricsLine> lines = sound.getLyricsLines(); */
        /* for (LyricsLine line : lines) { */
        /*     System.out.println(line.getMicrosecondLength() + " " + line.getText()); */
        /* } */

        /* SoundPlaylist soundPlaylist = new SoundPlaylist(); */
        /* soundPlaylist.add(SoundFile.create("1.wav")); */
        /* soundPlaylist.add(SoundFile.create("2.wav")); */
        /* ArrayList<SoundFile> list = soundPlaylist.list(); */
        /* ArrayList<SoundFile> listKeyword = soundPlaylist.list("2"); */
        /* System.out.println("list: " + list.size()); */
        /* System.out.println("listKeyword: " + listKeyword.size()); */

        /* Database database = Database.load(); */
        /* SoundPlaylist soundPlaylist = database.getSounds(); */
        /* soundPlaylist.add(SoundFile.create("1.wav")); */
        /* soundPlaylist.add(SoundFile.create("2.wav")); */
        /* database.save(); */

        /* database = Database.load(); */
        /* soundPlaylist = database.getSounds(); */
        /* System.out.println("restore: " + soundPlaylist.list().size()); */
        /* soundPlaylist.list().get(0).play(); */

        Thread.sleep(1000000);
    }
}
