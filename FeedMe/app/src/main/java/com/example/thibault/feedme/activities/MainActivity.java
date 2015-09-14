package com.example.thibault.feedme.activities;

import java.util.ArrayList;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thibault.feedme.R;
import com.example.thibault.feedme.fragments.BookAnnounceFragment;
import com.example.thibault.feedme.fragments.HomeFragment;
import com.example.thibault.feedme.fragments.PostAnnounceFragment;
import com.example.thibault.feedme.fragments.ProfilFragment;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout menuLayout;
    private ListView menuElementsList;
    private ActionBarDrawerToggle menuToggle;

    private CharSequence menuTitle = "Menu";
    private CharSequence activityTitle = "FeedMe";

    ArrayList<String> menu = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ajout premier fragment dans le layout nommé "mainLayout"
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.linearMain);

        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        Fragment myFrag = new HomeFragment();
        fragTransaction.add(mainLayout.getId(), myFrag, "fragment");
        fragTransaction.commit();

        menuLayout = (DrawerLayout) findViewById(R.id.menu_layout);
        menuElementsList = (ListView) findViewById(R.id.menu_elements);

        // set a custom shadow that overlays the main content when the drawer opens
        menuLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        // Liste des elements du menu
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                this, R.layout.element_menu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        menu.add(getString(R.string.profil));
        menu.add(getString(R.string.deposer));
        menu.add(getString(R.string.reserver));
        menu.add(getString(R.string.logout));
        for (int i = 0; i < menu.size(); i++) {
            adapter.add(menu.get(i));
        }
        menuElementsList.setAdapter(adapter);
        menuElementsList.setOnItemClickListener(new DrawerItemClickListener());
        // enable ActionBar app icon to behave as action to toggle menu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        menuToggle = new ActionBarDrawerToggle(this, /* host Activity */
                menuLayout, /* DrawerLayout object */
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(activityTitle);
                invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(menuTitle);
                invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };
        menuLayout.setDrawerListener(menuToggle);

        // If Application just started select Current TimeZone
        if (savedInstanceState == null) {


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Hide menu element when menu is opened
        boolean drawerOpen = menuLayout.isDrawerOpen(menuElementsList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.

        if (menuToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.drawable.action_search:
                Toast.makeText(this, R.string.search, Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment current = manager.findFragmentByTag("fragment");

        switch (position) {
            // Item 'Profil' du menu
            case 0:
                // Remplacer le fragment courant par le fragment profil
                ProfilFragment fProfil = new ProfilFragment();


                if (current != null) {

                    transaction.replace(current.getId(), fProfil, "fragment");

                    transaction.commit();
                }

                Toast.makeText(this, "profil", Toast.LENGTH_SHORT).show();
                break;
            // Item 'Proposer une offre' du menu
            case 1:
                // Remplacer le fragment courant par le fragment profil
                PostAnnounceFragment fPostAnnounce = new PostAnnounceFragment();


                if (current != null) {

                    transaction.replace(current.getId(), fPostAnnounce, "fragment");

                    transaction.commit();
                }
                Toast.makeText(this, "proposer", Toast.LENGTH_SHORT).show();
                break;
            // Item 'Reserver une offre' du menu
            case 2:
                BookAnnounceFragment fBookAnnounce = new BookAnnounceFragment();


                if (current != null) {

                    transaction.replace(current.getId(), fBookAnnounce, "fragment");

                    transaction.commit();
                }
                Toast.makeText(this, "reserver", Toast.LENGTH_LONG).show();
                break;
            // Item 'déconnexion' du menu
            case 3:

                Intent intent = new Intent(this, HomeLoginActivity.class);
                startActivity(intent);
                this.finish();
                Toast.makeText(this, "deco", Toast.LENGTH_LONG).show();
                break;
            default:

        }

        menuElementsList.setItemChecked(position, true);
        setTitle(menu.get(position));
        menuLayout.closeDrawer(menuElementsList);
    }


    @Override
    public void setTitle(CharSequence title) {
        activityTitle = title;
        getSupportActionBar().setTitle(activityTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        menuToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        menuToggle.onConfigurationChanged(newConfig);
    }

}