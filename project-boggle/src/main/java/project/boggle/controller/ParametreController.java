package project.boggle.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.boggle.model.Opponent;
import project.boggle.model.User;
import project.boggle.model.UserSettings;

@Component
@FxmlView
public class ParametreController {

    private final FxWeaver fxWeaver;
    private final RestTemplate restTemplate;
    @FXML
    private Button buttonSOLO;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Label length;
    @FXML
    private Label language;
    @FXML
    private Label time;
    @FXML
    private Label level;
    @FXML
    private Button back;

    private UserSettings save;
    private User user;
    private Stage stage;
    private boolean online;
    private Opponent opponent;

    @Value("${url}")
    private String url;

    @Autowired
    public ParametreController(FxWeaver fxWeaver,
                               RestTemplate restTemplate) {
        this.fxWeaver = fxWeaver;
        this.restTemplate = restTemplate;
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(anchor));
    }

    @FXML
    public void langueFR() {
        save.setLangue("fr");
        language.setText("French");
    }

    @FXML
    public void langueANG() {
        save.setLangue("en");
        language.setText("English");
    }

    @FXML
    public void langueALL() {
        save.setLangue("de");
        language.setText("German");
    }

    @FXML
    public void langueESP() {
        save.setLangue("es");
        language.setText("Spanish");
    }

    @FXML
    public void TimerOne() {
        save.setTimer(60);
        time.setText("1 min");
    }

    @FXML
    public void TimerTwo() {
        save.setTimer(120);
        time.setText("2 min");
    }

    @FXML
    public void TimerThree() {
        save.setTimer(180);
        time.setText("3 min");
    }

    @FXML
    public void TimerFour() {
        save.setTimer(240);
        time.setText("4 min");
    }

    @FXML
    public void TimerFive() {
        save.setTimer(300);
        time.setText("5 min");
    }

    @FXML
    public void TimerSix() {
        save.setTimer(360);
        time.setText("6 min");
    }

    @FXML
    public void TailleQuatre() {
        save.setTailleGrille(4);
        length.setText("4x4");
    }

    @FXML
    public void TailleCinq() {
        save.setTailleGrille(5);
        length.setText("5x5");
    }

    @FXML
    public void TailleSix() {
        save.setTailleGrille(6);
        length.setText("6x6");
    }

    @FXML
    public void facile() {
        save.setNiveau(1);
        level.setText("1");
    }

    @FXML
    public void moyen() {
        save.setNiveau(2);
        level.setText("2");
    }

    @FXML
    public void difficile() {
        save.setNiveau(3);
        level.setText("3");
    }

    @FXML
    protected void valider() {

        try {
            GameController gameController = ReplayController.getGameController(save, fxWeaver, this.user);

            if (online) {
                this.opponent.setSettings(save);
                RequestEntity<Opponent> request = RequestEntity.post(url + "/refreshOpponents?userName={userName}", this.user.getName()).body(this.opponent);
                restTemplate.exchange(request, Void.class);
                gameController.setOnline(this.opponent);
            }

            gameController.show();

            stage = (Stage) buttonSOLO.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
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

    public void setUser(User user) {
        this.user = user;
        save = user.getUserSettings();
    }

    public void show() {
        stage.show();
    }

    public void setOnline(Opponent opponent) {
        online = true;
        this.opponent = opponent;
        back.setOpacity(0);
        back.setDisable(true);
    }
}
