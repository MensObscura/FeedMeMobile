package com.example.thibault.feedme.fragments;


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
import android.widget.EditText;
import android.widget.Toast;

import com.example.thibault.feedme.Persistence.Authentification;
import com.example.thibault.feedme.Persistence.User;
import com.example.thibault.feedme.R;
import com.example.thibault.feedme.activities.MainActivity;
import com.example.thibault.feedme.databaseHelpers.FeedMeOpenDatabaseHelper;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;


public class LoginFragment extends Fragment {

    Button bValidate;
    EditText etName;
    EditText etPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vLogin = inflater.inflate(R.layout.fragment_login, container, false);

        //Instanciate view
        etName = (EditText) vLogin.findViewById(R.id.ETid);
        etPassword = (EditText) vLogin.findViewById(R.id.ETloginpassword);
        bValidate = (Button) vLogin.findViewById(R.id.Bvalid);

        bValidate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Go to home
                try {
                    launchApp();
                } catch (SQLException e) {
                    Log.e("Login Fragment", "echec login" + e);
                }
            }
        });

        // verification email valid
        this.etName.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean valid = Pattern.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$",s);

                if(valid){
                    etName.setBackgroundColor(etName.getDrawingCacheBackgroundColor());
                    bValidate.setEnabled(true);
                }else{
                    etName.setBackgroundColor(Color.RED);
                  //  Toast.makeText(getActivity(), R.string.notanemail, Toast.LENGTH_SHORT).show();
                    bValidate.setEnabled(false);
                }
            }
        });
        return vLogin;
    }

    private void launchApp() throws SQLException {

        if (authentification()) {
            Toast.makeText(getActivity(), "Hi, congrat you are in !", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            String message = etName.getText().toString();
            intent.putExtra("HOME_LOGIN", message);
            startActivity(intent);
            getActivity().finish();
        }
    }

    /**
     * Verifie si la combinaison login/password existe en base
     * @return True si l'utilisateur est reconnu, False sinon
     * @throws SQLException
     */
    public boolean authentification() throws SQLException {
        FeedMeOpenDatabaseHelper database = FeedMeOpenDatabaseHelper.getHelper(this.getActivity());

        String email = etName.getText().toString();
        String password = etPassword.getText().toString();
        email = email.trim();
        password = password.trim();
        //si champ non vides on verifie les ID dans la base
        if (email.length() > 0 && password.length() > 0) {

            List<User> users = database.getUsersDao().queryBuilder().where().eq("email", email).query();
            List<User> users2 = database.getUsersDao().queryForAll();

            Log.i("LOGIN", "\n\n********************* LOGIN *********************\n\n");
            /// DEBUG
            for(User u : users2) {
                Log.i("Login", "USER : "+u.toString());
            }
            if(users2.isEmpty()){
                Log.i("Login", "Pas d'utilisateurs en base =(");
            }

            if (users.size() == 1) {
                List<Authentification> auth = database.getAuthentificationDao().queryBuilder().where().eq("idUser_id", users.get(0)).and().eq("password", password).query();
                if (auth.size() == 1) {
                    return true; // On a bien trouvé un seul utilisateur
                }
            }
            Toast.makeText(getActivity(), R.string.loginIncorrect, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), R.string.champVide, Toast.LENGTH_SHORT).show();

            if (password.isEmpty()) {
                this.etPassword.setBackgroundColor(Color.RED);
            }
            if (email.isEmpty()){
                this.etName.setBackgroundColor(Color.RED);
            }
        }
        return false;
    }

}
