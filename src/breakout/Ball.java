package breakout;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Cole Spector
 * This class is used to create and manuver a ball within the breakout game
 * This class is dependant on Level.java for the paddle, and StatusDisplay.java
 * In order to use this class, call the setup method, and then call the move method within each frame.
 *
 */
public class Ball {
  public static final String BALL_IMAGE_NAME = "ballImage.gif";

  public static final int POWERUP_BALL_OFFSET = 10;
  public static final int BALL_IN_PLAY = 0;
  public static final int BALL_LOST = -1;
  public static final int BALL_WON = 1;


  private final ImageView BALL_IMAGE  = new ImageView(new Image(
      this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE_NAME)));
  private int ballLeftRight;
  private int ballUpDown = -1;
  private Group root;
  private StatusDisplay statusDisplay;
  private boolean inPlay;
  private double ballXSpeed = 1;
  private double ballYSpeed = 2;

  /**
   * This is the initializer method
   * This method will be called when a Ball is initialized
   * @param root this is the Group to which javaFX objects should be added to too appear on screen
   * @param statusDisplay This is the StatusDisplay used in Level.java
   */

  public Ball(Group root, StatusDisplay statusDisplay){
    ballLeftRight = new Random().nextBoolean() ? -1 : 1;
    this.statusDisplay = statusDisplay;
    this.root = root;
  }

  /**
   * This method is used to setup each ball, and to add it to the root Group.
   * @param x this is the x position to place the ball at
   * @param y this is the y position to place the ball at
   * @param enabled this is whether or not the ball is in play (or will be used as a powerup later on)
   */

  public void setup(double x, double y, boolean enabled){
    BALL_IMAGE.setX(x);
    BALL_IMAGE.setY(y);
    inPlay = enabled;
    if(!inPlay){
      BALL_IMAGE.setOpacity(0);
    }
    root.getChildren().add(BALL_IMAGE);
  }

  /**
   * This method is used to move the Ball
   * This method should be called within each frame
   * @param screenWidth this is the width of the game window in pixels
   * @param screenHeight this is the height of the game window in pixels
   * @param top this is the offset from the top of the screen to be the heighest point the ball can go
   * @param allBricksOnScreen this is a list of all the bricks on the screen
   * @param brickRows this is the number of rows of bricks
   * @param paddle this is an ImageView of the paddle from level.java
   * @param powerup this is the powerup initialized in level.java
   * @param activeBalls this is a list off all the current balls in play
   * @return this method returns whether or not the ball hit the top of the screen and won (1), hit the
   * bottom of the screen and lost (-1), or is still in play (0).
   */
  public int move(int screenWidth, int screenHeight, int top, ArrayList<ArrayList<Brick>> allBricksOnScreen, int brickRows, ImageView paddle, Powerup powerup, ArrayList<Ball> activeBalls) {
    if (inPlay) {
      if (BALL_IMAGE.getX() <= 0 || BALL_IMAGE.getX() >= (screenWidth - BALL_IMAGE
          .getBoundsInLocal().getWidth())) {
        ballLeftRight *= -1;
      }

      if (BALL_IMAGE.getY() <= top) {
        return BALL_WON;
      }
      if (BALL_IMAGE.getY() >= (screenHeight - BALL_IMAGE.getBoundsInLocal().getHeight())) {
        ballUpDown *= -1;
        if(ballHitFloor(activeBalls)){
          return BALL_LOST;
        }
        //this is a seperate thing for now because if it hits the top later goes to next level
      }

      if (BALL_IMAGE.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
        ballUpDown *= -1;

        if ((BALL_IMAGE.getX() + BALL_IMAGE.getBoundsInLocal().getWidth() / 2) < (paddle.getX()
            + paddle.getBoundsInLocal().getWidth() / 4)) {
          ballLeftRight = -1;
        } else if ((BALL_IMAGE.getX() + BALL_IMAGE.getBoundsInLocal().getWidth() / 2) > (
            paddle.getX() + (paddle.getBoundsInLocal().getWidth() / 4) * 3)) {
          ballLeftRight = 1;
        }

      }

      for (int row = 0; row < brickRows; row++) {
        for (Brick brick : allBricksOnScreen.get(row)) {
          if (brick.inPlay()) {
            if (BALL_IMAGE.getBoundsInParent()
                .intersects(brick.getImageView().getBoundsInParent())) {

              statusDisplay.updateScore(brick.isHit(powerup, BALL_IMAGE.getX(), BALL_IMAGE.getY()));

              ballLeftRight *= -1;
              ballUpDown *= -1;

              if (((BALL_IMAGE.getY() + BALL_IMAGE.getBoundsInLocal().getHeight() / 2) <= (
                  brick.getImageView().getY() + brick.getImageView().getBoundsInLocal()
                      .getHeight()))
                  &&
                  ((BALL_IMAGE.getY() + BALL_IMAGE.getBoundsInLocal().getHeight() / 2) >= (brick
                      .getImageView().getY()))) {
                ballUpDown *= -1;
              }

              if (((BALL_IMAGE.getX() + BALL_IMAGE.getBoundsInLocal().getWidth() / 2) >= brick
                  .getImageView().getX())
                  &&
                  ((BALL_IMAGE.getX() + BALL_IMAGE.getBoundsInLocal().getWidth() / 2) <= (
                      brick.getImageView().getX() + brick.getImageView().getBoundsInLocal()
                          .getWidth()))) {
                //brick is hit on the left side
                ballLeftRight *= -1;
              }

              break;

            }
          }

        }
      }
      BALL_IMAGE.setX(BALL_IMAGE.getX() + (ballXSpeed * ballLeftRight));
      BALL_IMAGE.setY(BALL_IMAGE.getY() + (ballYSpeed * ballUpDown));

    }
    return BALL_IN_PLAY;
  }

  /**
   * This method activates a powerup ball
   * @param paddle this is the paddle from level.java
   * @param activeBalls this is the list of currently active balls
   */
  public void powerupBallActivate(ImageView paddle, ArrayList<Ball> activeBalls){
    BALL_IMAGE.setX(paddle.getX() + paddle.getBoundsInLocal().getWidth()/2);
    BALL_IMAGE.setY(paddle.getY() - POWERUP_BALL_OFFSET);
    BALL_IMAGE.setOpacity(1);
    inPlay = true;
    activeBalls.add(this);
  }

  /**
   * This method is called whenever a ball hits the floor (and loses)
   * active balls must have been initialized before this method is called
   * @param activeBalls this is a list of the active balls
   * @return this method returns whether or not the user will lose a life (if they have any balls in play)
   */

  public boolean ballHitFloor(ArrayList<Ball> activeBalls){
    BALL_IMAGE.setX(0);
    BALL_IMAGE.setY(0);
    BALL_IMAGE.setOpacity(0);
    inPlay = false;
    activeBalls.remove(this);
    return (activeBalls.size() == 0);
  }

  /**
   * This method resets the balls x and y coordinate to line up with the paddle
   * @param paddle this is the paddle from Level.java
   */
  public void levelRefresh(ImageView paddle){
    BALL_IMAGE.setX(paddle.getX() + paddle.getBoundsInLocal().getWidth()/2);
    BALL_IMAGE.setY(paddle.getY() - POWERUP_BALL_OFFSET);
    BALL_IMAGE.setOpacity(1);
    inPlay = true;
    ballUpDown = 0;
    ballLeftRight = 0;
  }

  /**
   * This method is called to start a ball moving, if it had previously been stopped
   */
  public void levelStart(){
    ballUpDown = -1;
    ballLeftRight = new Random().nextBoolean() ? -1 : 1;
  }

  /**
   * This method will hide the current ball
   */
  public void destroy(){
    inPlay = false;
    BALL_IMAGE.setX(0);
    BALL_IMAGE.setY(0);
    BALL_IMAGE.setOpacity(0);
  }

  /**
   * This method will increase the ball's speed
   */
  public void upSpeed(){
    ballYSpeed += 1;
    ballXSpeed += .5;
  }

  /**
   * This method will lower the ball's speed
   */
  public void lowerSpeed(){
    if(ballXSpeed > 0) {
      ballYSpeed -= 1;
      ballXSpeed -= .5;
    }
  }

}
