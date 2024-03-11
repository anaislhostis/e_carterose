package com.example.e_carterose;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.app.DatePickerDialog;
import java.util.Date;
import java.text.ParseException;


public class FormulaireNaissanceFragment extends Fragment {

    private DatabaseAccess db;
    private Spinner spinnerSexe;
    private EditText editTextNom;
    private EditText editTextNumTra;
    private EditText editTextCodeRace;
    private Button buttonPickDateNais;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private EditText editTextCodeRacePere;
    private EditText editTextNumNatPere;
    private EditText editTextCodePaysPere;
    private EditText editTextCodeRaceMere;
    private EditText editTextNumNatMere;
    private EditText editTextCodePaysMere;
    private Button buttonSubmit;
    private Button buttonEffacer;

    public static FormulaireNaissanceFragment newInstance() {
        FormulaireNaissanceFragment fragment = new FormulaireNaissanceFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_formulaire_naissance, container, false);

        db = new DatabaseAccess(requireContext());

        // Récupérer les références des editText/Spinner/Boutton du formulaire
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonEffacer = view.findViewById(R.id.buttonEffacer);
        buttonPickDateNais = view.findViewById(R.id.buttonPickDateNais);
        editTextNom = view.findViewById(R.id.editTextNom);
        editTextNumTra = view.findViewById(R.id.editTextNumTra);
        editTextCodeRace = view.findViewById(R.id.editTextCodeRace);
        editTextCodeRacePere = view.findViewById(R.id.editTextCodeRacePere);
        editTextNumNatPere = view.findViewById(R.id.editTextNumNatPere);
        editTextCodePaysPere = view.findViewById(R.id.editTextCodePaysPere);
        editTextNumNatMere = view.findViewById(R.id.editTextNumNatMere);
        editTextCodeRaceMere = view.findViewById(R.id.editTextCodeRaceMere);
        editTextCodePaysMere = view.findViewById(R.id.editTextCodePaysMere);
        spinnerSexe = view.findViewById(R.id.spinnerSexe);

        // Définir les options à afficher dans le Spinner
        String[] options = {"1", "2"};

        // Créer un adaptateur pour le Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, options);

        // Spécifier la mise en forme du Spinner déroulant
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Appliquer l'adaptateur au Spinner
        spinnerSexe.setAdapter(adapter);



        // Initialisation de la date
        calendar = Calendar.getInstance();

        // Initialisation du DatePickerDialog
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
        buttonPickDateNais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });


        // Bouton soumettre
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Appeler la fonction de sauvgarde des données dans la bdd (insertion d'une nouvelle ligne)
                sauvegarderDonneesFormulaire();
            }
        });


        //Bouton effacer
        buttonEffacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réinitialiser le texte de tous les EditText
                editTextNom.setText("");
                editTextNumTra.setText("");
                editTextCodeRace.setText("");
                buttonPickDateNais.setText("Choisir une date");
                editTextCodePaysPere.setText("");
                editTextNumNatPere.setText("");
                editTextCodeRacePere.setText("");
                editTextCodePaysMere.setText("");
                editTextNumNatMere.setText("");
                editTextCodeRaceMere.setText("");

                // Sélectionner le premier élément dans le Spinner (s'il y en a)
                if (spinnerSexe.getAdapter() != null && spinnerSexe.getAdapter().getCount() > 0) {
                    spinnerSexe.setSelection(0);
                }

                // Afficher un message de confirmation
                Toast.makeText(requireContext(), "Champs effacés", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    // Méthode pour mettre à jour le texte du bouton avec la date sélectionnée
    private void updateDateButton() {
        String dateFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        buttonPickDateNais.setText(sdf.format(calendar.getTime()));
    }


    // Méthode pour récupérer les données du formulaire et les sauvegarder
    private void sauvegarderDonneesFormulaire() {
        // Récupération des valeurs des EditText et Sinner du formulaire
        String nom = editTextNom.getText().toString();
        String numTra = editTextNumTra.getText().toString();
        String codeRace = editTextCodeRace.getText().toString();
        String sexe = spinnerSexe.getSelectedItem().toString();
        String dateNais = buttonPickDateNais.getText().toString();
        String numNatPere = editTextNumNatPere.getText().toString();
        String codeRacePere = editTextCodeRacePere.getText().toString();
        String codePaysPere = editTextCodePaysPere.getText().toString();
        String numNatMere = editTextNumNatMere.getText().toString();
        String codeRaceMere= editTextCodeRaceMere.getText().toString();
        String codePaysMere = editTextCodePaysMere.getText().toString();
        //String numElevage = elevage.getNum_elevage();// Récupération du numéro d'élevage
        //String numNat = numElevage + numTra;
        String numElevage = "NULL";
        String numExpNais = "NULL";
        String numNat = "NULL";
        String codePays = "FR"; //Code pays de l'exploitation
        String codePaysNais = "FR"; //Code pays de l'exploitation (lieu de naissance)

        // Formater la date dans le bon format
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            // Convertir la date du format d'affichage en format de base de données
            Date date = inputFormat.parse(dateNais);
            if (date != null) {
                dateNais = outputFormat.format(date);
            } else {
                System.out.println("La date sélectionnée est invalide.");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la conversion de la date.");
        }

        // Vérifier si les champs obligatoires sont remplis
        if (!numTra.isEmpty() && !codeRace.isEmpty() && !sexe.isEmpty() && !dateNais.isEmpty() && !codeRacePere.isEmpty() && !codeRaceMere.isEmpty() && !numNatMere.isEmpty() && !codePaysMere.isEmpty()) {
            // Vérification si les champ sont vide et remplacement par null si nécessaire
            if (nom.isEmpty()) {
                nom = "NULL";
            }
            if (codePaysPere.isEmpty()){
                codePaysPere = "NULL";
            }
            if (numNatPere.isEmpty()){
                numNatPere = "NULL";
            }

            // Ajouter ces données à la base de données
            long newRowId = db.insertAnimal(numNat, numTra, codePays, nom, sexe, dateNais, codePaysNais, numExpNais,  codePaysPere, numNatPere, codeRacePere, codePaysMere, numNatMere, codeRaceMere, numElevage, codeRace);

            // Vérifier si l'insertion a réussi
            if (newRowId > 0) {
                // Afficher un message de confirmation
                Toast.makeText(requireContext(), "Formulaire soumis !", Toast.LENGTH_SHORT).show();
            } else {
                // Afficher un message d'erreur
                Toast.makeText(requireContext(), "Une erreur s'est produite lors de la sauvegarde du formulaire.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Afficher un message d'erreur indiquant les champs obligatoires manquants
            Toast.makeText(requireContext(), "Veuillez remplir tous les champs obligatoires.", Toast.LENGTH_SHORT).show();
        }
    }
}
