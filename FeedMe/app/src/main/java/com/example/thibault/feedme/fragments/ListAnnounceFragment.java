package com.example.thibault.feedme.fragments;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import com.example.thibault.feedme.adapters.ListAnnounceAdapter;

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
        announces.setAdapter(new ListAnnounceAdapter(getActivity()));

        announces.setOnItemClickListener(new AdapterView.OnItemClickListener() {

             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 manager = getActivity().getSupportFragmentManager();
                 transaction = manager.beginTransaction();
                 Fragment currentFragment = manager.findFragmentByTag("fragment");

                 if (currentFragment != null) {

                     // Remplacer le fragment courant par le fragment reserver
                     BookAnnounceFragment fBook = new BookAnnounceFragment();
                     Bundle bundle = new Bundle();
                     long idOffre = ((ListAnnounceAdapter) announces.getAdapter()).getOffreIdFromPosition(position);
                     bundle.putString("offre", "" + idOffre);
                     fBook.setArguments(bundle);
                     transaction.replace(currentFragment.getId(), fBook, "fragment");

                     transaction.commit();
                     getActivity().setTitle(getString(R.string.reservation));
                 }
             }
        });

        return fList;
    }
}
