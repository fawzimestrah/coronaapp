package com.example.covid_nutritionapp.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.time.chrono.Era;
import java.util.ArrayList;

public class Activity_AddForm extends AppCompatActivity {
    private EditText Ename,Edesc,EGroup;
    private EditText ValueQuest;
    private TextView TextQues;
    private Button bNext, bSubmit, bPrev,bDelete;
    private  LinearLayout ViewQuest;
    private ArrayList<LinearLayout>  listCurrentView;
    private ArrayList<EditText> listDataValueQuest;
    private ArrayList<ArrayList<AutoCompleteTextView>> listDataRadio;
    private Spinner mSpinnerGroupUser,mSpinnerQuest;
    private ArrayList<Data_Question> listQuest;
    private FirebaseUser user ;
    private int c=0,nb_Ques=0;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<Data_GroupADMIN> ListGroup;
    private ArrayList<String> listnamegroup;
    private ArrayList<String> listAutotCompleteRadio;
    private String [] listTypeQuest;
    private ArrayAdapter mArrayAdapterGroup;
    private ArrayAdapter mArrayAdapterTypeQuest;
    private ArrayAdapter mArrayAdapterAutuComp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_form_layout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        Ename=findViewById(R.id.IdEname);
        Edesc=findViewById(R.id.IdEdesc);
        bNext =findViewById(R.id.Idnext);
        bPrev =findViewById(R.id.IdPrev);
        ViewQuest=findViewById(R.id.IdEQues);
        TextQues =findViewById(R.id.IdTQues);
        EGroup=findViewById(R.id.IdGroup);
        EGroup.setText("A");
        bSubmit =findViewById(R.id.IdBSubmit);
        mSpinnerGroupUser =findViewById(R.id.IdSpinnernamegroup);
        mSpinnerQuest=findViewById(R.id.IdmSpinnerQuest);
        bDelete=findViewById(R.id.IdBdelete);

        listQuest =new ArrayList<Data_Question>();
        listDataValueQuest=new ArrayList<EditText>();
        listCurrentView=new ArrayList<LinearLayout>();
        listDataRadio=new ArrayList<ArrayList<AutoCompleteTextView>>();
        ListGroup=new ArrayList<Data_GroupADMIN>();
        listnamegroup=new ArrayList<String>();
        listAutotCompleteRadio=new ArrayList<String>();

        mArrayAdapterAutuComp = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listAutotCompleteRadio);

        listTypeQuest=new String[3];
        listTypeQuest[0]="TextField";
        listTypeQuest[1]="RadioButton";
        listTypeQuest[2]="CheckBox";
        mArrayAdapterTypeQuest = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,listTypeQuest);
        mArrayAdapterTypeQuest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(mSpinnerQuest!=null) {
            mSpinnerQuest.setAdapter(mArrayAdapterTypeQuest);
        }

        next_Quest(0);

        mSpinnerQuest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(listQuest.size()>0) {
                    editLayoutQuestion(listTypeQuest[mSpinnerQuest.getSelectedItemPosition()], c);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("GroupAdmin");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListGroup.clear();
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
                                    ListGroup.add(D);
                                    listnamegroup.add(D.getNameGroup());

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



        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listQuest.size()>c+1) {
                    listQuest.get(c).setQuestion(ValueQuest.getText().toString());
                    listQuest.get(c).setGroup(EGroup.getText().toString());
                    saveDataAutoCompList(c);
                    c++;
                    showLayoutQuestion(c,listCurrentView);
                    ValueQuest.setText(listQuest.get(c).getQuestion());
                    EGroup.setText(listQuest.get(c).getGroup());
                }
                else{

                    listQuest.get(c).setQuestion(ValueQuest.getText().toString());
                    listQuest.get(c).setGroup(EGroup.getText().toString());
                    saveDataAutoCompList(c);

                    c++;
                    next_Quest(c);
                    ValueQuest.setText("");
                }
                TextQues.setText("Question "+(c+1)+ "/"+nb_Ques+":");
            }
        });


        bPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listQuest.size()==c+1) {
                    listQuest.get(c).setQuestion(ValueQuest.getText().toString());
                    listQuest.get(c).setGroup(EGroup.getText().toString());
                    saveDataAutoCompList(c);
                }
                if(c>0) {
                    listQuest.get(c).setQuestion(ValueQuest.getText().toString());
                    EGroup.setText(listQuest.get(c).getGroup());
                    saveDataAutoCompList(c);

                    c--;
                    showLayoutQuestion(c,listCurrentView);
                    ValueQuest.setText(listQuest.get(c).getQuestion());
                    EGroup.setText(listQuest.get(c).getGroup());
                    TextQues.setText("Question " + (c+1) + "/"+nb_Ques+":");
                }
            }
        });

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listQuest.size()>0 ){
                    if(c>0 ) {
                        listQuest.remove(c); //
                        listDataRadio.remove(c);
                        listCurrentView.remove(c);
                        c--;
                        nb_Ques--;
                        showLayoutQuestion(c, listCurrentView);
                    }
                }

            }
        });

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listQuest.size() > 0) {
                    listQuest.get(c).setQuestion(ValueQuest.getText().toString());
                    listQuest.get(c).setGroup(EGroup.getText().toString());

                } // la7ata lcurrent tt3adal

                String KeyGroup=ListGroup.get(mSpinnerGroupUser.getSelectedItemPosition()).getKey_Group();
