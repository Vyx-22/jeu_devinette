import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FenetreJeu extends JFrame {
    private Joueur joueur1, joueur2, joueurEnCours;
    private List<Mot> mots;
    private int indiceMot = 0;

    private JLabel labelMot = new JLabel("", SwingConstants.CENTER);
    private JLabel labelInfo = new JLabel("", SwingConstants.CENTER);
    private JLabel labelScores = new JLabel("", SwingConstants.CENTER);
    private JLabel labelMessage = new JLabel(" ", SwingConstants.CENTER);
    private JTextField champLettre = new JTextField(3);

    public FenetreJeu(Joueur j1, Joueur j2, List<Mot> mots) {
        super("Jeu de devinette de mots");
        this.joueur1 = j1;
        this.joueur2 = j2;
        this.joueurEnCours = j1;
        this.mots = mots;

        // Mise en forme
        labelMot.setFont(new Font(Font.MONOSPACED, Font.BOLD, 36));
        labelInfo.setFont(new Font("Arial", Font.PLAIN, 16));
        labelScores.setFont(new Font("Arial", Font.PLAIN, 14));
        labelMessage.setFont(new Font("Arial", Font.BOLD, 16));
        champLettre.setFont(new Font("Arial", Font.BOLD, 20));
        champLettre.setHorizontalAlignment(JTextField.CENTER);

        JPanel centre = new JPanel(new GridLayout(4, 1));
        centre.add(labelInfo);
        centre.add(labelMot);
        centre.add(labelMessage);
        centre.add(labelScores);

        JButton boutonValider = new JButton("Valider");
        JButton boutonRegles = new JButton("Règles");
        JPanel bas = new JPanel();
        bas.add(new JLabel("Lettre :"));
        bas.add(champLettre);
        bas.add(boutonValider);
        bas.add(boutonRegles);

        add(centre, BorderLayout.CENTER);
        add(bas, BorderLayout.SOUTH);

        // Événements : clic sur Valider, ou touche Entrée dans le champ
        boutonValider.addActionListener(e -> traiterLettre());
        champLettre.addActionListener(e -> traiterLettre());
        boutonRegles.addActionListener(e -> afficherRegles());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(550, 350);
        setLocationRelativeTo(null);   // centre la fenêtre
        setVisible(true);

        demarrerMot();
    }

    private Mot motCourant() {
        return mots.get(indiceMot);
    }

    // Au début de chaque mot : question de l'indice pour les mots de plus de 6 lettres
    private void demarrerMot() {
        Mot mot = motCourant();
        if (mot.getTexte().length() > 6) {
            boolean ok1 = demanderIndice(joueur1);
            boolean ok2 = ok1 && demanderIndice(joueur2);
            if (ok1 && ok2) {
                mot.revelerCategorie();
            }
        }
        rafraichir();
    }

    private boolean demanderIndice(Joueur joueur) {
        int reponse = JOptionPane.showConfirmDialog(this,
                joueur.getNom() + ", révéler la catégorie ?\n(le mot ne vaudra plus que 25 points)",
                "Indice", JOptionPane.YES_NO_OPTION);
        return reponse == JOptionPane.YES_OPTION;
    }

    // Met à jour tout l'affichage
    private void rafraichir() {
        Mot mot = motCourant();
        String info = "Mot " + (indiceMot + 1) + "/" + mots.size()
                + "  |  Au tour de " + joueurEnCours.getNom();
        if (mot.isCategorieRevelee()) {
            info += "  |  Catégorie : " + mot.getCategorie();
        }
        labelInfo.setText(info);
        labelMot.setText(mot.afficherMasque());
        labelScores.setText(joueur1.getNom() + " : " + joueur1.getPoints() + " pts     "
                + joueur2.getNom() + " : " + joueur2.getPoints() + " pts");
        champLettre.requestFocus();
    }

    private void afficherMessage(String texte, Color couleur) {
        labelMessage.setText(texte);
        labelMessage.setForeground(couleur);
    }

    // Appelée à chaque clic sur Valider
    private void traiterLettre() {
        String saisie = champLettre.getText().trim();
        champLettre.setText("");

        if (saisie.length() != 1 || !Character.isLetter(saisie.charAt(0))) {
            afficherMessage("Tape une seule lettre.", Color.ORANGE.darker());
            return;
        }

        Mot mot = motCourant();
        if (mot.proposerLettre(saisie.charAt(0))) {
            if (mot.estComplete()) {
                int points = mot.getPointsBareme();
                joueurEnCours.ajouterPoints(points);
                JOptionPane.showMessageDialog(this, "Bravo " + joueurEnCours.getNom()
                        + " ! Le mot était " + mot.getTexte() + ". +" + points + " points");
                indiceMot++;
                if (indiceMot >= mots.size()) {
                    finPartie();
                    return;
                }
                afficherMessage(" ", Color.BLACK);
                demarrerMot();
                return;
            }
            afficherMessage("Bonne lettre, rejoue !", new Color(0, 140, 0));
        } else {
            afficherMessage("Raté !", Color.RED);
            joueurEnCours = (joueurEnCours == joueur1) ? joueur2 : joueur1;
        }
        rafraichir();
    }

    private void finPartie() {
        String resultat = joueur1.getNom() + " : " + joueur1.getPoints() + " points\n"
                + joueur2.getNom() + " : " + joueur2.getPoints() + " points\n\n";
        if (joueur1.getPoints() > joueur2.getPoints()) {
            resultat += joueur1.getNom() + " gagne !";
        } else if (joueur2.getPoints() > joueur1.getPoints()) {
            resultat += joueur2.getNom() + " gagne !";
        } else {
            resultat += "Match nul !";
        }
        JOptionPane.showMessageDialog(this, resultat, "Fin de la partie",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void afficherRegles() {
        JOptionPane.showMessageDialog(this,
                "- Deux joueurs, 4 mots, une lettre révélée par mot.\n"
              + "- Propose une lettre. Bonne lettre : tu rejoues.\n"
              + "- Mauvaise lettre : la main passe.\n"
              + "- Celui qui complète le mot gagne ses points :\n"
              + "  3 lettres = 15, 4 = 20, 5 = 30, 6 et plus = 50.\n"
              + "- Mot de plus de 6 lettres : si les deux acceptent,\n"
              + "  la catégorie est révélée, le mot vaut 25 points.\n"
              + "- Le meilleur score gagne. Égalité : match nul.",
                "Règles", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        Dictionnaire dico = new Dictionnaire("mots.txt");
        List<Mot> mots = dico.tirerMots(4);
        if (mots.size() < 4) {
            JOptionPane.showMessageDialog(null, "Pas assez de mots dans mots.txt");
            return;
        }
        String nom1 = JOptionPane.showInputDialog("Nom du joueur 1 :");
        String nom2 = JOptionPane.showInputDialog("Nom du joueur 2 :");
        if (nom1 == null || nom2 == null) {
            return;   // un joueur a cliqué sur Annuler
        }
        SwingUtilities.invokeLater(() -> new FenetreJeu(new Joueur(nom1), new Joueur(nom2), mots));
    }
}