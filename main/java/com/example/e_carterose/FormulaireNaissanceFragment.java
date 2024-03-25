package com.example.e_carterose;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FormulaireNaissanceFragment extends Fragment {

    private DatabaseAccess db;
    private List<Animal> allAnimals; // Variable pour stocker la liste complète des animaux
    private Spinner spinnerSexe;
    private EditText editTextNom;
    private EditText editTextNumNat;
    private EditText editTextCodeRace;
    private Button buttonPickDateNais;
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

        // Acces à la BDD
        db = new DatabaseAccess(requireContext());

        // Récupérer le numéro de l'élevage depuis la variable statique
        String numElevage = MainActivity.numeroElevage;
        allAnimals = db.getActiveAnimalsByElevage(numElevage);



        // Récupérer les références des editText/Spinner/Boutton du formulaire
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonEffacer = view.findViewById(R.id.buttonEffacer);
        buttonPickDateNais = view.findViewById(R.id.buttonPickDateNais);
        editTextNom = view.findViewById(R.id.editTextNom);
        editTextNumNat = view.findViewById(R.id.editTextNumNat);
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

                // Vérifier si le numéro de travail entré existe déjà dans l'élevage
                String numNat = editTextNumNat.getText().toString();
                String numTra = numNat.substring(numNat.length() - 4); // Obtient les 4 derniers chiffres de numNat comme numTra ;
                if (db.isNumTraExists(numTra, allAnimals)) {
                    // Afficher un message d'erreur
                    Toast.makeText(requireContext(), "Le numéro de travail " + numTra + " est déjà utilisé.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }

                // Vérifier que le nombre de chiffres entré dans les champs du formulaire correspond à la longueur maximale spécifiée dans le layout
                if (numNat.length() != 10) {
                    // Afficher un message d'erreur
                    Toast.makeText(requireContext(), "Le numéro national doit contenir exactement " + 10 + " chiffres.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                String codeRace = editTextCodeRace.getText().toString();
                if (codeRace.length() != 2) {
                    // Afficher un message d'erreur
                    Toast.makeText(requireContext(), "Le code race doit contenir exactement " + 2 + " chiffres.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                String numNatPere = editTextNumNatPere.getText().toString();
                if (numNatPere.length() != 10) {
                    // Afficher un message d'erreur
                    Toast.makeText(requireContext(), "Le numéro national du père doit contenir exactement " + 10 + " chiffres.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                String codePaysPere = editTextCodePaysPere.getText().toString();
                if (codePaysPere.length() != 2) {
                    // Afficher un message d'erreur
                    Toast.makeText(requireContext(), "Le code pays du père doit contenir exactement " + 2 + " lettres.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                String codeRacePere = editTextCodeRacePere.getText().toString();
                if (codeRacePere.length() != 2) {
                    // Afficher un message d'erreur
                    Toast.makeText(requireContext(), "Le code race du père doit contenir exactement " + 2 + " chiffres.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                String numNatMere = editTextNumNatMere.getText().toString();
                if (numNatMere.length() != 10) {
                    // Afficher un message d'erreur
                    Toast.makeText(requireContext(), "Le numéro national de la mère doit contenir exactement " + 10 + " chiffres.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                String codePaysMere = editTextCodePaysMere.getText().toString();
                if (codePaysMere.length() != 2) {
                    // Afficher un message d'erreur
                    Toast.makeText(requireContext(), "Le code pays de la mère doit contenir exactement " + 2 + " lettres.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }
                String codeRaceMere= editTextCodeRaceMere.getText().toString();
                if (codeRaceMere.length() != 2) {
                    // Afficher un message d'erreur
                    Toast.makeText(requireContext(), "Le code race doit contenir exactement " + 2 + " chiffres.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter l'exécution de la méthode
                }

                //Appeler la fonction de sauvgarde des données dans la bdd (insertion d'une nouvelle ligne)
                saveFormData();

                // Réinitialiser le texte de tous les EditText
                editTextNom.setText("");
                editTextNumNat.setText("");
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

            }
        });


        //Bouton effacer
        buttonEffacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réinitialiser le texte de tous les EditText
                editTextNom.setText("");
                editTextNumNat.setText("");
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
    private void saveFormData() {

        // Récupération des valeurs des EditText et Sinner du formulaire
        String nom = editTextNom.getText().toString();
        String numNat = editTextNumNat.getText().toString();
        String codeRace = editTextCodeRace.getText().toString();
        String sexe = spinnerSexe.getSelectedItem().toString();
        String dateNais = buttonPickDateNais.getText().toString();
        String numNatPere = editTextNumNatPere.getText().toString();
        String codeRacePere = editTextCodeRacePere.getText().toString();
        String codePaysPere = editTextCodePaysPere.getText().toString();
        String numNatMere = editTextNumNatMere.getText().toString();
        String codeRaceMere= editTextCodeRaceMere.getText().toString();
        String codePaysMere = editTextCodePaysMere.getText().toString();
        String numElevage = MainActivity.numeroElevage;
        String numExpNais = MainActivity.numeroElevage;
        String numTra = numNat.substring(numNat.length() - 4); // Obtient les 4 derniers chiffres de numNat comme numTra ;
        String codePays = "FR"; //Code pays de l'exploitation
        String codePaysNais = "FR"; //Code pays de l'exploitation (lieu de naissance)
        String actif = "1";

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
            // Mise en pplace de log.e pour voir si on insert les bonnes données
            Log.e("numElevage :", numElevage);
            Log.e("nom :", nom);
            Log.e("sexe :", sexe);
            Log.e("dateNaiss", dateNais);
            Log.e("numTra :", numTra);
            Log.e("numNat :", numNat);
            Log.e("numNatPere :", numNatPere);
            Log.e("codeRacePere :", codeRacePere);
            Log.e("codePaysPere :", codePaysPere);
            Log.e("numNatMere :", numNatMere);
            Log.e("codeRaceMere :", codeRaceMere);
            Log.e("codePaysMere :", codePaysMere);
            Log.e("numExpNais :", numExpNais);
            Log.e("codePays :", codePays);
            Log.e("codeRace :", codeRace);
            Log.e("codePaysNais :", codePaysNais);
            Log.e("actif", actif);




            // Ajouter ces données à la base de données
            long newRowId = db.insertAnimal(numNat, numTra, codePays, nom, sexe, dateNais, codePaysNais, numExpNais, codePaysPere, numNatPere, codeRacePere, codePaysMere, numNatMere, codeRaceMere, numElevage, codeRace, actif);

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