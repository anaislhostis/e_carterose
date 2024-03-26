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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e_carterose.databinding.FragmentFormulaireMortBinding;

import java.util.List;


public class FormulaireMortFragment extends Fragment {

    private DatabaseAccess db;
    private List<Animal> allAnimals; // Variable pour stocker la liste complète des animaux
    private List<Animal> DeadNotifiedAnimals; // Variable pour stocker la liste complète des animaux notifié morts
    private EditText editTextNumTra;
    private Button buttonSubmit;
    private Button buttonEffacer;
    private Button buttonVoirNotifMort;
    private FragmentFormulaireMortBinding binding;

    public static FormulaireMortFragment newInstance() {
        FormulaireMortFragment fragment = new FormulaireMortFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFormulaireMortBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Acces à la BDD
        db = new DatabaseAccess(requireContext());

        // Récupérer le numéro de l'élevage depuis la variable statique
        String numElevage = MainActivity.numeroElevage;
        Log.e("numéro élevage :", numElevage);


        // Récupérer tous les animaux de l'élevage
        allAnimals = db.getAnimalsByElevage(numElevage);
        Log.e("Animal :", allAnimals.toString());


        // Récupérer les références des editText/Boutton du formulaire
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonEffacer = view.findViewById(R.id.buttonEffacer);
        buttonVoirNotifMort = view.findViewById(R.id.buttonVoirNotifMort);
        editTextNumTra = view.findViewById(R.id.editTextNumTra);

        // Bouton soumettre
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Afficher le popup de confirmation de suppression
                popupConfirmationDeathNotification(numElevage);

                // Réinitialiser le texte de l'EditText
                editTextNumTra.setText("");
            }
        });

        // Bouton effacer
        buttonEffacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réinitialiser le texte de l'EditText
                editTextNumTra.setText("");
            }
        });

        // Bouton voir les notifications de mort
        binding.buttonVoirNotifMort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une instance de VisualisationAnimauxMortsFragment
                VisualisationAnimauxMortsFragment visualisationAnimauxMortsFragment = VisualisationAnimauxMortsFragment.newInstance();

                // Remplacer le fragment actuel par le fragment VisualisationAnimauxMortsFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, visualisationAnimauxMortsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }



    // Popup pour la confirmation de notification de mort de l'animal
    private void popupConfirmationDeathNotification(String numElevage) {
        // Récupération du numéro de travail entré dans le formulaire
        String numTra = editTextNumTra.getText().toString();
        Log.e("Numéro de travail :", numTra);


        // Vérifier si l'animal a déjà été déclaré morts
        DeadNotifiedAnimals = db.getDeadNotifiedAnimalsByElevage(numElevage);
        for (Animal animal : DeadNotifiedAnimals) {
            if (animal.getNumTra().equals(numTra)) {
                // L'animal a déjà été notifié mort
                Toast.makeText(requireContext(), "L'animal " + numTra + " a déjà été notifié mort.", Toast.LENGTH_SHORT).show();
                return; // Arrêter l'exécution de la méthode si le numéro de travail est déjà présent dans la liste
            }
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmer la notification de mort");
        builder.setMessage(String.format("Êtes-vous sûr de vouloir notifier l'animal %s comme mort ?", numTra));
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Vérifier si l'animal avec le numéro de travail donné existe dans la base de données
                if (db.isNumTraExists(numTra, allAnimals)) {

                    // Modifier le statut de l'animal (actif = 2)
                    db.updateStatus(numElevage, numTra, -1);

                    Log.e("Liste des animaux morts :", db.getDeadNotifiedAnimalsByElevage(numElevage).toString());

                    // Afficher un message de confirmation
                    Toast.makeText(requireContext(), "L'animal " + numTra + " a été notifié comme mort.", Toast.LENGTH_SHORT).show();


                } else {
                    // Afficher un message d'erreur
                    Toast.makeText(requireContext(), "L'animal " + numTra + " n'existe pas dans votre élevage.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Non", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}