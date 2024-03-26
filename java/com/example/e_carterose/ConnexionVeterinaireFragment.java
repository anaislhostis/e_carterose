package com.example.e_carterose;

import android.content.Context;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.e_carterose.databinding.FragmentConnexionVeterinaireBinding;

public class ConnexionVeterinaireFragment extends Fragment {

    private FragmentConnexionVeterinaireBinding binding;
    private static final int REQUEST_CAMERA_PERMISSION = 1001;
    private DatabaseAccess db;
    private Context context;


    public static ConnexionVeterinaireFragment newInstance() {
        return new ConnexionVeterinaireFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConnexionVeterinaireBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        context = getActivity();
        db = new DatabaseAccess(context);

        if (isNetworkAvailable()) {
            Log.e("ConnexionVeterinaire", "Connexion Internet disponible");

            db.sendAllSoinsToServer();
            db.sendAllvaccinsToServer();
            Log.e("ConnexionVeterinaire", "Données synchronisées");


        } else {
            Log.e("NETWORK", "Connexion Internet non disponible");
            Toast.makeText(context, "Les données seront mises à jour lors de la prochaine connexion à internet", Toast.LENGTH_SHORT).show();
        }

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
