package com.example.restservice;

import java.io.Serializable;

public class UserTrophy implements Serializable {
    private boolean gg;
    private boolean twelve;
    private boolean twohundreds;
    private boolean ghost; // si le joueur écrit ghost/fantome/fantasma/phantom
    private boolean vowels;
    private boolean zero;
    private boolean red;
    private boolean yellow;
    private boolean green;


    public int nbrTrue() {
        int cpt = 0;

        if (gg) cpt++;
        if (twelve) cpt++;
        if (twohundreds) cpt++;
        if (ghost) cpt++;
        if (vowels) cpt++;
        if (zero) cpt++;
        if (red) cpt++;
        if (yellow) cpt++;
        if (green) cpt++;

        return cpt;

    }

    public boolean isGG() {
        return gg;
    }

    public boolean isTwelve() {
        return twelve;
    }

    public boolean isTwoHundreds() {
        return twohundreds;
    }

    public boolean isGhost() {
        return ghost;
    }

    public boolean isVowels() {
        return vowels;
    }

    public boolean isZero() {
        return zero;
    }

    public boolean isRed() {
        return red;
    }

    public boolean isYellow() {
        return yellow;
    }

    public boolean isGreen() {
        return green;
    }

    public void valideGG() {
        gg = true;
    }

    public void valideTwelve() {
        twelve = true;
    }

    public void valideTwohundreds() {
        twohundreds = true;
    }

    public void valideGhost() {
        ghost = true;
    }

    public void valideVowels() {
        vowels = true;
    }

    public void valideZero() {
        zero = true;
    }

    public void valideRed() {
        red = true;
    }

    public void valideYellow() {
        yellow = true;
    }

    public void valideGreen() {
        green = true;
    }

}
