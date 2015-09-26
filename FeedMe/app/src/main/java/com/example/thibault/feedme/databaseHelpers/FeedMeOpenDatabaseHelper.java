package com.example.thibault.feedme.databaseHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.thibault.feedme.Persistence.Adresse;
import com.example.thibault.feedme.Persistence.Authentification;
import com.example.thibault.feedme.Persistence.Offre;
import com.example.thibault.feedme.Persistence.Particulier;
import com.example.thibault.feedme.Persistence.Pays;
import com.example.thibault.feedme.Persistence.Reservation;
import com.example.thibault.feedme.Persistence.Role;
import com.example.thibault.feedme.Persistence.TypeCuisine;
import com.example.thibault.feedme.Persistence.User;
import com.example.thibault.feedme.Persistence.Ville;
import com.example.thibault.feedme.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Thibault on 25/09/2015.
 */
public class FeedMeOpenDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "feedMe";
    private static final int DATABASE_VERSION = 1;

    /**
     * The data access object used to interact with the Sqlite database to do C.R.U.D operations.
     */
    private Dao<Adresse, Long> adresseDao;
    private Dao<Authentification, Long> authentificationDao;
    private Dao<Offre, Long> offresDao;
    private Dao<Particulier, Long> particuliersDao;
    private Dao<Pays, Long> paysDao;
    private Dao<Reservation, Long> reservationDao;
    private Dao<Role, Long> roleDao;
    private Dao<TypeCuisine, Long> typeCuisinesDao;
    private Dao<User, Long> userssDao;
    private Dao<Ville, Long> villesDao;
    private static FeedMeOpenDatabaseHelper instance;

    public FeedMeOpenDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION,
                /**
                 * R.raw.ormlite_config is a reference to the ormlite_config.txt file in the
                 * /res/raw/ directory of this project
                 * */
                R.raw.ormlite_config);
    }


    public static synchronized FeedMeOpenDatabaseHelper getHelper(Context context)
    {
        if (instance == null)
            instance = new FeedMeOpenDatabaseHelper(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            /**
             * creates the database tables
             */
            TableUtils.createTable(connectionSource, Pays.class);
            TableUtils.createTable(connectionSource, Role.class);
            TableUtils.createTable(connectionSource, TypeCuisine.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Ville.class);
            TableUtils.createTable(connectionSource, Adresse.class);
            TableUtils.createTable(connectionSource, Particulier.class);
            TableUtils.createTable(connectionSource, Authentification.class);
            TableUtils.createTable(connectionSource, Offre.class);
            TableUtils.createTable(connectionSource, Reservation.class);






            this.getPaysDao().create(new Pays("FR", "France"));
            this.getPaysDao().create(new Pays("BE", "Belgique"));

            this.getRolesDao().create(new Role("ROLE_PARTICULIER"));

            this.getTypeCuisinesDao().create(new TypeCuisine("Cuisine régionale"));
            this.getTypeCuisinesDao().create(new TypeCuisine("Africaine"));
            this.getTypeCuisinesDao().create(new TypeCuisine("Steak house"));
            this.getTypeCuisinesDao().create(new TypeCuisine("Gastronomique"));
            this.getTypeCuisinesDao().create(new TypeCuisine("Grec"));
            this.getTypeCuisinesDao().create(new TypeCuisine("Asiatique"));
            this.getTypeCuisinesDao().create(new TypeCuisine("Espagnole"));
            this.getTypeCuisinesDao().create(new TypeCuisine("Barbecue"));
            this.getTypeCuisinesDao().create(new TypeCuisine("Provencale"));
            this.getTypeCuisinesDao().create(new TypeCuisine("Bretonne"));
            this.getTypeCuisinesDao().create(new TypeCuisine("Italienne"));
            this.getTypeCuisinesDao().create(new TypeCuisine("Savoyard"));
            this.getTypeCuisinesDao().create(new TypeCuisine("Alsacienne"));
            this.getTypeCuisinesDao().create(new TypeCuisine("Mexicaine"));


        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Echec de la création de la database" +e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            /**
             * Recreates the database when onUpgrade is called by the framework
             */

            TableUtils.dropTable(connectionSource, Adresse.class, false);
            TableUtils.dropTable(connectionSource, Authentification.class, false);
            TableUtils.dropTable(connectionSource, Offre.class, false);
            TableUtils.dropTable(connectionSource, Particulier.class, false);
            TableUtils.dropTable(connectionSource, Pays.class, false);
            TableUtils.dropTable(connectionSource, Reservation.class, false);
            TableUtils.dropTable(connectionSource, Role.class, false);
            TableUtils.dropTable(connectionSource, TypeCuisine.class, false);
            TableUtils.dropTable(connectionSource, User.class, false);
            TableUtils.dropTable(connectionSource, Ville.class, false);

            onCreate(database, connectionSource);


        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Echec de la mise à jour de la database");
        }
    }

    /**
     * Returns an instance of the data access object
     *
     * @return
     * @throws SQLException
     */

    public Dao<Adresse, Long> getAdressesDao() throws SQLException {
        if (adresseDao == null) {
            adresseDao = getDao(Adresse.class);
        }
        return adresseDao;
    }

    public Dao<Authentification, Long> getAuthentificationDao() throws SQLException {
        if (authentificationDao == null) {
            authentificationDao = getDao(Authentification.class);
        }
        return authentificationDao;
    }

    public Dao<Particulier, Long> getParticuliersDao() throws SQLException {
        if (particuliersDao == null) {
            particuliersDao = getDao(Particulier.class);
        }
        return particuliersDao;
    }

    public Dao<Offre, Long> getOffresDao() throws SQLException {
        if (offresDao == null) {
            offresDao = getDao(Offre.class);
        }
        return offresDao;
    }

    public Dao<Pays, Long> getPaysDao() throws SQLException {
        if (paysDao == null) {
            paysDao = getDao(Pays.class);
        }
        return paysDao;
    }

    public Dao<Reservation, Long> getReservationDao() throws SQLException {
        if (reservationDao == null) {
            reservationDao = getDao(Reservation.class);
        }
        return reservationDao;
    }

    public Dao<Role, Long> getRolesDao() throws SQLException {
        if (roleDao == null) {
            roleDao = getDao(Role.class);
        }
        return roleDao;
    }

    public Dao<TypeCuisine, Long> getTypeCuisinesDao() throws SQLException {
        if (typeCuisinesDao == null) {
            typeCuisinesDao = getDao(TypeCuisine.class);
        }
        return typeCuisinesDao;
    }


    public Dao<User, Long> getUsersDao() throws SQLException {
        if (userssDao == null) {
            userssDao = getDao(User.class);
        }
        return userssDao;
    }

    public Dao<Ville, Long> getVillesDao() throws SQLException {
        if (villesDao == null) {
            villesDao = getDao(Ville.class);
        }
        return villesDao;
    }

}

