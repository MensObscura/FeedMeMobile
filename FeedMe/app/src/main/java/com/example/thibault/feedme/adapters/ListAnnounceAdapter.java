package com.example.thibault.feedme.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thibault.feedme.Persistence.Offre;
import com.example.thibault.feedme.Persistence.Reservation;
import com.example.thibault.feedme.Persistence.Ville;
import com.example.thibault.feedme.R;
import com.example.thibault.feedme.databaseHelpers.FeedMeOpenDatabaseHelper;
import com.example.thibault.feedme.fragments.ListAnnounceFragment;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Thibault on 14/09/2015.
 */
public class ListAnnounceAdapter extends BaseAdapter {
    private Context mContext;
    private List<Offre> announces;
    private FeedMeOpenDatabaseHelper databaseHelper;

    // Gets the context so it can be used later
    public ListAnnounceAdapter(Context c) {
        this.mContext = c;

        this.databaseHelper = FeedMeOpenDatabaseHelper.getHelper(c);
        this.announces = null;
        try {
            // Recuperer toutes les offres en base
            this.announces = this.databaseHelper.getOffresDao().queryForAll();
        } catch (SQLException e) {
            Log.e("ListAnnounceAdapter", "failed to get Offres from database");
        }
    }

    /**
     * Retourne le nombre d'annonces
     * @return le nombre d'annonces
     */
    public int getCount() {
        return this.announces.size();
    }

    // Require for structure, not really used in my code.
    public Object getItem(int position) {
        return null;
    }

    // Require for structure, not really used in my code. Can
    // be used to get the id of an item in the adapter for
    // manual control.
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View outputView = this.initComponent(convertView, position);
        outputView.setId(position);
        return outputView;
    }


    /**
     * Construction de la vue d'une offre dans la liste
     * @param convertView
     * @param position l'ID de l'offre dans la vue
     * @return la vue construite
     */
    private View initComponent(View convertView, int position) {

            //define compounant
        View outputView;
        TextView tvTitle;
        TextView tvAdresse;
        TextView tvTypecusine;
        TextView tvPrice;
        TextView tvRemainPlace;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            outputView = convertView;

            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            outputView = inflater.inflate(R.layout.card_announce, null);
        } else {
            outputView = convertView;
        }

        //init component
        tvTitle = (TextView) outputView.findViewById(R.id.TVcard_title);
        tvAdresse = (TextView) outputView.findViewById(R.id.TVcard_ville);
        tvPrice = (TextView) outputView.findViewById(R.id.TVcard_price);
        tvTypecusine = (TextView) outputView.findViewById(R.id.TVcard_type);
        tvRemainPlace = (TextView) outputView.findViewById(R.id.TVcard_remain_place);

        // fill component
        tvTitle.setText(this.announces.get(position).getTitre());
        tvAdresse.setText(this.announces.get(position).getIdAdress().getVille().getNom());
        tvPrice.setText(this.announces.get(position).getPrix() + " €");
        tvTypecusine.setText(this.announces.get(position).getTypeCuisine().getTypeCuisine());

        //verifing remain places
        int remainPlace = this.getRemainPlace(position);
        if (remainPlace > 0) {
            tvRemainPlace.setText("" + remainPlace);
        } else {
            tvRemainPlace.setText("Complet");
        }
        return outputView;
    }

    public long getOffreIdFromPosition(int position){
        return this.announces.get(position).getId();
    }

    /**
     * Retourne le nombre de places restantes pour une offre
     * @param position ID de l'offre pour laquelle on calcule le nombre de places restantes
     * @return le nombre de places restantes
     */
    public int getRemainPlace(int position) {
        // on calcul le nombre de place restantes
        List<Reservation> reservations = null;
        try {
            reservations = this.databaseHelper.getReservationDao().queryBuilder().where().eq("offreId_id", announces.get(position)).query();
        } catch (SQLException e) {
            Log.e("ListAnnounceAdapter", "Failed to get Reservation on offre " + this.announces.get(position).getTitre() + " : " + e);
        }

        if (reservations != null) {

            // calculating nb remain place

            int nbReservation = reservations.size();
            int remainPlace = this.announces.get(position).getNbPrsn() - nbReservation;
            return remainPlace;
        } else {
            Toast.makeText(this.mContext, R.string.reservationNotFound, Toast.LENGTH_SHORT);
        }
       return 0;
    }
}
