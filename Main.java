import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        Dictionnaire dico = new Dictionnaire("mots.txt");
        boolean continuer = true;

        while (continuer) {
            System.out.println();
            System.out.println("===== JEU DE DEVINETTE DE MOTS =====");
            System.out.println("1. Jouer");
            System.out.println("2. Voir les règles");
            System.out.println("3. Quitter");
            System.out.print("Ton choix : ");
            String choix = clavier.nextLine().trim();

            switch (choix) {
                case "1" -> {
                    System.out.print("Nom du joueur 1 : ");
                    Joueur j1 = new Joueur(clavier.nextLine().trim());
                    System.out.print("Nom du joueur 2 : ");
                    Joueur j2 = new Joueur(clavier.nextLine().trim());

                    List<Mot> mots = dico.tirerMots(4);
                    if (mots.size() < 4) {
                        System.out.println("Le dictionnaire ne contient pas assez de mots.");
                    } else {
                        Partie partie = new Partie(j1, j2, mots, clavier);
                        partie.jouer();
                    }
                }
                case "2" -> afficherRegles();
                case "3" -> continuer = false;
                default -> System.out.println("Choix invalide.");
            }
        }
        System.out.println("À bientôt !");
        clavier.close();
    }

    private static void afficherRegles() {
        System.out.println();
        System.out.println("===== RÈGLES =====");
        System.out.println("- Deux joueurs, 4 mots tirés au sort, une lettre révélée par mot.");
        System.out.println("- À ton tour, propose une lettre.");
        System.out.println("- Bonne lettre : tu rejoues. Mauvaise lettre : la main passe.");
        System.out.println("- Celui qui complète le mot gagne ses points :");
        System.out.println("  3 lettres = 15, 4 = 20, 5 = 30, 6 et plus = 50.");
        System.out.println("- Mot de plus de 6 lettres : si les deux joueurs acceptent,");
        System.out.println("  la catégorie est révélée, la valeur du mot est reduite de moitié soit 25points.");
        System.out.println("- Le meilleur score gagne. Égalité : match nul.");
    }
}