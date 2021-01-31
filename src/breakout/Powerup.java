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
  private static final int POWERUP_LIMIT = 500;
  private static final int POWERUP_DROP_SPEED = 1;
  private ImageView powerupImage;
  private int powerupNumber;
  private ArrayList<Powerup> droppingPowerups;


  //private boolean isActive;
  private Queue<Powerup> powerupQueue;



  public Powerup(){
    droppingPowerups = new ArrayList<>();
    Random powerupChoser = new Random();
    powerupNumber = powerupChoser.nextInt(3);
    powerupImage = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(
        POWERUP_IMAGE_FILES[powerupNumber])));
    //isActive = false;
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
   //droppingPowerups.get(droppingPowerups.size() - 1).isActive = true;
  }

  public void drop(){
    for(Powerup powerup : droppingPowerups){
        powerup.powerupImage.setY(powerup.powerupImage.getY() + POWERUP_DROP_SPEED);
    }
  }

  public ImageView getImage(){
    return powerupImage;
  }

}
