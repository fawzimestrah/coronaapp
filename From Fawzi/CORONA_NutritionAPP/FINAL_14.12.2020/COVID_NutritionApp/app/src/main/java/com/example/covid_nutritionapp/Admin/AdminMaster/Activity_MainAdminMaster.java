package com.example.covid_nutritionapp.Admin.AdminMaster;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Admin.Activity_MainAdmin;
import com.example.covid_nutritionapp.Activity_LoginActivity;
import com.example.covid_nutritionapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_MainAdminMaster extends AppCompatActivity {



    ImageView IVaddAdmin,IVdetailsForm,Blogout,IVaddGroup,IVdetailsGroup,IVexportToJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_admin_master_layout);
        IVaddAdmin=(ImageView)findViewById(R.id.IdAddAdmin);
        IVdetailsForm=(ImageView)findViewById(R.id.IdDetailsForms);
        IVaddGroup=(ImageView)findViewById(R.id.addGroup);
        IVdetailsGroup=(ImageView)findViewById(R.id.groupDetails);
        IVexportToJSON=(ImageView)findViewById(R.id.idexportJson);
        Blogout=(ImageView)findViewById(R.id.idLogout);

        Blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingOUT();
                Intent intent = new Intent(Activity_MainAdminMaster.this, Activity_LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        IVexportToJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportJson();
            }
        });

        IVdetailsForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_MainAdminMaster.this, Activity_MainAdmin_Plus.class);
               // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent,1);
                //finish();


            }
        });

        IVaddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_MainAdminMaster.this, Activity_MainRegisterAdmin.class);
               // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent,2);
                //finish();


            }
        });


        IVaddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_MainAdminMaster.this, Activity_AddGroupAdmin.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent,3);

            }
        });

        IVdetailsGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_MainAdminMaster.this, Activity_ShowAllGroupAdmin.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent,4);

            }
        });

    }

    private void exportJson(){
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://covid-nutrition20.firebaseio.com/.json?download"));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private void SingOUT(){
        FirebaseAuth.getInstance()
                .signOut();

    }

}


