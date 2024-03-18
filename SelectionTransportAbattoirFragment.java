package com.example.e_carterose;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;


public class SelectionTransportAbattoirFragment extends Fragment {

    private DatabaseAccess db;
    private List<Animal> allAnimals; // Variable pour stocker la liste complète des animaux

    private EditText editTextNumTra;
    private RadioGroup radioGroupTransfert;
    private Button buttonSubmit;
    private Button buttonEffacer;

    public static SelectionTransportAbattoirFragment newInstance() {
        SelectionTransportAbattoirFragment fragment = new SelectionTransportAbattoirFragment();
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_selection_transport_abattoir, container, false);

        // Acces à la BDD
        db = new DatabaseAccess(requireContext());

        // Récupérer le numéro de l'élevage depuis la variable statique
        String numElevage = MainActivity.numeroElevage;

        // Récupérer tous les animaux de l'élevage de départ
        allAnimals = db.getAnimalsByElevage(numElevage);

        // Références des vues
        editTextNumTra = rootView.findViewById(R.id.editTextNumTra);
        radioGroupTransfert = rootView.findViewById(R.id.radioGroupTransfert);
        buttonSubmit = rootView.findViewById(R.id.buttonSubmit);
        buttonEffacer = rootView.findViewById(R.id.buttonEffacer);


        // Bouton Soumettre
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération du numéro de travail entré par l'utilisateur
                String numTra = editTextNumTra.getText().toString();


                // Vérifier si l'animal avec le numéro de travail donné existe dans l'élevage
                if (!db.isNumTraExists(numTra, allAnimals)) {
                    Toast.makeText(getContext(), "L'animal " + numTra + " n'existe pas dans votre élevage.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }

                
                // Vérifier si un RadioButton est sélectionné dans le RadioGroup
                if (radioGroupTransfert.getCheckedRadioButtonId() == -1) {
                    // Aucun RadioButton n'est sélectionné
                    Toast.makeText(requireContext(), "Veuillez sélectionner un mode de transfert.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }

                // Si tous les champs entrés sont corrects, afficher une boîte de dialogue de confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("Êtes-vous sûr de vouloir réaliser le transfert de l'animal " + numTra + " vers l'abattoir ?");
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Modifier le statut de l'animal (3 = transfert vers l'abattoir)
                        db.updateStatus(numElevage, numTra, 3);
                    }
                });
                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ne rien faire
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            });

            // Gestion du clic sur le bouton Effacer
            buttonEffacer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Effacer les champs de texte et désélectionner les cases à cocher
                    editTextNumTra.setText("");
                    radioGroupTransfert.clearCheck();

                }
            });

        return rootView;
        }

}
