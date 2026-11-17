public class Joueur {
    private String nom;
    private int points;

    public Joueur(String nom) {
        this.nom = nom;
        this.points = 0;

    }

    public String getNom() {
        return this.nom;
    }

    public int getPoints() {
        return this.points;
    }

    public void ajouterPoints(int point) {
        if (point > 0) {
            this.points += point;
        }

    }

}
