package project.boggle.model;

public class Game {
    private final Board board;
    private final Player player;
    private final Chrono chrono;
    private boolean brasier;
    private int compteur;

    public Game() {
        player = new Player();
        board = new Board(4, 4);
        chrono = new Chrono();
        brasier = false;
        compteur = 0;
    }

    public void play() {
        if (player.wantToPlay()) {
            player.askSetName();
            player.help();
            boolean keepPlaying = true;
            chrono.starting();
            while (keepPlaying) {
                player.showWordInMaking(board);
                board.printBoardInTerminal();
                if (board.getWordInMaking().length() > 2 && player.askValidateWord()) {
                    if (board.containsValide()) {
                        player.wordAlreadyPlayed();
                    } else if (board.isTheWordInMakingCorrect()) {
                        player.wordIsCorrect(board);
                        board.ajoutMotValide();
                        board.updateScore(brasier);
                        verifBrasier(); // permet de verifier que le bonus dure seulement 2 tour

                    } else {
                        player.wordIsInCorrect(board);
                    }
                    reset();
                    player.showCurrentScore(board);
                } else {
                    int x = player.askX(board);
                    int y = player.askY(board);
                    Case c = board.findMeTheRightCase(x, y);
                    if (c.isTheCaseUsed()) {
                        System.out.println("You've already used this letter...");
                    } else if (board.isNextTo(c)) {
                        player.showLetter(x, y, board);
                        board.selectLetter(x, y);
                        board.setLastCase(c);
                    } else {
                        player.misplacedCase();
                    }
                    String choix = player.bonusOrDelete();
                    if (choix.equals("bonus")) actionBonus(player.chooseBonus());
                    if (choix.equals("delete")) reset();
                }
            }
            // FIXME : La suite du programme ne s'affiche pas -> le timer stop le programme ....
            System.out.println("END OF THE GAME. This is your score: " + board.getScore());
            gainCoins();
            chrono.getTimer().cancel();
        } else {
            System.out.println("Game cancelled.");
            System.exit(0);
        }
        // TODO : AFFICHER SCORE DES 10 DERNIERES PARTIES
    }

    public void reset() {
        board.resetUsed();
        board.resetWord();
    }

    public void actionBonus(String bonus) {
        boolean action = false;
        if (player.getCoins() > 0) {
            switch (bonus) {
                case "inspiration":
                    if (player.verificationCoins(4)) {
                        player.removeCoins(4);
                        board.inspiration();
                        action = true;
                    }
                    break;
                case "vision":
                    if (player.verificationCoins(2)) {
                        player.removeCoins(2);
                        board.vision();
                        action = true;
                    }
                    break;
                case "rotation":
                    if (player.verificationCoins(2)) {
                        player.removeCoins(2);
                        board.rotation();
                        action = true;
                    }
                    break;
                case "addchrono":
                    if (player.verificationCoins(3)) {
                        player.removeCoins(3);
                        chrono.addSec(30);
                        System.out.println("30 seconds were succesfully added to the timer !\n");
                        action = true;
                    }
                    break;
                case "brasier":
                    if (player.verificationCoins(2)) {
                        player.removeCoins(2);
                        brasier = true;
                        System.out.println("Your next two words will count double !!\n");
                        action = true;
                    }
                    break;
                default:
                    System.out.println("This bonus doesn't exist.\n");
                    action = true;
                    break;
            }
            if (!action) System.out.println("You don't have enough coins...");
        }
    }

    public void verifBrasier() {
        if (brasier) compteur++;
        if (compteur > 1) {
            compteur = 0;
            brasier = !brasier;
        }
    }

    public void gainCoins() {
        if (board.getScore() > 9) {
            player.setCoins(player.getCoins() + 1);
            System.out.println("Congratulations ! You won 1 coins !");
        }
    }
}
