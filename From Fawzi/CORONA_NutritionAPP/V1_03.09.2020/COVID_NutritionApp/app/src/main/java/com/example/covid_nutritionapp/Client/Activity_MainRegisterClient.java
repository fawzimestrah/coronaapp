package com.example.covid_nutritionapp.Client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import static android.text.TextUtils.isEmpty;

public class Activity_MainRegisterClient extends AppCompatActivity {
    private static final String TAG = "RegisterActivity" ;
    //widgets
    private EditText mEmail, mPassword, mConfirmPassword;
    private ProgressBar mProgressBar;
    private Button Bregister;

//type account
    private String typeAccount ="C1";
    //vars
    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        Bregister=(Button)findViewById(R.id.btn_register);
        mDb = FirebaseFirestore.getInstance();

        hideSoftKeyboard();


        Bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: attempting to register.");

                //check for null valued EditText fields
                if(!isEmpty(mEmail.getText().toString())
                        && !isEmpty(mPassword.getText().toString())
                        && !isEmpty(mConfirmPassword.getText().toString())){

                    //check if passwords match
                    if(mPassword.getText().toString().equals(mConfirmPassword.getText().toString())){

                        //Initiate registration task
                        registerNewEmail(mEmail.getText().toString(), mPassword.getText().toString());
                    }else{
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar.make(parentLayout, "Passwords do not Match", Snackbar.LENGTH_SHORT).show();
                        hideDialog();
                    }

                }else{
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "You must fill out all the fields", Snackbar.LENGTH_SHORT).show();
                    hideDialog();
                }

            }
        });
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
                            Data_User.Insert_User(Client);
                            redirectLoginScreen();

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"error: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
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

}
