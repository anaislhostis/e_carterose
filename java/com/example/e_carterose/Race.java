package com.example.e_carterose;

public class Race {
    private String codRace;
    private String libCourt;
    private String libLong;

    // Constructeur
    public Race(String codRace, String libCourt, String libLong) {
        this.codRace = codRace;
        this.libCourt = libCourt;
        this.libLong = libLong;
    }

    // Getter et setter pour codRace
    public String getCodRace() {
        return codRace;
    }

    public void setCodRace(String codRace) {
        this.codRace = codRace;
    }

    // Getter et setter pour libCourt
    public String getLibCourt() {
        return libCourt;
    }

    public void setLibCourt(String libCourt) {
        this.libCourt = libCourt;
    }

    // Getter et setter pour libLong
    public String getLibLong() {
        return libLong;
    }

    public void setLibLong(String libLong) {
        this.libLong = libLong;
    }
}