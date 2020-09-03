package com.example.covid_nutritionapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Data_Question;
import com.example.covid_nutritionapp.Data_forms;
import com.example.covid_nutritionapp.R;

import java.util.ArrayList;

public class AddForm extends AppCompatActivity {
EditText Ename,Edesc,ValueQuest,EGroup;
TextView TQues;
Button Bnext,Bsubmit,Bprev;
ArrayList<Data_Question> ListQuest;

int c=0,nb_Ques=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_form);

        Ename=findViewById(R.id.IdEname);
        Edesc=findViewById(R.id.IdEdesc);
        Bnext=findViewById(R.id.Idnext);
        Bprev=findViewById(R.id.IdPrev);
        ValueQuest=findViewById(R.id.IdEQues);
        TQues=findViewById(R.id.IdTQues);
        EGroup=findViewById(R.id.IdGroup);
        EGroup.setText("A");
        Bsubmit=findViewById(R.id.IdBSubmit);
        ListQuest=new ArrayList<Data_Question>();
        next_Quest(0);
        Bsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(ListQuest.size()>0 ) {
                    ListQuest.get(c).setQuestion(ValueQuest.getText().toString());
                    ListQuest.get(c).setGroup(EGroup.getText().toString());

                } // la7ata lcurrent tt3adal

                Data_forms.Insert_form(Ename.getText().toString(),Edesc.getText().toString(),ListQuest);

                Intent intent = new Intent(getApplicationContext(),MainAdmin.class);
                setResult(1,intent);
                finish();


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

}

