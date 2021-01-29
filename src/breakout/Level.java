package breakout;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
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
  private static final int BALL_OFFSET = 10;
  private static final int STATUS_BAR_OUTLINE_OFFSET = 20;
  private static final int PADDLE_SPEED = 10;
  private static final int WIDTH = 300;
  private static final int HEIGHT = 400;
  private static final int STARTING_LIVES = 3;
  private static final int STARTING_LEVEL = 1;
  private static final double BALL_X_SPEED = 1;
  private static final double BALL_Y_SPEED = 2;
  private static final String PADDLE_IMAGE = "paddleImage.gif";
  private static final String BALL_IMAGE = "ballImage.gif";
  private static final Paint BACKGROUND = Color.PURPLE;


  private ImageView paddle = new ImageView(
      new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE)));
  private ImageView ball = new ImageView(
      new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE)));
  private HBox statusBar;
  private Rectangle statusBarOutline;
  Label levelLabel;
  Label scoreLabel;
  Label livesLabel;

  public static final String LEVEL1 = "level1.txt";
  public static final String LEVEL2 = "level2.txt";
  public static final String LEVEL3 = "level3.txt";
  private String[] levelPaths = {LEVEL1, LEVEL2, LEVEL3};

  private FileReader fileReader;

  private int ballLeftRight;
  private int ballUpDown = -1;
  private int level;
  private int score;
  private int lives;


  public Level() throws FileNotFoundException {
    this(LEVEL1);
  }

  public Level(String fileName) throws FileNotFoundException {
    this(fileName, STARTING_LEVEL, 0, STARTING_LIVES);
  }

  public Level(String fileName, int level, int score, int lives) throws FileNotFoundException {
    Scanner input = new Scanner(this.getClass().getClassLoader().getResourceAsStream(fileName));
    ballLeftRight = new Random().nextBoolean() ? -1 : 1;
    this.level = level;
    this.score = score;
    this.lives = lives;
  }





  public Scene setupGame(){
    Group root = new Group();

    paddleSetup();
    ballSetup();
    statusBarSetup();

    root.getChildren().add(paddle);
    root.getChildren().add(ball);
    root.getChildren().add(statusBarOutline);
    root.getChildren().add(statusBar);

    Scene scene = new Scene(root, WIDTH, HEIGHT, BACKGROUND);
    scene.setOnKeyPressed(event -> handleUserInput(event.getCode()));

    return scene;
  }

  private void paddleSetup() {
    paddle.setX(WIDTH/2 - paddle.getBoundsInLocal().getWidth()/2);
    paddle.setY(HEIGHT - PADDLE_OFFSET);
  }

  private void ballSetup() {
    ball.setX(WIDTH/2 - paddle.getBoundsInLocal().getWidth()/2);
    ball.setY(paddle.getY() - BALL_OFFSET);
  }

  private void statusBarSetup() {
    statusBar = new HBox();
    levelLabel = new Label("  Level: "+ level);
    scoreLabel = new Label("Score: "+ score);
    livesLabel = new Label("Lives: "+ lives);
    statusBar.getChildren().addAll(levelLabel, scoreLabel, livesLabel);
    statusBar.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));
    statusBar.setSpacing(WIDTH/4);
    statusBar.setLayoutY(0);
    statusBar.setLayoutX(0);
    statusBar.setMaxWidth(WIDTH);
    statusBar.setMinWidth(WIDTH);
    statusBarOutline = new Rectangle(0, 0, WIDTH, statusBar.getHeight() + STATUS_BAR_OUTLINE_OFFSET);
    statusBarOutline.setFill(Color.BLACK);
  }

  public void step(double stepTime){
    if(ball.getX() <= 0 || ball.getX() >= (WIDTH - ball.getBoundsInLocal().getWidth())){
      ballLeftRight *= -1;
    }

    if(ball.getBoundsInParent().intersects(statusBarOutline.getBoundsInParent())){
      ballUpDown *= -1;
    }
    if(ball.getY() >= (HEIGHT - ball.getBoundsInLocal().getHeight())){
     ballUpDown *= -1;
     lives -= 1;
     livesLabel.setText("Lives: "+ lives);
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
