package com.example.e_carterose;

import static com.example.e_carterose.QRCodeFragment.generateQRContent;

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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DetailsAnimalEnTransfertElevageDepartFragment extends Fragment {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private Animal selectedAnimal;
    private Elevage selectedElevage;
    private List<Animal> allAnimalsBDD; //récupérer tous les animaux de la BDD
    private DatabaseAccess db;
    private List<Vaccins> listVaccines; // Liste contenant tous les vaccins de la table
    private List<Soins> listCare; // Liste contenant tous les soins de la table
    private LinearLayout vaccinesContainer; //Layout pour afficher les vaccins dans un layout
    private LinearLayout careContainer; //Layout pour afficher les soins dans un layout


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details_animal_en_transfert_elevage_depart, container, false);

        // Initialiser la base de données
        db = new DatabaseAccess(requireContext());

        // Récupérer le numéro de l'élevage depuis la variable statique
        String numElevage = MainActivity.numeroElevage;


        // Retrieve animal data from arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedAnimal = (Animal) bundle.getSerializable("animal");
            selectedElevage = db.getElevageByNumero(selectedAnimal.getNumElevage());
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

        String race = selectedAnimal.getCodRace();
        if (race != null && !race.isEmpty()) {
            textViewRace.setText(Html.fromHtml("<b>Race:</b> " + race));
        } else {
            textViewRace.setVisibility(View.GONE);
        }

        String racePere = selectedAnimal.getCodRacePere();
        if (racePere != null && !racePere.isEmpty()) {
            String raceLabelPere = db.getRaceLabelByCode(racePere);
            textViewRacePere.setText(Html.fromHtml("<b>Race du père:</b> " + raceLabelPere));
        } else {
            textViewRacePere.setVisibility(View.GONE);
        }

        String raceMere = selectedAnimal.getCodRaceMere();
        if (raceMere != null && !raceMere.isEmpty()) {
            String raceLabelMere = db.getRaceLabelByCode(raceMere);
            textViewRaceMere.setText(Html.fromHtml("<b>Race de la mère:</b> " + raceLabelMere));
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

                QRCodeFragment fragment_qr_code = new QRCodeFragment();

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
        int x = 5, y = 0; // Position de départ pour le dessin

        // Récupérer toutes les informations de l'animal
        String codPays = selectedAnimal.getCodPays();
        String numNat = selectedAnimal.getNumNat();
        String nom = selectedAnimal.getNom();
        String race = selectedAnimal.getCodRace();
        String dateNaiss = selectedAnimal.getDateNaiss() != null && selectedAnimal.getDateNaiss().length() >= 10 ? selectedAnimal.getDateNaiss().substring(0, 10) : "non attribué";
        String codPaysNaiss = selectedAnimal.getCodPaysNaiss();
        String expNaiss = selectedAnimal.getNumExpNaiss();
        String numTra = selectedAnimal.getNumTra();
        String sexe = (selectedAnimal.getSexe().equals("1") ? "M" : "F");
        String numNatPere = selectedAnimal.getNumNatPere() != null ? selectedAnimal.getNumNatPere() : "non connu";
        String codPaysPere = selectedAnimal.getCodPaysPere() != null ? selectedAnimal.getCodPaysPere() : "non connu";
        String codRacePere = selectedAnimal.getCodRacePere() != null ? selectedAnimal.getCodRacePere() : "non connue";
        String numNatMere = selectedAnimal.getNumNatMere() != null ? selectedAnimal.getNumNatMere() : "non connue";
        String codPaysMere = selectedAnimal.getCodPaysMere() != null ? selectedAnimal.getCodPaysMere() : "non connu";
        String codRaceMere = selectedAnimal.getCodRaceMere() != null ? selectedAnimal.getCodRaceMere() : "non connue";
        String num_elevage = selectedAnimal.getNumElevage();
        String codRace = selectedAnimal.getCodRace();
        String raceLabel = db.getRaceLabelByCode(codRace);
        String raceLabelMere = db.getRaceLabelByCode(codRaceMere);
        String raceLabelPere = db.getRaceLabelByCode(codRacePere);

        String soins = formatSoinsList(db.getCareByNumNat(numNat));
        String vaccins = formatVaccinsList(db.getVaccinesByNumNat(numNat));

        String nom_elevage = selectedElevage.getNom_elevage();
        String attestation = MainActivity.asda;

        // Définir la taille de la police pour les titres et le texte
        int textSize = 7; // Taille de police pour les titres et le texte
        float lineHeight = 12; // Hauteur de ligne pour les sauts de ligne
        paint.setTextSize(textSize);


        int couleurRosePale = ContextCompat.getColor(requireContext(), R.color.rose_pale);
        // Appliquer la couleur rose_pale au pinceau pour le fond
        paint.setColor(couleurRosePale);

        canvas.drawRect(0, y, canvas.getWidth(), y + 500, paint); // Dessiner un rectangle rose pour la section

        // Modifier la couleur du texte en noir
        paint.setColor(Color.BLACK);

        y += 20;

        // Définir le style gras
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // Texte à centrer
        String textToCenter = "PASSEPORT DU BOVIN";

        // Mesurer la largeur du texte à centrer
        float textWidth = paint.measureText(textToCenter);

        // Calculer la position horizontale pour centrer le texte
        float centerX = (canvas.getWidth() - textWidth) / 2;

        // Dessiner le texte centré horizontalement et en gras
        canvas.drawText(textToCenter, centerX, y, paint);
        y += 2*lineHeight;
        // Dessiner toutes les informations de l'animal sur le canevas
        canvas.drawText("N° DE TRAVAIL         CODE PAYS          N° NATIONAL             SEXE             RACE                                DATE DE NAISSANCE", x, y, paint);
        y += lineHeight;
        paint.setTypeface(Typeface.DEFAULT);
        canvas.drawText(numTra + "                            " + codPays + "                          " + numNat + "              " + sexe + "                  " + race + "               " + dateNaiss, x, y, paint);

        y += lineHeight * 2; // Espacement entre les sections
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("N° D'EXPLOITATION DE NAISSANCE       N° D'EXPLOITATION D'EDITION       CODE TYPE RACIAUX DES PARENTS        DATE D'EDITION ", x, y, paint);
        y += lineHeight;
        paint.setTypeface(Typeface.DEFAULT);
        canvas.drawText(codPaysNaiss + " " + expNaiss + "                                                  " + codPays + " " + num_elevage + "                                                               " + codRacePere + "     " + codRaceMere + "                                        " + java.time.LocalDate.now(), x, y, paint);

        y += lineHeight * 2; // Espacement entre les sections

        // Ajouter un fond blanc à l'attestation
        paint.setColor(Color.WHITE); // Blanc
        canvas.drawRect(0, y, canvas.getWidth(), y + 2 * lineHeight, paint); // Dessiner un rectangle blanc pour l'attestation

        // Dessiner le texte de l'attestation
        paint.setColor(Color.BLACK); // Texte en noir
        canvas.drawText(attestation, x, y + lineHeight, paint); // Dessiner le texte de l'attestation

        y += 20; // Espacement après l'attestation

        // Revenir à la couleur rose pâle pour le titre suivant
        paint.setColor(couleurRosePale); // Rose pâle
        canvas.drawRect(0, y, canvas.getWidth(), y + 500, paint); // Dessiner un rectangle rose pour la section suivante
        //Reprendre une coueur noire pour le texte
        paint.setColor(Color.BLACK); // Texte en noir

        y += lineHeight * 2; // Espacement entre les sections
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("CERTIFICAT DE PARENTE GENETIQUE DU BOVIN", x, y, paint);

        y += lineHeight; // Espacement entre les sections

        canvas.drawText("VEAU :", x, y, paint);
        y += lineHeight; // Espacement entre les sections
        paint.setTypeface(Typeface.DEFAULT);

        canvas.drawText("NOM / N° DE TRAVAIL : " + nom + "/ " + numTra, x, y, paint);
        y += lineHeight; // Espacement entre les sections
        canvas.drawText("N° NATIONAL : " + numNat + "             CODE RACE : " + codRace + "            SEXE : " + sexe + "              DATE DE NAISSANCE : " + dateNaiss, x, y, paint);
        y += lineHeight * 2; // Espacement entre les sections
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("PERE:                                                                      | MERE:", x, y, paint);
        paint.setTypeface(Typeface.DEFAULT);
        y += lineHeight; // Espacement entre les sections
        canvas.drawText("N° NATIONAL : " +codPaysPere+ " " +numNatPere+ "                     | N° NATIONAL : " +codPaysMere+ " " +numNatMere, x, y, paint);
        y += lineHeight; // Espacement entre les sections
        canvas.drawText( "CODE RACE : " + codRacePere + "                                                 | CODE RACE : " + codRaceMere, x, y, paint);
        y += lineHeight; // Espacement entre les sections
        canvas.drawText( "RACE : " + raceLabelPere + "                                        | RACE : " + raceLabelMere, x, y, paint);


        y += 2* lineHeight; // Espacement entre les sections
        canvas.drawText("NAISSEUR: " + nom_elevage, x, y, paint);


        y += 2*lineHeight; // Espacement entre les sections

        // Supprimer le fond rose
        paint.setColor(Color.WHITE); // Blanc
        canvas.drawRect(0, y, canvas.getWidth(), y + 500, paint); // Dessiner un rectangle blanc pour effacer le fond rose

        y += 10; // Espacement entre les sections

        // Ajouter une nouvelle section pour les vaccins avec fond vert
        paint.setColor(ContextCompat.getColor(requireContext(), R.color.green)); // Vert
        canvas.drawRect(0, y, canvas.getWidth(), y + 75, paint); // Dessiner un rectangle vert pour la section

        // Titre de la section des vaccins
        paint.setColor(Color.BLACK); // Texte en noir
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD)); // Texte en gras
        y += lineHeight; // Espacement entre la section précédente et le titre
        canvas.drawText("SOINS ET VACCINS", x, y, paint); // Ajouter un titre pour la section des vaccins

        // Réinitialiser la police pour le contenu
        paint.setTypeface(Typeface.DEFAULT); // Réinitialiser la police à la police par défaut
        paint.setTextSize(textSize); // Taille de police régulière pour le contenu

        // Dessiner les informations sur les vaccins
        y += lineHeight; // Espacement entre le titre et le contenu
        paint.setColor(Color.BLACK); // Couleur du texte en noir

        // Dessiner les informations sur les vaccins
        canvas.drawText("Soins: " + soins, x, y, paint); // Afficher les informations sur les soins
        y += lineHeight; // Espacement entre chaque ligne
        canvas.drawText("Vaccins: " + vaccins, x, y, paint); // Afficher les informations sur les vaccins
        y += 10;

        paint.setColor(Color.WHITE); // Blanc
        canvas.drawRect(0, y, canvas.getWidth(), y + 500, paint); // Dessiner un rectangle blanc pour effacer le fond rose


        // Récupérer toutes les informations de l'animal
        String animalInfo = generateQRContent(selectedAnimal, getContext());
        QRCodeFragment fragmentQRCode = new QRCodeFragment();
        Bitmap qrCodeBitmap = fragmentQRCode.generateQRCodeBitmap(animalInfo, requireContext(), 200, 200);

        // Si le bitmap du QR code est disponible, on dessine sur le canvas
        if (qrCodeBitmap != null) {
            // Calculer la position y pour afficher le QR code en dessous des autres informations
            y += 20;

            // Dessiner le QR code sur le canevas du PDF
            canvas.drawBitmap(qrCodeBitmap, x, y, paint);
            y += qrCodeBitmap.getHeight() + 10; // Incrémenter la valeur de y pour laisser de l'espace sous le QR code
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

    // Méthode pour formater la liste des soins avec les mots devant chaque champ
    private String formatSoinsList(List<Soins> soinsList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Soins soin : soinsList) {
            stringBuilder.append("Nom : ").append(soin.getNomSoin()).append(", ")
                    .append("Dose : ").append(soin.getDose()).append(", ")
                    .append("Date : ").append(soin.getDateSoin()).append("\n");
        }
        return stringBuilder.toString();
    }

    // Méthode pour formater la liste des vaccins avec les mots devant chaque champ
    private String formatVaccinsList(List<Vaccins> vaccinsList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Vaccins vaccin : vaccinsList) {
            stringBuilder.append("Nom : ").append(vaccin.getNomVaccin()).append(",    ")
                    .append("Dose : ").append(vaccin.getDose()).append("g,    ")
                    .append("Date : ").append(vaccin.getDateVaccin()).append("\n");
        }
        return stringBuilder.toString();
    }



}