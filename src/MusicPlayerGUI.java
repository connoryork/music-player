import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
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

    private static final int MIN_VOLUME = 0;
    private static final int MAX_VOLUME = 100;

    private MusicPlayerModel model;

    /**
     * Launches the GUI
     *
     * @param args not used
     */
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        Parameters params = getParameters();
        this.model = new MusicPlayerModel();
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
        //primaryStage.setX((screen.getWidth() - primaryStage.getWidth()) / 2);
        //primaryStage.setY((screen.getHeight() - primaryStage.getHeight()) / 4);
        primaryStage.setX(screen.getWidth() - primaryStage.getWidth());
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
     * Builds the root node for the GUI
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
     * Builds HBox and corresponding buttons
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
        rewind.setOnAction(e -> {
            // TODO
        });
        return rewind;
    }

    /**
     * Builds play button, which pauses and plays the song.
     *
     * @return play ToggleButton
     */
    private ToggleButton buildPlayPause() {
        ToggleButton play = new ToggleButton();
        setImage(play, "play.png");
        play.setOnAction(e -> {
            if (play.isSelected()) {
                setImage(play, "pause.png");
                model.start();
            } else {
                setImage(play, "play.png");
                model.stop();
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

    private void setImage(ButtonBase b, String filename) {
        Image image = new Image(getClass().getResourceAsStream("resources/" + filename));
        b.setGraphic(new ImageView(image));
    }

    private Slider buildVolumeSlider() {
        Slider slider = new Slider(MIN_VOLUME, MAX_VOLUME, 50); // replace starting value with current volume
        slider.setOrientation(Orientation.VERTICAL);
        slider.setPadding(new Insets(DEFAULT_PADDING));
        slider.setMaxHeight(DEFAULT_SLIDER_HEIGHT);
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
