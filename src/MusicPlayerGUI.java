import javafx.application.Application;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Observer;

/**
 * TODO COMMENT
 *
 * @author connoryork (cxy1054@rit.edu)
 */
public class MusicPlayerGUI extends Application implements Observer {

    private static final int DEFAULT_PADDING = 10;
    private static final int DEFAULT_SPACING = 5;

    private static final int MIN_VOLUME = 0;
    private static final int MAX_VOLUME = 100;

    /**
     * Launches the GUI
     *
     * @param args not used
     */
    
    public static void main(String[] args) {
        launch(args);
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
    }

    /*******************************************************
    *                                                      *
    *  HELPER METHODS FOR CONSTRUCTING GUI                 *
    *                                                      *
    *******************************************************/

    /**
     * Builds the root node for the GUI
     *
     * @return BorderPane Node
     */
    private BorderPane buildRoot() {
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(DEFAULT_PADDING));
        bp.setCenter(buildCenter());
        bp.setBottom(buildVolumeSlider());
        return bp;
    }

    /**
     *
     * @return center buttons in a HBox
     */
    private HBox buildCenter() {
        HBox box = new HBox();
        box.setSpacing(DEFAULT_SPACING);
        box.setPadding(new Insets(DEFAULT_PADDING));
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
        return rewind;
    }

    /**
     * Builds play button, which pauses and plays the song.
     *
     * @return play ToggleButton
     */
    private ToggleButton buildPlayPause() {
        ToggleButton play = new ToggleButton();
        return play;
    }

    /**
     * Builds next button, which skips the current song.
     *
     * @return next Button
     */
    private Button buildNext() {
        Button next = new Button();
        return next;
    }

    private Slider buildVolumeSlider() {
        Slider slider = new Slider(MIN_VOLUME, MAX_VOLUME, 50); // replace starting value with current volume
        return slider;
    }

    /**
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(java.util.Observable o, Object arg) {

    }
}
