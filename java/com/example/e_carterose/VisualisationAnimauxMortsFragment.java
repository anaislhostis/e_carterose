package com.example.e_carterose;

import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.List;


public class VisualisationAnimauxMortsFragment extends Fragment {

    private DatabaseAccess db;
    private List<Animal> DeadNotifiedAnimals; // Variable pour stocker la liste complète des animaux notifié morts
    private String numTra;
    private LinearLayout layout;
    private int selectedIndex = -1;


    public static VisualisationAnimauxMortsFragment newInstance() {
        VisualisationAnimauxMortsFragment fragment = new VisualisationAnimauxMortsFragment();

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_visualisation_animaux_morts, container, false); // Initialiser la vue
        layout = rootView.findViewById(R.id.dead_animals_container); // Récupérer le layout pour afficher les animaux

        db = new DatabaseAccess(requireContext()); // Initialiser la base de données

        // Récupérer le numéro de l'élevage depuis la variable statique
        String numElevage = MainActivity.numeroElevage;

        // Récupérer tous les animaux de l'élevage notifié morts (actif=-1)
        DeadNotifiedAnimals = db.getAnimalsByElevageAndActif(numElevage, -1);
        Log.e("DeadNotifiedAnimals : ", DeadNotifiedAnimals.toString());

        // Mise à jour de l'affichage des animaux notifés morts
        updateAnimalViews(DeadNotifiedAnimals);


        return rootView;
    }



    public void updateAnimalViews(List<Animal> DeadNotifiedAnimals){
        layout.removeAllViews(); // Supprimer tous les éléments actuellement affichés

        String numElevage = MainActivity.numeroElevage; //Récupérer le numéro d'élevage

        for (int i = 0; i < DeadNotifiedAnimals.size(); i++) {
            Animal animal = DeadNotifiedAnimals.get(i);
            // Créer et ajouter les vues pour chaque animal
            View animalView = LayoutInflater.from(requireContext()).inflate(R.layout.animal_item, layout, false);
            TextView textViewNumTra = animalView.findViewById(R.id.text_view_num_tra);
            TextView textViewNom = animalView.findViewById(R.id.text_view_nom);
            TextView textViewDateNaiss = animalView.findViewById(R.id.text_view_date_naiss);
            TextView textViewSexe = animalView.findViewById(R.id.text_view_sexe);

            textViewNumTra.setText("Numéro de travail: " + animal.getNumTra());

            String nom = animal.getNom();
            if (nom != null) {
                textViewNom.setText("Nom: " + animal.getNom());
            } else {
                Spanned spannedText = HtmlCompat.fromHtml("Nom: " + "<i>" + "non attribué" + "</i>", HtmlCompat.FROM_HTML_MODE_LEGACY);
                textViewNom.setText(spannedText);
            }

            String dateNaissance = animal.getDateNaiss();
            if (dateNaissance != null && dateNaissance.length() >= 10) {
                textViewDateNaiss.setText("Date de naissance: " + dateNaissance.substring(0, 10));
            }

            String sexe = animal.getSexe();
            if (sexe.equals("1")) {
                textViewSexe.setText("Sexe: Mâle");
            } else if (sexe.equals("2")) {
                textViewSexe.setText("Sexe: Femelle");
            }

            // Ajouter des logs pour visualiser les valeurs
            Log.d("VisualisationElevageFragment", "Numéro de travail: " + animal.getNumTra());
            Log.d("VisualisationElevageFragment", "Nom: " + animal.getNom());
            Log.d("VisualisationElevageFragment", "Date de naissance: " + dateNaissance);
            Log.d("VisualisationElevageFragment", "Sexe: " + animal.getSexe());
            Log.d("VisualisationElevageFragment", "Actif: " + animal.getActif());


            final int index = i;
            animalView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animal selectedAnimal = DeadNotifiedAnimals.get(index);

                    // Passer les données de l'animal au fragment de détails
                    DetailsAnimalMortFragment detailsAnimalMortFragment = new DetailsAnimalMortFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("animal", (Serializable) selectedAnimal);
                    detailsAnimalMortFragment.setArguments(bundle);

                    // Remplacer le contenu actuel par le fragment de détails
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, detailsAnimalMortFragment)
                            .addToBackStack(null) // Pour ajouter le fragment à la pile de retour
                            .commit();
                }
            });

            // Ajouter la vue de l'animal à ton layout principal
            layout.addView(animalView);

        }
    }

}