package breakout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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



  public Powerup(){
    activeLazers = new ArrayList<>();
    droppingPowerups = new ArrayList<>();
    Random powerupChoser = new Random();
    powerupNumber = powerupChoser.nextInt(POWERUP_IMAGE_FILES.length);
    powerupImage = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(
        POWERUP_IMAGE_FILES[powerupNumber])));
    isActive = false;
  }


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

  public void startDrop(double x, double y){
    droppingPowerups.add(powerupQueue.remove());
    droppingPowerups.get(droppingPowerups.size() - 1).powerupImage.setY(y);
    droppingPowerups.get(droppingPowerups.size() - 1).powerupImage.setX(x);
  }

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


  public ImageView getImage(){
    return powerupImage;
  }

}
