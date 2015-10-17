package com.example.thibault.feedme.fragments;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thibault.feedme.Persistence.Adresse;
import com.example.thibault.feedme.Persistence.Offre;
import com.example.thibault.feedme.Persistence.Pays;
import com.example.thibault.feedme.Persistence.TypeCuisine;
import com.example.thibault.feedme.Persistence.Ville;
import com.example.thibault.feedme.R;
import com.example.thibault.feedme.activities.MainActivity;
import com.example.thibault.feedme.databaseHelpers.FeedMeOpenDatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


public class PostAnnounceFragment extends Fragment {

    private ImageButton ibCalendar;
    private Calendar calendar;

    private EditText etDate;
    private Button bConfirm;
    private Spinner sType;
    private EditText etTitle;
    private EditText etNbPlaces;
    private EditText etPrice;
    private EditText etStreet;
    private EditText etCity;
    private EditText etCodePostal;
    private Spinner sPays;
    private EditText etMenu;
    private EditText etDuration;
    private EditText etAge;
    private EditText etBrief;
    private CheckBox cbPets;

    private int day;
    private int month;
    private int year;

    private List<EditText> neededComponants;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private FeedMeOpenDatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vPost = inflater.inflate(R.layout.fragment_post_announce, container, false);

        //instantiate components
        this.bConfirm = (Button) vPost.findViewById(R.id.Bconfirm);
        this.sType = (Spinner) vPost.findViewById(R.id.Stype);
        this.ibCalendar = (ImageButton) vPost.findViewById(R.id.IBCalendar);
        this.etDate = (EditText) vPost.findViewById(R.id.ETDate);
        this.etTitle = (EditText) vPost.findViewById(R.id.ETtitle);
        this.etNbPlaces = (EditText) vPost.findViewById((R.id.ETnbpeople));
        this.etPrice = (EditText) vPost.findViewById(R.id.ETprice);
        this.etStreet = (EditText) vPost.findViewById(R.id.ETstreet);
        this.etCity = (EditText) vPost.findViewById(R.id.ETcity);
        this.etCodePostal = (EditText) vPost.findViewById(R.id.ETcp);
        this.sPays = (Spinner) vPost.findViewById(R.id.Spays);
        this.etMenu = (EditText) vPost.findViewById(R.id.ETmenu);
        this.etDuration = (EditText) vPost.findViewById(R.id.ETduration);
        this.etAge = (EditText) vPost.findViewById(R.id.ETageback);
        this.etBrief = (EditText) vPost.findViewById(R.id.ETnotes);
        this.cbPets = (CheckBox) vPost.findViewById(R.id.CBpets);

        this.initListComponant();

        //Add item to spinners
        this.initSpinner();

        this.initAgeEditText();

