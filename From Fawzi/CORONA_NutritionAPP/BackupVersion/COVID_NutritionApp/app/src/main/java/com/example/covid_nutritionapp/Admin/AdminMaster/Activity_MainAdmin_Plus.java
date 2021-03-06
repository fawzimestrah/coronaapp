package com.example.covid_nutritionapp.Admin.AdminMaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Activity_LoginActivity;
import com.example.covid_nutritionapp.Admin.Activity_AddForm;
import com.example.covid_nutritionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_MainAdmin_Plus extends AppCompatActivity {

    ImageView IVaddForm, Blogout,BdetailsForm;

    GridView mListView;

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main_layout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mListView=(GridView) findViewById(R.id.idLform);
        IVaddForm=findViewById(R.id.IdAddForm);
        BdetailsForm=findViewById(R.id.IdDetailsForm);
        Blogout=findViewById(R.id.idLogout);

        Blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingOUT();

                Intent intent = new Intent(Activity_MainAdmin_Plus.this, Activity_LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        IVaddForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_MainAdmin_Plus.this, Activity_AddForm_Plus.class);
                startActivityForResult(intent,1);

            }
        });

        BdetailsForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_MainAdmin_Plus.this, Activity_ShowForms_Plus.class);
                startActivityForResult(intent,3);

            }
        });



    }


    private void SingOUT(){
        FirebaseAuth.getInstance()
                .signOut();

    }



    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
