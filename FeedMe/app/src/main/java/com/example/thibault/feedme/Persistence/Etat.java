package com.example.thibault.feedme.Persistence;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Thibault on 19/09/2015.
 */
public class Etat {


    @DatabaseField(id = true)
    String code;
    @DatabaseField
    String nom;


    Etat() {
        // needed by ormlite
    }

    public Etat(String code, String nom) {

        this.code = code;
        this.nom = nom;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("code : ").append(this.code);
        sb.append(", ").append("Nom : ").append(this.nom);

        return sb.toString();
    }
}
