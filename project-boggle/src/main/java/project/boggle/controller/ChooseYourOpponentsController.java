package project.boggle.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.boggle.model.Opponent;
import project.boggle.model.User;

@Component
@FxmlView
public class ChooseYourOpponentsController {
    private final FxWeaver fxWeaver;
    private final RestTemplate restTemplate;
    @FXML
    public AnchorPane anchor;
    @FXML
    public TextField opponents;
    private Stage stage;
    private User user;
    @FXML
    private Label wrong;
    @FXML
    private ListView<Button> opponentsList;
    @Value("${url}")
    private String url;

    @Autowired
    public ChooseYourOpponentsController(FxWeaver fxWeaver,
                                         RestTemplate restTemplate) {
        this.fxWeaver = fxWeaver;
        this.restTemplate = restTemplate;
    }

    public void setUser(User user) {
        this.user = restTemplate.getForObject(
                url + "/userConnection?name={name}&password={password}", User.class, user.getName(), user.getPassword());
        loadListView();
    }

    private void loadListView() {
        for (Opponent o : user.getOpponents()) {
            Button b;
            if (!o.isTurn()) {
                b = new Button(o.getName());
                EventHandler<ActionEvent> event = e -> {
                    user.setUserSettings(o.getSettings());
                    GameController gameController = ReplayController.getGameController(user.getUserSettings(), fxWeaver, user);
                    gameController.setUser(user);
                    gameController.setOnline(o);
                    gameController.show();
                    stage.close();
                };
                b.setOnAction(event);
            } else {
                b = new Button("en attente de la partie de " + o.getName());
                b.setDisable(true);
            }
            opponentsList.getItems().add(b);
        }
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(anchor));
    }

    @FXML
    protected void validate() {
        boolean exist = Boolean.TRUE.equals(restTemplate.getForObject(
                url + "/opponentsExist?userName={userName}&opponentName={opponentName}", boolean.class, user.getName(), opponents.getText()));
        if (exist) {
            Opponent opponent = restTemplate.getForObject(url + "/addOpponent?userName={userName}&opponentName={opponentName}", Opponent.class, user.getName(), opponents.getText());
            user.addOpponents(opponent);
            ParametreController parametreController = fxWeaver.loadController(ParametreController.class);
            parametreController.setUser(user);
            parametreController.setOnline(opponent);
            parametreController.show();
            stage.close();
        } else {
            wrong.setOpacity(1);
            opponents.setText("");
        }
    }

    @FXML
    protected void back() {
        try {
            MenuController menuController = fxWeaver.loadController(MenuController.class);
            menuController.setUser(user);
            menuController.show();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        stage.show();
    }

}
