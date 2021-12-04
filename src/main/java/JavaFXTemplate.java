import javafx.animation.PauseTransition;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Stack;

import java.util.Stack;

public class JavaFXTemplate extends Application {
	private static final int picHeight = 500;
	private static final int picWidth = 500;

	// Game board dimensions
	private static final int ROWS = 6;
	private static final int COLUMNS = 7;

	private Stage howToPlay;
	private int buttonPresses;
	private Button startGameBtn, exitGameBtn;
	private EventHandler<ActionEvent> closeHandler, gameButtonHandler, reverseHandler,
			orgThemeHandler, themeOneHandler, themeTwoHandler, howToPlayHandler, newGameHandler, endGameHandler;
	private GridPane gameBoard;
	private BorderPane gamePane;
	private Text whichPlayer;
	private ListView<String> gameLog;
	private GameButton [][] gameArray = new GameButton[ROWS][COLUMNS];
	private Stack<GameButton> moves = new Stack<>();
	private PauseTransition pause = new PauseTransition(Duration.seconds(4));

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
//		primaryStage.setTitle("Welcome to JavaFX");
//		//Test Commit
//		Scene scene = new Scene(new VBox(), 700,700);
//		primaryStage.setScene(scene);
//		primaryStage.show();
//		Thread t = new Thread(()-> {A_IDS_A_15solver ids = new A_IDS_A_15solver();});
//		t.start();

		primaryStage.setTitle("Welcome to Puzzle15");

		Parent root = FXMLLoader.load(getClass().getResource("/intro.fxml"));
		Scene scene = new Scene(root);

		gamePane = new BorderPane();

		// Button declarations
		startGameBtn = new Button("Play");
		exitGameBtn = new Button("Exit");

		// Handlers
		orgThemeHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				gamePane.setStyle("-fx-background-image: url(img1.jpg) ");
			}
		};

		themeOneHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				gamePane.setStyle("-fx-background-image: url(img2.jpg) ");
			}
		};

		themeTwoHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				gamePane.setStyle("-fx-background-image: url(img3.jpg) ");
			}
		};

		closeHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				System.exit(0);
			}
		};

