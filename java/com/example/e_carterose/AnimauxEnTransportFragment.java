package com.example.e_carterose;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.e_carterose.databinding.FragmentAnimauxEnTransportBinding;

import java.util.List;

public class AnimauxEnTransportFragment extends Fragment {

    private LinearLayout transportAnimalsContainer;
    private List<Animal> animaux_en_transport = MainActivity.animaux_en_transport;
    private FragmentAnimauxEnTransportBinding binding;

    public static AnimauxEnTransportFragment newInstance() {
        AnimauxEnTransportFragment fragment = new AnimauxEnTransportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_animaux_en_transport, container, false);
        binding = FragmentAnimauxEnTransportBinding.inflate(inflater, container, false);

        // Initialize transport animals container
        transportAnimalsContainer = rootView.findViewById(R.id.transport_animals_container);

        for (Animal animal : animaux_en_transport) {
            View animalItemView = inflater.inflate(R.layout.animal_transport_item, null);

            // Récupérez les TextView dans votre layout animal_transport_item.xml
            TextView textViewNumNat = animalItemView.findViewById(R.id.text_view_num_nat);
            TextView textViewNom = animalItemView.findViewById(R.id.text_view_nom);
            TextView textViewDateNaiss = animalItemView.findViewById(R.id.text_view_date_naiss);
            TextView textViewSexe = animalItemView.findViewById(R.id.text_view_sexe);

            // Set the values for TextViews
            textViewNumNat.setText("Numéro national: " + animal.getNumNat());
            textViewNom.setText("Nom: " + animal.getNom());
            textViewDateNaiss.setText("Date de naissance: " + animal.getDateNaiss());
            textViewSexe.setText("Sexe: " + (animal.getSexe().equals("1") ? "Mâle" : "Femelle"));

            // Ajoutez la vue de l'élément animal à votre conteneur de transport
            transportAnimalsContainer.addView(animalItemView);
        }

        Button buttonRetourAccueil = rootView.findViewById(R.id.ButtonRetourAccueil);
        buttonRetourAccueil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Naviguer vers ConnexionTransporteurFragment
                ConnexionTransporteurFragment connexionTransporteurFragment = ConnexionTransporteurFragment.newInstance();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, connexionTransporteurFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pour chaque vue dans le conteneur d'animaux transportés
        for (int i = 0; i < transportAnimalsContainer.getChildCount(); i++) {
            View animalItemView = transportAnimalsContainer.getChildAt(i);
            final Animal animal = animaux_en_transport.get(i);

            // Ajouter un onClickListener à chaque élément de la liste
            animalItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Afficher un dialogue de confirmation
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Confirmation");
                    builder.setMessage("L'animal est-il bien arrivé à destination ?");
                    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Si l'utilisateur confirme, supprimez l'animal de la liste
                            animaux_en_transport.remove(animal);
                            // Actualiser l'affichage
                            transportAnimalsContainer.removeView(animalItemView);
                            // Afficher un message de succès
                            Toast.makeText(getContext(), "L'animal a été retiré de la liste de transport.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Si l'utilisateur annule, ne rien faire
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }


    }
}