        this.bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (insertOffreInDatabase()) {
                    Toast.makeText(getActivity(), "Annonce Postée", Toast.LENGTH_SHORT).show();
                    manager = getActivity().getSupportFragmentManager();
                    transaction = manager.beginTransaction();
                    Fragment current = manager.findFragmentByTag("fragment");

                    // Remplacer le fragment courant par le fragment home
                    HomeFragment fHome = new HomeFragment();
                    transaction.replace(current.getId(), fHome, "fragment");

                    transaction.commit();
                    getActivity().setTitle(getString(R.string.app_name));
                }
            }


        });


        //Date picker dialog
        View.OnClickListener onDateEntryClick = new View.OnClickListener() {
            public void onClick(View v) {
                // Open a date piker
                CreateDialog(0);
            }
        };

        this.calendar = Calendar.getInstance();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);

        //add onclick listener on edit text and image button
        this.ibCalendar.setOnClickListener(onDateEntryClick);
        this.etDate.setOnClickListener(onDateEntryClick);

        // Inflate the layout for this fragment
        return vPost;
    }

    /**
     * Initialisation du champ pour l'age de l'utilisateur
     */
    private void initAgeEditText() {
        this.etAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean valid = Pattern.matches("[0-9]{1,3}-[0-9]{1,3}", s);
                if (valid) {
                    String[] ages = s.toString().trim().split("-");

                    int min = Integer.parseInt(ages[0]);
                    int max = Integer.parseInt(ages[1]);
                    if (max > min) {
                        etAge.setBackgroundColor(etAge.getDrawingCacheBackgroundColor());
                        bConfirm.setEnabled(true);
                    } else {
                        Toast.makeText(getActivity(), "max must be gretter than min", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    etAge.setBackgroundColor(Color.RED);
                    bConfirm.setEnabled(false);
                }
            }
        });
    }

    /**
     * Initialisation de la liste des elements du fragment
     */
    private void initListComponant() {
        this.neededComponants = new ArrayList<EditText>();
        //needed information
        this.neededComponants.add(etTitle);
        this.neededComponants.add(etNbPlaces);
        this.neededComponants.add(etPrice);
        this.neededComponants.add(etStreet);
        this.neededComponants.add(etCity);
        this.neededComponants.add(etCodePostal);
        this.neededComponants.add(etMenu);
        this.neededComponants.add(etDate);
    }

    /**
     * Initialisation des spinners du fragment
     */
    private void initSpinner() {
        this.initSpinnerPays();
        this.initSpinnerTypeCuisine();
    }

    /**
     * Initialisation du spinner pour les types de cuisine
     */
    private void initSpinnerTypeCuisine() {
        ArrayAdapter<String> adapter = null;
        List<String> list = new ArrayList<String>();
        List<TypeCuisine> typeCuisines = null;

        FeedMeOpenDatabaseHelper database = FeedMeOpenDatabaseHelper.getHelper(this.getActivity());

        // On recupere tous les types de cuisine en base
        try {
            typeCuisines = database.getTypeCuisinesDao().queryForAll();
        } catch (SQLException e) {
            Log.e("PostAnnouceFragment", "Echec getting type de cuisine from db" + e);
        }

        // Pour chaque type de cuisine, s'il y a, on ajoute le type dans le spinner
        if (typeCuisines != null) {
            if (typeCuisines.size() > 0) {

                for (TypeCuisine t : typeCuisines) {
                    list.add(t.getTypeCuisine());
                }
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            } else {
                Log.e("PostAnnouceFragment", "TypeCuisine List is empty");
            }
        } else {
            Log.e("PostAnnouceFragment", "TypeCuisine List is null");
        }

        this.sType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.sType.setAdapter(adapter);
    }

    /**
     * Initialisation du spinner pour les pays
     */
    private void initSpinnerPays() {
        ArrayAdapter<String> adapter = null;
        List<String> list = new ArrayList<String>();
        List<Pays> pays = null;

        FeedMeOpenDatabaseHelper database = FeedMeOpenDatabaseHelper.getHelper(this.getActivity());

        // On recupere toutes les entrees dans la table PAYS
        try {
            pays = database.getPaysDao().queryForAll();
        } catch (SQLException e) {
            Log.e("PostAnnouceFragment", "Echec getting country from db" + e);
        }

        // Pour chaque pays, si cette liste n'est pas nulle, on ajoute le nom du pays au spinner
        if (pays != null) {
            if (pays.size() > 0) {

                for (Pays p : pays) {
                    list.add(p.getNom());
                }
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            } else {
                Log.e("PostAnnouceFragment", "Country List is empty");
            }
        } else {
            Log.e("PostAnnouceFragment", "Country List is null");
        }


        this.sPays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.sPays.setAdapter(adapter);
    }

    /**
     * Verifie que l'utilisateur a bien choisi une valeur dans les spinners (Pays et TypeCuisine)
     *
     * @return True si la valeur des spinners a bien ete choisie
     */
    public boolean formWellFill() {
        boolean wellFill = true;

        for (EditText e : neededComponants) {
            if (e.getText().toString().trim().length() == 0) {
                e.setBackgroundColor(Color.RED);
                wellFill = false;
            }
        }

        if (this.sPays.getSelectedItem().toString().length() == 0) {
            wellFill = false;
            this.sPays.setBackgroundColor(Color.RED);
        }

        if (this.sType.getSelectedItem().toString().length() == 0) {
            wellFill = false;
            this.sType.setBackgroundColor(Color.RED);

        }
        return wellFill;
    }

    /**
     * Creation d'une nouvelle offre en base
     *
     * @return True si toutes les donnees ont ete inserees dans les tables, False sinon
     */
    private boolean insertOffreInDatabase() {
        if (this.formWellFill()) {
            this.databaseHelper = FeedMeOpenDatabaseHelper.getHelper(this.getActivity());

            //On récupère toutes les données
            String titre = this.etTitle.getText().toString();
            Integer nbPlace = Integer.parseInt(this.etNbPlaces.getText().toString());
            Integer price = Integer.parseInt(this.etPrice.getText().toString());
            String street = this.etStreet.getText().toString();
            String city = this.etCity.getText().toString();
            String cp = this.etCodePostal.getText().toString();
            String pays = this.sPays.getSelectedItem().toString();
            Date dateRepas = this.calendar.getTime();
            Date creationOffre = Calendar.getInstance().getTime();
            String menu = this.etMenu.getText().toString();
            String typeCuisine = this.sType.getSelectedItem().toString();
            Integer duration = 0;
            if (this.etDuration.getText().toString().trim().length() != 0)
                duration = Integer.parseInt(this.etDuration.getText().toString());

            Integer ageMin = 0;
            Integer ageMax = 100;
            if (this.etAge.getText().length() != 0) {
                String[] ages = this.etAge.getText().toString().trim().split("-");
                ageMin = Integer.parseInt(ages[0]);
                ageMax = Integer.parseInt(ages[1]);
            }

            String brief = this.etBrief.getText().toString();
            boolean pets = this.cbPets.isChecked();

            //on met tout dans la database
            return this.insertAction(creationOffre, titre, price, nbPlace, duration, dateRepas, city, cp, street, pays, typeCuisine, brief, menu, ageMin, ageMax, pets);
        } else {
            Toast.makeText(this.getActivity(), R.string.champVide, Toast.LENGTH_SHORT).show();
            return false;
        }


    }

    private boolean insertAction(Date creationOffre, String titre, Integer price, Integer nbPlace, Integer duration, Date dateRepas, String city, String cp, String street, String pays, String typeCuisine, String brief, String menu, Integer ageMin, Integer ageMax, boolean pets) {


        //On récupère le pays
        Pays objetPays = this.getPays(pays);
        if (objetPays == null) return false;
        Log.d("PostAnnounceFragment",objetPays.toString());

        //On récupère le Type de cuisine
        TypeCuisine objetTypeCusine = this.getTypeCuisine(typeCuisine);
        if (objetTypeCusine == null) return false;

        //on crée les objets !
        Ville objetVille = new Ville(city, cp, objetPays);
        Adresse objetAdresse = new Adresse(street, objetVille);
        //on crée l' offre
        Log.d("PostAnnounceFragment",objetAdresse.toString());
        Offre objetOffre = null;
        try {


            objetOffre = new Offre(creationOffre, titre, price, nbPlace, duration, dateRepas, objetAdresse, brief, menu, ageMin, ageMax, pets, objetTypeCusine, ((MainActivity) this.getActivity()).getCurrentUser());
            //On insert tout dans la database
            databaseHelper.getOffresDao().create(objetOffre);
            Log.d("PostAnnounceFragment", objetOffre.toString());
        } catch (SQLException e) {
            Log.e("PostAnnouceFragment", "Echec inserting offre in database : " + e);
            return false;
        }
        return true;
    }


    private Pays getPays(String pays) {
        //On récupère le pays
        Pays objetPays = null;

        List<Pays> listPays = null;
        try {
            listPays = databaseHelper.getPaysDao().queryBuilder().where().eq("nom", pays).query();
        } catch (SQLException e) {
            Log.e("PostAnnouceFragement", "Echec getting pays from database : " + e);
            return null;
        }
        if (listPays != null && listPays.size() == 1) {
            objetPays = listPays.get(0);
        } else {
            Toast.makeText(this.getActivity(), R.string.paysnotfound, Toast.LENGTH_SHORT).show();
            Log.e("PostannouceFragment", "Echec find pays, no tuple on multiple possibility");
            return null;
        }

        return objetPays;
    }

    private TypeCuisine getTypeCuisine(String typeCuisine) {

        //On récupère le Type de cuisine
        TypeCuisine objetTypeCusine = null;

        List<TypeCuisine> listTypeCuisine = null;
        try {
            listTypeCuisine = databaseHelper.getTypeCuisinesDao().queryBuilder().where().eq("typeCuisine", typeCuisine).query();
        } catch (SQLException e) {
            Log.e("PostAnnouceFragement", "Echec getting Type cuisine from database: " + e);
            return null;
        }
        if (listTypeCuisine != null && listTypeCuisine.size() == 1) {
            objetTypeCusine = listTypeCuisine.get(0);
        } else {
            Toast.makeText(this.getActivity(), R.string.typecuisinenotfound, Toast.LENGTH_SHORT).show();
            Log.e("PostannouceFragment", "Echec find Type cuisine, no tuple on multiple possibility");
            return null;
        }
        return objetTypeCusine;
    }


    protected void CreateDialog(int id) {
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                etDate.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                        + selectedYear);

                calendar.set(selectedYear, selectedMonth, selectedDay);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, year, month, day);

        datePickerDialog.show();
    }

}
