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

import com.example.e_carterose.databinding.FragmentAnimauxEnTransfertBinding;

import java.util.List;


public class AnimauxEnTransfertFragment extends Fragment {

    private DatabaseAccess db;
    private List<Animal> ElevageTransfer; // Variable pour stocker la liste des animaux en transfert entre élevage
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


        // Supprimer tous les éléments actuellement affichés
        layout_arrivals.removeAllViews();
        layout_departures.removeAllViews();
        layout_slaughterhouse.removeAllViews();


        ElevageTransfer = db.getAnimalsTransferELevageByElevage(numElevage);
        SlaughterhouseTransfer = db.getAnimalsTransferslaughterhouseByElevage(numElevage);
        Log.e("Animaux transferés entre élevages : ", ElevageTransfer.toString());
        Log.e("Animaux transferés abattoir : ", SlaughterhouseTransfer.toString());

        // Parcourir la liste des animaux de l'élevage
        for (Animal animal : ElevageTransfer) {

            String numNat = animal.getNumNat();
            Log.e("numNat", numNat); // valeur null
            if (numNat != null && numNat.startsWith("000000")) {

                // Ajouter l'animal au layout des départs
                addAnimalToLayout(animal, layout_departures);
            } else {
                // Ajouter l'animal au layout des arrivées
                addAnimalToLayout(animal, layout_arrivals);
            }
        }

        for (Animal animal : SlaughterhouseTransfer) {
            // Ajouter l'animal au layout de l'abattoir
            addAnimalToLayout(animal, layout_slaughterhouse);
        }

        return rootView;
    }

    // Méthode pour ajouter un animal à un layout donné
    private void addAnimalToLayout(Animal animal, LinearLayout layout) {
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

        if (animal.getSexe().equals("1")) {
            textViewSexe.setText("Sexe: Mâle");
        } else if (animal.getSexe().equals("2")) {
            textViewSexe.setText("Sexe: Femelle");
        }

        // Ajouter la vue de l'animal au layout spécifié
        layout.addView(animalView);
    }
}
