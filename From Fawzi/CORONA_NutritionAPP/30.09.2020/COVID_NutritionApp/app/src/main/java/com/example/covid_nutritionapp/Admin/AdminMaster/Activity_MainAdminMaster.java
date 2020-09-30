package com.example.covid_nutritionapp.Admin.AdminMaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Admin.Activity_MainAdmin;
import com.example.covid_nutritionapp.Activity_LoginActivity;
import com.example.covid_nutritionapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_MainAdminMaster extends AppCompatActivity {



    ImageView IVaddAdmin,IVdetailsForm,Blogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_admin_master_layout);
        IVaddAdmin=(ImageView)findViewById(R.id.IdAddAdmin);
        IVdetailsForm=(ImageView)findViewById(R.id.IdDetailsForms);
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

        IVdetailsForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_MainAdminMaster.this, Activity_MainAdmin.class);
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


    }


    private void SingOUT(){
        FirebaseAuth.getInstance()
                .signOut();

    }

}
