package com.example.e_carterose;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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


    // Méthode pour récupérer tous les animaux de la BDD
    @SuppressLint("Range")
    public List<Animal> getAllanimalsFromLocalDatabase() {
        List<Animal> animals = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // Ouvrir la base de données en mode lecture
            db = openHelper.getReadableDatabase();

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

    // Récupérer la liste des animaux de l'élevage en focntion de leur statut : actif, mort, en transfert...
    @SuppressLint("Range")
    public List<Animal> getAnimalsByElevageAndActif(String numElevage, int actif) {
        List<Animal> animals = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = openHelper.getReadableDatabase();
            String query = "SELECT animal.*, elevage.* FROM animal INNER JOIN elevage ON animal.num_elevage = elevage.num_elevage WHERE elevage.num_elevage = ? AND animal.actif = ?";
            cursor = db.rawQuery(query, new String[]{numElevage, String.valueOf(actif)});

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




    // Récupération de tous les animaux de l'élevage (morts/notifiés morts compris/en transfert compris)
    @SuppressLint("Range")
    public List<Animal> getAnimalsByElevage(String numElevage) {
        List<Animal> animals = new ArrayList<>();
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

                animalObj.setActif(cursor.getString(cursor.getColumnIndex("actif")));
                animalObj.setDateModif(cursor.getString(cursor.getColumnIndex("date_modif")));


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

    public List<Soins> getCareByNumNat(String numNat) {
        List<Soins> careList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = openHelper.getReadableDatabase();

            // Exécutez la requête SQL pour récupérer les vaccins en fonction du numéro national
            cursor = db.rawQuery("SELECT * FROM soins WHERE num_nat = ?", new String[]{numNat});

            // Parcourez le curseur pour récupérer les enregistrements de vaccins
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Créez un nouvel objet Soin à partir des données du curseur
                    Soins soin = new Soins();
                    soin.setNomSoin(cursor.getString(cursor.getColumnIndex("nom_soin")));
                    soin.setDose(cursor.getString(cursor.getColumnIndex("dose")));
                    soin.setDateSoin(cursor.getString(cursor.getColumnIndex("date_soin")));

                    // Ajoutez le vaccin à la liste
                    careList.add(soin);
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
        // Retournez la liste des soins
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

    public boolean isMdpCreated(String numElevage) {
        Cursor cursor = null;
        boolean mdpCreated = false;
        try {
            open();
            String query = "SELECT mdp FROM elevage WHERE num_elevage = ?";
            cursor = db.rawQuery(query, new String[]{numElevage});
            if (cursor != null && cursor.moveToFirst()) {
                String motDePasse = cursor.getString(cursor.getColumnIndex("mdp"));
                // Vérifier si le mot de passe est null
                mdpCreated = (motDePasse != null);
            }
        } catch (SQLException e) {
            Log.e("DatabaseAccess", "Error checking if password is created", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            close();
        }

        return mdpCreated;
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
                values.put("date_modif", getCurrentDateTime()); // Modifier la date de modification

                result = db.update("elevage", values, "num_elevage = ?", new String[]{numeroElevage});
            } else {
                // Si l'enregistrement n'existe pas, insérer un nouvel enregistrement
                ContentValues values = new ContentValues();
                values.put("num_elevage", numeroElevage);
                values.put("nom", nomElevage);
                values.put("mdp", motDePasse);
                values.put("date_modif", getCurrentDateTime()); // Modifier la date de modification

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


    // Inserer un animal dans la table animal
    public long insertAnimal(String numNat, String numTra, String codePays, String nom, String sexe, String dateNais, String codePaysNais, String numExpNais,  String codePaysPere, String numNatPere, String codeRacePere, String codePaysMere, String numNatMere, String codeRaceMere, String numElevage, String codeRace, String actif) {
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
        values.put("date_modif", getCurrentDateTime());
        values.put("actif", actif);

        // Insérer la ligne dans la table
        long newRowId = db.insert("animal", null, values);
        close(); // Fermer la connexion à la base de données
        return newRowId;
    }


    // Modifier le statut de l'animal
    public void updateStatus(String numElevage, String numTra, int statut) {
        try {
            open(); // Ouvrir la connexion vers la base de données

            // Convertir le statut en String
            String statutString = String.valueOf(statut);
            String formattedDate =getCurrentDateTime();

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

}