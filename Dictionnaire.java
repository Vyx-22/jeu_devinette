import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dictionnaire {
    private List<String> lignes = new ArrayList<>();

    public Dictionnaire(String cheminFichier) {
        try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                if (!ligne.isEmpty()) {
                    lignes.add(ligne);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }

    public List<Mot> tirerMots(int nombre) {
        List<Mot> motsTires = new ArrayList<>();
        List<String> copieList = new ArrayList<>(this.lignes);
        Collections.shuffle(copieList);
        for (int i = 0; i < nombre; i++) {
            String ligne = copieList.get(i);
            String[] parties = ligne.split(";");
            motsTires.add(new Mot(parties[0], parties[1]));

        }

        return motsTires;
    }
}