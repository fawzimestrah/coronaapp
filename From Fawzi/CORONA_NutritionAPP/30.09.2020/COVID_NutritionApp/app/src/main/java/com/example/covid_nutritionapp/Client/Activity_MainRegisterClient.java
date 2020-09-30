package com.example.covid_nutritionapp.Client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Activity_LoginActivity;
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

import java.util.Calendar;

import static android.text.TextUtils.isEmpty;

public class Activity_MainRegisterClient extends AppCompatActivity {
    private static final String TAG = "RegisterActivity" ;
    //widgets
    private EditText mEmail, mPassword, mConfirmPassword;
    private ProgressBar mProgressBar;
    private Button Bregister;
    private EditText  date_naissance,  poids, taille,  nb_prs_famille, nb_fils, nb_fils_prs, nb_chambres
            , suivi_recommandations, nb_ciguarette,nb_Narguile;

    RadioGroup  sexe,niveau_Education, lieu_Resistance, situation_Sociale,specialiste_sante, assurance, alcool,cas_professionnel,ciguarette,narguile;
    String   sexeflag,niveau_Educationflag, lieu_Resistanceflag, situation_Socialeflag,specialiste_santeflag, assuranceflag, alcoolflag,cas_professionnelflag,ciguaretteflag,narguileflag;

