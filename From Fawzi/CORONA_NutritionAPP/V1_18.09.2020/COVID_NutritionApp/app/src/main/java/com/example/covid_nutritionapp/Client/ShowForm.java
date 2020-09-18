package com.example.covid_nutritionapp.Client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Admin.MainAdmin;
import com.example.covid_nutritionapp.Data_GroupItem;
import com.example.covid_nutritionapp.Data_Question;
import com.example.covid_nutritionapp.Data_UserAnswer;
import com.example.covid_nutritionapp.Data_answer;
import com.example.covid_nutritionapp.Data_forms;
import com.example.covid_nutritionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowForm extends AppCompatActivity {

    EditText Ename,Edesc,ValueQuest,EGroup;;
    Button Bsubmit,Bnext,Bprev;
    TextView TQues ;

    FirebaseUser user ;
    String USERID="";
    FirebaseDatabase database;
    DatabaseReference myRef,myRef_details, myRef_Reponse, myRef_Reponse_details ;
    Data_forms CurrentForm;
    String KeyForm;
    Bundle extra;
    ArrayList<Data_Question> ListQuest;
    ArrayList<Data_answer> ListReponse;

    int c=0,nb_Ques=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_showform);
        user= FirebaseAuth.getInstance().getCurrentUser();
        USERID=user.getUid();
        Ename=findViewById(R.id.IdEname);
        Edesc=findViewById(R.id.IdEdesc);
        Bsubmit=findViewById(R.id.IdBSubmit);
        Bnext=findViewById(R.id.Idnext);
        Bprev=findViewById(R.id.IdPrev);
        ValueQuest=findViewById(R.id.IdEQues);
        EGroup=findViewById(R.id.IdGroup);
        TQues=findViewById(R.id.IdTQues);
        ListQuest=new ArrayList<Data_Question>();
        ListReponse=new ArrayList<Data_answer>();

        Ename.setEnabled(false);
        Edesc.setEnabled(false);
        EGroup.setEnabled(false);

        extra = getIntent().getExtras();
        if(extra!=null) {
            KeyForm=extra.getString("keyForm");
        }




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
                    Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());

                    myRef_details.child(KeyForm).child("QUESTIONS").child(D.getName_group()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Data_Question Dq = snapshot.getValue(Data_Question.class);
                                Dq.setKey_value(snapshot.getKey());
                                ListQuest.add(Dq);
                                // create data answer for each quest and add keyQuest

                                ListReponse.add(new Data_answer(Dq.getQuestion() , Dq.getKey_value()));
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

        Data_UserAnswer User=new Data_UserAnswer(USERID);

        myRef_Reponse.child(KeyForm).child(User.getUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());
                    myRef_Reponse_details.child(KeyForm).child(USERID).child(D.getName_group()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Data_answer Dq = snapshot.getValue(Data_answer.class);
                                    Dq.setKey_value(snapshot.getKey());
//                                    Toast.makeText(getApplicationContext()," "+snapshot.getKey(),Toast.LENGTH_LONG).show();
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
                                    TQues.setText(ListQuest.get(c).getQuestion()+ ":"+(c+1)+"/"+nb_Ques);
                                    EGroup.setText(ListQuest.get(c).getGroup());
                                    ValueQuest.setText(ListReponse.get(c).getAnswer());

                                }else
                                     if (ListReponse.size() ==c){
                                        // y3ne e5r we7de

                                        ValueQuest.setText("");
                                        EGroup.setText("");
                                        TQues.setText("Finish ");

                                    }

                            }

                            }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
                // start

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        Bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (ValueQuest.getText().toString().length() > 1) {

                            if (ListReponse.size() > c && ListQuest.size() > c) {
                                // submit one answer
                                validation(c);
                                if (ListQuest.size() > c + 1) {
                                    ListReponse.get(c).setAnswer(ValueQuest.getText().toString());
                                    c++;
                                    ValueQuest.setText(ListReponse.get(c).getAnswer());
                                    EGroup.setText(ListQuest.get(c).getGroup());
                                    TQues.setText(ListQuest.get(c).getQuestion());

                                    TQues.setText(ListQuest.get(c).getQuestion() + "  " + (c + 1) + "/" + nb_Ques + ":");

                                }else{
                                    // y3ne b3d  e5r we7de

                                    ValueQuest.setText("");
                                    EGroup.setText("");
                                    TQues.setText("Finish ");
                                    c++;
                                }
                            }

                    }else{
                        Toast.makeText(v.getContext(),"Empty ",Toast.LENGTH_LONG).show();
                    }
            }
        });


        Bprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        // submit one answer
                        if(ListReponse.size()>c && !ValueQuest.getText().toString().equals(ListReponse.get(c).getAnswer())){
                            // Data_answer.insert_answer();
                            Toast.makeText(v.getContext(),"A modifier prev",Toast.LENGTH_LONG).show();
                        }
                        if(ListReponse.size()==c+1) {
                            ListReponse.get(c).setAnswer(ValueQuest.getText().toString());

                        }

                        if(c>0) {

                            c--;
                            TQues.setText(ListQuest.get(c).getQuestion()+"  " + (c+1) + "/"+nb_Ques+":");
                            ValueQuest.setText(ListReponse.get(c).getAnswer());

                            EGroup.setText(ListQuest.get(c).getGroup());

                        }

                    }
        });

        Bsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainClient.class);
                setResult(2,intent);
                finish();

            }
        });





    }




    public void validation(int c){
        if(!ValueQuest.getText().toString().equals(ListReponse.get(c).getAnswer())){
            Data_answer.insert_answer(CurrentForm.getKey_value(),ListReponse.get(c).getKey_value(),ListReponse.get(c).getQuestion()
                    ,ValueQuest.getText().toString(),USERID,ListQuest.get(c).getNum_Item()
                    ,ListQuest.get(c).getGroup(),0);
        }


    }

}
