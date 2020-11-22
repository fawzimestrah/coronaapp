package com.example.covid_nutritionapp;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.covid_nutritionapp.Admin.AdminMaster.Activity_MainAdminMaster;
import com.example.covid_nutritionapp.Admin.Activity_MainAdmin;
import com.example.covid_nutritionapp.Admin.Data_Backup;
import com.example.covid_nutritionapp.Client.Activity_MainClient;
import com.example.covid_nutritionapp.Client.Activity_MainRegisterClient;
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

public class Activity_LoginActivity extends AppCompatActivity {

    private static final String TAG = "Activity_LoginActivity";
    private final int REQUEST_PERMISSIONS_REQUEST_CODE=44;

    private FirebaseAuth.AuthStateListener mAuthListener;
    public static final int ERROR_DIALOG_REQUEST = 9001;
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
    ImageView img;
    TextView about;
    boolean locationPermission=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

//        Data_Backup Backup= new Data_Backup();
  //      Backup.checkPermission(getApplicationContext(),this);
        checkPermissionApp();


        database = FirebaseDatabase.getInstance();
        Bregister=(Button)findViewById(R.id.IdButtonRegister);
        EUsername=(EditText)findViewById(R.id.IdEUsername);
        EPassword=(EditText)findViewById(R.id.IdEPassword);
        BLogin=(Button) findViewById(R.id.IdLogin);
        mProgressBar = findViewById(R.id.progressBar);
        img=findViewById(R.id.IdIMG);
        about=findViewById(R.id.idtabout);
        SharedPreferences sharedPreferences = getSharedPreferences("com.CovidNutrition.application.prefs", getApplication().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean firstOpen=sharedPreferences.getBoolean("firstOpen", true);
        if(firstOpen) {
            Intent intent = new Intent(Activity_LoginActivity.this, Activity_About.class);
            intent.putExtra("firstOpen",true);
            startActivityForResult(intent,1);
            finish();
        }
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_LoginActivity.this, Activity_About.class);
                intent.putExtra("firstOpen",false);
                startActivityForResult(intent,1);

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
   //             Intent intent = new Intent(Activity_LoginActivity.this, Activity_About.class);
 //               startActivityForResult(intent,1);

            }
        });
        showDialog();
        setupFirebaseAuth();
        hideSoftKeyboard();
        BLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                signIn();
            }
        });
        Bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_LoginActivity.this, Activity_MainRegisterClient.class);
                startActivityForResult(intent, 2);
         }
        });
    }

    private void setupFirebaseAuth() {
        showDialog();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Authenticated with: " + user.getEmail(), Snackbar.LENGTH_SHORT).show();
                   // hideDialog();

                    // Determiner si login Client ou Admin
                    Determiner_Type(user.getUid()); // + LOGIN
//                    hideDialog();

                }
                else{
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "No Athenticated: " , Snackbar.LENGTH_SHORT).show();
                    hideDialog();
                }
            }
        };
    }

    private void signIn() {
        if(!isEmpty(EUsername.getText().toString())
                && !isEmpty(EPassword.getText().toString())) {
            showDialog();
            FirebaseAuth.getInstance().signInWithEmailAndPassword(EUsername.getText().toString(),
                    EPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                             FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                             hideDialog();
                             setupFirebaseAuth();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Authentication Failed", Snackbar.LENGTH_SHORT).show();
                    hideDialog();
                }
            });
        }
        else{
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "You didn't fill in all the fields.", Snackbar.LENGTH_SHORT).show();
            hideDialog();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
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


    private void Determiner_Type(String idUser){
        Id_USER=idUser;
        myRef = database.getReference("USERS");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(Id_USER.equals(snapshot.getKey()) ){
                        Data_User U=snapshot.getValue(Data_User.class);

                        TYPE=U.getClass_Type();
                        LOGIN_MAIN(TYPE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            });
    }

    private void LOGIN_MAIN(String type){
        Intent intent;
        switch (type){
            case "A":
                intent = new Intent(Activity_LoginActivity.this, Activity_MainAdminMaster.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case "C1":
                intent = new Intent(Activity_LoginActivity.this, Activity_MainClient.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case "AdminUser":
                intent = new Intent(Activity_LoginActivity.this, Activity_MainAdmin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public  void checkPermissionApp() {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);

        }else{
            locationPermission=true;
        }

    }


}
