package project.boggle.model;

public class Case {
    private final char letter;
    private int x, y;
    private boolean used;

    public Case(int x, int y, char letter) {
        this.x = x;
        this.y = y;
        this.letter = letter;
        used = false;
    }

    public boolean isTheCaseUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public char getLetter() {
        return letter;
    }

    public int getX() {
        return x;
    }

    public void setX(int val) {
        this.x = val;
    }

    public int getY() {
        return y;
    }

    public void setY(int val) {
        this.y = val;
    }
}
