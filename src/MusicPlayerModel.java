import javax.sound.sampled.*;
import java.util.Observable;
/**
 *
 *
 * @author mbroman (broman334@tamu.edu)
 */
public class MusicPlayerModel extends Observable {

    /**
     * PRIVATE DATA MEMBERS
     */
    private Clip clip;
    private AudioInputStream audioStream;
    private AudioInputStream decodedStream;
    private AudioFormat baseFormat;
    private AudioFormat decodeFormat;

    /********************************************************
    *                                                       *
    *      CONSTRUCTORS                                     *
    *                                                       *
    ********************************************************/

    public MusicPlayerModel() {
        this.clip = null;
        this.audioStream = null;
        this.decodedStream = null;
        this.baseFormat = null;
        this.decodeFormat = null;
    }

    /**
     * Constuctor that loads a song
     *
     * @param song name of the song as a string intended to be played
     */
    public MusicPlayerModel(String song) {
        try {

            this.audioStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(song));
            this.baseFormat = audioStream.getFormat();
            this.decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );

            this.decodedStream = AudioSystem.getAudioInputStream(decodeFormat, audioStream);
            this.clip = AudioSystem.getClip();
            this.clip.open(decodedStream);
            this.clip.setFramePosition(0);

        } catch (Exception e) {
            System.out.println("Failed to load audio.");
        }
    }

    /********************************************************
    *                                                       *
    *    HELPER METHODS FOR CONSTRUCTING MUSIC PLAYER       *
    *                                                       *
    ********************************************************/

    /**
     * changes the song loaded onto the clip
     *
     * @param song
     */
    public void changeSong(String song) {
        try {

            this.audioStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(song));
            this.baseFormat = audioStream.getFormat();
            this.decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );

            this.decodedStream = AudioSystem.getAudioInputStream(decodeFormat, audioStream);
            this.clip = AudioSystem.getClip();
            this.clip.open(decodedStream);
            this.clip.setFramePosition(0);

        } catch (Exception e) {
            System.out.println("Failed to load audio.");
        }

    }

    /**
     * starts the song from its current position
     */
    public void start() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
    }

    /**
     * pauses the clip;
     */
    public void stop() {
        if (this.clip.isRunning() && this.clip != null) {
            this.clip.stop();
        }
    }

    /**
     * changes the clips loudness
     *
     * @param decibels decibels desired by the user
     */
    public void volumeChange(double decibels) {
        FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (decibels == ((int)getMaxVolume() + (int)getMinVolume())/2) {
            gainControl.setValue((float)this.getMinVolume());
        }
        else
            gainControl.setValue((float)decibels);
        System.out.println(gainControl.getValue());
    }

    /**
     * rewinds the clip to the start
     */
    public void rewindToStart() {
        if(this.clip.isRunning() && this.clip != null) {
            this.clip.stop();
            this.clip.setFramePosition(0);
            this.clip.start();
        }
    }

    /**
     *
     */
    public void setSongPosition(int changed) {
        if(this.clip.isRunning() && this.clip != null) {
            this.clip.stop();
            this.clip.setFramePosition(this.clip.getFramePosition() - changed);
            this.clip.start();
        }
    }


    public double getMinVolume() {
        FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        return gainControl.getMinimum();
    }

    public double getMaxVolume() {
        FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        return gainControl.getMaximum();
    }

    /**
     * returns the state of the music player.
     *
     * @return clip.isRunning()
     */
    public boolean isRunning() {
        return this.clip.isRunning();
    }
}
