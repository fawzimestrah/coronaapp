package com.example.covid_nutritionapp.Admin.AdminMaster;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Admin.Data_GroupADMIN;
import com.example.covid_nutritionapp.Admin.Activity_MainAdmin;
import com.example.covid_nutritionapp.R;
import com.example.covid_nutritionapp.User;
import com.example.covid_nutritionapp.Data_User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

public class Activity_MainRegisterAdmin extends AppCompatActivity {

    private static final String TAG = "RegisterActivity" ;
    //widgets
    private EditText mEmail, mPassword, mConfirmPassword;
    private CheckBox ChGroup;
    private ProgressBar mProgressBar;
    private Button Bregister;
    ImageView bgapp;
    LinearLayout texthome;
    RelativeLayout menus;
    LinearLayout layoutGroup,Lcurrent ;
    Animation frombottom;
    private String TYPE_Account="AdminUser";
    //vars
    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<Data_GroupADMIN> ListGroupAdmin,ListGroupSelected;
    private  ImageView AddGroup;
    private int index_checkbox=0;
    private ArrayList<CheckBox> cb;
    private DI_Adapter_Checkbox mArrayAdapter;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);


        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        Bregister=(Button)findViewById(R.id.btn_register);
    //    ChGroup=(CheckBox)findViewById(R.id.IdCheckGroup);
        mDb = FirebaseFirestore.getInstance();
        layoutGroup = (LinearLayout)findViewById(R.id.IdLineairGroup);

        ListGroupAdmin=new ArrayList<Data_GroupADMIN>();
        cb=new ArrayList<CheckBox>();
        AddGroup=findViewById(R.id.IdAddGroupAdmn);

        GridView mgridView=findViewById(R.id.IdGridviewCheckbox);

        AddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_MainRegisterAdmin.this, Activity_AddGroup.class);
                startActivityForResult(intent,1);

            }
        });


        hideSoftKeyboard();



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("GroupAdmin");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListGroupAdmin.clear();
               layoutGroup.removeAllViews();
                index_checkbox=0;
                cb.clear();
                Lcurrent = new LinearLayout(getApplicationContext());
                Lcurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Lcurrent.setOrientation(LinearLayout.HORIZONTAL);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();

                    Data_GroupADMIN D = snapshot.getValue(Data_GroupADMIN.class);
                    D.setKey_Group(snapshot.getKey());
                    ListGroupAdmin.add(D);

              // Toast.makeText(getApplicationContext(),"asac"+ListGroupAdmin.size(),Toast.LENGTH_LONG).show();
                    index_checkbox++;

                }

                mArrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mArrayAdapter = new DI_Adapter_Checkbox(getApplicationContext(), R.layout.cust_adapt_checkbox,ListGroupAdmin);

        mArrayAdapter.notifyDataSetChanged();
        if(mgridView !=null)
        {
            mgridView.setAdapter(mArrayAdapter);
        }

        mgridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                Toast.makeText(getApplicationContext()," mgriview ",Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: attempting to register.");

                //check for null valued EditText fields
                if(!isEmpty(mEmail.getText().toString())
                        && !isEmpty(mPassword.getText().toString())
                        && !isEmpty(mConfirmPassword.getText().toString()) && isChekedList(ListGroupAdmin)){

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


                           /* newUserRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    hideDialog();

                                    if(task.isSuccessful()){
                                        redirectLoginScreen();
                                    }else{
                                        View parentLayout = findViewById(android.R.id.content);
                                        Snackbar.make(parentLayout, "Something went wrong.\nerror:"+task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                        hideDialog();
                                    }
                                }

                            });
*/

                            newUserRef.set(user);
                            Data_User userAdmin=new Data_User(FirebaseAuth.getInstance().getUid(),email,TYPE_Account);
                            Data_User.insert_User_Admin(userAdmin,ListGroupAdmin);//
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
        //Toast.makeText(getApplicationContext(),"Register succ",Toast.LENGTH_LONG).show();

        Intent intent = new Intent(Activity_MainRegisterAdmin.this, Activity_MainAdmin.class);
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


    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:{
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
                break;
            }
        }
    }




    public  boolean isChekedList(ArrayList<Data_GroupADMIN> ListCH){
        for(int i=0;i<ListCH.size();i++){
            if(ListCH.get(i).isChecked()){
                return true;
            }
        }

        return false;
    }

}
