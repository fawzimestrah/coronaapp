package com.example.covid_nutritionapp.Client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

    TextView Ename,Edesc,EGroup;
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
    private EditText valueReponse; // reponse
    private RadioGroup radioGroup;
    private ArrayList<LinearLayout>  listCurrentView; // liste view
    private ArrayList<EditText> listDataValueQuest; // liste edittext reponse

    private ArrayList<Data_answer> listReponse;

    int c=0, nbQues =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_showform_layout);
        user= FirebaseAuth.getInstance().getCurrentUser();
        userId =user.getUid();
        Ename=findViewById(R.id.IdEname);
        Edesc=findViewById(R.id.IdEdesc);
        Bsubmit=findViewById(R.id.IdBSubmit);
        Bnext=findViewById(R.id.Idnext);
        Bprev=findViewById(R.id.IdPrev);
        viewQuest=findViewById(R.id.IdEQues);
        radioGroup=findViewById(R.id.idradiogroup);
        EGroup=findViewById(R.id.IdGroup);
        textQues =findViewById(R.id.IdTQues);
        listQuest =new ArrayList<Data_Question>();
        listReponse =new ArrayList<Data_answer>();
        listQuest=new ArrayList<Data_Question>();
        listDataValueQuest=new ArrayList<EditText>();
        listCurrentView=new ArrayList<LinearLayout>();

        //Ename.setEnabled(false);
       // Edesc.setEnabled(false);
       // EGroup.setEnabled(false);

        textQues.setTextSize(20);


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
                                nbQues++;


                            }
                            // start
                            for (int k=0;k<listQuest.size();k++){
                                createLayoutQuestion(k);
                            }
                            showLayoutQuestion(0);
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
                listCurrentView.clear();
                listDataValueQuest.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());
                    myRef_Reponse_details.child(keyForm).child(userId).child(D.getName_group()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Data_answer Dq = snapshot.getValue(Data_answer.class);
                                Dq.setKey_value(snapshot.getKey());
                                if( c< nbQues  && listReponse.get(c).getKey_value().equals(Dq.getKey_value()) ){
                                    listReponse.get(c).setKey_value(Dq.getKey_value());
                                    listReponse.get(c).setUser(Dq.getUser());
                                    listReponse.get(c).setKey_form(Dq.getKey_form());
                                    listReponse.get(c).setAnswer(Dq.getAnswer());
                                    listReponse.get(c).setNum_Item(Dq.getNum_Item());
                                    listReponse.get(c).setGroup(Dq.getGroup());
                                    listReponse.get(c).setGrpAnswers(Dq.getGrpAnswers());
                                    if(listReponse.size()>c && listDataValueQuest.size()>c) {
                                        listDataValueQuest.get(c).setText(listReponse.get(c).getAnswer());
                                       }
                                    c++;
                                }

                            }

                            listCurrentView.clear();
                            listDataValueQuest.clear();

                            for (int k=0;k<listQuest.size();k++){
                                createLayoutQuestion(k);

                            }
                            if (listReponse.size() ==c){
                                // y3ne e5r we7de
                                valueReponse.setText("");
                                EGroup.setText("");
                                textQues.setText("Finish ");
                                viewQuest.removeAllViews();
                                radioGroup.removeAllViews();

                            }else{

                                showLayoutQuestion(c);

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




        Bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valueReponse.getText().toString().length() >= 1) {
                    if (listReponse.size() > c && listQuest.size() > c) {
                        // submit one answer
                        validation(c);
                        listReponse.get(c).setAnswer(valueReponse.getText().toString());
                        if (listQuest.size() > c + 1) {
                            c++;
                            showLayoutQuestion(c);
                            valueReponse.setText(listReponse.get(c).getAnswer());
                            EGroup.setText(listQuest.get(c).getGroup());
                            textQues.setText(listQuest.get(c).getQuestion() + "  " + (c + 1) + "/" + nbQues + ":");
                        }else{
                            // y3ne b3d  e5r we7de
                            valueReponse.setText("");
                            EGroup.setText("");
                            textQues.setText("Finish ");
                            viewQuest.removeAllViews();
                            radioGroup.removeAllViews();
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
                if(listReponse.size()>c && !valueReponse.getText().toString().equals(listReponse.get(c).getAnswer())){
                    validation(c);
                    listReponse.get(c).setAnswer(valueReponse.getText().toString());
                }
                if(listReponse.size()==c+1) {
                    listReponse.get(c).setAnswer(valueReponse.getText().toString());
                }
                if(c>0) {
                    c--;
                    showLayoutQuestion(c);
                    }
            }
        });

        Bsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(listQuest.size()>=c+1){
                validation(c);
            }
                Intent intent = new Intent(getApplicationContext(), Activity_MainClient.class);
                setResult(2,intent);
                finish();
            }
        });





    }

    public void validation(int index) {

        switch (listQuest.get(index).getTypeQuestion()) {
            case "TextField":

                if (!valueReponse.getText().toString().equals(listReponse.get(index).getAnswer())) {
                    Data_answer.insert_answer(currentForm.getKey_value(), listReponse.get(index).getKey_value(), listReponse.get(index).getQuestion()
                            , valueReponse.getText().toString(), userId, listQuest.get(index).getNum_Item()
                            , listQuest.get(index).getGroup(), 0,null);
                }
                break;

            case "RadioButton":
                if (!valueReponse.getText().toString().equals(listReponse.get(index).getAnswer())) {
                    Data_answer.insert_answer(currentForm.getKey_value(), listReponse.get(index).getKey_value(), listReponse.get(index).getQuestion()
                        , valueReponse.getText().toString(), userId, listQuest.get(index).getNum_Item()
                        , listQuest.get(index).getGroup(), 0,null);
                }
                break;

            case "CheckBox":

                if (listReponse.size()>=index+1 &&listReponse.get(index).getGrpAnswers().size()>0) {
                    Data_answer.insert_answer(currentForm.getKey_value(), listReponse.get(index).getKey_value(), listReponse.get(index).getQuestion()
                            , valueReponse.getText().toString(), userId, listQuest.get(index).getNum_Item()
                            , listQuest.get(index).getGroup(), 0,listReponse.get(index).getGrpAnswers());
                }

            default:
                //Toast.makeText(getApplicationContext(),"default"+listQuest.get(index).getTypeQuestion(),Toast.LENGTH_LONG).show();
                break;
        }
    }





    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ResourceAsColor")
    public void showLayoutQuestion(final int index){
        viewQuest.removeAllViews();
        radioGroup.removeAllViews();
        if(listQuest.size()<index+1){
            return;
        }
        switch (listQuest.get(index).getTypeQuestion()){
            case "TextField":
                if(listQuest.size()!=0 && listQuest.size()>index  &&listDataValueQuest.size()!=0) {
                    textQues.setText(listQuest.get(index).getQuestion()+"  " + (index+1) + "/"+ nbQues +":");
                    EGroup.setText(listQuest.get(index).getGroup());
                    viewQuest.addView(listCurrentView.get(index));
                    valueReponse = listDataValueQuest.get(index); // valueQuest ==field current
                    valueReponse.setText(listReponse.get(index).getAnswer());
                    radioGroup.setVisibility(View.INVISIBLE);

                    valueReponse.setEnabled(true);
                    // create view (edit text to radio button )
                }
                break;
            case "RadioButton":
                if(listQuest.size()!=0 && listQuest.size()>index  &&listDataValueQuest.size()!=0) {
                    textQues.setText(listQuest.get(index).getQuestion()+"  " + (index+1) + "/"+ nbQues +":");
                    EGroup.setText(listQuest.get(index).getGroup());
                    viewQuest.addView(listCurrentView.get(index));
                    valueReponse = listDataValueQuest.get(index); // valueQuest ==field current
                    radioGroup.setVisibility(View.VISIBLE);
//                    valueReponse.setVisibility(View.INVISIBLE);
                    radioGroup.removeAllViews();
                    final ArrayList<RadioButton> currentRadio=new ArrayList<RadioButton>();
                    //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    float scale = getResources().getDisplayMetrics().density;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (50 * scale), 1f);
                    for(int i = 0; i< listQuest.get(index).getChoix().size(); i++){ // nbquest-1 la eno heye e5r question bl list

                        RadioButton radioButton=new RadioButton(getApplicationContext());

                        radioButton.setLayoutParams(params);
                        //radioButton.setMinimumWidth(500);
                        radioButton.setMinimumHeight(50);
                        radioButton.setTextSize(18);
                        radioButton.setText(listQuest.get(index).getChoix().get(i));
                        radioButton.setId(i); // id 0--> end
                        radioButton.setTextColor(R.color.design_default_color_primary_dark);
                        radioButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        if(i==0) {
                            radioButton.setBackgroundResource(R.drawable.radio_button_top_selector);
                        }else if(i==listQuest.get(index).getChoix().size()-1){
                            radioButton.setBackgroundResource(R.drawable.radio_button_bottom_selector);

                        }else{
                            radioButton.setBackgroundResource(R.drawable.radio_button_center_selector);

                        }radioButton.setButtonDrawable(R.color.transparentGrey);
                        radioButton.setTextColor(R.color.dimGrey);

                        radioGroup.addView(radioButton);

                        currentRadio.add(radioButton);
                       radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            valueReponse.setText(listQuest.get(index).getChoix().get(checkedId));

                        }
                    });
                        if(listReponse.size()>index&& listReponse.get(index)!=null
                                &&  !listReponse.get(index).getAnswer().equals("") && listReponse.get(index).getAnswer().equals(listQuest.get(index).getChoix().get(i)))
                        {
                            radioButton.setChecked(true);
                            radioGroup.check(radioGroup.getCheckedRadioButtonId());
                            valueReponse.setText(radioButton.getText().toString());
                        }

                    }

                valueReponse.setEnabled(false);
                }
                break;
            case "CheckBox":
                if(listQuest.size()!=0 && listQuest.size()>index  &&listDataValueQuest.size()!=0) {
                    textQues.setText(listQuest.get(index).getQuestion()+"  " + (index+1) + "/"+ nbQues +":");
                    EGroup.setText(listQuest.get(index).getGroup());
               //     viewQuest.addView(listCurrentView.get(index));
                 viewQuest.removeAllViews();
                    valueReponse = listDataValueQuest.get(index); // valueQuest ==field current
                    valueReponse.setVisibility(View.INVISIBLE);

                    if(listReponse.get(index).getGrpAnswers().size()==0 ){ // no check
                        valueReponse.setText("");
                    }else{
                        valueReponse.setText("nofield");
                    }
                   // radioGroup.setVisibility(View.INVISIBLE);
                    radioGroup.removeAllViews();

                    LinearLayout linearallcheck = new LinearLayout(getApplicationContext());
                    linearallcheck.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    linearallcheck.setOrientation(LinearLayout.HORIZONTAL);

                    final ArrayList<CheckBox> currentCheck=new ArrayList<CheckBox>();
                    for(int i = 0; i< listQuest.get(index).getChoix().size(); i++){ // nbquest-1 la eno heye e5r question bl list
                        final CheckBox checkBox=new CheckBox(getApplicationContext());

                        checkBox.setText(listQuest.get(index).getChoix().get(i));
                      //  checkBox.setId(i); // id 0--> end
                        checkBox.setTextColor(R.color.design_default_color_primary_dark);

                        linearallcheck.addView(checkBox);

                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked) {
                                    if (listReponse.size() >= index + 1 && !listReponse.get(index).getGrpAnswers().contains(checkBox.getText().toString())) {

                                        listReponse.get(index).addGrpAnswers(checkBox.getText().toString());
                                     //   Toast.makeText(getApplicationContext(),"if grpanswers size= "+ listReponse.get(index).getGrpAnswers().size(),Toast.LENGTH_LONG).show();

                                    }}
                                else {
                                        if (listReponse.size() >= index + 1  && listReponse.get(index).getGrpAnswers().contains(checkBox.getText().toString()))
                                            listReponse.get(index).getGrpAnswers().remove(checkBox.getText().toString());

                                       // Toast.makeText(getApplicationContext(),"else grpanswers size= "+ listReponse.get(index).getGrpAnswers().size(),Toast.LENGTH_LONG).show();

                                    }

                                if(listReponse.get(index).getGrpAnswers().size()==0 ){ // no check
                                    valueReponse.setText("");
                                }else{
                                    valueReponse.setText("nofield");
                                }

                            }
                        });
                        currentCheck.add(checkBox);
                        if(listReponse.size()>=index+1  && listReponse.get(index).getGrpAnswers().size()>0
                                    && listReponse.get(index).getGrpAnswers().contains(listQuest.get(index).getChoix().get(i))){
                               checkBox.setChecked(true);
                                checkBox.setSelected(true);
                        }
                    }

                    valueReponse.setEnabled(false);
                 viewQuest.addView(linearallcheck);

                }
                break;

        }
    }



    @SuppressLint("ResourceAsColor")
    public void createLayoutQuestion(final int index){
        LinearLayout linearCurrent;
        switch (listQuest.get(index).getTypeQuestion()) {
            case "TextField":
                viewQuest.removeAllViews();
                linearCurrent = new LinearLayout(getApplicationContext());
                linearCurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearCurrent.setOrientation(LinearLayout.VERTICAL);
                valueReponse = new EditText(getApplicationContext());
                valueReponse.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                valueReponse.setTextColor(R.color.colorPrimaryDark);
                valueReponse.setBackgroundResource(R.drawable.cadre_background_textview);
                textQues.setText(listQuest.get(index).getQuestion());
                if(listReponse.size()>= index +1 && listReponse.get(index).getAnswer()!=null &&  !listReponse.get(index).getAnswer().equals("")  ) { // last index in list
                    valueReponse.setText(listReponse.get(index).getAnswer());
                }else{
                    valueReponse.setHintTextColor(R.color.blue2);
                }

                linearCurrent.addView(valueReponse);
                listDataValueQuest.add(valueReponse);
                viewQuest.addView(linearCurrent);
                listCurrentView.add(linearCurrent); // list
                break;

            case "RadioButton":
//                Toast.makeText(getApplicationContext(),"radio create"+listQuest.get(index).getQuestion(),Toast.LENGTH_LONG).show();

                viewQuest.removeAllViews();
                linearCurrent = new LinearLayout(getApplicationContext());
                linearCurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearCurrent.setOrientation(LinearLayout.VERTICAL);
                valueReponse = new EditText(getApplicationContext());
                valueReponse.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                valueReponse.setTextColor(R.color.colorPrimaryDark);
                //valueReponse.setHint("Enter Text  ");
                linearCurrent.addView(valueReponse);
                valueReponse.setText("");
//                valueReponse.setVisibility(View.INVISIBLE);

                // add question to list question  and radio null  to list radio\
                listDataValueQuest.add(valueReponse);
                viewQuest.addView(linearCurrent);
                listCurrentView.add(linearCurrent); // list
                break;

            case "CheckBox":
//                Toast.makeText(getApplicationContext(),"radio create"+listQuest.get(index).getQuestion(),Toast.LENGTH_LONG).show();

                viewQuest.removeAllViews();
                linearCurrent = new LinearLayout(getApplicationContext());
                linearCurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearCurrent.setOrientation(LinearLayout.VERTICAL);
                valueReponse = new EditText(getApplicationContext());
                valueReponse.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                valueReponse.setTextColor(R.color.colorPrimaryDark);
                valueReponse.setHint("Enter Text  ");
                linearCurrent.addView(valueReponse);
             //  valueReponse.setVisibility(View.INVISIBLE);

                // add question to list question  and radio null  to list radio\
                listDataValueQuest.add(valueReponse);
                viewQuest.addView(linearCurrent);
                listCurrentView.add(linearCurrent); // list
                break;



            default:
                Toast.makeText(getApplicationContext(),"default",Toast.LENGTH_LONG).show();
                break;
        }
    }


    @Override
    public void onPanelClosed(int featureId, @NonNull Menu menu) {
        super.onPanelClosed(featureId, menu);
        finish();
    }
}

