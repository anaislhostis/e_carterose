package com.example.e_carterose;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;

////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////Création, ouverture, fermeture de la BDD//////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
public class DatabaseAccess {
    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;

    public DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        };
        return instance;
    };

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

    //Definition du nom des tables et colonnes :
    private static final String ANIMAL = "animal";
    private static final String NUM_ELEVAGE = "num_elevage";


    @SuppressLint("Range")
    public List<Animal> getAnimalsByElevage(String numElevage) {
        List<Animal> animals = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = openHelper.getReadableDatabase();
            String query = "SELECT animal.*, elevage.* FROM animal INNER JOIN elevage ON substr(animal.num_nat, 1, 6) = elevage.num_elevage WHERE elevage.num_elevage = ?";
            cursor = db.rawQuery(query, new String[]{numElevage});

            open(); // Ouvrir la connexion vers la base de données


            while (cursor.moveToNext()) {
                Animal animalObj = new Animal();
                animalObj.setNumNat(cursor.getString(cursor.getColumnIndex("num_nat")));
                animalObj.setNumTra(cursor.getString(cursor.getColumnIndex("num_tra")));
                animalObj.setCodPays(cursor.getString(cursor.getColumnIndex("cod_pays")));
                animalObj.setNom(cursor.getString(cursor.getColumnIndex("nom")));
                animalObj.setSexe(cursor.getString(cursor.getColumnIndex("sexe")));
                animalObj.setDateNaiss(cursor.getString(cursor.getColumnIndex("date_naiss")));
                animalObj.setCodPaysNaiss(cursor.getString(cursor.getColumnIndex("cod_pays_naiss")));
                animalObj.setNumExpNaiss(cursor.getString(cursor.getColumnIndex("num_exp_naiss")));
                animalObj.setCodPaysPere(cursor.getString(cursor.getColumnIndex("cod_pays_pere")));
                animalObj.setNumNatPere(cursor.getString(cursor.getColumnIndex("num_nat_pere")));
                animalObj.setCodRacePere(cursor.getString(cursor.getColumnIndex("cod_race_pere")));
                animalObj.setCodPaysMere(cursor.getString(cursor.getColumnIndex("cod_pays_mere")));
                animalObj.setNumNatMere(cursor.getString(cursor.getColumnIndex("num_nat_mere")));
                animalObj.setCodRaceMere(cursor.getString(cursor.getColumnIndex("cod_race_mere")));
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


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////Insertion données dans la BDD/////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////Suppression données de la table animal///////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteAnimal(String numNat) {
        open(); // Ouvrir la connexion vers la base de données
        db.delete("animal", "num_nat = ?", new String[]{numNat});
        close(); // Fermer la connexion à la base de données
    }

}
