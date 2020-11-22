package com.example.covid_nutritionapp.Client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.covid_nutritionapp.Activity_LoginActivity;
import com.example.covid_nutritionapp.Admin.Activity_EditForm;
import com.example.covid_nutritionapp.Admin.Activity_MainAdmin;
import com.example.covid_nutritionapp.Data_Question;
import com.example.covid_nutritionapp.Data_forms;
import com.example.covid_nutritionapp.R;
import com.example.covid_nutritionapp.User;
import com.example.covid_nutritionapp.Data_User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.util.Listener;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.text.TextUtils.isEmpty;

public class Activity_MainRegisterClient extends AppCompatActivity {
    private Layout layoutview;
    private static final String TAG = "RegisterActivity";
    private final int REQUEST_PERMISSIONS_REQUEST_CODE=44;
    private Location lastKnownLocation = null;
    //widgets
    private EditText mEmail, mPassword, mConfirmPassword;
    private ProgressBar mProgressBar;
    private Button Bregister;
    private EditText date_naissance, poids, taille, nb_prs_famille, nb_fils, nb_fils_prs, nb_chambres, nb_ciguarette, nb_Narguile;

    private RadioGroup sexe, niveau_Education, lieu_Resistance, situation_Sociale, specialiste_sante, assurance, alcool, cas_professionnel, ciguarette, narguile;
                    String sexeflag, niveau_Educationflag, lieu_Resistanceflag, situation_Socialeflag, specialiste_santeflag, assuranceflag, alcoolflag, cas_professionnelflag, ciguaretteflag, narguileflag
                    ,currentCity,latitude,longitude,country;
    private WebView tDesciptionRegister;
    private  TextView tsexe,tEdu,tLieu,tEtat,tCasProf,tSpecSante,tAssurance,tAlcool,tfume,tnarguile;
    private RadioButton radioMale,radioFemale,radioEdu0,radioEdu1,radioEdu2,radioEdu3,radioEdu4,
                        radioLieu1,radioLieu2,radioLieu3,radioLieu4,radioLieu5,radioLieu6,radioLieu7,radioLieu8,
                        radioSituation1,radioSituation2,radioSituation3,radioSituation4,
                        radioCas1,radioCas2,radioCas3,radioCas4,radioSpecNo,radioSpecYes,radioAss1,radioAss2,radioAss3,radioAss4,
                        radioAlcoll1,radioAlcoll2,radioAlcoll3,radiofume1,radiofume2,radiofume3,radiofume4,radioNarg1,radioNarg2,radioNarg3,radioNarg4;