/*                for(int h = 0; h< listQuest.size(); h++){
                    if(listDataRadio.get(h)!=null){
                        for (int j=0;j<listDataRadio.get(h).size();j++) {
                            if (!listDataRadio.get(h).get(j).getText().equals("")) {
                                listQuest.get(h).AddChoix(listDataRadio.get(h).get(j).getText().toString());
                            }
                        }
                    }
                }
*/


                for(int h = 0; h< listQuest.size(); h++){
                    saveDataradio(h);
                }

                Data_forms.Insert_form(Ename.getText().toString(), Edesc.getText().toString(), user.getUid(), KeyGroup, listQuest);

                Intent intent = new Intent(getApplicationContext(), Activity_MainAdmin.class);
                setResult(1, intent);
                finish();


            }
        });

    }




    public void next_Quest(int index){

        nb_Ques++;
        Data_Question D1=new Data_Question("");
        D1.setGroup("");
        D1.setNum_Item(nb_Ques);
        if(mSpinnerQuest.getSelectedItemPosition()>=0) {
            D1.setTypeQuestion(listTypeQuest[mSpinnerQuest.getSelectedItemPosition()]);
        }
        listQuest.add(D1);
        createLayoutQuestion(listTypeQuest[mSpinnerQuest.getSelectedItemPosition()]);

    }


    public void showLayoutQuestion(int index,ArrayList<LinearLayout> listlayout){

        saveDataradio(index);
        ViewQuest.removeAllViews();
        ViewQuest.addView(listCurrentView.get(index));
        ValueQuest=listDataValueQuest.get(index); // valueQuest ==field current
        // create view (edit text to radio button )
        switch (listQuest.get(index).getTypeQuestion()){
            case("TextField") :
                mSpinnerQuest.setSelection(0);
                break;
            case("RadioButton") :
            case "CheckBox":
                mArrayAdapterAutuComp = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listAutotCompleteRadio);
                if(listQuest.get(index).getTypeQuestion().equals("RadioButton")){

                    mSpinnerQuest.setSelection(1);

                }else{
                    mSpinnerQuest.setSelection(2);
                }

                for(int m=0;m<listDataRadio.get(index).size();m++){
                    if(listDataRadio.get(index).get(m)!=null) {
                        listDataRadio.get(index).get(m).setAdapter(mArrayAdapterAutuComp);
                        listDataRadio.get(index).get(m).setThreshold(0);
                    }
                }


                break;
        }
    }


    @SuppressLint("ResourceAsColor")
    public void createLayoutQuestion(String TypeQuestion){
        final LinearLayout Lcurrent;
        switch (TypeQuestion) {
            case "TextField":
                ViewQuest.removeAllViews();
                Lcurrent = new LinearLayout(getApplicationContext());
                Lcurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Lcurrent.setOrientation(LinearLayout.VERTICAL);
                ValueQuest = new EditText(getApplicationContext());
                ValueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                ValueQuest.setTextColor(R.color.colorPrimaryDark);
                ValueQuest.setHint("Enter Text Question ");
                Lcurrent.addView(ValueQuest);
                // add question to list question  and radio null  to list radio
                listDataValueQuest.add(ValueQuest);
                listDataRadio.add(null);
                ViewQuest.addView(Lcurrent);
                listCurrentView.add(Lcurrent); // list
                break;
            case "RadioButton" :
            case"CheckBox":

                ViewQuest.removeAllViews();
                Lcurrent = new LinearLayout(getApplicationContext());
                Lcurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Lcurrent.setOrientation(LinearLayout.VERTICAL);
                ValueQuest = new EditText(getApplicationContext());
                ValueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                ValueQuest.setTextColor(R.color.colorPrimaryDark);
                ValueQuest.setHint("Enter Text Question ");
                Lcurrent.addView(ValueQuest);
                final ArrayList<AutoCompleteTextView> currentRadio=new ArrayList<AutoCompleteTextView>();
                LinearLayout LRadio=new LinearLayout(getApplicationContext());
                LRadio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LRadio.setOrientation(LinearLayout.VERTICAL);
                final AutoCompleteTextView Eradio = new AutoCompleteTextView(getApplicationContext());
                Eradio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if(Eradio !=null) {
                    Eradio.setAdapter(mArrayAdapterAutuComp);
                }
                Eradio.setTextColor(R.color.colorPrimaryDark);
                Eradio.setHint("Option");
                Eradio.setHintTextColor(R.color.design_default_color_primary_dark);
                LinearLayout LAdd=new LinearLayout(getApplicationContext());
                LAdd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LAdd.setOrientation(LinearLayout.HORIZONTAL);
                final ImageView imadd=new ImageView(getApplicationContext());
                imadd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                imadd.setImageResource(R.drawable.ic_add_24dp);
                LAdd.addView(imadd);
                Lcurrent.addView(LAdd);

                imadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout LRadio1=new LinearLayout(getApplicationContext());
                        LRadio1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        LRadio1.setOrientation(LinearLayout.HORIZONTAL);
                        final AutoCompleteTextView Eradio1 = new AutoCompleteTextView(getApplicationContext());
                        Eradio1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        Eradio1.setTextColor(R.color.colorPrimaryDark);
                        Eradio1.setHint("Option");
                        if(Eradio1 !=null) {
                            Eradio1.setAdapter(mArrayAdapterAutuComp);
                        }

                        Eradio1.setHintTextColor(R.color.design_default_color_primary_dark);
                        // add to list
                        LRadio1.addView(Eradio1);
                        currentRadio.add(Eradio1);
                        //add view
                        Lcurrent.addView(LRadio1);
                    }
                });


                // add question to list question  and radio null  to list radio
                listDataValueQuest.add(ValueQuest);
                currentRadio.add(Eradio);
                LRadio.addView(Eradio);
                // LRadio.addView(imAdd);
                Lcurrent.addView(LRadio);
                ViewQuest.addView(Lcurrent);
                listCurrentView.add(Lcurrent); // list
                listDataRadio.add(currentRadio);
                break;

            // add question to list question  and radio null  to list radio
            // LRadio.addView(imAdd);
            default:
                Toast.makeText(getApplicationContext(),"default",Toast.LENGTH_LONG).show();
                break;
        }

    }


    @SuppressLint("ResourceAsColor")
    public void editLayoutQuestion(String TypeQuestion, int index){
        final LinearLayout Lcurrent;

        if(ValueQuest!=null) {
            //save the question
            listQuest.get(index).setQuestion(ValueQuest.getText().toString());
            listQuest.get(index).setGroup(EGroup.getText().toString());
            listQuest.get(index).setTypeQuestion(TypeQuestion);
            ViewQuest.removeAllViews();
            listCurrentView.get(index).removeAllViews();

        }
        switch (TypeQuestion) {
            case "TextField":
                Lcurrent = new LinearLayout(getApplicationContext());
                Lcurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Lcurrent.setOrientation(LinearLayout.VERTICAL);
                ValueQuest = new EditText(getApplicationContext());
                ValueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                ValueQuest.setTextColor(R.color.colorPrimaryDark);
                ValueQuest.setText(listQuest.get(index).getQuestion());
                Lcurrent.addView(ValueQuest); // valueQuest meme
                ViewQuest.addView(Lcurrent);
                //add to view
                listCurrentView.remove(index);
                listCurrentView.add(index,Lcurrent); // list
                // add question to list question  and radio null  to list radio\
                listDataValueQuest.remove(index);
                listDataValueQuest.add(index,ValueQuest);
                // radio null (textfield)
                listDataRadio.remove(c);
                listDataRadio.add(c,null);
                break;
            case "RadioButton":
            case"CheckBox":

                Lcurrent = new LinearLayout(getApplicationContext());
                Lcurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Lcurrent.setOrientation(LinearLayout.VERTICAL);
                ValueQuest = new EditText(getApplicationContext());
                ValueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                ValueQuest.setTextColor(R.color.colorPrimaryDark);
                ValueQuest.setText(listQuest.get(c).getQuestion());
                Lcurrent.addView(ValueQuest);// valueQuest meme

                final ArrayList<AutoCompleteTextView> currentRadio=new ArrayList<AutoCompleteTextView>();
                final LinearLayout LRadio=new LinearLayout(getApplicationContext());
                LRadio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LRadio.setOrientation(LinearLayout.HORIZONTAL);
                final AutoCompleteTextView Eradio = new AutoCompleteTextView(getApplicationContext());
                Eradio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if(Eradio !=null) {
                    Eradio.setAdapter(mArrayAdapterAutuComp);
                }

                Eradio.setTextColor(R.color.colorPrimaryDark);
                Eradio.setHint("Option");
                Eradio.setHintTextColor(R.color.design_default_color_primary_dark);

                LinearLayout LAdd=new LinearLayout(getApplicationContext());
                LAdd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LAdd.setOrientation(LinearLayout.HORIZONTAL);

                final ImageView imadd=new ImageView(getApplicationContext());
                imadd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                imadd.setImageResource(R.drawable.ic_add_24dp);
                LAdd.addView(imadd);
                Lcurrent.addView(LAdd);

                imadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LinearLayout LRadio1=new LinearLayout(getApplicationContext());
                        LRadio1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        LRadio1.setOrientation(LinearLayout.HORIZONTAL);
                        final AutoCompleteTextView Eradio1 = new AutoCompleteTextView(getApplicationContext());
                        Eradio1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        Eradio1.setTextColor(R.color.colorPrimaryDark);
                        if(Eradio1 !=null) {
                            Eradio1.setAdapter(mArrayAdapterAutuComp);
                        }

                        Eradio1.setHint("Option");
                        Eradio1.setHintTextColor(R.color.design_default_color_primary_dark);
                        // add to list
                        LRadio1.addView(Eradio1);
                        currentRadio.add(Eradio1);
                        //add view
                        Lcurrent.addView(LRadio1);
                    }
                });
                currentRadio.add(Eradio);
                listDataRadio.add(currentRadio);
                LRadio.addView(Eradio);

                Lcurrent.addView(LRadio);
                ViewQuest.addView(Lcurrent);
                // question
                listDataValueQuest.remove(c);
                listDataValueQuest.add(ValueQuest);
                // add view
                listCurrentView.remove(c);
                listCurrentView.add(c,Lcurrent); // list
                // add question to list question  and radio null  to list radio
                listDataValueQuest.remove(index);
                listDataValueQuest.add(index,ValueQuest);
                //add radio
                listDataRadio.remove(c);
                listDataRadio.add(c,currentRadio);

                break;
            default:
                Toast.makeText(getApplicationContext(),"default",Toast.LENGTH_LONG).show();
                break;
        }



    }


    public void saveDataAutoCompList(int index){
        if(listDataRadio.get(index)!=null) {
            for (int m = 0; m < listDataRadio.get(index).size(); m++) {
                if (listDataRadio.get(index).get(m) != null) {

                    if (!listAutotCompleteRadio.contains(listDataRadio.get(index).get(m).getText().toString())) {
                        listAutotCompleteRadio.add(listDataRadio.get(index).get(m).getText().toString());
                    }
                }
            }
        }

    }


    public  void  saveDataradio(int h){
        if(listDataRadio.get(h)!=null){
            listQuest.get(h).getChoix().clear();
            for (int j=0;j<listDataRadio.get(h).size();j++){
                listQuest.get(h).AddChoix(listDataRadio.get(h).get(j).getText().toString());
            }
        }


    }
}

