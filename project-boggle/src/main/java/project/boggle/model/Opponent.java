package project.boggle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Opponent implements Serializable {
    private String name;
    private List<Integer> score;
    private UserSettings settings;
    private boolean turn;

    public Opponent(String name) {
        this.name = name;
        score = new ArrayList<>();
        settings = new UserSettings();
    }

    public Opponent(String name, UserSettings settings) {
        this.name = name;
        score = new ArrayList<>();
        this.settings = settings;
    }

    public Opponent() {
        name = "";
        score = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        this.settings = settings;
    }

    public List<Integer> getScore() {
        return score;
    }

    public void setScore(List<Integer> score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score.add(score);
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }
}