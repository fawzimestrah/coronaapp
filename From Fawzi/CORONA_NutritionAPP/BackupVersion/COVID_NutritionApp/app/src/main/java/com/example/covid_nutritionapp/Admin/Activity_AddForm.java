package com.example.covid_nutritionapp.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.covid_nutritionapp.Data_Question;
import com.example.covid_nutritionapp.Data_forms;
import com.example.covid_nutritionapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.chrono.Era;
import java.util.ArrayList;
import java.util.HashMap;

import static android.text.TextUtils.isEmpty;

public class Activity_AddForm extends AppCompatActivity {
    private EditText Ename,Edesc;
    private HashMap<String,String> descLang=new HashMap<String,String>();;
    private Button  bSubmit,bDelete;
    private Spinner mSpinnerGroupUser,mSpinnerLanguage;
    private ArrayList<Data_Question> listQuest;
    private FirebaseUser user ;
    private int c=0,nb_Ques=0;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<Data_GroupADMIN> ListGroup;
    private ArrayList<String> listnamegroup;
    private String [] listTypeQuest;
    private ArrayAdapter mArrayAdapterGroup;
    private ArrayAdapter mArrayAdapterTypeQuest;
    private String language="";
    private ArrayAdapter mArrayAdapterLanguage;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_form_layout);

        hideSoftKeyboard();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Ename=findViewById(R.id.IdEname);
        Edesc=findViewById(R.id.IdEdesc);
        bSubmit =findViewById(R.id.IdBSubmit);
        mSpinnerGroupUser =findViewById(R.id.IdSpinnernamegroup);
        mSpinnerLanguage=findViewById(R.id.IdSpinnerlanguage);
        bDelete=findViewById(R.id.IdBdelete);
        listQuest =new ArrayList<Data_Question>();
        ListGroup=new ArrayList<Data_GroupADMIN>();
        listnamegroup=new ArrayList<String>();
        mProgressBar=findViewById(R.id.progressBar);

        bSubmit.setEnabled(false);
        String [] listLanguage=getResources().getStringArray(R.array.languages);

        mArrayAdapterLanguage=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,listLanguage);
        if(mSpinnerLanguage!=null) {
            mSpinnerLanguage.setAdapter(mArrayAdapterLanguage);
        }


        mSpinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!language.equals("")) {
                    descLang.put(language, Edesc.getText().toString());

                }
                language = mSpinnerLanguage.getSelectedItem().toString();
                Edesc.setText(descLang.get(language));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        listTypeQuest=new String[3];
        listTypeQuest[0]="TextField";
        listTypeQuest[1]="RadioButton";
        listTypeQuest[2]="CheckBox";
        mArrayAdapterTypeQuest = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,listTypeQuest);
        mArrayAdapterTypeQuest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        next_Quest(0);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("GroupAdmin");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListGroup.clear();
                listnamegroup.clear();
                showDialog();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    final Data_GroupADMIN D = snapshot.getValue(Data_GroupADMIN.class);
                    D.setKey_Group(snapshot.getKey());
                    Log.e("listgroup","\\\\aa  "+D.getKey_Group());

                    DatabaseReference myRefUser = database.getReference("GroupAdmin").child(snapshot.getKey()).child("Members");
                    myRefUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //snapshot.getKey();
                                String Dkey = snapshot.getValue(String.class);
                                Log.e("listgroup","\\\\bb  "+Dkey);
                                if(Dkey.equals(user.getUid())){
                                    ListGroup.add(D);
                                    listnamegroup.add(D.getNameGroup());
                                    Log.e("listgroup",D.getNameGroup()+"");
                                }
                            }
                            String [] Array_namegroup=new String[listnamegroup.size()];
                            for(int i=0;i<listnamegroup.size();i++){
                                Array_namegroup[i]=listnamegroup.get(i);
                            }
                            mArrayAdapterGroup = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,Array_namegroup);
                            mArrayAdapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            if(mSpinnerGroupUser !=null) {
                                mSpinnerGroupUser.setAdapter(mArrayAdapterGroup);

                                bSubmit.setEnabled(true);
                                hideDialog();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                if (checkField()) {
                    if(!language.equals("")) {
                        descLang.put(language, Edesc.getText().toString());

                    }
                    String KeyGroup = ListGroup.get(mSpinnerGroupUser.getSelectedItemPosition()).getKey_Group();

                    String keyForm = Data_forms.Insert_form(Ename.getText().toString(), Edesc.getText().toString(),descLang, user.getUid(), KeyGroup, listQuest);
                    Intent intent = new Intent(getApplicationContext(), Activity_EditForm.class);
                    intent.putExtra("keyForm", keyForm);
                    intent.putExtra("fromActivity","ADD");
                    startActivityForResult(intent, 2);
                    finish();
                    hideDialog();
                }
            }
        });

    }


    public void next_Quest(int index){
        nb_Ques++;
        Data_Question D1=new Data_Question("EN"," ");
        D1.addLanguage_Question("AR"," ");
        D1.setGroup("Default");
        D1.setNum_Item(nb_Ques);
            D1.setTypeQuestion(listTypeQuest[0]);
        listQuest.add(D1);
    }





    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }



    private boolean checkField(){
        if( isEmpty(Ename.getText().toString()) || isEmpty(Edesc.getText().toString()) ){
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "You didn't fill in all the fields. ", Snackbar.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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


