package com.example.e_carterose;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String numeroElevage;
    public static String asda;
    private List<String> numTraAnimauxMorts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Charger le fragment IndexFragment dans le conteneur
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new IndexFragment())
                .commit();

        // Initialiser la liste des numéros de travail des animaux morts
        numTraAnimauxMorts = new ArrayList<>();
    }


    // Méthode pour ajouter un numéro de travail à la liste des animaux morts
    public void ajouterAnimalMort(String numeroTravail) {
        numTraAnimauxMorts.add(numeroTravail);
    }

    
    // Méthode pour supprimer un numéro de travail de la liste des animaux morts
    public void supprimerAnimalMort(String numeroTravail) {
        numTraAnimauxMorts.remove(numeroTravail);
    }
    

    // Méthode pour récupérer la liste des numéros de travail des animaux morts
    public List<String> getNumTraAnimauxMorts() {
        return numTraAnimauxMorts;
    }


}
