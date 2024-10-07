package project.boggle.model;

import java.util.Scanner;

public class Player {
    private final Scanner scanAnswer;
    private final int score = 0;
    private String name;
    private int coins; // monnaie contre l'utilisation des bonus

    public Player() {
        name = "The secret guy";
        scanAnswer = new Scanner(System.in);
        this.coins = 4; // on l'initialise à 4
    }

    public void setName(String n) {
        name = n;
    }

    public String demanderStr(String q) { // permet de récupérer les réponses des joueurs via le scanner
        System.out.print(q);
        String reponse = scanAnswer.next();
        stop(reponse);
        return reponse.toLowerCase();
    }

    public String verificationMot(String s) { // verifie que le joueur répond bien par yes ou no
        while (!s.equals("yes") && !s.equals("no")) {

            s = demanderStr("Error. Please retype your answer. ");
        }
        return s;
    }

    public int verificationChiffre(String reponse) {
        while (!reponse.matches("[+-]?\\d*(\\.\\d+)?")) { // verifie que le joueur a bien rentré un chiffre
            reponse = demanderStr("Error. Please type a number. ");
        }
        return Integer.parseInt(reponse);
    }

    public boolean wantToPlay() {
        String a = demanderStr("Do you want to play ? (yes/no) ");
        a = verificationMot(a);
        return a.equals("yes");
    }

    public void askSetName() {
        String a = demanderStr("Do you want to set a name ? (yes/no) ");
        a = verificationMot(a);
        if (a.equals("yes")) {
            a = demanderStr("So, what's your name ? ");
            setName(a);
        }
    }

    public int askX(Board b) {
        System.out.println("\nYou are now going to choose a letter:\n");
        String reponse = demanderStr("  - which line do you want to choose ? ");
        int a = verificationChiffre(reponse);
        if (a < b.getHeight() && a >= 0) {
            return a;
        } else {
            return notInTheBoard(b);
        }
    }

    public int askY(Board b) {
        String reponse = demanderStr("  - now the column ? ");
        int a = verificationChiffre(reponse);
        if (a < b.getLength() && a >= 0) {
            return a;
        } else {
            return notInTheBoard(b);
        }
    }

    public int notInTheBoard(Board b) {
        String reponse = demanderStr("The number you choose isn't part of the board. Please try again");
        int a = verificationChiffre(reponse);
        if (a < b.getHeight() && a >= 0) {
            return a;
        } else {
            return notInTheBoard(b);
        }
    }

    public void showLetter(int x, int y, Board b) {
        char c = b.findMeTheRightCase(x, y).getLetter();
        System.out.println("\nYou picked the letter " + c);
    }

    public void showWordInMaking(Board b) {
        System.out.println("The word you are currently building is for now : " + b.getWordInMaking() + "\n");
    }

    public boolean askValidateWord() {
        String a = demanderStr("Do you want to validate the word in making ? (yes/no) ");
        a = verificationMot(a);
        return a.equals("yes");
    }

    public void wordAlreadyPlayed() {
        System.out.println("This word has already been played");
    }

    public void wordIsCorrect(Board b) {
        System.out.println("The word : \"" + b.getWordInMaking() + "\" exist in the dictionnary");
    }

    public void wordIsInCorrect(Board b) {
        System.out.println("The word : \"" + b.getWordInMaking() + "\" doesn't exist in the dictionnary");
    }

    public void showCurrentScore(Board b) {
        System.out.println("Your current score is: " + b.getScore());
    }

    public boolean askDeleteWordInMaking(Board b) {
        String a = demanderStr("Do you want to delete the word in making ?(yes/no)");
        a = verificationMot(a);
        return a.equals("yes");
    }

    public void misplacedCase() {
        System.out.println("You have to use a letter next to the last one.");
    }

    public void stop(String choix) {
        if (choix.equals("stop")) System.exit(0);
    }

    public String bonusOrDelete() {
        String a = demanderStr("\nChoose your action :\n  - delete the word in making (write delete)\n  - use a bonus (write bonus)\n  - do nothing (write no)\n");
        while (!a.equals("bonus") && !a.equals("delete") && !a.equals("no")) {
            a = demanderStr("Error, you have to write bonus, delete or no. ");
        }
        return a;
    }

    public String chooseBonus() {
        String reponse = "";
        if (this.coins > 0) {
            System.out.println("\nYou have " + this.coins + " coins. ");
            afficheBonus();
            reponse = demanderStr("Which bonus do you want ? ");
        } else {
            System.out.println("\nYou don't have any coins...\n");
            reponse = "no";
        }
        return reponse;
    }

    public void removeCoins(int i) {
        this.setCoins(this.getCoins() - i);
    }

    public boolean verificationCoins(int i) {
        return this.coins >= i;
    }

    public int getCoins() {
        return this.coins;
    }

    public void setCoins(int i) {
        this.coins = i;
    }

    public void help() {
        afficheBonus();
        System.out.println("If you want to stop the game, just type : stop ");
        System.out.println("A word can only be validated from 3 letters\n");

    }

    public void afficheBonus() {
        System.out.println("\nYou have access to several bonuses :\n\n   - addchrono (3 coins) : add 30 seconds to the timer.\n   - brasier (2 coins) : double points for the two next words.\n   - rotation (2 coins) : shake the game board.\n   - inspiration (4 coins) : indicate a word not found yet.\n   - vision (2 coins) : give a clue to a word not found yet.\n");
    }
}
