package com.example.thibault.feedme.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.thibault.feedme.R;

public class HomeFragment extends Fragment {

    Button bShareMeat;
    Button bFindMeat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vHome = inflater.inflate(R.layout.fragment_home, container, false);
        //Instanciate Button

        bFindMeat = (Button) vHome.findViewById(R.id.Bfindmeat);
        bShareMeat = (Button) vHome.findViewById(R.id.Bsharemeat);

        bShareMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFromMeal();
            }
        });

        bFindMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchListMeal();

            }
        });

        return vHome;
    }

    private void launchFromMeal() {



    }
    private void launchListMeal() {


    }
}
