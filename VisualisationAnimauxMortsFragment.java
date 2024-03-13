package com.example.e_carterose;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VisualisationAnimauxMortsFragment extends Fragment {

    private DatabaseAccess db;
    private String numTra;
    private LinearLayout layout;


    public static VisualisationAnimauxMortsFragment newInstance() {
        VisualisationAnimauxMortsFragment fragment = new VisualisationAnimauxMortsFragment();

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_visualisation_animaux_morts, container, false); // Initialiser la vue
        layout = rootView.findViewById(R.id.dead_animals_container); // Récupérer le layout pour afficher les animaux

        db = new DatabaseAccess(requireContext()); // Initialiser la base de données

        // Obtenir une référence au MainActivity
        MainActivity mainActivity = (MainActivity) requireActivity();

        // Récupérer la liste des numéros de travail des animaux morts
        List<String> numTraAnimauxMorts = mainActivity.getNumTraAnimauxMorts();

        // Mise à jour de l'affichage des animaux notifés morts
        updateAnimalViews(numTraAnimauxMorts);


        return rootView;
    }


    public void updateAnimalViews(List<String> numTraAnimauxMorts){
        layout.removeAllViews(); // Supprimer tous les éléments actuellement affichés

        // Récupérer les informations de chaque animal en fonction de son numéro de travail
        for (String numTra : numTraAnimauxMorts) {
            Animal animal = db.getAnimalByNumTra(numTra);
            if (animal != null) {
                // Récupération des information de l'animal
                String nom = animal.getNom();
                String dateNaiss = animal.getDateNaiss();
                String numNat= animal.getNumNat();
                String codPays = animal.getCodPays();
                String sexe = animal.getSexe();
                String codPaysNaiss = animal.getCodPaysNaiss();
                String numExpNaiss = animal.getNumExpNaiss();
                String codPaysPere = animal.getCodPaysPere();
                String numNatPere = animal.getNumNatPere();
                String codRacePere = animal.getCodRacePere();
                String codPaysMere = animal.getCodPaysMere();
                String numNatMere = animal.getNumNatMere();
                String codRaceMere = animal.getCodRaceMere();
                String numElevage = animal.getNumElevage();
                String race = animal.getRace();


                // Créer et ajouter les vues pour chaque animal
                View animalView = LayoutInflater.from(requireContext()).inflate(R.layout.animal_item, layout, false);
                TextView textViewNumTra = animalView.findViewById(R.id.text_view_num_tra);
                TextView textViewNom = animalView.findViewById(R.id.text_view_nom);
                TextView textViewDateNaiss = animalView.findViewById(R.id.text_view_date_naiss);
                TextView textViewSexe = animalView.findViewById(R.id.text_view_sexe);

                textViewNumTra.setText("Numéro de travail:" + numTra);

                // Si l'animal n'a pas de nom
                if (nom != null) {
                    textViewNom.setText("Nom: " + nom);
                } else {
                    Spanned spannedText = HtmlCompat.fromHtml("Nom: " + "<i>" + "non attribué" + "</i>", HtmlCompat.FROM_HTML_MODE_LEGACY);
                    textViewNom.setText(spannedText);
                }

                // Garder que la date de naissance et pas l'heure
                if (dateNaiss != null && dateNaiss.length() >= 10) {
                    textViewDateNaiss.setText("Date de naissance: " + dateNaiss.substring(0, 10));
                }

                // Remplacer le sexe 1 par mâle et 2 par femelle
                if (sexe.equals("1")) {
                    textViewSexe.setText("Sexe: Mâle");
                } else if (sexe.equals("2")) {
                    textViewSexe.setText("Sexe: Femelle");
                }

                // Ajouter la vue de l'animal à ton layout principal
                layout.addView(animalView);


                // Ajouter des logs pour visualiser les valeurs
                Log.d("VisualisationElevageFragment", "Numéro de travail: " + numTra);
                Log.d("VisualisationElevageFragment", "Nom: " + nom);
                Log.d("VisualisationElevageFragment", "Date de naissance: " + dateNaiss);
                Log.d("VisualisationElevageFragment", "Sexe: " + sexe);
            }
        }
    }
}
