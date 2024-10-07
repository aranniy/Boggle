package com.example.restservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class User implements Serializable{
    private final String name;
    private final String password;
    private final String mail;
    private UserTrophy userTrophy;
    private List<Integer> lastScore;
    private UserSettings userSettings;
    private int score;
    private int coins;
    private int numberOfGamesPlayed;
    private int indiceLastScore;
    private double moyenne;
    private int bestScore;
    private List<Opponent> opponents;
    private HashSet<String> motsMaster;
    private HashSet<String> motsPossibles;
    private List<String> lastWords;

    public User(String n, String p, String mail) {
        name = n;
        password = p;
        coins = 3;
        lastScore = new ArrayList<>();
        this.mail = mail;
        userTrophy = new UserTrophy();
        userSettings = new UserSettings();
        opponents = new ArrayList<>();
        moyenne = 0;
        motsMaster = new HashSet<>();
    }

    public User() {
        name = "";
        password = "";
        coins = 3;
        lastScore = new ArrayList<>();
        this.mail = "";
        userTrophy = new UserTrophy();
        userSettings = new UserSettings();
        opponents = new ArrayList<>();
        moyenne = 0;
        motsMaster = new HashSet<>();
    }

    public UserTrophy getUserTrophy() {
        return userTrophy;
    }

    public void setUserTrophy(UserTrophy userTrophy) {
        this.userTrophy = userTrophy;
    }

    public String toString() {
        return "name = " + name + "\ncoins = " + coins;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int i) {
        this.score = i;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int i) {
        this.coins = i;
    }

    public boolean enoughCoins(int i) {
        return this.getCoins() >= i;
    }

    public void removeCoins(int i) {
        this.setCoins(this.getCoins() - i);
    }

    public int getNumberOfGamesPlayed() {
        return numberOfGamesPlayed;
    }

    public void setNumberOfGamesPlayed() {
        numberOfGamesPlayed++;
    }

    public List<Integer> getLastScore() {
        return lastScore;
    }

    public void setLastScore(List<Integer> lastScore) {
        this.lastScore = lastScore;
    }

    public String getTenLastScores() {
        String a = "";
        int taille = lastScore.size();
        for (int i = taille - 1; i >= taille - 11 && i >= 0; i--) {
            if (lastScore.get(i) >= 0)
                a += lastScore.get(i) + " - ";
        }
        if (a.length() >= 2)
            a = a.substring(0, a.length() - 2);
        return a;
    }

    public int giveMeLastScore() {
        return lastScore.get(lastScore.size() - 1);
    }

    public void changeLastScore(int score) {
        lastScore.add(score);
    }

    public double getMoyenne() {
        return Math.round(moyenne * 100.0) / 100.0;
    }

    public void setMoyenne(double moyenne) {
        this.moyenne = moyenne;
    }

    public void changeMoyenne(int score) {
        double nbPartie = numberOfGamesPlayed;
        moyenne = (moyenne * (nbPartie - 1) + (double) score) / nbPartie;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int score) {
        if (score > getBestScore())
            bestScore = score;
    }

    public void updateUser(int finalScore) {
        // en fin de partie
        if (motsMaster.size() > 0 && finalScore > pointIA())
            coins += userSettings.getNiveau();
        setNumberOfGamesPlayed();
        changeLastScore(finalScore);
        changeMoyenne(finalScore);
        setBestScore(finalScore);
    }

    public String getMail() {
        return mail;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings settings) {
        this.userSettings = settings;
    }

    public List<Opponent> getOpponents() {
        return opponents;
    }

    public void setOpponents(List<Opponent> opponents) {
        this.opponents = opponents;
    }

    public void addOpponents(Opponent opponent) {
        opponents.add(opponent);
    }

    public boolean opponentsExistAlready(Opponent opponent) {
        return opponents.contains(opponent);
    }

    public boolean opponentsExistAlready(String name) {
        for (Opponent o : opponents) {
            if (o.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public HashSet<String> getMotsMaster() {
        return motsMaster;
    }

    public void setMotsMaster(HashSet<String> mm) {
        motsMaster = mm;
    }

    public HashSet<String> getMotsPossibles() {
        return motsPossibles;
    }

    public void setMotsPossibles(HashSet<String> mp) {
        motsPossibles = mp;
    }

    public List<String> getLastWords() {
        return lastWords;
    }

    public void setLastWords(List<String> lw) {
        lastWords = lw;
    }

    public int pointIA() {
        int points = 0;
        for (String s : motsMaster) {
            int taille = s.length();
            switch (taille) {
                case 3:
                case 4:
                    points += 1;
                    break;
                case 5:
                    points += 2;
                    break;
                case 6:
                    points += 3;
                    break;
                case 7:
                    points += 5;
                    break;
                default:
                    points += 11;
                    break;
            }
        }
        return points;
    }


    public Opponent opponentsExist(String name) {
        for(Opponent o : opponents){
            if(o.getName().equals(name)){
                return o;
            }
        }
        return null;
    }
}
