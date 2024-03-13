package com.example.e_carterose;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////Création, ouverture, fermeture de la BDD//////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
public class DatabaseAccess extends DatabaseOpenHelper {
    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;

    public DatabaseAccess(Context context) {
        super(context);
        this.openHelper = new DatabaseOpenHelper(context);
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
    public long createElevageProfile(String numeroElevage, String nomElevage, String motDePasse) {
        SQLiteDatabase db = null;
        long result = -1;

        try {
            db = openHelper.getWritableDatabase();

            // Vérifier d'abord si l'enregistrement avec num_elevage existe déjà
            Cursor cursor = db.rawQuery("SELECT * FROM elevage WHERE num_elevage = ?", new String[]{numeroElevage});

            // Si l'enregistrement existe, mettre à jour les colonnes nom et motDePasse
            if (cursor != null && cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put("nom", nomElevage);
                values.put("mdp", motDePasse);

                result = db.update("elevage", values, "num_elevage = ?", new String[]{numeroElevage});
            } else {
                // Si l'enregistrement n'existe pas, insérer un nouvel enregistrement
                ContentValues values = new ContentValues();
                values.put("num_elevage", numeroElevage);
                values.put("nom", nomElevage);
                values.put("mdp", motDePasse);

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
            String query = "SELECT nom FROM elevage WHERE num_elevage = ?";
            cursor = db.rawQuery(query, new String[]{numElevage});

            // Log pour vérifier les informations sur le curseur
            Log.d("DatabaseAccess", "Cursor count: " + cursor.getCount());
            Log.d("DatabaseAccess", "Cursor column count: " + cursor.getColumnCount());
            Log.d("DatabaseAccess", "Cursor column names: " + Arrays.toString(cursor.getColumnNames()));

            if (cursor.moveToFirst()) nomElevage = cursor.getString(cursor.getColumnIndex("nom"));
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

    // Méthode pour récupérer les informationsde l'animal par le numTra
    @SuppressLint("Range")
    public Animal getAnimalByNumTra(String numTra) {
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

    public long insertAnimal(String numNat, String numTra, String codePays, String nom, String sexe, String dateNais, String codePaysNais, String numExpNais,  String codePaysPere, String numNatPere, String codeRacePere, String codePaysMere, String numNatMere, String codeRaceMere, String numElevage, String codeRace) {
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

        // Insérer la ligne dans la table
        long newRowId = db.insert("animal", null, values);
        close(); // Fermer la connexion à la base de données
        return newRowId;
    }

    
    //Suppression données de la table animal
    public void deleteAnimal(String numNat) {
        open(); // Ouvrir la connexion vers la base de données
        db.delete("animal", "num_nat = ?", new String[]{numNat});
        close(); // Fermer la connexion à la base de données
    }

}



