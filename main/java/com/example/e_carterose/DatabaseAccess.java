package com.example.e_carterose;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////Création, ouverture, fermeture de la BDD//////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
public class DatabaseAccess extends DatabaseOpenHelper {
    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    private Context context;
    private String num_elevage = MainActivity.numeroElevage;

    public DatabaseAccess(Context context) {
        super(context); // Appel au constructeur de la classe mère DatabaseOpenHelper
        this.context = context; // Attribution correcte du contexte à la variable de membre
        openHelper = this; // Attribution de l'instance actuelle à la variable openHelper
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        ;
        return instance;
    }


    public void open() {
        // Ouvrir la base de données pour la manipulation des données
        this.db = openHelper.getWritableDatabase();
    }

    // Fermeture de la connexion vers la BDD
    public void close() {
        if (db != null) {
            this.db.close();
        }
    }


    ;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////Requêtes vers la BDD//////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    List<Animal> animals = new ArrayList<>();

    @SuppressLint("Range")
    public List<Animal> getAnimalsByElevage(String numElevage) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = openHelper.getReadableDatabase();
            String query = "SELECT animal.*, elevage.* FROM animal INNER JOIN elevage ON animal.num_elevage = elevage.num_elevage WHERE elevage.num_elevage = ?";
            cursor = db.rawQuery(query, new String[]{numElevage});

            open(); // Ouvrir la connexion vers la base de données

