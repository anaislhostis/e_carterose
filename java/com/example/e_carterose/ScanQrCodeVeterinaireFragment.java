package com.example.e_carterose;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


public class ScanQrCodeVeterinaireFragment extends Fragment implements TextureView.SurfaceTextureListener {
    private TextureView textureView;
    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private ScanQRCode qrCodeScanner;

    private static final int REQUEST_CAMERA_PERMISSION = 1001;

    public static ScanQrCodeVeterinaireFragment newInstance() {
        return new ScanQrCodeVeterinaireFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_qr_code, container, false);
        textureView = view.findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);
        // Initialisation du scanner QR code
        qrCodeScanner = new ScanQRCode(this);

        // Vérifier et demander la permission de la caméra si nécessaire
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
            startQRCodeScan();
        }

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragmentManager().popBackStack();
            }
        });
    }


    // Méthode pour démarrer le scan QR code
    private void startQRCodeScan() {
        qrCodeScanner.startScan();
    }

    // Méthode appelée lors du résultat du scan QR code
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        qrCodeScanner.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        openCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Pas besoin de traiter cela ici
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        closeCamera();
        return true;
    }

    // Méthode appelée lorsqu'une nouvelle image est disponible dans le TextureView
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        scanQRCode(); // Scanner le code QR dès qu'une nouvelle image est disponible
    }

    private void scanQRCode() {
        // Convertir le TextureView en image
        textureView.getBitmap();

        // Lancer le scan QR code
        qrCodeScanner.startScan();
    }


    private void openCamera() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // La permission n'est pas accordée, demandez-la à l'utilisateur
            Log.e("Camera", "La permission d'accès à la caméra n'est pas accordée.");
            return;
        }
        try {
            cameraManager = (CameraManager) requireContext().getSystemService(Context.CAMERA_SERVICE);
            String[] cameraIds = cameraManager.getCameraIdList();
            if (cameraIds.length > 0) {
                String cameraId = cameraIds[0]; // Sélectionnez la première caméra disponible
                cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                    @Override
                    public void onOpened(@NonNull CameraDevice camera) {
                        cameraDevice = camera;
                        Log.d("Camera", "Caméra ouverte avec succès.");
                        // Commencez la prévisualisation de la caméra ici
                        createCameraPreview();
                    }

                    @Override
                    public void onDisconnected(@NonNull CameraDevice camera) {
                        camera.close();
                        cameraDevice = null;
                        Log.e("Camera", "Connexion à la caméra interrompue.");
                    }

                    @Override
                    public void onError(@NonNull CameraDevice camera, int error) {
                        camera.close();
                        cameraDevice = null;
                        Log.e("Camera", "Erreur lors de l'ouverture de la caméra: " + error);
                    }
                }, null);
            } else {
                Log.e("Camera", "Aucune caméra disponible.");
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.e("Camera", "Erreur d'accès à la caméra: " + e.getMessage());
        }
    }

    private void closeCamera() {
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void createCameraPreview() {
        try {
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(2000, 2500); // Définissez la taille de la surface de prévisualisation

            Surface surface = new Surface(surfaceTexture);

            // Créez une session de capture pour la prévisualisation
            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        // La configuration de la session est terminée, commencez la prévisualisation
                        CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        captureRequestBuilder.addTarget(surface);
                        CaptureRequest captureRequest = captureRequestBuilder.build();
                        session.setRepeatingRequest(captureRequest, null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                        Log.e("Camera", "Erreur lors de la configuration de la prévisualisation de la caméra: " + e.getMessage());
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.e("Camera", "Échec de la configuration de la session de capture pour la prévisualisation.");
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.e("Camera", "Erreur lors de la création de la prévisualisation de la caméra: " + e.getMessage());
        }
    }

    public class ScanQRCode {
        private Fragment fragment;
        private Animal scannedAnimal;

        public ScanQRCode(Fragment fragment) {
            this.fragment = fragment;
        }

        public void startScan() {
            IntentIntegrator.forSupportFragment(fragment)
                    .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                    .setPrompt("Scan QR Code")
                    .setOrientationLocked(false)
                    .initiateScan();
        }

        public void handleActivityResult(int requestCode, int resultCode, Intent data) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            handleScanResult(result);
        }

        private void handleScanResult(IntentResult result) {
            if (fragment.isAdded() && !fragment.isDetached() && !fragment.isRemoving()) {
                Context context = fragment.requireContext();
                if (result != null && result.getContents() != null) {
                    String qrContent = result.getContents();
                    String[] parts = qrContent.split(":", -1); // Utiliser -1 comme limite pour conserver les parties vides

                    Log.d("QRCode", "Contenu du QR code : " + qrContent);
                    Log.d("QRCode", "Nombre de parties : " + parts.length);

                    for (String part : parts) {
                        Log.d("QRCode", "Partie : " + part);
                    }

                    if (parts.length >= 20) {
                        scannedAnimal = new Animal();

                        scannedAnimal.setNumNat(parts[0]);
                        scannedAnimal.setNom(parts[1]);
                        scannedAnimal.setRace(parts[2]);
                        scannedAnimal.setDateNaiss(parts[3]);
                        scannedAnimal.setCodPaysNaiss(parts[4]);
                        scannedAnimal.setNumExpNaiss(parts[5]);
                        scannedAnimal.setNumTra(parts[6]);
                        scannedAnimal.setSexe(parts[7]);
                        scannedAnimal.setNumNatPere(parts[8].equals("null") ? "" : parts[8]);
                        scannedAnimal.setCodPaysPere(parts[9].equals("null") ? "" : parts[9]);
                        scannedAnimal.setCodRacePere(parts[10].equals("null") ? "" : parts[10]);
                        scannedAnimal.setNumNatMere(parts[11].equals("null") ? "" : parts[11]);
                        scannedAnimal.setCodPaysMere(parts[12].equals("null") ? "" : parts[12]);
                        scannedAnimal.setCodRaceMere(parts[13].equals("null") ? "" : parts[13]);
                        scannedAnimal.setActif(parts[14].equals("null") ? "" : parts[14]);
                        scannedAnimal.setNumElevage(parts[15].equals("null") ? "" : parts[15]);
                        scannedAnimal.setDateModif(parts[16].equals("null") ? "" : parts[16]);
                        scannedAnimal.setCodPays(parts[17]);

                        Toast.makeText(context, "QR code scanné", Toast.LENGTH_SHORT).show();

                        // Vérifier si le fragment est attaché à une activité avant de créer une instance
                        if (fragment.isAdded()) {
                            // Passer les données extraites du QR Code au fragment
                            Bundle args = new Bundle();
                            args.putSerializable("animal", scannedAnimal);
                            DetailsAnimalVeterinaireFragment fragmentDetailsAnimalVeterinaire = new DetailsAnimalVeterinaireFragment();

                            // Récupérer les soins et les vaccins de la base de données
                            DatabaseAccess dbAccess = new DatabaseAccess(context);
                            List<Soins> soinsList = dbAccess.getCareByNumNat(scannedAnimal.getNumNat());
                            List<Vaccins> vaccinsList = dbAccess.getVaccinesByNumNat(scannedAnimal.getNumNat());

                            args.putSerializable("soinsList", (Serializable) soinsList);
                            args.putSerializable("vaccinsList", (Serializable) vaccinsList);

                            fragmentDetailsAnimalVeterinaire.setArguments(args);

                            // Remplacer le fragment actuel par le fragment DetailsAnimalFragment
                            fragment.getParentFragmentManager().beginTransaction()
                                    .replace(R.id.container, fragmentDetailsAnimalVeterinaire)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            // Si le fragment n'est pas attaché à une activité
                            Log.e("ScanQRCodeFragment", "Le fragment n'est pas attaché à une activité.");
                        }
                    } else {
                        // Si le contenu du QR code est invalide
                        Toast.makeText(fragment.requireContext(), "Contenu du QR code invalide", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si le scan a échoué ou a été annulé
                    Toast.makeText(fragment.requireContext(), "Scan annulé ou échoué", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Si le fragment n'est pas attaché à une activité
                Log.e("ScanQRCodeFragment", "Le fragment n'est pas attaché à une activité.");
            }
        }
    }
}
