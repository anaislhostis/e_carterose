package com.example.e_carterose;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e_carterose.databinding.FragmentCreationProfilEleveurBinding;

public class CreationProfilEleveurFragment extends Fragment {

    private FragmentCreationProfilEleveurBinding binding;
    private EditText editTextNumeroElevage;
    private EditText editTextNomElevage;
    private EditText editTextMotDePasse;
    private Button buttonCreaCompte;

    private DatabaseAccess databaseAccess;

    public static CreationProfilEleveurFragment newInstance() {
        return new CreationProfilEleveurFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open(); // Ouvrir la connexion à la base de données
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreationProfilEleveurBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseAccess.close(); // Fermer la connexion à la base de données
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextNumeroElevage = view.findViewById(R.id.editTextNumeroElevage);
        editTextNomElevage = view.findViewById(R.id.editTextNomElevage);
        editTextMotDePasse = view.findViewById(R.id.editTextMotDePasse);
        buttonCreaCompte = view.findViewById(R.id.ButtonCreaCompte);

        buttonCreaCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numeroElevage = editTextNumeroElevage.getText().toString();
                String nomElevage = editTextNomElevage.getText().toString();
                String motDePasse = editTextMotDePasse.getText().toString();

                if (databaseAccess.isElevageExists(numeroElevage)) {
                    Toast.makeText(getContext(), "Un compte existe déjà pour ce numéro d'élevage", Toast.LENGTH_SHORT).show();
                } else {
                    // Appel de la méthode createElevageProfile pour créer un nouveau profil d'élevage
                    long result = databaseAccess.createElevageProfile(numeroElevage, nomElevage, motDePasse);

                    if (result != -1) {
                        Toast.makeText(getContext(), "Profil créé avec succès", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Une erreur est survenue lors de la création du profil", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}