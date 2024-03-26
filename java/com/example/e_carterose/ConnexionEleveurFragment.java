package com.example.e_carterose;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.e_carterose.databinding.FragmentConnexionEleveurBinding;

public class ConnexionEleveurFragment extends Fragment {

    private FragmentConnexionEleveurBinding binding;
    private EditText editTextNumeroElevage;
    private EditText editTextMotDePasse;

    private DatabaseAccess databaseAccess;

    public static ConnexionEleveurFragment newInstance() {
        return new ConnexionEleveurFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseAccess = DatabaseAccess.getInstance(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConnexionEleveurBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextNumeroElevage = view.findViewById(R.id.editTextNumeroElevage);
        editTextMotDePasse = view.findViewById(R.id.editTextMotDePasse);

        binding.ButtonSoumettre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numeroElevage = editTextNumeroElevage.getText().toString();
                String motDePasse = editTextMotDePasse.getText().toString();

                // Vérifier si le profil existe dans la base de données
                boolean mdpExists = databaseAccess.isMdpCreated(numeroElevage);
                if (!mdpExists) {
                    // Si le profil n'existe pas, afficher un message d'erreur
                    Toast.makeText(getContext(), "Aucun compte n'est relié à ce numéro d'élevage", Toast.LENGTH_SHORT).show();
                } else {
                    // Vérifier les identifiants dans la base de données
                    boolean credentialsValid = databaseAccess.checkElevageCredentials(numeroElevage, motDePasse);
                    if (credentialsValid) {
                        // Identifiants valides, naviguer vers un autre fragment par exemple
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        SelectionActionEleveurFragment selectionActionEleveurFragment = SelectionActionEleveurFragment.newInstance();
                        fragmentTransaction.replace(R.id.container, selectionActionEleveurFragment);
                        fragmentTransaction.addToBackStack(null);

                        MainActivity.numeroElevage = numeroElevage; // Enregistrer le numéro de l'élevage dans la variable statique

                        fragmentTransaction.commit();
                    } else {
                        // Afficher un message d'erreur à l'utilisateur
                        Toast.makeText(getContext(), "Identifiants incorrects", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.LienCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigation vers le fragment CreationProfilEleveurFragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                CreationProfilEleveurFragment creationProfilEleveurFragment = CreationProfilEleveurFragment.newInstance();
                fragmentTransaction.replace(R.id.container, creationProfilEleveurFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}