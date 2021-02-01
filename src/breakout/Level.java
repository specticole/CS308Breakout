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
  public static final int WON_GAME = 1;
  public static final int LOST_GAME = -1;
  public static final int START_GAME = 0;


  private int paddleXSpeed = 10;
  private int paddleYSpeed = 5;

  private ImageView paddle = new ImageView(
      new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE)));

  public static final String LEVEL1 = "level1.txt";
  public static final String LEVEL2 = "level2.txt";
  public static final String LEVEL3 = "level3.txt";
  public static final String EMPTYLEVEL = "emptyLevel.txt";

  private String[] levelPaths = {LEVEL1, LEVEL2, LEVEL3, EMPTYLEVEL};

  private StatusDisplay statusDisplay;
  private Ball startingBall;
  private Group root;
  private Powerup powerup;
  private boolean gameOver = true;

  private ArrayList<ArrayList<Brick>> allBricksOnScreen;
  private ArrayList<Ball> activeBalls;
  private Queue<Ball> powerupBallQueue;
  private SplashScreen splashScreen;


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
    splashScreen = new SplashScreen(root);
    splashScreen.showSplashScreen(START_GAME, statusDisplay);

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
    getBricksFromFile(levelPaths[statusDisplay.getLevel()]);
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

  private void getBricksFromFile(String fileName){
    Scanner input = new Scanner(this.getClass().getClassLoader().getResourceAsStream(fileName));

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


  public void step(double stepTime){

    if(!gameOver) {
      for (Ball ball : activeBalls) {
        switch (ball
            .move(WIDTH, HEIGHT, statusDisplay.getStatusBarHeight(), allBricksOnScreen, BRICK_ROWS,
                paddle, powerup, activeBalls)) {
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
  }

  private void lostLife(){

    if(statusDisplay.getLives() <= 0){
      gameOver(LOST_GAME);
    } else {
      statusDisplay.loseLife();
      powerup.clearPowerups();
      paddleSetup();
      startingBall.levelRefresh(paddle);
      activeBalls.add(startingBall);
      startingBall.levelStart();
    }

  }

  private void gameOver(int gameCondition){
    gameOver = true;
    clearActiveBalls();
    splashScreen.showSplashScreen(gameCondition, statusDisplay);
    goToLevel(0);
    statusDisplay.reset();
  }


  public void handleUserInput(KeyCode keyPressed) {

    //paddle movement
    if (keyPressed == KeyCode.RIGHT
        && (paddle.getX() + paddle.getBoundsInLocal().getWidth()) < WIDTH) {
      paddle.setX(paddle.getX() + paddleXSpeed);
    } else if (keyPressed == KeyCode.LEFT && paddle.getX() > 0) {
      paddle.setX(paddle.getX() - paddleXSpeed);
    } else if (keyPressed == KeyCode.UP && paddle.getY() >= HEIGHT - PADDLE_Y_BARRIER) {
      paddle.setY(paddle.getY() - paddleYSpeed);
    } else if (keyPressed == KeyCode.DOWN
        && (paddle.getY() + paddle.getBoundsInLocal().getHeight()) <= HEIGHT) {
      paddle.setY(paddle.getY() + paddleYSpeed);
    }

    //splash screen hide
    if (keyPressed == KeyCode.ENTER) {
      if (gameOver) {
        gameOver = false;
        splashScreen.hide();
      }
    }

    //cheats
    if (keyPressed == KeyCode.L) {
      statusDisplay.extraLife();
    } else if (keyPressed == KeyCode.R) {
      paddleSetup();
      clearActiveBalls();
      activeBalls.add(startingBall);
      startingBall.levelRefresh(paddle);
      startingBall.levelStart();
    } else if (keyPressed == KeyCode.DIGIT0) {
      goToLevel(0);
    } else if (keyPressed == KeyCode.DIGIT1) {
      goToLevel(1);
    } else if (keyPressed == KeyCode.DIGIT2
        || keyPressed == KeyCode.DIGIT3
        || keyPressed == KeyCode.DIGIT4
        || keyPressed == KeyCode.DIGIT5
        || keyPressed == KeyCode.DIGIT6
        || keyPressed == KeyCode.DIGIT7
        || keyPressed == KeyCode.DIGIT8
        || keyPressed == KeyCode.DIGIT9) {
    goToLevel(2);
    } else if(keyPressed == KeyCode.W) {
      paddleXSpeed += 4;
      paddleYSpeed += 2;
    } else if (keyPressed == KeyCode.S) {
      if(paddleYSpeed > 0) {
        paddleXSpeed -= 4;
        paddleYSpeed -= 2;
      }
    } else if (keyPressed == KeyCode.A) {
      for(Ball b : activeBalls) {
        b.lowerSpeed();
      }
    } else if (keyPressed == KeyCode.D) {
      for(Ball b : activeBalls) {
        b.upSpeed();
      }
    } else if (keyPressed == KeyCode.C) {
      getBricksFromFile(levelPaths[levelPaths.length-1]);
    } else if (keyPressed == KeyCode.P) {
      powerup.startDrop(WIDTH/2, 0);
    }
  }

  public void goToNextLevel(){
    goToLevel(statusDisplay.getLevel() + 1);
  }
  private void goToLevel(int level){
    if(level >= 3){
      gameOver(WON_GAME);
    } else {
      clearActiveBalls();
      powerup.clearPowerups();
      paddleSetup();
      startingBall.levelRefresh(paddle);
      statusDisplay.setLevel(level);
      getBricksFromFile(levelPaths[statusDisplay
            .getLevel()]);
      activeBalls.add(startingBall);
      startingBall.levelStart();
    }
  }

  private void clearActiveBalls(){
    for(Ball b : activeBalls){
      b.destroy();
    }
    activeBalls.clear();
  }



}