    private RadioGroup[] radioGroupArray;
    //type account
    private String typeAccount = "C1";
    //vars
    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;
    private ArrayAdapter mArrayAdapterLanguage;
    private Spinner mSpinnerLanguage;
    private String language="";
    private ScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client_layout);
        mSpinnerLanguage=findViewById(R.id.IdSpinnerlanguage);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        Bregister = (Button) findViewById(R.id.btn_register);
        date_naissance = (EditText) findViewById(R.id.input_date);
        poids = (EditText) findViewById(R.id.input_poids);
        taille = (EditText) findViewById(R.id.input_taille);
        nb_prs_famille = (EditText) findViewById(R.id.input_nbprsfamille);
        nb_fils = (EditText) findViewById(R.id.input_nbfils);
        nb_fils_prs = (EditText) findViewById(R.id.input_nbfils_pers);
        nb_chambres = (EditText) findViewById(R.id.input_nb_chambres);
        nb_ciguarette = (EditText) findViewById(R.id.input_quatite_cigarette);
        nb_Narguile = (EditText) findViewById(R.id.input_quatite_narguile);

        sexe = (RadioGroup) findViewById(R.id.radiogrp_sexe);
        niveau_Education = (RadioGroup) findViewById(R.id.radiogrp_niveauedu);
        lieu_Resistance = (RadioGroup) findViewById(R.id.radiogrp_lieuresistance);
        situation_Sociale = (RadioGroup) findViewById(R.id.radiogrp_situation_sociale);
        cas_professionnel = (RadioGroup) findViewById(R.id.radiogrp_casprofessionnel);
        specialiste_sante = (RadioGroup) findViewById(R.id.radiogrp_specialistesante);
        assurance = (RadioGroup) findViewById(R.id.radiogrp_assurance);
        alcool = (RadioGroup) findViewById(R.id.radiogrp_alcool);
        ciguarette = (RadioGroup) findViewById(R.id.radiogrp_ciguarette);
        narguile = (RadioGroup) findViewById(R.id.radiogrp_nargile);



        tDesciptionRegister=(WebView) findViewById(R.id.textView4);
        tDesciptionRegister.setBackgroundColor(Color.TRANSPARENT);
        tEdu=findViewById(R.id.textView2);
        radioMale=findViewById(R.id.Radio_male);
        radioFemale=findViewById(R.id.Radio_female);
        radioEdu0=findViewById(R.id.Radio_edu0);
        radioEdu1=findViewById(R.id.Radio_edu1);
        radioEdu2=findViewById(R.id.Radio_edu2);
        radioEdu3=findViewById(R.id.Radio_edu3);
        radioEdu4=findViewById(R.id.Radio_edu4);
        tLieu=findViewById(R.id.textView3);
        radioLieu1=findViewById(R.id.Radio_lieu1);
        radioLieu2=findViewById(R.id.Radio_lieu2);
        radioLieu3=findViewById(R.id.Radio_lieu3);
        radioLieu4=findViewById(R.id.Radio_lieu4);
        radioLieu5=findViewById(R.id.Radio_lieu5);
        radioLieu6=findViewById(R.id.Radio_lieu6);
        radioLieu7=findViewById(R.id.Radio_lieu7);
        radioLieu8=findViewById(R.id.Radio_lieu8);
        tEtat=findViewById(R.id.textView5);;
        radioSituation1=findViewById(R.id.Radio_situation1);
        radioSituation2=findViewById(R.id.Radio_situation2);
        radioSituation3=findViewById(R.id.Radio_situation3);
        radioSituation4=findViewById(R.id.Radio_situation4);
        tCasProf=findViewById(R.id.textView6);
        radioCas1=findViewById(R.id.Radio_cas1);
        radioCas2=findViewById(R.id.Radio_cas2);
        radioCas3=findViewById(R.id.Radio_cas3);
        radioCas4=findViewById(R.id.Radio_cas4);
        tsexe=findViewById(R.id.textView7);
        tSpecSante=findViewById(R.id.textView8);
        radioSpecYes=findViewById(R.id.Radiospecyes);
        radioSpecNo=findViewById(R.id.Radiospecno);
        tAssurance=findViewById(R.id.textView9);
        radioAss1=findViewById(R.id.Radio_ass1);
        radioAss2=findViewById(R.id.Radio_ass2);
        radioAss3=findViewById(R.id.Radio_ass3);
        radioAss4=findViewById(R.id.Radio_ass4);
        tAlcool=findViewById(R.id.textView10);
        radioAlcoll1=findViewById(R.id.Radio_16_1);
        radioAlcoll2=findViewById(R.id.Radio_16_2);
        radioAlcoll3=findViewById(R.id.Radio_16_3);
        tfume=findViewById(R.id.textView11);
        radiofume1=findViewById(R.id.Radio_17_1);
        radiofume2=findViewById(R.id.Radio_17_2);
        radiofume3=findViewById(R.id.Radio_17_3);
        radiofume4=findViewById(R.id.Radio_17_4);
        tnarguile=findViewById(R.id.textView12);
        radioNarg1=findViewById(R.id.Radio_18_1);
        radioNarg2=findViewById(R.id.Radio_18_2);
        radioNarg3=findViewById(R.id.Radio_18_3);
        radioNarg4=findViewById(R.id.Radio_18_4);


         View.OnFocusChangeListener listener= new View.OnFocusChangeListener() {
             @Override
             public void onFocusChange(View v, boolean hasFocus) {
                 if(!hasFocus){
                     hideKeyboard(Activity_MainRegisterClient.this);
                 }
             }
         };

        taille.setOnFocusChangeListener(listener);
        poids.setOnFocusChangeListener(listener);
       nb_prs_famille.setOnFocusChangeListener(listener);
       nb_fils.setOnFocusChangeListener(listener);
       nb_fils_prs.setOnFocusChangeListener(listener);
       nb_chambres.setOnFocusChangeListener(listener);
       nb_ciguarette.setOnFocusChangeListener(listener);
       nb_Narguile.setOnFocusChangeListener(listener);

        getLocation();


        String [] listLanguage=getResources().getStringArray(R.array.languages);

        mArrayAdapterLanguage=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,listLanguage);
        if(mSpinnerLanguage!=null) {
            mSpinnerLanguage.setAdapter(mArrayAdapterLanguage);
        }


        mSpinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!language.equals("")) {
  //                  Toast.makeText(getApplicationContext(),"hehe"+R.string.niveau_edu0_AR,Toast.LENGTH_LONG).show();

                }
                language = mSpinnerLanguage.getSelectedItem().toString();
                getStringResourceByLanguage(language);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getStringResourceByLanguage("");




        date_naissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(Activity_MainRegisterClient.this);

                Calendar cldr = Calendar.getInstance();
                final int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                final int year = cldr.get(Calendar.YEAR);

                // CREATE DATE picker
                DatePickerDialog picker = new DatePickerDialog(Activity_MainRegisterClient.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        int M = (month + 1);
                        date_naissance.setText(dayOfMonth + "/" + M + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });


        sexe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                sexeflag = rb.getText().toString();
                //Toast.makeText(getApplicationContext(),""+sexeflag, Toast.LENGTH_LONG).show();
            }
        });
        niveau_Education.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                niveau_Educationflag = rb.getText().toString();
            }
        });

        lieu_Resistance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                lieu_Resistanceflag = rb.getText().toString();
            }
        });
        situation_Sociale.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                situation_Socialeflag = rb.getText().toString();
            }
        });
        specialiste_sante.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                specialiste_santeflag = rb.getText().toString();
            }
        });

        assurance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                assuranceflag = rb.getText().toString();
            }
        });
        ciguarette.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);

                ciguaretteflag = rb.getText().toString();
                if (ciguaretteflag.equals("لا") || ciguaretteflag.equals("No") ) {
                    nb_ciguarette.setText("0");
                    nb_ciguarette.setEnabled(false);
                } else {

                    nb_ciguarette.setEnabled(true);
                }
            }
        });
        narguile.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                narguileflag = rb.getText().toString();

                if (narguileflag.equals("لا") || narguileflag.equals("No") ) {
                    nb_Narguile.setText("0");
                    nb_Narguile.setEnabled(false);
                } else {

                    nb_Narguile.setEnabled(true);
                }
            }
        });

        alcool.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                alcoolflag = rb.getText().toString();
            }
        });
        cas_professionnel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                cas_professionnelflag = rb.getText().toString();
            }
        });


        mDb = FirebaseFirestore.getInstance();

        hideSoftKeyboard();


        Bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: attempting to register.");

                //check for null valued EditText fields
                if (checkForNull()) {
                    if (!isEmpty(mEmail.getText().toString())
                            && !isEmpty(mPassword.getText().toString())
                            && !isEmpty(mConfirmPassword.getText().toString())) {

                        //check if passwords match
                        if (mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {

                            //Initiate registration task
                            registerNewEmail(mEmail.getText().toString(), mPassword.getText().toString());
                        } else {
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar.make(parentLayout, "Passwords do not Match", Snackbar.LENGTH_SHORT).show();
                            hideDialog();
                        }

                    } else {
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar.make(parentLayout, "You must fill out all the fields (Email And Password)", Snackbar.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } else {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "You must fill out all the fields", Snackbar.LENGTH_SHORT).show();

                }
                    }
        });


    }

    @SuppressLint("ResourceAsColor")
    private boolean checkForNull() {
        boolean correct = true;
        if (sexe.getCheckedRadioButtonId() == -1) {
            correct = correct && false;
        }
        if (niveau_Education.getCheckedRadioButtonId() == -1) {
            correct = correct && false;
        }
        if (lieu_Resistance.getCheckedRadioButtonId() == -1) {
            correct = correct && false;
        }
        if (situation_Sociale.getCheckedRadioButtonId() == -1) {
            correct = correct && false;
        }
        if (specialiste_sante.getCheckedRadioButtonId() == -1) {
            correct = correct && false;
        }
        if (assurance.getCheckedRadioButtonId() == -1) {
            correct = correct && false;
        }
        if (alcool.getCheckedRadioButtonId() == -1) {
            correct = correct && false;
        }
        if (cas_professionnel.getCheckedRadioButtonId() == -1) {
            correct = correct && false;
        }
        if (ciguarette.getCheckedRadioButtonId() == -1) {
            correct = correct && false;
        }
        if (narguile.getCheckedRadioButtonId() == -1) {
            correct = correct && false;
        }

        if (isEmpty(nb_Narguile.getText().toString())) {
            nb_Narguile.setHintTextColor(R.color.red1);
//            nb_Narguile.setHint("Empty ");
            nb_Narguile.setFocusable(true);
            correct = correct && false;
        }
        if (isEmpty(nb_ciguarette.getText().toString())) {
            nb_ciguarette.setHintTextColor(R.color.red1);
            //          nb_ciguarette.setHint("Empty ");
            nb_ciguarette.setFocusable(true);
            correct = correct && false;
        }
        if (isEmpty(nb_chambres.getText().toString())) {
            nb_chambres.setHintTextColor(R.color.red1);
            //      nb_chambres.setHint("Empty ");
            nb_chambres.setFocusable(true);
            correct = correct && false;
        }
        if (isEmpty(nb_fils_prs.getText().toString())) {
            nb_fils_prs.setHintTextColor(R.color.red1);
            //    nb_fils_prs.setHint("Empty ");
            nb_fils_prs.setFocusable(true);
            correct = correct && false;
        }

        if (isEmpty(nb_fils.getText().toString())) {
            nb_fils.setHintTextColor(R.color.red1);
            //  nb_fils.setHint("Empty ");
            nb_fils.setFocusable(true);
            correct = correct && false;
        }
        if (isEmpty(nb_prs_famille.getText().toString())) {
            nb_prs_famille.setHintTextColor(R.color.red1);
            //nb_prs_famille.setHint("Empty ");
            nb_prs_famille.setFocusable(true);
            correct = correct && false;
        }
        if (isEmpty(taille.getText().toString())) {
            taille.setHintTextColor(R.color.red1);
            // taille.setHint("Empty ");
            taille.setFocusable(true);
            correct = correct && false;
        }

        if (isEmpty(poids.getText().toString())) {
            poids.setHintTextColor(R.color.red1);
            // poids.setHint("Empty ");
            poids.setFocusable(true);
            correct = correct && false;

        }
        if (isEmpty(date_naissance.getText().toString())) {
            date_naissance.setHintTextColor(R.color.red1);
            // date_naissance.setHint("Empty ");
            date_naissance.setFocusable(true);
            correct = correct && false;


        }

        return correct;
    }

    public void registerNewEmail(final String email, String password) {
        showDialog();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                            //insert some default data
                            User user = new User();
                            user.setEmail(email);
                            user.setUsername(email.substring(0, email.indexOf("@")));
                            user.setUser_id(FirebaseAuth.getInstance().getUid());

                            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                                    .setTimestampsInSnapshotsEnabled(true)
                                    .build();
                            mDb.setFirestoreSettings(settings);

                            DocumentReference newUserRef = mDb
                                    .collection(getString(R.string.collection_users))
                                    .document(FirebaseAuth.getInstance().getUid());

                            //Toast.makeText(getApplicationContext(),"In onComplete newUserRef",Toast.LENGTH_LONG).show();

                            newUserRef.set(user);
                            Data_User Client = new Data_User(FirebaseAuth.getInstance().getUid(), email, typeAccount);

                            Client.setDate_naissance(date_naissance.getText().toString());
                            Client.setPoids(poids.getText().toString());
                            Client.setTaille(taille.getText().toString());
                            Client.setNb_prs_famille(nb_prs_famille.getText().toString());
                            Client.setNb_fils(nb_fils.getText().toString());
                            Client.setNb_fils_prs(nb_fils_prs.getText().toString());
                            Client.setNb_chambres(nb_chambres.getText().toString());
                            Client.setNb_ciguarette(nb_ciguarette.getText().toString());
                            Client.setNb_Narguile(nb_Narguile.getText().toString());
                            Client.setSexe(sexeflag);
                            Client.setBolciguarette(ciguaretteflag);
                            Client.setBolnarguile(narguileflag);

                            Client.setNiveau_Education(niveau_Educationflag);
                            Client.setLieu_Resistance(lieu_Resistanceflag);
                            Client.setSituation_Sociale(situation_Socialeflag);
                            Client.setSpecialiste_sante(specialiste_santeflag);
                            Client.setAssurance(assuranceflag);
                            Client.setAlcool(alcoolflag);
                            Client.setCas_professionnel(cas_professionnelflag);

                            // data location
                            Client.setCountry(country);
                            Client.setLatitude(latitude);
                            Client.setLongitude(longitude);
                            Client.setCity(currentCity);
                            Data_User.Insert_User(Client);
                            redirectLoginScreen();

                        } else {
                            //Toast.makeText(getApplicationContext(),"error: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar.make(parentLayout, "Something went wrong 22.\nerror:" + task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                            hideDialog();
                        }

                        // ...
                    }
                });
    }

    /**
     * Redirects the user to the login screen
     */
    private void redirectLoginScreen() {
        //Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_MainRegisterClient.this);
        builder.setTitle("Confidentiality and Data Security");
        if(language.equals("EN")) {
            builder.setMessage(R.string.Note_Register_EN);
            builder.setPositiveButton("Ok ", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    setResult(2);
                    finish();

                }
            });

        }
        else{
            builder.setMessage(R.string.Note_Register_AR);
            builder.setNegativeButton("Ok ", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    setResult(2);
                    finish();

                }
            });

        }
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // Do nothing but close the dialog
                setResult(2);
                finish();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }


    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void getLocation() {
        LocationManager locationManager = null;
        LocationListener locationListener = null;
        Intent intent = getIntent();
        if (intent.getIntExtra("Place Number",0) == 0 ){
            // Zoom into users location
            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lastKnownLocation = location;
                    // location city and contry
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                    try {
                        /**
                         * Geocoder.getFromLocation - Returns an array of Addresses
                         * that are known to describe the area immediately surrounding the given latitude and longitude.
                         */

                        List<Address> addresses = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
                        currentCity = addresses.get(0).getSubAdminArea();
                        longitude = String.valueOf(addresses.get(0).getLongitude());
                        latitude = String.valueOf(addresses.get(0).getLatitude());
                        country = addresses.get(0).getCountryName();

                    } catch (IOException e) {
                        //e.printStackTrace();
                        Log.e("namecities", "Impossible to connect to Geocoder", e);
                    }

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastKnownLocation!=null) {
                    Log.e("test", "lastlocations");
                    // location city and contry
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                    try {
                        /**
                         * Geocoder.getFromLocation - Returns an array of Addresses
                         * that are known to describe the area immediately surrounding the given latitude and longitude.
                         */

                        List<Address> addresses = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
                        currentCity=addresses.get(0).getSubAdminArea();
                        longitude=String.valueOf(addresses.get(0).getLongitude());
                        latitude=String.valueOf(addresses.get(0).getLatitude());
                        country=addresses.get(0).getCountryName();

                        Log.e("namecities", "cities=" + addresses.get(0).getSubAdminArea());

                    /*
                     String stringLatitude = String.valueOf(gpsTracker.latitude);
                     String stringLongitude = String.valueOf(gpsTracker.longitude);
                     String country = gpsTracker.getCountryName(this);
                     String city = gpsTracker.getLocality(this);
                    String postalCode = gpsTracker.getPostalCode(this);
                    String addressLine = gpsTracker.getAddressLine(this);
                     */
                    hideDialog();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        Log.e("namecities", "Impossible to connect to Geocoder", e);
                    }


                }else{// location is off
                    statusCheck();

                }
            } else {
                Log.e("Debut","ELSE OnmapReady");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSIONS_REQUEST_CODE);

            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
