package com.example.e_carterose;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.app.DatePickerDialog;
import java.util.Date;
import java.text.ParseException;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FormulaireVaccinsFragment extends Fragment {
    private Animal selectedAnimal;
    private DatabaseAccess db;
    private EditText editTextNomVaccin;
    private EditText editTextDose;
    private Button buttonPickDateVaccin;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private Button buttonSubmit;
    private Button buttonEffacer;


    public static FormulaireVaccinsFragment newInstance() {
        FormulaireVaccinsFragment fragment = new FormulaireVaccinsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_formulaire_vaccins, container, false);

        // Acces à la BDD
        db = new DatabaseAccess(requireContext());

        // Recupérer les données de l'animal passés en argument
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedAnimal = (Animal) bundle.getSerializable("animal");
        }

        // Initialiser les vues
        TextView textViewNumTra = rootView.findViewById(R.id.TitreFormAddVaccines);
        editTextNomVaccin = rootView.findViewById(R.id.editTextNomVaccin);
        editTextDose = rootView.findViewById(R.id.editTextDose);
        buttonPickDateVaccin = rootView.findViewById(R.id.buttonPickDateVaccin);
        buttonSubmit = rootView.findViewById(R.id.buttonSubmit);
        buttonEffacer = rootView.findViewById(R.id.buttonEffacer);

        // Ajouter le numéro de travail de l'animal dans le titre du formulaire
        textViewNumTra.setText(Html.fromHtml("<b> Ajouter un vaccin à l'animal n°</b> " + selectedAnimal.getNumTra()));

        // Initialiser la date
        calendar = Calendar.getInstance();

        // Initialiser le DatePickerDialog
        datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateButton(); // Met à jour le texte du bouton avec la date sélectionnée
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Bouton pour sélectionner la date
        buttonPickDateVaccin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        // Bouton pour soumettre le formulaire
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs entrées dans les champs EditText
                String nomVaccin = editTextNomVaccin.getText().toString().trim();
                String dose = editTextDose.getText().toString().trim();
                String dateVaccin = buttonPickDateVaccin.getText().toString().trim();

                // Vérifier si les champs ne sont pas vides
                if (nomVaccin.isEmpty() || dose.isEmpty() || dateVaccin.isEmpty()) {
                    Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return; // Sortir de la méthode onClick sans rien faire
                }

                // Créer le popup de confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Êtes-vous sûr de vouloir ajouter le vaccin '" + nomVaccin + "' pour l'animal " + selectedAnimal.getNumTra() + " ?");
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ajouter le vaccin à la base de données
                        db.addVaccine(selectedAnimal.getNumNat(), nomVaccin, dose, dateVaccin);
                        // Revenir au fragment précédent (DetailsAnimalFragment)
                        getParentFragmentManager().popBackStack();
                    }
                });
                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ne rien faire, fermer le dialogue
                    }
                });
                builder.show();
            }
        });


        return rootView;
    }

    // Méthode pour mettre à jour le texte du bouton avec la date sélectionnée
    private void updateDateButton() {
        String dateFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        buttonPickDateVaccin.setText(sdf.format(calendar.getTime()));
    }
}