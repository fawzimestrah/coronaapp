package com.example.covid_nutritionapp.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Data_GroupItem;
import com.example.covid_nutritionapp.Data_Question;
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

public class Activity_EditForm extends AppCompatActivity {

    EditText Ename,Edesc,EGroup;;
    Button Bedit,Bnext,Bprev,Bdelete;
    ImageView imgdetails;
    FirebaseDatabase database;
    DatabaseReference myRef, myRefDetails;
    Data_forms currentForm;
    String keyForm;
    Bundle extra;

    private ArrayList<Data_Question> listQuest;
    TextView textQues;
    private EditText valueQuest;
    private  LinearLayout viewQuest;
    private ArrayList<LinearLayout>  listCurrentView;
    private ArrayList<EditText> listDataValueQuest;
    private ArrayList<ArrayList<EditText>> listDataRadio;
    private  ArrayList<Data_GroupADMIN> listGroup;
    private ArrayList<String> listnamegroup;
    private ArrayAdapter mArrayAdapterGroup,mArrayAdapterTypeQuest;
    private String[] listTypeQuest;
    int c=0, nbQues =0;
    private FirebaseUser user;
    private Spinner mSpinner,mSpinnerQuest;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_form_layout);


        user = FirebaseAuth.getInstance().getCurrentUser();
        imgdetails=(ImageView)findViewById(R.id.imageviewdetails);
        Ename=findViewById(R.id.IdEname);
        Edesc=findViewById(R.id.IdEdesc);
        Bedit=findViewById(R.id.IdBEdit);
        Bnext=findViewById(R.id.Idnext);
        Bprev=findViewById(R.id.IdPrev);
        viewQuest =findViewById(R.id.IdEQues);

        EGroup=findViewById(R.id.IdGroup);
        textQues =findViewById(R.id.IdTQues);
        Bdelete=findViewById(R.id.IdBdelete);



        listQuest =new ArrayList<Data_Question>();

        mSpinner=findViewById(R.id.IdSpinnernamegroup);
        mSpinner.setEnabled(false);
        mSpinnerQuest=findViewById(R.id.IdmSpinnerQuest);
