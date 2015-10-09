package com.example.thibault.feedme.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thibault.feedme.Persistence.Offre;
import com.example.thibault.feedme.Persistence.Particulier;
import com.example.thibault.feedme.Persistence.Reservation;
import com.example.thibault.feedme.R;
import com.example.thibault.feedme.activities.MainActivity;
import com.example.thibault.feedme.databaseHelpers.FeedMeOpenDatabaseHelper;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class BookAnnounceFragment extends Fragment {

    private Button bConfirm;
    private TextView tvTitle;
    private TextView tvNameAuthor;
    private TextView tvAddress;
    private TextView tvPrice;
    private TextView tvDate;
    private TextView tvMenu;
    private TextView tvRemainPlaces;
    private TextView tvTypeCuisine;
    private TextView tvDuration;
    private TextView tvPets;
    private TextView tvBrief;
    private TextView tvTotalPlaces;
    private EditText etBookPlace;

    private Offre currentOffre;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vBook = inflater.inflate(R.layout.fragment_book_announce, container, false);

        //ON initialise les conposants puis on les remplits.
        this.initComponants(vBook);

        String idOffre = (String) getArguments().get("offre");

        //fill components
        this.fillComponants(idOffre);

        //Verification des champs et ajout dans database
        this.bConfirm.setOnClickListener(this.createFormOnClickListener());
        return vBook;
    }

    private View.OnClickListener createFormOnClickListener() {
        View.OnClickListener formOnclick =new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etBookPlace.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), R.string.champVide, Toast.LENGTH_SHORT).show();
                    etBookPlace.setBackgroundColor(Color.RED);
                } else if (currentOffre != null) {

                    FeedMeOpenDatabaseHelper databaseHelper = FeedMeOpenDatabaseHelper.getHelper(getActivity());

                    try {
                        //On crée autant de reservation que le nombre de place entrée
                        Log.d("BookAnnounceFragment", "toc");
                        for (int i = 0; i < Integer.parseInt(etBookPlace.getText().toString()); i++) {
                            Log.d("BookAnnounceFragment", "" + i);
                            databaseHelper.getReservationDao().create(new Reservation(currentOffre, ((MainActivity) getActivity()).getCurrentUser(), Calendar.getInstance().getTime()));
                        }
                        Log.d("BookAnnounceFragment", "out");
                        List<Reservation> list = databaseHelper.getReservationDao().queryForAll();

                    } catch (SQLException e) {
                        Log.e("BookAnnounceFragment", "Failed to store reservation in Database : " + e);
                        return;
                    }

                    Toast.makeText(getActivity(), "Couvert Réservé", Toast.LENGTH_SHORT).show();

                   setFragment();


                }

            }




        };
        return formOnclick;
    }

    //On set le fragment home
    private void setFragment() {
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment current = manager.findFragmentByTag("fragment");

        // Remplacer le fragment courant par le fragment home
        HomeFragment fHome = new HomeFragment();
        transaction.replace(current.getId(), fHome, "fragment");

        transaction.commit();
        getActivity().setTitle(getString(R.string.app_name));
    }

    //on init les composant
    private void initComponants(View vBook) {

        this.bConfirm = (Button) vBook.findViewById(R.id.Bconfirm_book);
        this.tvTitle = (TextView) vBook.findViewById(R.id.TVtitle);
        this.tvNameAuthor = (TextView) vBook.findViewById(R.id.TVname_author);
        this.tvAddress = (TextView) vBook.findViewById(R.id.TVaddress);
        this.tvPrice = (TextView) vBook.findViewById(R.id.TVprice);
        this.tvDate = (TextView) vBook.findViewById(R.id.TVdate_hour);
        this.tvMenu = (TextView) vBook.findViewById(R.id.TVmenu);
        this.tvRemainPlaces = (TextView) vBook.findViewById(R.id.TVremain_place);
        this.tvTypeCuisine = (TextView) vBook.findViewById(R.id.TVtype);
        this.tvDuration = (TextView) vBook.findViewById(R.id.TVduration);
        this.tvPets = (TextView) vBook.findViewById(R.id.TVpets);
        this.tvBrief = (TextView) vBook.findViewById(R.id.TVnotes);
        this.tvTotalPlaces = (TextView) vBook.findViewById(R.id.TVtotalPlaces);
        this.etBookPlace = (EditText) vBook.findViewById(R.id.ETnbpeople_book);
    }


    private void fillComponants(String idOffre) {

        // récupère l'offre au préalable

        Offre offre = this.getOffre(idOffre);
        Particulier particulier = this.getCreator(offre);

        //On remplit les champs

        if (offre != null && particulier != null) {
            this.currentOffre = offre;
            int remainPlaces = this.getRemainPlaces(offre);

            this.tvTitle.setText(offre.getTitre());
            this.tvNameAuthor.setText(offre.getIdUser().getNom() + " " + particulier.getPrenom());
            this.tvAddress.setText(offre.getIdAdress().getVoie() + "\n" + offre.getIdAdress().getVille().getCodePostal() + " " + offre.getIdAdress().getVille().getNom() + "\n" + offre.getIdAdress().getVille().getPays().getNom());
            this.tvPrice.setText(offre.getPrix() + " €");
            this.tvDate.setText(offre.getDateRepas().toString());
            this.tvMenu.setText(offre.getMenu());
            this.tvRemainPlaces.setText(remainPlaces == 0 ? "Complet" : remainPlaces + "");
            if (remainPlaces == 0) {
                this.etBookPlace.setEnabled(false);
                this.bConfirm.setEnabled(false);
            }
            this.tvTypeCuisine.setText(offre.getTypeCuisine().getTypeCuisine());
            this.tvDuration.setText(getString(R.string.duration)+" : "+(offre.getDurée() == 0 ? "inconnue" : offre.getDurée()) + "");
            String animaux = getString(offre.isAnimaux() ? R.string.yes : R.string.no);
            this.tvPets.setText("Animaux de compagnie : " + animaux);
            this.tvBrief.setText(offre.getNotes());
            this.tvTotalPlaces.setText(remainPlaces + "");
            this.defineETBookPlace(remainPlaces);
        }

    }

    //On definit le text watcher de l'editText reservation : nb place
    private void defineETBookPlace(final int remainPlaces) {

        this.etBookPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (Integer.parseInt(s.toString()) > remainPlaces) {
                        etBookPlace.setText(remainPlaces + "");
                        Toast.makeText(getActivity(), R.string.mustbeInferiorOrEqual, Toast.LENGTH_SHORT).show();
                    }
                    etBookPlace.setBackgroundColor(etBookPlace.getDrawingCacheBackgroundColor());
                    bConfirm.setEnabled(true);
                } else {
                    etBookPlace.setBackgroundColor(Color.RED);
                    bConfirm.setEnabled(false);
                }
            }
        });
    }

    // get les places restantes
    private int getRemainPlaces(Offre offre) {

        FeedMeOpenDatabaseHelper databaseHelper = FeedMeOpenDatabaseHelper.getHelper(getActivity());
        List<Reservation> reservations = null;
        try {

            reservations = databaseHelper.getReservationDao().queryBuilder().where().eq("offreId_id", offre).query();


        } catch (SQLException e) {
            Log.e("ListAnnounceAdapter", "Failed to get Reservation on offre " + offre.getTitre() + " : " + e);
        }

        if (reservations != null) {

            int nbReservation = reservations.size();
            int remainPlace = offre.getNbPrsn() - nbReservation;

            return remainPlace;


        } else {

            Toast.makeText(this.getActivity(), R.string.reservationNotFound, Toast.LENGTH_SHORT);
            return 0;
        }

    }

    // on get le particulier correspondant au user current
    private Particulier getCreator(Offre offre) {
        FeedMeOpenDatabaseHelper databaseHelper = FeedMeOpenDatabaseHelper.getHelper(getActivity());

        List<Particulier> particuliers = null;

        try {
            particuliers = databaseHelper.getParticuliersDao().queryBuilder().where().eq("idUser_id", offre.getIdUser()).query();
        } catch (SQLException e) {
            Log.e("BookAnnounceFragment", "Fail to get particulier from database");
        }

        if (particuliers != null && particuliers.size() == 1) {
            return particuliers.get(0);
        } else {
            Toast.makeText(getActivity(), R.string.particulierNotFound, Toast.LENGTH_SHORT);
            Log.d("BookAnnounceFragment", "Particulier not found or Multiple propositions");
            return null;
        }
    }
    //getting clicked offre
    private Offre getOffre(String idOffre) {

        FeedMeOpenDatabaseHelper databaseHelper = FeedMeOpenDatabaseHelper.getHelper(getActivity());
        long id = Long.parseLong(idOffre);

        List<Offre> offres = null;
        try {
            offres = databaseHelper.getOffresDao().queryBuilder().where().eq("id", id).query();
        } catch (SQLException e) {
            Log.e("BookAnnounceFragment", "Fail to get offre from database");
        }

        if (offres != null && offres.size() == 1) {
            return offres.get(0);

        } else {

            Toast.makeText(getActivity(), R.string.offreNotFound, Toast.LENGTH_SHORT);
            Log.d("BookAnnounceFragment", "Offre not found or Multiple propositions");
            return null;
        }
    }

}
