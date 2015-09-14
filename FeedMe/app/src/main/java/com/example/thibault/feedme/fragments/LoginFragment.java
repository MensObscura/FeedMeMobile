package com.example.thibault.feedme.fragments;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thibault.feedme.R;
import com.example.thibault.feedme.activities.MainActivity;



public class LoginFragment extends Fragment {


    Button bValidate;
    TextView tvName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vLogin = inflater.inflate(R.layout.fragment_login, container, false);

        //Instanciate view

        tvName = (TextView) vLogin.findViewById(R.id.ETid);
        bValidate = (Button) vLogin.findViewById(R.id.Bvalid);

        bValidate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Go to home
                launchApp();
            }
        });


        return vLogin;
    }

    private void launchApp() {


        Toast.makeText(getActivity(), "Hi, congrat you are in !", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        String message = tvName.getText().toString();
        intent.putExtra("HOME_LOGIN",message);
        startActivity(intent);
        getActivity().finish();
    }



}
