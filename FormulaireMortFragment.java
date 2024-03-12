package com.example.e_carterose;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.e_carterose.databinding.FragmentFormulaireMortBinding;



public class FormulaireMortFragment extends Fragment {

    private DatabaseAccess db;
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
                popupConfirmationNotificationMort();

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

        // Bouton notification de mort
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
    private void popupConfirmationNotificationMort() {
        // Récupération du numéro de travail entré dans le formulaire
        String numTra = editTextNumTra.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmer la notification de mort");
        builder.setMessage(String.format("Êtes-vous sûr de vouloir notifier l'animal %s comme mort ?", numTra));
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Ajouter le numéro de travail dans la liste des animaux morts du MainActivity
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.ajouterAnimalMort(numTra);

                // Afficher un message de confirmation
                Toast.makeText(requireContext(), "L'animal avec le numéro de travail " + numTra + " a été notifié comme mort.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Non", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
