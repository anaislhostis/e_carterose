package com.example.e_carterose;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.List;


public class SelectionTransportElevageFragment extends Fragment {

    private DatabaseAccess db;
    private List<Animal> allAnimals; // Variable pour stocker la liste complète des animaux
    private EditText editTextNumTra;
    private EditText editTextNumElevage;
    private RadioGroup radioGroupTransfert;
    private Button buttonSubmit;
    private Button buttonEffacer;

    public static SelectionTransportElevageFragment newInstance() {
        SelectionTransportElevageFragment fragment = new SelectionTransportElevageFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_selection_transport_elevage, container, false);

        // Acces à la BDD
        db = new DatabaseAccess(requireContext());

        // Récupérer le numéro de l'élevage depuis la variable statique
        String numElevage1 = MainActivity.numeroElevage;

        // Récupérer tous les animaux de l'élevage de départ
        allAnimals = db.getAnimalsByElevage(numElevage1);

        // Références des vues
        editTextNumTra = rootView.findViewById(R.id.editTextNumTra);
        editTextNumElevage = rootView.findViewById(R.id.editTextNumElevage);
        radioGroupTransfert = rootView.findViewById(R.id.radioGroupTransfert);
        buttonSubmit = rootView.findViewById(R.id.buttonSubmit);
        buttonEffacer = rootView.findViewById(R.id.buttonEffacer);


        // Bouton Soumettre
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération des valeurs entrées par l'utilisateur
                String numTra = editTextNumTra.getText().toString();
                String numElevage2 = editTextNumElevage.getText().toString();


                // Vérification si l'élevage d'arrivée n'existe pas
                if (!db.isElevageExists(numElevage2)) {
                    Toast.makeText(getContext(), "L'élevage d'arrivée n'existe pas.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                // Vérifier si l'animal avec le numéro de travail donné existe dans l'élevage de départ
                if (!db.isNumTraExists(numTra, allAnimals)) {
                    Toast.makeText(getContext(), "L'animal " + numTra + " n'existe pas dans votre élevage.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                // Vérifier si le transfert est possible avec les codes ASDA des 2 élevages
                if (!isCodAsdaOK(numElevage1, numElevage2)){
                    Toast.makeText(getContext(), "Transfert impossible : passeports sanitaires incompatibles.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                // Vérifier si l'utilisateur a entré son propre numéro d'élevage
                if (numElevage1.equals(MainActivity.numeroElevage)) {
                    Toast.makeText(requireContext(), "Vous ne pouvez pas transférer vers votre propre élevage", Toast.LENGTH_SHORT).show();
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
                builder.setMessage("Êtes-vous sûr de vouloir réaliser le transfert de l'animal " + numTra + " vers l'élevage " + numElevage2 + " ?");
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Modifier le statut de l'animal toujours présent dans l'élevage 1 (2 = transfert entre élevages)
                        db.updateStatus(numElevage1, numTra, 2);

                        //Récupérer les informations de l'animal
                        Animal animal = db.getAnimalByNumTra(numElevage1, numTra);
                        String nom = animal.getNom();
                        String dateNais = animal.getDateNaiss();
                        String sexe = animal.getSexe();
                        String numNat = "000000" + numTra; // le numéro national d'un animal doit être unique : on modifie les 6 premiers chiffres
                        String codePays = animal.getCodPays();
                        String codePaysNais = animal.getCodPaysNaiss();
                        String codePaysPere = animal.getCodPaysPere();
                        String codePaysMere = animal.getCodPaysMere();
                        String numExpNais = animal.getNumExpNaiss();
                        String numNatPere = animal.getNumNatPere();
                        String numNatMere = animal.getNumNatMere();
                        String codeRace = animal.getRace();
                        String codeRacePere = animal.getCodRacePere();
                        String codeRaceMere = animal.getCodRaceMere();
                        String actif = animal.getActif();

                        // Transfert de l'animal vers l'élevage 2 : modification du numéro d'élevage de l'animal
                        db.updateNumElevage(numElevage1, numElevage2, numTra);

                        //Création de l'animal dans l'élevage 1 avec le numNat mofifié
                        db.insertAnimal(numNat, numTra, codePays, nom, sexe, dateNais, codePaysNais, numExpNais,  codePaysPere, numNatPere, codeRacePere, codePaysMere, numNatMere, codeRaceMere, numElevage1, codeRace, actif);

                        // Procéder au transfert
                        Toast.makeText(requireContext(), "Transfert en cours...", Toast.LENGTH_SHORT).show();


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
                editTextNumElevage.setText("");
                radioGroupTransfert.clearCheck();

            }
        });

        return rootView;
    }

    public boolean isCodAsdaOK(String numElevage1, String numElevage2) {
        // Récupérer les codes ASDA des deux élevages
        String codAsda1 = db.getCodAsdaByNumElevage(numElevage1);
        String codAsda2 = db.getCodAsdaByNumElevage(numElevage2);

        // Comparer les codes ASDA
        if (codAsda1 != null && codAsda2 != null) {
            // Convertir les codes ASDA en entiers pour la comparaison
            int asda1 = Integer.parseInt(codAsda1);
            int asda2 = Integer.parseInt(codAsda2);

            if (asda1 == 1) {
                // Code ASDA vert : tous les transfert lui sont possible
                return true;
            }
            if (asda1 == 2) {
                // Code ASDA jaune : tous les transfert lui sont possible faut pour un élevage vert
                if (asda2 == 1) {
                    return false;
                } else {
                    return true;
                }
            }
            if (asda1 == 3) {
                // Code ASDA orange : les transferts vers les élevages orange ou rouge lui sont possible
                //                    les transferts vers les élevages vert ou jaune lui sont interdit
                if (asda2 == 1 || asda2 == 2 ) {
                    return false;
                } else {
                    return true;
                }
            }
            if (asda1 == 4) {
                // Code ASDA orange : seuls les transferts vers les élevages rouges lui sont possible
                if (asda2 == 4) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            // Gérer le cas où l'un des codes ASDA est manquant
            return false;
        }
        return false;
    }
}
