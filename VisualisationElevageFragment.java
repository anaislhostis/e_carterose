package com.example.e_carterose;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.example.e_carterose.databinding.FragmentVisualisationElevageBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VisualisationElevageFragment extends Fragment {
    private DatabaseAccess db;
    private List<Animal> allAnimals; // Variable pour stocker la liste complète des animaux
    private LinearLayout layout;
    private FragmentVisualisationElevageBinding binding;
    private TextView textViewAttestation;

    public static VisualisationElevageFragment newInstance() {
        return new VisualisationElevageFragment();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_visualisation_elevage, container, false); // Initialiser la vue
        TextView textViewNomElevage = rootView.findViewById(R.id.text_view_nom_elevage); // Récupérer le textView pour afficher le nom de l'élevage
        textViewAttestation = rootView.findViewById(R.id.text_view_attestation); // Récupérer le textView pour afficher l'attestation sanitaire
        layout = rootView.findViewById(R.id.animals_container); // Récupérer le layout pour afficher les animaux
        binding = FragmentVisualisationElevageBinding.inflate(inflater, container, false); // Initialiser la liaison de données

        // Initialiser la barre de recherche
        SearchView searchView = rootView.findViewById(R.id.searchView);
        binding.searchView.setQueryHint("Rechercher un numéro de travail...");

        db = new DatabaseAccess(requireContext()); // Initialiser la base de données



        // Récupérer le numéro de l'élevage depuis la variable statique
        String numElevage = MainActivity.numeroElevage;

        // Récupérer les informations des animaux actifs sur l'élevage depuis la base de données
        Elevage elevage = db.getElevageByNumero(numElevage);

        // Vérifier si l'élevage a été trouvé dans la base de données
        if (elevage != null) {
            // Utiliser le nom de l'élevage récupéré pour définir le texte du textViewNomElevage
            textViewNomElevage.setText(elevage.getNom_elevage());
        } else {
            // Gérer le cas où l'élevage n'a pas été trouvé dans la base de données
            textViewNomElevage.setText("Elevage n°" + numElevage);
        }

        // Récupérer tous les animaux de l'élevage
        allAnimals = db.getActiveAnimalsByElevage(numElevage);


        // Mettre en place un écouteur pour le texte saisi dans la barre de recherche
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Ne rien faire lors de la soumission du texte
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtrer les animaux avec le texte saisi dans la barre de recherche
                filterAnimals(newText);
                return true;
            }

        });

        // Récupérer le cod_asda correspondant au num_elevage
        String codAsda = db.getCodAsdaByNumElevage(numElevage);

        // Ajouter des logs pour vérifier la valeur de codAsda
        Log.d("VisualisationElevageFragment", "Valeur de codAsda : " + codAsda);

        // Afficher tous les animaux initialement
        updateAnimalViews(allAnimals);

        // Vérifier si le cod_asda est récupéré avec succès
        if (codAsda != null) {
            // Définir la couleur et le text du TextView en fonction du cod_asda
            String attestationText;

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

        return rootView;
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
}
