import javafx.application.Application;
import javafx.beans.Observable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Observer;

/**
 * TODO COMMENT
 *
 * @author connoryork (cxy1054@rit.edu)
 */
public class MusicPlayerGUI extends Application implements Observer {

    /**
     * Launches the GUI
     *
     * @param args not used
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // create main pane
        BorderPane bp = new BorderPane();

        // create bottom control buttons
        HBox bot = new HBox();
        bot.setAlignment(Pos.CENTER);

        // rewind button
        Button rewind = new Button();
        bot.getChildren().add(rewind);

        // play/pause button
        Button play = new Button();
        bot.getChildren().add(play);

        // next song button
        Button next = new Button();
        bot.getChildren().add(next);

        bp.setBottom(bot);

        Scene s = new Scene(bp);
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Music Player");
        primaryStage.show();
    }

    @Override
    public void update(java.util.Observable o, Object arg) {

    }
}
