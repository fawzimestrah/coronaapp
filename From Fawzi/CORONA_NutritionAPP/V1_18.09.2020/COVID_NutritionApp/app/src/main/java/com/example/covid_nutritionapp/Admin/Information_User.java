package com.example.covid_nutritionapp.Admin;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Data_forms;
import com.example.covid_nutritionapp.R;
import com.example.covid_nutritionapp.UserData;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class Information_User extends AppCompatActivity {

    private EditText mEmail, mClass, mDatereg;
    private ProgressBar mProgressBar;
    private Bundle extra;
    private String KeyId_person;
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
            KeyId_person=extra.getString("KeyId");

            //          View parentLayout = findViewById(android.R.id.content);
//            Snackbar.make(parentLayout, "KeyId Sync: "+KeyId_person , Snackbar.LENGTH_SHORT).show();

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
                        if(snapshot.getKey().equals(KeyId_person)) {
                            UserData D = snapshot.getValue(UserData.class);
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
