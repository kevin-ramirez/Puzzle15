import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    Button test;

    @FXML
    GridPane gameBoard;

    @FXML
    public void testHandler(ActionEvent actionEvent) {
        System.out.println("Hello");
    }
}
