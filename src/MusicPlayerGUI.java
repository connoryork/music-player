import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.Observer;

/**
 * View/Controller in the Model-View-Controller design pattern for MusicPlayer.
 * Deals with GUI construction, event handling, and updating the GUI.
 *
 * @author connoryork (cxy1054@rit.edu)
 */
public class MusicPlayerGUI extends Application implements Observer {

    /** CONSTANTS FOR GUI DESIGN AND LAYOUT */
    private static final int DEFAULT_PADDING = 5;
    private static final int DEFAULT_SPACING = 10;
    private static final int DEFAULT_SLIDER_HEIGHT = 80;

    /** CONSTANTS TO A SONG FOR EASY UPDATING OF SLIDER */
    private static int MIN_VOLUME;
    private static int MAX_VOLUME;

    /** Model for easy access */
    private MusicPlayerModel model;

    /** Play/Pause button for easy access */
    private Button play;
    /** Volume Slider for easy access */
    private Slider volumeSlider;
    /** Song Slider for easy access */
    private Slider songSlider;

    /**
     * Launches the GUI.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the model and sets the GUI as an observer.
     */
    @Override
    public void init() {
        this.model = new MusicPlayerModel();
        this.model.addObserver(this);
    }

    /**
     * JavaFX start method. Builds and displays the GUI.
     *
     * @param primaryStage stage to build GUI on
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene s = new Scene(buildRoot(primaryStage));
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.getIcons().add(new Image("resources/musicnotelarge.png"));
        primaryStage.setTitle("No Song Selected ~ MusicPlayer");
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(screen.getWidth()/2);
        System.out.println(screen.getHeight());
        System.out.println(primaryStage.getHeight());
        primaryStage.setY(screen.getHeight() - primaryStage.getHeight());
    }

    /*******************************************************
    *                                                      *
    *  HELPER METHODS FOR CONSTRUCTING GUI                 *
    *                                                      *
    *******************************************************/

    /**
     * Builds the root node for the GUI.
     *
     * @return BorderPane Node
     */
    private BorderPane buildRoot(Stage stage) {
        BorderPane bp = new BorderPane();
        bp.setPrefSize(300, 80);
        bp.setCenter(buildCenter());
        bp.setRight(buildVolumeSlider());
        bp.setTop(buildMenuBar(stage));
        bp.setBottom(buildSongSlider());
        return bp;
    }

    /**
     * Builds HBox and corresponding buttons.
     *
     * @return center buttons in a HBox
     */
    private HBox buildCenter() {
        HBox box = new HBox();
        box.setSpacing(DEFAULT_SPACING);
        box.setPadding(new Insets(DEFAULT_PADDING,DEFAULT_PADDING,0,DEFAULT_PADDING));
        box.setAlignment(Pos.CENTER);
        box.getChildren().add(buildRewind());
        box.getChildren().add(buildPlayPause());
        box.getChildren().add(buildNext());
        return box;
    }

    /**
     * Builds rewind button, which restarts the current song.
     *
     * @return rewind Button
     */
    private Button buildRewind() {
        Button rewind = new Button();
        setImage(rewind, "rewind.png");
        rewind.setOnAction(e -> this.model.rewindToStart());
        return rewind;
    }

    /**
     * Builds play button, which pauses and plays the song.
     *
     * @return play Button
     */
    private Button buildPlayPause() {
        Button play = new Button();
        setImage(play, "play.png");
        play.setOnAction(e -> {
            if (this.model.hasClip()) {
                if (!this.model.isRunning()) {
                    if (this.model.atEnd()) {
                        this.model.setSongPosition(0);
                        setImage(play, "pause.png");
                        this.model.start();
                    }
                    setImage(play, "pause.png");
                    this.model.start();
                } else {
                    setImage(play, "play.png");
                    this.model.stop();
                }
            } else {
                setImage(play, "play.png");
            }
        });
        this.play = play;
        return play;
    }

    /**
     * Builds next button, which skips the current song.
     *
     * @return next Button
     */
    private Button buildNext() {
        Button next = new Button();
        setImage(next, "fastforward.png");
        next.setOnAction(e -> {
            // TODO play next song
        });
        return next;
    }

    /**
     * Sets the image of the Button.
     *
     * @param b Button object
     * @param filename filename of image
     */
    private void setImage(ButtonBase b, String filename) {
        Image image = new Image(getClass().getResourceAsStream("resources/" + filename));
        b.setGraphic(new ImageView(image));
    }

    /**
     * Builds a slider that controls the volume of the GUI.
     *
     * @return Slider which controls volume
     */
    private Slider buildVolumeSlider() {
        Slider slider = new Slider(0, 0, 0);
        slider.setOrientation(Orientation.VERTICAL);
        slider.setPadding(new Insets(DEFAULT_PADDING,DEFAULT_PADDING,0,DEFAULT_PADDING));
        slider.setMaxHeight(DEFAULT_SLIDER_HEIGHT);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.model.volumeChange(newValue.doubleValue());
        });
        this.volumeSlider = slider;
        return slider;
    }

    /**
     * Builds the menu bar, which allows picking of songs.
     *
     * @param stage GUI's stage
     * @return MenuBar object to add to the stage
     */
    private MenuBar buildMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem choose = new MenuItem("Choose Song",
                new ImageView(new Image("resources/musicnote.png")));
        choose.setOnAction(e -> {
            FileChooser songChooser = new FileChooser();
            File newSong = songChooser.showOpenDialog(stage);
            if (newSong != null) {
                if (this.model.hasClip() && this.model.isRunning())
                    this.model.stop();
                this.model.changeSong("resources/" + newSong.getName());
                stage.setTitle(newSong.getName() + " ~ MusicPlayer");
                MIN_VOLUME = (int) this.model.getMinVolume();
                MAX_VOLUME = (int) this.model.getMaxVolume();
                // update volume slider
                int half = (MAX_VOLUME + MIN_VOLUME)/2;
                this.volumeSlider.setMax(MAX_VOLUME);
                this.volumeSlider.setMin(half);
                this.volumeSlider.setValue((MAX_VOLUME + half)/2);
                // update song slider
                this.songSlider.setMax(this.model.getClipLength());
                this.songSlider.setMin(0);
                this.songSlider.setValue(0);
            }
        });
        menuFile.getItems().add(choose);
        menuBar.getMenus().addAll(menuFile);
        return menuBar;
    }

    private Slider buildSongSlider() {
        Slider songSlider = new Slider(0,0,0);
        this.songSlider = songSlider;
        songSlider.setPadding(new Insets(DEFAULT_PADDING));
        songSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            this.model.setSongPosition(newValue.intValue());
        }));
        return songSlider;
    }

    /**
     * Updates GUI based on changes in the model
     *
     * @param o not used
     * @param arg not used
     */
    @Override
    public void update(java.util.Observable o, Object arg) {
        // make sure play button is in sync
        if (this.model.isRunning())
            setImage(this.play, "pause.png");
        else
            setImage(this.play, "play.png");
        // update slider based on current song position
        this.songSlider.setValue(this.model.getClipCurrentValue());
    }
}
