package com.example.thibault.feedme.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.thibault.feedme.R;

public class BookAnnounceFragment extends Fragment {

    private Button bConfirm;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View vBook = inflater.inflate(R.layout.fragment_book_announce, container, false);

        bConfirm = (Button) vBook.findViewById(R.id.Bconfirm_book);

        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Couvert Réservé", Toast.LENGTH_SHORT).show();

                manager = getActivity().getSupportFragmentManager();
                transaction = manager.beginTransaction();
                Fragment current = manager.findFragmentByTag("fragment");

                // Remplacer le fragment courant par le fragment home
                HomeFragment fHome = new HomeFragment();
                transaction.replace(current.getId(), fHome, "fragment");

                transaction.commit();
                getActivity().setTitle(getString(R.string.app_name));
            }
        });
        return vBook;
    }


}
