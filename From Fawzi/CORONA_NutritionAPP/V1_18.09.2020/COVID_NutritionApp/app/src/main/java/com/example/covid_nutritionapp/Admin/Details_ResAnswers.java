package com.example.covid_nutritionapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class Details_ResAnswers extends AppCompatActivity {


    EditText Ename,Edesc,ValueQuest,EGroup;;
    Button BDelete,Bnext,Bprev;
    TextView TQues ;
    ImageView Help_Info;
    FirebaseUser user ;
    String USERID="";
    FirebaseDatabase database;
    DatabaseReference myRef,myRef_details, myRef_Reponse, myRef_Reponse_details ;
    Data_forms CurrentForm;
    String KeyId_person,KeyForm;
    Bundle extra;
    ArrayList<Data_Question> ListQuest;
    ArrayList<Data_answer> ListReponse;

    ProgressBar mProgressBar;
    int c=0,nb_Ques=0,index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_result_details_answer);
        user= FirebaseAuth.getInstance().getCurrentUser();
        USERID=user.getUid();
        mProgressBar=findViewById(R.id.progressBar);
        Ename=findViewById(R.id.IdEname);
        Edesc=findViewById(R.id.IdEdesc);
        BDelete=findViewById(R.id.IdBDelete);
        Bnext=findViewById(R.id.Idnext);
        Bprev=findViewById(R.id.IdPrev);
        ValueQuest=findViewById(R.id.IdEQues);
        EGroup=findViewById(R.id.IdGroup);
        TQues=findViewById(R.id.IdTQues);
        Help_Info=findViewById(R.id.IdInfoPerson);

        ListQuest=new ArrayList<Data_Question>();
        ListReponse=new ArrayList<Data_answer>();

        Ename.setEnabled(false);
        Edesc.setEnabled(false);
        EGroup.setEnabled(false);
        ValueQuest.setEnabled(false);
        showDialog();

        extra = getIntent().getExtras();
        if(extra!=null) {
            KeyForm=extra.getString("keyForm");
            KeyId_person=extra.getString("keyId");

        }



        Help_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KeyId_person==null) {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Error snc", Snackbar.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(Details_ResAnswers.this, Information_User.class);
                    intent.putExtra("KeyId", KeyId_person);
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

                    if(D.getKey_value().equals(KeyForm)){
                        CurrentForm=D ;
                    }


                }
                Ename.setText(CurrentForm.getNameform());
                Edesc.setText(CurrentForm.getDescform());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        myRef_details = database.getReference("FORMS");

        myRef.child(KeyForm).child("QUESTIONS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListQuest.clear();
                nb_Ques=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());

                    myRef_details.child(KeyForm).child("QUESTIONS").child(D.getName_group()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Data_Question Dq = snapshot.getValue(Data_Question.class);
                                Dq.setKey_value(snapshot.getKey());
                                ListQuest.add(Dq);
                                // create data answer for each quest and add keyQuest

                                ListReponse.add(new Data_answer(Dq.getQuestion() ,Dq.getKey_value()));
                                nb_Ques++;
                            }
                            // start

                            TQues.setText(ListQuest.get(0).getQuestion());
                            EGroup.setText(ListQuest.get(0).getGroup());

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
        Data_UserAnswer User=new Data_UserAnswer(KeyId_person);

        myRef_Reponse.child(KeyForm).child(User.getUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             //   Toast.makeText(getApplicationContext(),"quesFBase"+KeyId_person,Toast.LENGTH_LONG).show();
                c=0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());


                    myRef_Reponse_details.child(KeyForm).child(KeyId_person).child(D.getName_group()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Data_answer Dq = snapshot.getValue(Data_answer.class);
                                Dq.setKey_value(snapshot.getKey());

                                if(c<nb_Ques && ListReponse.get(c).getKey_value().equals(Dq.getKey_value()) ){
                                    ListReponse.get(c).setKey_value(Dq.getKey_value());
                                    ListReponse.get(c).setUser(Dq.getUser());
                                    ListReponse.get(c).setKey_form(Dq.getKey_form());
                                    ListReponse.get(c).setAnswer(Dq.getAnswer());
                                    ListReponse.get(c).setNum_Item(Dq.getNum_Item());
                                    ListReponse.get(c).setGroup(Dq.getGroup());
                                    c++;
                                }


                                if(ListReponse.size()!=0 && ListQuest.size()!=0 && ListReponse.size()>c && ListQuest.size()>c)  {
                                    TQues.setText(ListQuest.get(0).getQuestion()+ ":"+(1)+"/"+nb_Ques);
                                    EGroup.setText(ListQuest.get(0).getGroup());
                                    ValueQuest.setText(ListReponse.get(0).getAnswer());

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

                if (ListQuest.size() > index+1) {
                    index++;
                    TQues.setText(ListQuest.get(index).getQuestion()+"  " + (index+1) + "/"+nb_Ques+":");
                    ValueQuest.setText(ListReponse.get(index).getAnswer());
                    EGroup.setText(ListQuest.get(index).getGroup());

                }
            }
        });


        Bprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(index>0) {

                    index--;
                    TQues.setText(ListQuest.get(index).getQuestion()+"  " + (index+1) + "/"+nb_Ques+":");
                    ValueQuest.setText(ListReponse.get(index).getAnswer());
                    EGroup.setText(ListQuest.get(index).getGroup());

                }

            }
        });

        BDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(getApplicationContext(), MainClient.class);
              //  setResult(2,intent);
             //   finish();

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

