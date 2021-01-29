package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Paddle {

  public static final String PADDLE_IMAGE = "paddleImage.gif";


  private ImageView paddleImage = new ImageView(
      new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE)));

  public Paddle(){

  }

  public ImageView getImageView(){
    return paddleImage;
  }

  public double getImageWidth(){
    return paddleImage.getBoundsInLocal().getWidth();
  }

}
