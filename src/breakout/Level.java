package breakout;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.*;

public class Level {


  //private Paddle paddle;

  private static final int PADDLE_OFFSET = 10;
  private static final int BALL_OFFSET = 4;
  public static final int PADDLE_SPEED = 10;
  public static final int WIDTH = 300;
  public static final int HEIGHT = 400;
  public static final double BALL_X_SPEED = 1;
  public static final double BALL_Y_SPEED = 2;
  public static final String PADDLE_IMAGE = "paddleImage.gif";
  public static final String BALL_IMAGE = "ballImage.gif";
  public static final Paint BACKGROUND = Color.PURPLE;


  private ImageView paddle = new ImageView(
      new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE)));
  private ImageView ball = new ImageView(
      new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE)));

  private FileReader fileReader;

  private int ballLeftRight;
  private int ballUpDown = 1;

  public Level(String fileName) throws FileNotFoundException {
    Scanner input = new Scanner(this.getClass().getClassLoader().getResourceAsStream(fileName));
    ballLeftRight = new Random().nextBoolean() ? -1 : 1;
  }

  public Scene setupGame(){
    Group root = new Group();

    paddle.setX(WIDTH/2 - paddle.getBoundsInLocal().getWidth()/2);
    paddle.setY(HEIGHT - PADDLE_OFFSET);

    ball.setX(WIDTH/2 - paddle.getBoundsInLocal().getWidth()/2);
    ball.setY(HEIGHT - HEIGHT/BALL_OFFSET);

    root.getChildren().add(paddle);
    root.getChildren().add(ball);

    Scene scene = new Scene(root, WIDTH, HEIGHT, BACKGROUND);
    scene.setOnKeyPressed(event -> handleUserInput(event.getCode()));

    return scene;
  }

  public void step(double stepTime){
    if(ball.getX() <= 0 || ball.getX() >= (WIDTH - ball.getBoundsInLocal().getWidth())){
      ballLeftRight *= -1;
    }

    if(ball.getY() <= 0){
      ballUpDown *= -1;
    }
    if(ball.getY() >= (HEIGHT - ball.getBoundsInLocal().getHeight())){
     ballUpDown *= -1;
     //this is a seperate thing for now because if it hits the top later goes to next level
    }


    ball.setX(ball.getX() + (BALL_X_SPEED * ballLeftRight));
    ball.setY(ball.getY() + (BALL_Y_SPEED * ballUpDown));

    if(ball.getBoundsInParent().intersects(paddle.getBoundsInParent())){
      ballUpDown *= -1;

      if((ball.getX() + ball.getBoundsInLocal().getWidth()/2) < (paddle.getX() + paddle.getBoundsInLocal().getWidth()/4)){
        ballLeftRight = -1;
      }
      else if((ball.getX() + ball.getBoundsInLocal().getWidth()/2) > (paddle.getX() + (paddle.getBoundsInLocal().getWidth()/4)*3)){
        ballLeftRight = 1;
      }

    }

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
