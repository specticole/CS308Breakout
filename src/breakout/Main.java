package breakout;

import java.io.FileNotFoundException;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

/**
 * @author Cole Spector
 * This is the class with the main method, and starts the game
 * This class is dependant on Level.java
 * run this class within your preferred java engine
 *
 */
public class Main extends Application {


  public static final String TITLE = "Breakout";
  public static final int FRAMES_PER_SECOND = 60;
  public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;




  private Scene myScene;
  private Level level;


  /**
   * Start of the program.
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  /**
   * This method starts the game, and sets up the stage
   * This method is called with launch(args).
   * This method takes in a Stage as a parameter, which is used to create the game window.
   */
  public void start(Stage stage) throws FileNotFoundException {


    level = new Level();
    myScene = level.setupGame();
    stage.setScene(myScene);
    stage.setTitle(TITLE);
    stage.show();

    KeyFrame frame = new KeyFrame(Duration.seconds(SECOND_DELAY), event -> level.step(SECOND_DELAY));
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();


  }




}
