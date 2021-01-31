package breakout;

import java.util.Random;
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
  private Powerup powerup;

  public Brick(char brickType){

    this.brickType = brickType;
    switch(brickType){
      case BASIC_CHAR:
        boolean powerup = (powerupDropChance.nextInt(10) == 1);
        brickImage = imageSetup(B_BRICK, powerup);
        assignPowerup();
        break;
      case XTRA_CHAR:
        brickImage = imageSetup(X_BRICK, true);
        assignPowerup();
        break;
      case REINFORCED_CHAR:
        brickImage = imageSetup(R_BRICK, false);
        break;
      case UNBREAKABLE_CHAR:
        brickImage = imageSetup(U_BRICK, false);
        break;
      case EMPTY_CHAR:
        brickImage = imageSetup(E_BRICK, false);
        break;
    }
  }

  private void assignPowerup(){
    if(hasPowerup){
      powerup = new Powerup();
    }
  }

  public Powerup getPowerup(){
    return powerup;
  }

  private ImageView imageSetup(String fileName, boolean powerup){
    hasPowerup = powerup;
    return new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(fileName)));
  }

  public int isHit(){
    switch(brickType) {
      case BASIC_CHAR:
        switchImage(E_BRICK, EMPTY_CHAR);
        return 50;
      case XTRA_CHAR:
        switchImage(E_BRICK, EMPTY_CHAR);
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
