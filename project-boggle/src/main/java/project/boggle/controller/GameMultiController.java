package project.boggle.controller;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.boggle.model.Board;
import project.boggle.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static project.boggle.controller.GameController.tryToPress;

@Component
@FxmlView
public class GameMultiController {
    public static long total = 60;
    public static long y = total - 1;
    public static long x;
    public static int count1, count2;
    private static Board board;
    private final FxWeaver fxWeaver;
    private final RestTemplate restTemplate;
    public ChangeUserController changeUserController;
    @FXML
    public Label score1;
    private boolean disable = false;
    private Stage stage;
    private long min, sec = 0;
    private Timer time;
    @FXML
    private AnchorPane wordsFounded;
    @FXML
    private Label wordInMaking;
    @FXML
    private Label textDisable;
    @FXML
    private Label smileySad;
    @FXML
    private Label smileyHappy;
    @FXML
    private AnchorPane viewBoard;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Label timer;
    private User user;
    @FXML
    private Button lastButtonPressed;
    @FXML
    private Label score2;
    @FXML
    private Label player1;
    @FXML
    private Label player2;
    @Value("${url}")
    private String url;


    public GameMultiController(FxWeaver fxWeaver, RestTemplate restTemplate, ChangeUserController changeUserController) {
        this.fxWeaver = fxWeaver;
        this.restTemplate = restTemplate;
        this.changeUserController = changeUserController;
    }

    public static long getTime() {
        return total;
    }

    public static void setTime(long count) {
        total = count;
    }

    public static void setTotal() {
        x = total;
    }

    @FXML
    public void initialize() {
        player2.setText(MultiplayerController.joueur);

        this.stage = new Stage();
        stage.setScene(new Scene(anchor));
        stage.setResizable(false);
        stage.setResizable(false);
        time = new Timer();
        stage.setOnCloseRequest(event -> time.cancel());
        int count = 0;
        board = new Board(4, 4);
        for (Node b : viewBoard.getChildren()) {
            Button b1 = (Button) b;
            b1.setText("" + board.getAllCases().get(count).getLetter());
            count++;
        }
    }

    public void setUser(User user) {
        this.user = user;
        player1.setText(user.getName());

        board.setUserForInit(user);
    }

    public void show() {
        stage.show();
        starting();
    }

    @FXML
    protected void buttonOnAction(ActionEvent event) {
        if (!disable) {
            hideSmiley();
            Button b1 = (Button) event.getSource();

            int pos = 0;
            for (Node b : viewBoard.getChildren()) {
                if (b1 == b) {
                    break;
                }
                pos++;
            }

            if (canBePressed(pos)) {
                if (wordInMaking.getText().equals("Word already made") || wordInMaking.getText().equals("Try again")) {
                    wordInMaking.setText("");
                }
                lastButtonPressed = b1;
                wordInMaking.setText(wordInMaking.getText() + b1.getText());
                board.setWordInMaking(wordInMaking.getText());
                b1.setDisable(true);
                board.setCaseUsedWithPos(pos);
            }
        }
    }

    private void hideSmiley() {
        smileySad.setOpacity(0);
        smileyHappy.setOpacity(0);
    }

    private void showSadSmiley() {
        smileySad.setOpacity(1);
        RotateTransition rt = new RotateTransition(Duration.millis(1000), smileySad);
        rt.setByAngle(360);
        rt.setCycleCount(1);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();
    }

    private void showHappySmiley() {
        GameController.happySmiley(smileyHappy);
    }

    private boolean canBePressed(int pos2) {
        return tryToPress(pos2, lastButtonPressed, viewBoard, board);
    }

    @FXML
    protected void keyTyped(KeyEvent event) {

        if (!event.getCharacter().equals("\u2386")) {
            hideSmiley();
            disable = true;
            textDisable.setOpacity(1);
            if (wordInMaking.getText().equals("Word already made") || wordInMaking.getText().equals("Try again")) {
                wordInMaking.setText("");
                board.setWordInMaking(wordInMaking.getText());
            }
            if (event.getCharacter().equals("\u0008") || event.getCharacter().equals("\u007F")) {
                if (wordInMaking.getText().length() > 0) {
                    wordInMaking.setText(wordInMaking.getText().substring(0, wordInMaking.getText().length() - 1));
                    board.setWordInMaking(wordInMaking.getText());
                } else {
                    disable = false;
                    textDisable.setOpacity(0);
                }
            } else if (theCharacterIsPlayable(event.getText())) {
                wordInMaking.setText(wordInMaking.getText() + event.getText().toUpperCase());
                board.setWordInMaking(wordInMaking.getText());
            }
        } else {
            validate();
        }
    }