//		howToPlayHandler = new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent actionEvent) {
//				howToPlayScreen().show();
//			}
//		};

		newGameHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				whichPlayer.setText("Player One");
				gameLog.getItems().clear();
				gameLog.getItems().add("New Game!");
				GameLogic.clearBoard(gameArray);
				moves.clear();
				buttonPresses = 0;

			}
		};

		endGameHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				GameLogic.enableAll(gameArray);
				buttonPresses = 0;
				moves.clear();
				gamePane = new BorderPane();
				primaryStage.setScene(gamePlayScreen());
			}
		};

		gameButtonHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				GameButton button = (GameButton)actionEvent.getSource();

				// Game Logic
				// Check if move is valid
				if (!GameLogic.determineValidMove(button, gameArray)) {
					gameLog.getItems().add("That was an invalid move. Try again");
					return;
				}
				button.setDisable(true);

				// Edits the button on the board to the player that pressed it
				gameLog.getItems().add(whichPlayer.getText() + " placed a piece at cords: " + button.row + ", " + button.column);
				buttonPresses++;

				// Check for a win
				int player;
				if (whichPlayer.getText().equals("Player One")) {
					player = 1;
				}
				else {
					player = 2;
				}

				if (GameLogic.checkForWin(player, button, gameArray) || buttonPresses == 42) {
					//GameLogic.disableAll(gameArray);
					if (buttonPresses == 42) {
						gameLog.getItems().add("Tie game");
					} else {
						gameLog.getItems().add(whichPlayer.getText() + " WON!");
					}
					pause.play();
					pause.setOnFinished(e -> {
						//primaryStage.setScene(winScreen(whichPlayer.getText(), buttonPresses));
						primaryStage.show();
					});
					return;
				}
				// End Logic

				if (whichPlayer.getText().equals("Player One")) {
					button.player = 1;
					button.setStyle("-fx-background-color: orange");
					whichPlayer.setText("Player Two");
				} else {
					button.player = 2;
					button.setStyle("-fx-background-color: purple");
					whichPlayer.setText("Player One");
				}

				moves.push(button);
			}
		};


		reverseHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				GameButton button;
				if (moves.empty()) {
					gameLog.getItems().add("You can't reverse a move if the board is empty!");
					return;
				}

				if (whichPlayer.getText().equals("Player One")) {
					whichPlayer.setText("Player Two");
				} else {
					whichPlayer.setText("Player One");
				}

				button = moves.pop();
				gameLog.getItems().add("Player " + button.player + " reversed their move");
				button.player = 0;
				button.setDisable(false);
				button.setStyle("-fx-background-color: transparent");
				button.setStyle("-fx-border-color: black; -fx-border-width: 2px");
				buttonPresses--;

			}
		};

		// End Handlers

		// Assign Handlers
		exitGameBtn.setOnAction(closeHandler);
		startGameBtn.setOnAction(e -> primaryStage.setScene(gamePlayScreen()));

		// End Assign Handlers

		//primaryStage.setScene(welcomeScreen());
		//primaryStage.show();



		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		primaryStage.setScene(welcomeScreen());
		primaryStage.show();

	}

	// Sets the welcome screen
	private Scene welcomeScreen() {
		BorderPane pane = new BorderPane();
		Text welcomeScreenText = new Text();
		welcomeScreenText.setText("Welcome! Press PLAY to play or EXIT to exit");
		Image pic = new Image("img1.jpg");
		ImageView view = new ImageView(pic);
		view.setFitHeight(picHeight);
		view.setFitWidth(picWidth);
		view.setPreserveRatio(true);
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		vBox.getChildren().add(welcomeScreenText);
		hBox.getChildren().addAll(startGameBtn, exitGameBtn);
		pane.setTop(vBox);
		pane.setCenter(view);
		pane.setBottom(hBox);

		pane.setStyle("-fx-background-color: lightsalmon;");
		vBox.setAlignment(Pos.CENTER);
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(15, 12, 15, 12));
		vBox.setPadding(new Insets(15, 12, 15, 12));
		startGameBtn.setMinSize(70, 25);
		exitGameBtn.setMinSize(70,25);

		return new Scene(pane, 500, 500);
	}

	// Creates the game play screen
	private Scene gamePlayScreen() {
		HBox hBox = new HBox();
		HBox hBox1 = new HBox();
		gameLog = new ListView<>();
		whichPlayer = new Text();
		whichPlayer.setText("Player One");
		gameBoard = new GridPane();

		// Create MenuBar
		Menu gameplay = new Menu("Game Play");
		Menu themes = new Menu("Themes");
		Menu options = new Menu("Options");
		// Menu Items
		MenuItem reverse = new MenuItem("Reverse Move");
		MenuItem orgTheme = new MenuItem("original theme");
		MenuItem themeOne = new MenuItem("theme one");
		MenuItem themeTwo = new MenuItem("theme two");
		MenuItem howToPlay = new MenuItem("how to play");
		MenuItem newGame = new MenuItem("new game");
		MenuItem exit = new MenuItem("exit");

		gameplay.getItems().add(reverse);
		themes.getItems().addAll(orgTheme, themeOne, themeTwo);
		options.getItems().addAll(howToPlay, newGame, exit);

		MenuBar menubar = new MenuBar();
		menubar.getMenus().addAll(gameplay, themes, options);

		gameBoard.setMinWidth(300);
		gameBoard.setMaxWidth(400);
		addGrid(gameBoard);

		gameLog.setEditable(true);
		gameLog.setPrefSize(300, 200);
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(15, 12, 15, 12));
		hBox.getChildren().addAll(gameLog, whichPlayer);
		hBox1.getChildren().add(menubar);
		hBox1.setAlignment(Pos.CENTER);
		gameBoard.setAlignment(Pos.CENTER);

		// Assign menubar handlers
		reverse.setOnAction(reverseHandler);
		orgTheme.setOnAction(orgThemeHandler);
		themeOne.setOnAction(themeOneHandler);
		themeTwo.setOnAction(themeTwoHandler);
		howToPlay.setOnAction(howToPlayHandler);
		newGame.setOnAction(newGameHandler);
		exit.setOnAction(closeHandler);

		gamePane.setCenter(gameBoard);
		gamePane.setBottom(hBox);
		gamePane.setTop(hBox1);
		gamePane.setStyle("-fx-background-image: url(img1.jpg) ");

		return new Scene(gamePane, 700, 700);
	}

	// Loads the game buttons into the grid pane and into an internal 2d array to be used for game logic
	private void addGrid(GridPane pane) {
		for (int x = 0; x < COLUMNS; x++) {
			for (int y = 0; y < ROWS; y++) {
				GameButton button = new GameButton(x, y);
				button.setOnAction(gameButtonHandler);

				button.setMinSize(50, 50);
				button.setStyle("-fx-background-color: transparent");
				button.setStyle("-fx-border-color: black; -fx-border-width: 2px");
				gameArray[y][x] = button;
				pane.add(button, x, y);
			}
		}
		pane.setHgap(10);
		pane.setVgap(10);
	}

	// Displays a screen with instructions on how to play the game
	private Stage howToPlayScreen() {
		howToPlay = new Stage();
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		Button button = new Button();
		button.setText("Thanks, now let me play!");
		button.setOnAction(e->howToPlay.close());
		Text text = new Text();
		text.setText("Welcome to Connect 4!");

		vbox.setSpacing(10);
		vbox.setPadding(new Insets(15, 12, 15, 12));
		vbox.getChildren().addAll(button, text);

		BorderPane pane = new BorderPane();
		Label label = new Label(WORDS);
		label.setWrapText(true);
		label.setAlignment(Pos.CENTER);

		pane.setTop(vbox);
		pane.setCenter(label);
		pane.setStyle("-fx-background-color: lightsalmon;");
		Scene scene = new Scene(pane, 800, 400);
		howToPlay.setTitle("How To Play");
		howToPlay.setScene(scene);

		return howToPlay;
	}

	private static final String WORDS =
			"This game is played by connecting 4 pieces in a row.\n" +
					"This can be done diagonally, horizontally, or vertically.\n" +
					"Moves are only valid when they are placed in a position that is at the bottom most column.\n" +
					"For example, if the column chosen is empty that piece can only be placed at the bottom.\n" +
					"If there is already a piece placed in that column then the piece currently being placed can\n" +
					"Only be placed right above that piece.\n" +
					"This is a 2 player game where players turns alternate. Have FUN!!!";

}
