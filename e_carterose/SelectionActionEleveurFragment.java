package com.example.e_carterose;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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


public class SelectionActionEleveurFragment extends Fragment {
    private DatabaseAccess db;
    private Context context;

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
            db.synchronizeElevagesData(); // Synchroniser les données de la table elevage
            db.synchronizeAnimauxData(); // Synchroniser les données de la table animal
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

}