//                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted. Kick off the process of building and connecting
                // GoogleApiClient.
                getLocation(); // Retry
            } else {
                // Permission denied.
   //             finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    private void getStringResourceByLanguage(String languageQuest) {



        switch (languageQuest){
            case "EN":
//                tDesciptionRegister.setText(R.string.desciption_register_EN);


                tDesciptionRegister.loadData("<html align=\"justify\">" +
                        "        <head>" +
                        "        </head>" +
                        "        <body>" +
                        " <p >" +
                        "Dear Sir/Madame." +
                        "</br>The aim of this mobile application is to assess and monitor the nutritional status and public health" +
                        "during emergencies for the purpose of collecting accurate and high-quality data on the nutritional status of the population." +
                        " </br>Your cooperation is very important for the success of this study." +
                        "</br>Please answer the following questions accurately and in explicitly." +
                        "Information contained in it will remain secret and will be used only for scientific purposes." +
                        "</br>Thank you in advance and we wish you health and wellness." +
                        " Sincerely yours. </p> " +
                        " </body> " +
                        " </html>","text/html","utf-8");

                date_naissance.setHint(R.string.date_naissance_EN);
                tsexe.setText(R.string.sexe_EN);
                radioMale.setText(R.string.male_EN);
                radioFemale.setText(R.string.female_EN);
                poids.setHint(R.string.poids_actuel_EN);
                taille.setHint(R.string.taille_actuelle_EN);
                tEdu.setText(R.string.niveau_edu_EN);
                radioEdu0.setText(R.string.niveau_edu0_EN);
                radioEdu1.setText(R.string.niveau_edu1_EN);
                radioEdu2.setText(R.string.niveau_edu2_EN);
                radioEdu3.setText(R.string.niveau_edu3_EN);
                radioEdu4.setText(R.string.niveau_edu4_EN);
                tLieu.setText(R.string.lieu_residance_EN);
                radioLieu1.setText(R.string.liew_1_EN);
                radioLieu2.setText(R.string.liew_2_EN);
                radioLieu3.setText(R.string.liew_3_EN);
                radioLieu4.setText(R.string.liew_4_EN);
                radioLieu5.setText(R.string.liew_5_EN);
                radioLieu6.setText(R.string.liew_6_EN);
                radioLieu7.setText(R.string.liew_7_EN);
                radioLieu8.setText(R.string.liew_8_EN);
                tEtat.setText(R.string.situation_sociale_EN);
                radioSituation1.setText(R.string.situation1_EN);
                radioSituation2.setText(R.string.situation2_EN);
                radioSituation3.setText(R.string.situation3_EN);
                radioSituation4.setText(R.string.situation4_EN);
                nb_prs_famille.setHint(R.string.nb_prs_famille_EN);
                nb_fils.setHint(R.string.nb_fils_EN);
                nb_fils_prs.setHint(R.string.nb_fils_prs_EN);
                nb_chambres.setHint(R.string.nb_chambres_EN);
                tCasProf.setText(R.string.cas_professionnel_EN);
                radioCas1.setText(R.string.cas_1_EN);
                radioCas2.setText(R.string.cas_2_EN);
                radioCas3.setText(R.string.cas_3_EN);
                radioCas4.setText(R.string.cas_4_EN);
                tSpecSante.setText(R.string.spec_sante_EN);
                radioSpecYes.setText(R.string.text_yes_EN);
                radioSpecNo.setText(R.string.text_no_EN);
                tAssurance.setText(R.string.assurance_EN);
                radioAss1.setText(R.string.ass_1_EN);
                radioAss2.setText(R.string.ass_2_EN);
                radioAss3.setText(R.string.ass_3_EN);
                radioAss4.setText(R.string.ass_4_EN);
                tAlcool.setText(R.string.quest16_EN);
                radioAlcoll1.setText(R.string.quest16_1_EN);
                radioAlcoll2.setText(R.string.quest16_2_EN);
                radioAlcoll3.setText(R.string.quest16_3_EN);
                tfume.setText(R.string.quest17_EN);
                radiofume1.setText(R.string.quest1718_1_EN);
                radiofume2.setText(R.string.quest1718_2_EN);
                radiofume3.setText(R.string.quest1718_3_EN);
                radiofume4.setText(R.string.quest1718_4_EN);
                nb_ciguarette.setHint(R.string.quantite_EN);
                tnarguile.setText(R.string.quest18_EN);
                nb_Narguile.setHint(R.string.quantite_EN);
                radioNarg1.setText(R.string.quest1718_1_EN);
                radioNarg2.setText(R.string.quest1718_2_EN);
                radioNarg3.setText(R.string.quest1718_3_EN);
                radioNarg4.setText(R.string.quest1718_4_EN);

                mEmail.setHint(R.string.email);
                mPassword.setHint(R.string.password);
                mConfirmPassword.setHint(R.string.confirm_password);

                break;
            case "AR":

//                tDesciptionRegister.setText(R.string.desciption_register_AR);

                tDesciptionRegister.loadData("  <html dir=\"rtl\" lang=\"ar\">" +
                        "        <head>" +
                        "        </head>" +
                        "        <body>" +
                        "        <p align=\"justify\"  >" +
                        "    حضرة السيد(ة) الكريم(ة)،" +
                        "" +
                        "        إن تعاونكم مهم جدا لنجاح هذه الدراسة." +
                        "" +
                        "        الهدف من هذا التطبيق الالكتروني هو تقييم ورصد الحالة التغذوية والصحة العامة خلال حالات الطوارئ لغرض جمع بيانات دقيقة وعالية الجودة على الوضع الغذائي للناس." +
                        "" +
                        "         نرجو منكم الإجابة عن الأسئلة التالية بدقة وصراحة ." +
                        "" +
                        "        المعلومات الواردة فيها ستبقى سرية ولن تستعمل إلا لأهداف علمية." +
                        "" +
                        "         شكرا لكم سلفا، ونتمنى لكم الصحة والعافية." +
                        "" +
                        "        وتفضلوا بقبول فائق الاحترام.  </p>" +
                        "        </body>" +
                        "        </html>" +
                        "    ", "text/html; charset=UTF-8", "utf-8");



                date_naissance.setHint(R.string.date_naissance_AR);
                tsexe.setText(R.string.sexe_AR);

                radioMale.setText(R.string.male_AR);
                radioFemale.setText(R.string.female_AR);
                poids.setHint(R.string.poids_actuel_AR);
                taille.setHint(R.string.taille_actuelle_AR);
                tEdu.setText(R.string.niveau_edu_AR);
                radioEdu0.setText(R.string.niveau_edu0_AR);
                radioEdu1.setText(R.string.niveau_edu1_AR);
                radioEdu2.setText(R.string.niveau_edu2_AR);
                radioEdu3.setText(R.string.niveau_edu3_AR);
                radioEdu4.setText(R.string.niveau_edu4_AR);
                tLieu.setText(R.string.lieu_residance_AR);
                radioLieu1.setText(R.string.liew_1_AR);
                radioLieu2.setText(R.string.liew_2_AR);
                radioLieu3.setText(R.string.liew_3_AR);
                radioLieu4.setText(R.string.liew_4_AR);
                radioLieu5.setText(R.string.liew_5_AR);
                radioLieu6.setText(R.string.liew_6_AR);
                radioLieu7.setText(R.string.liew_7_AR);
                radioLieu8.setText(R.string.liew_8_AR);
                tEtat.setText(R.string.situation_sociale_AR);
                radioSituation1.setText(R.string.situation1_AR);
                radioSituation2.setText(R.string.situation2_AR);
                radioSituation3.setText(R.string.situation3_AR);
                radioSituation4.setText(R.string.situation4_AR);
                nb_prs_famille.setHint(R.string.nb_prs_famille_AR);
                nb_fils.setHint(R.string.nb_fils_AR);
                nb_fils_prs.setHint(R.string.nb_fils_prs_AR);
                nb_chambres.setHint(R.string.nb_chambres_AR);
                tCasProf.setText(R.string.cas_professionnel_AR);
                radioCas1.setText(R.string.cas_1_AR);
                radioCas2.setText(R.string.cas_2_AR);
                radioCas3.setText(R.string.cas_3_AR);
                radioCas4.setText(R.string.cas_4_AR);
                tSpecSante.setText(R.string.spec_sante_AR);
                radioSpecYes.setText(R.string.text_yes_AR);
                radioSpecNo.setText(R.string.text_no_AR);
                tAssurance.setText(R.string.assurance_AR);
                radioAss1.setText(R.string.ass_1_AR);
                radioAss2.setText(R.string.ass_2_AR);
                radioAss3.setText(R.string.ass_3_AR);
                radioAss4.setText(R.string.ass_4_AR);
                tAlcool.setText(R.string.quest16_AR);
                radioAlcoll1.setText(R.string.quest16_1_AR);
                radioAlcoll2.setText(R.string.quest16_2_AR);
                radioAlcoll3.setText(R.string.quest16_3_AR);
                tfume.setText(R.string.quest17_AR);
                radiofume1.setText(R.string.quest1718_1_AR);
                radiofume2.setText(R.string.quest1718_2_AR);
                radiofume3.setText(R.string.quest1718_3_AR);
                radiofume4.setText(R.string.quest1718_4_AR);
                nb_ciguarette.setHint(R.string.quantite_AR);
                tnarguile.setText(R.string.quest18_AR);
                nb_Narguile.setHint(R.string.quantite_AR);
                radioNarg1.setText(R.string.quest1718_1_AR);
                radioNarg2.setText(R.string.quest1718_2_AR);
                radioNarg3.setText(R.string.quest1718_3_AR);
                radioNarg4.setText(R.string.quest1718_4_AR);

                mEmail.setHint(R.string.emailar_AR);
                mPassword.setHint(R.string.passar_AR);
                mConfirmPassword.setHint(R.string.confirmpassar_AR);

        }

  }
/*
    public void changeLanguage(String lang) {
        Locale myLocale;
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }
*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
