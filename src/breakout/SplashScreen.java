package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;



public class SplashScreen {
  public static final String START_TEXT = "In this version of breakout, the obective is simple: \n"
      + "\t get to the top of the screen!\n"
      + "Use the arrow keys to move your paddle in order to hit \n"
      + "the ball into the blocks to clear your way.\n"
      + "Every once and a while, after you destroy a block, a \n"
      + "powerup will fall in the shape of a star, catch it \n"
      + "with your paddle to activate it!\n"
      + "Keep your eyes open for a block with a star on it, that\n"
      + " special type of block will always drop a powerup!\n"
      + "However, if you let the ball hit the bottom of the\n"
      + " screen, you will lose a life, and if you lose all\n"
      + "of your lives you lose the game!"
      + "\n\n"
      + "Now that you know the rules, press ENTER to start!";

  public static final int WIDTH = 300;
  public static final int HEIGHT = 400;
  public static final int RULE_X_OFFSET = 50;
  public static final int RULE_Y_OFFSET = 4;
  public static final int WON_GAME = 1;
  public static final int LOST_GAME = -1;
  public static final int START_GAME = 0;
  public static final Paint BACKGROUND = Color.PURPLE;
  private Rectangle background;
  private Text text;


  public SplashScreen(Group root){
    background = new Rectangle(0,0, WIDTH, HEIGHT);
    background.setFill(BACKGROUND);
    text = new Text("");
    root.getChildren().addAll(background, text);
  }

  public void showSplashScreen(int gameCondition, StatusDisplay statusDisplay){
    String printText = "";
    switch (gameCondition){
      case WON_GAME:
        printText = "You won the game :)"
            + "\n\n"
            + "You had " + statusDisplay.getLives() + "left"
            + "\n and had a score of " + statusDisplay.getScore()
            + "\n\n"
            + "press ENTER to start over";
        break;
      case LOST_GAME: // i wanted to put this text as a final static, but i cant because of the level
        printText = "You lost the game :("
            + "\n\n"
            + "You reached level " + statusDisplay.getLevel()
            + "\n and had a score of " + statusDisplay.getScore()
            + "\n\n"
            + "press ENTER to start over";
        break;
      case START_GAME:
        printText = START_TEXT;
        break;
    }
    text.setText(printText);
    text.setY(HEIGHT/2 - text.getBoundsInLocal().getHeight()/2);
    text.setX(WIDTH/2 - text.getBoundsInLocal().getWidth()/2);
    text.setFill(Color.GREEN);
    text.setStroke(Color.GREEN);
    text.setOpacity(1);
    background.setOpacity(1);
  }


  public void hide(){
    background.setOpacity(0);
    text.setOpacity(0);
  }
}
