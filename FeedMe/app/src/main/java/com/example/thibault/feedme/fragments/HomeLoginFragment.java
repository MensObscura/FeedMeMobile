package com.example.thibault.feedme.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.thibault.feedme.R;
import com.example.thibault.feedme.activities.HomeLoginActivity;

public class HomeLoginFragment extends Fragment {

    private Button bLogin;
    private Button bSignIn;
    private static HomeLoginActivity myOnClickListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vLogin = inflater.inflate(R.layout.fragment_home_login, container, false);
        //Instanciate Button
        bLogin = (Button) vLogin.findViewById(R.id.Blogin);
        bSignIn = (Button) vLogin.findViewById(R.id.Bsignin);


        bLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Go to the Login
                login();
            }
        });

        bSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Go to the Login Page
                signIn();
            }
        });

        return vLogin;
    }


    public static void setMyOnClickListener(HomeLoginActivity myOnClickListener) {
        HomeLoginFragment.myOnClickListener = myOnClickListener;
    }

    public void login() {


        LoginFragment fragmentLogin = new LoginFragment();
        myOnClickListener.onMySignal(R.id.home, fragmentLogin);


    }


    /**
     * Intent intent = new Intent(this, MainActivity.class);
     * String message = "login";
     * intent.putExtra(EXTRA_MESSAGE, message);
     * startActivity(intent);
     * this.finish();
     */
    public void signIn() {


        SignInFragment fragmentSignIn = new SignInFragment();
        myOnClickListener.onMySignal(R.id.home, fragmentSignIn);
    }
}
