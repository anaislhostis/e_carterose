package com.example.e_carterose;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeFragment extends Fragment {

    private ImageView qrCodeImageView;
    private TextView textViewNumTra;
    private static DatabaseAccess db;
    private DatabaseAccess databaseAccess;
    private Context context;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull   LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_code, container, false);


        // Récupérer la largeur de l'écran en pixels
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidthPx = displayMetrics.widthPixels;

        // Récupérer les vues
        qrCodeImageView = view.findViewById(R.id.qr_code_image_view);
        textViewNumTra = view.findViewById(R.id.text_view_num_tra);

        // Récupérer les informations de l'animal
        Bundle args = getArguments();
        if (args != null) {
            Animal selectedAnimal = (Animal) args.getSerializable("animal");
            if (selectedAnimal != null) {
                String animalInfo = generateQRContent(selectedAnimal, context);

                String numTra = selectedAnimal.getNumTra();

                // Afficher le numéro de travail de l'animal dans le TextView correspondant
                textViewNumTra.setText(Html.fromHtml("<b>Animal n° :</b> " + "<b>" + numTra));

                // Générer et afficher le QR code dans l'ImageView correspondant
                Bitmap qrCodeBitmap = generateQRCodeBitmap(animalInfo, context, screenWidthPx, screenWidthPx);
                if (qrCodeBitmap != null) {
                    qrCodeImageView.setImageBitmap(qrCodeBitmap);
                }
            }
        }

        return view;
    }

    public static String generateQRContent(Animal animal, Context context) {

        String soins = "";
        String vaccins = "";

        // Récupérer toutes les informations de l'animal
        String codPays = animal.getCodPays();
        String numNat = animal.getNumNat();
        String nom = animal.getNom();
        String race = animal.getRace();
        String dateNaiss = animal.getDateNaiss();
        String codPaysNaiss = animal.getCodPaysNaiss();
        String expNaiss = animal.getNumExpNaiss();
        String numTra = animal.getNumTra();
        String sexe = (animal.getSexe().equals("1") ? "Mâle" : "Femelle");
        String numNatPere = animal.getNumNatPere();
        String codPaysPere = animal.getCodPaysPere();
        String codRacePere = animal.getCodRacePere();
        String numNatMere = animal.getNumNatMere();
        String codPaysMere = animal.getCodPaysMere();
        String codRaceMere = animal.getCodRaceMere();
        String actif = animal.getActif();
        String numElevage = animal.getNumElevage();
        String dateModif = animal.getDateModif();

        DatabaseAccess db = new DatabaseAccess(context);
        if (db != null) {
            // Utilisez databaseAccess pour accéder à la base de données
            soins = db.getCareByNumNat(animal.getNumNat()).toString();
            vaccins = db.getVaccinesByNumNat(animal.getNumNat()).toString();
            // Autres traitements...
        } else {
            Log.e("QRCodeFragment", "DatabaseAccess instance is null");
            // Gérer le cas où databaseAccess est null
        }

        // Afficher chaque champ pour vérification
        System.out.println("codPays: " + codPays);
        System.out.println("numNat: " + numNat);
        System.out.println("nom: " + nom);
        System.out.println("race: " + race);
        System.out.println("dateNaiss: " + dateNaiss);
        System.out.println("codPaysNaiss: " + codPaysNaiss);
        System.out.println("expNaiss: " + expNaiss);
        System.out.println("numTra: " + numTra);
        System.out.println("sexe: " + sexe);
        System.out.println("numNatPere: " + numNatPere);
        System.out.println("codPaysPere: " + codPaysPere);
        System.out.println("codRacePere: " + codRacePere);
        System.out.println("numNatMere: " + numNatMere);
        System.out.println("codPaysMere: " + codPaysMere);
        System.out.println("codRaceMere: " + codRaceMere);
        System.out.println("actif: " + actif);
        System.out.println("numElevage: " + numElevage);
        System.out.println("dateModif: " + dateModif);
        System.out.println("codPays: " + codPays);



        // Concaténer toutes les informations dans une chaîne de caractères
        return numNat + ":" + nom + ":" + race + ":" + dateNaiss + ":" + codPaysNaiss + ":" + expNaiss + ":" + numTra + ":" + sexe + ":" + numNatPere + ":" + codPaysPere + ":" + codRacePere + ":" + numNatMere + ":" + codPaysMere + ":" + codRaceMere + ":" + actif + ":" + numElevage + ":" + dateModif + ":" + codPays + ":" + soins + ":" + vaccins;
    }


    public Bitmap generateQRCodeBitmap(String qrContent, Context context, int width, int height) {

        if (context == null) {
            Log.e("FragmentQRCode", "Context is null");
            return null;
        }

        Log.d("QRCode", "Contenu du QR code : " + qrContent);

        try {
            // Utiliser la bibliothèque ZXing pour générer le QR code
            com.google.zxing.qrcode.QRCodeWriter writer = new com.google.zxing.qrcode.QRCodeWriter();

            // Utiliser la largeur de l'écran comme dimension pour le QR code
            BitMatrix bitMatrix = writer.encode(qrContent, BarcodeFormat.QR_CODE, width, height);

            // Créer le bitmap à partir de la matrice de bits
            Bitmap qrCodeBitmap = createBitmap(bitMatrix);

            if (qrCodeBitmap != null) {
                Log.d("QRCode", "Le bitmap du QR code a été généré avec succès");
            } else {
                Log.e("QRCode", "Le bitmap du QR code est null après la génération");
            }

            return qrCodeBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap createBitmap(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        return bitmap;
    }



    public void setDatabaseAccess(DatabaseAccess db) {
        this.databaseAccess = db;
    }



}
