package breakout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Cole Spector
 * This class controls the falling and activation of powerups within the game by creating a queue and filling it with subsequent Powerups.
 * This class depends on information given in parameters from Level.java, and must be called from Brick.java
 * In order to use this class, call the setup method, then implement the step method within the frame of the game, and the startDrop method must be called from whenever a block which should drop a powerup is destroyed.
 * In order to add a new powerup, one should append onto the switch statement (which after a recent class, i learned should be swapped with child classes).
 */
public class Powerup {


  private static final String[] POWERUP_IMAGE_FILES = {"ballPowerup.gif", "lazerPowerup.gif", "paddlePowerup.gif"};
  private static final String LAZER_IMAGE = "lazerImage.gif";
  private static final String LONG_PADDLE_IMAGE = "longPaddleImage.gif";
  private static final String SHORT_PADDLE_IMAGE = "paddleImage.gif";
  private static final int BALL_NUMBER = 0;
  private static final int LAZER_NUMBER = 1;
  private static final int PADDLE_NUMBER = 2;
  private static final int POWERUP_LIMIT = 500;
  private static final int POWERUP_DROP_SPEED = 1;
  private static final int LAZER_OFFSET = 10;
  private static final int LAZER_SPEED = 5;
  private ImageView powerupImage;
  private int powerupNumber;
  private ArrayList<Powerup> droppingPowerups;


  private boolean isActive;
  private boolean isLongPaddle = false;
  private boolean isLazer = false;
  private Queue<Powerup> powerupQueue;
  private ArrayList<ImageView> activeLazers;


  /**
   * This is the initializer method
   *
   */
  public Powerup(){
    activeLazers = new ArrayList<>();
    droppingPowerups = new ArrayList<>();
    Random powerupChoser = new Random();
    powerupNumber = powerupChoser.nextInt(POWERUP_IMAGE_FILES.length);
    powerupImage = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(
        POWERUP_IMAGE_FILES[powerupNumber])));
    isActive = false;
  }

  /**
   * This method sets up the powerups so that they can be used in the game
   * there are no known assumptions which could cause this method to fail
   * @param root this is the root to which the Javafx objects must be added to in order to appear on screen.
   */
  public void setup(Group root){
    powerupQueue = new LinkedList<>();
    for(int i = 0; i < POWERUP_LIMIT; i ++){
      Powerup powerup = new Powerup();
      powerup.powerupImage.setY(0);
      powerup.powerupImage.setX(0);
      root.getChildren().add(powerup.powerupImage);
      powerupQueue.add(powerup);
    }
  }


  /**
   * This method is called once a block is broken, and a powerup should start dropping from that block
   * there are no known assumptions which could cause this method to fail
   * @param x This is the x position of the block which was hit
   * @param y This is the y position of the block which was hit
   */
  public void startDrop(double x, double y){
    droppingPowerups.add(powerupQueue.remove());
    droppingPowerups.get(droppingPowerups.size() - 1).powerupImage.setY(y);
    droppingPowerups.get(droppingPowerups.size() - 1).powerupImage.setX(x);
  }


  /**
   * This method is called within each frame of the game, and updates the position of all active powerups
   * there are no known assumptions which could cause this method to fail
   * @param paddle this is the paddle used in Level.java
   * @param activeBalls this is the list of balls which are in play
   * @param powerupBallQueue this is the queue of potential balls which can be added by a powerup
   * @param allBricksOnScreen this is a 2D array of all the bricks on the screen
   * @param brickRows this is the number of rows of bricks in each level
   */

  public void move(ImageView paddle, ArrayList<Ball> activeBalls, Queue<Ball> powerupBallQueue, ArrayList<ArrayList<Brick>> allBricksOnScreen, int brickRows){
    int index = 0;
    for(Powerup powerup : droppingPowerups){

        powerup.powerupImage.setY(powerup.powerupImage.getY() + POWERUP_DROP_SPEED);
        if(powerup.powerupImage.getBoundsInParent().intersects(paddle.getBoundsInParent())){
          powerup.isActive = true;
          powerup.powerupImage.setX(0);
          powerup.powerupImage.setY(0);
          activatePowerup(paddle, activeBalls, powerupBallQueue, index, powerup.powerupNumber);
        }

      index ++;
    }
    index = 0;
    for(ImageView lazer : activeLazers){
      lazer.setY(lazer.getY() - LAZER_SPEED);
      for (int row = 0; row < brickRows; row++) {
        for (Brick brick : allBricksOnScreen.get(row)) {
          if (brick.inPlay()) {
            if(lazer.getBoundsInParent().intersects(brick.getImageView().getBoundsInParent())){
              brick.isHit(this, lazer.getX(), lazer.getY());
              lazer.setX(0);
              lazer.setY(0);
              activeLazers.remove(index);
            }
          }
        }
      }
      index ++;
    }



  }

  private void activatePowerup(ImageView paddle, ArrayList<Ball> activeBalls, Queue<Ball> powerupBallQueue, int index, int powerupNumber){
    switch(powerupNumber){
      case BALL_NUMBER:
        Ball addedBall = powerupBallQueue.remove();
        addedBall.powerupBallActivate(paddle, activeBalls);
        droppingPowerups.remove(index);
        break;
      case LAZER_NUMBER:
        droppingPowerups.get(index).powerupImage.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream(LAZER_IMAGE)));
        droppingPowerups.get(index).powerupImage.setX(paddle.getX() + paddle.getBoundsInLocal().getWidth()/2);
        droppingPowerups.get(index).powerupImage.setY(paddle.getY() - LAZER_OFFSET);
        droppingPowerups.get(index).isLazer = true;
        activeLazers.add(droppingPowerups.remove(index).powerupImage);
        break;
      case PADDLE_NUMBER:
        if(!isLongPaddle) {
          paddle.setImage(
              new Image(this.getClass().getClassLoader().getResourceAsStream(LONG_PADDLE_IMAGE)));
          droppingPowerups.remove(index);
          isLongPaddle = true;
        } else {
          paddle.setImage(
              new Image(this.getClass().getClassLoader().getResourceAsStream(SHORT_PADDLE_IMAGE)));
          droppingPowerups.remove(index);
          isLongPaddle = false;
        }

        break;
    }
  }

  /**
   * This method clears all of the powerups in use and falling on the screen
   * There are no known assumptions which can cause this method to fail
   *
   */

  public void clearPowerups(){
    for(Powerup powerup : droppingPowerups){
      powerup.powerupImage.setOpacity(0);
    }
    droppingPowerups.clear();

    for(ImageView lazer : activeLazers){
      lazer.setOpacity(0);
    }
    activeLazers.clear();
  }

  /**
   * this method returns the ImageView used for a powerup
   * @return the ImageView for this specific powerup
   */
  public ImageView getImage(){
    return powerupImage;
  }

}
