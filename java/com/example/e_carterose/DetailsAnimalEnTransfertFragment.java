package com.example.e_carterose;

import static com.example.e_carterose.QRCodeFragment.generateQRContent;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class DetailsAnimalEnTransfertFragment extends Fragment {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private Animal selectedAnimal;
    private DatabaseAccess db;
    private List<Vaccins> listVaccines; // Liste contenant tous les vaccins de la table
    private List<Soins> listCare; // Liste contenant tous les soins de la table
    private LinearLayout vaccinesContainer; //Layout pour afficher les vaccins dans un layout
    private LinearLayout careContainer; //Layout pour afficher les soins dans un layout


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details_animal_en_transfert, container, false);

        db = new DatabaseAccess(requireContext()); // Initialisez la base de données

        // Retrieve animal data from arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedAnimal = (Animal) bundle.getSerializable("animal");
        }

        // Initialize views
        TextView textViewNom = rootView.findViewById(R.id.detail_nom);
        TextView textViewNumTra = rootView.findViewById(R.id.num_tra);
        TextView textViewDateNaiss = rootView.findViewById(R.id.detail_date_naiss);
        TextView textViewSexe = rootView.findViewById(R.id.detail_sexe);
        TextView textViewRace = rootView.findViewById(R.id.detail_race);
        TextView textViewRacePere = rootView.findViewById(R.id.race_pere);
        TextView textViewRaceMere = rootView.findViewById(R.id.race_mere);
        TextView textViewNumNatPere = rootView.findViewById(R.id.num_nat_pere);
        TextView textViewNumNatMere = rootView.findViewById(R.id.num_nat_mere);

        // Display animal details
        textViewNom.setText(Html.fromHtml("<b>Nom:</b> " + (selectedAnimal.getNom() != null && !selectedAnimal.getNom().isEmpty() ? selectedAnimal.getNom() : "<i>Non attribué</i>")));

        if(selectedAnimal.getDateNaiss().length() >= 10) {
            textViewDateNaiss.setText(Html.fromHtml("<b>Date de naissance:</b> " + selectedAnimal.getDateNaiss().substring(0, 10)));
        } else {
            textViewDateNaiss.setText(Html.fromHtml("<b>Date de naissance:</b> " + selectedAnimal.getDateNaiss()));
        }

        textViewNumTra.setText(Html.fromHtml("<b>Animal n° :</b> " + selectedAnimal.getNumTra()));
        textViewSexe.setText(Html.fromHtml("<b>Sexe:</b> " + (selectedAnimal.getSexe().equals("1") ? "Mâle" : "Femelle")));

        String race = selectedAnimal.getRace();
        if (race != null && !race.isEmpty()) {
            textViewRace.setText(Html.fromHtml("<b>Race:</b> " + race));
        } else {
            textViewRace.setVisibility(View.GONE);
        }

        String racePere = selectedAnimal.getCodRacePere();
        if (racePere != null && !racePere.isEmpty()) {
            textViewRacePere.setText(Html.fromHtml("<b>Race du père:</b> " + racePere));
        } else {
            textViewRacePere.setVisibility(View.GONE);
        }

        String raceMere = selectedAnimal.getCodRaceMere();
        if (raceMere != null && !raceMere.isEmpty()) {
            textViewRaceMere.setText(Html.fromHtml("<b>Race de la mère:</b> " + raceMere));
        } else {
            textViewRaceMere.setVisibility(View.GONE);
        }

        String numNatPere = selectedAnimal.getNumNatPere();
        if (numNatPere != null && !numNatPere.isEmpty()) {
            textViewNumNatPere.setText(Html.fromHtml("<b>Numéro national du père:</b> " + numNatPere));
        } else {
            textViewNumNatPere.setVisibility(View.GONE);
        }

        String numNatMere = selectedAnimal.getNumNatMere();
        if (numNatMere != null && !numNatMere.isEmpty()) {
            textViewNumNatMere.setText(Html.fromHtml("<b>Numéro national de la mère:</b> " + numNatMere));
        } else {
            textViewNumNatMere.setVisibility(View.GONE);
        }

