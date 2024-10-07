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
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;
import project.boggle.model.Board;
import project.boggle.model.Opponent;
import project.boggle.model.User;
import project.boggle.model.UserSettings;

import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class GameController {

    static Board board;
    private final FxWeaver fxWeaver;
    private final RestTemplate restTemplate;
    @FXML
    public Label coins;
    @FXML
    AnchorPane viewBoard;
    @FXML
    AnchorPane anchor;
    Stage stage;
    Timer time;
    private UserSettings userSettings;
    @FXML
    private AnchorPane wordsFounded;
    @FXML
    private Label wordInMaking;
    @FXML
    private Label score;
    @FXML
    private Label textDisable;
    @FXML
    private Label smileySad;
    @FXML
    private Label smileyHappy;
    @FXML
    private Label timer;
    private User user;
    private boolean bonus = false;
    private int compteur = 0;
    @FXML
    private Label gainTrophy;
    private Button lastButtonPressed;
    private boolean disable = false;
    private long min, sec = 0;
    private long total = 180;
    private Opponent opponent;
    private boolean online;
    @Value("${url}")
    private String url;

    public GameController(FxWeaver fxWeaver, RestTemplate restTemplate) {
        this.fxWeaver = fxWeaver;
        this.restTemplate = restTemplate;
    }

    public static int count(String string, String substr) {
        int i;
        int last = 0;
        int count = 0;
        do {
            i = string.indexOf(substr, last);
            if (i != -1) count++;
            last = i + substr.length();
        } while (i != -1);
        return count;
    }

    static boolean tryToPress(int pos2, Button lastButtonPressed, AnchorPane viewBoard, Board board) {
        if (lastButtonPressed == null) {
            return true;
        }
        int pos1 = 0;
        for (Node b : viewBoard.getChildren()) {
            if (lastButtonPressed == b) {
                break;
            }
            pos1++;
        }
        return board.areTheTwoLettersClose(pos1, pos2);
    }

    static void wordFoundedText(AnchorPane wordsFounded, Label wordInMaking) {
        String lastWord = "";
        int count = 0;
        for (Node b : wordsFounded.getChildren()) {
            Label b1 = (Label) b;
            if (count == 0) {
                lastWord = b1.getText();
                b1.setText(wordInMaking.getText());
                count++;
            } else {
                String st = lastWord;
                lastWord = b1.getText();
                b1.setText(st);
            }
        }
    }

    static void happySmiley(Label smileyHappy) {
        smileyHappy.setOpacity(1);
        RotateTransition rotator = new RotateTransition(Duration.millis(10000), smileyHappy);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(0);
        rotator.setToAngle(360);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(10);
        rotator.play();
    }

    public void setUser(User u) {
        this.user = u;
        assert user != null;
        if (user.getNumberOfGamesPlayed() > 9) gainATrophy("red");
        if (user.getNumberOfGamesPlayed() > 99) gainATrophy("yellow");
        if (user.getNumberOfGamesPlayed() > 999) gainATrophy("green");
        userSettings = user.getUserSettings();
        board.setUserForInit(user);
        coins.setText(user.getCoins() + "");
        user.setMotsMaster(board.jeuIA());
        user.setMotsPossibles(board.getListeMotPossible());
    }

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(anchor));
        stage.setResizable(false);
        time = new Timer();
        stage.setOnCloseRequest(event -> time.cancel());
        int count = 0;
        board = new Board(4, 4);
        board.setUserForInit(new User());
        for (Node b : viewBoard.getChildren()) {
            Button b1 = (Button) b;
            b1.setText("" + board.getAllCases().get(count).getLetter());
            count++;
        }
    }

    public void gainATrophy(String name) {
        switch (name) {
            case "gg":
                if (!user.getUserTrophy().isGG()) {
                    user.getUserTrophy().valideGG();
                    gainTrophy.setText("Hey you won the GG Trophy !");
                    user.setCoins(user.getCoins() + 3);
                }
                break;
            case "twelve":
                if (!user.getUserTrophy().isTwelve()) {
                    user.getUserTrophy().valideTwelve();
                    gainTrophy.setText("Hey you won the Twelve Trophy !");
                    user.setCoins(user.getCoins() + 3);
                }
                break;
            case "twohundreds":
                if (!user.getUserTrophy().isTwoHundreds()) {
                    user.getUserTrophy().valideTwohundreds();
                    gainTrophy.setText("Hey you won the TwoHundreds Trophy !");
                    user.setCoins(user.getCoins() + 3);
                }
                break;
            case "ghost":
                if (!user.getUserTrophy().isGhost()) {
                    user.getUserTrophy().valideGhost();
                    gainTrophy.setText("Hey you won the Secret Trophy !");
                    user.setCoins(user.getCoins() + 3);
                }
                break;
            case "vowels":
                if (!user.getUserTrophy().isVowels()) {
                    user.getUserTrophy().valideVowels();
                    gainTrophy.setText("Hey you won the Vowels trophy !");
                    user.setCoins(user.getCoins() + 3);
                }
                break;
            case "zero":
                if (!user.getUserTrophy().isZero()) {
                    user.getUserTrophy().valideZero();
                    gainTrophy.setText("Hey you won the Zero trophy !");
                    user.setCoins(user.getCoins() + 3);
                }
                break;
            case "red":
                if (!user.getUserTrophy().isRed()) {
                    user.getUserTrophy().valideRed();
                    gainTrophy.setText("Hey you won the Red trophy !");
                    user.setCoins(user.getCoins() + 3);
                }
                break;
            case "yellow":
                if (!user.getUserTrophy().isYellow()) {
                    user.getUserTrophy().valideYellow();
                    gainTrophy.setText("Hey you won the Yellow trophy !");
                    user.setCoins(user.getCoins() + 3);
                }
                break;
            case "green":
                if (!user.getUserTrophy().isGreen()) {
                    user.getUserTrophy().valideGreen();
                    gainTrophy.setText("Hey you won the Green trophy !");
                    user.setCoins(user.getCoins() + 3);
                }
                break;
            default:
                gainTrophy.setText("");
                break;
        }

        coins.setText(user.getCoins() + "");
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
                if (wordInMaking.getText().equals("Word already made") || wordInMaking.getText().equals("You don't have enough coins...") || wordInMaking.getText().equals("Try again") || wordInMaking.getText().equals("Your next two words will count double !!") || wordInMaking.getText().equals("The board has been rotated ! Hope you will have more luck this time !") || wordInMaking.getText().contains("You can") || wordInMaking.getText().equals("30 seconds were succesfully added to the timer !")) {
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

    private boolean canBePressed(int pos2) {
        return tryToPress(pos2, lastButtonPressed, viewBoard, board);
    }

    @FXML
    public void cancel() {
        hideSmiley();
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
                    if ("AEIOUaeiou".contains(wordInMaking.getText())) gainATrophy("vowels");
                    if (wordInMaking.getText().length() > 11) gainATrophy("twelve");
                    if (count(wordInMaking.getText(), "g") == 2) gainATrophy("gg");
                    if (Objects.equals(wordInMaking.getText(), "ghost") || Objects.equals(wordInMaking.getText(), "phantom") || Objects.equals(wordInMaking.getText(), "fantasma") || Objects.equals(wordInMaking.getText(), "fantome"))
                        gainATrophy("ghost");

                    setWordsFoundedText();
                    board.resetUsed();
                    board.aGoodWordAsBeenFounded();
                    updateScore(bonus);
                    verifBrasier();
                    wordInMaking.setText("");


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

    @FXML
    public void vision() {

        int val = 2;

        if (user.enoughCoins(val)) {

            lastButtonPressed = null;

            String[] listeMot = board.getListeMotPossible().toArray(new String[0]);

            Random r = new Random();

            int mot = r.nextInt(board.getListeMotPossible().size());

            while (board.getListeValides().contains(listeMot[val])) {
                mot = r.nextInt(board.getListeMotPossible().size());
            }

            char premierelettre = listeMot[mot].charAt(0);

            int taille = listeMot[mot].length();

            wordInMaking.setText("You can find a word that is " + taille + " letters long and starts with the letter " + premierelettre + ".");

            user.removeCoins(val);
            String c = Integer.toString(user.getCoins());
            coins.setText(c);


        } else {

            wordInMaking.setText("You don't have enough coins...");

        }

    }

    @FXML
    public void inspiration() {

        int val = 4;

        if (user.enoughCoins(val)) {

            String[] listeMot = board.getListeMotPossible().toArray(new String[0]);

            Random r = new Random();

            int mot = r.nextInt(board.getListeMotPossible().size());

            while (board.getListeValides().contains(listeMot[mot])) {
                mot = r.nextInt(board.getListeMotPossible().size());
            }

            wordInMaking.setText("You can find the word : " + listeMot[mot] + ", in this grid.");

            user.removeCoins(val);
            String c = Integer.toString(user.getCoins());
            coins.setText(c);


        } else {

            wordInMaking.setText("You don't have enough coins...");

        }

    }

    @FXML
    public void brasier() {

        int val = 2;

        if (user.enoughCoins(val)) {

            bonus = true;
            wordInMaking.setText("Your next two words will count double !!");
            user.removeCoins(val);
            String c = Integer.toString(user.getCoins());
            coins.setText(c);

        } else {

            wordInMaking.setText("You don't have enough coins...");

        }

    }

    @FXML
    public void addChrono() {

        int val = 3;

        if (user.enoughCoins(val)) {

            setTime(this.getTime() + 30);
            wordInMaking.setText("30 seconds were succesfully added to the timer !");
            user.removeCoins(val);
            String c = Integer.toString(user.getCoins());
            coins.setText(c);

        } else {

            wordInMaking.setText("You don't have enough coins...");

        }

    }

    public void verifBrasier() {

        if (bonus) compteur++;
        if (compteur > 1) {
            compteur = 0;
            bonus = !bonus;
        }
    }

    @FXML
    public void rotation() {

        int val = 2;

        if (user.enoughCoins(val)) {

            int count = 0;
            wordInMaking.setText("The board has been rotated ! Hope you will have more luck this time !");
            Collections.shuffle(board.getAllCases());
            board.setPosCase();
            for (Node b : viewBoard.getChildren()) {
                Button b1 = (Button) b;
                b1.setText("" + board.getAllCases().get(count).getLetter());
                count++;
            }
            board.setMotsJouables();
            user.removeCoins(val);
            String c = Integer.toString(user.getCoins());
            coins.setText(c);

        } else {

            wordInMaking.setText("You don't have enough coins...");

        }

    }

    private void updateScore(boolean bonus) {
        board.updateScore(bonus);
        score.setText("" + board.getScore());
    }

    public void setWordsFoundedText() {
        wordFoundedText(wordsFounded, wordInMaking);
    }

    private void showSadSmiley() {
        smileySad.setOpacity(1);
        RotateTransition rt = new RotateTransition(Duration.millis(1000), smileySad);
        rt.setByAngle(360);
        rt.setCycleCount(1);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();
    }

    private void hideSmiley() {
        smileySad.setOpacity(0);
        smileyHappy.setOpacity(0);
    }

    private void showHappySmiley() {
        happySmiley(smileyHappy);
    }

    public long getTime() {
        return this.total;
    }

    public void setTime(long count) {
        this.total = count;
    }

    public void ConvertTime() {
        min = TimeUnit.SECONDS.toMinutes(total);
        sec = total - (min * 60);
        timer.setText("TIME LEFT: " + affiche(min) + ":" + affiche(sec));
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

        long tmp = userSettings.getTimer();

        setTime(tmp);

        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    ConvertTime();
                    timer.setText("TIME LEFT: " + affiche(min) + ":" + affiche(sec));
                    if (total < 0) {
                        if (board.getScore() > 199) gainATrophy("twohundreds");
                        if (board.getScore() == 0) gainATrophy("zero");
                        time.cancel();
                        timer.setText("TIME OVER !");
                        replay();

                    }
                });
            }
        };
        time.scheduleAtFixedRate(timertask, 0, 1000);

    }


    public void replay() {
        user.setLastWords(board.getLastWords());
        if (online) {
            user.updateUser(board.getScore());
            opponent.addScore(board.getScore());
            opponent.setTurn(true);

            Map<String, String> params = new HashMap<>();
            sendUserScore(params, this.user, board, url, restTemplate);


            RequestEntity<Opponent> request = RequestEntity.post(url + "/sendEmailToOpponent?userName={userName}", this.user.getName()).body(this.opponent);
            restTemplate.exchange(request, Void.class);


//            restTemplate.postForObject(url + "/addScore", this.user.getName(), board.getScore(), String.class);
//            restTemplate.postForObject(url + "/sendEmailToOpponent", opponent, Opponent.class);
//            restTemplate.postForObject(url + "/refreshUser", user, User.class);
            OnlineGameOverController onlineGameOverController = fxWeaver.loadController(OnlineGameOverController.class);
            onlineGameOverController.setUser(this.user);
            onlineGameOverController.setOpponentName(this.opponent.getName());
            onlineGameOverController.show();

            stage = (Stage) score.getScene().getWindow();
            stage.close();
        } else {
            try {
                user.updateUser(board.getScore());

                Map<String, String> params = new HashMap<>();
                params.put("userName", this.user.getName());
                params.put("score", Integer.valueOf(board.getScore()).toString());
                URI uri = new UriTemplate(url + "/addScore?userName={userName}&score={score}").expand(params);
                RequestEntity request = RequestEntity.post(uri).build();
                restTemplate.exchange(request, Void.class);

                request = RequestEntity.post(url + "/refreshUserTrophyAndCoins?userName={userName}", this.user.getName()).body(user);
                restTemplate.exchange(request, Void.class);

                ReplayController replayController = fxWeaver.loadController(ReplayController.class);
                replayController.setUser(this.user);
                replayController.show();

                stage = (Stage) score.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void sendUserScore(Map<String, String> params, User user, Board board, String url, RestTemplate restTemplate) {
        params.put("userName", user.getName());
        params.put("score", Integer.valueOf(board.getScore()).toString());
        URI uri = new UriTemplate(url + "/addScore?userName={userName}&score={score}").expand(params);
        RequestEntity request = RequestEntity.post(uri).build();
        restTemplate.exchange(request, Void.class);

        request = RequestEntity.post(url + "/refreshUserTrophyAndCoins?userName={userName}", user.getName()).body(user);
        restTemplate.exchange(request, Void.class);
    }


    public void show() {
        stage.show();
        starting();
    }

    public void setOnline(Opponent opponent) {
        online = true;
        this.opponent = opponent;
    }
}