//        boolean flageSpinnerChange=true;

        listDataValueQuest=new ArrayList<EditText>();
        listDataRadio=new ArrayList<ArrayList<EditText>>();
        listCurrentView=new ArrayList<LinearLayout>();
        listGroup = new ArrayList<Data_GroupADMIN>();
        listnamegroup=new ArrayList<String>();


        listTypeQuest=new String[3];
        listTypeQuest[0]="TextField";
        listTypeQuest[1]="RadioButton";
        listTypeQuest[2]="CheckBox";

        mArrayAdapterTypeQuest = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,listTypeQuest);
        mArrayAdapterTypeQuest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(mSpinnerQuest!=null) {
            mSpinnerQuest.setAdapter(mArrayAdapterTypeQuest);

        }

        mSpinnerQuest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(listQuest.size()>0 ) {
                    // Toast.makeText(getApplicationContext(),"id="+id,Toast.LENGTH_LONG).show();
                    editLayoutQuestion(listTypeQuest[mSpinnerQuest.getSelectedItemPosition()], c);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        extra = getIntent().getExtras();
        if(extra!=null) {
            keyForm =extra.getString("keyForm");
        }

        imgdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(keyForm!=null) {
                    Intent intent = new Intent(Activity_EditForm.this, Activity_ShowAllAnswers.class);
                    intent.putExtra("keyForm", keyForm);
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
                    currentForm=D;
                }
                Ename.setText(currentForm.getNameform());
                Edesc.setText(currentForm.getDescform());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        DatabaseReference myRefG = database.getReference("GroupAdmin");
        myRefG.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listGroup.clear();
                listnamegroup.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    final Data_GroupADMIN D = snapshot.getValue(Data_GroupADMIN.class);
                    D.setKey_Group(snapshot.getKey());
                    DatabaseReference myRefUser = database.getReference("GroupAdmin").child(snapshot.getKey()).child("Members");
                    myRefUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //snapshot.getKey();
                                String Dkey = snapshot.getValue(String.class);
                                if(Dkey.equals(user.getUid())){
                                    listGroup.add(D);
                                    listnamegroup.add(D.getNameGroup());

                                }
                            }

                            String [] Array_namegroup=new String[listnamegroup.size()];
                            for(int i=0;i<listnamegroup.size();i++){
                                Array_namegroup[i]=listnamegroup.get(i);
                            }
                            mArrayAdapterGroup = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,Array_namegroup);
                            mArrayAdapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            if(mSpinner!=null) {
                                mSpinner.setAdapter(mArrayAdapterGroup);

                            }


                            if(currentForm!=null && currentForm.getGroupCreator().equals(D.getKey_Group()) && listnamegroup.size()>0){
                                mSpinner.setSelection(listnamegroup.indexOf(D.getNameGroup()));
                           //     Toast.makeText(getApplicationContext()," keygroup="+D.getKey_Group(),Toast.LENGTH_LONG).show();
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

        myRefDetails = database.getReference("FORMS");

        myRef.child(keyForm).child("QUESTIONS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listQuest.clear();
                nbQues =0;
                c=0;
//                listCurrentView.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());
                    myRefDetails.child(keyForm).child("QUESTIONS").child(D.getName_group()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Data_Question Dq = snapshot.getValue(Data_Question.class);
                                Dq.setKey_value(snapshot.getKey());
                                listQuest.add(Dq);
                                if(Dq.getTypeQuestion()!=null) {
                                    createLayoutQuestion(Dq.getTypeQuestion());
                                }
                                nbQues++;
                            }
                            showLayoutQuestion(0,listCurrentView);
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

                if(listQuest.size()>0 ) {
                    listQuest.get(c).setQuestion(valueQuest.getText().toString());
                    listQuest.get(c).setGroup(EGroup.getText().toString());
                } // kermel e5r we7de
                for(int h = 0; h< listQuest.size(); h++){
                    saveDataradio(h);
                }
                String KeyGroup= listGroup.get(mSpinner.getSelectedItemPosition()).getKey_Group();
                Data_forms.Update_form(currentForm.getKey_value(),Ename.getText().toString(), currentForm.getDateform(),Edesc.getText().toString(), user.getUid(), KeyGroup, listQuest);
                Intent intent = new Intent(getApplicationContext(), Activity_MainAdmin.class);
                setResult(1,intent);
                finish();

            }
        });


        Bdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/*
                if(ListQuest.size()>0){
                ListQuest.remove(c); //
                listDataRadio.remove(c);
                listCurrentView.remove(c);
                if(c>0){
                c--;
                }
                nb_Ques--;
                }
            showLayoutQuestion(c,listCurrentView);
            */
                Data_forms.deleteForm(keyForm);
                Intent intent = new Intent(getApplicationContext(), Activity_MainAdmin.class);
                setResult(1,intent);
                finish();


            }
        });


        Bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listQuest.size()>c+1) {
                    listQuest.get(c).setQuestion(valueQuest.getText().toString());
                    listQuest.get(c).setGroup(EGroup.getText().toString());
                    c++;
                    showLayoutQuestion(c,listCurrentView);
                    //  valueQuest.setText(listQuest.get(c).getQuestion());
                    // EGroup.setText(listQuest.get(c).getGroup());
                    //Toast.makeText(getApplicationContext(),"c="+c+"reponse="+valueQuest.getText().toString(),Toast.LENGTH_LONG).show();


                }
                else{
                    listQuest.get(c).setQuestion(valueQuest.getText().toString());
                    listQuest.get(c).setGroup(EGroup.getText().toString());
                    // hon case krmel spinner
                    c++;
                    next_Quest(c);
                    valueQuest.setText("");
                }
                textQues.setText("Question "+(c+1)+ "/"+ nbQues +":");
            }
        });


        Bprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listQuest.size() == c + 1) {
                    listQuest.get(c).setQuestion(valueQuest.getText().toString());
                    listQuest.get(c).setGroup(EGroup.getText().toString());
                }
                if (c > 0) {
                    listQuest.get(c).setQuestion(valueQuest.getText().toString());
                    listQuest.get(c).setGroup(EGroup.getText().toString());
                    EGroup.setText(listQuest.get(c).getGroup());
                    c--;
                    showLayoutQuestion(c, listCurrentView);
                    valueQuest.setText(listQuest.get(c).getQuestion());
                    EGroup.setText(listQuest.get(c).getGroup());
                    textQues.setText("Question " + (c + 1) + "/" + nbQues + ":");
                }
            }
        });


    }


    public void next_Quest(int index){
        nbQues++;
        Data_Question D1=new Data_Question("");
        D1.setGroup("");
        D1.setNum_Item(nbQues);
        if(mSpinnerQuest.getSelectedItemPosition()>=0) {
            D1.setTypeQuestion(listTypeQuest[mSpinnerQuest.getSelectedItemPosition()]);
        }
        listQuest.add(D1);
        createLayoutQuestion("TextField");

    }


    public  void  saveDataradio(int h){
        if(listDataRadio.get(h)!=null){
            listQuest.get(h).getChoix().clear();
            for (int j=0;j<listDataRadio.get(h).size();j++){
                listQuest.get(h).AddChoix(listDataRadio.get(h).get(j).getText().toString());
            }
        }


    }

    public void showLayoutQuestion(int index,ArrayList<LinearLayout> listlayout){

        saveDataradio(index);
        viewQuest.removeAllViews();
        viewQuest.addView(listCurrentView.get(index));
        valueQuest =listDataValueQuest.get(index); // valueQuest ==field current
        valueQuest.setText(listQuest.get(index).getQuestion());
        EGroup.setText(listQuest.get(index).getGroup());
        //    Toast.makeText(getApplicationContext(),"quest="+valueQuest.getText(),Toast.LENGTH_LONG).show();

        switch (listQuest.get(index).getTypeQuestion()){
            case("TextField") :

                mSpinnerQuest.setSelection(0);
                break;
            case("RadioButton") :

                mSpinnerQuest.setSelection(1);
                break;
            case ("CheckBox"):
                mSpinnerQuest.setSelection(2);
                break;
        }

    }


    @SuppressLint("ResourceAsColor")
    public void createLayoutQuestion(String TypeQuestion){
        final LinearLayout linearCurrent;
        switch (TypeQuestion) {
            case "TextField":
                viewQuest.removeAllViews();
                linearCurrent = new LinearLayout(getApplicationContext());
                linearCurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearCurrent.setOrientation(LinearLayout.VERTICAL);
                valueQuest = new EditText(getApplicationContext());
                valueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                valueQuest.setTextColor(R.color.colorPrimaryDark);
                valueQuest.setHint("Enter Text Question ");
                if(listQuest.size()>= nbQues +1) { // last index in list
                    valueQuest.setText(listQuest.get(nbQues).getQuestion());

                }
                linearCurrent.addView(valueQuest);
                // add question to list question  and radio null  to list radio
                listDataValueQuest.add(valueQuest);
                listDataRadio.add(null);
                viewQuest.addView(linearCurrent);
                listCurrentView.add(linearCurrent); // list
                break;

            case "RadioButton":
            case "CheckBox":
                viewQuest.removeAllViews();
                linearCurrent = new LinearLayout(getApplicationContext());
                linearCurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearCurrent.setOrientation(LinearLayout.VERTICAL);
                valueQuest = new EditText(getApplicationContext());
                valueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                valueQuest.setTextColor(R.color.colorPrimaryDark);
                valueQuest.setHint("Enter Text Question ");
                linearCurrent.addView(valueQuest);
                valueQuest.setText(listQuest.get(nbQues).getQuestion());
                //          Toast.makeText(getApplicationContext(),"in createlayout radio "+listQuest.get(nbQues).getQuestion(),Toast.LENGTH_LONG).show();

                LinearLayout LAdd=new LinearLayout(getApplicationContext());
                LAdd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LAdd.setOrientation(LinearLayout.HORIZONTAL);

                final ImageView imadd=new ImageView(getApplicationContext());
                imadd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                imadd.setImageResource(R.drawable.ic_add_24dp);
                LAdd.addView(imadd);
                linearCurrent.addView(LAdd);
                listCurrentView.add(linearCurrent); // list

                final ArrayList<EditText> currentRadio=new ArrayList<EditText>();

                imadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LinearLayout LRadio1=new LinearLayout(getApplicationContext());
                        LRadio1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        LRadio1.setOrientation(LinearLayout.HORIZONTAL);
                        final EditText Eradio1 = new EditText(getApplicationContext());
                        Eradio1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        Eradio1.setTextColor(R.color.colorPrimaryDark);
                        Eradio1.setHint("Enter Text RadioButton");
                        Eradio1.setHintTextColor(R.color.design_default_color_primary_dark);
                        // add to list
                        LRadio1.addView(Eradio1);
                        currentRadio.add(Eradio1);
                        //add view
                        linearCurrent.addView(LRadio1);
                                 }
                });

                for(int i = 0; i< listQuest.get(nbQues).getChoix().size(); i++){ // nbquest-1 la eno heye e5r question bl list
                    LinearLayout linearRadio=new LinearLayout(getApplicationContext());
                    linearRadio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    linearRadio.setOrientation(LinearLayout.VERTICAL);
                    final EditText Eradio = new EditText(getApplicationContext());
                    Eradio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    Eradio.setTextColor(R.color.colorPrimaryDark);
                    Eradio.setHint("Enter Text RadioButton1");
                    Eradio.setHintTextColor(R.color.design_default_color_primary_dark);
                    Eradio.setText(listQuest.get(nbQues).getChoix().get(i));
                    currentRadio.add(Eradio);
                    linearRadio.addView(Eradio);
                    // LRadio.addView(imAdd);
                    linearCurrent.addView(linearRadio);
                }


                // add question to list question  and radio null  to list radio
                listDataValueQuest.add(valueQuest);
                viewQuest.addView(linearCurrent);
                listDataRadio.add(currentRadio);
                break;
            default:
                Toast.makeText(getApplicationContext(),"default",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @SuppressLint("ResourceAsColor")
    public void editLayoutQuestion(String TypeQuestion, int index){
        final LinearLayout linearcurrent;
        if(valueQuest !=null) {
            //save  question
            listQuest.get(index).setQuestion(valueQuest.getText().toString());
            listQuest.get(index).setGroup(EGroup.getText().toString());
            listQuest.get(index).setTypeQuestion(TypeQuestion);
            viewQuest.removeAllViews();
            listCurrentView.get(index).removeAllViews();
        }
        switch (TypeQuestion) {
            case "TextField":
                linearcurrent = new LinearLayout(getApplicationContext());
                linearcurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearcurrent.setOrientation(LinearLayout.VERTICAL);
                valueQuest = new EditText(getApplicationContext());
                valueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                valueQuest.setTextColor(R.color.colorPrimaryDark);
                valueQuest.setText(listQuest.get(index).getQuestion());
                linearcurrent.addView(valueQuest); // valueQuest meme
                viewQuest.addView(linearcurrent);
                //add to view
                listCurrentView.remove(index);
                listCurrentView.add(index,linearcurrent); // list
                // add question to list question  and radio null  to list radio\
                listDataValueQuest.remove(index);
                listDataValueQuest.add(index, valueQuest);
                // radio null (textfield)
                listDataRadio.remove(c);
                listDataRadio.add(c,null);
                break;
            case "RadioButton":
            case"CheckBox":
                linearcurrent = new LinearLayout(getApplicationContext());
                linearcurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearcurrent.setOrientation(LinearLayout.VERTICAL);
                valueQuest = new EditText(getApplicationContext());
                valueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                valueQuest.setTextColor(R.color.colorPrimaryDark);
                valueQuest.setText(listQuest.get(c).getQuestion());
                linearcurrent.addView(valueQuest);// valueQuest meme
                final ArrayList<EditText> currentRadio=new ArrayList<EditText>();

                LinearLayout LAdd=new LinearLayout(getApplicationContext());
                LAdd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LAdd.setOrientation(LinearLayout.HORIZONTAL);


                final ImageView imadd=new ImageView(getApplicationContext());
                imadd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                imadd.setImageResource(R.drawable.ic_add_24dp);
                LAdd.addView(imadd);
                linearcurrent.addView(LAdd);
                listDataRadio.add(currentRadio);


                if(listQuest.get(index).getChoix().size()!=0) {
                    for (int i = 0; i < listQuest.get(index).getChoix().size(); i++) { // nbquest-1 la eno heye e5r question bl list
                        LinearLayout linearRadio = new LinearLayout(getApplicationContext());
                        linearRadio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        linearRadio.setOrientation(LinearLayout.VERTICAL);
                        final EditText Eradio = new EditText(getApplicationContext());
                        Eradio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        Eradio.setTextColor(R.color.colorPrimaryDark);
                        Eradio.setHint("Enter Text RadioButton1");
                        Eradio.setHintTextColor(R.color.design_default_color_primary_dark);
                        // Toast.makeText(getApplicationContext(),"i="+i,Toast.LENGTH_LONG).show();
                        Eradio.setText(listQuest.get(index).getChoix().get(i));
                        currentRadio.add(Eradio);
                        linearRadio.addView(Eradio);

                        // LRadio.addView(imAdd);
                        linearcurrent.addView(linearRadio);

                    }
                }


                imadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LinearLayout LRadio1=new LinearLayout(getApplicationContext());
                        LRadio1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        LRadio1.setOrientation(LinearLayout.HORIZONTAL);
                        final EditText Eradio1 = new EditText(getApplicationContext());
                        Eradio1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        Eradio1.setTextColor(R.color.colorPrimaryDark);
                        Eradio1.setHint("Enter Text RadioButton");
                        Eradio1.setHintTextColor(R.color.design_default_color_primary_dark);
                        // add to list
                        LRadio1.addView(Eradio1);
                        currentRadio.add(Eradio1);
                        //add view
                        linearcurrent.addView(LRadio1);
                    }
                });
                viewQuest.addView(linearcurrent);
                // question
                listDataValueQuest.remove(c);
                listDataValueQuest.add(valueQuest);
                // add view
                listCurrentView.remove(c);
                listCurrentView.add(c,linearcurrent); // list

                // add question to list question  and radio null  to list radio
                listDataValueQuest.remove(index);
                listDataValueQuest.add(index, valueQuest);
                //add radio
                listDataRadio.remove(c);
                listDataRadio.add(c,currentRadio);
                break;
            default:
                Toast.makeText(getApplicationContext(),"default",Toast.LENGTH_LONG).show();
                break;
        }



    }



}
