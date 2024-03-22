package com.example.e_carterose;

import static com.example.e_carterose.FragmentQRCode.generateQRContent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
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
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.List;
import android.util.Log;



import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DetailsAnimalEnTransfertElevageArriveeFragment extends Fragment {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private Animal selectedAnimal;
    private List<Animal> allAnimalsBDD; //récupérer tous les animaux de la BDD
    private DatabaseAccess db;
    private List<Vaccins> listVaccines; // Liste contenant tous les vaccins de la table
    private List<Soins> listCare; // Liste contenant tous les soins de la table
    private LinearLayout vaccinesContainer; //Layout pour afficher les vaccins dans un layout
    private LinearLayout careContainer; //Layout pour afficher les soins dans un layout


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details_animal_en_transfert_elevage_arrivee, container, false);

        // Initialiser la base de données
        db = new DatabaseAccess(requireContext());

        // Récupérer le numéro de l'élevage depuis la variable statique
        String numElevage = MainActivity.numeroElevage;


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

        // Set onClickListener for buttonQRCode
        Button buttonQRCode = rootView.findViewById(R.id.buttonQRCode);
        buttonQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace current layout with QR code layout

                FragmentQRCode fragment_qr_code = new FragmentQRCode();

                // Pass animal data to QR code fragment
                Bundle args = new Bundle();
                args.putSerializable("animal", selectedAnimal);
                fragment_qr_code.setArguments(args);

                // Replace current layout with QR code layout
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment_qr_code)
                        .addToBackStack(null)
                        .commit();
            }
        });


        Button buttonGeneratePDF = rootView.findViewById(R.id.buttonTelecharger);
        buttonGeneratePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });

        Button buttonArrival = rootView.findViewById(R.id.buttonArrivalAnimal);
        buttonArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Afficher une boîte de dialogue de confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("Êtes-vous sûr de valider le dépot de l'animal dans votre élevage ?");
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Récupérer le numéro de travail de l'animal
                        String numTra = selectedAnimal.getNumTra();

                        // Mettre à jour le statut de l'animal : actif = 1
                        db.updateStatus(numElevage, numTra, 1);

                        // Récupérer tous les animaux de la base de données
                        allAnimalsBDD = db.getAllanimalsFromLocalDatabase();

                        // Chercher l'animal dans l'exploitation de départ à travers les animaux de la BDD
                        for (Animal animal : allAnimalsBDD) {
                            // Vérifier si les numéros de travail et d'exploitation de naissance sont identiques (=vaches identiques)
                            if (isSameAnimal(animal, selectedAnimal)) {
                                if (!numElevage.equals(animal.getNumElevage())) {
                                    // Mettre à jour le statut de l'animal trouvé : déclare mort
                                    Log.e("numElevageDepart", animal.getNumElevage());
                                    Log.e("numElevageArrivée", numElevage);
                                    db.updateStatus(animal.getNumElevage(), numTra, 0);
                                }
                            }
                        }

                        // Afficher un message de confirmation
                        Toast.makeText(requireContext(), "L'animal " + numTra + " a été déposé dans votre élevage.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Non", null); // Pas besoin d'implémenter d'action pour le bouton "Non"
                builder.show();
            }
        });

        return rootView;
    }

    // Méthode pour vérifier si deux animaux sont identiques
    public boolean isSameAnimal(Animal animal1, Animal animal2) {
        if (animal1.getNumTra().equals(animal2.getNumTra()) && animal1.getNumExpNaiss().equals(animal2.getNumExpNaiss())){
            // Animaux identiques
            return true;
        }
        return false;
    }

    private void generatePDF() {
        // Vérifier si la permission WRITE_EXTERNAL_STORAGE est accordée
        if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Demander la permission à l'utilisateur
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // La permission est déjà accordée, vous pouvez générer le PDF
            createPDF();
        }
    }


    // Gérer la réponse de la demande de permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // La permission a été accordée, vous pouvez générer le PDF
                createPDF();
            } else {
                // La permission a été refusée, afficher un message à l'utilisateur ou prenez une autre action appropriée
                Toast.makeText(getActivity(), "Permission refusée pour écrire dans le stockage externe", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createPDF() {
        // Créer un nouveau document PDF
        PdfDocument document = new PdfDocument();
        // Créer une description de page
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // Taille A4 : 595x842
        // Démarrer une page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        int x = 50, y = 5; // Position de départ pour le dessin

        // Récupérer toutes les informations de l'animal
        String codPays = selectedAnimal.getCodPays();
        String numNat = selectedAnimal.getNumNat();
        String nom = selectedAnimal.getNom();
        String race = selectedAnimal.getRace();
        String dateNaiss = selectedAnimal.getDateNaiss() != null && selectedAnimal.getDateNaiss().length() >= 10 ? selectedAnimal.getDateNaiss().substring(0, 10) : "non attribué";
        String codPaysNaiss = selectedAnimal.getCodPaysNaiss();
        String expNaiss = selectedAnimal.getNumExpNaiss();
        String numTra = selectedAnimal.getNumTra();
        String sexe = (selectedAnimal.getSexe().equals("1") ? "Mâle" : "Femelle");
        String numNatPere = selectedAnimal.getNumNatPere() != null ? selectedAnimal.getNumNatPere() : "non connu";
        String codPaysPere = selectedAnimal.getCodPaysPere() != null ? selectedAnimal.getCodPaysPere() : "non connu";
        String codRacePere = selectedAnimal.getCodRacePere() != null ? selectedAnimal.getCodRacePere() : "non connue";
        String numNatMere = selectedAnimal.getNumNatMere() != null ? selectedAnimal.getNumNatMere() : "non connue";
        String codPaysMere = selectedAnimal.getCodPaysMere() != null ? selectedAnimal.getCodPaysMere() : "non connu";
        String codRaceMere = selectedAnimal.getCodRaceMere() != null ? selectedAnimal.getCodRaceMere() : "non connue";



        // Dessiner toutes les informations de l'animal sur le canevas
        // Première section rose
        paint.setColor(ContextCompat.getColor(requireContext(), R.color.rose_pale)); // Rose
        canvas.drawRect(0, y - 20, canvas.getWidth(), y + 250, paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD)); // Mettre en gras
        paint.setTextSize(24);
        paint.setColor(Color.BLACK); // Changer la couleur du texte en noir
        y += 25; // Espacement entre le haut de la page et le titre de la section rose

        canvas.drawText("Informations générales", x, y, paint); // Ajouter un titre à la section rose

        paint.setTypeface(Typeface.DEFAULT); // Réinitialiser la police à la police par défaut
        paint.setTextSize(18); // Taille de police régulière pour le contenu
        paint.setColor(Color.BLACK); // Changer la couleur du texte en noir

        y += 35; // Espacement entre le titre et le contenu

        canvas.drawText("Numéro de travail: " + numTra, x, y, paint);
        y += 25;
        canvas.drawText("Numéro national: " + numNat, x, y, paint);
        y += 25;
        canvas.drawText("Nom de l'animal: " + nom, x, y, paint);
        y += 25;
        canvas.drawText("Race: " + race, x, y, paint);
        y += 25;
        canvas.drawText("Date de naissance: " + dateNaiss, x, y, paint);
        y += 25;
        canvas.drawText("Code du pays de naissance: " + codPaysNaiss, x, y, paint);
        y += 25;
        canvas.drawText("Numéro d'exploitation de naissance: " + expNaiss, x, y, paint);
        y += 25;
        canvas.drawText("Sexe: " + sexe, x, y, paint);
        y += 25;



        // Zone blanche entre les deux sections
        paint.setColor(Color.WHITE); // Couleur de fond blanc
        canvas.drawRect(0, y, canvas.getWidth(), y + 50, paint);


        // Deuxième section bleue
        paint.setColor(ContextCompat.getColor(requireContext(), R.color.blue)); // Bleu clair pour le fond de la section
        canvas.drawRect(0, y, canvas.getWidth(), y + 225, paint);
        y += 25; // Espacement entre la zone blanche et le titre de la section bleue

        // Titre de la deuxième section en gras
        paint.setColor(Color.BLACK); // Couleur de texte noire pour le titre
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD)); // Mettre en gras
        paint.setTextSize(24);
        canvas.drawText("Génétique", x, y, paint); // Ajouter un titre à la section bleue


        // Réinitialiser la police pour le contenu
        paint.setTypeface(Typeface.DEFAULT); // Réinitialiser la police à la police par défaut
        paint.setTextSize(18); // Taille de police régulière pour le contenu

        y += 35; // Espacement entre le titre et le contenu
        paint.setColor(Color.BLACK);
        canvas.drawText("Numéro national du père: " + numNatPere, x, y, paint);
        y += 25;
        canvas.drawText("Code du pays du père: " + codPaysPere, x, y, paint);
        y += 25;
        canvas.drawText("Race du père: " + codRacePere, x, y, paint);
        y += 45;
        canvas.drawText("Numéro national de la mère: " + numNatMere, x, y, paint);
        y += 25;
        canvas.drawText("Code du pays de la mère: " + codPaysMere, x, y, paint);
        y += 25;
        canvas.drawText("Race de la mère: " + codRaceMere, x, y, paint);
        y += 25;




        // Récupérer toutes les informations de l'animal
        String animalInfo = generateQRContent(selectedAnimal);
        FragmentQRCode fragmentQRCode = new FragmentQRCode();
        Bitmap qrCodeBitmap = fragmentQRCode.generateQRCodeBitmap(animalInfo, requireContext(), 300, 300);

        // Si le bitmap du QR code est disponible, dessinez-le sur le canevas
        if (qrCodeBitmap != null) {
            // Calculer la position y pour afficher le QR code en dessous des autres informations
            y += 20; // Vous pouvez ajuster cette valeur selon vos besoins

            // Dessiner le QR code sur le canevas du PDF
            canvas.drawBitmap(qrCodeBitmap, x, y, paint);
            y += qrCodeBitmap.getHeight() + 50; // Incrémenter la valeur de y pour laisser de l'espace sous le QR code
        }


        // Terminer la page
        document.finishPage(page);

        // Obtenir le répertoire Téléchargements
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Enregistrer le document dans le répertoire Téléchargements
        String fileName = numTra + ".pdf";
        File file = new File(downloadsDir, fileName);

        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(getActivity(), "PDF créé avec succès dans Téléchargements", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Erreur lors de la création du PDF", Toast.LENGTH_SHORT).show();
        }

        // Fermer le document
        document.close();
    }


}