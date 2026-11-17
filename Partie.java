import java.util.List;
import java.util.Scanner;

public class Partie {
    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueurEnCours;
    private List<Mot> mots;
    private int indiceMot;
    private Scanner clavier;

    public Partie(Joueur joueur1, Joueur joueur2, List<Mot> mots, Scanner clavier) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.joueurEnCours = joueur1;   
        this.mots = mots;
        this.indiceMot = 0;
        this.clavier = clavier;
    }

    // Passe la main à l'autre joueur
    private void changerJoueur() {
        if (joueurEnCours == joueur1) {
            joueurEnCours = joueur2;
        } else {
            joueurEnCours = joueur1;
        }
    }

    // Pose la question de l'indice à un joueur, retourne true s'il répond "o"
    private boolean demanderOuiNon(Joueur joueur) {
        System.out.print(joueur.getNom() + ", voulez-vous révéler la catégorie ? (o/n) : ");
        String reponse = clavier.nextLine().trim();
        return reponse.equalsIgnoreCase("o");
    }

    public void jouer() {
        for (indiceMot = 0; indiceMot < mots.size(); indiceMot++) {
            Mot mot = mots.get(indiceMot);

            System.out.println();
            System.out.println("===== Mot " + (indiceMot + 1) + " sur " + mots.size() + " =====");

            // A. Indice proposé une seule fois, pour les mots de plus de 6 lettres
            if (mot.getTexte().length() > 6) {
                System.out.println("Mot long ! Révéler la catégorie divise sa valeur par deux.");
                System.out.println(mot.afficherMasque());
                if (demanderOuiNon(joueur1) && demanderOuiNon(joueur2)) {
                    mot.revelerCategorie();
                    System.out.println("Catégorie révélée.");
                } else {
                    System.out.println("Pas d'indice pour ce mot.");
                }
            }

            while (!mot.estComplete()) {
                // B. Affichage
                System.out.println();
                System.out.println(mot.afficherMasque());
                if (mot.isCategorieRevelee()) {
                    System.out.println("Catégorie : " + mot.getCategorie());
                }
                System.out.print(joueurEnCours.getNom() + ", propose une lettre : ");

                // C. Lecture et vérification de la saisie
                String saisie = clavier.nextLine().trim();
                if (saisie.length() != 1 || !Character.isLetter(saisie.charAt(0))) {
                    System.out.println("Merci de taper une seule lettre.");
                    continue;   // on redemande, sans changer de joueur
                }

                // D. Traitement de la lettre
                if (mot.proposerLettre(saisie.charAt(0))) {
                    if (mot.estComplete()) {
                        int points = mot.getPointsBareme();
                        joueurEnCours.ajouterPoints(points);
                        System.out.println("Bravo " + joueurEnCours.getNom() + " ! Le mot était "
                                + mot.getTexte() + ". +" + points + " points");
                    } else {
                        System.out.println("Bonne lettre, rejoue !");
                    }
                } else {
                    System.out.println("Raté !");
                    changerJoueur();
                }
            }
        }
        afficherResultat();
    }

    private void afficherResultat() {
        System.out.println();
        System.out.println("===== Fin de la partie =====");
        System.out.println(joueur1.getNom() + " : " + joueur1.getPoints() + " points");
        System.out.println(joueur2.getNom() + " : " + joueur2.getPoints() + " points");

        if (joueur1.getPoints() > joueur2.getPoints()) {
            System.out.println(joueur1.getNom() + " gagne !");
        } else if (joueur2.getPoints() > joueur1.getPoints()) {
            System.out.println(joueur2.getNom() + " gagne !");
        } else {
            System.out.println("Match nul !");
        }
    }
}