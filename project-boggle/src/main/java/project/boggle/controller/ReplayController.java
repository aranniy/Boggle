package project.boggle.controller;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.boggle.model.User;
import project.boggle.model.UserSettings;

import java.util.HashSet;

@Component
@FxmlView
public class ReplayController {
    private final FxWeaver fxWeaver;
    private final RestTemplate restTemplate;
    @FXML
    private Text score;
    @FXML
    private Text master;
    @FXML
    private Text scoreMaster;
    @FXML
    private Text allWords;
    @FXML
    private Text yourWords;
    @FXML
    private Text masterWords;
    @FXML
    private AnchorPane anchor;
    private User user;
    private Stage stage;
    @Value("${url}")
    private String url;

    @Autowired
    public ReplayController(FxWeaver fxWeaver, RestTemplate restTemplate) {
        this.fxWeaver = fxWeaver;
        this.restTemplate = restTemplate;
    }

    static GameController getGameController(UserSettings save, FxWeaver fxWeaver, User user) {
        GameController gameController = null;
        if (save.getTailleGrille() == 4) {
            gameController = fxWeaver.loadController(GameController4X4.class);
        }
        if (save.getTailleGrille() == 5) {
            gameController = fxWeaver.loadController(GameController5X5.class);
        }
        if (save.getTailleGrille() == 6) {
            gameController = fxWeaver.loadController(GameController6X6.class);
        }

        assert gameController != null;
        gameController.setUser(user);
        return gameController;
    }

    public static String[] ordreAlpha(String[] tab) {
        for (int i = 0; i < tab.length - 1; i++) {
            for (int j = i + 1; j < tab.length; j++) {
                if (tab[i].compareTo(tab[j]) > 0) {
                    String tmp = tab[i];
                    tab[i] = tab[j];
                    tab[j] = tmp;
                }
            }
        }
        return tab;
    }

    public static String[] ordreNum(String[] tab) {
        for (int i = 0; i < tab.length - 1; i++) {
            for (int j = i + 1; j < tab.length; j++) {
                if (tab[i].length() < tab[j].length()) {
                    String tmp = tab[i];
                    tab[i] = tab[j];
                    tab[j] = tmp;
                }
            }
        }
        return tab;
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(anchor));
    }

    @FXML
    protected void ButtonYes() {
        try {
            UserSettings save = user.getUserSettings();

            GameController gameController = getGameController(save, fxWeaver, user);

            gameController.show();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void ButtonNo() {
        try {
            MenuController menuController = fxWeaver.loadController(MenuController.class);
            user.setUserSettings(new UserSettings());
            menuController.setUser(user);
            menuController.show();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user) {
        this.user = user;
        score.setText("Your score : " + user.giveMeLastScore());
        int point = user.pointIA();
        scoreMaster.setText("Master's score : " + point);
        if (point < user.giveMeLastScore())
            master.setText("You beat The Master! You get 1 coin.");

        StringBuilder a = new StringBuilder();
        for (String s : user.getLastWords()) {
            a.append(s).append("\n");
        }
        yourWords.setText(a.toString());

        a = new StringBuilder();
        for (String s : organize(user.getMotsMaster())) {
            a.append(s).append("\n");
        }
        masterWords.setText(a.toString());

        a = new StringBuilder();
        for (String s : organize(user.getMotsPossibles())) {
            a.append(s).append("\n");
        }
        allWords.setText(a.toString());
    }

    public String[] organize(HashSet<String> liste) {
        String[] res = liste.toArray(new String[0]);
        return ordreNum(ordreAlpha(res));
    }

    public void show() {
        stage.show();
    }
}
