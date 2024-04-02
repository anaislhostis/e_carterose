package com.example.e_carterose;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e_carterose.databinding.FragmentSelectionActionEleveurBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SelectionActionEleveurFragment extends Fragment {
    private DatabaseAccess db;
    private Context context;
    private List<Animal> allAnimals; // Variable pour stocker la liste complète des animaux

    private FragmentSelectionActionEleveurBinding binding;

    public static SelectionActionEleveurFragment newInstance() {
        SelectionActionEleveurFragment fragment = new SelectionActionEleveurFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectionActionEleveurBinding.inflate(inflater, container, false);

        context = getActivity();
        db = new DatabaseAccess(context); // Initialiser la base de données

        if (isNetworkAvailable()) {
            Log.e("NETWORK", "Connexion Internet disponible");

            fetchAnimauxData();
            fetchElevageData();

            db.sendAllAnimauxToServer();
            db.sendAllElevagesToServer();
            db.sendAllSoinsToServer();
            db.sendAllVaccinsToServer();
            db.getDistinctLocalElevageNumbers();

            db.synchronizeElevagesData(); // Synchroniser les données de la table elevage
            db.synchronizeAnimauxData(); // Synchroniser les données de la table animal
            db.synchronizeSoinsData(); // Synchroniser les données de la table soins

            Log.e("NETWORK", "Données synchronisées");

        } else {
            Log.e("NETWORK", "Connexion Internet non disponible");
            Toast.makeText(context, "Connexion Internet non disponible", Toast.LENGTH_SHORT).show();
        }



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.ButtonVisu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une instance de VisualisationElevageFragment
                VisualisationElevageFragment visualisationElevageFragment = VisualisationElevageFragment.newInstance();

                // Remplacer le fragment actuel par le fragment VisualisationElevageFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, visualisationElevageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        binding.ButtonDeclaNais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une instance de FormulaireNaissanceFragment
                FormulaireNaissanceFragment formulaireNaissanceFragment = FormulaireNaissanceFragment.newInstance();

                // Remplacer le fragment actuel par le fragment FormulaireNaissanceFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, formulaireNaissanceFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        binding.ButtonDeclaDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une instance de FormulaireMortFragment
                FormulaireMortFragment formulaireMortFragment = FormulaireMortFragment.newInstance();

                // Remplacer le fragment actuel par le fragment FormulaireMortFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, formulaireMortFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Bouton transférer un animal
        binding.ButtonTransfert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une instance de SelectionEndroitTransfertFragment
                SelectionEndroitTransfertFragment selectionEndroitTransfertFragment = SelectionEndroitTransfertFragment.newInstance();

                // Remplacer le fragment actuel par le fragment SelectionEndroitTransfertFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, selectionEndroitTransfertFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }


    // Vérifie si une connexion Internet est disponible
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void fetchElevageData() {
        String elevageUrl = "http://10.10.64.12:5000/elevages_remote/";
        new FetchElevageDataTask().execute(elevageUrl);
    }

    private void fetchAnimauxData() {
        String animalUrl = "http://10.10.64.12:5000/animaux_remote/";
        new FetchAnimauxDataTask().execute(animalUrl);
    }

    //Va chercher les informations de l'élevage présentes dans la base de données distante
    private class FetchElevageDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                reader.close();

                return stringBuilder.toString();
            } catch (Exception e) {
                Log.e("Error", "Exception: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                List<Elevage> allElevages = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray elevageArray = jsonObject.getJSONArray("elevage_data");

                for (int i = 0; i < elevageArray.length(); i++) {
                    JSONObject elevageObject = elevageArray.getJSONObject(i);
                    Elevage elevage = new Elevage();

                    elevage.setNum_elevage(elevageObject.getString("num_elevage"));
                    elevage.setCod_asda(elevageObject.getString("cod_asda"));
                    elevage.setMdp(elevageObject.getString("mdp"));

                    // Vérifier si la clé "nom" existe avant de l'extraire
                    if (elevageObject.has("nom") && !elevageObject.isNull("nom")) {
                        String nomElevage = elevageObject.getString("nom");
                        elevage.setNom_elevage(nomElevage);

                    } else {
                        // Si la clé "nom" est absente ou nulle, définir "Non défini" comme valeur par défaut
                        String nomElevage = "Non défini";
                        elevage.setNom_elevage(nomElevage);
                    }

                    allElevages.add(elevage);

                    // Log des données extraites pour vérification
                    Log.d("FetchElevageDataTask", "Nom elevage: " + elevage.getNom_elevage());
                    Log.d("FetchElevageDataTask", "Code ASDA: " + elevage.getCod_asda());
                }

            } catch (JSONException e) {
                Log.e("FetchElevageDataTask", "Erreur lors du traitement du résultat : " + e.getMessage());
            }
        }
    }



    // Tâche asynchrone pour récupérer les données des animaux depuis la base de données distante
    private class FetchAnimauxDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                reader.close();

                return stringBuilder.toString();
            } catch (Exception e) {
                Log.e("Error", "Exception: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("FetchAnimalDataTask", "Résultat: " + result);
            try {
                List<Animal> allAnimals = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray animauxArray = jsonObject.getJSONArray("animaux_data");

                for (int i = 0; i < animauxArray.length(); i++) {
                    JSONObject animalObject = animauxArray.getJSONObject(i);
                    Animal animal = new Animal();
                        animal.setNumNat(animalObject.getString("num_nat"));
                        animal.setNumTra(animalObject.getString("num_tra"));
                        animal.setCodPays(animalObject.getString("cod_pays"));
                        // Vérifie si la clé "nom" existe et si elle n'est pas nulle
                        if (jsonObject.has("nom") && !jsonObject.isNull("nom")) {
                            animal.setNom(animalObject.getString("nom"));
                        } else {
                            // Si la clé "nom" n'existe pas ou si sa valeur est null, définissez le nom sur "Non défini"
                            animal.setNom("Non défini");
                        }
                        animal.setSexe(animalObject.getString("sexe"));
                        animal.setDateNaiss(animalObject.getString("date_naiss"));
                        animal.setCodPaysNaiss(animalObject.getString("cod_pays_naiss"));
                        animal.setNumExpNaiss(animalObject.getString("num_exp_naiss"));
                        animal.setCodPaysPere(animalObject.getString("cod_pays_pere"));
                        animal.setNumNatPere(animalObject.getString("num_nat_pere"));
                        animal.setCodRacePere(animalObject.getString("cod_race_pere"));
                        animal.setCodPaysMere(animalObject.getString("cod_pays_mere"));
                        animal.setNumNatMere(animalObject.getString("num_nat_mere"));
                        animal.setCodRaceMere(animalObject.getString("cod_race_mere"));
                        animal.setNumElevage(animalObject.getString("num_elevage"));
                        animal.setRace(animalObject.getString("cod_race"));
                        animal.setDateModif(animalObject.getString("date_modif"));
                        animal.setActif(animalObject.getString("actif"));

                        allAnimals.add(animal);
                    }

                } catch (JSONException e) {
                    Log.e("FetchAnimalDataTask", "Erreur lors du traitement du résultat : " + e.getMessage());
                }
            }
        }


}
