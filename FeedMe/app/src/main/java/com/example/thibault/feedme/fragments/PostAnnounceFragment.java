package com.example.thibault.feedme.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thibault.feedme.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PostAnnounceFragment extends Fragment {

    private ImageButton ibCalendar;
    private Calendar calendar;
    private int day;
    private int month;
    private int year;
    private EditText etDate;
    private Button bConfirm;
    private Spinner sType;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vPost = inflater.inflate(R.layout.fragment_post_announce, container, false);

        bConfirm = (Button) vPost.findViewById(R.id.Bconfirm);
        sType = (Spinner) vPost.findViewById(R.id.Stype);

        ArrayAdapter<String> adapter;
        List<String> list;

        list = new ArrayList<>();
        list.add("Item 1");
        list.add("Item 2");
        list.add("Item 3");
        list.add("Item 4");
        list.add("Item 5");

        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sType.setAdapter(adapter);

        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Annonce Postée", Toast.LENGTH_SHORT).show();

                manager = getActivity().getSupportFragmentManager();
                transaction = manager.beginTransaction();
                Fragment current = manager.findFragmentByTag("fragment");

                // Remplacer le fragment courant par le fragment home
                HomeFragment fHome = new HomeFragment();
                transaction.replace(current.getId(), fHome, "fragment");

                transaction.commit();

            }
        });


        //Date picker dialog

        View.OnClickListener onDateEntryClick = new View.OnClickListener() {
            public void onClick(View v) {
                // Open a date piker
                CreateDialog(0);
            }
        };

        ibCalendar = (ImageButton) vPost.findViewById(R.id.IBCalendar);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        etDate = (EditText) vPost.findViewById(R.id.ETDate);
        ibCalendar.setOnClickListener(onDateEntryClick);
        etDate.setOnClickListener(onDateEntryClick);


        // Inflate the layout for this fragment
        return vPost;
    }

    protected void CreateDialog(int id) {
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                etDate.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                        + selectedYear);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, year, month, day);

        datePickerDialog.show();
    }

}
