package com.example.covid_nutritionapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Admin.AdminMaster.MainAdminMaster;
import com.example.covid_nutritionapp.Admin.MainAdmin;
import com.example.covid_nutritionapp.Client.MainClient;
import com.example.covid_nutritionapp.Client.Main_RegisterClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.text.TextUtils.isEmpty;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static final int ERROR_DIALOG_REQUEST = 9001;
    //start variables added to test permission
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;
    private boolean mLocationPermissionGranted = false;
    FirebaseDatabase database;
    DatabaseReference myRef;



    EditText EUsername,EPassword;
    Button Bregister,BLogin;
    ProgressBar mProgressBar;
    String Id_USER="";
    String TYPE="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();


        Bregister=(Button)findViewById(R.id.IdButtonRegister);
        EUsername=(EditText)findViewById(R.id.IdEUsername);
        EPassword=(EditText)findViewById(R.id.IdEPassword);
        BLogin=(Button) findViewById(R.id.IdLogin);
        mProgressBar = findViewById(R.id.progressBar);
        showDialog();

        //Log.d(TAG, "onCreate before setup fire base auth.");
        setupFirebaseAuth();
        //Log.d(TAG, "onCreate after setup fire base auth.");


        hideSoftKeyboard();



        BLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });


        Bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Main_RegisterClient.class);
                startActivity(intent);

            }
        });


    }

    private void setupFirebaseAuth() {

        //Log.d(TAG, "setupFirebaseAuth: started.");
        showDialog();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Authenticated with: " + user.getEmail(), Snackbar.LENGTH_SHORT).show();

                    // Determiner si login Client ou Admin
                    String Type="";
                    Type=Determiner_Type(user.getUid()); // + LOGIN
                    hideDialog();

                }else{
                    // User is signed out
                  //  Toast.makeText(LoginActivity.this, "Authenticated with: None", Toast.LENGTH_LONG).show();
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "No Athenticated: " , Snackbar.LENGTH_SHORT).show();

                    hideDialog();
                }
            }


        };
    }

    private void signIn() {

        //check if the fields are filled out
        if(!isEmpty(EUsername.getText().toString())
                && !isEmpty(EPassword.getText().toString())) {

            //Log.d(TAG, "onClick: attempting to authenticate.");

            showDialog();
            FirebaseAuth.getInstance().signInWithEmailAndPassword(EUsername.getText().toString(),
                    EPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                             FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
//                                Toast.makeText(getApplicationContext(),"sign in "+user.getUid(),Toast.LENGTH_LONG).show();

                                hideDialog();
                            setupFirebaseAuth();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Authentication Failed", Snackbar.LENGTH_SHORT).show();
                    hideDialog();

                }
            });
        }else{
            //Toast.makeText(LoginActivity.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "You didn't fill in all the fields.", Snackbar.LENGTH_SHORT).show();
            hideDialog();
        }

    }

        @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart before addAuthStateListener");
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
        Log.d(TAG, "onStart after addAuthStateListener");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }


    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }


    private String Determiner_Type(String idUser){
//        Toast.makeText(getApplicationContext(),""+idUser,Toast.LENGTH_LONG).show();

        Id_USER=idUser;
         myRef = database.getReference("USERS");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(Id_USER.equals(snapshot.getKey()) ){
//                           Toast.makeText(getApplicationContext(),"hehe"+snapshot.getKey(),Toast.LENGTH_LONG).show();
                            UserData U=snapshot.getValue(UserData.class);
                            TYPE=U.getClass_Type();
//                            Toast.makeText(getApplicationContext(),"typehehe : "+TYPE,Toast.LENGTH_LONG).show();
                            LOGIN_MAIN(TYPE);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        return TYPE;
        }

        private void LOGIN_MAIN(String type){
            Intent intent;

        switch (type){
            case "A":
                intent = new Intent(LoginActivity.this, MainAdminMaster.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case "C1":
                intent = new Intent(LoginActivity.this, MainClient.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case "AdminUser":
                intent = new Intent(LoginActivity.this, MainAdmin .class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;

        }
        }



}
