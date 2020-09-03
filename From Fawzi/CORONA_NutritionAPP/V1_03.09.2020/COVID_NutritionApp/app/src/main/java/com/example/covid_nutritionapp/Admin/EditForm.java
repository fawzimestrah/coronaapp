package com.example.covid_nutritionapp.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Data_GroupItem;
import com.example.covid_nutritionapp.Data_Question;
import com.example.covid_nutritionapp.Data_forms;
import com.example.covid_nutritionapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class EditForm extends AppCompatActivity {

    EditText Ename,Edesc,ValueQuest,EGroup;;
    Button Bedit,Bnext,Bprev,Bdelete;
    LinearLayout RQuest;
    TextView TQues ;
    FirebaseDatabase database;
    DatabaseReference myRef,myRef_details;
    Data_forms CurrentForm;
    String KeyForm;
    Bundle extra;

    ArrayList<Data_Question> ListQuest;
    int c=0,nb_Ques=0;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_form);



        Ename=findViewById(R.id.IdEname);
        Edesc=findViewById(R.id.IdEdesc);
        Bedit=findViewById(R.id.IdBEdit);
        RQuest=findViewById(R.id.IdViewQuest);
        Bnext=findViewById(R.id.Idnext);
        Bprev=findViewById(R.id.IdPrev);
        ValueQuest=findViewById(R.id.IdEQues);
        EGroup=findViewById(R.id.IdGroup);
        TQues=findViewById(R.id.IdTQues);
        Bdelete=findViewById(R.id.IdBdelete);
        ListQuest=new ArrayList<Data_Question>();

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
                                                nb_Ques++;
                                            }
                                            // start

                                            ListQuest=SortQuest(ListQuest);
                                            ValueQuest.setText(ListQuest.get(0).getQuestion());
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




        Bedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ListQuest.size()>0 ) {
                    ListQuest.get(c).setQuestion(ValueQuest.getText().toString());
                    ListQuest.get(c).setGroup(EGroup.getText().toString());

                } // kermel e5r we7de

                Data_forms.Update_form(CurrentForm.getKey_value(),Ename.getText().toString(),CurrentForm.getDateform(),Edesc.getText().toString(),ListQuest);

                Intent intent = new Intent(getApplicationContext(),MainAdmin.class);
                setResult(1,intent);
                finish();

            }
        });


        Bdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ListQuest.size()>0){
                ListQuest.remove(c); //
                nb_Ques--;
                }
            }
        });


        Bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ListQuest.size()>c+1) {
                    ListQuest.get(c).setQuestion(ValueQuest.getText().toString());
                    c++;
                    ValueQuest.setText(ListQuest.get(c).getQuestion());
                    EGroup.setText(ListQuest.get(c).getGroup());
                }
                else{

                    c++;
                    next_Quest(c);
                    ListQuest.get(c-1).setQuestion(ValueQuest.getText().toString());
                    ListQuest.get(c-1).setGroup(EGroup.getText().toString());
                    ValueQuest.setText("");


                }
                TQues.setText("Question "+(c+1)+ "/"+nb_Ques+":");
            }
        });


        Bprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ListQuest.size()==c+1) {
                    ListQuest.get(c).setQuestion(ValueQuest.getText().toString());
                    ListQuest.get(c).setGroup(EGroup.getText().toString());

                }

                if(c>0) {

                    c--;
                    ValueQuest.setText(ListQuest.get(c).getQuestion());
                    EGroup.setText(ListQuest.get(c).getGroup());

                    TQues.setText("Question " + (c+1) + "/"+nb_Ques+":");

                }
            }
        });








    }


    public void next_Quest(int index){
        nb_Ques++;
        Data_Question D1=new Data_Question("");
        D1.setGroup("");
        D1.setNum_Item(nb_Ques);
        ListQuest.add(D1);

    }

    public ArrayList<Data_Question> SortQuest(ArrayList<Data_Question> List){
        ArrayList<Data_Question> ListSort=new ArrayList<Data_Question>();
        int temp = 0;
        int ind=0;
        while (List.size()>0) {
            temp=0;
            for (int j = 0; j < List.size(); j++) {

                 if (List.get(temp).getNum_Item() > List.get(j).getNum_Item()) {
                    temp = j;
                 }
            }
            ListSort.add(List.get(temp));
            List.remove(List.get(temp));
        }

        return ListSort;
    }



           /*
                             c++;
                LinearLayout Lcurrent = new LinearLayout(v.getContext());
                Lcurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Lcurrent.setOrientation(LinearLayout.VERTICAL);

                EditText text = new EditText(v.getContext());
                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                text.setText("t1");
                Lcurrent.addView(text);


                Button BaddQuest = new Button(v.getContext());
                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                BaddQuest.setText("AddQuest");
                BaddQuest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(v.getContext(),"Add count="+c,Toast.LENGTH_LONG).show();
                    }
                });

                Lcurrent.addView(BaddQuest);


                RQuest.addView(Lcurrent);


            */
}

