package com.example.covid_nutritionapp.Client;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private int REQUEST_RESUTLT_MAIN=2;
    private int REQUEST_CONTINUE=1;

    TextView eName;
    WebView eDesc;
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
        eDesc =(WebView) findViewById(R.id.IdEdesc);
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
                if( !language.equals("")  && !language.equals(" ")  ) {
//                    eDesc.setText(currentForm.getDescForm_lang(language));
                    if(language.equals("AR")){
                        eDesc.loadData("<html dir=\"rtl\" lang=\"ar\">\n" +
                                "        <head>\n" +
                                "        </head>\n" +
                                "        <body>\n" +
                                "        <p align=\"justify\">\n" +
                                currentForm.getDescForm_lang(language)
                                +"          </p>\n" +
                                "        </body>\n" +
                                "        </html>\n" +
                                "", "text/html; charset=UTF-8", "utf-8");

                    }else {
                        eDesc.loadData("<html>\n" +
                                "        <head>\n" +
                                "        </head>\n" +
                                "        <body>\n" +
                                "        <p align=\"justify\">\n" +
                                currentForm.getDescForm_lang(language)
                                + "          </p>\n" +
                                "        </body>\n" +
                                "        </html>\n" +
                                "", "text/html; charset=UTF-8", "utf-8");
                    }
                }else{
//                    eDesc.setText("No Translate" );
                    eDesc.loadData("No Translate", "text/html; charset=UTF-8", "utf-8");

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
                showDialog();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data_forms D = snapshot.getValue(Data_forms.class);
                    D.setKey_value(snapshot.getKey());

                    if(D.getKey_value().equals(keyForm)){
                        currentForm =D ;
                    }


                }
                eName.setText(currentForm.getNameform());
//                eDesc.setText(currentForm.getDescform());
                eDesc.loadData(currentForm.getDescform(), "text/html; charset=UTF-8", "utf-8");

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
                setResult(REQUEST_RESUTLT_MAIN);
                showDialog();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CONTINUE){ // showFormClient
            setResult(REQUEST_RESUTLT_MAIN);
            finish();
        }
    }
}
