package com.example.e_carterose;

public class Elevage {
    private String num_elevage;
    private String nom_elevage;
    private String mdp;
    private String cod_asda;
    private String date_modif;
    private String actif;

    public String getNom_elevage() {
        return nom_elevage;
    }

    public void setNom_elevage(String nom_elevage) {
        this.nom_elevage = nom_elevage;
    }

    public String getNum_elevage() {
        return num_elevage;
    }

    public void setNum_elevage(String num_elevage) {
        this.num_elevage = num_elevage;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getCod_asda() {
        return cod_asda;
    }

    public void setCod_asda(String cod_asda) {
        this.cod_asda = cod_asda;
    }

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }

    public String getDate_modif() {
        return date_modif;
    }

    public void setDate_modif(String date_modif) {
        this.date_modif = date_modif;
    }
}