// Récupérer le numNat de l'animal
        String numNat = selectedAnimal.getNumNat();

        // Récupérer la liste des vaccins qui concerne l'animal
        listVaccines = db.getVaccinesByNumNat(numNat);

        // Récupérer une référence au layout pour afficher les vaccins
        vaccinesContainer = rootView.findViewById(R.id.animals_vaccines_container);

        // Vérifier si la liste des vaccins est vide
        if (listVaccines.isEmpty()) {
            // La liste des vaccins est vide
            TextView textViewNoVaccines = new TextView(requireContext());
            textViewNoVaccines.setText(Html.fromHtml("<i>Pas de vaccins recensés pour cet animal</i>"));
            textViewNoVaccines.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            vaccinesContainer.addView(textViewNoVaccines);

        } else {
            // Créer un TableLayout
            TableLayout tableLayoutVaccines = new TableLayout(requireContext());

            // Créer un TableRow pour les en-têtes
            TableRow headerRow = new TableRow(requireContext());
            TableRow.LayoutParams headerParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
            headerRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            // Créer des TextView pour les en-têtes de colonnes
            TextView headerNom = new TextView(requireContext());
            TextView headerDose = new TextView(requireContext());
            TextView headerDate = new TextView(requireContext());



            // Définir les en-têtes des colonnes en gras
            headerNom.setTypeface(null, Typeface.BOLD);
            headerNom.setText("Nom");
            headerDose.setTypeface(null, Typeface.BOLD);
            headerDose.setText("Dose (g)");
            headerDate.setTypeface(null, Typeface.BOLD);
            headerDate.setText("Date");

            // Ajout des en-têtes de colonnes au TableRow
            headerRow.addView(headerNom, headerParams);
            headerRow.addView(headerDose, headerParams);
            headerRow.addView(headerDate, headerParams);

            // Ajout du TableRow au TableLayout
            tableLayoutVaccines.addView(headerRow);


            // Parcourir la liste des vaccins pour ajouter les données au tableau
            for (Vaccins vaccin : listVaccines) {
                TableRow rowVaccine = new TableRow(requireContext());
                TableRow.LayoutParams rowParamsVaccine = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

                // Création des TextView pour les données du vaccin
                TextView nomVaccin = new TextView(requireContext());
                TextView doseVaccin = new TextView(requireContext());
                TextView dateVaccin = new TextView(requireContext());

                // Définition des données du vaccin
                nomVaccin.setText(vaccin.getNomVaccin());
                doseVaccin.setText(vaccin.getDose());
                dateVaccin.setText(vaccin.getDateVaccin());

                // Ajout des TextView au TableRow avec les paramètres de poids
                rowVaccine.addView(nomVaccin, headerParams);
                rowVaccine.addView(doseVaccin, headerParams);
                rowVaccine.addView(dateVaccin, headerParams);

                // Ajout du TableRow au TableLayout
                tableLayoutVaccines.addView(rowVaccine, rowParamsVaccine);
            }

            // Ajout du TableLayout à votre conteneur de vaccins
            vaccinesContainer.addView(tableLayoutVaccines);
        }

        // Récupérer la liste des soins qui concerne l'animal
        listCare = db.getCareByNumNat(numNat);

        // Récupérer une référence au layout pour afficher les vaccins
        careContainer = rootView.findViewById(R.id.animals_care_container);

        // Vérifier si la liste des vaccins est vide
        if (listCare.isEmpty()) {
            // La liste des vaccins est vide
            TextView textViewNoCare = new TextView(requireContext());
            textViewNoCare.setText(Html.fromHtml("<i>Pas de soins recensés pour cet animal</i>"));
            textViewNoCare.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            careContainer.addView(textViewNoCare);

        } else {
            // Créer un TableLayout
            TableLayout tableLayoutCare = new TableLayout(requireContext());

            // Créer un TableRow pour les en-têtes
            TableRow headerRow = new TableRow(requireContext());
            TableRow.LayoutParams headerParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
            headerRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            // Créer des TextView pour les en-têtes de colonnes
            TextView headerNom = new TextView(requireContext());
            TextView headerDose = new TextView(requireContext());
            TextView headerDate = new TextView(requireContext());

            // Définir les en-têtes des colonnes en gras
            headerNom.setTypeface(null, Typeface.BOLD);
            headerNom.setText("Nom");
            headerDose.setTypeface(null, Typeface.BOLD);
            headerDose.setText("Dose (g)");
            headerDate.setTypeface(null, Typeface.BOLD);
            headerDate.setText("Date");

            // Ajout des en-têtes de colonnes au TableRow
            headerRow.addView(headerNom, headerParams);
            headerRow.addView(headerDose, headerParams);
            headerRow.addView(headerDate, headerParams);

            // Ajout du TableRow au TableLayout
            tableLayoutCare.addView(headerRow);

            // Parcourir la liste des soins pour ajouter les données au tableau
            for (Soins soin : listCare) {
                TableRow rowCare = new TableRow(requireContext());
                TableRow.LayoutParams rowParamsCare = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

                // Création des TextView pour les données du soin
                TextView nomSoin = new TextView(requireContext());
                TextView doseSoin = new TextView(requireContext());
                TextView dateSoin = new TextView(requireContext());

                // Définition des données du vaccin
                nomSoin.setText(soin.getNomSoin());
                doseSoin.setText(soin.getDose());
                dateSoin.setText(soin.getDateSoin());

                // Ajout des TextView au TableRow avec les paramètres de poids
                rowCare.addView(nomSoin, headerParams);
                rowCare.addView(doseSoin, headerParams);
                rowCare.addView(dateSoin, headerParams);

                // Ajout du TableRow au TableLayout
                tableLayoutCare.addView(rowCare, rowParamsCare);
            }

            // Ajout du TableLayout à votre conteneur de soins
            careContainer.addView(tableLayoutCare);
        }


        // Bouton récupération de l'animal par l'équarisseur
        Button buttonRecupEquarisseur = rootView.findViewById(R.id.buttonArrivalAnimal);
        buttonRecupEquarisseur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numTra = selectedAnimal.getNumTra();
                String numElevage = selectedAnimal.getNumElevage();
                popupConfirmationAnimalTransfer(numElevage, numTra);

            }
        });

        return rootView;
    }





    private void popupConfirmationAnimalTransfer(String numElevage, String numTra) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmation de récupération de l'animal");
        builder.setMessage(String.format("L'animal %s a-t-il été déposé dans l'élevage  ?", selectedAnimal.getNumTra()));
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Appeler la fonction qui change le satut de l'animal (passe à 0 = non actif)
                db.updateStatus(numElevage, numTra, 0);

                Toast.makeText(requireContext(), "L'animal a été déposé dans son nouvel élevage.", Toast.LENGTH_SHORT).show();


            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(requireContext(), "L'animal n'a pas été récupéré par l'équarrisseur.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}