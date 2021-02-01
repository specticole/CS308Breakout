package breakout;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


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


  public Brick(Group root){
    //this.root = root;
    brickImage = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(B_BRICK)));
    //this.brickType = brickType;
    //setBrickProperties(brickType);
  }

  public void setBrickProperties(char brickType) {
    this.brickType = brickType;
    switch(brickType){
      case BASIC_CHAR:
        hasPowerup = true; //(powerupDropChance.nextInt(2) == 1);
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


//  private ImageView imageSetup(String fileName){
//    return new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(fileName)));
//  }

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

  public ImageView getImageView(){
    return brickImage;
  }

  public void setXY(double x, double y){
    brickImage.setX(x);
    brickImage.setY(y);
  }

  public boolean inPlay(){
    return (brickType != EMPTY_CHAR);
  }




}
