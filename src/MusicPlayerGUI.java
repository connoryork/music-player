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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Observer;

/**
 * TODO COMMENT
 *
 * @author connoryork (cxy1054@rit.edu)
 */
public class MusicPlayerGUI extends Application implements Observer {

    private static final int DEFAULT_PADDING = 5;
    private static final int DEFAULT_SPACING = 10;
    private static final int DEFAULT_SLIDER_HEIGHT = 80;
    private static final int DEFAULT_WINDOW_HEIGHT = 0;
    private static final int DEFAULT_WINDOW_WIDTH = 0;

    private static int MIN_VOLUME;
    private static int MAX_VOLUME;

    private MusicPlayerModel model;

    /**
     * Launches the GUI.
     *
     * @param args not used
     */
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        Parameters params = getParameters();
        this.model = new MusicPlayerModel("resources/" + params.getRaw().get(0));
        MIN_VOLUME = (int) this.model.getMinVolume();
        MAX_VOLUME = (int) this.model.getMaxVolume();
    }

    /**
     * JavaFX start method. Builds and displays the GUI.
     *
     * @param primaryStage stage to build GUI on
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene s = new Scene(buildRoot());
        primaryStage.initStyle(StageStyle.UTILITY);
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
    private BorderPane buildRoot() {
        BorderPane bp = new BorderPane();
        bp.setPrefSize(350, 80);
        bp.setPadding(new Insets(DEFAULT_PADDING));
        bp.setCenter(buildCenter());
        bp.setRight(buildVolumeSlider());
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
        box.setPadding(new Insets(DEFAULT_PADDING));
        box.setAlignment(Pos.TOP_CENTER);
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
            if (!this.model.isRunning()) {
                setImage(play, "pause.png");
                this.model.start();
            } else {
                setImage(play, "play.png");
                this.model.stop();
            }
        });
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
        int half = (MAX_VOLUME + MIN_VOLUME)/2;
        Slider slider = new Slider(half, MAX_VOLUME, (MAX_VOLUME + half)/2);
        slider.setOrientation(Orientation.VERTICAL);
        slider.setPadding(new Insets(DEFAULT_PADDING));
        slider.setMaxHeight(DEFAULT_SLIDER_HEIGHT);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.model.volumeChange(newValue.doubleValue());
        });
        return slider;
    }

    //private MenuBar

    /**
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(java.util.Observable o, Object arg) {

    }
}
