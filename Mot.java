import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Mot {
    private String texte;
    private String categorie;
    private Set<Character> lettresTrouvees = new HashSet<>();
    private int positionRevelee;
    private boolean categorieRevelee = false;

    public Mot(String texte, String categorie) {

        this.texte = texte.toUpperCase();
        this.categorie = categorie;

        Random random = new Random();
        this.positionRevelee = random.nextInt(this.texte.length());

    }

    public String getTexte() {
        return this.texte;
    }

    public String getCategorie() {
        return this.categorie;
    }

    public String afficherMasque() {

        String motEnCour = "";

        for (int i = 0; i < this.texte.length(); i++) {
            if (lettresTrouvees.contains(this.texte.charAt(i)) || i == this.positionRevelee) {

                motEnCour += this.texte.charAt(i) + " ";

            } else {
                motEnCour += "_ ";
            }
        }
        return motEnCour;
    }

    public boolean proposerLettre(char lettre) {
        char lettreMajuscule = Character.toUpperCase(lettre);

        if (!this.texte.contains(Character.toString(lettreMajuscule))) {
            return false;
        }

        if (this.lettresTrouvees.contains(lettreMajuscule)) {
            return false;
        }

        for (int i = 0; i < this.texte.length(); i++) {
            if (this.texte.charAt(i) == lettreMajuscule
                    && i != this.positionRevelee) {
                lettresTrouvees.add(lettreMajuscule);
                return true;
            }
        }

        return false;
    }

    public boolean estComplete() {
        String motEncour = afficherMasque();
        if (motEncour.contains("_")) {
            return false;
        } else {
            return true;
        }
    }

    public int getPointsBareme() {

        if (this.texte.length() >= 3) {
            switch (this.texte.length()) {
                case 3:
                    return 15;
                case 4:
                    return 20;
                case 5:
                    return 30;
                default:
                    if (this.categorieRevelee) {
                        return 25;
                    }
                    return 50;

            }
        } else {
            return 0;
        }

    }

    public void revelerCategorie() {
        this.categorieRevelee = true;
    }

    public boolean isCategorieRevelee() {
        return this.categorieRevelee;
    }
}
