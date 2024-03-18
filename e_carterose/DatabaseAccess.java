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

    // Vérifie si un animal existe déjà dans la base de données locale
    public boolean animalExists(String numNat) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM animal WHERE num_nat = ?", new String[]{numNat});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }



    List<Animal> activeAnimals = new ArrayList<>();
    @SuppressLint("Range")
    public List<Animal> getActiveAnimalsByElevage(String numElevage) {
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




    @SuppressLint("Range")
    public List<Animal> getAllAnimalsFromLocalDatabase() {
        List<Animal> allAnimals = new ArrayList<>();

        // Assurez-vous d'adapter cette partie en fonction de votre implémentation réelle de l'accès à la base de données locale

        // 1. Ouvrez la base de données
        open();
        // 2. Obtenez une instance de la base de données en mode lecture
        SQLiteDatabase db = getReadableDatabase();

        // 3. Définissez la requête SQL pour récupérer tous les animaux
        String query = "SELECT * FROM Animal";

        // 4. Exécutez la requête SQL
        Cursor cursor = db.rawQuery(query, null);

        // 5. Parcourez le curseur et ajoutez chaque animal à la liste
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Animal animal = new Animal();
                // Remplacez les méthodes getXXXX par les méthodes réelles pour extraire les valeurs du curseur
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

                allAnimals.add(animal);
            } while (cursor.moveToNext());

            // 6. Fermez le curseur
            cursor.close();
        }

        // 7. Fermez la base de données
        db.close();

        return allAnimals;
    }



    public void sendAllAnimauxToServer() {
        new SendDataAsyncTask().execute();
    }

    private class SendDataAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean success = false;
            try {
                // Récupérer tous les animaux de la base de données locale
                List<Animal> allAnimals = getAllAnimalsFromLocalDatabase();

                if (allAnimals != null && !allAnimals.isEmpty()) {
                    // Convertir la liste d'animaux en tableau JSON
                    JSONArray jsonArray = new JSONArray();
                    for (Animal animal : allAnimals) {
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
                        Log.e("DatabaseAccess", "Valeur de la date de modification : " + animal.getDateModif());
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


    public void getDistinctLocalElevageNumbers() {
        new GetDistinctLocalElevageNumbersTask().execute();
    }

    // Renvoie les numéros d'élevage distincts vers le serveur
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


    public void synchronizeElevagesData() {
        // Créer une instance de la tâche de synchronisation
        SynchronizeElevageTask synchronizeTask = new SynchronizeElevageTask(context, openHelper);

        // Exécuter la tâche de synchronisation
        synchronizeTask.execute();
    }


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



                // Récupérer les données à mettre à jour et à ajouter
                JSONArray toUpdateArray = response.getJSONArray("to_update");
                JSONArray toAddArray = response.getJSONArray("to_add");
                JSONArray elevagesArray = response.getJSONArray("elevage_data");

                // Ajouter les élevages à la base de données locale (to_add)
                for (int i = 0; i < toAddArray.length(); i++) {
                    try {
                        JSONObject elevageObject = toAddArray.getJSONObject(i);
                        String numElevage = elevageObject.getString("num_elevage");
                        String nom = elevageObject.getString("nom");
                        String mdp = elevageObject.getString("mdp");
                        String codAsda = elevageObject.getString("cod_asda");

                        // Insérer l'élevage dans la base de données locale
                        insertElevage(numElevage, nom, mdp, codAsda);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // Mettre à jour les élevages dans la base de données locale (to_update)
                for (int i = 0; i < toUpdateArray.length(); i++) {
                    try {
                        JSONObject elevageObject = toUpdateArray.getJSONObject(i);
                        String numElevage = elevageObject.getString("num_elevage");
                        String nom = elevageObject.getString("nom");
                        String mdp = elevageObject.getString("mdp");
                        String codAsda = elevageObject.getString("cod_asda");

                        // Mettre à jour l'élevage dans la base de données locale
                        updateElevage(numElevage, nom, mdp, codAsda);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException | JSONException e) {
                Log.e("SynchronizeTask", "Erreur lors de la synchronisation : " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Afficher un message de synchronisation réussie
            Toast.makeText(context, "Données locaes synchronisées avec celles du server.", Toast.LENGTH_SHORT).show();
        }

        private void insertElevage(String numElevage, String nom, String mdp, String codAsda) {
            SQLiteDatabase db = openHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("num_elevage", numElevage);
            values.put("nom", nom);
            values.put("mdp", mdp);
            values.put("cod_asda", codAsda);
            long newRowId = db.insert("elevage", null, values);
        }

        private void updateElevage(String numElevage, String nom, String mdp, String codAsda) {
            SQLiteDatabase db = openHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nom", nom);
            values.put("mdp", mdp);
            values.put("cod_asda", codAsda);
            String selection = "num_elevage = ?";
            String[] selectionArgs = { numElevage };
            int count = db.update("elevage", values, selection, selectionArgs);
        }

    }


    public void synchronizeAnimauxData() {
        // Créer une instance de la tâche de synchronisation
        SynchronizeAnimauxTask synchronizeTask = new SynchronizeAnimauxTask(context, openHelper);

        // Exécuter la tâche de synchronisation
        synchronizeTask.execute();
    }

    private class SynchronizeAnimauxTask extends AsyncTask<Void, Void, Void> {
        private Context mContext;
        private DatabaseOpenHelper mOpenHelper;
        private JSONArray mToUpdateArray;
        private JSONArray mToAddArray;

        public SynchronizeAnimauxTask(Context context, DatabaseOpenHelper openHelper) {
            mContext = context;
            mOpenHelper = openHelper;
        }

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

                // Récupérer les données à mettre à jour et à ajouter
                JSONArray toUpdateArray = response.getJSONArray("to_update");
                JSONArray toAddArray = response.getJSONArray("to_add");
                JSONArray animauxArray = response.getJSONArray("animal_data");

                // Ajouter les animaux à la base de données locale (to_add)
                for (int i = 0; i < toAddArray.length(); i++) {
                    try {
                        JSONObject animalObject = toAddArray.getJSONObject(i);
                        // Extraire les données de l'animal depuis l'objet JSON
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
                        // Insérer l'animal dans la base de données locale
                        insertAnimal(numNat, numTra, codPays, nom, sexe, dateNaiss, codPaysNaiss, numExpNaiss,
                                codPaysPere, numNatPere, codRacePere, codPaysMere, numNatMere, codRaceMere,
                                numElevage, codRace);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // Mettre à jour les animaux dans la base de données locale (to_update)
                // Votre code pour la mise à jour des animaux ici...

            } catch (IOException | JSONException e) {
                Log.e("SynchronizeTask", "Erreur lors de la synchronisation : " + e.getMessage());
            }

            return null;
        }
    }


}









