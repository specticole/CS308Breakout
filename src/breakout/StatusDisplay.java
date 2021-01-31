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
//       level = STARTING_LEVEL;
       score = 0;
//       lives = STARTING_LIVES;
       levelText = new Text("  Level: "+ STARTING_LEVEL);
       textSetup(levelText, 0);
       scoreText = new Text("Score: "+ 0);
       textSetup(scoreText, screenWidth/2 - scoreText.getBoundsInParent().getWidth()/2);
       livesText = new Text("Lives: "+ STARTING_LIVES);
       textSetup(livesText, screenWidth- scoreText.getBoundsInParent().getWidth());
       //statusBar.getChildren().addAll(levelLabel, scoreLabel, livesLabel);
       //statusBar.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));
       //statusBar.setSpacing(WIDTH/5);
       //statusBar.setLayoutY(0);
       //statusBar.setLayoutX(0);
       //statusBar.setMaxWidth(WIDTH);
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
    lives -= 1;
    livesText.setText("Lives: "+ lives);
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

}
