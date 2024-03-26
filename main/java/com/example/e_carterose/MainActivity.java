package com.example.e_carterose;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String numeroElevage;
    public static String asda;
    private List<String> numTraAnimauxMorts;
    private List<String> numNatAnimauxTransfert;

    public static List<Animal> animaux_en_transport = new ArrayList<>();;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Charger le fragment IndexFragment dans le conteneur
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new IndexFragment())
                .commit();
    }


}
