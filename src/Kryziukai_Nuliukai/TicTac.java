package Kryziukai_Nuliukai;


import java.awt.List;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TicTac extends Application {
	
	private boolean playable = true;
	private boolean turnX = true;
	private Tile[][] board = new Tile[3][3];
	private ArrayList<Combo> combos = new ArrayList<>();
	
	private Pane root = new Pane();

	private Parent createContent() {

		root.setPrefSize(600, 600);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Tile tile = new Tile();
				tile.setTranslateX(j * 200);
				tile.setTranslateY(i * 200);

				root.getChildren().add(tile);
				board[j][i] = tile;
			}
		}
		
		//horizontaliai
		
		for (int y = 0; y < 3; y++) {
			combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
		}
		
		//vertikaliai
		for (int x = 0; x < 3; x++) {
			combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
		}
		//diagonale
		combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
		combos.add(new Combo(board[2][0], board[1][1], board[0][2]));
		
		return root;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.show();

	}
	
	private void checkState() {
		for(Combo combo: combos) {
			if (combo.isComplete()) {
				playable = false;
				playWinAnimation(combo);
				break;
			}
		}
	}
	
	private void playWinAnimation(Combo combo) {
		Line linija = new Line();
		linija.setStartX(combo.tiles[0].getCenterX());
		linija.setStartY(combo.tiles[0].getCenterY());
		linija.setEndX(combo.tiles[0].getCenterX());
		linija.setEndY(combo.tiles[0].getCenterY());
		
		root.getChildren().add(linija);
		
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new KeyValue(linija.endXProperty(), combo.tiles[2].getCenterX()), new KeyValue(linija.endYProperty(), combo.tiles[2].getCenterY()))); 
		timeline.play();
	}
	
	private class Combo {
		
		private Tile[] tiles;
		public Combo(Tile...tiles) {
			this.tiles = tiles;
		}

	
	public boolean isComplete() {
		
		if (tiles[0].getValue().isEmpty())
			return false;
		
		return tiles[0].getValue().equals(tiles[1].getValue()) 
				&& tiles[0].getValue().equals(tiles[2].getValue());
	}
	}

	private class Tile extends StackPane {
		private Text tekstas = new Text();
		
		public Tile() {

			Rectangle langelis = new Rectangle(200, 200);
			langelis.setFill(null);
			langelis.setStroke(Color.BLACK);
			
			tekstas.setFont(Font.font(72));

			setAlignment(Pos.CENTER);
			getChildren().addAll(langelis, tekstas);
			
			setOnMouseClicked(event -> {
				if (!playable)
					return;
				
				if (event.getButton() == MouseButton.PRIMARY) {
					if (!turnX)
						return;
					
					pieskX();
					turnX = false;
					checkState();
				}
				else if (event.getButton() == MouseButton.SECONDARY){
					if (turnX)
						return;
					
					pieskO();
					turnX = true;
					checkState();
				
			}
			});
		}
		
		public double getCenterX() {
			return getTranslateX() + 100;
		}
		
		public double getCenterY() {
			return getTranslateY() + 100;
		}
		
		public String getValue() {
			return tekstas.getText();
		}
		
		private void pieskX() {
			tekstas.setText("X");
			tekstas.setStroke(Color.BLUE);
		}
		
		private void pieskO() {
			tekstas.setText("O");
			tekstas.setStroke(Color.BLUE);
		}

	}

	public static void main(String[] args) {
		launch(args);

	}

}
