package project.boggle.controller;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.boggle.model.User;

@Component
@FxmlView
public class MultiplayerController {
    public static String joueur;
    private final FxWeaver fxWeaver;
    @FXML
    public TextField player2;
    private Stage stage;
    @FXML
    private AnchorPane anchor;
    @FXML
    private User user;


    @Autowired
    public MultiplayerController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize() {
        joueur = player2.getText();
        this.stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(anchor));

    }

    @FXML
    protected void setPlayer2() {
        if (!player2.getText().equals("")) {
            joueur = player2.getText();
        } else {
            joueur = "Player 2";
        }
    }

    @FXML
    protected void buttonStart() {
        try {
            setPlayer2();
            ChangeUserController changeUserController = fxWeaver.loadController(ChangeUserController.class);
            changeUserController.setUser(user);
            changeUserController.show();
            stage = (Stage) anchor.getScene().getWindow();
            stage.close();
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
