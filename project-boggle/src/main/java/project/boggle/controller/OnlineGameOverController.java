package project.boggle.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.boggle.model.User;

@Component
@FxmlView
public class OnlineGameOverController {
    private final FxWeaver fxWeaver;

    @FXML
    public Label opponentName;

    private User user;
    private Stage stage;

    @FXML
    private AnchorPane anchor;

    @Autowired
    public OnlineGameOverController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(anchor));
    }


    public void setUser(User user) {
        this.user = user;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName.setText(opponentName);
    }

    public void show() {
        stage.show();
    }


    @FXML
    protected void pageTrophees() {
        try {
            TrophyController trophyController = fxWeaver.loadController(TrophyController.class);
            trophyController.setUser(user);
            trophyController.show();

            stage = (Stage) anchor.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void pageStatistiques(ActionEvent actionEvent) {
        try {
            StatistiquesController statistiquesController = fxWeaver.loadController(StatistiquesController.class);
            statistiquesController.setUser(user);
            statistiquesController.show();

            stage = (Stage) anchor.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
