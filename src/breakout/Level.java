package breakout;

import java.io.FileNotFoundException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.*;

public class Level {

  public static final int PADDLE_OFFSET = 10;
  public static final int BALL_OFFSET = 10;
  public static final int STATUS_BAR_OUTLINE_OFFSET = 20;
  public static final int PADDLE_X_SPEED = 10;
  public static final int PADDLE_Y_SPEED = 5;
  public static final int WIDTH = 300;
  public static final int HEIGHT = 400;
  public static final int STARTING_LIVES = 3;
  public static final int STARTING_LEVEL = 0;
  public static final int BRICK_ROWS = 7;
  public static final int BRICK_COLS = 16;
  public static final int BRICK_OFFSET = 30;
  public static final int BRICKS_PER_LEVEL = 112;
  public static final int PADDLE_Y_BARRIER = 50;
  public static final int MAX_BALLS = 1000;
  public static final String PADDLE_IMAGE = "paddleImage.gif";
  public static final Paint BACKGROUND = Color.PURPLE;
  public static final int BALL_IN_PLAY = 0;
  public static final int BALL_LOST = -1;
  public static final int BALL_WON = 1;
  public static final int MAX_LEVEL = 3;



  private ImageView paddle = new ImageView(
      new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE)));

  public static final String LEVEL1 = "level1.txt";
  public static final String LEVEL2 = "level2.txt";
  public static final String LEVEL3 = "level3.txt";
  private String[] levelPaths = {LEVEL1, LEVEL2, LEVEL3};

  private StatusDisplay statusDisplay;
  private Ball startingBall;
  private Group root;
  private Powerup powerup;

  private ArrayList<ArrayList<Brick>> allBricksOnScreen;
  private ArrayList<Ball> activeBalls;
  private Queue<Ball> powerupBallQueue;


  public Level() throws FileNotFoundException {
    allBricksOnScreen = new ArrayList<>();
    activeBalls = new ArrayList<>();
    powerupBallQueue = new LinkedList<>();
  }





  public Scene setupGame(){
    root = new Group();
    powerup = new Powerup();
    powerup.setup(root);
    statusDisplay = new StatusDisplay(root);
    statusDisplay.setup(WIDTH);
    brickLayer();
    paddleSetup();
    root.getChildren().add(paddle);
    startingBall = new Ball(root, statusDisplay);
    activeBalls.add(startingBall);
    startingBall.setup(WIDTH/2 - paddle.getBoundsInLocal().getWidth()/2, paddle.getY() - BALL_OFFSET, true);
    fillPowerupBallQueue();

    Scene scene = new Scene(root, WIDTH, HEIGHT, BACKGROUND);
    scene.setOnKeyPressed(event -> handleUserInput(event.getCode()));
    return scene;
  }

  private void fillPowerupBallQueue(){
    for (int i = 0; i < MAX_BALLS; i++){
      Ball powerupBall = new Ball(root, statusDisplay);
      powerupBall.setup(0, 0, false);
      powerupBallQueue.add(powerupBall);
    }
  }

  private void brickLayer(){
    setupAllBricksOnScreen();
    getBricksFromFile();
    putBricksOnScreen();
  }

  private void putBricksOnScreen(){
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


  private void setupAllBricksOnScreen(){
    for(int row = 0; row < BRICK_ROWS; row++) {
      allBricksOnScreen.add(new ArrayList<Brick>());
      for (int col = 0; col < BRICK_COLS; col++) {
        allBricksOnScreen.get(row).add(new Brick(root));

      }
    }
  }

  private void getBricksFromFile(){
    Scanner input = new Scanner(this.getClass().getClassLoader().getResourceAsStream(levelPaths[statusDisplay
        .getLevel()]));

    for(int row = 0; row < BRICK_ROWS; row++){
      String brickRow = input.next();
      for(int col = 0; col < BRICK_COLS; col++){
          allBricksOnScreen.get(row).get(col).setBrickProperties(brickRow.charAt(col));
      }
    }
  }

  private void paddleSetup() {
    paddle.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE)));
    paddle.setX(WIDTH/2 - paddle.getBoundsInLocal().getWidth()/2);
    paddle.setY(HEIGHT - PADDLE_OFFSET);
  }

  private void paddleRefresh(){

  }

  public void step(double stepTime){


    for(Ball ball : activeBalls){
      switch (ball.move(WIDTH, HEIGHT, statusDisplay.getStatusBarHeight(), allBricksOnScreen, BRICK_ROWS, paddle, powerup, activeBalls)){
        case BALL_LOST:
          lostLife();
          break;
        case BALL_WON:
          goToNextLevel();
          break;
      }
    }

    powerup.move(paddle, activeBalls, powerupBallQueue, allBricksOnScreen, BRICK_ROWS);

  }

  private void lostLife(){
    statusDisplay.loseLife();
  }


  public void handleUserInput(KeyCode keyPressed){
    if(keyPressed == KeyCode.RIGHT && (paddle.getX() + paddle.getBoundsInLocal().getWidth()) < WIDTH){
      paddle.setX(paddle.getX() + PADDLE_X_SPEED);
    } else if (keyPressed == KeyCode.LEFT && paddle.getX() > 0){
      paddle.setX(paddle.getX() - PADDLE_X_SPEED);
    } else if (keyPressed == KeyCode.UP && paddle.getY() >= HEIGHT - PADDLE_Y_BARRIER){
      paddle.setY(paddle.getY() - PADDLE_Y_SPEED);
    } else if (keyPressed == KeyCode.DOWN && (paddle.getY() + paddle.getBoundsInLocal().getHeight()) <= HEIGHT){
      paddle.setY(paddle.getY() + PADDLE_Y_SPEED);
    }

  }


  public void goToNextLevel(){

    clearActiveBalls();
    paddleSetup();
    startingBall.levelRefresh(paddle);
    statusDisplay.nextLevel();
    if(!(statusDisplay.getLevel() > MAX_LEVEL)){
      getBricksFromFile();
    }
    activeBalls.add(startingBall);
    startingBall.levelStart();

  }

  private void clearActiveBalls(){
    for(Ball b : activeBalls){
      b.destroy();
    }
    activeBalls.clear();
  }



}
