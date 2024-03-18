package com.example.e_carterose;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static String numeroElevage = "75012345";
    public static String asda;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Charger le fragment IndexFragment dans le conteneur
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new IndexFragment())
                .commit();
    }
}
