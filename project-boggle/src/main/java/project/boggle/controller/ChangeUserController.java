package project.boggle.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.boggle.model.User;

@Component
@FxmlView
public class ChangeUserController {
    private static int x = 0;
    private final FxWeaver fxWeaver;
    private Stage stage;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Label changePlayer;
    @FXML
    private User user;

    @Autowired
    public ChangeUserController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    public static void setX(int t) {
        x = t;
    }

    public String getChangePlayer() {
        return changePlayer.getText();
    }

    public void setChangePlayer(String newChangePlayer) {
        changePlayer.setText(newChangePlayer);
    }

    @FXML
    public void initialize() {
        this.stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        stage.setScene(new Scene(anchor));
        if (changePlayer.getText().equals("")) {
            changePlayer.setText(MultiplayerController.joueur);
        }

    }

    @FXML
    protected void start() {
        try {
            if (x == 0) {
                GameMultiController gameMultiController = fxWeaver.loadController(GameMultiController.class);
                gameMultiController.setUser(user);
                gameMultiController.show();
                setX(1);
                stage = (Stage) anchor.getScene().getWindow();
                stage.close();
            } else {
                GameMultiController.setTime(GameMultiController.x + 1);
                stage.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        stage.show();
    }

    public void setUser(User user) {
        this.user = user;
    }
}
