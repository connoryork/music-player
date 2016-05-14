import javax.sound.sampled.*;
import java.util.Observable;
/**
 * TODO
 *
 * @author connoryork (cxy1054@rit.edu)
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

    /**
     * TODO
     */
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
     * Changes the song loaded onto the clip.
     *
     * @param song TODO
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
        announceChanges();
    }

    /**
     * Starts the song from its current position.
     */
    public void start() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
    }

    /**
     * Pauses the clip.
     */
    public void stop() {
        if (this.clip.isRunning() && this.clip != null) {
            this.clip.stop();
        }
    }

    /**
     * Changes the clips loudness.
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
    }

    /**
     * Rewinds the clip to the start.
     */
    public void rewindToStart() {
        if(this.clip != null) {
            boolean prevRun = this.clip.isRunning();
            this.clip.stop();
            this.clip.setFramePosition(0);
            if(prevRun) {
                this.clip.start();
            }
        }
    }

    /**
     * TODO
     *
     * @param position frame position to set song at (0 < position < this.clip.getFrameLength())
     */
    public void setSongPosition(int position) {
        if(this.clip != null) {
            boolean prevRun = this.clip.isRunning();
            this.clip.stop();
            System.out.println(this.clip.getFramePosition());
            this.clip.setFramePosition(position);
            System.out.println(this.clip.getFramePosition());
            if (prevRun) {
                this.clip.start();
            }
        }
    }

    /**
     * Gets the minimum decibel volume of the clip.
     *
     * @return min decibel volume of the current clip
     */
    public double getMinVolume() {
        FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        return gainControl.getMinimum();
    }

    /**
     * Gets the maximum decibel volume of the clip.
     *
     * @return max decibel volume of the current clip
     */
    public double getMaxVolume() {
        FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        return gainControl.getMaximum();
    }

    /**
     * Gets the total length of the current clip.
     *
     * @return length of the current clip
     */
    public int getClipLength() {
        return this.clip.getFrameLength();
    }

    /**
     * Gets the current position of the song.
     *
     * @return the current integer position of the song
     */
    public int getClipCurrentValue() {
        return this.clip.getFramePosition();
    }

    /**
     * Checks if the song is at the end.
     *
     * @return true if at the end, false otherwise
     */
    public boolean atEnd() {
        return this.clip.getFramePosition() == this.clip.getFrameLength();
    }

    /**
     * Returns the state of the music player.
     *
     * @return true if song is playing, false otherwise
     */
    public boolean isRunning() {
        return (this.clip.isRunning() || this.atEnd());
    }

    /**
     * Returns if the music player has a current song (clip).
     *
     * @return true if song exists, false otherwise
     */
    public boolean hasClip() {
        return this.clip != null;
    }

    /**
     * Utility function to notify GUI that changes were made with the model's instance variables.
     */
    private void announceChanges() {
        setChanged();
        notifyObservers();
    }

    /**
     * Periodically notifies the GUI to update based on changes in the model.
     */
    private class Updater extends Thread {

        /** Model for easy access */
        private MusicPlayerModel model;

        /**
         * Contructor for Updater, which starts the thread.
         *
         * @param model MusicPlayerModel which thread is constantly updating
         */
        public Updater(MusicPlayerModel model) {
            this.model = model;
            this.start();
        }

        /**
         * Waits a millisecond, and then notifies the GUI to update.
         * This happens until the song is at the end.
         */
        @Override
        public void run() {
            while (this.model.clip.getFramePosition() != this.model.clip.getFrameLength()) {
                try {
                    wait(1);
                } catch (InterruptedException ie) {
                    System.out.println(ie.getMessage());
                }
                this.model.announceChanges();
            }
        }
    }
}