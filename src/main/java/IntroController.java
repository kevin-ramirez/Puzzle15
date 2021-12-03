import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IntroController implements Initializable {

    @FXML
    AnchorPane intro;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.play();
        pause.setOnFinished(e -> {
            try {
                AnchorPane newIntro = FXMLLoader.load(getClass().getResource("play.fxml"));
                intro.getChildren().setAll(newIntro);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
