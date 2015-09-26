package com.example.thibault.feedme.fragments;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thibault.feedme.Persistence.Authentification;
import com.example.thibault.feedme.Persistence.User;
import com.example.thibault.feedme.R;
import com.example.thibault.feedme.activities.MainActivity;
import com.example.thibault.feedme.databaseHelpers.FeedMeOpenDatabaseHelper;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;


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


        return vLogin;
    }

    private void launchApp() throws SQLException {

        if(etPassword.getText().length()==0){

        }else if(authentification()) {


            Toast.makeText(getActivity(), "Hi, congrat you are in !", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            String message = etName.getText().toString();
            intent.putExtra("HOME_LOGIN", message);
            startActivity(intent);
            getActivity().finish();
        }else{
            Toast.makeText(getActivity(), "Combinaison mot de passse identifiant inconnu !", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean authentification() throws SQLException {

        FeedMeOpenDatabaseHelper database = FeedMeOpenDatabaseHelper.getHelper(this.getActivity());

        String email = etName.getText().toString();
        String password = etPassword.getText().toString();
        List<User> users = database.getUsersDao().queryBuilder().where().eq("email",email).query();

        if(users.size() == 1) {

            List<Authentification> auth = database.getAuthentificationDao().queryBuilder().where().eq("idUser_id",users.get(0)).and().eq("password",password).query();
            if(auth.size()==1){
                return true;
            }
        }
        return false;
    }

}
