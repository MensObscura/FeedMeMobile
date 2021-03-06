package com.example.thibault.feedme.Persistence;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Thibault on 19/09/2015.
 */
public class User {

    // id is generated by the database and set on the object automagically
    @DatabaseField(generatedId = true)
    long id;
    @DatabaseField(canBeNull = false)
    String nom;
    @DatabaseField(canBeNull = false, unique = true)
    String email;


    User() {
        // needed by ormlite
    }

    public User(String nom, String email) {

        this.nom = nom;
        this.email = email;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=").append(this.id);
        sb.append(", ").append("Nom : ").append(this.nom);
        sb.append(", ").append("email : ").append(this.email);

        return sb.toString();
    }

    public long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }
}
