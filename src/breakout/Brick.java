package breakout;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Cole Spector
 * This class is used to create and manage each individual brick on the screen
 * this class depends on Ball.java, Level.java and StatusDisplay.java
 * In order to use this class, whenever you want to use a brick call the setup method, and call the isHit method whever a brick is interacted with.
 * As was learned in class this week, this method should be split up into extensions, one for each brick type
 */
public class Brick {

  private static final String B_BRICK = "basicBrick.gif";
  private static final String R_BRICK = "reinforcedBrick.gif";
  private static final String U_BRICK = "unbreakableBrick.gif";
  private static final String E_BRICK = "emptyBrick.gif";
  private static final String X_BRICK = "powerupBrick.gif";
  public static final char BASIC_CHAR = 'B';
  public static final char XTRA_CHAR = 'X';
  public static final char REINFORCED_CHAR = 'R';
  public static final char UNBREAKABLE_CHAR = 'U';
  public static final char EMPTY_CHAR = 'E';


  Random powerupDropChance = new Random();


  private ImageView brickImage;
  private boolean hasPowerup;
  private char brickType;
  private Group root;

  /**
   * This is the initializer method
   * this method will be called whenever a brick is initialized
   * @param root this is the Group to which all javaFX objects should be added to too show up on screen.
   */
  public Brick(Group root){
    //this.root = root;
    brickImage = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(B_BRICK)));
    //this.brickType = brickType;
    //setBrickProperties(brickType);
  }

  /**
   * This method switches the type that the brick is
   * brickType must be one of the defined types above
   * @param brickType this is the type to swap the brick to.
   */

  public void setBrickProperties(char brickType) {
    this.brickType = brickType;
    switch(brickType){
      case BASIC_CHAR:
        hasPowerup = (powerupDropChance.nextInt(10) == 1);
        brickImage.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream(B_BRICK)));
        break;
      case XTRA_CHAR:
        hasPowerup = true;
        brickImage.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream(X_BRICK)));
        break;
      case REINFORCED_CHAR:
        brickImage.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream(R_BRICK)));
        break;
      case UNBREAKABLE_CHAR:
        brickImage.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream(U_BRICK)));
        break;
      case EMPTY_CHAR:
        brickImage.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream(E_BRICK)));
        break;
    }
  }

  /**
   * This is the method to be called whenever a brick is interacted with
   * @param powerup this is the powerup declared in level.java
   * @param x the x position of the brick
   * @param y the y position of the brick
   * @return the number of points to be added to the user's score
   */

  public int isHit(Powerup powerup, double x, double y){
    switch(brickType) {
      case BASIC_CHAR:
        switchImage(E_BRICK, EMPTY_CHAR);
        if(hasPowerup){
          powerup.startDrop(x, y);
        }
        return 50;
      case XTRA_CHAR:
        switchImage(E_BRICK, EMPTY_CHAR);
        powerup.startDrop(x, y);
        return 50;
      case REINFORCED_CHAR:
        switchImage(B_BRICK, BASIC_CHAR);
        return 25;
      case UNBREAKABLE_CHAR:
        return 1;
      case EMPTY_CHAR:
        return 0;
    }
    return 0;
  }


  private void switchImage(String newImage, char brickType){
    brickImage.setImage(new Image(this.getClass().getClassLoader().getResourceAsStream(newImage)));
    this.brickType = brickType;
  }

  /**
   * This method returns the current brick's ImageView
   * @return the ImageView for this brick
   */
  public ImageView getImageView(){
    return brickImage;
  }


  /**
   * This method sets the x and y coordinates for the brick
   * @param x the new x coordinate
   * @param y the new y coordinate
   */
  public void setXY(double x, double y){
    brickImage.setX(x);
    brickImage.setY(y);
  }

  /**
   * This method returns whether or not the brick is in play (i.e. not an empty space)
   * @return whether or not the brick is empty
   */
  public boolean inPlay(){
    return (brickType != EMPTY_CHAR);
  }




}
