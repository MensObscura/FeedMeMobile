package com.example.thibault.feedme.Persistence;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by Thibault on 19/09/2015.
 */
public class Particulier {


    // id is generated by the database and set on the object automagically
    @DatabaseField(generatedId = true)
    long id;
    @DatabaseField(canBeNull = false)
    String prenom;
    @DatabaseField(canBeNull = false)
    Date dateNaissance;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    User idUser;


    Particulier() {
        // needed by ormlite
    }

    public Particulier(String prenom, Date dateNaissance, User idUser) {

        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=").append(this.id);
        sb.append(", ").append("Prénom : ").append(this.prenom);
        sb.append(", ").append("date de naissance : ").append(this.dateNaissance);
        sb.append(", ").append("utilisateur").append(this.idUser.toString());
        return sb.toString();
    }

    public User getIdUser() {
        return idUser;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public String getPrenom() {
        return prenom;
    }

    public long getId() {
        return id;
    }
}
