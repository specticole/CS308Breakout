package breakout;

import java.io.FileNotFoundException;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.*;
import javafx.scene.text.Text;

public class Level {

  private static final int PADDLE_OFFSET = 10;
  private static final int BALL_OFFSET = 10;
  private static final int STATUS_BAR_OUTLINE_OFFSET = 20;
  private static final int PADDLE_SPEED = 10;
  private static final int WIDTH = 300;
  private static final int HEIGHT = 400;
  private static final int STARTING_LIVES = 3;
  private static final int STARTING_LEVEL = 0;
  private static final int BRICK_ROWS = 7;
  private static final int BRICK_COLS = 16;
  private static final int BRICK_OFFSET = 30;
  private static final int BRICKS_PER_LEVEL = 112;

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
  Text levelText;
  Text scoreText;
  Text livesText;

  public static final String LEVEL1 = "level1.txt";
  public static final String LEVEL2 = "level2.txt";
  public static final String LEVEL3 = "level3.txt";
  private String[] levelPaths = {LEVEL1, LEVEL2, LEVEL3};

  private ArrayList<ArrayList<Brick>> allBricksOnScreen;

  private int ballLeftRight;
  private int ballUpDown = -1;
  private int level;
  private int score;
  private int lives;


  public Level() throws FileNotFoundException {
    this(STARTING_LEVEL, 0, STARTING_LIVES);
  }

  public Level(int level, int score, int lives) throws FileNotFoundException {
    ballLeftRight = new Random().nextBoolean() ? -1 : 1;
    this.level = level;
    this.score = score;
    this.lives = lives;
    allBricksOnScreen = new ArrayList<>();
    getBricksFromFile();
  }





  public Scene setupGame(){
    Group root = new Group();

    paddleSetup();
    ballSetup();
    statusBarSetup();


    putBricksOnScreen(root);
    root.getChildren().add(paddle);
    root.getChildren().add(ball);
    root.getChildren().add(statusBarOutline);
    root.getChildren().add(levelText);
    root.getChildren().add(scoreText);
    root.getChildren().add(livesText);


    Scene scene = new Scene(root, WIDTH, HEIGHT, BACKGROUND);
    scene.setOnKeyPressed(event -> handleUserInput(event.getCode()));

    return scene;
  }

  private void putBricksOnScreen(Group root){
    //root.getChildren().remove(0, BRICKS_PER_LEVEL);
    for(int row = 0; row < BRICK_ROWS; row++){
      for(int col = 0; col < BRICK_COLS; col++){
        Brick b = allBricksOnScreen.get(row).get(col);
        double x = col * b.getImageView().getBoundsInParent().getWidth();
        double y = BRICK_OFFSET + (row * b.getImageView().getBoundsInParent().getHeight());
        b.setXY(x,y);
        root.getChildren().add(b.getImageView());
      }
    }
  }

  private void getBricksFromFile(){
    Scanner input = new Scanner(this.getClass().getClassLoader().getResourceAsStream(levelPaths[level]));
    allBricksOnScreen.clear();

    for(int row = 0; row < BRICK_ROWS; row++){
      allBricksOnScreen.add(new ArrayList<Brick>());
      String brickRow = input.next();
      for(int col = 0; col < BRICK_COLS; col++){
        allBricksOnScreen.get(row).add(new Brick(brickRow.charAt(col)));
      }
    }
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
    //statusBar = new HBox();
    levelText = new Text("  Level: "+ level);
    textSetup(levelText, 0);
    scoreText = new Text("Score: "+ score);
    textSetup(scoreText, WIDTH/2 - scoreText.getBoundsInParent().getWidth()/2);
    livesText = new Text("Lives: "+ lives);
    textSetup(livesText, WIDTH- scoreText.getBoundsInParent().getWidth());
    //statusBar.getChildren().addAll(levelLabel, scoreLabel, livesLabel);
    //statusBar.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));
    //statusBar.setSpacing(WIDTH/5);
    //statusBar.setLayoutY(0);
    //statusBar.setLayoutX(0);
    //statusBar.setMaxWidth(WIDTH);
    statusBarOutline = new Rectangle(0, 0, WIDTH, 20);
    statusBarOutline.setFill(Color.BLACK);

  }

  public void textSetup(Text text, double x){
    text.setX(x);
    text.setY(15);
    text.setFill(Color.GREEN);
    text.setStroke(Color.GREEN);
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
     livesText.setText("Lives: "+ lives);
     //this is a seperate thing for now because if it hits the top later goes to next level
    }




    if(ball.getBoundsInParent().intersects(paddle.getBoundsInParent())){
      ballUpDown *= -1;

      if((ball.getX() + ball.getBoundsInLocal().getWidth()/2) < (paddle.getX() + paddle.getBoundsInLocal().getWidth()/4)){
        ballLeftRight = -1;
      }
      else if((ball.getX() + ball.getBoundsInLocal().getWidth()/2) > (paddle.getX() + (paddle.getBoundsInLocal().getWidth()/4)*3)){
        ballLeftRight = 1;
      }

    }

    for(int row = 0; row < BRICK_ROWS; row++){
      for(Brick brick : allBricksOnScreen.get(row)){
        if(brick.inPlay()){
          if(ball.getBoundsInParent().intersects(brick.getImageView().getBoundsInParent())){

            score += brick.isHit();
            scoreText.setText("Score: " + score);

            ballLeftRight *= -1;
            ballUpDown *= -1;


            if(((ball.getY() + ball.getBoundsInLocal().getHeight()/2) <= (brick.getImageView().getY() + brick.getImageView().getBoundsInLocal().getHeight()))
                &&
                ((ball.getY() + ball.getBoundsInLocal().getHeight()/2) >= (brick.getImageView().getY()))){
              ballUpDown *= -1;
            }

            if(((ball.getX() + ball.getBoundsInLocal().getWidth()/2) >= brick.getImageView().getX())
                &&
                ((ball.getX() + ball.getBoundsInLocal().getWidth()/2) <=  (brick.getImageView().getX() + brick.getImageView().getBoundsInLocal().getWidth()))){
              //brick is hit on the left side
              ballLeftRight *= -1;
            }


            break;

          }
        }

      }
    }

    ball.setX(ball.getX() + (BALL_X_SPEED * ballLeftRight));
    ball.setY(ball.getY() + (BALL_Y_SPEED * ballUpDown));

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
