package com.example.e_carterose;

public class Asda {
    private String codAsda;
    private String couleur;

    // Constructeur
    public Asda(String codAsda, String couleur) {
        this.codAsda = codAsda;
        this.couleur = couleur;
    }

    // Getter et setter pour codAsda
    public String getCodAsda() {
        return codAsda;
    }

    public void setCodAsda(String codAsda) {
        this.codAsda = codAsda;
    }

    // Getter et setter pour couleur
    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }
}
