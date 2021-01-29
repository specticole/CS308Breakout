package breakout;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class StatusDisplay {
  private HBox statusBar;
  private Label level;
  private Label score;
  private Label lives;

  StatusDisplay(int level, int score, int lives){
    statusBar = new HBox();
    this.level = new Label(Integer.toString(level));
    this.score = new Label(Integer.toString(score));
    this.lives = new Label(Integer.toString(lives));
    statusBar.getChildren().addAll(this.level, this.score, this.lives);
    statusBar.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
  }

}
