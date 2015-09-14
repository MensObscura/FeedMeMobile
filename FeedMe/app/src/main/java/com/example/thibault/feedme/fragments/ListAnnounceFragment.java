package com.example.thibault.feedme.fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.thibault.feedme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListAnnounceFragment extends Fragment {

    GridView announces;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fList = inflater.inflate(R.layout.fragment_list_announce, container, false);


        announces = (GridView) fList.findViewById(R.id.gridAnnounce);


        announces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                                 Fragment current = manager.findFragmentByTag("fragment");


                                                 if (current != null) {


                                                     // Remplacer le fragment courant par le fragment reserver
                                                     BookAnnounceFragment fBook = new BookAnnounceFragment();
                                                     transaction.replace(current.getId(), fBook, "fragment");

                                                     transaction.commit();
                                                 }
                                             }
                                         }
        );

        return fList;
    }


}
