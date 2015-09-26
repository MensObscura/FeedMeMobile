package com.example.thibault.feedme.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.thibault.feedme.Persistence.Authentification;
import com.example.thibault.feedme.Persistence.Particulier;
import com.example.thibault.feedme.Persistence.Role;
import com.example.thibault.feedme.Persistence.User;
import com.example.thibault.feedme.R;
import com.example.thibault.feedme.activities.MainActivity;
import com.example.thibault.feedme.databaseHelpers.FeedMeOpenDatabaseHelper;


import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;


public class SignInFragment extends Fragment {


    private Button bValidate;
    private ImageButton ibCalendar;
    private Calendar calendar;

    private int day;
    private int month;
    private int year;

    private EditText etBirthDate;
    private EditText etLastName;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etBirthdate;

    private FeedMeOpenDatabaseHelper database;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vSignIn = inflater.inflate(R.layout.fragment_signin, container, false);

        //Instanciate Button
        this.bValidate = (Button) vSignIn.findViewById(R.id.Bconfirm);

        View.OnClickListener onDateEntryClick = new View.OnClickListener() {
            public void onClick(View v) {
                // Open a date piker
                CreateDialog(0);
            }
        };
        this.bValidate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //go to the home
                try {
                    launchApp();
                } catch (SQLException e) {
                    Log.e("Sign in Fragement","Echec de l'enregistrement en bdd des données de l'utilisateur");
                }

            }
        });


        this.etName = (EditText) vSignIn.findViewById(R.id.ETname);
        this.etLastName = (EditText) vSignIn.findViewById(R.id.ETlastname);
        this.etPassword = (EditText) vSignIn.findViewById(R.id.ETpassword);
        this.etConfirmPassword = (EditText) vSignIn.findViewById(R.id.ETconfirmpassword);
        this.etEmail = (EditText) vSignIn.findViewById((R.id.ETmail));

        this.ibCalendar = (ImageButton) vSignIn.findViewById(R.id.IBCalendar);
        this.calendar = Calendar.getInstance();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);
        this.etBirthDate = (EditText) vSignIn.findViewById(R.id.ETBirthDate);
        this.ibCalendar.setOnClickListener(onDateEntryClick);
        this.etBirthDate.setOnClickListener(onDateEntryClick);

        return vSignIn;
    }

    private void launchApp() throws SQLException {

        String message = this.etName.getText().toString();

        this.database = FeedMeOpenDatabaseHelper.getHelper(this.getActivity());

        String nom = this.etLastName.getText().toString();
        String prenom = this.etName.getText().toString();
        String email = this.etEmail.getText().toString();
        String password = this.etPassword.getText().toString();
        String confirm = this.etConfirmPassword.getText().toString();
        Date birth = this.calendar.getTime();

        if (password.equals(confirm)) {

            if(database != null) {
                User user = new User(nom, email);
                this.database.getUsersDao().create(user);


                this.database.getParticuliersDao().create(new Particulier(prenom, birth, user));

                Role role;
                if(this.database.getRolesDao().queryForAll().size() != 0) {
                     role =this.database.getRolesDao().queryForAll().get(0);
                }else{
                    role = new Role("ROLE_PARTICULIER");
                    this.database.getRolesDao().create(role);
                }



                this.database.getAuthentificationDao().create(new Authentification(password, role, user));

                Toast.makeText(getActivity(), "Hi, " + nom + " congrat you are in !", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), MainActivity.class);

                Log.d("sign in", this.database.getUsersDao().objectToString(user));

                intent.putExtra("HOME_LOGIN", message);
                startActivity(intent);
                getActivity().finish();
            }else {
                Toast.makeText(getActivity(), "database null", Toast.LENGTH_SHORT).show();
            }
        } else {

            Toast.makeText(getActivity(), "Les password ne coresspondent pas", Toast.LENGTH_SHORT).show();

            etPassword.setBackgroundColor(Color.RED);

        }

    }


    protected void CreateDialog(int id) {
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                etBirthDate.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                        + selectedYear);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, this.year, this.month, this.day);


        datePickerDialog.show();
    }


}

