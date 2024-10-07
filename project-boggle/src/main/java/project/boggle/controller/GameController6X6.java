package project.boggle.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.boggle.model.Board;

import java.util.Timer;

@Component
@FxmlView
public class GameController6X6 extends GameController {

    public GameController6X6(FxWeaver fxWeaver, RestTemplate restTemplate) {
        super(fxWeaver, restTemplate);
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(anchor));
        int count = 0;
        stage.setResizable(false);
        time = new Timer();
        stage.setOnCloseRequest(event -> time.cancel());
        board = new Board(6, 6);
        for (Node b : viewBoard.getChildren()) {
            Button b1 = (Button) b;
            b1.setText("" + board.getAllCases().get(count).getLetter());
            count++;
        }
    }
}
