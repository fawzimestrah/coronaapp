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

    private EditText mEmail, mClass, mDatereg;
    private ProgressBar mProgressBar;
    private Bundle extra;
    private String keyIdPerson;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations_user);

        mEmail =(EditText) findViewById(R.id.input_email);
        mClass =(EditText) findViewById(R.id.input_password);
        mDatereg =(EditText) findViewById(R.id.idDateRegister);
        mProgressBar =(ProgressBar) findViewById(R.id.progressBar);

        mEmail.setEnabled(false);
        mClass.setEnabled(false);
        mDatereg.setEnabled(false);
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
                            mDatereg.setText(D.getData_Register());

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
