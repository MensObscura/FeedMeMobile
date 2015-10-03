package com.example.thibault.feedme.Persistence;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Thibault on 25/09/2015.
 */
public class Authentification {


    @DatabaseField
    String password;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false,foreignAutoCreate = true)
    Role idRole;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false, unique = true, foreignAutoCreate = true)
    User idUser;


    Authentification() {
        // needed by ormlite
    }

    public Authentification(String password, Role idRole, User idUser) {

        this.password = password;
        this.idRole = idRole;
        this.idUser = idUser;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Password : ").append("*********");
        sb.append(", ").append("id Role : ").append(this.idRole.toString());
        sb.append(", ").append("id Utilisateur : ").append(this.idUser.toString());

        return sb.toString();
    }


}
