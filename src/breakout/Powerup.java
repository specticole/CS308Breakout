package breakout;

import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Powerup {

  private static final String[] powerupImageFiles = {"ballPowerup.gif", "lazerPowerup.gif", "paddlePowerup.gif"};
  private ImageView powerupImage;
  private int powerupNumber;


  public Powerup(){
    Random powerupChoser = new Random();
    powerupNumber = powerupChoser.nextInt(3);
    powerupImage = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(
        powerupImageFiles[powerupNumber])));
  }

  public void activate(){

  }

}
