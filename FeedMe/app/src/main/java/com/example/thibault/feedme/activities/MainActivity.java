package com.example.thibault.feedme.activities;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thibault.feedme.Persistence.User;
import com.example.thibault.feedme.R;
import com.example.thibault.feedme.databaseHelpers.FeedMeOpenDatabaseHelper;
import com.example.thibault.feedme.fragments.BookAnnounceFragment;
import com.example.thibault.feedme.fragments.HomeFragment;
import com.example.thibault.feedme.fragments.ListAnnounceFragment;
import com.example.thibault.feedme.fragments.PostAnnounceFragment;
import com.example.thibault.feedme.fragments.ProfilFragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ArrayList<String> menu = new ArrayList<String>();
    private DrawerLayout menuLayout;
    private ListView menuElementsList;
    private ActionBarDrawerToggle menuToggle;
    private CharSequence menuTitle = "Menu";
    private CharSequence activityTitle = "FeedMe";
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ajout premier fragment dans le layout nommé "mainLayout"
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.linearMain);
        this.setFragment(mainLayout);

        // Initialisation des composants du menu
        this.initComponents();

        // Construction du contenu du menu
        this.fillMenu();

        // On sauvegarde le current user
        try {
            this.currentUser = getCurrentUser();
        } catch (SQLException e) {
            Log.e("MainActivity", "Fail gettingUser : " + e);
        }
    }

    /**
     * Initialisation des composants :
     * layout du menu
     * liste des elements du menu
     */
    private void initComponents() {
        this.menuLayout = (DrawerLayout) findViewById(R.id.menu_layout);
        this.menuElementsList = (ListView) findViewById(R.id.menu_elements);
    }


    /**
     * Construction du contenu du menu
     */
    private void fillMenu() {
        // set a custom shadow that overlays the main content when the drawer opens
        this.menuLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        // Liste des elements du menu
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                this, R.layout.element_menu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.menu.add(getString(R.string.profil));
        this.menu.add(getString(R.string.deposer));
        this.menu.add(getString(R.string.rechercher));
        this.menu.add(getString(R.string.logout));
        for (int i = 0; i < menu.size(); i++) {
            adapter.add(menu.get(i));
        }

        //On set l'adapter
        this.menuElementsList.setAdapter(adapter);

        // Evenement au clic : selection de l'element du menu
        this.menuElementsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        // enable ActionBar app icon to behave as action to toggle menu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        this.initToggle();

        this.menuLayout.setDrawerListener(menuToggle);
    }

    private void initToggle() {
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        this.menuToggle = new ActionBarDrawerToggle(this, /* host Activity */
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

    }

    private void setFragment(LinearLayout mainLayout) {
        // on fait l'ajout du fragment dans le layout donné
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Fragment myFrag = new HomeFragment();
        transaction.add(mainLayout.getId(), myFrag, "fragment");
        transaction.commit();
    }

    /**
     * Retourne l'utilisateur courant
     * @return User - l'utilisateur courant
     * @throws SQLException
     */
    public User getCurrentUser() throws SQLException {
        if (currentUser == null) {
            FeedMeOpenDatabaseHelper database = FeedMeOpenDatabaseHelper.getHelper(this);

            //Getting Current User
            String email = this.getIntent().getStringExtra("HOME_LOGIN");

            // Liste des utilisateurs en base
            List<User> users = database.getUsersDao().queryBuilder().where().eq("email", email).query();

            // Si on trouve bien un seul utilisateur, alors on le retourne
            if (users.size() == 1) {
                return users.get(0);
            }

            // Sinon il n'est pas trouve
            Toast.makeText(this, R.string.notFound, Toast.LENGTH_LONG).show();
        } else {
            return this.currentUser;
        }
        return null;
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

    /**
     * Selectionne un element du menu et remplace le fragment actuel par celui choisi
     * @param position la position de l'element dans le menu
     */
    private void selectItem(int position) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment currentFragment = manager.findFragmentByTag("fragment");

        switch (position) {
            // Item 'Profil' du menu
            case 0:
                // Remplacer le fragment courant par le fragment profil
                ProfilFragment fProfil = new ProfilFragment();
                // si le fragment courant existe
                if (currentFragment != null) {
                    transaction.replace(currentFragment.getId(), fProfil, "fragment");
                    transaction.commit();
                }
                break;

            // Item 'Proposer une offre' du menu
            case 1:
                // Remplacer le fragment courant par le fragment PostAnnounce
                PostAnnounceFragment fPostAnnounce = new PostAnnounceFragment();
                // si le fragment existe
                if (currentFragment != null) {
                    transaction.replace(currentFragment.getId(), fPostAnnounce, "fragment");
                    transaction.commit();
                }
                break;

            // Item 'Lister les offres' du menu
            case 2:
                ListAnnounceFragment fListAnnounce = new ListAnnounceFragment();

                if (currentFragment != null) {
                    transaction.replace(currentFragment.getId(), fListAnnounce, "fragment");
                    transaction.commit();
                }
                break;

            // Item 'déconnexion' du menu
            case 3:
                Intent intent = new Intent(this, HomeLoginActivity.class);
                startActivity(intent);
                this.finish();
                Toast.makeText(this, "Bybye", Toast.LENGTH_LONG).show();
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

    // 2.0 and above on button back pressed
    @Override
    public void onBackPressed() {

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment currentFragment = manager.findFragmentByTag("fragment");

        // Si le fragment courant existe, qu'il n'est pas celui de l'accueil
        // et n'est pas celuui de la liste des annonces
        if (currentFragment != null && !currentFragment.getClass().equals(HomeFragment.class)
                && !currentFragment.getClass().equals(ListAnnounceFragment.class)) {

            // On remplace le fragment courant par le fragment Home
            HomeFragment fHome = new HomeFragment();
            transaction.replace(currentFragment.getId(), fHome, "fragment");

            transaction.commit();
            setTitle(getString(R.string.app_name));
        }
        // Sinon si le fragment existe et qu'il n'est pas celui de l'accueil
        else if (currentFragment != null && !currentFragment.getClass().equals(HomeFragment.class)) {

            // On remplace le fragment courant par le fragment List
            ListAnnounceFragment fList = new ListAnnounceFragment();
            transaction.replace(currentFragment.getId(), fList, "fragment");

            transaction.commit();
            setTitle(getString(R.string.rechercher));
        } else {
            super.onBackPressed();
        }
    }

    // Before 2.0 on button back pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            manager = getSupportFragmentManager();
            transaction = manager.beginTransaction();
            Fragment current = manager.findFragmentByTag("fragment");

            if (current != null && !current.getClass().equals(HomeFragment.class)
                    && !current.getClass().equals(BookAnnounceFragment.class)) {

                // Remplacer le fragment courant par le fragment home
                HomeFragment fHome = new HomeFragment();
                transaction.replace(current.getId(), fHome, "fragment");

                transaction.commit();
                setTitle(getString(R.string.app_name));

                return true;
            } else if (current != null && !current.getClass().equals(HomeFragment.class)) {

                // Remplacer le fragment courant par le fragment List
                ListAnnounceFragment fList = new ListAnnounceFragment();
                transaction.replace(current.getId(), fList, "fragment");
                setTitle(getString(R.string.rechercher));
                transaction.commit();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}