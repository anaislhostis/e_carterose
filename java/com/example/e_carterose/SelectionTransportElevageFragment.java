package com.example.e_carterose;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;


public class SelectionTransportElevageFragment extends Fragment {

    private DatabaseAccess db;
    private List<Animal> acctiveAnimals; // Variable pour stocker la liste complète des animaux actifs
    private List<Animal> ElevageTransfert; // Variable pour stocker la liste des animaux en transfert entre élevages
    private List<Animal> SlaughterhouseTransfer; // Variable pour stocker la liste des animaux en transfert vers l'abattoir
    private List<Animal> DeadNotifiedAnimals; // Variable pour stocker la liste des animaux notifiés morts
    private List<Animal> DeadAnimals; // Variable pour stocker la liste des animaux morts
    private EditText editTextNumTra;
    private EditText editTextNumElevage;
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

        // Récupérer tous les animaux de l'élevage de départ (actif=1)
        acctiveAnimals = db.getAnimalsByElevageAndActif(numElevage1, 1);

        // Références des vues
        editTextNumTra = rootView.findViewById(R.id.editTextNumTra);
        editTextNumElevage = rootView.findViewById(R.id.editTextNumElevage);
        buttonSubmit = rootView.findViewById(R.id.buttonSubmit);
        buttonEffacer = rootView.findViewById(R.id.buttonEffacer);


        // Bouton Soumettre
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération des valeurs entrées par l'utilisateur
                String numTra = editTextNumTra.getText().toString();
                String numElevage2 = editTextNumElevage.getText().toString();

                // Vérifier si le champs numTra n'est pas vide
                if (numTra.isEmpty()) {
                    Toast.makeText(getContext(), "Veuillez entrer le numéro de travail de l'animal", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                // Vérifier que le nombre de chiffres entré dans les champs du formulaire correspond à la longueur maximale spécifiée dans le layout
                if (numTra.length() != 4) {
                    // Afficher un message d'erreur
                    Toast.makeText(requireContext(), "Le numéro de travail doit contenir exactement " + 4 + " chiffres.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                // Vérifier si le champs numElevage2 n'est pas vide
                if (numElevage2.isEmpty()) {
                    Toast.makeText(getContext(), "Veuillez entrer le numéro de l'autre élevage", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                // Vérifier que le nombre de chiffres entré dans les champs du formulaire correspond à la longueur maximale spécifiée dans le layout
                if (numElevage2.length() != 8) {
                    // Afficher un message d'erreur
                    Toast.makeText(requireContext(), "Le numéro d'élevage doit contenir exactement " + 8 + " chiffres.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }

                // Vérifier si l'animal est déjà déclaré en cours de transfert vers un autre élevage (actif=2)
                ElevageTransfert = db.getAnimalsByElevageAndActif(numElevage1, 2);
                for (Animal animal : ElevageTransfert) {
                    if (animal.getNumTra().equals(numTra)) {
                        Toast.makeText(getContext(), "L'animal " + numTra + " est déjà en cours de transfert vers un autre élevage.", Toast.LENGTH_SHORT).show();
                        return; // Arrêter l'exécution de la méthode
                    }
                }
                // Vérifier si l'animal est déjà déclaré en cours de transfert vers l'abattoir (actif=3)
                SlaughterhouseTransfer = db.getAnimalsByElevageAndActif(numElevage1, 3);
                for (Animal animal : SlaughterhouseTransfer) {
                    if (animal.getNumTra().equals(numTra)) {
                        Toast.makeText(getContext(), "L'animal " + numTra + " est déjà en cours de transfert vers l'abattoir.", Toast.LENGTH_SHORT).show();
                        return; // Arrêter l'exécution de la méthode
                    }
                }
                // Vérifier si l'animal est déjà notifié mort (actif=-1)
                DeadNotifiedAnimals = db.getAnimalsByElevageAndActif(numElevage1, -1);
                for (Animal animal : DeadNotifiedAnimals) {
                    if (animal.getNumTra().equals(numTra)) {
                        Toast.makeText(getContext(), "L'animal " + numTra + " est déjà notifié mort.", Toast.LENGTH_SHORT).show();
                        return; // Arrêter l'exécution de la méthode
                    }
                }
                // Vérifier si l'animal est déjà mort (actif=0)
                DeadAnimals = db.getAnimalsByElevageAndActif(numElevage1, 0);
                for (Animal animal : DeadAnimals) {
                    if (animal.getNumTra().equals(numTra) && !animal.getNumNat().startsWith("000000")) {
                        Toast.makeText(getContext(), "L'animal " + numTra + " est déjà mort.", Toast.LENGTH_SHORT).show();
                        return; // Arrêter l'exécution de la méthode
                    }
                }

                // Vérification si l'élevage d'arrivée n'existe pas
                if (!db.isElevageExists(numElevage2)) {
                    Toast.makeText(getContext(), "L'élevage d'arrivée n'existe pas.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                // Vérifier si l'animal avec le numéro de travail donné existe dans l'élevage de départ
                if (!db.isNumTraExists(numTra, acctiveAnimals)) {
                    Toast.makeText(getContext(), "L'animal " + numTra + " n'existe pas dans votre élevage.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                // Vérifier si le transfert est possible avec les codes ASDA des 2 élevages
                if (!isCodAsdaOK(numElevage1, numElevage2)){
                    Toast.makeText(getContext(), "Transfert impossible : passeports sanitaires incompatibles.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                // Vérifier si l'utilisateur a entré son propre numéro d'élevage
                if (numElevage2.equals(MainActivity.numeroElevage)) {
                    Toast.makeText(requireContext(), "Vous ne pouvez pas transférer vers votre propre élevage", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }

                // Si tous les champs entrés sont corrects, afficher une boîte de dialogue de confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Confirmation");
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
                        String codeRace = animal.getCodRace();
                        String codeRacePere = animal.getCodRacePere();
                        String codeRaceMere = animal.getCodRaceMere();
                        String actif = animal.getActif();

                        // Transfert de l'animal vers l'élevage 2 : modification du numéro d'élevage de l'animal
                        db.updateNumElevage(numElevage1, numElevage2, numTra);

                        Log.d("SelectionTransportElevageFragment", "num_elevage1: " + numElevage1);

                        //Création de l'animal dans l'élevage 1 avec le numNat mofifié
                        db.insertAnimal(numNat, numTra, codePays, nom, sexe, dateNais, codePaysNais, numExpNais,  codePaysPere, numNatPere, codeRacePere, codePaysMere, numNatMere, codeRaceMere, numElevage1, codeRace, actif);

                        // Procéder au transfert
                        Toast.makeText(requireContext(), "L'animal " + numTra + " est en attente de transfert vers l'élevage " + numElevage2 +".", Toast.LENGTH_SHORT).show();


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
                // Code ASDA rouge : seuls les transferts vers l'abattoir lui sont possible
                return false;
            }
        } else {
            // Gérer le cas où l'un des codes ASDA est manquant
            return false;
        }
        return false;
    }
}