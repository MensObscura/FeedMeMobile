package com.example.thibault.feedme.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thibault.feedme.Persistence.Particulier;
import com.example.thibault.feedme.Persistence.User;
import com.example.thibault.feedme.R;
import com.example.thibault.feedme.activities.MainActivity;
import com.example.thibault.feedme.databaseHelpers.FeedMeOpenDatabaseHelper;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    private TextView tvNom;
    private TextView tvPrenom;
    private TextView tvEmail;
    private TextView tvAge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vProfil = inflater.inflate(R.layout.fragment_profil, container, false);

        // intanciate TextView
        this.tvNom = (TextView) vProfil.findViewById(R.id.TVlastname);
        this.tvPrenom = (TextView) vProfil.findViewById(R.id.TVname);
        this.tvEmail = (TextView) vProfil.findViewById(R.id.TVemail);
        this.tvAge = (TextView) vProfil.findViewById(R.id.TVage);

        //Set value in text view;
        this.setValue();


        return vProfil;
    }

    public void setValue() {

        try {
            User user = ((MainActivity) this.getActivity()).getCurrentUser();
            Particulier particulier;

            Calendar calendar = Calendar.getInstance();


            FeedMeOpenDatabaseHelper database = FeedMeOpenDatabaseHelper.getHelper(this.getActivity());


            //Getting Current Particulier

            List<Particulier> particuliers = database.getParticuliersDao().queryBuilder().where().eq("idUser_id", user).query();


            if (particuliers.size() == 1) {

                particulier = particuliers.get(0);
                calendar.setTime(particulier.getDateNaissance());
                Log.d("ProfilFragment",calendar.get(calendar.YEAR)+"");
                Log.d("ProfilFragment",particulier.getDateNaissance().get()+"");
                int age = Calendar.getInstance().get(Calendar.YEAR) - calendar.get(calendar.YEAR);

                //fill textView
                this.tvNom.setText(user.getNom());
                this.tvPrenom.setText(particulier.getPrenom());
                this.tvEmail.setText(user.getEmail());
                this.tvAge.setText("" + age);

            } else {

                Toast.makeText(this.getActivity(), R.string.notFound, Toast.LENGTH_LONG).show();
            }


        } catch (SQLException e) {
            Log.e("ProfilFragment", "GettingUserFail : " + e);
        }

    }

}
