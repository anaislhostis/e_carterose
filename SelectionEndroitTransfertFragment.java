package com.example.e_carterose;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.e_carterose.databinding.FragmentSelectionEndroitTransfertBinding;


public class SelectionEndroitTransfertFragment extends Fragment {

    private FragmentSelectionEndroitTransfertBinding binding;

    public static SelectionEndroitTransfertFragment newInstance() {
        SelectionEndroitTransfertFragment fragment = new SelectionEndroitTransfertFragment();
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentSelectionEndroitTransfertBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Bouton transfert vers un autre élevage
        binding.BoutonTransfertEleveur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une instance de SelectionTransportElevageFragment
                SelectionTransportElevageFragment selectionTransportElevageFragment = SelectionTransportElevageFragment.newInstance();

                // Remplacer le fragment actuel par le fragment SelectionTransportElevageFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, selectionTransportElevageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Bouton transfert vers l'abattoir
        binding.BoutonTransfertAbattoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une instance de SelectionTransportAbattoirFragment
                SelectionTransportAbattoirFragment selectionTransportAbattoirFragment = SelectionTransportAbattoirFragment.newInstance();

                // Remplacer le fragment actuel par le fragment SelectionTransportAbattoirFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, selectionTransportAbattoirFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


    }
}
