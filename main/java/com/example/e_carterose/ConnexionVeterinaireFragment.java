package com.example.e_carterose;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.e_carterose.databinding.FragmentConnexionVeterinaireBinding;

public class ConnexionVeterinaireFragment extends Fragment {

    private FragmentConnexionVeterinaireBinding binding;
    private static final int REQUEST_CAMERA_PERMISSION = 1001;


    public static ConnexionVeterinaireFragment newInstance() {
        return new ConnexionVeterinaireFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConnexionVeterinaireBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Ajouter un écouteur de clic au bouton pour scanner une carte rose
        binding.ButtonScannerCarteRose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vérifier si la permission de la caméra est accordée
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // La permission n'est pas accordée, demandez-la à l'utilisateur
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    // La permission est accordée, ouvrez le scanner QR code
                    openQRCodeScanner();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Méthode pour ouvrir le scanner QR code
    private void openQRCodeScanner() {
        // Créer une instance du fragment du scanner QR code
        ScanQrCodeVeterinaireFragment scanQrCodeVeterinaireFragment = ScanQrCodeVeterinaireFragment.newInstance();

        // Remplacer le fragment actuel par le fragment du scanner QR code
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, scanQrCodeVeterinaireFragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
