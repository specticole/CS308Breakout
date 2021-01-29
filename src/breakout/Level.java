package breakout;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Level {


  //private Paddle paddle;

  private static final int PADDLE_OFFSET = 10;
  public static final int PADDLE_SPEED = 5;
  public static final int WIDTH = 300;
  public static final int HEIGHT = 400;
  public static final String PADDLE_IMAGE = "paddleImage.gif";
  public static final Paint BACKGROUND = Color.PURPLE;


  private ImageView paddle = new ImageView(
      new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE)));

  private FileReader fileReader;

  public Level(String path) throws FileNotFoundException {
    //fileReader = new FileReader(path);
  }

  public Scene setupGame(){
    Group root = new Group();
    //paddle = new Paddle();
    paddle.setX(WIDTH/2 - paddle.getBoundsInLocal().getWidth()/2);
    paddle.setY(HEIGHT - PADDLE_OFFSET);

    root.getChildren().add(paddle);

    Scene scene = new Scene(root, WIDTH, HEIGHT, BACKGROUND);
    scene.setOnKeyPressed(event -> handleUserInput(event.getCode()));

    return scene;
  }

  public void step(double stepTime){

  }

  public void handleUserInput(KeyCode keyPressed){
    if(keyPressed == KeyCode.RIGHT
      && (paddle.getX() + paddle.getBoundsInLocal().getWidth()) < WIDTH){
      paddle.setX(paddle.getX() + PADDLE_SPEED);
    } else if (keyPressed == KeyCode.LEFT
        && paddle.getX() > 0){
      paddle.setX(paddle.getX() - PADDLE_SPEED);
    }
  }



}
