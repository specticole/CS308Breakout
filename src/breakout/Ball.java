package breakout;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {
  public static final String BALL_IMAGE_NAME = "ballImage.gif";
  public static final double BALL_X_SPEED = 1;
  public static final double BALL_Y_SPEED = 2;
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

  public Ball(Group root, StatusDisplay statusDisplay){
    ballLeftRight = new Random().nextBoolean() ? -1 : 1;
    this.statusDisplay = statusDisplay;
    this.root = root;
  }

  public void setup(double x, double y, boolean enabled){
    BALL_IMAGE.setX(x);
    BALL_IMAGE.setY(y);
    inPlay = enabled;
    if(!inPlay){
      BALL_IMAGE.setOpacity(0);
    }
    root.getChildren().add(BALL_IMAGE);
  }

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
      BALL_IMAGE.setX(BALL_IMAGE.getX() + (BALL_X_SPEED * ballLeftRight));
      BALL_IMAGE.setY(BALL_IMAGE.getY() + (BALL_Y_SPEED * ballUpDown));

    }
    return BALL_IN_PLAY;
  }

  public void powerupBallActivate(ImageView paddle, ArrayList<Ball> activeBalls){
    BALL_IMAGE.setX(paddle.getX() + paddle.getBoundsInLocal().getWidth()/2);
    BALL_IMAGE.setY(paddle.getY() - POWERUP_BALL_OFFSET);
    BALL_IMAGE.setOpacity(1);
    inPlay = true;
    activeBalls.add(this);
  }

  public boolean ballHitFloor(ArrayList<Ball> activeBalls){
    BALL_IMAGE.setX(0);
    BALL_IMAGE.setY(0);
    BALL_IMAGE.setOpacity(0);
    inPlay = false;
    activeBalls.remove(this);
    return (activeBalls.size() == 0);
  }

  public void levelRefresh(ImageView paddle){
    BALL_IMAGE.setX(paddle.getX() + paddle.getBoundsInLocal().getWidth()/2);
    BALL_IMAGE.setY(paddle.getY() - POWERUP_BALL_OFFSET);
    BALL_IMAGE.setOpacity(1);
    inPlay = true;
    ballUpDown = 0;
    ballLeftRight = 0;
  }

  public void levelStart(){
    ballUpDown = -1;
    ballLeftRight = new Random().nextBoolean() ? -1 : 1;
  }

  public void destroy(){
    inPlay = false;
    BALL_IMAGE.setX(0);
    BALL_IMAGE.setY(0);
    BALL_IMAGE.setOpacity(0);
  }

}
