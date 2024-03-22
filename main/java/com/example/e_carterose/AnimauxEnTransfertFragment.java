package com.example.e_carterose;

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


import com.example.e_carterose.databinding.FragmentAnimauxEnTransfertBinding;

import java.io.Serializable;
import java.util.List;


public class AnimauxEnTransfertFragment extends Fragment {

    private DatabaseAccess db;
    private List<Animal> ElevageTransfer; // Variable pour stocker la liste des animaux en transfert entre élevages
    private List<Animal> arrivalsAnimals; //Animaux qui arrivent dans l'élevage
    private List<Animal> departuresAnimals; //Animaux qui partent de l'élevage
    private List<Animal> SlaughterhouseTransfer; // Variable pour stocker la liste des animaux en transfert vers l'abattoir
    private LinearLayout layout_arrivals;
    private LinearLayout layout_departures;
    private LinearLayout layout_slaughterhouse;
    private FragmentAnimauxEnTransfertBinding binding;

    public static AnimauxEnTransfertFragment newInstance() {
        AnimauxEnTransfertFragment fragment = new AnimauxEnTransfertFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_animaux_en_transfert, container, false); // Initialiser la vue
        layout_arrivals = rootView.findViewById(R.id.arrivals_animals_container); // Récupérer le layout pour afficher les animaux qui arrivent dans un autre élevage
        layout_departures = rootView.findViewById(R.id.departures_animals_container); // Récupérer le layout pour afficher les animaux qui partent dans un autre élevage
        layout_slaughterhouse = rootView.findViewById(R.id.slaughterhouse_animals_container); // Récupérer le layout pour afficher les animaux qui vont à l'abattoir
        binding = FragmentAnimauxEnTransfertBinding.inflate(inflater, container, false); // Initialiser la liaison de données

        db = new DatabaseAccess(requireContext()); // Initialiser la base de données

        // Récupérer le numéro de l'élevage depuis la variable statique
        String numElevage = MainActivity.numeroElevage;
        Log.e("numElevage", numElevage);


        ElevageTransfer = db.getAnimalsByElevageAndActif(numElevage, 2);
        SlaughterhouseTransfer = db.getAnimalsByElevageAndActif(numElevage, 3);


        // Initialiser les listes d'animaux
        arrivalsAnimals = new ArrayList<>();
        departuresAnimals = new ArrayList<>();

        // Parcourir la liste des animaux de l'élevage pour séparer les animaux qui arrivent et qui partent de l'élevage
        for (Animal animal : ElevageTransfer) {
            String numNat = animal.getNumNat();
            Log.e("numNat", numNat); // valeur null
            if (numNat != null && numNat.startsWith("000000")) {

                // Ajouter l'animal à la liste des départs
                departuresAnimals.add(animal);
            } else {
                // Ajouter l'animal à la liste des arrivées
                arrivalsAnimals.add(animal);
            }
        }

        // Mise à jour de l'affichage des animaux en transfert
        addAnimalToLayout(arrivalsAnimals,layout_arrivals, "arrivals");
        addAnimalToLayout(departuresAnimals, layout_departures, "departures");
        addAnimalToLayout(SlaughterhouseTransfer, layout_slaughterhouse, "slaughterhouse");

        // Vérifier si les layouts sont vides et afficher "pas d'animal en transfert" si nécessaire
        checkEmptyLayout(arrivalsAnimals, layout_arrivals);
        checkEmptyLayout(departuresAnimals, layout_departures);
        checkEmptyLayout(SlaughterhouseTransfer, layout_slaughterhouse);


        return rootView;
    }

    // Méthode pour ajouter un animal à un layout donné
    private void addAnimalToLayout(List<Animal> animals, LinearLayout layout, final String transferType) {
        layout.removeAllViews(); // Supprimer tous les éléments actuellement affichés

        String numElevage = MainActivity.numeroElevage; //Récupérer le numéro d'élevage

        for (int i = 0; i < animals.size(); i++) {
            Animal animal = animals.get(i);
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
            // Ajouter un OnClickListener à la vue de l'animal
            animalView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Créer le fragment détail en fonction du type de transfert
                    Fragment detailFragment;
                    switch (transferType) {
                        case "arrivals":
                            detailFragment = new DetailsAnimalEnTransfertElevageArriveeFragment();
                            break;
                        case "departures":
                            detailFragment = new DetailsAnimalEnTransfertElevageDepartFragment();
                            break;
                        case "slaughterhouse":
                            detailFragment = new DetailsAnimalEnTransfertAbattoirFragment();
                            break;
                        default:
                            // Par défaut, ouvrir le fragment de détail par arrivée
                            detailFragment = new DetailsAnimalEnTransfertElevageArriveeFragment();
                    }

                    // Passer les données de l'animal au fragment de détails
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("animal", animal);
                    detailFragment.setArguments(bundle);

                    // Remplacer le contenu actuel par le fragment de détails
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, detailFragment)
                            .addToBackStack(null) // Pour ajouter le fragment à la pile de retour
                            .commit();
                }
            });

            // Ajouter la vue de l'animal au layout spécifié
            layout.addView(animalView);
        }
    }
    // Méthode pour vérifier si la liste d'animaux est vide et afficher le texte approprié
    private void checkEmptyLayout(List<Animal> animals, LinearLayout layout) {
        // Si la liste d'animaux est vide
        if (animals.isEmpty()) {
            // Créer un TextView pour afficher "Pas d'animal en transfert" en italique et en gris
            TextView textView = new TextView(requireContext());
            textView.setText(HtmlCompat.fromHtml("<i><font color='#808080'>Pas d'animal en transfert</font></i>", HtmlCompat.FROM_HTML_MODE_LEGACY));
            // Ajouter le TextView au layout
            layout.addView(textView);
        }
    }
}