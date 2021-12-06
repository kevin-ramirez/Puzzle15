import javafx.animation.PauseTransition;
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
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Arrays;
import java.util.Random;


/*
Notes for Stefan:

Data members of note
puzzles is an ArrayList of int arrays that holds all the puzzles

gamePuzzle is initialized to the first index of the puzzles array and will contain the puzzle that will be used for
the current game session, and it is the int array that is passed to the AI solver, gamePuzzle is what should be used
to check for wins.

solutionArray is the hardcoded win state for the game

Methods of note
addGrid(GridPane pane, int[] arr)
Takes in the gameBoard and the game puzzle array. Will clear the board and fill it back up according to the layout
of the game puzzle array passed in. Should be used to animate changes or when declaring a new game

h1Handler
h2Handler
Both handlers send a ArrayList of Node to the AI solver that returns the solution path to the game

showSolution()
In the solution() method and called using Platform.runlater() the method will animate the moves the AI solver did using ArrayList<Node>
since it will return the solution path. I already did the checking to make sure it does not show more than 10 moves.
I think here is where code such a win check can go in the lambda of the pauseTransition action event

A few thoughts, both the gamePuzzle and solutionArray are int[] so checking if they are the same should be straightforward
remember the gamePuzzle is the internal array that changes according to actions made by the user.

A few flags to be aware of
gamePlayEnabled
gameplay Menu
newGame MenuItem

I disable these during animation of the solution path to prevent unwanted errors, make sure the flags are reset
when the AI solver returns a win path

 */


public class JavaFXTemplate extends Application {
	// Game board dimensions
	private static final int ROWS = 4;
	private static final int COLUMNS = 4;
	private Stage howToPlay;
	private Button exitGameBtn;
	private EventHandler<ActionEvent> closeHandler, h1Handler, h2Handler, showBtnHandler,
			howToPlayHandler, newGameHandler;
	private BorderPane gamePane;
	private ListView<String> gameLog;

	// Project 4 declarations
	private ArrayList<int []> puzzles = new ArrayList<>();
	private int[] gamePuzzle;
	// [0] = x cord [1] = y cord
	private int zeroIndexCords[] = new int[2];
	private GridPane gameBoard = new GridPane();
	private Button [][] gameArr = new Button[ROWS][COLUMNS];
	private Boolean gamePlayEnabled = true;
	private int[] solutionArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
	ArrayList<Node> solutionPath;
	private Menu gameplay;
	private MenuItem newGame;
	MenuItem showSolutionBtn;
	Text text = new Text();

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

		ExecutorService ex = Executors.newFixedThreadPool(5);

		// Project 4 declarations
		puzzles.add(new int[] {2, 6, 10, 3, 1, 4, 7, 11, 8, 5, 9, 15, 12, 13, 14, 0});
		puzzles.add(new int[] {14, 10, 6, 7, 4, 11, 15, 8, 12, 13, 2, 1, 9, 5, 3, 0});
		puzzles.add(new int[] {15, 4, 6, 14, 1, 5, 11, 13, 12, 2, 7, 9, 10, 8, 3, 0});
		puzzles.add(new int[] {15, 2, 1, 5, 8, 12, 6, 10, 3, 7, 4, 11, 9, 14, 13, 0});
		puzzles.add(new int[] {9, 13, 7, 2, 10, 14, 4, 15, 3, 6, 12, 8, 11, 1, 5, 0});
		puzzles.add(new int[] {11, 13, 12, 3, 2, 15, 14, 6, 4, 7, 5, 1, 10, 9, 8, 0});
		puzzles.add(new int[] {11, 10, 1, 2, 8, 9, 14, 15, 3, 13, 12, 5, 6, 4, 7, 0});
		puzzles.add(new int[] {5, 6, 9, 14, 11, 12, 8, 4, 15, 1, 10, 2, 7, 3, 13, 0});
		puzzles.add(new int[] {7, 14, 15, 11, 1, 8, 6, 12, 5, 13, 2, 3, 10, 9, 4, 0});
		puzzles.add(new int[] {4, 12, 15, 14, 6, 7, 2, 8, 13, 11, 10, 1, 3, 9, 5, 0});

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
				Random rand = new Random();
				int randomNum = rand.nextInt((9) + 1);
				gamePuzzle = puzzles.get(randomNum);
				addGrid(gameBoard, gamePuzzle);
				gameLog.getItems().clear();
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
				gameLog.getItems().add("Running heuristicOne");
				try {
					Future<ArrayList<Node>> future = ex.submit(new A_IDS_A_15solver(gamePuzzle, "heuristicOne"));
					solution(future, ex);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		};

		h2Handler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				gameLog.getItems().add("Running heuristicTwo");
				try {
					Future<ArrayList<Node>> future = ex.submit(new A_IDS_A_15solver(gamePuzzle, "heuristicTwo"));
					solution(future, ex);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		};

		showBtnHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameplay.setDisable(true);
				gamePlayEnabled = false;
				newGame.setDisable(true);
				showSolution();
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
		gameBoard.setVisible(false);
		gameLog.setVisible(false);

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
			} else {
				gameLog.getItems().add("Wait for solution to finish displaying.");
			}
		});


		primaryStage.setScene(scene);
		primaryStage.show();

		PauseTransition pause = new PauseTransition(Duration.seconds(3));
		pause.setOnFinished(event -> {
			gameBoard.setVisible(true);
			gameLog.setVisible(true);
			text.setText("Use W A S D keys to move");
			text.setTextAlignment(TextAlignment.CENTER);
			text.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		});
		pause.play();

	}

	// Retries the array list of nodes from the Callable of the ai to show part of the solution path
	private void solution(Future<ArrayList<Node>> future, ExecutorService ex) {
		ex.submit(() -> {
			while (true) {
				if (future.isDone()) {
					try {
						solutionPath = future.get();
						Thread.sleep(100);

						//int[] arr = solutionPath.get(solutionPath.size() - 1).getKey();

						//System.out.println(Arrays.toString(arr));

						Platform.runLater(() -> gameLog.getItems().add("AI solver has completed running."));
						Platform.runLater(() -> gameLog.getItems().add("Press show solution Button"));
						Platform.runLater(() -> showSolutionBtn.setDisable(false));

					} catch (Exception e) {
						System.out.println(e);
					}
					break;
				}
			}
		});
	}

	// Animates the solution using a pause transition so each move made by the ai can be clearly seen
	private void showSolution() {
		int showMax = 10;

		// Check to see if the solution path is greater than 10 moves
		if (solutionPath.size() < 10) {
			showMax = solutionPath.size();
		}


		for (int i = 0; i < showMax; i++) {
			int[] currentState;

			if (showMax != 10) {
				currentState = solutionPath.get(i).getKey();
			} else {
				currentState = solutionPath.get(i + 1).getKey();
			}

			int pauseTime = i;

			PauseTransition pause = new PauseTransition(Duration.seconds(pauseTime + 1));
			pause.setOnFinished(event -> {
				gamePuzzle = currentState;
				addGrid(gameBoard, currentState);
				if (checkForWin(solutionArray, gamePuzzle)){
					gamePlayEnabled = false;
					gameplay.setDisable(true);
					newGame.setDisable(false);
					gameLog.getItems().add("Congratulations - this is a win!");
					gameLog.getItems().add("Choose a new game or exit");
				}

				int time = pauseTime + 1;
				gameLog.getItems().add("Showing move " + time);

				if (time == 10) {
					gamePlayEnabled = true;
					gameplay.setDisable(false);
					newGame.setDisable(false);
					showSolutionBtn.setDisable(true);
					gameLog.getItems().add("Done showing");
				}

				if (Arrays.equals(gamePuzzle, solutionArray)) {
					System.out.println("There was a win!");
				}
			});
			pause.play();

			// For testing to show instant animation
			//gamePuzzle = currentState;
			//addGrid(gameBoard, currentState);

			//System.out.println(Arrays.toString(currentState));

		}
		//System.out.println(Arrays.toString(gamePuzzle));
	}

	// Swap function which swaps the 0 button in both the puzzle array and the game array the coordinates being passed in
	// is the square where the 0 button is being moved to
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

		if (checkForWin(solutionArray, gamePuzzle)){
			gamePlayEnabled = false;
			gameplay.setDisable(true);
			newGame.setDisable(false);
			gameLog.getItems().add("Congratulations - this is a win!");
			gameLog.getItems().add("Choose a new game or exit");
		}

	}

	private Boolean checkForWin(int[] solutionArray, int [] gameBoard){
		if (Arrays.equals(solutionArray, gameBoard)) {
			gameLog.getItems().add("There is a win");
			return true;
		}
		return false;
	}

	// Creates the game play screen
	private Scene gamePlayScreen() {
		HBox hBox = new HBox();
		VBox hBox1 = new VBox();
		gameLog = new ListView<>();

		//Text Bod
		text.setText("Welcome to Puzzle 15");
		text.setTextAlignment(TextAlignment.CENTER);
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 30));


		// Create MenuBar
		gameplay = new Menu("Game Play");
		Menu options = new Menu("Options");
		// Menu Items
		MenuItem howToPlay = new MenuItem("How to play");
		newGame = new MenuItem("New Game");
		MenuItem exit = new MenuItem("Exit");
		MenuItem h1 = new Menu("Run H1");
		MenuItem h2 = new Menu("Run H2");
		showSolutionBtn = new Menu("Show Solution");
		showSolutionBtn.setDisable(true);

		gameplay.getItems().addAll(h1, h2, showSolutionBtn);
		options.getItems().addAll(howToPlay, newGame, exit);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(gameplay, options);

		gameBoard.setMinWidth(300);
		gameBoard.setMaxWidth(400);
		addGrid(gameBoard, gamePuzzle);

		gameLog.setEditable(true);
		gameLog.setPrefSize(300, 200);
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(15, 12, 15, 12));
		hBox.getChildren().addAll(gameLog);
		hBox1.getChildren().addAll(menuBar, text);
		hBox1.setAlignment(Pos.CENTER);
		gameBoard.setAlignment(Pos.CENTER);

		// Assign menuBar handlers
		howToPlay.setOnAction(howToPlayHandler);
		newGame.setOnAction(newGameHandler);
		exit.setOnAction(closeHandler);
		h1.setOnAction(h1Handler);
		h2.setOnAction(h2Handler);
		showSolutionBtn.setOnAction(showBtnHandler);

		gamePane.setCenter(gameBoard);
		gamePane.setBottom(hBox);
		gamePane.setTop(hBox1);
		gamePane.setStyle("-fx-background-image: url(img1.jpg) ");

		return new Scene(gamePane, 700, 700);
	}

	// Load buttons into the grid pane and into an internal array to be used for game logic
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
		text.setText("Welcome to Puzzle 15!");

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
			"This game is played by swapping the blank button with any other adjacent buttons.\n" +
					"The goal is to display buttons 1-15 in order while having blank button in the top left corner.\n" +
					"You can also run either h1 or h2 AI solutions if you get stuck, with them being limited to showing up 10 moves.\n" +
					"Good luck!.\n";
}
