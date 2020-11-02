package com.example.covid_nutritionapp.Client;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Data_forms;
import com.example.covid_nutritionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_ShowFormDescriptionClient extends AppCompatActivity {

    TextView eName, eDesc;
    Button bCancel,bContinue;
    FirebaseUser user ;
    String userId ="";
    FirebaseDatabase database;
    DatabaseReference myRef;
    Data_forms currentForm;
    String keyForm;
    Bundle extra;
    ProgressBar mProgressBar;
    Spinner mSpinnerLanguage;
    private ArrayAdapter mArrayAdapterLanguage;
    private String language="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_showform_description_layout);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        eName =(TextView) findViewById(R.id.IdEname);
        eDesc =(TextView) findViewById(R.id.IdEdesc);
        bCancel=(Button) findViewById(R.id.IdBcancel);
        bContinue=(Button) findViewById(R.id.IdBContunie);
        mSpinnerLanguage=(Spinner)findViewById(R.id.IdSpinnerlanguage);
        mProgressBar = findViewById(R.id.progressBar);

        showDialog();
        extra = getIntent().getExtras();
        if(extra!=null) {
            keyForm =extra.getString("keyForm");
        }



        String [] listLanguage=getResources().getStringArray(R.array.languages);

        mArrayAdapterLanguage=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,listLanguage);
        if(mSpinnerLanguage!=null) {
            mSpinnerLanguage.setAdapter(mArrayAdapterLanguage);
        }


        mSpinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                language = mSpinnerLanguage.getSelectedItem().toString();
                if(!language.equals("")) {
                    eDesc.setText(currentForm.getDescForm_lang(language));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });













        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("FORMS");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data_forms D = snapshot.getValue(Data_forms.class);
                    D.setKey_value(snapshot.getKey());

                    if(D.getKey_value().equals(keyForm)){
                        currentForm =D ;
                    }


                }
                eName.setText(currentForm.getNameform());
                eDesc.setText(currentForm.getDescform());
                hideDialog();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                Intent intent = new Intent(Activity_ShowFormDescriptionClient.this, Activity_ShowFormClient.class);
                intent.putExtra("keyForm",currentForm.getKey_value());
                startActivityForResult(intent,1);
                hideDialog();
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }





    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

}
