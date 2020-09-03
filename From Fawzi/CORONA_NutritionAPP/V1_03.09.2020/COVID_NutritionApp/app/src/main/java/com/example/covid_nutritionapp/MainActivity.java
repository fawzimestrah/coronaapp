package com.example.covid_nutritionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.covid_nutritionapp.Admin.MainAdmin;
import com.example.covid_nutritionapp.Client.MainClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText EUsername,EPassword;
    Button BloginAdmin,BloginClient,Bregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BloginAdmin=(Button)findViewById(R.id.IdButtonLogin);
        BloginClient=(Button)findViewById(R.id.IdButtonLoginClient);
        Bregister=(Button)findViewById(R.id.IdButtonRegister);
        EUsername=(EditText)findViewById(R.id.IdEUsername);
        EPassword=(EditText)findViewById(R.id.IdEPassword);




        BloginAdmin.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(MainActivity.this, MainAdmin.class);
                                          startActivity(intent);
                                      }
                                  }
        );

        BloginClient.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(MainActivity.this, MainClient.class);
                                               startActivity(intent);
                                           }
                                       }
        );

    }




}
