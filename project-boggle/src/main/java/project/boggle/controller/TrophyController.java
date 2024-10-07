package project.boggle.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.boggle.model.User;
import project.boggle.model.UserTrophy;

@Component
@FxmlView
public class TrophyController {

    private final FxWeaver fxWeaver;
    @FXML
    public ImageView completed;
    @FXML
    private Button retour;

    @FXML
    private Label succes;

    @FXML
    private Label un;
    @FXML
    private Label deux;
    @FXML
    private Label trois;
    @FXML
    private Label quatre;
    @FXML
    private Label cinq;
    @FXML
    private Label six;
    @FXML
    private Label sept;
    @FXML
    private Label huit;
    @FXML
    private Label neuf;
    @FXML
    private AnchorPane anchor;

    private Stage stage;
    private User user;

    @Autowired
    public TrophyController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(anchor));

    }

    public void init(String val) {
        UserTrophy saveTrophy = user.getUserTrophy();
        succes.setText("" + saveTrophy.nbrTrue());
        if (saveTrophy.isGG()) un.setText(val);
        if (saveTrophy.isTwelve()) deux.setText(val);
        if (saveTrophy.isTwoHundreds()) trois.setText(val);
        if (saveTrophy.isGhost()) quatre.setText(val);
        if (saveTrophy.isVowels()) cinq.setText(val);
        if (saveTrophy.isZero()) six.setText(val);
        if (saveTrophy.isRed()) sept.setText(val);
        if (saveTrophy.isYellow()) huit.setText(val);
        if (saveTrophy.isGreen()) neuf.setText(val);

    }

    @FXML
    protected void retour() {
        try {
            MenuController menuController = fxWeaver.loadController(MenuController.class);
            menuController.setUser(user);
            menuController.show();

            stage = (Stage) retour.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setUser(User user) {
        this.user = user;
        init("1");
    }

    public void show() {
        stage.show();
    }
}
