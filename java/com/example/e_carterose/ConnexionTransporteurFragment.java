package com.example.e_carterose;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e_carterose.databinding.FragmentConnexionTransporteurBinding;


public class ConnexionTransporteurFragment extends Fragment {

    private FragmentConnexionTransporteurBinding binding;
    private DatabaseAccess db;
    private Context context;

    public static ConnexionTransporteurFragment newInstance() {
        ConnexionTransporteurFragment fragment = new ConnexionTransporteurFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConnexionTransporteurBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Initialiser votre objet DatabaseAccess
        DatabaseAccess databaseAccess = new DatabaseAccess(getContext());

        // Vérifier si databaseAccess est null avant d'appeler la méthode
        if (databaseAccess != null) {
            // Appeler la méthode sendAllSoinsToServer() sur votre objet DatabaseAccess
            databaseAccess.sendAllSoinsToServer();
        }


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.ButtonScannerCarteRose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanQrCodeTransportFragment scanQrCodeTransportFragment = ScanQrCodeTransportFragment.newInstance();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, scanQrCodeTransportFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        binding.ButtonVisualiserAnimauxTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimauxEnTransportFragment animauxEnTransportFragment = AnimauxEnTransportFragment.newInstance();

                // Remplacer le fragment actuel par le fragment des animaux en transport
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, animauxEnTransportFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

}
