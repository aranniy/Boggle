package project.boggle.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import project.boggle.model.User;

@Component
@FxmlView
public class StatistiquesController {

    private final FxWeaver fxWeaver;

    @FXML
    private Text name;
    @FXML
    private Text coins;
    @FXML
    private Text bestScore;
    @FXML
    private Text lastScore;
    @FXML
    private Text average;
    @FXML
    private Text nbGame;
    @FXML
    private AnchorPane anchor;

    private User user;
    private Stage stage;

    public StatistiquesController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    public void setUser(User u) {
        user = u;
        name.setText(u.getName() + "");
        coins.setText(u.getCoins() + "");
        bestScore.setText(u.getBestScore() + "");
        if (u.getNumberOfGamesPlayed() > 0) {
            lastScore.setText(u.getTenLastScores());
        } else {
            lastScore.setText("None played games");
        }
        average.setText(u.getMoyenne() + "");
        nbGame.setText(u.getNumberOfGamesPlayed() + "");
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(anchor));
    }

    @FXML
    protected void retour() {
        try {
            MenuController menuController = fxWeaver.loadController(MenuController.class);
            menuController.setUser(user);
            menuController.show();

            stage = (Stage) name.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        stage.show();
    }

}
