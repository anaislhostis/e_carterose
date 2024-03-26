package com.example.e_carterose;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;


public class SelectionTransportAbattoirFragment extends Fragment {

    private DatabaseAccess db;
    private List<Animal> acctiveAnimals; // Variable pour stocker la liste complète des animaux actifs
    private List<Animal> ElevageTransfert; // Variable pour stocker la liste des animaux en transfert entre élevages
    private List<Animal> SlaughterhouseTransfer; // Variable pour stocker la liste des animaux en transfert vers l'abattoir
    private List<Animal> DeadNotifiedAnimals; // Variable pour stocker la liste des animaux notifiés morts
    private List<Animal> DeadAnimals; // Variable pour stocker la liste des animaux morts
    private EditText editTextNumTra;
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

        // Récupérer tous les animaux actif (=1) de l'élevage
        acctiveAnimals = db.getAnimalsByElevageAndActif(numElevage, 1);

        // Références des vues
        editTextNumTra = rootView.findViewById(R.id.editTextNumTra);
        buttonSubmit = rootView.findViewById(R.id.buttonSubmit);
        buttonEffacer = rootView.findViewById(R.id.buttonEffacer);


        // Bouton Soumettre
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération du numéro de travail entré par l'utilisateur
                String numTra = editTextNumTra.getText().toString();

                // Vérification si le champs numTra n'est pas vide
                if (numTra.isEmpty()) {
                    Toast.makeText(getContext(), "Veuillez entrer un numéro de travail", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                // Vérifier que le nombre de chiffres entré dans les champs du formulaire correspond à la longueur maximale spécifiée dans le layout
                if (numTra.length() != 4) {
                    // Afficher un message d'erreur
                    Toast.makeText(requireContext(), "Le numéro de travail doit contenir exactement " + 4 + " chiffres.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }

                // Vérifier si l'animal est déjà déclaré en cours de transfert vers un autre élevage
                ElevageTransfert = db.getAnimalsByElevageAndActif(numElevage, 2);
                for (Animal animal : ElevageTransfert) {
                    if (animal.getNumTra().equals(numTra)) {
                        Toast.makeText(getContext(), "L'animal " + numTra + " est déjà en cours de transfert vers un autre élevage.", Toast.LENGTH_SHORT).show();
                        return; // Arrêter l'exécution de la méthode
                    }
                }

                // Vérifier si l'animal est déjà déclaré en cours de transfert vers l'abattoir (actif=3)
                SlaughterhouseTransfer = db.getAnimalsByElevageAndActif(numElevage, 3);
                for (Animal animal : SlaughterhouseTransfer) {
                    if (animal.getNumTra().equals(numTra)) {
                        Toast.makeText(getContext(), "L'animal " + numTra + " est déjà en cours de transfert vers l'abattoir.", Toast.LENGTH_SHORT).show();
                        return; // Arrêter l'exécution de la méthode
                    }
                }

                // Vérifier si l'animal est déjà notifié mort (actif=-1)
                DeadNotifiedAnimals = db.getAnimalsByElevageAndActif(numElevage, -1);
                for (Animal animal : DeadNotifiedAnimals) {
                    if (animal.getNumTra().equals(numTra)) {
                        Toast.makeText(getContext(), "L'animal " + numTra + " est déjà notifié mort.", Toast.LENGTH_SHORT).show();
                        return; // Arrêter l'exécution de la méthode
                    }
                }

                // Vérifier si l'animal est déjà notifié mort (actif=0)
                DeadAnimals = db.getAnimalsByElevageAndActif(numElevage, 0);
                for (Animal animal : DeadAnimals) {
                    if (animal.getNumTra().equals(numTra)) {
                        Toast.makeText(getContext(), "L'animal " + numTra + " est déjà mort.", Toast.LENGTH_SHORT).show();
                        return; // Arrêter l'exécution de la méthode
                    }
                }

                // Vérifier si l'animal avec le numéro de travail donné existe dans l'élevage
                if (!db.isNumTraExists(numTra, acctiveAnimals)) {
                    Toast.makeText(getContext(), "L'animal " + numTra + " n'existe pas dans votre élevage.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }



                // Si tous les champs entrés sont corrects, afficher une boîte de dialogue de confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Êtes-vous sûr de vouloir réaliser le transfert de l'animal " + numTra + " vers l'abattoir ?");
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Modifier le statut de l'animal (3 = transfert vers l'abattoir)
                        db.updateStatus(numElevage, numTra, 3);

                        // Afficher un message de confirmation
                        Toast.makeText(requireContext(), "L'animal " + numTra + " est en attente de transfert à l'abattoir.", Toast.LENGTH_SHORT).show();
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

            }
        });

        return rootView;
    }

}