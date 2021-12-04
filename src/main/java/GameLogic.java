public class GameLogic {
    public static final int ROWS = 6;
    public static final int COLUMNS = 7;

    public static boolean determineValidMove(GameButton button, GameButton[][] board) {
        // We only need to check buttons that are not in the bottom row
        if (button.row != 5) {
            return board[button.row + 1][button.column].player != 0;
        }
        return true;
    }

    public static void clearBoard(GameButton[][] board) {
        for (int x = 0; x < COLUMNS; x++) {
            for (int y = 0; y < ROWS; y++) {
                board[y][x].setDisable(false);
                board[y][x].player = 0;
                board[y][x].setStyle("-fx-background-color: transparent");
                board[y][x].setStyle("-fx-border-color: black; -fx-border-width: 2px");
            }
        }
    }

    public static void disableAll(GameButton[][] board) {
        for (int x = 0; x < COLUMNS; x++) {
            for (int y = 0; y < ROWS; y++) {
                board[y][x].setDisable(true);
            }
        }
    }

    public static void enableAll(GameButton[][] board) {
        for (int x = 0; x < COLUMNS; x++) {
            for (int y = 0; y < ROWS; y++) {
                board[y][x].setDisable(false);
            }
        }
    }

    public static boolean checkForWin(int player, GameButton button, GameButton[][] arr) {

        int row = button.row;
        int column = button.column;

        int count = 1;
        //Check vertical
        for (int i = row + 1; i < ROWS; i++){
            if (arr[i][column].player != player){
                break;
            }
            else {
                count += 1;
            }
            if (count == 4){
                arr[i][column].setStyle("-fx-background-color: green");
                arr[i-1][column].setStyle("-fx-background-color: green");
                arr[i-2][column].setStyle("-fx-background-color: green");
                arr[i-3][column].setStyle("-fx-background-color: green");
                return true;
            }
        }

        // Check horizontal
        count = 1;
        for (int i = column-1; i >=0; i--){
            if (arr[row][i].player != player){
                break;
            }
            else {
                count += 1;
            }
            if (count == 4){
                arr[row][i].setStyle("-fx-background-color: green");
                arr[row][i+1].setStyle("-fx-background-color: green");
                arr[row][i+2].setStyle("-fx-background-color: green");
                arr[row][i+3].setStyle("-fx-background-color: green");
                return true;
            }
        }

        for (int i = column+1; i < COLUMNS; i++){
            if (arr[row][i].player != player){
                break;
            }
            else {
                count += 1;
            }
            if (count == 4){
                arr[row][i].setStyle("-fx-background-color: green");
                arr[row][i-1].setStyle("-fx-background-color: green");
                arr[row][i-2].setStyle("-fx-background-color: green");
                arr[row][i-3].setStyle("-fx-background-color: green");
                return true;
            }
        }

        //Check diagonal
        //Going SW

        count = 1;

        outerloop1:
        for (int i = row + 1; i < ROWS; i++){
            for (int j = column - 1; j >= 0; j--){
                if (arr[i][j].player != player){
                    break outerloop1;
                }
                else {
                    count += 1;
                }
                if (count == 4) {
                    arr[i][j].setStyle("-fx-background-color: green");
                    arr[i-1][j+1].setStyle("-fx-background-color: green");
                    arr[i-2][j+2].setStyle("-fx-background-color: green");
                    arr[i-3][j+3].setStyle("-fx-background-color: green");
                    return true;
                }
                i++;

                if( i >= ROWS){
                    break outerloop1;
                }
            }
        }

        //Going NE
        outerloop2:
        for (int i = row - 1; i >= 0; i--){
            for (int j = column + 1; j < COLUMNS; j++){
                if (arr[i][j].player != player){
                    break outerloop2;
                }
                else {
                    count += 1;
                }
                if (count == 4) {
                    arr[i][j].setStyle("-fx-background-color: blue");
                    arr[i+1][j-1].setStyle("-fx-background-color: blue");
                    arr[i+2][j-2].setStyle("-fx-background-color: blue");
                    arr[i+3][j-3].setStyle("-fx-background-color: blue");
                    return true;
                }
                i--;
                if( i < 0){
                    break outerloop2;
                }

            }
        }

        count = 1;

        //SE
        outerloop3:
        for (int i = row + 1; i < ROWS; i++){
            for (int j = column + 1; j < COLUMNS; j++){
                if (arr[i][j].player != player){
                    break outerloop3;
                }
                else {
                    count += 1;
                }
                if (count == 4) {
                    arr[i][j].setStyle("-fx-background-color: red");
                    arr[i-1][j-1].setStyle("-fx-background-color: red");
                    arr[i-2][j-2].setStyle("-fx-background-color: red");
                    arr[i-3][j-3].setStyle("-fx-background-color: red");
                    return true;
                }
                i++;
                if( i >= ROWS){
                    break outerloop3;
                }
            }
        }

        //NW
        outerloop4:
        for (int i = row - 1; i >= 0; i--){
            for (int j = column - 1; j >= 0; j--){
                if (arr[i][j].player != player){
                    break outerloop4;
                }
                else {
                    count += 1;
                }
                if (count == 4) {
                    arr[i][j].setStyle("-fx-background-color: pink");
                    arr[i+1][j+1].setStyle("-fx-background-color: pink");
                    arr[i+2][j+2].setStyle("-fx-background-color: pink");
                    arr[i+3][j+3].setStyle("-fx-background-color: pink");
                    return true;
                }
                i--;
                if( i < 0){
                    break outerloop4;
                }
            }
        }

        return false;

    }


    //x  x x  x
    //easier way
    // check from only last user input button

}
