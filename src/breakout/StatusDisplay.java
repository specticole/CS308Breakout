package breakout;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * This class is used to display the player's level, score, and number of lives left
 * In order to use this class, call the setup method, and then call the included methods to change one part of the display (level, score or lives).
 */
public class StatusDisplay {

  public static final int STATUS_BAR_HEIGHT = 20;
  public static final int STARTING_LIVES = 3;
  public static final int STARTING_LEVEL = 0;


  Text levelText;
  Text scoreText;
  Text livesText;
  private int level;
  private int score;
  private int lives;
  private Group root;
  private Rectangle statusBarOutline;

  StatusDisplay(Group root){
    this.root = root;
   }

  /**
   * This method sets up the statusDisplay and adds children to the root
   * If screenWidth is negative, this method may fail
   * @param screenWidth this is the width of the game window in pixels
   *
   */
   public void setup(int screenWidth){
       level = STARTING_LEVEL;
       score = 0;
       lives = STARTING_LIVES;
       levelText = new Text("  Level: "+ level);
       textSetup(levelText, 0);
       scoreText = new Text("Score: "+ score);
       textSetup(scoreText, screenWidth/2 - scoreText.getBoundsInParent().getWidth()/2);
       livesText = new Text("Lives: "+ lives);
       textSetup(livesText, screenWidth- scoreText.getBoundsInParent().getWidth());
       statusBarOutline = new Rectangle(0, 0, screenWidth, STATUS_BAR_HEIGHT);
       statusBarOutline.setFill(Color.BLACK);
       root.getChildren().add(statusBarOutline);
       root.getChildren().add(levelText);
       root.getChildren().add(scoreText);
       root.getChildren().add(livesText);
   }

  /**
   * This method sets up the text for each portion of the status display
   * @param text this is the Text object you wish to setup
   * @param x this is the x coordinates to place the Text object
   */
  public void textSetup(Text text, double x){
    text.setX(x);
    text.setY(15);
    text.setFill(Color.GREEN);
    text.setStroke(Color.GREEN);
  }

  /**
   * This method could be used to update the number of lives the user has in the status display
   * @param lives the number to set the user's ives to.
   */
  public void updateLives(int lives){
    livesText.setText("Lives: "+ lives);
  }


  /**
   * This method takes one life away from the user, and updates the status display accordingly
   *
   */
  public void loseLife(){
    lives = lives - 1;
    livesText.setText("Lives: "+ lives);
  }

  /**
   * This method gives the user one extra life, and updates the status display accordingly
   */
  public void extraLife(){
    lives = lives + 1;
    livesText.setText("Lives: "+ lives);
  }

  /**
   * This method updates the level, and updates the status display accordingly
   * @param level this is the level the user wishes to go to
   */
  public void setLevel(int level){
    this.level = level;
    levelText.setText("Level: " + level);
  }

  /**
   * This method adds points to the score, and updates the status display accordingly
   * @param pointsAdded this is the number of points to add to the user's score
   */

  public void updateScore(int pointsAdded){
    score+= pointsAdded;
    scoreText.setText("Score: " + score);
  }

  /**
   * This method returns the current level
   * @return the current level
   */
  public int getLevel(){
    return level;
  }

  /**
   * This method returns the height of the status display
   * @return the height of the status display as an int
   */
  public int getStatusBarHeight(){
    return STATUS_BAR_HEIGHT;
  }

  /**
   * This method returns the number of lives the user has
   * @return the number of lives the user has as an int
   */
  public int getLives(){
    return lives;
  }

  /**
   * This method resets the status display to the default values
   */
  public void reset(){
    level = 0;
    levelText.setText("Level: " + level);

    score = 0;
    scoreText.setText("Score: " + score);

    lives = STARTING_LIVES;
    livesText.setText("Lives: "+ lives);
  }

  /**
   * This method returns the user's current score
   * @return the users current score as an int
   */
  public int getScore(){
    return score;
  }
}
