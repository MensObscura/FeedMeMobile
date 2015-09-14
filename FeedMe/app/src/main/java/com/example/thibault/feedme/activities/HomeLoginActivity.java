package com.example.thibault.feedme.activities;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.thibault.feedme.MyOnClickListener;
import com.example.thibault.feedme.R;
import com.example.thibault.feedme.fragments.HomeLoginFragment;

public class HomeLoginActivity extends FragmentActivity implements MyOnClickListener {

    private static final String EXTRA_MESSAGE = "com.example.thibault.feedme";
    private Button bLogin;
    private Button bSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_login);

        //Fragment
        HomeLoginFragment.setMyOnClickListener(this);




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMySignal(int lNum, Fragment fragment) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();



        //replace your current container being most of the time as FrameLayout
        transaction.replace(lNum,fragment);
        transaction.commit();
    }
}
