package com.example.e_carterose;

import java.io.Serializable;

public class Animal implements Serializable {
    private String num_nat;
    private String num_tra;
    private String cod_pays;
    private String nom;
    private String sexe;
    private String date_naiss;
    private String cod_pays_naiss;
    private String num_exp_naiss;
    private String cod_pays_pere;
    private String num_nat_pere;
    private String cod_race_pere;
    private String cod_pays_mere;
    private String num_nat_mere;
    private String cod_race_mere;
    private String num_elevage;
    private String cod_race;
    private String date_modif;
    private String actif;

    public String getNumNat() {
        return num_nat;
    }

    public void setNumNat(String num_nat) {
        this.num_nat = num_nat;
    }

    public String getNumTra() {
        return num_tra;
    }

    public void setNumTra(String num_tra) {
        this.num_tra = num_tra;
    }

    public String getCodPays() {
        return cod_pays;
    }

    public void setCodPays(String cod_pays) {
        this.cod_pays = cod_pays;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getDateNaiss() {
        return date_naiss;
    }

    public void setDateNaiss(String date_naiss) {
        this.date_naiss = date_naiss;
    }

    public String getCodPaysNaiss() {
        return cod_pays_naiss;
    }

    public void setCodPaysNaiss(String cod_pays_naiss) {
        this.cod_pays_naiss = cod_pays_naiss;
    }

    public String getNumExpNaiss() {
        return num_exp_naiss;
    }

    public void setNumExpNaiss(String num_exp_naiss) {
        this.num_exp_naiss = num_exp_naiss;
    }

    public String getCodPaysPere() {
        return cod_pays_pere;
    }

    public void setCodPaysPere(String cod_pays_pere) {
        this.cod_pays_pere = cod_pays_pere;
    }

    public String getNumNatPere() {
        return num_nat_pere;
    }

    public void setNumNatPere(String num_nat_pere) {
        this.num_nat_pere = num_nat_pere;
    }

    public String getCodRacePere() {
        return cod_race_pere;
    }

    public void setCodRacePere(String cod_race_pere) {
        this.cod_race_pere = cod_race_pere;
    }

    public String getCodPaysMere() {
        return cod_pays_mere;
    }

    public void setCodPaysMere(String cod_pays_mere) {
        this.cod_pays_mere = cod_pays_mere;
    }

    public String getNumNatMere() {
        return num_nat_mere;
    }

    public void setNumNatMere(String num_nat_mere) {
        this.num_nat_mere = num_nat_mere;
    }

    public String getCodRaceMere() {
        return cod_race_mere;
    }

    public void setCodRaceMere(String cod_race_mere) {
        this.cod_race_mere = cod_race_mere;
    }

    public String getNumElevage() {
        return num_elevage;
    }

    public void setNumElevage(String num_nat) {
        if(num_nat.length() >= 6) { // Vérifie si la longueur de num_nat est supérieure ou égale à 6
            this.num_elevage = num_nat.substring(0, 6); // Extrayez les 6 premiers caractères de num_nat
        } else {
            // Gérer le cas où la longueur de num_nat est inférieure à 6 (optionnel)
            System.out.println("La longueur de num_nat est inférieure à 6");
            this.num_elevage = num_nat; // Affectez num_nat tel quel à num_elevage
        }
    }

    public String getRace() {
        return cod_race;
    }

    public void setRace(String cod_race) {
        this.cod_race = cod_race;
    }

    public String getDateModif() {
        return date_modif;
    }
    public void setDateModif(String cod_race_mere) {
        this.date_modif = date_modif;
    }

    public String getActif() {
        return actif;
    }
    public void setActif(String actif) {
        this.actif = actif;
    }
}
