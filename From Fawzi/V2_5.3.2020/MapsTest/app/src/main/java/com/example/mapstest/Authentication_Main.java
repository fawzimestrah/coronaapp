package com.example.mapstest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Authentication_Main extends AppCompatActivity {
EditText Ephon,Everf;
Button Bsend,Bverf;
String codeSend;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();



        setContentView(R.layout.test_authentication);
        Ephon=findViewById(R.id.IdEd_NBphone);
        Everf=findViewById(R.id.IdEd_VerifPhoness);
        Bsend=findViewById(R.id.IdButton_Send);
        Bverf=findViewById(R.id.IdButton_Verification);

        Bsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send_Verfication();

            }
        });


        Bverf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            VerifySignCode();
            }
        });

    }


private void VerifySignCode(){

     String code= Everf.getText().toString();
    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSend, code);
    signInWithPhoneAuthCredential(credential);
}



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Succ"+mAuth.getUid(),Toast.LENGTH_LONG).show();




                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"Incorrect",Toast.LENGTH_LONG).show();


                            }
                        }
                    }
                });
    }





private   void Send_Verfication(){

    String nbPhone=Ephon.getText().toString();
    if(nbPhone.isEmpty()){
        Ephon.setError("Phone is required");
        Ephon.requestFocus();
    }
    if(nbPhone.length()<8){
        Ephon.setError("Phone is required");
        Ephon.requestFocus();
    }











    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSend=s ; // code


        }
    };

    PhoneAuthProvider.getInstance().verifyPhoneNumber(
            nbPhone, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks); // OnVerificationStateChangedCallbacks


}




}


