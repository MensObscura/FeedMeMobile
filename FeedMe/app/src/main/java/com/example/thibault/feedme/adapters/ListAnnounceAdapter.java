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
            this.announces = this.databaseHelper.getOffresDao().queryForAll();
        } catch (SQLException e) {
            Log.e("ListAnnounceAdapter", "failed to get Offres from database");
        }
    }

    // Total number of things contained within the adapter
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
        tvTitle = (TextView) outputView.findViewById(R.id.TVcard_title);
        tvAdresse = (TextView) outputView.findViewById(R.id.TVcard_ville);
        tvPrice = (TextView) outputView.findViewById(R.id.TVcard_price);
        tvTypecusine = (TextView) outputView.findViewById(R.id.TVcard_type);
        tvRemainPlace = (TextView) outputView.findViewById(R.id.TVcard_remain_place);

        tvTitle.setText(this.announces.get(position).getTitre());
        tvAdresse.setText(this.announces.get(position).getIdAdress().getVille().getNom());
        tvPrice.setText(this.announces.get(position).getPrix() + "");
        tvTypecusine.setText(this.announces.get(position).getTypeCuisine().getTypeCuisine());


        // on calcul le nombre de place restantes
        List<Reservation> reservations = null;
        try {

            reservations = this.databaseHelper.getReservationDao().queryBuilder().where().eq("offreId_id", announces.get(position)).query();

        } catch (SQLException e) {
            Log.e("ListAnnounceAdapter", "Failed to get Reservation on offre " + this.announces.get(position).getTitre() + " : " + e);
        }

        if (reservations != null) {

            int nbReservation = reservations.size();
            int remainPlace = this.announces.get(position).getNbPrsn() - nbReservation;

            if (remainPlace > 0) {
                tvRemainPlace.setText("" + remainPlace);
            } else {
                tvRemainPlace.setText("Complet");
            }


        } else {

            Toast.makeText(this.mContext, R.string.reservationNotFound, Toast.LENGTH_SHORT);
        }


        outputView.setId(position);

        return outputView;
    }

    public long getOffreIdFromPosition(int position){

        return this.announces.get(position).getId();
    }
}
