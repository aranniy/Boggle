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
public class GameController4X4 extends GameController {

    public GameController4X4(FxWeaver fxWeaver, RestTemplate restTemplate) {
        super(fxWeaver, restTemplate);
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(anchor));
        stage.setResizable(false);
        time = new Timer();
        stage.setOnCloseRequest(event -> time.cancel());
        int count = 0;
        board = new Board(4, 4);
        for (Node b : viewBoard.getChildren()) {
            Button b1 = (Button) b;
            b1.setText("" + board.getAllCases().get(count).getLetter());
            count++;
        }
    }
}
