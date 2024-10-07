package project.boggle.controller;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.boggle.model.User;

@Component
@FxmlView
public class HelpController {
    private final FxWeaver fxWeaver;
    private Stage stage;

    private User user;

    @FXML
    private Button retour;

    @FXML
    private AnchorPane anchor;

    @Autowired
    public HelpController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(anchor));
    }

    public void show() {
        stage.show();
    }

    public void setUser(User u) {
        user = u;
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
}
