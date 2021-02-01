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

  public void textSetup(Text text, double x){
    text.setX(x);
    text.setY(15);
    text.setFill(Color.GREEN);
    text.setStroke(Color.GREEN);
  }

  public void updateLives(int lives){
    livesText.setText("Lives: "+ lives);
  }

  public void loseLife(){
    lives = lives - 1;
    livesText.setText("Lives: "+ lives);
  }

  public void extraLife(){
    lives = lives + 1;
    livesText.setText("Lives: "+ lives);
  }

  public void setLevel(int level){
    this.level = level;
    levelText.setText("Level: " + level);
  }

  public void updateScore(int pointsAdded){
    score+= pointsAdded;
    scoreText.setText("Score: " + score);
  }

  public int getLevel(){
    return level;
  }

  public int getStatusBarHeight(){
    return STATUS_BAR_HEIGHT;
  }

  public int getLives(){
    return lives;
  }

  public void reset(){
    level = 0;
    levelText.setText("Level: " + level);

    score = 0;
    scoreText.setText("Score: " + score);

    lives = STARTING_LIVES;
    livesText.setText("Lives: "+ lives);
  }

  public int getScore(){
    return score;
  }
}
