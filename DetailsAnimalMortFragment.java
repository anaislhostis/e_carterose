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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DetailsAnimalMortFragment extends Fragment {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private Animal selectedAnimal;
    private DatabaseAccess db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details_animal_mort, container, false);

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


        // Bouton de génération du PDF de la carte rose
        Button buttonGeneratePDF = rootView.findViewById(R.id.buttonTelecharger);
        buttonGeneratePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });


        // Bouton récupération de l'animal par l'équarisseur
        Button buttonRecupEquarisseur = rootView.findViewById(R.id.buttonRecupEquarisseur);
        buttonRecupEquarisseur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numNat = selectedAnimal.getNumNat();
                popupConfirmationRecoveryDeadAnimal(numNat);
            }
        });

        return rootView;
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

    private void popupConfirmationRecoveryDeadAnimal(String numNat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmation de récupération de l'animal");
        builder.setMessage(String.format("L'animal %s a-t-il été récupéré par l'équarrisseur ?", selectedAnimal.getNumTra()));
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(requireContext(), "L'animal a été récupéré par l'équarrisseur.", Toast.LENGTH_SHORT).show();

                //Appeler la fonction qui supprime l'animal de la base de données
                db.deleteAnimal(numNat);
                Log.e("numNat :", numNat);
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
