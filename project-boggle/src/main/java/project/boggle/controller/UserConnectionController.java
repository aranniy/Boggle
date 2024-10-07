package project.boggle.controller;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.boggle.model.User;
import project.boggle.model.UserSettings;

@Component
@FxmlView
public class UserConnectionController {
    public static String name;
    private final FxWeaver fxWeaver;
    private final RestTemplate restTemplate;
    boolean showPassword;
    @FXML
    private Pane eye;
    @FXML
    private PasswordField userPassword;
    @FXML
    private TextField username;
    @FXML
    private Tooltip tooltip;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Label wrong;
    private Stage stage;

    @Value("${url}")
    private String url;

    @Autowired
    public UserConnectionController(FxWeaver fxWeaver,
                                    RestTemplate restTemplate) {
        this.fxWeaver = fxWeaver;
        this.restTemplate = restTemplate;
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(anchor));

        tooltip.setAutoHide(false);
        tooltip.setMinWidth(50);
        username.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));

        userPassword.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));
    }

    @FXML
    protected void passwordDifficult() {
        tooltip.setText(userPassword.getText());
    }

    @FXML
    protected void validate() {
        //verif que le User exist
        User user = restTemplate.getForObject(
                url + "/userConnection?name={name}&password={password}", User.class, username.getText(), userPassword.getText());


        if (user == null) {
            wrong.setOpacity(1);
            username.setText("");
            userPassword.setText("");
        } else {
            name = username.getText();
            user.setUserSettings(new UserSettings());
            MenuController menuController = fxWeaver.loadController(MenuController.class);
            menuController.setUser(user);
            menuController.show();

            stage = (Stage) username.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    protected void createAccount() {
        fxWeaver.loadController(NewUserController.class).show();
        stage.close();
    }

    @FXML
    protected void changeEye() {
        if (showPassword) {
            showPassword = false;
            eye.setStyle("-fx-background-image: url('png/eyeClosed.png')");
            tooltip.hide();
        } else {
            showPassword = true;
            eye.setStyle("-fx-background-image: url('png/eyeOpen.png')");
            Point2D p = userPassword.localToScene(userPassword.getBoundsInLocal().getMaxX(), userPassword.getBoundsInLocal().getMaxY());
            tooltip.setText(userPassword.getText());
            tooltip.show(userPassword,
                    p.getX() + userPassword.getScene().getX() + userPassword.getScene().getWindow().getX(),
                    p.getY() + userPassword.getScene().getY() + userPassword.getScene().getWindow().getY());
        }
    }


    public void show() {
        stage.show();
    }
}