    private boolean theCharacterIsPlayable(String character) {
        if (character.length() > 0) {
            return board.isTheCharacterPlayable(character.toUpperCase());
        } else {
            return false;
        }
    }

    @FXML
    public void cancel() {
        board.resetUsed();
        lastButtonPressed = null;
        wordInMaking.setText("");
        for (Node b : viewBoard.getChildren()) {
            b.setDisable(false);
        }
        disable = false;
        textDisable.setOpacity(0);
    }

    @FXML
    public void validate() {
        lastButtonPressed = null;
        if (!wordInMaking.getText().equals("Word already made") || !wordInMaking.getText().equals("Try again")) {
            if (board.isTheWordInMakingCorrect()) {
                if (board.isTheWordInMakingANewWord()) {
                    setWordsFoundedText();
                    board.resetUsed();
                    board.aGoodWordAsBeenFounded();
                    wordInMaking.setText("");
                    updateScore();
                    showHappySmiley();
                } else {
                    board.resetUsed();
                    showSadSmiley();
                    wordInMaking.setText("Word already made");
                }
            } else {
                board.resetUsed();
                showSadSmiley();
                wordInMaking.setText("Try again");
            }
        }
        for (Node b : viewBoard.getChildren()) {
            b.setDisable(false);
        }
        disable = false;
        textDisable.setOpacity(0);
    }

    public void setWordsFoundedText() {
        GameController.wordFoundedText(wordsFounded, wordInMaking);
    }

    public void ConvertTime() {
        min = TimeUnit.SECONDS.toMinutes(total);
        sec = total - (min * 60);
        timer.setText(affiche(min) + ":" + affiche(sec));
        total--;

    }

    public String affiche(long val) {
        if (val < 10) {
            return 0 + "" + val;
        } else
            return val + "";
    }

    public void starting() {
        // Changer la valeur dans setTime pour modifier la durée de jeu

        long tmp = 60;
        setTime(tmp);
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    ConvertTime();

                    timer.setText(affiche(min) + ":" + affiche(sec));
                    if (total == y - 15 && total > 0) {
                        y = total;
                        setTotal();

                        //Remise à 0 wordInMaking
                        board.resetUsed();
                        lastButtonPressed = null;
                        wordInMaking.setText("");
                        for (Node b : viewBoard.getChildren()) {
                            b.setDisable(false);
                        }
                        disable = false;
                        textDisable.setOpacity(0);

                        changeUser();
                    }

                    if (total < 0) {
                        time.cancel();
                        timer.setText("TIME OVER !");
                        highscore();
                        replay();
                        stage.close();
                    }
                });
            }
        };
        time.scheduleAtFixedRate(timertask, 0, 1000);

    }

    protected void changeUser() {
        if (isP1()) {
            changeUserController.setChangePlayer(MultiplayerController.joueur);

        } else if (changeUserController.getChangePlayer().equals(MultiplayerController.joueur)) {
            changeUserController.setChangePlayer(user.getName());

        }
        changeUserController.setUser(user);
        changeUserController.show();
    }

    public boolean isP1() {
        return changeUserController.getChangePlayer().equals(user.getName());
    }

    public void replay() {
        try {
            player2.setText("");
            total = 60;
            y = total - 1;
            ChangeUserController.setX(0);
            user.updateUser(board.getScore());

            Map<String, String> params = new HashMap<>();
            GameController.sendUserScore(params, this.user, board, url, restTemplate);

            ReplayMultiController replayMultiController = fxWeaver.loadController(ReplayMultiController.class);
            replayMultiController.setUser(this.user);
            replayMultiController.show();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void highscore() {
        count1 = Integer.parseInt(score1.getText());
        count2 = Integer.parseInt(score2.getText());
    }

    @FXML
    public void updateScore() {

        count1 = Integer.parseInt(score1.getText());
        count2 = Integer.parseInt(score2.getText());
        int val = 0;
        int taille = board.getWordInMaking().length();
        if (taille == 3 || taille == 4) val = 1;
        else if (taille == 5) val = 2;
        else if (taille == 6) val = 3;
        else if (taille == 7) val = 5;
        else if (taille > 7) val = 11;

        if (isP1()) {
            score1.setText(String.valueOf(count1 + val));

        } else score2.setText(String.valueOf(count2 + val));


    }
}

