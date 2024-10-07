package project.boggle.model;


import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    private final int length;
    private final int height;

    private final List<Case> allCases = new ArrayList<>();
    private final List<String> lastWords = new ArrayList<>();
    private final ArrayList<String> listeValides;
    private String wordInMaking = "";
    private int score;
    private List<String> dico;
    private HashSet<String> listeMotPossible;
    private Case lastCase;

    private UserSettings saveSettings;

    public Board(int height, int length) {
        this.length = length;
        this.height = height;
        initCases();
        this.score = 0;
        this.listeValides = new ArrayList<>();
    }

    // return true si a est avant b dans l'ordre alphabetique
    public static boolean alphabet(String a, String b) {
        int i = 0;
        while (i < a.length() && i < b.length()) {
            if (a.charAt(i) > b.charAt(i))
                return false;
            if (a.charAt(i) < b.charAt(i) || a.charAt(i) == b.charAt(i) && a.length() == i + 1)
                return true;
            i++;
        }
        return false;
    }

    // Source : https://www.developpez.net/forums/d460531/java/general-java/langage/supprimer-accents-chaine/
    public static String removeAccent(String s) {
        s = s.replaceAll("\\p{Punct}", ""); // pour enlever les tirets
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
    }

    public void setUserForInit(User user) {
        saveSettings = user.getUserSettings();
        loadDictionary();
        setMotsJouables();
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Case> getAllCases() {
        return allCases;
    }

    public String getWordInMaking() {
        return wordInMaking;
    }

    public void setWordInMaking(String s) {
        wordInMaking = s;
    }

    public void initCases() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                allCases.add(new Case(i, j, findAChar()));
            }
        }

    }

    public void setPosCase() {
        int k = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                allCases.get(k).setX(i);
                allCases.get(k).setY(j);
                k++;
            }
        }
    }

    public HashSet<String> getListeMotPossible() {
        return listeMotPossible;
    }

    public List<String> getLastWords() {
        return lastWords;
    }

    public ArrayList<String> getListeValides() {
        return listeValides;
    }

    //Source: https://fr.wikipedia.org/wiki/Fr%C3%A9quence_d%27apparition_des_lettres_en_fran%C3%A7ais
    public char findAChar() {
        Random rd = new Random();
        float i = rd.nextInt(1000000);
        i = i / 1000000;
        if (i <= .08173) return 'A';    // a = 8.173% et b = 0.901 : on additionne progressivement
        else if (i <= .09074) return 'B';    // 9074 = 8173 + 901
        else if (i <= .12420) return 'C';
        else if (i <= .16089) return 'D';
        else if (i <= .32805) return 'E';
        else if (i <= .33871) return 'F';
        else if (i <= .34737) return 'G';
        else if (i <= .35474) return 'H';
        else if (i <= .43053) return 'I';
        else if (i <= .43666) return 'J';
        else if (i <= .43740) return 'K';
        else if (i <= .49196) return 'L';
        else if (i <= .52164) return 'M';
        else if (i <= .59259) return 'N';
        else if (i <= .65078) return 'O';
        else if (i <= .67599) return 'P';
        else if (i <= .68961) return 'Q';
        else if (i <= .75654) return 'R';
        else if (i <= .83602) return 'S';
        else if (i <= .90846) return 'T';
        else if (i <= .97275) return 'U';
        else if (i <= .99113) return 'V';
        else if (i <= .99162) return 'W';
        else if (i <= .99589) return 'X';
        else if (i <= .99717) return 'Y';
        else return 'Z';
    }

    public Case findMeTheRightCase(int x, int y) {
        int result = x * length + y;
        return allCases.get(result);
    }

    public void printBoardInTerminal() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                if (j == 0) {
                    System.out.print("| " + findMeTheRightCase(i, j).getLetter() + " | ");
                } else {
                    System.out.print(findMeTheRightCase(i, j).getLetter() + " | ");
                }
            }
            System.out.println();
        }
    }

    public void selectLetter(int x, int y) { // Ajout d'une condition if pour s'assurer qu'on utilise pas la lettre une deuxième fois
        wordInMaking += findMeTheRightCase(x, y).getLetter();
        findMeTheRightCase(x, y).setUsed(true);
    }

    public void resetWord() { // remet à zero le mot en cours
        this.wordInMaking = "";
        this.lastCase = null;
    }

    public void resetUsed() { // remet à false toutes les cases utilisées pour le mot précédent
        for (Case allCase : allCases) {
            if (allCase.isTheCaseUsed()) {
                allCase.setUsed(false);
            }
        }
    }

    public boolean isTheWordInMakingCorrect() {
        return listeMotPossible.contains(wordInMaking);
    }

    public boolean isNextTo(Case c) {
        if (lastCase == null)
            return true;
        return Math.abs(lastCase.getX() - c.getX()) <= 1 && Math.abs(lastCase.getY() - c.getY()) <= 1;
    }

    public void setLastCase(Case c) {
        this.lastCase = c;
    }

    public void ajoutMotValide() {
        listeValides.add(wordInMaking);
    }

    public boolean containsValide() {
        return listeValides.contains(wordInMaking);
    }

    public void updateScore(boolean bonus) {

        int multiplicateur = bonus ? 2 : 1;
        int val;

        int taille = this.wordInMaking.length();

        switch (taille) {

            case 3:
            case 4:
                val = 1;
                break;
            case 5:
                val = 2;
                break;
            case 6:
                val = 3;
                break;
            case 7:
                val = 5;
                break;
            default:
                val = 11;
                break;

        }

        this.setScore(this.getScore() + val * multiplicateur);

    }

    // retourne true si le mot existe dans le dictionnaire
    public boolean exist(String s) {
        return existInter(s, 0, dico.size() - 1);
    }

    // Cherche si le mot est dans le dictionnaire en plus rapide
    public boolean existInter(String s, int debut, int fin) {
        int m = (debut + fin) / 2;
        if (m == debut || m == fin)
            return dico.get(debut).equals(s) || dico.get(fin).equals(s);

        if (alphabet(dico.get(m), s))
            debut = m;
        else
            fin = m;

        return existInter(s, debut, fin);
    }

    public void setMotsJouables() {
        listeMotPossible = new HashSet<String>();
        for (String s : dico) {
            if (s.length() > 2) {
                setMotsInter(null, s, 0);
            }
        }
    }

    public void setMotsInter(Case c, String mot, int i) {
        if (mot.length() == i) {
            listeMotPossible.add(mot);
        } else {
            if (c != null)
                c.setUsed(true);
            for (Case s : getCases(c, mot.charAt(i))) {
                if (!s.isTheCaseUsed())
                    setMotsInter(s, mot, i + 1);
            }
        }
        if (c != null)
            c.setUsed(false);
    }

    public ArrayList<Case> getCases(Case c, char l) {
        ArrayList<Case> res = new ArrayList<Case>();
        for (Case ca : allCases) {
            if (ca.getLetter() == l && areNextTo(c, ca))
                res.add(ca);
        }
        return res;
    }

    public boolean areNextTo(Case c1, Case c2) {
        if (c1 == null || c2 == null)
            return true;
        return Math.abs(c1.getX() - c2.getX()) <= 1 && Math.abs(c1.getY() - c2.getY()) <= 1;
    }

    // Transforme le texte fr.txt en une liste de String
    public void loadDictionary() {

        String langue = saveSettings.getLangue();
        if (langue == null) langue = "fr"; // langue par défaut

        dico = new ArrayList<>();

        try {
            URL resource = getClass().getClassLoader().getResource("Dictionnaire/" + langue + ".txt");
            assert resource != null;
            Stream<String> lines = Files.lines(Path.of(resource.toURI()), StandardCharsets.UTF_8);
            dico.addAll(lines.parallel().map(line -> removeAccent(line).toUpperCase()).collect(Collectors.toList()));

        } catch (IOException | URISyntaxException e) {
            System.out.println("Erreur de lecture");
        }

    }

    public boolean areTheTwoLettersClose(int pos1, int pos2) {
        return areTheTwoPositionClose(allCases.get(pos1).getX(), allCases.get(pos1).getY(), allCases.get(pos2).getX(), allCases.get(pos2).getY());
    }

    public boolean areTheTwoPositionClose(int x, int y, int x1, int y1) {
        //le cas ou les deux positions sont les mêmes n'éxiste pas, donc pas besoin de gêrer cet evenement particulier

        if (x - x1 == 0 || x - x1 == 1 || x - x1 == -1) {
            return y - y1 == 0 || y - y1 == 1 || y - y1 == -1;
        }
        return false;
    }

    public void setCaseUsedWithPos(int pos) {
        allCases.get(pos).setUsed(true);
    }

    public boolean isTheWordInMakingANewWord() {
        for (String s : lastWords) {
            if (wordInMaking.equals(s)) {
                return false;
            }
        }
        return true;
    }

    public void aGoodWordAsBeenFounded() {
        lastWords.add(wordInMaking);
    }

    public void inspiration() {

        String[] listeMot = listeMotPossible.toArray(new String[listeMotPossible.size()]);

        Random r = new Random();

        int val = r.nextInt(listeMotPossible.size());

        while (listeValides.contains(listeMot[val])) {
            val = r.nextInt(listeMotPossible.size());
        }

        System.out.println("\nYou can find the word : " + listeMot[val] + ", in this grid.\n");

    }

    public void vision() {

        String[] listeMot = listeMotPossible.toArray(new String[listeMotPossible.size()]);

        Random r = new Random();

        int val = r.nextInt(listeMotPossible.size());

        while (listeValides.contains(listeMot[val])) {
            val = r.nextInt(listeMotPossible.size());
        }

        char premierelettre = listeMot[val].charAt(0);

        int taille = listeMot[val].length();

        System.out.println("\nYou can find a word that is " + taille + " letters long and starts with the letter " + premierelettre + " .\n");

    }

    public void rotation() {
        Collections.shuffle(allCases);
        System.out.println("\nThe board has been rotated ! Hope you will have more luck this time !\n");
        setMotsJouables(); // chargement assez long ...

    }

    public boolean isTheCharacterPlayable(String character) {
        char[] c = character.toCharArray();
        return Character.isAlphabetic(c[0]);
    }

    public HashSet<String> jeuIA() {
        Random r = new Random();
        int niveau = saveSettings.getNiveau();
        long timer = saveSettings.getTimer();
        if (niveau == 0) niveau = 2;
        if (timer <= 0) timer = 180;

        int nbMots = (r.nextInt((int) timer / 4)) * niveau + 5;
        int a = listeMotPossible.size();
        String[] listeMot = listeMotPossible.toArray(new String[a]);
        HashSet<String> res = new HashSet<String>();
        for (int i = 0; i < nbMots; i++) {
            res.add(listeMot[r.nextInt(a)]);
        }
        return res;
    }
}