            while (cursor.moveToNext()) {
                Animal animalObj = new Animal();

                String num_nat = cursor.getString(cursor.getColumnIndex("num_nat"));
                animalObj.setNumNat(cursor.getString(cursor.getColumnIndex("num_nat")));

                String nom = getNomByNumNat(num_nat);
                animalObj.setNom(nom);

                animalObj.setNumTra(cursor.getString(cursor.getColumnIndex("num_tra")));
                animalObj.setCodPays(cursor.getString(cursor.getColumnIndex("cod_pays")));
                animalObj.setSexe(cursor.getString(cursor.getColumnIndex("sexe")));
                animalObj.setDateNaiss(cursor.getString(cursor.getColumnIndex("date_naiss")));
                animalObj.setCodPaysNaiss(cursor.getString(cursor.getColumnIndex("cod_pays_naiss")));
                animalObj.setNumExpNaiss(cursor.getString(cursor.getColumnIndex("num_exp_naiss")));
                animalObj.setCodPaysPere(cursor.getString(cursor.getColumnIndex("cod_pays_pere")));
                animalObj.setNumNatPere(cursor.getString(cursor.getColumnIndex("num_nat_pere")));

                String raceCodePere = cursor.getString(cursor.getColumnIndex("cod_race_pere"));
                String raceLabelPere = getRaceLabelByCode(raceCodePere);
                animalObj.setCodRacePere(raceLabelPere);

                animalObj.setCodPaysMere(cursor.getString(cursor.getColumnIndex("cod_pays_mere")));
                animalObj.setNumNatMere(cursor.getString(cursor.getColumnIndex("num_nat_mere")));

                String raceCodeMere = cursor.getString(cursor.getColumnIndex("cod_race_mere"));
                String raceLabelMere = getRaceLabelByCode(raceCodeMere);
                animalObj.setCodRaceMere(raceLabelMere);

                animalObj.setNumElevage(cursor.getString(cursor.getColumnIndex("num_elevage")));

                String raceCode = cursor.getString(cursor.getColumnIndex("cod_race"));
                String raceLabel = getRaceLabelByCode(raceCode);
                animalObj.setRace(raceLabel);


                animals.add(animalObj);
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving data from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                close(); // Fermer la connexion vers la base de données dans tous les cas
            }
        }
        return animals;
    }


    @SuppressLint("Range")
    public String getCodAsdaByNumElevage(String numElevage) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String codAsda = null;
        try {
            if (numElevage != null) { // Vérifier que numElevage n'est pas null
                db = openHelper.getReadableDatabase();
                String query = "SELECT asda.cod_asda " +
                        "FROM asda " +
                        "JOIN elevage ON asda.cod_asda = elevage.cod_asda " +
                        "WHERE elevage.num_elevage = ?";

                Log.d("Requête SQL", query);

                cursor = db.rawQuery(query, new String[]{numElevage});

                if (cursor != null && cursor.moveToFirst()) {
                    // S'il y a une correspondance, récupérer la valeur de cod_asda
                    codAsda = cursor.getString(cursor.getColumnIndex("cod_asda"));
                    Log.d("Valeur asda récupérée ", codAsda);
                }
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving cod_asda from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                close(); // Fermer la connexion vers la base de données dans tous les cas
            }
        }
        return codAsda;
    }


    @SuppressLint("Range")
    public String getRaceLabelByCode(String raceCode) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String raceLabel = null;
        try {
            db = openHelper.getReadableDatabase();
            String query = "SELECT lib_long FROM race WHERE cod_race = ?";
            cursor = db.rawQuery(query, new String[]{raceCode});
            if (cursor.moveToFirst()) {
                raceLabel = cursor.getString(cursor.getColumnIndex("lib_long"));
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving race label from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                close();
            }
        }
        return raceLabel;
    }


    @SuppressLint("Range")
    public String getNomByNumNat(String numNat) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String nom = null;
        try {
            db = openHelper.getReadableDatabase();
            String query = "SELECT nom FROM animal WHERE num_nat = ?";
            cursor = db.rawQuery(query, new String[]{numNat});
            if (cursor.moveToFirst()) nom = cursor.getString(cursor.getColumnIndex("nom"));
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving nom from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                close();
            }
        }
        return nom;
    }


    public boolean checkElevageCredentials(String numElevage, String motDePasse) {
        Cursor cursor = null;
        boolean credentialsValid = false;
        try {
            open();
            String query = "SELECT * FROM elevage WHERE num_elevage = ? AND mdp = ?";
            cursor = db.rawQuery(query, new String[]{numElevage, motDePasse});
            if (cursor != null && cursor.getCount() > 0) {
                credentialsValid = true;
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error checking elevage credentials", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            close();
        }

        return credentialsValid;
    }


    public boolean isElevageExists(String numeroElevage) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean exists = false;
        try {
            db = openHelper.getReadableDatabase();
            String query = "SELECT 1 FROM elevage WHERE num_elevage = ?";
            cursor = db.rawQuery(query, new String[]{numeroElevage});
            exists = cursor != null && cursor.moveToFirst();

            // Vérifier également si le nom ou le mot de passe est non null
            if (exists) {
                String queryCheckCredentials = "SELECT 1 FROM elevage WHERE num_elevage = ? AND (nom IS NOT NULL OR mdp IS NOT NULL)";
                cursor = db.rawQuery(queryCheckCredentials, new String[]{numeroElevage});
                exists = cursor != null && cursor.moveToFirst();
            }

            Log.d("DatabaseAccess", "Elevage exists: " + exists);
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error checking if elevage exists in database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                close();
            }
        }
        return exists;
    }

    @SuppressLint("Range")
    public long createElevageProfile(String numeroElevage, String nomElevage, String motDePasse, String dateModif, String actif) {
        SQLiteDatabase db = null;
        long result = -1;

        try {
            db = openHelper.getWritableDatabase();

            // Vérifier d'abord si l'enregistrement avec num_elevage existe déjà
            Cursor cursor = db.rawQuery("SELECT * FROM elevage WHERE num_elevage = ?", new String[]{numeroElevage});

            // Si l'enregistrement existe, mettre à jour les colonnes nom, motDePasse, date_modif et actif
            if (cursor != null && cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put("nom", nomElevage);
                values.put("mdp", motDePasse);
                values.put("date_modif", dateModif);
                values.put("actif", actif);

                result = db.update("elevage", values, "num_elevage = ?", new String[]{numeroElevage});
            } else {
                // Si l'enregistrement n'existe pas, insérer un nouvel enregistrement
                ContentValues values = new ContentValues();
                values.put("num_elevage", numeroElevage);
                values.put("nom", nomElevage);
                values.put("mdp", motDePasse);
                values.put("date_modif", dateModif);
                values.put("actif", actif);

                result = db.insert("elevage", null, values);
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error creating/updating elevage profile", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return result;
    }


    // Méthode pour récupérer les informations sur l'élevage en fonction du numéro d'élevage
    @SuppressLint("Range")
    public Elevage getElevageByNumero(String numeroElevage) {
        Elevage elevage = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Définir la requête SQL pour récupérer les informations sur l'élevage en fonction du numéro d'élevage
            String query = "SELECT * FROM elevage WHERE num_elevage = ?";

            // Exécuter la requête avec le numéro d'élevage en tant que paramètre
            cursor = db.rawQuery(query, new String[]{numeroElevage});

            // Vérifier si des données ont été trouvées
            if (cursor != null && cursor.moveToFirst()) {
                // Récupérer les données de l'élevage depuis le curseur
                String nom = getNomElevageByNumero(numeroElevage);

                // Ajout de logs pour vérifier les valeurs
                Log.d("DatabaseAccess", "Valeur du nom de l'élevage dans la base de données : " + cursor.getString(cursor.getColumnIndex("nom")));
                Log.d("DatabaseAccess", "Valeur du nom dans getElevageByNumero : " + nom);

                String mdp = cursor.getString(cursor.getColumnIndex("mdp"));
                String codAsda = cursor.getString(cursor.getColumnIndex("cod_asda"));

                // Créer une nouvelle instance d'Elevage avec les données récupérées
                elevage = new Elevage();
                elevage.setNum_elevage(numeroElevage);
                elevage.setNom_elevage(nom);
                elevage.setMdp(mdp);
                elevage.setCod_asda(codAsda);
            }
        } catch (Exception e) {
            Log.e("DatabaseAccess", "Error while getting elevage by numero: " + e.getMessage());
        } finally {
            // Fermer le curseur et la base de données
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return elevage;
    }

    @SuppressLint("Range")
    public String getNomElevageByNumero(String numElevage) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String nomElevage = null;
        try {
            db = openHelper.getReadableDatabase();
            if (numElevage != null) { // Vérifier que numElevage n'est pas null
                String query = "SELECT nom FROM elevage WHERE num_elevage = ?";
                cursor = db.rawQuery(query, new String[]{numElevage});

                // Vérifier si le curseur contient des lignes
                if (cursor != null && cursor.moveToFirst()) {
                    // Si oui, récupérer la valeur du nom de l'élevage
                    nomElevage = cursor.getString(cursor.getColumnIndex("nom"));
                }
            } else {
                Log.e("DatabaseAccess", "numElevage is null");
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving nom from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                close();
            }
        }
        return nomElevage;
    }


    public long insertAnimal(String numNat, String numTra, String codePays, String nom, String sexe, String dateNais, String codePaysNais, String numExpNais, String codePaysPere, String numNatPere, String codeRacePere, String codePaysMere, String numNatMere, String codeRaceMere, String numElevage, String codeRace, String actif) {
        open(); // Ouvrir la connexion vers la base de données
        ContentValues values = new ContentValues(); //Classe pour stocker l'ensemble de valeurs
        values.put("cod_pays", codePays);
        values.put("num_nat", numNat);
        values.put("nom", nom);
        values.put("cod_race", codeRace);
        values.put("sexe", sexe);
        values.put("date_naiss", dateNais);
        values.put("cod_pays_naiss", codePaysNais);
        values.put("num_elevage", numElevage);
        values.put("cod_race_pere", codeRacePere);
        values.put("num_nat_pere", numNatPere);
        values.put("cod_pays_pere", codePaysPere);
        values.put("cod_race_mere", codeRaceMere);
        values.put("num_nat_mere", numNatMere);
        values.put("cod_pays_mere", codePaysMere);
        values.put("num_tra", numTra);
        values.put("num_exp_naiss", numExpNais);
        values.put("actif", actif);
        values.put("date_modif", getCurrentDateTime());

        // Insérer la ligne dans la table
        long newRowId = db.insert("animal", null, values);
        close(); // Fermer la connexion à la base de données
        return newRowId;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////Suppression données de la table animal///////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteAnimal(String numNat) {
        open(); // Ouvrir la connexion vers la base de données
        db.delete("animal", "num_nat = ?", new String[]{numNat});
        close(); // Fermer la connexion à la base de données
    }

    // Vérifie si un animal existe déjà dans la base de données locale
    public boolean animalExists(String numNat) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM animal WHERE num_nat = ?", new String[]{numNat});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }



    // Méthode pour obtenir la date actuelle au format spécifié
    public String getCurrentDateTime() {
        // Obtenir la date actuelle
        Date currentDate = new Date();

        // Définir le format de date souhaité
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Formater la date actuelle selon le format spécifié
        String formattedDate = dateFormat.format(currentDate);

        // Retourner la date formattée
        return formattedDate;
    }

    // Met à jour les informations de l'animal dans la base de données locale
    public void updateAnimal(Animal animal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Mettre à jour les colonnes appropriées avec les nouvelles valeurs de l'animal
        values.put("cod_pays", animal.getCodPays());
        values.put("num_nat", animal.getNumNat());
        values.put("nom", animal.getNom());
        values.put("cod_race", animal.getRace());
        values.put("sexe", animal.getSexe());
        values.put("date_naiss", animal.getDateNaiss());
        values.put("cod_pays_naiss", animal.getCodPaysNaiss());
        values.put("num_elevage", animal.getNumElevage());
        values.put("cod_race_pere", animal.getCodRacePere());
        values.put("num_nat_pere", animal.getNumNatPere());
        values.put("cod_pays_pere", animal.getCodPaysPere());
        values.put("cod_race_mere", animal.getCodRaceMere());
        values.put("num_nat_mere", animal.getNumNatMere());
        values.put("cod_pays_mere", animal.getCodPaysMere());
        values.put("num_tra", animal.getNumTra());
        values.put("num_exp_naiss", animal.getNumExpNaiss());

        db.update("animal", values, "num_nat = ?", new String[]{animal.getNumNat()});
    }




    private void insertElevage(String numElevage, String nom, String mdp, String codAsda, String actif, String dateModif) {
            SQLiteDatabase db = openHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("num_elevage", numElevage);
            values.put("nom", nom);
            values.put("mdp", mdp);
            values.put("cod_asda", codAsda);
            values.put("actif", actif);
            values.put("date_modif", dateModif);

            long newRowId = db.insert("elevage", null, values);
    }


    private void updateElevage(String numElevage, String nom, String mdp, String codAsda, String actif, String dateModif) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("mdp", mdp);
        values.put("cod_asda", codAsda);
        values.put("actif", actif);
        values.put("date_modif", dateModif);
        String selection = "num_elevage = ?";
        String[] selectionArgs = {numElevage};
        int count = db.update("elevage", values, selection, selectionArgs);
    }



    // Récupérer les animaux viviants (actif=1) de l'élevage
    @SuppressLint("Range")
    public List<Animal> getActiveAnimalsByElevage(String numElevage) {
        List<Animal> activeAnimals = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = openHelper.getReadableDatabase();
            String query = "SELECT animal.*, elevage.* FROM animal INNER JOIN elevage ON animal.num_elevage = elevage.num_elevage WHERE elevage.num_elevage = ? AND animal.actif = 1";
            cursor = db.rawQuery(query, new String[]{numElevage});

            open(); // Ouvrir la connexion vers la base de données

            while (cursor.moveToNext()) {
                Animal animalObj = new Animal();

                String num_nat = cursor.getString(cursor.getColumnIndex("num_nat"));
                animalObj.setNumNat(cursor.getString(cursor.getColumnIndex("num_nat")));

                String nom = getNomByNumNat(num_nat);
                animalObj.setNom(nom);

                animalObj.setNumTra(cursor.getString(cursor.getColumnIndex("num_tra")));
                animalObj.setCodPays(cursor.getString(cursor.getColumnIndex("cod_pays")));
                animalObj.setSexe(cursor.getString(cursor.getColumnIndex("sexe")));
                animalObj.setDateNaiss(cursor.getString(cursor.getColumnIndex("date_naiss")));
                animalObj.setCodPaysNaiss(cursor.getString(cursor.getColumnIndex("cod_pays_naiss")));
                animalObj.setNumExpNaiss(cursor.getString(cursor.getColumnIndex("num_exp_naiss")));
                animalObj.setCodPaysPere(cursor.getString(cursor.getColumnIndex("cod_pays_pere")));
                animalObj.setNumNatPere(cursor.getString(cursor.getColumnIndex("num_nat_pere")));

                String raceCodePere = cursor.getString(cursor.getColumnIndex("cod_race_pere"));
                String raceLabelPere = getRaceLabelByCode(raceCodePere);
                animalObj.setCodRacePere(raceLabelPere);

                animalObj.setCodPaysMere(cursor.getString(cursor.getColumnIndex("cod_pays_mere")));
                animalObj.setNumNatMere(cursor.getString(cursor.getColumnIndex("num_nat_mere")));

                String raceCodeMere = cursor.getString(cursor.getColumnIndex("cod_race_mere"));
                String raceLabelMere = getRaceLabelByCode(raceCodeMere);
                animalObj.setCodRaceMere(raceLabelMere);

                animalObj.setNumElevage(cursor.getString(cursor.getColumnIndex("num_elevage")));

                String raceCode = cursor.getString(cursor.getColumnIndex("cod_race"));
                String raceLabel = getRaceLabelByCode(raceCode);
                animalObj.setRace(raceLabel);

                animalObj.setActif(cursor.getString(cursor.getColumnIndex("actif")));
                animalObj.setDateModif(cursor.getString(cursor.getColumnIndex("date_modif")));


                activeAnimals.add(animalObj);
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving data from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                close(); // Fermer la connexion vers la base de données dans tous les cas
            }
        }
        return activeAnimals;
    }


    // Récupérer les animaux notifiés morts (actif='-1') de l'élevage
    @SuppressLint("Range")
    public List<Animal> getDeadNotifiedAnimalsByElevage(String numElevage) {
        List<Animal> DeadNotifiedAnimals = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = openHelper.getReadableDatabase();
            String query = "SELECT animal.*, elevage.* FROM animal INNER JOIN elevage ON animal.num_elevage = elevage.num_elevage WHERE elevage.num_elevage = ? AND animal.actif = -1";
            cursor = db.rawQuery(query, new String[]{numElevage});

            open(); // Ouvrir la connexion vers la base de données

            while (cursor.moveToNext()) {
                Animal animalObj = new Animal();

                String num_nat = cursor.getString(cursor.getColumnIndex("num_nat"));
                animalObj.setNumNat(cursor.getString(cursor.getColumnIndex("num_nat")));

                String nom = getNomByNumNat(num_nat);
                animalObj.setNom(nom);

                animalObj.setNumTra(cursor.getString(cursor.getColumnIndex("num_tra")));
                animalObj.setCodPays(cursor.getString(cursor.getColumnIndex("cod_pays")));
                animalObj.setSexe(cursor.getString(cursor.getColumnIndex("sexe")));
                animalObj.setDateNaiss(cursor.getString(cursor.getColumnIndex("date_naiss")));
                animalObj.setCodPaysNaiss(cursor.getString(cursor.getColumnIndex("cod_pays_naiss")));
                animalObj.setNumExpNaiss(cursor.getString(cursor.getColumnIndex("num_exp_naiss")));
                animalObj.setCodPaysPere(cursor.getString(cursor.getColumnIndex("cod_pays_pere")));
                animalObj.setNumNatPere(cursor.getString(cursor.getColumnIndex("num_nat_pere")));

                String raceCodePere = cursor.getString(cursor.getColumnIndex("cod_race_pere"));
                String raceLabelPere = getRaceLabelByCode(raceCodePere);
                animalObj.setCodRacePere(raceLabelPere);

                animalObj.setCodPaysMere(cursor.getString(cursor.getColumnIndex("cod_pays_mere")));
                animalObj.setNumNatMere(cursor.getString(cursor.getColumnIndex("num_nat_mere")));

                String raceCodeMere = cursor.getString(cursor.getColumnIndex("cod_race_mere"));
                String raceLabelMere = getRaceLabelByCode(raceCodeMere);
                animalObj.setCodRaceMere(raceLabelMere);

                animalObj.setNumElevage(cursor.getString(cursor.getColumnIndex("num_elevage")));

                String raceCode = cursor.getString(cursor.getColumnIndex("cod_race"));
                String raceLabel = getRaceLabelByCode(raceCode);
                animalObj.setRace(raceLabel);

                animalObj.setActif(cursor.getString(cursor.getColumnIndex("actif")));
                animalObj.setDateModif(cursor.getString(cursor.getColumnIndex("date_modif")));


                DeadNotifiedAnimals.add(animalObj);
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving data from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                close(); // Fermer la connexion vers la base de données dans tous les cas
            }
        }
        return DeadNotifiedAnimals;
    }


    // Récupérer les animaux en transfert vers un autre élevage de l'élevage
    @SuppressLint("Range")
    public List<Animal> getAnimalsTransferELevageByElevage(String numElevage) {
        List<Animal> AnimalsTransferELevage = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = openHelper.getReadableDatabase();
            String query = "SELECT animal.*, elevage.* FROM animal INNER JOIN elevage ON animal.num_elevage = elevage.num_elevage WHERE elevage.num_elevage = ? AND animal.actif = 2";
            cursor = db.rawQuery(query, new String[]{numElevage});

            open(); // Ouvrir la connexion vers la base de données

            while (cursor.moveToNext()) {
                Animal animalObj = new Animal();

                String num_nat = cursor.getString(cursor.getColumnIndex("num_nat"));
                animalObj.setNumNat(cursor.getString(cursor.getColumnIndex("num_nat")));

                String nom = getNomByNumNat(num_nat);
                animalObj.setNom(nom);

                animalObj.setNumTra(cursor.getString(cursor.getColumnIndex("num_tra")));
                animalObj.setCodPays(cursor.getString(cursor.getColumnIndex("cod_pays")));
                animalObj.setSexe(cursor.getString(cursor.getColumnIndex("sexe")));
                animalObj.setDateNaiss(cursor.getString(cursor.getColumnIndex("date_naiss")));
                animalObj.setCodPaysNaiss(cursor.getString(cursor.getColumnIndex("cod_pays_naiss")));
                animalObj.setNumExpNaiss(cursor.getString(cursor.getColumnIndex("num_exp_naiss")));
                animalObj.setCodPaysPere(cursor.getString(cursor.getColumnIndex("cod_pays_pere")));
                animalObj.setNumNatPere(cursor.getString(cursor.getColumnIndex("num_nat_pere")));

                String raceCodePere = cursor.getString(cursor.getColumnIndex("cod_race_pere"));
                String raceLabelPere = getRaceLabelByCode(raceCodePere);
                animalObj.setCodRacePere(raceLabelPere);

                animalObj.setCodPaysMere(cursor.getString(cursor.getColumnIndex("cod_pays_mere")));
                animalObj.setNumNatMere(cursor.getString(cursor.getColumnIndex("num_nat_mere")));

                String raceCodeMere = cursor.getString(cursor.getColumnIndex("cod_race_mere"));
                String raceLabelMere = getRaceLabelByCode(raceCodeMere);
                animalObj.setCodRaceMere(raceLabelMere);

                animalObj.setNumElevage(cursor.getString(cursor.getColumnIndex("num_elevage")));

                String raceCode = cursor.getString(cursor.getColumnIndex("cod_race"));
                String raceLabel = getRaceLabelByCode(raceCode);
                animalObj.setRace(raceLabel);

                animalObj.setActif(cursor.getString(cursor.getColumnIndex("actif")));
                animalObj.setDateModif(cursor.getString(cursor.getColumnIndex("date_modif")));


                AnimalsTransferELevage.add(animalObj);
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving data from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                close(); // Fermer la connexion vers la base de données dans tous les cas
            }
        }
        return AnimalsTransferELevage;
    }


    // Récupérer les animaux en transfert vers l'abattoir de l'élevage
    @SuppressLint("Range")
    public List<Animal> getAnimalsTransferslaughterhouseByElevage(String numElevage) {
        List<Animal> AnimalsTransferslaughterhouse = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = openHelper.getReadableDatabase();
            String query = "SELECT animal.*, elevage.* FROM animal INNER JOIN elevage ON animal.num_elevage = elevage.num_elevage WHERE elevage.num_elevage = ? AND animal.actif = 3";
            cursor = db.rawQuery(query, new String[]{numElevage});

            open(); // Ouvrir la connexion vers la base de données

            while (cursor.moveToNext()) {
                Animal animalObj = new Animal();

                String num_nat = cursor.getString(cursor.getColumnIndex("num_nat"));
                animalObj.setNumNat(cursor.getString(cursor.getColumnIndex("num_nat")));

                String nom = getNomByNumNat(num_nat);
                animalObj.setNom(nom);

                animalObj.setNumTra(cursor.getString(cursor.getColumnIndex("num_tra")));
                animalObj.setCodPays(cursor.getString(cursor.getColumnIndex("cod_pays")));
                animalObj.setSexe(cursor.getString(cursor.getColumnIndex("sexe")));
                animalObj.setDateNaiss(cursor.getString(cursor.getColumnIndex("date_naiss")));
                animalObj.setCodPaysNaiss(cursor.getString(cursor.getColumnIndex("cod_pays_naiss")));
                animalObj.setNumExpNaiss(cursor.getString(cursor.getColumnIndex("num_exp_naiss")));
                animalObj.setCodPaysPere(cursor.getString(cursor.getColumnIndex("cod_pays_pere")));
                animalObj.setNumNatPere(cursor.getString(cursor.getColumnIndex("num_nat_pere")));

                String raceCodePere = cursor.getString(cursor.getColumnIndex("cod_race_pere"));
                String raceLabelPere = getRaceLabelByCode(raceCodePere);
                animalObj.setCodRacePere(raceLabelPere);

                animalObj.setCodPaysMere(cursor.getString(cursor.getColumnIndex("cod_pays_mere")));
                animalObj.setNumNatMere(cursor.getString(cursor.getColumnIndex("num_nat_mere")));

                String raceCodeMere = cursor.getString(cursor.getColumnIndex("cod_race_mere"));
                String raceLabelMere = getRaceLabelByCode(raceCodeMere);
                animalObj.setCodRaceMere(raceLabelMere);

                animalObj.setNumElevage(cursor.getString(cursor.getColumnIndex("num_elevage")));

                String raceCode = cursor.getString(cursor.getColumnIndex("cod_race"));
                String raceLabel = getRaceLabelByCode(raceCode);
                animalObj.setRace(raceLabel);

                animalObj.setActif(cursor.getString(cursor.getColumnIndex("actif")));
                animalObj.setDateModif(cursor.getString(cursor.getColumnIndex("date_modif")));


                AnimalsTransferslaughterhouse.add(animalObj);
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving data from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                close(); // Fermer la connexion vers la base de données dans tous les cas
            }
        }
        return AnimalsTransferslaughterhouse;
    }

    // Fonction pour vérifier si un animal avec le numéro de travail donné existe déjà dans l'élevage
    public boolean isNumTraExists(String numTra, List<Animal> allAnimals) {

        // Parcourir la liste de tous les animaux de l'élevage
        for (Animal animal : allAnimals) {

            // Vérifier si le numéro de travail de l'animal entré correspond a un numéro de travail
            if (animal.getNumTra().equals(numTra)) {
                // Retourner vrai si l'animal existe
                return true;
            }
        }
        // Retourner faux si l'animal n'existe pas
        return false;
    }

    // Modifier le statut de l'animal
    public void updateStatus(String numElevage, String numTra, int statut) {
        try {
            open(); // Ouvrir la connexion vers la base de données

            // Convertir le statut en String
            String statutString = String.valueOf(statut);
            String formattedDate = getCurrentDateTime();

            // Créer un ContentValues pour mettre à jour la colonne "actif"
            ContentValues values = new ContentValues();
            values.put("actif", statutString);
            values.put("date_modif", formattedDate); // Modifier la date de modification

            // Exécuter la mise à jour dans la table "animal" en fonction du numéro d'élevage et du numéro de travail
            db.update("animal", values, "num_elevage = ? AND num_tra = ?", new String[]{numElevage, numTra});

        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error updating actif status in database", e);
        } finally {
            close(); // Fermer la connexion à la base de données
        }
    }

    // Méthode pour récupérer les informations de l'animal par le numTra et numElevage
    @SuppressLint("Range")
    public Animal getAnimalByNumTra(String numELevage, String numTra) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Animal animal = null;
        try {
            db = openHelper.getReadableDatabase();
            String query = "SELECT * FROM animal WHERE num_tra = ?";
            cursor = db.rawQuery(query, new String[]{numTra});
            if (cursor.moveToFirst()) {
                // Créer une nouvelle instance de Animal avec les données récupérées
                animal = new Animal();
                animal.setNumNat(cursor.getString(cursor.getColumnIndex("num_nat")));
                animal.setNom(cursor.getString(cursor.getColumnIndex("nom")));
                animal.setCodPays(cursor.getString(cursor.getColumnIndex("cod_pays")));
                animal.setSexe(cursor.getString(cursor.getColumnIndex("sexe")));
                animal.setDateNaiss(cursor.getString(cursor.getColumnIndex("date_naiss")));
                animal.setCodPaysNaiss(cursor.getString(cursor.getColumnIndex("cod_pays_naiss")));
                animal.setNumExpNaiss(cursor.getString(cursor.getColumnIndex("num_exp_naiss")));
                animal.setCodPaysPere(cursor.getString(cursor.getColumnIndex("cod_pays_pere")));
                animal.setNumNatPere(cursor.getString(cursor.getColumnIndex("num_nat_pere")));
                animal.setCodRacePere(cursor.getString(cursor.getColumnIndex("cod_race_pere")));
                animal.setCodPaysMere(cursor.getString(cursor.getColumnIndex("cod_pays_mere")));
                animal.setNumNatMere(cursor.getString(cursor.getColumnIndex("num_nat_mere")));
                animal.setCodRaceMere(cursor.getString(cursor.getColumnIndex("cod_race_mere")));
                animal.setNumElevage(cursor.getString(cursor.getColumnIndex("num_elevage")));
                animal.setRace(cursor.getString(cursor.getColumnIndex("cod_race")));
                animal.setDateModif(cursor.getString(cursor.getColumnIndex("date_modif")));
                animal.setActif(cursor.getString(cursor.getColumnIndex("actif")));
                animal.setNumTra(numTra);
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving animal by numTra from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                close();
            }
        }
        return animal;
    }

    // Méthode pour modifier le numElevage de l'animal en transfert
    public void updateNumElevage(String numElevage1, String numElevage2, String numTra) {
        try {
            open(); // Ouvrir la connexion vers la base de données

            // Créer un ContentValues pour mettre à jour la colonne "num_elevage"
            ContentValues values = new ContentValues();
            values.put("num_elevage", numElevage2);
            values.put("date_modif", getCurrentDateTime()); // Modifier la date de modification

            // Exécuter la mise à jour dans la table "animal" en fonction du numéro d'élevage et du numéro de travail
            db.update("animal", values, "num_elevage = ? AND num_tra = ?", new String[]{numElevage1, numTra});

        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error updating elevage in database", e);
        } finally {
            close(); // Fermer la connexion à la base de données
        }
    }


    @SuppressLint("Range")
    public List<Vaccins> getVaccinesByNumNat(String numNat) {
        List<Vaccins> vaccineList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openHelper.getReadableDatabase();

            // Exécutez la requête SQL pour récupérer les vaccins en fonction du numéro national
            cursor = db.rawQuery("SELECT * FROM vaccins WHERE num_nat = ?", new String[]{numNat});

            // Parcourez le curseur pour récupérer les enregistrements de vaccins
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Créez un nouvel objet Vaccine à partir des données du curseur
                    Vaccins vaccine = new Vaccins();
                    vaccine.setNomVaccin(cursor.getString(cursor.getColumnIndex("nom_vaccin")));
                    vaccine.setDose(cursor.getString(cursor.getColumnIndex("dose")));
                    vaccine.setDateVaccin(cursor.getString(cursor.getColumnIndex("date_vaccin")));

                    // Ajoutez le vaccin à la liste
                    vaccineList.add(vaccine);
                } while (cursor.moveToNext());

                // Fermez le curseur après utilisation
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving data from database", e);
        } finally {
            // Fermez la connexion à la base de données
            if (db != null) {
                close();
            }
        }
        // Retournez la liste des vaccins
        return vaccineList;
    }

    // Ajouter un vaccin à la table vaccins
    public void addVaccine(String numNat, String nomVaccin, String dose, String dateVaccin) {
        SQLiteDatabase db = null;

        try {
            db = openHelper.getWritableDatabase();

            // Création d'un ContentValues pour stocker les valeurs à insérer
            ContentValues values = new ContentValues();
            values.put("num_nat", numNat);
            values.put("nom_vaccin", nomVaccin);
            values.put("dose", dose);
            values.put("date_vaccin", dateVaccin);

            // Insertion des valeurs dans la table "vaccins"
            db.insert("vaccins", null, values);
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error adding vaccine to database", e);
        } finally {
            // Fermeture de la connexion à la base de données
            if (db != null) {
                db.close();
            }
        }
    }

    @SuppressLint("Range")
    public List<Soins> getCareByNumNat(String numNat) {
        List<Soins> careList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openHelper.getReadableDatabase();

            // Exécuter la requête SQL pour récupérer les vaccins en fonction du numéro national
            cursor = db.rawQuery("SELECT * FROM soins WHERE num_nat = ?", new String[]{numNat});

            // Parcourir le curseur pour récupérer les enregistrements de vaccins
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Créer un nouvel objet Soin à partir des données du curseur
                    Soins soin = new Soins();
                    soin.setNomSoin(cursor.getString(cursor.getColumnIndex("nom_soin")));
                    soin.setDose(cursor.getString(cursor.getColumnIndex("dose")));
                    soin.setDateSoin(cursor.getString(cursor.getColumnIndex("date_soin")));

                    // Ajouter le vaccin à la liste
                    careList.add(soin);
                } while (cursor.moveToNext());

                // Fermer le curseur après utilisation
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving data from database", e);
        } finally {
            // Fermer la connexion à la base de données
            if (db != null) {
                close();
            }
        }
        // Retourner la liste des soins
        return careList;
    }

    // Ajouter un soin à la table soins
    public void addCare(String numNat, String nomSoin, String dose, String dateSoin) {
        SQLiteDatabase db = null;

        try {
            db = openHelper.getWritableDatabase();

            // Création d'un ContentValues pour stocker les valeurs à insérer
            ContentValues values = new ContentValues();
            values.put("num_nat", numNat);
            values.put("nom_soin", nomSoin);
            values.put("dose", dose);
            values.put("date_soin", dateSoin);

            // Insertion des valeurs dans la table "soins"
            db.insert("soins", null, values);
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error adding care to database", e);
        } finally {
            // Fermeture de la connexion à la base de données
            if (db != null) {
                db.close();
            }
        }
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////// Méthodes pour la synchronisation des données des de la base du téléphone ///////////
    //////////////////////////////////avec le serveur distant //////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    // Méthode pour récupérer tous les élevages de la base de données locale
    @SuppressLint("Range")
    public List<Animal> getAllanimalsFromLocalDatabase() {
        List<Animal> animals = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // Ouvrir la base de données en mode lecture
            db = getContext().openOrCreateDatabase(DatabaseOpenHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

            // Exécuter la requête SQL pour récupérer tous les animaux
            cursor = db.rawQuery("SELECT * FROM animal", null);

            // Parcourir le curseur et ajouter chaque animal à la liste
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Animal animal = new Animal();
                    animal.setNumNat(cursor.getString(cursor.getColumnIndex("num_nat")));
                    animal.setNumTra(cursor.getString(cursor.getColumnIndex("num_tra")));
                    animal.setCodPays(cursor.getString(cursor.getColumnIndex("cod_pays")));
                    animal.setNom(cursor.getString(cursor.getColumnIndex("nom")));
                    animal.setSexe(cursor.getString(cursor.getColumnIndex("sexe")));
                    animal.setDateNaiss(cursor.getString(cursor.getColumnIndex("date_naiss")));
                    animal.setCodPaysNaiss(cursor.getString(cursor.getColumnIndex("cod_pays_naiss")));
                    animal.setNumExpNaiss(cursor.getString(cursor.getColumnIndex("num_exp_naiss")));
                    animal.setCodPaysPere(cursor.getString(cursor.getColumnIndex("cod_pays_pere")));
                    animal.setNumNatPere(cursor.getString(cursor.getColumnIndex("num_nat_pere")));
                    animal.setCodRacePere(cursor.getString(cursor.getColumnIndex("cod_race_pere")));
                    animal.setCodPaysMere(cursor.getString(cursor.getColumnIndex("cod_pays_mere")));
                    animal.setNumNatMere(cursor.getString(cursor.getColumnIndex("num_nat_mere")));
                    animal.setCodRaceMere(cursor.getString(cursor.getColumnIndex("cod_race_mere")));
                    animal.setNumElevage(cursor.getString(cursor.getColumnIndex("num_elevage")));
                    animal.setRace(cursor.getString(cursor.getColumnIndex("cod_race")));
                    animal.setDateModif(cursor.getString(cursor.getColumnIndex("date_modif")));
                    animal.setActif(cursor.getString(cursor.getColumnIndex("actif")));


                    animals.add(animal);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseAccess", "Erreur lors de la récupération des élevages depuis la base de données locale: " + e.getMessage());
        } finally {
            // Fermer le curseur et la base de données
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return animals;
    }


    // Méthode pour envoyer tous les animaux de la base locale au serveur
    public void sendAllAnimauxToServer() {
        new SendDataAsyncTask().execute();
    }

    // AsyncTask pour envoyer les données des animaux au serveur
    private class SendDataAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @SuppressLint("Range")
        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean success = false;
            try {

                List<Animal> allAnimaux = getAllanimalsFromLocalDatabase();
                // Parcourir le curseur et ajouter chaque élevage à la liste
                if (allAnimaux != null && !allAnimaux.isEmpty()) {
                    // Convertir la liste d'élevages en tableau JSON
                    JSONArray jsonArray = new JSONArray();

                    for (Animal animal : allAnimaux) {
                        JSONObject animalJson = new JSONObject();

                        animalJson.put("num_nat", animal.getNumNat());
                        animalJson.put("num_tra", animal.getNumTra());
                        animalJson.put("cod_pays", animal.getCodPays());
                        animalJson.put("nom", animal.getNom());
                        animalJson.put("sexe", animal.getSexe());
                        animalJson.put("date_naiss", animal.getDateNaiss());
                        animalJson.put("cod_pays_naiss", animal.getCodPaysNaiss());
                        animalJson.put("num_exp_naiss", animal.getNumExpNaiss());
                        animalJson.put("cod_pays_pere", animal.getCodPaysPere());
                        animalJson.put("num_nat_pere", animal.getNumNatPere());
                        animalJson.put("cod_race_pere", animal.getCodRacePere());
                        animalJson.put("cod_pays_mere", animal.getCodPaysMere());
                        animalJson.put("num_nat_mere", animal.getNumNatMere());
                        animalJson.put("cod_race_mere", animal.getCodRaceMere());
                        animalJson.put("num_elevage", animal.getNumElevage());
                        animalJson.put("cod_race", animal.getRace());
                        animalJson.put("date_modif", animal.getDateModif());
                        animalJson.put("actif", animal.getActif());

                        jsonArray.put(animalJson);

                    }

                    // Convertir le tableau JSON en chaîne JSON
                    String jsonData = jsonArray.toString();

                    // Créer une connexion HTTP et configurer la requête pour l'envoi des données JSON
                    URL url = new URL("http://10.10.64.12:5000/animaux_local/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Envoyer les données JSON à l'API
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(jsonData);
                    writer.flush();

                    // Vérifier le code de réponse
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        success = true;
                    }
                } else {
                    Log.e("SendDataAsyncTask", "Aucun animal à envoyer.");
                }
            } catch (IOException | JSONException e) {
                Log.e("SendDataAsyncTask", "Erreur lors de l'envoi des données des animaux : " + e.getMessage());
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                // Les données ont été envoyées avec succès
            } else {
                // L'envoi des données a échoué
            }
        }
    }



    // Méthode pour récupérer tous les élevages de la base de données locale
    @SuppressLint("Range")
    public List<Elevage> getAllElevagesFromLocalDatabase() {
        List<Elevage> elevages = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // Ouvrir la base de données en mode lecture
            db = getContext().openOrCreateDatabase(DatabaseOpenHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

            // Exécuter la requête SQL pour récupérer tous les élevages
            cursor = db.rawQuery("SELECT * FROM elevage", null);

            // Parcourir le curseur et ajouter chaque élevage à la liste
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Elevage elevage = new Elevage();
                    elevage.setNum_elevage(cursor.getString(cursor.getColumnIndex("num_elevage")));
                    elevage.setNom_elevage(cursor.getString(cursor.getColumnIndex("nom")));
                    elevage.setMdp(cursor.getString(cursor.getColumnIndex("mdp")));
                    elevage.setCod_asda(cursor.getString(cursor.getColumnIndex("cod_asda")));
                    elevage.setActif(cursor.getString(cursor.getColumnIndex("actif")));
                    elevage.setDate_modif(cursor.getString(cursor.getColumnIndex("date_modif")));

                    elevages.add(elevage);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseAccess", "Erreur lors de la récupération des élevages depuis la base de données locale: " + e.getMessage());
        } finally {
            // Fermer le curseur et la base de données
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return elevages;
    }


    // Méthode pour envoyer tous les élevages au serveur
    public void sendAllElevagesToServer() {
        new SendAllElevagesToServerTask().execute();
    }

    public class SendAllElevagesToServerTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean success = false;
            try {
                // Récupérer tous les élevages de la base de données locale
                List<Elevage> allElevages = getAllElevagesFromLocalDatabase();

                if (allElevages != null && !allElevages.isEmpty()) {
                    // Convertir la liste d'élevages en tableau JSON
                    JSONArray jsonArray = new JSONArray();
                    for (Elevage elevage : allElevages) {
                        JSONObject elevageJson = new JSONObject();
                        elevageJson.put("num_elevage", elevage.getNum_elevage());
                        elevageJson.put("nom", elevage.getNom_elevage());
                        elevageJson.put("mdp", elevage.getMdp());
                        elevageJson.put("cod_asda", elevage.getCod_asda());
                        elevageJson.put("actif", elevage.getActif());
                        elevageJson.put("date_modif", elevage.getDate_modif());

                        jsonArray.put(elevageJson);
                    }

                    // Convertir le tableau JSON en chaîne JSON
                    String jsonData = jsonArray.toString();

                    // Créer une connexion HTTP et configurer la requête pour l'envoi des données JSON
                    URL url = new URL("http://10.10.64.12:5000/elevages_local/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Envoyer les données JSON à l'API
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(jsonData);
                    writer.flush();

                    // Vérifier le code de réponse
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        success = true;
                    }
                } else {
                    Log.e("SendAllElevagesToServerTask", "Aucun élevage à envoyer.");
                }
            } catch (IOException | JSONException e) {
                Log.e("SendAllElevagesToServerTask", "Erreur lors de l'envoi des données des élevages : " + e.getMessage());
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                // Les données ont été envoyées avec succès
            } else {
                // L'envoi des données a échoué
            }
        }
    }



    // Méthode pour récupérer les numéros d'élevage distincts de la base de données locale
    public void getDistinctLocalElevageNumbers() {
        new GetDistinctLocalElevageNumbersTask().execute();
    }

    // Tâche asynchrone pour récupérer les numéros d'élevage distincts de la base de données locale
    public class GetDistinctLocalElevageNumbersTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... voids) {
            String[] distinctNumbers = null;
            SQLiteDatabase db = null;
            Cursor cursor = null;

            try {
                // Ouvrir la base de données en mode lecture
                db = getContext().openOrCreateDatabase(DatabaseOpenHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

                // Exécuter la requête SQL pour récupérer les numéros d'élevage distincts
                cursor = db.rawQuery("SELECT DISTINCT num_elevage FROM elevage", null);

                // Initialiser le tableau pour stocker les numéros d'élevage distincts
                List<String> numberList = new ArrayList<>();

                // Parcourir le curseur et ajouter chaque numéro d'élevage distinct à la liste
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex("num_elevage"));
                        numberList.add(number);
                    } while (cursor.moveToNext());
                }

                // Convertir la liste en tableau
                distinctNumbers = numberList.toArray(new String[0]);
                // Envoyer les numéros d'élevage distincts à l'URL spécifiée
                sendDistinctNumbersToServer(distinctNumbers);
            } catch (Exception e) {
                Log.e("GetDistinctElevageNumbersTask", "Erreur lors de la récupération des numéros d'élevage distincts depuis la base de données locale: " + e.getMessage());
            } finally {
                // Fermer le curseur et la base de données
                if (cursor != null) {
                    cursor.close();
                }
                if (db != null) {
                    db.close();
                }
            }

            return distinctNumbers;
        }

        @Override
        protected void onPostExecute(String[] distinctNumbers) {
            super.onPostExecute(distinctNumbers);
            if (distinctNumbers != null) {
                // Les numéros d'élevage distincts ont été récupérés avec succès
                // Faire quelque chose avec les numéros d'élevage (par exemple, les afficher)
            } else {
                // La récupération des numéros d'élevage distincts a échoué
            }
        }
    }


    // Méthode pour envoyer les numéros d'élevage distincts au serveur
    private void sendDistinctNumbersToServer(String[] distinctNumbers) {
        try {
            // Convertir la liste de numéros d'élevage distincts en tableau JSON
            JSONArray numbers = new JSONArray();
            for (String number : distinctNumbers) {
                numbers.put(number);
            }

            // Créer un objet JSON contenant le tableau de numéros d'élevage distincts
            JSONObject requestBody = new JSONObject();
            requestBody.put("distinct_elevage_numbers", numbers);

            // Créer l'URL pour la requête POST
            URL url = new URL("http://10.10.64.12:5000/distinct_local_elevage_numbers/");

            // Ouvrir la connexion HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Envoyer les données JSON à l'API
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(requestBody.toString());
            writer.flush();
            writer.close();

            // Vérifier le code de réponse
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Lecture de la réponse si nécessaire
            } else {
                // Gérer les erreurs de réponse du serveur si nécessaire
            }
        } catch (IOException | JSONException e) {
            Log.e("GetDistinctElevageNumbersTask", "Erreur lors de l'envoi des numéros d'élevage distincts : " + e.getMessage());
        }
    }








    // Méthode pour synchroniser les données des élevages
    public void synchronizeElevagesData() {
        // Créer une instance de la tâche de synchronisation
        SynchronizeElevageTask synchronizeTask = new SynchronizeElevageTask(context, openHelper);

        // Exécuter la tâche de synchronisation
        synchronizeTask.execute();
    }

    // Tâche asynchrone pour synchroniser les données des élevages
    private class SynchronizeElevageTask extends AsyncTask<Void, Void, Void> {
        private Context mContext;
        private DatabaseOpenHelper mOpenHelper;
        private JSONArray mToUpdateArray;
        private JSONArray mToAddArray;

        public SynchronizeElevageTask(Context context, DatabaseOpenHelper openHelper) {
            mContext = context;
            mOpenHelper = openHelper;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "http://10.10.64.12:5000/elevages_remote/";

                // Établir une connexion HTTP avec l'URL donnée
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");

                // Lire la réponse JSON
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                inputStream.close();

                // Convertir la réponse JSON en objet JSON
                JSONObject response = new JSONObject(stringBuilder.toString());

                // Extraire les chaînes de caractères pour "to_update" et "to_add"
                String toUpdateString = response.getString("to_update");
                String toAddString = response.getString("to_add");

                // Analyser les chaînes de caractères JSON en tant que tableaux JSON
                JSONArray toUpdateArray = new JSONArray(toUpdateString);
                JSONArray toAddArray = new JSONArray(toAddString);

                Log.d("SynchronizeElevageTask", "Données à mettre à jour : " + toUpdateArray.toString());
                Log.d("SynchronizeElevageTask", "Données à ajouter : " + toAddArray.toString());

                // Ajouter les élevages à la base de données locale (to_add)
                for (int i = 0; i < toAddArray.length(); i++) {
                    JSONObject elevageObject = toAddArray.getJSONObject(i);
                    String numElevage = elevageObject.getString("num_elevage");
                    String nom = elevageObject.getString("nom");
                    String mdp = elevageObject.getString("mdp");
                    String codAsda = elevageObject.getString("cod_asda");
                    String actif = elevageObject.getString("actif");
                    String dateModif = elevageObject.getString("date_modif");

                    Log.d("SynchronizeElevageTask", "num_elevage : " + numElevage);
                    Log.d("SynchronizeElevageTask", "nom : " + nom);
                    Log.d("SynchronizeElevageTask", "mdp : " + mdp);
                    Log.d("SynchronizeElevageTask", "cod_asda : " + codAsda);
                    Log.d("SynchronizeElevageTask", "actif : " + actif);
                    Log.d("SynchronizeElevageTask", "date_modif : " + dateModif);
                    Log.d("SynchronizeElevageTask", "Ajout de l'élevage : " + numElevage + " - " + nom + " - " + mdp + " - " + codAsda);

                    // Insérer l'élevage dans la base de données locale
                    insertElevage(numElevage, nom, mdp, codAsda, actif, dateModif);
                }

                // Mettre à jour les élevages dans la base de données locale (to_update)
                for (int i = 0; i < toUpdateArray.length(); i++) {
                    JSONObject elevageObject = toUpdateArray.getJSONObject(i);
                    String numElevage = elevageObject.getString("num_elevage");
                    String nom = elevageObject.getString("nom");
                    String mdp = elevageObject.getString("mdp");
                    String codAsda = elevageObject.getString("cod_asda");
                    String actif = elevageObject.getString("actif");
                    String dateModif = elevageObject.getString("date_modif");

                    // Mettre à jour l'élevage dans la base de données locale
                    updateElevage(numElevage, nom, mdp, codAsda, actif, dateModif);
                }
            } catch (IOException | JSONException e) {
                Log.e("SynchronizeElevageTask", "Erreur lors de la synchronisation : " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Afficher un message de synchronisation réussie
            Toast.makeText(context, "Données locales des élevages synchronisées avec celles du serveur.", Toast.LENGTH_SHORT).show();
        }
    }




    // Méthode pour synchroniser les données des animaux
    public void synchronizeAnimauxData() {
        // Créer une instance de la tâche de synchronisation
        SynchronizeAnimauxTask synchronizeTask = new SynchronizeAnimauxTask(context, openHelper);

        // Exécuter la tâche de synchronisation
        synchronizeTask.execute();
    }

    // Tâche asynchrone pour synchroniser les données des animaux
    private class SynchronizeAnimauxTask extends AsyncTask<Void, Void, Void> {
        private Context mContext;
        private DatabaseOpenHelper mOpenHelper;
        private JSONArray toUpdateArray; // Déclaration de la variable toUpdateArray
        private JSONArray toAddArray; // Déclaration de la variable toAddArray

        public SynchronizeAnimauxTask(Context context, DatabaseOpenHelper openHelper) {
            mContext = context;
            mOpenHelper = openHelper;
        }

        @SuppressLint("Range")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "http://10.10.64.12:5000/animaux_remote/";

                // Établir une connexion HTTP avec l'URL donnée
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");

                // Lire la réponse JSON
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                inputStream.close();

                // Convertir la réponse JSON en objet JSON
                JSONObject response = new JSONObject(stringBuilder.toString());

                // Extraire les chaînes de caractères pour "to_update" et "to_add"
                String toUpdateString = response.optString("to_update");
                String toAddString = response.optString("to_add");

                Log.d("SynchronizeAnimauxTask", "Données à mettre à jour : " + toUpdateString);
                Log.d("SynchronizeAnimauxTask", "Données à ajouter : " + toAddString);

                // Analyser les chaînes de caractères JSON en tant que tableaux JSON (si elles ne sont pas vides)
                if (!toUpdateString.isEmpty()) {
                    toUpdateArray = new JSONArray(toUpdateString);
                } else {
                    toUpdateArray = new JSONArray(); // Créer un tableau JSON vide
                }

                if (!toAddString.isEmpty()) {
                    toAddArray = new JSONArray(toAddString);
                } else {
                    toAddArray = new JSONArray(); // Créer un tableau JSON vide
                }

                if (toAddArray != null) {
                    // Ajouter les élevages à la base de données locale (to_add)
                    for (int i = 0; i < toAddArray.length(); i++) {
                        JSONObject animalObject = toAddArray.getJSONObject(i);

                        // Vérifier et récupérer la valeur pour chaque clé
                        String numNat = animalObject.has("num_nat") ? animalObject.getString("num_nat") : "";
                        String numTra = animalObject.has("num_tra") ? animalObject.getString("num_tra") : "";
                        String codPays = animalObject.has("cod_pays") ? animalObject.getString("cod_pays") : "";
                        String nom = animalObject.has("nom") ? animalObject.getString("nom") : "";
                        String sexe = animalObject.has("sexe") ? animalObject.getString("sexe") : "";
                        String dateNaiss = animalObject.has("date_naiss") ? animalObject.getString("date_naiss") : "";
                        String codPaysNaiss = animalObject.has("cod_pays_naiss") ? animalObject.getString("cod_pays_naiss") : "";
                        String numExpNaiss = animalObject.has("num_exp_naiss") ? animalObject.getString("num_exp_naiss") : "";
                        String codPaysPere = animalObject.has("cod_pays_pere") ? animalObject.getString("cod_pays_pere") : "";
                        String numNatPere = animalObject.has("num_nat_pere") ? animalObject.getString("num_nat_pere") : "";
                        String codRacePere = animalObject.has("cod_race_pere") ? animalObject.getString("cod_race_pere") : "";
                        String codPaysMere = animalObject.has("cod_pays_mere") ? animalObject.getString("cod_pays_mere") : "";
                        String numNatMere = animalObject.has("num_nat_mere") ? animalObject.getString("num_nat_mere") : "";
                        String codRaceMere = animalObject.has("cod_race_mere") ? animalObject.getString("cod_race_mere") : "";
                        String numElevage = animalObject.has("num_elevage") ? animalObject.getString("num_elevage") : "";
                        String codRace = animalObject.has("cod_race") ? animalObject.getString("cod_race") : "";
                        String actif = animalObject.has("actif") ? animalObject.getString("actif") : "";
                        String dateModif = animalObject.has("date_modif") ? animalObject.getString("date_modif") : "";

                        // Log des valeurs récupérées
                        Log.d("SynchronizeAnimauxTask", "num_nat : " + numNat);
                        Log.d("SynchronizeAnimauxTask", "num_tra : " + numTra);
                        Log.d("SynchronizeAnimauxTask", "cod_pays : " + codPays);
                        Log.d("SynchronizeAnimauxTask", "nom : " + nom);
                        Log.d("SynchronizeAnimauxTask", "sexe : " + sexe);
                        Log.d("SynchronizeAnimauxTask", "date_naiss : " + dateNaiss);
                        Log.d("SynchronizeAnimauxTask", "cod_pays_naiss : " + codPaysNaiss);
                        Log.d("SynchronizeAnimauxTask", "num_exp_naiss : " + numExpNaiss);
                        Log.d("SynchronizeAnimauxTask", "cod_pays_pere : " + codPaysPere);
                        Log.d("SynchronizeAnimauxTask", "num_nat_pere : " + numNatPere);
                        Log.d("SynchronizeAnimauxTask", "cod_race_pere : " + codRacePere);
                        Log.d("SynchronizeAnimauxTask", "cod_pays_mere : " + codPaysMere);
                        Log.d("SynchronizeAnimauxTask", "num_nat_mere : " + numNatMere);
                        Log.d("SynchronizeAnimauxTask", "cod_race_mere : " + codRaceMere);
                        Log.d("SynchronizeAnimauxTask", "num_elevage : " + numElevage);
                        Log.d("SynchronizeAnimauxTask", "cod_race : " + codRace);
                        Log.d("SynchronizeAnimauxTask", "actif : " + actif);
                        Log.d("SynchronizeAnimauxTask", "date_modif : " + dateModif);

                        // Utiliser les valeurs récupérées selon les besoins...

                        // Insérer l'animal dans la base de données locale
                        insertAnimal(numNat, numTra, codPays, nom, sexe, dateNaiss, codPaysNaiss, numExpNaiss, codPaysPere,
                                numNatPere, codRacePere, codPaysMere, numNatMere, codRaceMere, numElevage, codRace, actif);
                    }

                } else {
                    Log.d("SynchronizeAnimauxTask", "Aucune donnée à ajouter.");
                }

                // Mettre à jour les animaux dans la base de données locale (to_update)
                if (toUpdateArray != null) {

                    // Mettre à jour les animaux dans la base de données locale (to_update)
                    for (int i = 0; i < toUpdateArray.length(); i++) {
                        JSONObject animalObject = toUpdateArray.getJSONObject(i);
                        Animal animal = new Animal();
                        String numNat = animalObject.getString("num_nat");
                        String numTra = animalObject.getString("num_tra");
                        String codPays = animalObject.getString("cod_pays");
                        String nom = animalObject.getString("nom");
                        String sexe = animalObject.getString("sexe");
                        String dateNaiss = animalObject.getString("date_naiss");
                        String codPaysNaiss = animalObject.getString("cod_pays_naiss");
                        String numExpNaiss = animalObject.getString("num_exp_naiss");
                        String codPaysPere = animalObject.getString("cod_pays_pere");
                        String numNatPere = animalObject.getString("num_nat_pere");
                        String codRacePere = animalObject.getString("cod_race_pere");
                        String codPaysMere = animalObject.getString("cod_pays_mere");
                        String numNatMere = animalObject.getString("num_nat_mere");
                        String codRaceMere = animalObject.getString("cod_race_mere");
                        String numElevage = animalObject.getString("num_elevage");
                        String codRace = animalObject.getString("cod_race");
                        String actif = animalObject.getString("actif");
                        String dateModif = animalObject.getString("date_modif");

                        animal.setNumNat(numNat);
                        animal.setNumTra(numTra);
                        animal.setCodPays(codPays);
                        animal.setNom(nom);
                        animal.setSexe(sexe);
                        animal.setDateNaiss(dateNaiss);
                        animal.setCodPaysNaiss(codPaysNaiss);
                        animal.setNumExpNaiss(numExpNaiss);
                        animal.setCodPaysPere(codPaysPere);
                        animal.setNumNatPere(numNatPere);
                        animal.setCodRacePere(codRacePere);
                        animal.setCodPaysMere(codPaysMere);
                        animal.setNumNatMere(numNatMere);
                        animal.setCodRaceMere(codRaceMere);
                        animal.setNumElevage(numElevage);
                        animal.setRace(codRace);
                        animal.setActif(actif);
                        animal.setDateModif(dateModif);

                        // Mettre à jour l'animal dans la base de données locale
                        updateAnimal(animal);

                    }

                } else {
                    Log.d("SynchronizeAnimauxTask", "Aucune donnée à mettre à jour.");
                }

            } catch (IOException | JSONException e) {
                Log.e("SynchronizeAnimauxTask", "Erreur lors de la synchronisation : " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, "Données locales des animaux synchronisées avec celles du serveur.", Toast.LENGTH_SHORT).show();
        }
    }






    // Méthode pour récupérer toutes les informations des soins de la base de données locale
    @SuppressLint("Range")
    public List<Soins> getAllSoinsFromLocalDatabase(){
        List<Soins> soinsList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openHelper.getReadableDatabase();

            // Exécuter la requête SQL pour récupérer les soins
            cursor = db.rawQuery("SELECT * FROM soins", null);

            // Parcourir le curseur pour récupérer les enregistrements de soins
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Créer un nouvel objet Soin à partir des données du curseur
                    Soins soin = new Soins();
                    soin.setNumNat(cursor.getString(cursor.getColumnIndex("num_nat")));
                    soin.setNomSoin(cursor.getString(cursor.getColumnIndex("nom_soin")));
                    soin.setDose(cursor.getString(cursor.getColumnIndex("dose")));
                    soin.setDateSoin(cursor.getString(cursor.getColumnIndex("date_soin")));

                    // Ajouter le soin à la liste
                    soinsList.add(soin);
                } while (cursor.moveToNext());
                Log.d("DatabaseAccess", "Liste des soins récupérée : " + soinsList.toString());

                // Fermer le curseur après utilisation
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving data from database", e);
        } finally {
            // Fermer la connexion à la base de données
            if (db != null) {
                close();
            }
        }
        // Retourner la liste des soins
        return soinsList;
    }


    // Méthode pour envoyer tous les soins au serveur
    public void sendAllSoinsToServer() {
        new SendAllSoinsToServerTask().execute();
    }

    public class SendAllSoinsToServerTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean success = false;
            try {
                // Récupérer tous les élevages de la base de données locale
                List<Soins> allSoins = getAllSoinsFromLocalDatabase();
                for (Soins soin : allSoins) {
                    Log.d("DatabaseAccess", "Soin: " + soin.toString());
                }

                if (allSoins != null && !allSoins.isEmpty()) {
                    // Convertir la liste d'élevages en tableau JSON
                    JSONArray jsonArray = new JSONArray();
                    for (Soins soin : allSoins) {
                        JSONObject soinJson = new JSONObject();
                        soinJson.put("num_nat", soin.getNumNat());
                        soinJson.put("nom_soin", soin.getNomSoin());
                        soinJson.put("dose", soin.getDose());
                        soinJson.put("date_soin", soin.getDateSoin());
                        jsonArray.put(soinJson);
                    }

                    // Convertir le tableau JSON en chaîne JSON
                    String jsonData = jsonArray.toString();

                    // Créer une connexion HTTP et configurer la requête pour l'envoi des données JSON
                    URL url = new URL("http://10.10.64.12:5000/soins_local/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Envoyer les données JSON à l'API
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(jsonData);
                    writer.flush();

                    // Vérifier le code de réponse
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        success = true;
                    }
                } else {
                    Log.e("SendAllSoinsToServerTask", "Aucun soin à envoyer.");
                }
            } catch (IOException | JSONException e) {
                Log.e("SendAllSoinsToServerTask", "Erreur lors de l'envoi des données des soins : " + e.getMessage());
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                Log.d("SendAllSoinsToServerTask", "Les données des soins ont été envoyées avec succès.");
            } else {
                Log.e("SendAllSoinsToServerTask", "Erreur lors de l'envoi des données des soins.");
            }
        }
    }




    // Méthode pour récupérer toutes les informations des vaccins de la base de données locale
    @SuppressLint("Range")
    public List<Vaccins> getAllVaccinsFromLocalDatabase(){
        List<Vaccins> vaccinsList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openHelper.getReadableDatabase();

            // Exécuter la requête SQL pour récupérer les vaccins
            cursor = db.rawQuery("SELECT * FROM vaccins", null);

            // Parcourir le curseur pour récupérer les enregistrements de vaccins
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Créer un nouvel objet vaccin à partir des données du curseur
                    Vaccins vaccin = new Vaccins();
                    vaccin.setNumNat(cursor.getString(cursor.getColumnIndex("num_nat")));
                    vaccin.setNomVaccin(cursor.getString(cursor.getColumnIndex("nom_vaccin")));
                    vaccin.setDose(cursor.getString(cursor.getColumnIndex("dose")));
                    vaccin.setDateVaccin(cursor.getString(cursor.getColumnIndex("date_vaccin")));

                    // Ajouter le vaccin à la liste
                    vaccinsList.add(vaccin);
                } while (cursor.moveToNext());
                Log.d("DatabaseAccess", "Liste des vaccins récupérée : " + vaccinsList.toString());

                // Fermer le curseur après utilisation
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error retrieving data from database", e);
        } finally {
            // Fermer la connexion à la base de données
            if (db != null) {
                close();
            }
        }
        // Retourner la liste des vaccins
        return vaccinsList;
    }


    // Méthode pour envoyer tous les vaccins au serveur
    public void sendAllvaccinsToServer() {
        new SendAllvaccinsToServerTask().execute();
    }

    public class SendAllvaccinsToServerTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean success = false;
            try {
                // Récupérer tous les élevages de la base de données locale
                List<Vaccins> allvaccins = getAllVaccinsFromLocalDatabase();
                for (Vaccins vaccin : allvaccins) {
                    Log.d("DatabaseAccess", "vaccin: " + vaccin.toString());
                }

                if (allvaccins != null && !allvaccins.isEmpty()) {
                    // Convertir la liste d'élevages en tableau JSON
                    JSONArray jsonArray = new JSONArray();
                    for (Vaccins vaccin : allvaccins) {
                        JSONObject vaccinJson = new JSONObject();
                        vaccinJson.put("num_nat", vaccin.getNumNat());
                        vaccinJson.put("nom_vaccin", vaccin.getNomVaccin());
                        vaccinJson.put("dose", vaccin.getDose());
                        vaccinJson.put("date_vaccin", vaccin.getDateVaccin());
                        jsonArray.put(vaccinJson);
                    }

                    // Convertir le tableau JSON en chaîne JSON
                    String jsonData = jsonArray.toString();


                    // Créer une connexion HTTP et configurer la requête pour l'envoi des données JSON
                    URL url = new URL("http://10.10.64.12:5000/vaccins_local/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Envoyer les données JSON à l'API
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(jsonData);
                    writer.flush();

                    // Vérifier le code de réponse
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        success = true;
                    }
                } else {
                    // Afficher un message d'erreur si les données n'ont pas été envoyées
                    Log.e("SendAllVaccinsToServerTask", "Aucun vaccin à envoyer.");
                }
            } catch (IOException | JSONException e) {
                // Afficher un message d'erreur si les données n'ont pas été envoyées
                Log.e("SendAllVaccinsToServerTask", "Erreur lors de l'envoi des données des vaccins : " + e.getMessage());
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                // Afficher un message de succès si les données ont été envoyées avec succès
                Log.d("SendAllVaccinsToServerTask", "Les données des vaccins ont été envoyées avec succès.");
            } else {
                // Afficher un message d'erreur si les données n'ont pas été envoyées
                Log.e("SendAllVaccinsToServerTask", "Erreur lors de l'envoi des données des vaccins.");
            }
        }
    }





}