    private RadioGroup [] radioGroupArray;
//type account
    private String typeAccount ="C1";
    //vars
    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client_layout);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        Bregister=(Button)findViewById(R.id.btn_register);


        date_naissance=(EditText)findViewById(R.id.input_date);
        poids=(EditText)findViewById(R.id.input_poids);
        taille=(EditText)findViewById(R.id.input_taille);
        nb_prs_famille=(EditText)findViewById(R.id.input_nbprsfamille);
        nb_fils=(EditText)findViewById(R.id.input_nbfils);
        nb_fils_prs=(EditText)findViewById(R.id.input_nbfils_pers);
        nb_chambres=(EditText)findViewById(R.id.input_nb_chambres);
        suivi_recommandations= (EditText)findViewById(R.id.input_suivi_recomm);
        nb_ciguarette=(EditText)findViewById(R.id.input_quatite_cigarette);
        nb_Narguile=(EditText)findViewById(R.id.input_quatite_narguile);


        sexe=(RadioGroup) findViewById(R.id.radiogrp_sexe);
        niveau_Education=(RadioGroup) findViewById(R.id.radiogrp_niveauedu);
        lieu_Resistance=(RadioGroup)findViewById(R.id.radiogrp_lieuresistance);
        situation_Sociale=(RadioGroup)findViewById(R.id.radiogrp_situation_sociale);
        cas_professionnel=(RadioGroup)findViewById(R.id.radiogrp_casprofessionnel);
        specialiste_sante=(RadioGroup)findViewById(R.id.radiogrp_specialistesante);
        assurance=(RadioGroup)findViewById(R.id.radiogrp_assurance);
        alcool=(RadioGroup)findViewById(R.id.radiogrp_alcool);
        ciguarette=(RadioGroup)findViewById(R.id.radiogrp_ciguarette);
        narguile=(RadioGroup)findViewById(R.id.radiogrp_nargile);



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

                        int M=(month + 1);
                        date_naissance.setText(dayOfMonth + "/" +M + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });


        sexe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton rb=(RadioButton)findViewById(checkedId);
            sexeflag=rb.getText().toString();
            //Toast.makeText(getApplicationContext(),""+sexeflag, Toast.LENGTH_LONG).show();
            }
        });
        niveau_Education.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                niveau_Educationflag=rb.getText().toString();
            }
        });

        lieu_Resistance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                lieu_Resistanceflag=rb.getText().toString();
            }
        });
        situation_Sociale.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                situation_Socialeflag=rb.getText().toString();
            }
        });
        specialiste_sante.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                specialiste_santeflag=rb.getText().toString();
              }
        });

        assurance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                assuranceflag=rb.getText().toString();
            }
        });
        ciguarette.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);

                ciguaretteflag=rb.getText().toString();
                if(ciguaretteflag.equals("لا")){
                    nb_ciguarette.setText("0");
                    nb_ciguarette.setEnabled(false);
                }else{

                    nb_ciguarette.setEnabled(true);
                }
            }
        });
        narguile.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                narguileflag=rb.getText().toString();

                if(narguileflag.equals("لا")){
                    nb_Narguile.setText("0");
                    nb_Narguile.setEnabled(false);
                }else{

                    nb_Narguile.setEnabled(true);
                }
             }
        });

        alcool.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                alcoolflag=rb.getText().toString();
            }
        });
        cas_professionnel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                cas_professionnelflag=rb.getText().toString();
              }
        });



          mDb = FirebaseFirestore.getInstance();

        hideSoftKeyboard();


        Bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: attempting to register.");

                //check for null valued EditText fields
                if(checkForNull()) {
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
                }else{
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "You must fill out all the fields", Snackbar.LENGTH_SHORT).show();

                }
            }
        });
     }

    @SuppressLint("ResourceAsColor")
    private boolean checkForNull() {
        boolean correct=true;
        if(sexe.getCheckedRadioButtonId()==-1) {
            correct = correct && false;
        }
        if(niveau_Education.getCheckedRadioButtonId()==-1){
            correct=correct && false;
        }
        if(lieu_Resistance.getCheckedRadioButtonId()==-1){
            correct=correct && false;
        }
        if(situation_Sociale.getCheckedRadioButtonId()==-1){
            correct=correct && false;
        }
        if(specialiste_sante.getCheckedRadioButtonId()==-1){
            correct=correct && false;
        }
        if(assurance.getCheckedRadioButtonId()==-1){
            correct=correct && false;
        }
        if(alcool.getCheckedRadioButtonId()==-1){
            correct=correct && false;
        }
        if(cas_professionnel.getCheckedRadioButtonId()==-1){
            correct=correct && false;
        }
        if(ciguarette.getCheckedRadioButtonId()==-1){
            correct=correct && false;
        }
        if(narguile.getCheckedRadioButtonId()==-1){
            correct=correct && false;
        }

        if(isEmpty( nb_Narguile.getText().toString())){
            nb_Narguile.setHintTextColor(R.color.red1);
//            nb_Narguile.setHint("Empty ");
            nb_Narguile.setFocusable(true);
            correct=correct && false;
        }
        if(isEmpty( nb_ciguarette.getText().toString())){
            nb_ciguarette.setHintTextColor(R.color.red1);
  //          nb_ciguarette.setHint("Empty ");
            nb_ciguarette.setFocusable(true);
            correct=correct && false;
        }
        if(isEmpty( suivi_recommandations.getText().toString())){
            suivi_recommandations.setHintTextColor(R.color.red1);
    //        suivi_recommandations.setHint("Empty ");
            suivi_recommandations.setFocusable(true);
            correct=correct && false;
        }
        if(isEmpty( nb_chambres.getText().toString())){
            nb_chambres.setHintTextColor(R.color.red1);
      //      nb_chambres.setHint("Empty ");
            nb_chambres.setFocusable(true);
            correct=correct && false;
        }
        if(isEmpty(  nb_fils_prs.getText().toString())){
            nb_fils_prs.setHintTextColor(R.color.red1);
        //    nb_fils_prs.setHint("Empty ");
            nb_fils_prs.setFocusable(true);
            correct=correct && false;
        }

        if(isEmpty( nb_fils.getText().toString())){
            nb_fils.setHintTextColor(R.color.red1);
          //  nb_fils.setHint("Empty ");
            nb_fils.setFocusable(true);
            correct=correct && false;
        }
        if(isEmpty( nb_prs_famille.getText().toString())){
            nb_prs_famille.setHintTextColor(R.color.red1);
            //nb_prs_famille.setHint("Empty ");
            nb_prs_famille.setFocusable(true);
            correct=correct && false;
        }
        if(isEmpty(taille.getText().toString())){
            taille.setHintTextColor(R.color.red1);
           // taille.setHint("Empty ");
            taille.setFocusable(true);
            correct=correct && false;
        }

        if(isEmpty(poids.getText().toString())){
            poids.setHintTextColor(R.color.red1);
           // poids.setHint("Empty ");
            poids.setFocusable(true);
            correct=correct && false;

        }
        if(isEmpty(date_naissance.getText().toString())){
            date_naissance.setHintTextColor(R.color.red1);
           // date_naissance.setHint("Empty ");
            date_naissance.setFocusable(true);
            correct=correct && false;


        }

        return correct;
    }

    public void registerNewEmail(final String email, String password){
        showDialog();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()){
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
                           Data_User Client=new Data_User(FirebaseAuth.getInstance().getUid(),email, typeAccount);

                            Client.setDate_naissance(date_naissance.getText().toString());
                            Client.setPoids(poids.getText().toString());
                            Client.setTaille(taille.getText().toString());
                            Client.setNb_prs_famille(nb_prs_famille.getText().toString());
                            Client.setNb_fils(nb_fils.getText().toString());
                            Client.setNb_fils_prs(nb_fils_prs.getText().toString());
                            Client.setNb_chambres(nb_chambres.getText().toString());
                            Client.setSuivi_recommandations(suivi_recommandations.getText().toString());
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

                            Data_User.Insert_User(Client);

                            redirectLoginScreen();

                        }
                        else {
                            //Toast.makeText(getApplicationContext(),"error: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar.make(parentLayout, "Something went wrong 22.\nerror:"+task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                            hideDialog();
                        }

                        // ...
                    }
                });
    }

    /**
     * Redirects the user to the login screen
     */
    private void redirectLoginScreen(){
        //Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");
        Intent intent = new Intent(Activity_MainRegisterClient.this, Activity_LoginActivity.class);
        startActivity(intent);
        finish();

    }


    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
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



}

