package com.example.covid_nutritionapp.Admin;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.R;
import com.example.covid_nutritionapp.Data_User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_InformationUser extends AppCompatActivity {

    private EditText mEmail, mClass, mDateReg;
    private ProgressBar mProgressBar;
    private Bundle extra;
    private String keyIdPerson;
    FirebaseDatabase database;
    DatabaseReference myRef;



    private EditText  date_naissance,  poids, taille,  nb_prs_famille, nb_fils, nb_fils_prs, nb_chambres
            , suivi_recommandations, nb_ciguarette,nb_Narguile,sexe,niveau_Education, lieu_Resistance, situation_Sociale,specialiste_sante, assurance, alcool,cas_professionnel,ciguarette,narguile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations_user);


        mEmail =(EditText) findViewById(R.id.input_email);
        mEmail.setEnabled(false);
        mClass =(EditText) findViewById(R.id.input_password);
        mDateReg =(EditText) findViewById(R.id.idDateRegister);
        mProgressBar =(ProgressBar) findViewById(R.id.progressBar);
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

        sexe=(EditText) findViewById(R.id.input_sexe);
        niveau_Education=(EditText) findViewById(R.id.input_niveauedu);
        lieu_Resistance=(EditText)findViewById(R.id.input_lieuresistance);
        situation_Sociale=(EditText)findViewById(R.id.input_situation_sociale);
        cas_professionnel=(EditText)findViewById(R.id.input_casprofessionnel);
        specialiste_sante=(EditText)findViewById(R.id.input_specialistesante);
        assurance=(EditText)findViewById(R.id.input_assurance);
        alcool=(EditText)findViewById(R.id.input_alcool);
        ciguarette=(EditText)findViewById(R.id.input_ciguarette);
        narguile=(EditText)findViewById(R.id.input_nargile);





        mEmail.setEnabled(false);
        mClass.setEnabled(false);
        mDateReg.setEnabled(false);
        showDialog();
        hideSoftKeyboard();
        extra = getIntent().getExtras();
        if(extra!=null) {
            keyIdPerson =extra.getString("KeyId");
        }else{
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Error Sync", Snackbar.LENGTH_SHORT).show();
        }

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("USERS");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showDialog();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getKey().equals(keyIdPerson)) {
                            Data_User D = snapshot.getValue(Data_User.class);
                            mEmail.setText(D.getEmail());
                            mClass.setText(D.getClass_Type());
                            mDateReg.setText(D.getData_Register());
/////////
                            date_naissance.setText(D.getDate_naissance());
                            poids.setText(D.getPoids());
                            taille.setText(D.getTaille());
                            nb_prs_famille.setText(D.getNb_prs_famille());
                            nb_fils.setText(D.getNb_fils());
                            nb_fils_prs.setText(D.getNb_fils_prs());
                            nb_chambres.setText(D.getNb_chambres());
                            suivi_recommandations.setText(D.getSuivi_recommandations());
                            nb_ciguarette.setText(D.getNb_ciguarette());
                            nb_Narguile.setText(D.getNb_Narguile());

                            sexe.setText(D.getSexe());
                            niveau_Education.setText(D.getNiveau_Education());
                            lieu_Resistance.setText(D.getLieu_Resistance());
                            situation_Sociale.setText(D.getSituation_Sociale());
                            specialiste_sante.setText(D.getSpecialiste_sante());
                            assurance.setText(D.getAssurance());
                            alcool.setText(D.getAlcool());
                            cas_professionnel.setText(D.getCas_professionnel());
                            ciguarette.setText(D.getBolciguarette());
                            narguile.setText(D.getBolnarguile());
                            /////////






                        }
                }
                hideDialog();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }


    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
