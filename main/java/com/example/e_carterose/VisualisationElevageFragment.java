package com.example.e_carterose;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.example.e_carterose.databinding.FragmentVisualisationElevageBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class VisualisationElevageFragment extends Fragment {
    private DatabaseAccess db;
    private List<Animal> allAnimals; // Variable pour stocker la liste complète des animaux
    private LinearLayout layout;
    private Context context;
    private FragmentVisualisationElevageBinding binding;
    private TextView textViewNomElevage;

    private String attestationText;

    private TextView textViewAttestation;
    String num_elevage = MainActivity.numeroElevage;

    public static VisualisationElevageFragment newInstance() {
        return new VisualisationElevageFragment();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            db = new DatabaseAccess(context);
        } catch (NullPointerException e) {
            // Handle the exception gracefully, log the error, or perform any necessary action
            Log.e("DatabaseAccess", "Error initializing database access: " + e.getMessage());
        }
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_visualisation_elevage, container, false); // Initialiser la vue
        textViewNomElevage = rootView.findViewById(R.id.text_view_nom_elevage); // Récupérer le textView pour afficher le nom de l'élevage
        textViewAttestation = rootView.findViewById(R.id.text_view_attestation); // Récupérer le textView pour afficher l'attestation sanitaire
        layout = rootView.findViewById(R.id.animals_container); // Récupérer le layout pour afficher les animaux
        binding = FragmentVisualisationElevageBinding.inflate(inflater, container, false); // Initialiser la liaison de données

        context = getActivity();
        allAnimals = new ArrayList<>();



        // Initialiser la barre de recherche
        SearchView searchView = rootView.findViewById(R.id.searchView);
        binding.searchView.setQueryHint("Rechercher un numéro de travail...");
        db = new DatabaseAccess(context); // Initialiser la base de données


        // Récupérer le numéro de l'élevage depuis la variable statique
        num_elevage= MainActivity.numeroElevage;


        if (isNetworkAvailable()) {
            Log.e("NETWORK", "Connexion Internet disponible");

            //fetchElevageData(num_elevage);
            //Log.d("onCreateView", "Informations de l'élevage récupérées");
            //fetchAnimauxData(num_elevage);
            //Log.d("onCreateView", "Informations des animaux récupérées");


            //Code à enlever quand l'API sera ok :
            allAnimals = db.getAnimalsByElevage(num_elevage);
            String codAsda = db.getCodAsdaByNumElevage(num_elevage);
            textViewNomElevage.setText(db.getNomElevageByNumero(num_elevage));
            setAttestationColor(codAsda);

            updateAnimalViews(allAnimals);
            

            Toast.makeText(context, "Les données sont chargées depuis le server.", Toast.LENGTH_SHORT).show();


        } else {
            Log.e("NETWORK", "Connexion Internet non disponible");
            allAnimals = db.getAnimalsByElevage(num_elevage);
            String codAsda = db.getCodAsdaByNumElevage(num_elevage);
            textViewNomElevage.setText(db.getNomElevageByNumero(num_elevage));
            setAttestationColor(codAsda);

            updateAnimalViews(allAnimals);
            Toast.makeText(context, "Les données sont chargées localement.", Toast.LENGTH_SHORT).show();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterAnimals(newText);
                return true;
            }
        });

        return rootView;
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



    // Va chercher les informations des animaux présentes dans la base de données distante
    private void fetchAnimauxData(String num_elevage) {
        String animalUrl = "http://10.10.64.12:5000/animaux_remote/" + num_elevage;
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
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Elevage elevage = new Elevage();
                    elevage.setCod_asda(jsonObject.getString("cod_asda"));
                    elevage.setMdp(jsonObject.getString("mdp"));
                    // Vérifiez si la clé "nom" existe avant de l'extraire
                    if (jsonObject.has("nom") && !jsonObject.isNull("nom")) {
                        String nomElevage = jsonObject.getString("nom");
                        elevage.setNom_elevage(nomElevage);
                        textViewNomElevage.setText(nomElevage);
                    } else {
                        // Si la clé "nom" est absente ou nulle, définir "Non défini" comme valeur par défaut
                        String nomElevage = "Non défini";
                        elevage.setNom_elevage(nomElevage);
                        textViewNomElevage.setText(nomElevage);
                    }

                    elevage.setNum_elevage(jsonObject.getString("num_elevage"));

                    // Log des données extraites pour vérification
                    Log.d("FetchElevageDataTask", "Nom elevage: " + elevage.getNom_elevage());
                    Log.d("FetchElevageDataTask", "Code ASDA: " + elevage.getCod_asda());

                    // Mise à jour des TextViews avec les données extraites
                    textViewNomElevage.setText(elevage.getNom_elevage());
                    setAttestationColor(elevage.getCod_asda());
                } catch (JSONException e) {
                    Log.e("FetchElevageDataTask", "Erreur lors du traitement du résultat : " + e.getMessage());
                }
            }
        }
    }


    public void fetchElevageData(String num_elevage) {
        String elevageUrl = "http://10.10.64.12:5000/elevage_remote/" + num_elevage;
        new FetchElevageDataTask().execute(elevageUrl);
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
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Animal animal = new Animal();
                        animal.setNumNat(jsonObject.getString("num_nat"));
                        animal.setNumTra(jsonObject.getString("num_tra"));
                        animal.setCodPays(jsonObject.getString("cod_pays"));
                        // Vérifie si la clé "nom" existe et si elle n'est pas nulle
                        if (jsonObject.has("nom") && !jsonObject.isNull("nom")) {
                            animal.setNom(jsonObject.getString("nom"));
                        } else {
                            // Si la clé "nom" n'existe pas ou si sa valeur est null, définissez le nom sur "Non défini"
                            animal.setNom("Non défini");
                        }
                        animal.setSexe(jsonObject.getString("sexe"));
                        animal.setDateNaiss(jsonObject.getString("date_naiss"));
                        animal.setCodPaysNaiss(jsonObject.getString("cod_pays_naiss"));
                        animal.setNumExpNaiss(jsonObject.getString("num_exp_naiss"));
                        animal.setCodPaysPere(jsonObject.getString("cod_pays_pere"));
                        animal.setNumNatPere(jsonObject.getString("num_nat_pere"));
                        animal.setCodRacePere(jsonObject.getString("cod_race_pere"));
                        animal.setCodPaysMere(jsonObject.getString("cod_pays_mere"));
                        animal.setNumNatMere(jsonObject.getString("num_nat_mere"));
                        animal.setCodRaceMere(jsonObject.getString("cod_race_mere"));
                        animal.setNumElevage(jsonObject.getString("num_elevage"));
                        animal.setRace(jsonObject.getString("cod_race"));

                        allAnimals.add(animal);
                    }

                    // Mettre à jour les vues
                    updateAnimalViews(allAnimals);


                } catch (JSONException e) {
                    Log.e("FetchAnimalDataTask", "Erreur lors du traitement du résultat : " + e.getMessage());
                }
            }
        }


    }



    private void filterAnimals(String query) {
        List<Animal> filteredList = new ArrayList<>();
        for (Animal animal : allAnimals) {
            if (animal.getNumTra().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(animal);
            }
        }
        // Mettre à jour l'affichage avec la liste filtrée
        updateAnimalViews(filteredList);
    }
    public void updateAnimalViews(List<Animal> animals) {
        layout.removeAllViews(); // Supprimer tous les éléments actuellement affichés
        for (int i = 0; i < animals.size(); i++) {
            Animal animal = animals.get(i);
            // Créer et ajouter les vues pour chaque animal
            View animalView = LayoutInflater.from(requireContext()).inflate(R.layout.animal_item, layout, false);
            TextView textViewNumTra = animalView.findViewById(R.id.text_view_num_tra);
            TextView textViewNom = animalView.findViewById(R.id.text_view_nom);
            TextView textViewDateNaiss = animalView.findViewById(R.id.text_view_date_naiss);
            TextView textViewSexe = animalView.findViewById(R.id.text_view_sexe);
            textViewNumTra.setText("Numéro de travail: " + animal.getNumTra());
            String nom = animal.getNom();
            if (nom != null) {
                textViewNom.setText("Nom: " + animal.getNom());
            } else {
                Spanned spannedText = HtmlCompat.fromHtml("Nom: " + "<i>" + "non attribué" + "</i>", HtmlCompat.FROM_HTML_MODE_LEGACY);
                textViewNom.setText(spannedText);
            }
            String dateNaissance = animal.getDateNaiss();
            if (dateNaissance != null && dateNaissance.length() >= 10) {
                textViewDateNaiss.setText("Date de naissance: " + dateNaissance.substring(0, 10));
            }
            if (animal.getSexe().equals("1")) {
                textViewSexe.setText("Sexe: Mâle");
            } else if (animal.getSexe().equals("2")) {
                textViewSexe.setText("Sexe: Femelle");
            }
            // Ajouter des logs pour visualiser les valeurs
            Log.d("VisualisationElevageFragment", "Numéro de travail: " + animal.getNumTra());
            Log.d("VisualisationElevageFragment", "Nom: " + animal.getNom());
            Log.d("VisualisationElevageFragment", "Date de naissance: " + dateNaissance);
            Log.d("VisualisationElevageFragment", "Sexe: " + animal.getSexe());
            final int index = i;
            animalView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animal selectedAnimal = animals.get(index);
                    // Passer les données de l'animal au fragment de détails
                    DetailsAnimalFragment detailsFragment = new DetailsAnimalFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("animal", (Serializable) selectedAnimal);
                    detailsFragment.setArguments(bundle);
                    // Remplacer le contenu actuel par le fragment de détails
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, detailsFragment)
                            .addToBackStack(null) // Pour ajouter le fragment à la pile de retour
                            .commit();
                }
            });
            layout.addView(animalView);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (db != null) {
            db.close();
            db = null;
        }
    }

    private void setAttestationColor(String codAsda) {
        // Mise à jour de textViewAttestation en fonction du code ASDA

        switch (codAsda) {
            case "1":
                textViewAttestation.setBackgroundResource(R.color.vert);
                attestationText = "ATTESTATION SANITAIRE : Circuit indemne";
                break;
            case "2":
                textViewAttestation.setBackgroundResource(R.color.jaune);
                attestationText = "ATTESTATION SANITAIRE : Circuit non indemne";
                break;
            case "3":
                textViewAttestation.setBackgroundResource(R.color.orange);
                attestationText = "ATTESTATION SANITAIRE : Circuit à risque contrôlé";
                break;
            case "4":
                textViewAttestation.setBackgroundResource(R.color.rouge);
                attestationText = "ATTESTATION SANITAIRE : Circuit infecté";
                break;
            default:
                textViewAttestation.setBackgroundResource(R.color.black);
                attestationText = "ATTESTATION SANITAIRE : Non communiquée";
        }
        MainActivity.asda = attestationText;
        textViewAttestation.setText(attestationText);
    }






    private boolean isAnimalValid(Animal animal) {
                // Vérifiez ici si l'animal est valide selon les critères de votre application
                // Par exemple, vous pouvez vérifier si tous les champs requis sont présents et non vides
                // Vous pouvez également effectuer d'autres validations spécifiques à votre application

                // Exemple de vérification simple : vérifier si le numéro national de l'animal n'est pas vide
                return animal != null && animal.getNumNat() != null && !animal.getNumNat().isEmpty();
    }







    private void sendAnimalsToUpdateOnServer(List<Animal> animalsToUpdate) {
        try {
                // Convertir la liste des animaux à mettre à jour en JSON
                Gson gson = new Gson();
                String animalsJson = gson.toJson(animalsToUpdate);

                // Créer une connexion HTTP et configurer la requête
                URL url = new URL("http://10.10.64.12:5000/animaux_to_update_on_server");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Écrire les données JSON dans le corps de la requête
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(animalsJson);
                writer.flush();

                // Vérifier le code de réponse
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // L'envoi des données a réussi
                    // Vous pouvez lire la réponse du serveur si nécessaire
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Afficher la réponse du serveur (si nécessaire)
                    Log.d("SendAnimalsToUpdate", "Réponse du serveur : " + response.toString());
                } else {
                    // L'envoi des données a échoué
                    Log.e("SendAnimalsToUpdate", "Échec de l'envoi des données au serveur. Code de réponse : " + responseCode);
                }
            } catch (IOException e) {
                // Gérer les erreurs d'entrée/sortie
                Log.e("SendAnimalsToUpdate", "Erreur lors de l'envoi des données au serveur : " + e.getMessage());
            }
        }


        // Méthode pour rechercher un animal correspondant dans une liste d'animaux
        private Animal findCorrespondingAnimal(List<Animal> animals, Animal targetAnimal) {
            for (Animal animal : animals) {
                if (animal.getNumNat().equals(targetAnimal.getNumNat())) {
                    return animal;
                }
            }
            return null;
        }




}






