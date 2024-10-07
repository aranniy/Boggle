package project.boggle.controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import project.boggle.model.User;

@Component
@FxmlView
public class MenuController {
    private final FxWeaver fxWeaver;
    private Stage stage;

    @FXML
    private AnchorPane anchor;

    @FXML
    private ImageView cherry;

    @FXML
    private ImageView pacman1;

    private User user;

    public MenuController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    static void startAnimation(ImageView pacman, ImageView cherry) {
        animation(pacman);
        animation(cherry);
    }

    static void animation(Node node) {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(node);
        translate.setDuration(Duration.millis(1000));
        translate.setCycleCount(TranslateTransition.INDEFINITE);
        translate.setByX(500);
        translate.setAutoReverse(true);
        translate.play();
    }

    public void setUser(User u) {
        user = u;
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(anchor));
        startAnimation(pacman1, cherry);

    }

    @FXML
    protected void buttonSOLO() {
        ParametreController parametreController = fxWeaver.loadController(ParametreController.class);
        parametreController.setUser(user);
        parametreController.show();
        stage.close();
    }

    @FXML
    protected void buttonMULTI(ActionEvent event) {
        MultiplayerController multiplayerController = fxWeaver.loadController(MultiplayerController.class);
        multiplayerController.setUser(user);
        multiplayerController.show();
        stage.close();
    }

    @FXML
    protected void buttonOnline(ActionEvent event) {
        ChooseYourOpponentsController chooseYourOpponentsController = fxWeaver.loadController(ChooseYourOpponentsController.class);
        chooseYourOpponentsController.setUser(user);
        chooseYourOpponentsController.show();
        stage.close();
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
    protected void pageStatistiques() {
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

    @FXML
    protected void help() {
        try {
            HelpController helpController = fxWeaver.loadController(HelpController.class);
            helpController.setUser(user);
            helpController.show();

            stage = (Stage) anchor.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        stage.show();
    }
}