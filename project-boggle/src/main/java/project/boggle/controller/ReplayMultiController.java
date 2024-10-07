package project.boggle.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.boggle.model.User;

@Component
@FxmlView
public class ReplayMultiController {
    private final FxWeaver fxWeaver;
    private Stage stage;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Text winner;
    @FXML
    private Text player1;
    @FXML
    private Text player2;
    @FXML
    private Text score1;
    @FXML
    private Text score2;
    @FXML
    private User user;

    @Autowired
    public ReplayMultiController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(anchor));

        player2.setText(MultiplayerController.joueur + " :");
        score1.setText(String.valueOf(GameMultiController.count1));
        score2.setText(String.valueOf(GameMultiController.count2));
        winner();
    }

    @FXML
    protected void ButtonNo() {
        try {
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    protected void ButtonYes() {
        try {
            MenuController menuController = fxWeaver.loadController(MenuController.class);
            menuController.setUser(user);
            menuController.show();

            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void winner() {
        if (GameMultiController.count1 > GameMultiController.count2) {
            winner.setText("WINNER : " + UserConnectionController.name);
        } else if (GameMultiController.count1 == GameMultiController.count2) {
            winner.setText("WINNERS : " + UserConnectionController.name + " and " + MultiplayerController.joueur);
        } else {
            winner.setText("WINNER : " + MultiplayerController.joueur);
        }
    }

    public void show() {
        stage.show();
    }

    public void setUser(User user) {
        this.user = user;
        player1.setText(user.getName() + " :");
    }


}
