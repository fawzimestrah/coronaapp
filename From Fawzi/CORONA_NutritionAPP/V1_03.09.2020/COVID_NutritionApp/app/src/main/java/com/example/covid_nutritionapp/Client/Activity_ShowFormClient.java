package com.example.covid_nutritionapp.Client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_ShowFormClient extends AppCompatActivity {

    EditText Ename,Edesc,EGroup;
    Button Bsubmit,Bnext,Bprev;

    FirebaseUser user ;
    String userId ="";
    FirebaseDatabase database;
    DatabaseReference myRef,myRef_details, myRef_Reponse, myRef_Reponse_details ;
    Data_forms currentForm;
    String keyForm;
    Bundle extra;

    private ArrayList<Data_Question> listQuest;
    TextView textQues; // question
    private  LinearLayout viewQuest;
    private EditText valueQuest; // reponse
    private ArrayList<LinearLayout>  listCurrentView; // liste view
    private ArrayList<EditText> listDataValueQuest; // liste edittext reponse

    private ArrayList<Data_answer> listReponse;

    int c=0, nbQues =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_showform);
        user= FirebaseAuth.getInstance().getCurrentUser();
        userId =user.getUid();
        Ename=findViewById(R.id.IdEname);
        Edesc=findViewById(R.id.IdEdesc);
        Bsubmit=findViewById(R.id.IdBSubmit);
        Bnext=findViewById(R.id.Idnext);
        Bprev=findViewById(R.id.IdPrev);
        viewQuest=findViewById(R.id.IdEQues);
        EGroup=findViewById(R.id.IdGroup);
        textQues =findViewById(R.id.IdTQues);
        listQuest =new ArrayList<Data_Question>();
        listReponse =new ArrayList<Data_answer>();
        listQuest=new ArrayList<Data_Question>();

        listDataValueQuest=new ArrayList<EditText>();
        listCurrentView=new ArrayList<LinearLayout>();

        Ename.setEnabled(false);
        Edesc.setEnabled(false);
        EGroup.setEnabled(false);




        extra = getIntent().getExtras();
        if(extra!=null) {
            keyForm =extra.getString("keyForm");
        }

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
                    Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());

                    myRef_details.child(keyForm).child("QUESTIONS").child(D.getName_group()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Data_Question Dq = snapshot.getValue(Data_Question.class);
                                Dq.setKey_value(snapshot.getKey());
                                listQuest.add(Dq);
                                // create data answer for each quest and add keyQuest

                                listReponse.add(new Data_answer(Dq.getQuestion() , Dq.getKey_value()));
                                createLayoutQuestion(c);

                                nbQues++;
                            }
                            // start

                        //    textQues.setText(listQuest.get(0).getQuestion());
                          //  EGroup.setText(listQuest.get(0).getGroup());

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
        Data_UserAnswer User=new Data_UserAnswer(userId);
        myRef_Reponse.child(keyForm).child(User.getUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());
                    myRef_Reponse_details.child(keyForm).child(userId).child(D.getName_group()).addValueEventListener(new ValueEventListener() {
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
                                    c++;
                                    }


                                if(listReponse.size()!=0 && listQuest.size()!=0 && listReponse.size()>c && listQuest.size()>c)  {
                                 //   textQues.setText(listQuest.get(c).getQuestion()+ ":"+(c+1)+"/"+ nbQues);
                                   /// EGroup.setText(listQuest.get(c).getGroup());
                         //           valueQuest.setText(listReponse.get(c).getAnswer());

                                }else
                                     if (listReponse.size() ==c){
                                            // y3ne e5r we7de
                                            valueQuest.setText("");
                                            EGroup.setText("");
                                            textQues.setText("Finish ");
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
                    if (valueQuest.getText().toString().length() > 1) {
                        if (listReponse.size() > c && listQuest.size() > c) {
                            // submit one answer
                            validation(c);
                            if (listQuest.size() > c + 1) {

                                c++;
                                showLayoutQuestion(c);
                            /*    listReponse.get(c).setAnswer(valueQuest.getText().toString());
                                c++;
                                valueQuest.setText(listReponse.get(c).getAnswer());
                                EGroup.setText(listQuest.get(c).getGroup());
                                textQues.setText(listQuest.get(c).getQuestion());
                                textQues.setText(listQuest.get(c).getQuestion() + "  " + (c + 1) + "/" + nbQues + ":");
                              */

                            }else{
                                // y3ne b3d  e5r we7de
                                valueQuest.setText("");
                                EGroup.setText("");
                                textQues.setText("Finish ");
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
                if(listReponse.size()>c && !valueQuest.getText().toString().equals(listReponse.get(c).getAnswer())){
                    validation(c);
                    listReponse.get(c).setAnswer(valueQuest.getText().toString());
                   }
                if(listReponse.size()==c+1) {
                    listReponse.get(c).setAnswer(valueQuest.getText().toString());
                    }

                if(c>0) {
                    c--;
                    textQues.setText(listQuest.get(c).getQuestion()+"  " + (c+1) + "/"+ nbQues +":");
                    valueQuest.setText(listReponse.get(c).getAnswer());
                    EGroup.setText(listQuest.get(c).getGroup());
                    }
            }
        });

        Bsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_MainClient.class);
                setResult(2,intent);
                finish();

            }
        });





    }

    public void validation(int c) {
        switch (listQuest.get(c).getTypeQuestion()) {
            case "TextField":
                if (!valueQuest.getText().toString().equals(listReponse.get(c).getAnswer())) {
                    Data_answer.insert_answer(currentForm.getKey_value(), listReponse.get(c).getKey_value(), listReponse.get(c).getQuestion()
                            , valueQuest.getText().toString(), userId, listQuest.get(c).getNum_Item()
                            , listQuest.get(c).getGroup(), 0);
                }
                break;

            case "RadioButton":
                //               if(!valueQuest.getText().toString().equals(listReponse.get(c).getAnswer())) {
                Data_answer.insert_answer(currentForm.getKey_value(), listReponse.get(c).getKey_value(), listReponse.get(c).getQuestion()
                        , valueQuest.getText().toString(), userId, listQuest.get(c).getNum_Item()
                        , listQuest.get(c).getGroup(), 0);

                //             }
                break;

        }
    }





    public void showLayoutQuestion(int index){

        viewQuest.removeAllViews();
        viewQuest.addView(listCurrentView.get(index));
        valueQuest=listDataValueQuest.get(index); // valueQuest ==field current
        // create view (edit text to radio button )

    }



    @SuppressLint("ResourceAsColor")
    public void createLayoutQuestion(int index){
        LinearLayout linearCurrent;
        switch (listQuest.get(index).getTypeQuestion()) {
            case "TextField":
                viewQuest.removeAllViews();
                linearCurrent = new LinearLayout(getApplicationContext());
                linearCurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearCurrent.setOrientation(LinearLayout.VERTICAL);
                valueQuest = new EditText(getApplicationContext());
                valueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                valueQuest.setTextColor(R.color.colorPrimaryDark);
                textQues.setText(listQuest.get(index).getQuestion());
                if(listReponse.size()>= index +1) { // last index in list
                    valueQuest.setText(listReponse.get(index).getAnswer());
                    Toast.makeText(getApplicationContext(),"in if ",Toast.LENGTH_LONG);
                }
                linearCurrent.addView(valueQuest);

                listDataValueQuest.add(valueQuest);
                viewQuest.addView(linearCurrent);
                listCurrentView.add(linearCurrent); // list
                break;

            case "RadioButton":
                viewQuest.removeAllViews();
                linearCurrent = new LinearLayout(getApplicationContext());
                linearCurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearCurrent.setOrientation(LinearLayout.VERTICAL);
                valueQuest = new EditText(getApplicationContext());
                valueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                valueQuest.setTextColor(R.color.colorPrimaryDark);
                valueQuest.setHint("Enter Text Question ");
                linearCurrent.addView(valueQuest);
                valueQuest.setText(listQuest.get(index).getQuestion());
                ArrayList<RadioButton> currentRadio=new ArrayList<RadioButton>();

                LinearLayout linearRadio=new LinearLayout(getApplicationContext());
                linearRadio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearRadio.setOrientation(LinearLayout.VERTICAL);
                final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
                radioGroup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


                for(int i = 0; i< listQuest.get(index).getChoix().size(); i++){ // nbquest-1 la eno heye e5r question bl list

                    RadioButton radioButton=new RadioButton(getApplicationContext());
                    radioButton.setText(listQuest.get(index).getChoix().get(i));
                    currentRadio.add(radioButton);
                    radioGroup.addView(radioButton);
                    linearRadio.addView(radioButton);
                    linearCurrent.addView(linearRadio);

                    if(listReponse.size()>= index +1 && listReponse.get(index).getAnswer().equals(listQuest.get(nbQues).getChoix().get(i))) {
                      radioButton.setSelected(true);
                    }
                }
                // add question to list question  and radio null  to list radio
                listDataValueQuest.add(valueQuest);
                viewQuest.addView(linearCurrent);
                listCurrentView.add(linearCurrent); // list
                break;
            default:
                Toast.makeText(getApplicationContext(),"default",Toast.LENGTH_LONG).show();
                break;
        }
    }






}

