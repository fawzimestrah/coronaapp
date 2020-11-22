package com.example.covid_nutritionapp.Client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

    private int REQUESTE_RESULT_CONTINUE=1;
    TextView Ename,EGroup;
    Button Bsubmit,Bnext,Bprev;
    private ProgressBar mProgressBar,mProgressQuestion;

    FirebaseUser user ;
    String userId ="";
    FirebaseDatabase database;
    DatabaseReference myRef,myRef_details, myRef_Reponse, myRef_Reponse_details ;
    Data_forms currentForm;
    String keyForm;
    Bundle extra;
    private ArrayList<Data_Question> listQuest;
    TextView textQues,textviewGroup; // question
    private  LinearLayout viewQuest;
    private EditText valueReponse; // reponse
    private RadioGroup radioGroup;
    private ArrayList<LinearLayout>  listCurrentView; // liste view
    private ArrayList<EditText> listDataValueQuest; // liste edittext reponse
    private ArrayList<Data_answer> listReponse;
    private  String language;
    private Spinner mSpinnerLanguage;
    private ArrayAdapter mArrayAdapterLanguage;
    int c=0, nbQues =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_showform_layout);
        user= FirebaseAuth.getInstance().getCurrentUser();
        userId =user.getUid();
        Ename=findViewById(R.id.IdEname);
        Bsubmit=findViewById(R.id.IdBSubmit);
        Bnext=findViewById(R.id.Idnext);
        Bprev=findViewById(R.id.IdPrev);
        viewQuest=findViewById(R.id.IdEQues);
        radioGroup=findViewById(R.id.idradiogroup);
        EGroup=findViewById(R.id.IdGroup);
        textviewGroup=findViewById(R.id.textviewgroup);
        textQues =findViewById(R.id.IdTQues);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressQuestion = (ProgressBar) findViewById(R.id.idprogressQuest);
        listQuest =new ArrayList<Data_Question>();
        listReponse =new ArrayList<Data_answer>();
        listQuest=new ArrayList<Data_Question>();
        listDataValueQuest=new ArrayList<EditText>();
        listCurrentView=new ArrayList<LinearLayout>();
        mSpinnerLanguage=(Spinner)findViewById(R.id.idSpinnerLanguage);

        EGroup.setVisibility(View.INVISIBLE);
        textviewGroup.setVisibility(View.INVISIBLE);
        textQues.setTextSize(20);



        String [] listLanguage=getResources().getStringArray(R.array.languages);

        mArrayAdapterLanguage=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,listLanguage);
        if(mSpinnerLanguage!=null) {
            mSpinnerLanguage.setAdapter(mArrayAdapterLanguage);

        }


        mSpinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                language=mSpinnerLanguage.getSelectedItem().toString();
                showLayoutQuestion(c);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




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
                showDialog();
                nbQues =0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());

                    myRef_details.child(keyForm).child("QUESTIONS").child(D.getName_group()).addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Data_Question Dq = snapshot.getValue(Data_Question.class);
                                Dq.setKey_value(snapshot.getKey());
                                if(Dq.isShared()){
                                listQuest.add(Dq);
                                // create data answer for each quest and add keyQuest
                                listReponse.add(new Data_answer(Dq.getQuestion(), Dq.getKey_value()));
                                nbQues++;
                                }
                            }
                            // start
                            for (int k=0;k<listQuest.size();k++){
                                createLayoutQuestion(k);
                            }
                            showLayoutQuestion(0);
                            hideDialog();
                            mProgressQuestion.setMax(nbQues);

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
                    showDialog();

                    final Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());
                    myRef_Reponse_details.child(keyForm).child(userId).child(D.getName_group()).addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
                            hideDialog();
                            listCurrentView.clear();
                            listDataValueQuest.clear();

                            for (int k=0;k<listQuest.size();k++){
                                createLayoutQuestion(k);

                            }
                            mProgressQuestion.setProgress(c);

                            if (listReponse.size() ==c){
                                showFinishQuestions();
                            }else{
                                language=mSpinnerLanguage.getSelectedItem().toString();
                                showLayoutQuestion(c);
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                hideDialog();
                }

}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        Bnext.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
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
                            textQues.setText(listQuest.get(c).getQuestion_lang(language) + "  " + (c + 1) + "/" + nbQues + ":");
                        }else{
                            // y3ne b3d  e5r we7de
                            showFinishQuestions();
                            c++;
                        }
                    }

                }else{
                    Toast.makeText(v.getContext(),"Empty ",Toast.LENGTH_LONG).show();
                }
            }
        });


        Bprev.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
                else{ // return to activity desciption

                    finish();
                }
            }
        });

        Bsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(listQuest.size()>=c+1){
                validation(c);
            }
              //  Intent intent = new Intent(getApplicationContext(), Activity_MainClient.class);
                //startActivityForResult(intent,2);
                setResult(REQUESTE_RESULT_CONTINUE);
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
                if(listReponse.get(index).getGrpAnswers().size()>0){
                    String checkboxChoix="";
                    for(int ch=0;ch<listReponse.get(index).getGrpAnswers().size();ch++) {
                        checkboxChoix+=listReponse.get(index).getGrpAnswers().get(ch)+" ; ";
                    }
                    valueReponse.setText(checkboxChoix);
                }

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
        showDialog();
        viewQuest.removeAllViews();
        radioGroup.removeAllViews();
        if(listQuest.size()<index+1){
            hideDialog();
            return;
        }
        switch (listQuest.get(index).getTypeQuestion()){
            case "TextField":
                if(listQuest.size()!=0 && listQuest.size()>index  &&listDataValueQuest.size()!=0) {
                    if(listQuest.get(index).getQuestion_lang(language)==null ||listQuest.get(index).getQuestion_lang(language).equals("") ||  listQuest.get(index).getQuestion_lang(language).equals(" ")){
                        textQues.setText("No Translate" + (index + 1) + "/" + nbQues + ":");

                    }else {
                        textQues.setText(listQuest.get(index).getQuestion_lang(language) + "  " + (index + 1) + "/" + nbQues + ":");
                    }
                    EGroup.setText(listQuest.get(index).getGroup());
                    viewQuest.addView(listCurrentView.get(index));
                    valueReponse = listDataValueQuest.get(index); // valueQuest ==field current
                    valueReponse.setText(listReponse.get(index).getAnswer());
                    radioGroup.setVisibility(View.INVISIBLE);

                    valueReponse.setEnabled(true);
                    // create view (edit text to radio button )
                }
                hideDialog();

                break;
            case "RadioButton":
                if(listQuest.size()!=0 && listQuest.size()>index  &&listDataValueQuest.size()!=0) {
                    if(listQuest.get(index).getQuestion_lang(language)==null ||listQuest.get(index).getQuestion_lang(language).equals("")  ){
                        textQues.setText("No Translate" + (index + 1) + "/" + nbQues + ":");

                    }else {
                        textQues.setText(listQuest.get(index).getQuestion_lang(language) + "  " + (index + 1) + "/" + nbQues + ":");
                    }
                    EGroup.setText(listQuest.get(index).getGroup());
                    viewQuest.addView(listCurrentView.get(index));
                    valueReponse = listDataValueQuest.get(index); // valueQuest ==field current
                    radioGroup.setVisibility(View.VISIBLE);
//                    valueReponse.setVisibility(View.INVISIBLE);
                    radioGroup.removeAllViews();
                    final ArrayList<RadioButton> currentRadio=new ArrayList<RadioButton>();
                    //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    float scale = getResources().getDisplayMetrics().density;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    for(int i = 0; i< listQuest.get(index).getChoix().size(); i++){ // nbquest-1 la eno heye e5r question bl list

                        RadioButton radioButton=new RadioButton(getApplicationContext());
                        radioButton.setLayoutParams(params);
                        //radioButton.setMinimumWidth(500);
                        radioButton.setMinimumHeight(150);
                        radioButton.setTextSize(18);
                        radioButton.setText(listQuest.get(index).getChoix().get(i));
                        radioButton.setId(i); // id 0--> end*
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
                            currentRadio.get(checkedId).setChecked(true);
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
                hideDialog();

                break;
            case "CheckBox":
                if(listQuest.size()!=0 && listQuest.size()>index  &&listDataValueQuest.size()!=0) {
                    if(listQuest.get(index).getQuestion_lang(language)==null ||listQuest.get(index).getQuestion_lang(language).equals("")  ){
                        textQues.setText("No Translate " + (index + 1) + "/" + nbQues + ":");

                    }else {
                        textQues.setText(listQuest.get(index).getQuestion_lang(language) + "  " + (index + 1) + "/" + nbQues + ":");
                    }
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
                    linearallcheck.setOrientation(LinearLayout.VERTICAL);

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
                hideDialog();

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
                textQues.setText(listQuest.get(index).getQuestion_lang(language));
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


    public void showFinishQuestions(){
        // y3ne e5r we7de
        valueReponse.setText("");
        EGroup.setText("");
        viewQuest.removeAllViews();
        radioGroup.removeAllViews();
        switch (language){
            case "AR":
                textQues.setText(R.string.finishquestionAR);
                break;
            case "EN":
                textQues.setText(R.string.finishquestionEN);
                break;
            case "FR":
                textQues.setText(R.string.finishquestionFR);
                break;
            default:
                textQues.setText(R.string.finishquestionEN);
                break;
        }
        Bprev.setEnabled(false);
        Bnext.setEnabled(false);
        mSpinnerLanguage.setEnabled(false);
        Bsubmit.setText("Finish");
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

