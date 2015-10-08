package com.example.thibault.feedme.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

    /**
     * Selection "Proposer un repas" du menu d'accueil
     */
    private void launchFromMeal() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment currentFragment = manager.findFragmentByTag("fragment");

            // Remplacer le fragment courant par le fragment partager
            PostAnnounceFragment fPost = new PostAnnounceFragment();

            if (currentFragment != null) {
                transaction.replace(currentFragment.getId(), fPost, "fragment");
                transaction.commit();
                getActivity().setTitle(getString(R.string.deposer));
            }
    }

    /**
     * Selection "Rechercher un repas" du menu d'accueil
     */
    private void launchListMeal() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment currentFragment = manager.findFragmentByTag("fragment");

        // Remplacer le fragment courant par le fragment partager
        ListAnnounceFragment fList = new ListAnnounceFragment();

        if (currentFragment != null) {
            transaction.replace(currentFragment.getId(), fList, "fragment");
            transaction.commit();
            getActivity().setTitle(getString(R.string.rechercher));
        }
    }
}
