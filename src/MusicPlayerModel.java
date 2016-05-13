import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 *
 * @author mbroman (broman334@tamu.edu)
 */
public class MusicPlayerModel {

   private Clip clip;

    //Audio Magic, We get a stream from a file name and decode it. the we turn it into a clip.
    public MusicPlayerModel(String song) {
        try {

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(song));
            AudioFormat baseFormat = audioStream.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );

            AudioInputStream decodedStream = AudioSystem.getAudioInputStream(decodeFormat, audioStream);
            clip = AudioSystem.getClip();
            clip.open(decodedStream);


        } catch (Exception e) {
            System.out.println("Failed to load audio.");
        }
    }
     //play starts the music player
    public void play() {
        if (clip == null) { return; }
        stop();
        clip.setFramePosition(0);
        clip.start();
    }

    //stop stops the music player
    public void stop() { if (clip.isRunning()) clip.stop(); }

    //a pesudo delete
    public void close() {
        stop();
        clip.close();
    }
}
