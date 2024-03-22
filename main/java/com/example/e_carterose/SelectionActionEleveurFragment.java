package com.example.e_carterose;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e_carterose.databinding.FragmentSelectionActionEleveurBinding;



public class SelectionActionEleveurFragment extends Fragment {

    private FragmentSelectionActionEleveurBinding binding;

    public static SelectionActionEleveurFragment newInstance() {
        SelectionActionEleveurFragment fragment = new SelectionActionEleveurFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectionActionEleveurBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Bouton visualisation élevage
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

        //Bouton notification de naissance
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

        //Bouton notification de mort
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
}
