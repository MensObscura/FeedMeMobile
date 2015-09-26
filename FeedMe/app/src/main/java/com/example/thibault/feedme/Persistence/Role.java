package com.example.thibault.feedme.Persistence;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Thibault on 19/09/2015.
 */
public class Role {


    @DatabaseField(generatedId = true)
    long id;
    @DatabaseField
    String role;


    Role() {
        // needed by ormlite
    }

    public Role(String role) {

        this.role = role;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id : ").append(this.id);
        sb.append(", ").append("Role : ").append(this.role);

        return sb.toString();
    }
}
