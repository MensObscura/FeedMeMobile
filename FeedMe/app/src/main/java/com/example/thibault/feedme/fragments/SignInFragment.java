package com.example.thibault.feedme.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


public class SignInFragment extends Fragment {


    private Button bValidate;
    private ImageButton ibCalendar;
    private Calendar calendar;

    private boolean allowConfirm;

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
    private List<EditText> editable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vSignIn = inflater.inflate(R.layout.fragment_signin, container, false);

        //Instanciate Button
        this.bValidate = (Button) vSignIn.findViewById(R.id.Bconfirm);

        //ajout du onClickListener
        this.bValidate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //go to the home
                try {
                    //on Lance l'application
                    launchApp();
                } catch (SQLException e) {
                    Log.e("Sign in Fragement", "Echec de l'enregistrement en bdd des données de l'utilisateur : " + e);
                }

            }
        });

        //instanciation des inputs (editext)
        this.etName = (EditText) vSignIn.findViewById(R.id.ETname);
        this.etLastName = (EditText) vSignIn.findViewById(R.id.ETlastname);
        this.etPassword = (EditText) vSignIn.findViewById(R.id.ETpassword);
        this.etConfirmPassword = (EditText) vSignIn.findViewById(R.id.ETconfirmpassword);
        this.etEmail = (EditText) vSignIn.findViewById((R.id.ETmail));
        this.etBirthDate = (EditText) vSignIn.findViewById(R.id.ETBirthDate);

        //ajout des editText dans la liste
        this.editable = new ArrayList<EditText>();
        this.editable.add(this.etName);
        this.editable.add(this.etLastName);
        this.editable.add(this.etPassword);
        this.editable.add(this.etConfirmPassword);
        this.editable.add(this.etEmail);
        this.editable.add(this.etBirthDate);

        //Verifcation email valid
        this.etEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean valid = Pattern.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$", s);

                if (valid) {
                    etEmail.setHintTextColor(Color.BLACK);
                    bValidate.setEnabled(true);

                } else {
                    etEmail.setHintTextColor(Color.RED);
                    bValidate.setEnabled(false);
                }
            }
        });

        //Intanciation de l'image button calendrier et préparation des entrées de dates
        this.ibCalendar = (ImageButton) vSignIn.findViewById(R.id.IBCalendar);
        this.calendar = Calendar.getInstance();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);

        //OnClick sur champs et image de date.
        View.OnClickListener onDateEntryClick = new View.OnClickListener() {
            public void onClick(View v) {
                // Open a date piker
                CreateDialog(0);
            }
        };

        // ajout de datelistener
        this.ibCalendar.setOnClickListener(onDateEntryClick);
        this.etBirthDate.setOnClickListener(onDateEntryClick);

        return vSignIn;
    }

    private void launchApp() throws SQLException {
        this.database = FeedMeOpenDatabaseHelper.getHelper(this.getActivity());

        String nom = this.etLastName.getText().toString();
        String prenom = this.etName.getText().toString();
        String email = this.etEmail.getText().toString();
        String password = this.etPassword.getText().toString();
        String confirm = this.etConfirmPassword.getText().toString();
        Date birth = this.calendar.getTime();

        //on verifie que les champs sont rempli
        this.allowConfirm = true;
        for (EditText e : this.editable) {
            if (e.getText().toString().trim().length() == 0) {
                this.allowConfirm = false;
                e.setHintTextColor(Color.RED);;
            }
        }
        // si le mot de passe et la confirmation sont identique et que tout les champs sont remplis
        if (password.equals(confirm) && allowConfirm) {
            //si la base est créée
            if (database != null) {
                //On créé un user et sont role, puis une nouvelle authentification et on lance l'app
                User user = new User(nom, email);
                this.database.getUsersDao().create(user);

                this.database.getParticuliersDao().create(new Particulier(prenom, birth, user));

                Role role;
                if (this.database.getRolesDao().queryForAll().size() != 0) {
                    role = this.database.getRolesDao().queryForAll().get(0);
                } else {
                    role = new Role("ROLE_PARTICULIER");
                    this.database.getRolesDao().create(role);
                }

                this.database.getAuthentificationDao().create(new Authentification(password, role, user));

                Toast.makeText(getActivity(), "Hi, " + nom + " congrat you are in !", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                String message = email;
                intent.putExtra("HOME_LOGIN", message);
                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "database null", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Si pas de champs de vide, alors on signal que le mot de passe et la confirmation sont différents, sinon on signal les champs vides
            if (allowConfirm) {
                Toast.makeText(getActivity(), R.string.passwordIncompatible, Toast.LENGTH_SHORT).show();
                etPassword.setTextColor(Color.RED);
                etConfirmPassword.setTextColor(Color.RED);
            } else {
                Toast.makeText(getActivity(), R.string.champVide, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Ici encore on vous laisse le choix dans la date.
    protected void CreateDialog(int id) {
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                etBirthDate.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                        + selectedYear);

                calendar.set(selectedYear, selectedMonth, selectedDay);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, this.year, this.month, this.day);
        Calendar cal = Calendar.getInstance();
        //29 février Tadammm
        if (cal.get(Calendar.MONTH) == Calendar.FEBRUARY && cal.get(Calendar.DAY_OF_MONTH) == 29) {
            cal.set(Calendar.MONTH, Calendar.MARCH);
            cal.set(Calendar.DAY_OF_MONTH, 1);
        }
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 18);

        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        Log.d("SignInFragment", "" + datePickerDialog.getDatePicker().getMaxDate());
        datePickerDialog.show();
    }
}

