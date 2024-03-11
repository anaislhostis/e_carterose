package com.example.e_carterose;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class FormulaireMortFragment extends Fragment {

    private DatabaseAccess db;
    private EditText editTextNumNat;
    private Button buttonSubmit;
    private Button buttonEffacer;

    public static FormulaireMortFragment newInstance() {
        FormulaireMortFragment fragment = new FormulaireMortFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_formulaire_mort, container, false);

        // Acces à la BDD
        db = new DatabaseAccess(requireContext());

        // Récupérer les références des editText/Boutton du formulaire
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonEffacer = view.findViewById(R.id.buttonEffacer);
        editTextNumNat = view.findViewById(R.id.editTextNumNat);

        // Bouton soumettre
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Afficher le popup de confirmation de suppression
                popupConfirmationSuppression();
            }
        });


        // Bouton effacer
        buttonEffacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réinitialiser le texte de l'EditText
                editTextNumNat.setText("");
            }
        });

        return view;
    }


    // Méthode pour supprimer les données de l'animal mort dans la base de données
    private void suppressionanimal(){
        // Récupération de la valeur numNat de l'EditText du formulaire
        String numNat = editTextNumNat.getText().toString();

        //Suppression de l'animal de la base de données
        db.deleteAnimal(numNat);

    }

    // Popup pour la confirmation de suppression de l'animal
    private void popupConfirmationSuppression() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmer la déclaration de mort");
        builder.setMessage("Êtes-vous sûr de vouloir déclarer cet animal comme mort ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                suppressionanimal();
            }
        });
        builder.setNegativeButton("Non", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
