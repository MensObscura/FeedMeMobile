package com.example.thibault.feedme.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thibault.feedme.R;
import com.example.thibault.feedme.activities.MainActivity;

import java.util.Calendar;


public class SignInFragment extends Fragment {


    Button bValidate;
    private ImageButton ibCalendar;
    private Calendar calendar;
    private int day;
    private int month;
    private int year;
    private EditText etBirthDate;
    private TextView tvName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vSignIn = inflater.inflate(R.layout.fragment_signin, container, false);

        //Instanciate Button
        bValidate = (Button) vSignIn.findViewById(R.id.Bconfirm);

        View.OnClickListener onDateEntryClick = new View.OnClickListener() {
            public void onClick(View v) {
                // Open a date piker
                CreateDialog(0);
            }
        };
        bValidate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //go to the home
                launchApp();

            }
        });

        tvName = (TextView) vSignIn.findViewById(R.id.ETname);
        ibCalendar = (ImageButton) vSignIn.findViewById(R.id.IBCalendar);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        etBirthDate = (EditText) vSignIn.findViewById(R.id.ETBirthDate);
        ibCalendar.setOnClickListener(onDateEntryClick);
        etBirthDate.setOnClickListener(onDateEntryClick);

        return vSignIn;
    }

    private void launchApp() {

        Toast.makeText(getActivity(), "Hi, congrat you are in !", Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Hi, congrat you are in !", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        String message = tvName.getText().toString();
        intent.putExtra("HOME_LOGIN", message);
        startActivity(intent);
        getActivity().finish();
    }


    protected void CreateDialog(int id) {
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                etBirthDate.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                        + selectedYear);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, year, month, day);


        datePickerDialog.show();
    }


}

