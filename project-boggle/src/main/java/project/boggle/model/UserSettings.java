package project.boggle.model;

public class UserSettings {
    private int tailleGrille;
    private long timer;
    private String langue;
    private int niveau;

    public UserSettings() {
        tailleGrille = 4;
        timer = 180;
        langue = "fr";
        niveau = 1;
    }

    public UserSettings(int tailleGrille, long timer, String langue, int niveau) {
        this.tailleGrille = tailleGrille;
        this.timer = timer;
        this.langue = langue;
        this.niveau = niveau;
    }

    public int getTailleGrille() {
        return tailleGrille;
    }

    public void setTailleGrille(int t) {
        tailleGrille = t;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long t) {
        timer = t;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String l) {
        langue = l;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int n) {
        niveau = n;
    }
}
