import javax.sound.sampled.*;
import java.io.File;
import java.util.List;
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
    private List<File> playlist;
    private int playlistPosition;

    /********************************************************
    *                                                       *
    *      CONSTRUCTORS                                     *
    *                                                       *
    ********************************************************/

    /**
     * Constructor for the model. Essentially sets up the model with everything set to null.
     */
    public MusicPlayerModel() {
        this.clip = null;
        this.audioStream = null;
        this.decodedStream = null;
        this.baseFormat = null;
        this.decodeFormat = null;
        this.playlist = null;
    }

    /**
     * Changes the song loaded onto the clip.
     *
     * @param song TODO
     */
    public void changeSong(String song) { //TODO make so it can load from outside resources
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
        } finally {
            announceChanges();
        }
    }

    /**
     * Loads the next song in the playlist, if possible.
     *
     * @return TODO
     */
    public File loadNextSong() {
        if (this.playlist != null) {
            this.changeSong("resources/" + this.playlist.get(playlistPosition).getName());
            File song = this.playlist.get(playlistPosition);
            this.playlistPosition++;
            if (this.playlistPosition >= this.playlist.size())
                this.playlistPosition = 0;
            return song;
        }
        return null;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    public File loadPrevSong() {
        if (this.playlist != null) {
            this.playlistPosition--;
            if (this.playlistPosition < 0)
                this.playlistPosition = this.playlist.size() - 1;
            this.changeSong("resources/" + this.playlist.get(playlistPosition).getName());
            return this.playlist.get(playlistPosition);
        }
        return null;
    }

    /**
     * Starts the song from its current position.
     */
    public void start() {
        if (this.hasClip() && !this.clip.isRunning()) {
            this.clip.start();
        }
    }

    /**
     * Pauses the clip.
     */
    public void stop() {
        if (this.hasClip() && this.clip.isRunning()) {
            this.clip.stop();
        }
    }

    /**
     * Changes the clips loudness.
     *
     * @param decibels decibels desired by the user
     */
    public void volumeChange(double decibels) {
        if (this.hasClip()) {
            FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (decibels == ((int) getMaxVolume() + (int) getMinVolume()) / 2) {
                gainControl.setValue((float) this.getMinVolume());
            } else
                gainControl.setValue((float) decibels);
        }
    }

    /**
     * Rewinds the clip to the start.
     */
    public void rewindToStart() {
        if (this.hasClip()) {
            boolean prevRun = this.clip.isRunning();
            this.clip.stop();
            this.clip.setFramePosition(0);
            if(prevRun) {
                this.clip.start();
            }
        }
    }

    /**
     * Sets the clip's position to the new value.
     *
     * @param position frame position to set song at (0 < position < this.clip.getFrameLength())
     */
    public void setSongPosition(int position) {
        if (this.hasClip()) {
            boolean prevRun = this.clip.isRunning();
            this.clip.stop();
            this.clip.setFramePosition(position);
            if (prevRun) {
                this.clip.start();
            }
        }
    }

    /**
     * Sets the playlist to a new list.
     *
     * @param playlist list of Files to set the new playlist as
     */
    public void setPlaylist(List<File> playlist) {
        this.playlist = playlist;
        this.playlistPosition = 0;
    }

    /**
     * Gets the minimum decibel volume of the clip.
     * Implies that there is a current song stored in this.clip.
     *
     * @return min decibel volume of the current clip
     */
    public double getMinVolume() {
        FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        return gainControl.getMinimum();
    }

    /**
     * Gets the maximum decibel volume of the clip.
     * Implies that there is a current song stored in this.clip.
     *
     * @return max decibel volume of the current clip
     */
    public double getMaxVolume() {
        FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        return gainControl.getMaximum();
    }

    /**
     * Gets the total length of the current clip.
     * Implies that there is a current song stored in this.clip.
     *
     * @return length of the current clip
     */
    public int getClipLength() {
        return this.clip.getFrameLength();
    }

    /**
     * Gets the current position of the song.
     * Implies that there is a current song stored in this.clip.
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
        return this.clip.getFramePosition() >= this.clip.getFrameLength() - 100;
    }

    /**
     * Returns the state of the music player.
     *
     * @return true if song is playing, false otherwise
     */
    public boolean isRunning() {
        return this.hasClip() && (this.clip.isRunning() || this.atEnd());
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
     * TODO
     * @return
     */
    public boolean hasPlaylist() {
        return this.playlist != null;
    }

    /**
     * Utility function to notify GUI that changes were made with the model's instance variables.
     */
    public void announceChanges() {
        setChanged();
        notifyObservers();
    }
}