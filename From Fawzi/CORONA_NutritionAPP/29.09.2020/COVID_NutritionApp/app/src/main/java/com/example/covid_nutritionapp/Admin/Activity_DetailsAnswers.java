package com.example.covid_nutritionapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Data_GroupItem;
import com.example.covid_nutritionapp.Data_Question;
import com.example.covid_nutritionapp.Data_UserAnswer;
import com.example.covid_nutritionapp.Data_answer;
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

import java.util.ArrayList;

public class Activity_DetailsAnswers extends AppCompatActivity {


    EditText Ename,Edesc,ValueQuest,EGroup;;
    Button BDelete,Bnext,Bprev;
    TextView textQues;
    ImageView Help_Info;
    FirebaseUser user ;
    String userId ="";
    FirebaseDatabase database;
    DatabaseReference myRef,myRef_details, myRef_Reponse, myRef_Reponse_details ;
    Data_forms currentForm;
    String keyIdPerson, keyForm;
    Bundle extra;
    ArrayList<Data_Question> listQuest;
    ArrayList<Data_answer> listReponse;
    ProgressBar mProgressBar;
    int c=0, nbQues =0,index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_result_details_answer_layout);
        user= FirebaseAuth.getInstance().getCurrentUser();
        userId =user.getUid();
        mProgressBar=findViewById(R.id.progressBar);
        Ename=findViewById(R.id.IdEname);
        Edesc=findViewById(R.id.IdEdesc);
        BDelete=findViewById(R.id.IdBDelete);
        Bnext=findViewById(R.id.Idnext);
        Bprev=findViewById(R.id.IdPrev);
        ValueQuest=findViewById(R.id.IdEQues);
        EGroup=findViewById(R.id.IdGroup);
        textQues =findViewById(R.id.IdTQues);
        Help_Info=findViewById(R.id.IdInfoPerson);

        listQuest =new ArrayList<Data_Question>();
        listReponse =new ArrayList<Data_answer>();

        Ename.setEnabled(false);
        Edesc.setEnabled(false);
        EGroup.setEnabled(false);
        ValueQuest.setEnabled(false);
        showDialog();

        extra = getIntent().getExtras();
        if(extra!=null) {
            keyForm =extra.getString("keyForm");
            keyIdPerson =extra.getString("keyId");

        }


        Help_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(keyIdPerson ==null) {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Error snc", Snackbar.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(Activity_DetailsAnswers.this, Activity_InformationUser.class);
                    intent.putExtra("KeyId", keyIdPerson);
                    startActivity(intent);
                }
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
                Ename.setText(currentForm.getNameform());
                Edesc.setText(currentForm.getDescform());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        myRef_details = database.getReference("FORMS");
        myRef.child(keyForm).child("QUESTIONS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listQuest.clear();
                nbQues =0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());

                    myRef_details.child(keyForm).child("QUESTIONS").child(D.getName_group()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Data_Question Dq = snapshot.getValue(Data_Question.class);
                                Dq.setKey_value(snapshot.getKey());
                                listQuest.add(Dq);
                                // create data answer for each quest and add keyQuest
                                listReponse.add(new Data_answer(Dq.getQuestion() ,Dq.getKey_value()));
                                nbQues++;
                            }
                            // start

                            textQues.setText(listQuest.get(0).getQuestion());
                            EGroup.setText(listQuest.get(0).getGroup());

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




        myRef_Reponse= database.getReference("FORMS_Data");
        myRef_Reponse_details = database.getReference("FORMS_Data");
        Data_UserAnswer User=new Data_UserAnswer(keyIdPerson);

        myRef_Reponse.child(keyForm).child(User.getUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());
                    myRef_Reponse_details.child(keyForm).child(keyIdPerson).child(D.getName_group()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Data_answer Dq = snapshot.getValue(Data_answer.class);
                                Dq.setKey_value(snapshot.getKey());
                                if(c< nbQues && listReponse.get(c).getKey_value().equals(Dq.getKey_value()) ){
                                    listReponse.get(c).setKey_value(Dq.getKey_value());
                                    listReponse.get(c).setUser(Dq.getUser());
                                    listReponse.get(c).setKey_form(Dq.getKey_form());
                                    listReponse.get(c).setAnswer(Dq.getAnswer());
                                    listReponse.get(c).setNum_Item(Dq.getNum_Item());
                                    listReponse.get(c).setGroup(Dq.getGroup());
                                    listReponse.get(c).setGrpAnswers(Dq.getGrpAnswers());
                                    c++;

                                }
                                if(listReponse.size()!=0 && listQuest.size()!=0 && listReponse.size()>c && listQuest.size()>c)  {
                                    showQuestion(0);
                                }
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                // start

                hideDialog();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        Bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listQuest.size() > index+1) {
                    index++;
                    showQuestion(index);
                }
            }
        });


        Bprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(index>0) {
                    index--;
                    showQuestion(index);


                }

            }
        });

        BDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(getApplicationContext(), Activity_MainClient.class);
              //  setResult(2,intent);
             //   finish();

            }
        });




    }


    private void  showQuestion(int index){
        textQues.setText(listQuest.get(index).getQuestion()+"  " + (index+1) + "/"+ nbQues +":");
        ValueQuest.setText(listReponse.get(index).getAnswer());
        EGroup.setText(listQuest.get(index).getGroup());
        if(listReponse.get(index).getGrpAnswers().size()>0){
            String checkboxChoix="";
            for(int ch=0;ch<listReponse.get(index).getGrpAnswers().size();ch++) {
                checkboxChoix+=listReponse.get(index).getGrpAnswers().get(ch)+" .  \n";
            }
            ValueQuest.setText(checkboxChoix);
            Toast.makeText(getApplicationContext(),"checkbox"+ checkboxChoix,Toast.LENGTH_LONG).show();
        }

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

