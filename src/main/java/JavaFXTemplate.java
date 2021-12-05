import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import java.util.ArrayList;


public class JavaFXTemplate extends Application {
	// Game board dimensions
	private static final int ROWS = 4;
	private static final int COLUMNS = 4;
	private Stage howToPlay;
	private Button exitGameBtn;
	private EventHandler<ActionEvent> closeHandler, h1Handler,
			howToPlayHandler, newGameHandler;
	private BorderPane gamePane;
	private ListView<String> gameLog;

	// Project 4 declarations
	private ArrayList<int []> puzzles = new ArrayList<>();
	private int[] starterPuzzle;
	private int[] gamePuzzle;
	// [0] = x cord [1] = y cord
	private int zeroIndexCords[] = new int[2];
	private GridPane gameBoard = new GridPane();
	private Button [][] gameArr = new Button[ROWS][COLUMNS];
	private Boolean gamePlayEnabled = true;
	private int[] solutionArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

	public JavaFXTemplate() {
	}

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

		// Project 4 declarations
		puzzles.add(new int[] {2, 6, 10, 3, 1, 4, 7, 11, 8, 5, 9, 15, 12, 13, 14, 0});

		starterPuzzle = puzzles.get(0);

		gamePuzzle = puzzles.get(0);

		gamePane = new BorderPane();

		// Button declarations
		exitGameBtn = new Button("Exit");

		// Handlers
		closeHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				System.exit(0);
			}
		};

		newGameHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {

			}
		};

		howToPlayHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				howToPlayScreen().show();
			}
		};

		h1Handler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				System.out.println("Hello");
			}
		};
		// End Handlers

		// Assign Handlers
		exitGameBtn.setOnAction(closeHandler);

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		Scene scene = gamePlayScreen();

		// add keyboard functionality to screen
		scene.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
			if (gamePlayEnabled) {
				/*
				W = up
				A = left
				S = down
				D = right
				zeroIndexCord[0] = x/row
				zeroIndexCord[1] = y/col
				 */
				if (keyEvent.getCode().toString().equals("W")) {
					if (zeroIndexCords[0] == 0) {
						gameLog.getItems().add("Can not move up anymore");
					} else {
						gameLog.getItems().add("0 square moved up");
						int x = zeroIndexCords[0] - 1;
						int y = zeroIndexCords[1];
						System.out.println(x + " : " + y);
						swap(x, y);
						zeroIndexCords[0]--;

					}
				} else if (keyEvent.getCode().toString().equals("A")) {
					if (zeroIndexCords[1] == 0) {
						gameLog.getItems().add("Can not move left anymore");
					} else {
						gameLog.getItems().add("0 square moved left");
						int x = zeroIndexCords[0];
						int y = zeroIndexCords[1] - 1;
						System.out.println(x + " : " + y);
						swap(x, y);
						zeroIndexCords[1]--;

					}
				} else if (keyEvent.getCode().toString().equals("S")) {
					if (zeroIndexCords[0] == 3) {
						gameLog.getItems().add("Can not move down anymore");
					} else {
						gameLog.getItems().add("0 square moved down");
						int x = zeroIndexCords[0] + 1;
						int y = zeroIndexCords[1];
						System.out.println(x + " : " + y);
						swap(x, y);
						zeroIndexCords[0]++;
					}
				} else if (keyEvent.getCode().toString().equals("D")) {
					if (zeroIndexCords[1] == 3) {
						gameLog.getItems().add("Can not move right anymore");
					} else {
						gameLog.getItems().add("0 square moved right");
						int x = zeroIndexCords[0];
						int y = zeroIndexCords[1] + 1;
						System.out.println(x + " : " + y);
						swap(x, y);
						zeroIndexCords[1]++;
					}
				}
			}
		});

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	// Swap function which swaps the 0 button in both the puzzle array and the game array
	private void swap (int x, int y) {
		int target = Integer.parseInt(gameArr[x][y].getText());
		System.out.println(target);

		// find where the 0 and number being swapped to are in the array
		int zero = -1;
		int beingSwapped = -1;
		for (int i = 0; i < gamePuzzle.length; i++) {

			if (gamePuzzle[i] == 0) {
				zero = i;
			} else if (gamePuzzle[i] == target) {
				beingSwapped = i;
			}
		}

		// swap gamePuzzle array
		gamePuzzle[zero] = gamePuzzle[beingSwapped];
		gamePuzzle[beingSwapped] = 0;

		Button button = gameArr[zeroIndexCords[0]][zeroIndexCords[1]];

		button.setText(gameArr[x][y].getText());
		button.setVisible(true);

		gameArr[x][y].setText("0");
		gameArr[x][y].setVisible(false);



	}

	// Creates the game play screen
	private Scene gamePlayScreen() {
		HBox hBox = new HBox();
		VBox hBox1 = new VBox();
		gameLog = new ListView<>();

		//Text Bod
		Text text = new Text();
		text.setText("Use W A S D keys to move");
		text.setTextAlignment(TextAlignment.CENTER);
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 30));


		// Create MenuBar
		Menu gameplay = new Menu("Game Play");
		Menu options = new Menu("Options");
		// Menu Items
		MenuItem howToPlay = new MenuItem("how to play");
		MenuItem newGame = new MenuItem("new game");
		MenuItem exit = new MenuItem("exit");
		MenuItem h1 = new Menu("Run H1");
		MenuItem h2 = new Menu("Run H2");

		gameplay.getItems().addAll(h1, h2);
		options.getItems().addAll(howToPlay, newGame, exit);

		MenuBar menubar = new MenuBar();
		menubar.getMenus().addAll(gameplay, options);

		gameBoard.setMinWidth(300);
		gameBoard.setMaxWidth(400);
		addGrid(gameBoard, starterPuzzle);

		gameLog.setEditable(true);
		gameLog.setPrefSize(300, 200);
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(15, 12, 15, 12));
		hBox.getChildren().addAll(gameLog);
		hBox1.getChildren().addAll(menubar, text);
		hBox1.setAlignment(Pos.CENTER);
		gameBoard.setAlignment(Pos.CENTER);

		// Assign menubar handlers
		howToPlay.setOnAction(howToPlayHandler);
		newGame.setOnAction(newGameHandler);
		exit.setOnAction(closeHandler);
		h1.setOnAction(h1Handler);

		gamePane.setCenter(gameBoard);
		gamePane.setBottom(hBox);
		gamePane.setTop(hBox1);
		gamePane.setStyle("-fx-background-image: url(img1.jpg) ");

		return new Scene(gamePane, 700, 700);
	}

	// Loads the game buttons into the grid pane and into an internal 2d array to be used for game logic
	private void addGrid(GridPane pane, int[] arr) {

		pane.getChildren().removeAll();
		pane.getChildren().clear();
		int index = 0;

		for (int x = 0; x < COLUMNS; x++) {
			for (int y = 0; y < ROWS; y++) {

				gameArr[x][y] = new Button();
				Button currBtn = gameArr[x][y];
				currBtn.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
				currBtn.setText(String.valueOf(arr[index]));
				currBtn.setMinSize(75, 75);
				currBtn.setMaxSize(75,75);
				currBtn.setStyle("-fx-background-color: lightgrey");
				index++;

				// Check to see if the button is the 0 button
				if (gameArr[x][y].getText().equals("0")) {
					currBtn.setVisible(false);
					zeroIndexCords[0] = x;
					zeroIndexCords[1] = y;
				}

				pane.add(currBtn, y, x);
			}
		}
		pane.setHgap(3);
		pane.setVgap(3);
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
