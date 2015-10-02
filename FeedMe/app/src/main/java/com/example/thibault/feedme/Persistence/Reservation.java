package com.example.thibault.feedme.Persistence;

import com.j256.ormlite.field.DatabaseField;

import java.sql.Timestamp;

/**
 * Created by Thibault on 19/09/2015.
 */
public class Reservation {
    // id is generated by the database and set on the object automagically
    @DatabaseField(generatedId = true)
    long id;
    @DatabaseField(foreign = true,canBeNull = false)
    Offre offreId;
    @DatabaseField(foreign = true,canBeNull = false)
    User conId;
   /** @DatabaseField(canBeNull = false)
    String etat;*/
    @DatabaseField(canBeNull = false)
    Timestamp date;


    Reservation() {
        // needed by ormlite
    }

    public Reservation(Offre offreId, User conId, Timestamp date) {

        this.offreId = offreId;
        this.conId = conId;
        this.date = date;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id=").append(this.id);
        sb.append(", ").append("offre ID = ").append(this.offreId);
        sb.append(", ").append("con ID : ").append(this.conId);
        sb.append(", ").append("date : ").append(this.date);

        return sb.toString();
    }

    public Offre getOffreId() {
        return offreId;
    }

    public long getId() {
        return id;
    }

    public User getConId() {
        return conId;
    }

    public Timestamp getDate() {
        return date;
    }
}
