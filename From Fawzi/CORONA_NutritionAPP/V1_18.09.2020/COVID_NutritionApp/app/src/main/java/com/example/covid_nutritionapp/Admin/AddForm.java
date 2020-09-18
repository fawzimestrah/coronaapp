package com.example.covid_nutritionapp.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import java.util.ArrayList;

public class AddForm extends AppCompatActivity {
EditText Ename,Edesc,EGroup;

EditText ValueQuest;
ArrayList<EditText> listDataValueQuest;
private  LinearLayout ViewQuest;
private ArrayList<LinearLayout>  ListCurrentView;
TextView TQues;
Button Bnext,Bsubmit,Bprev;
Spinner mSpinner,mSpinnerQuest;
ArrayList<Data_Question> ListQuest;
FirebaseUser user ;

int c=0,nb_Ques=0;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    ArrayList<Data_GroupADMIN> ListGroup;
    ArrayList<String> listnamegroup;
    String [] listTypeQuest;
    private ArrayAdapter mArrayAdapterGroup;
    private ArrayAdapter mArrayAdapterTypeQuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_form);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ListCurrentView=new ArrayList<LinearLayout>();

        Ename=findViewById(R.id.IdEname);
        Edesc=findViewById(R.id.IdEdesc);
        Bnext=findViewById(R.id.Idnext);
        Bprev=findViewById(R.id.IdPrev);
        ViewQuest=findViewById(R.id.IdEQues);
        TQues=findViewById(R.id.IdTQues);
        EGroup=findViewById(R.id.IdGroup);
        EGroup.setText("A");
        Bsubmit=findViewById(R.id.IdBSubmit);
        mSpinner=findViewById(R.id.IdSpinnernamegroup);
        mSpinnerQuest=findViewById(R.id.IdmSpinnerQuest);
        ListQuest=new ArrayList<Data_Question>();
        listDataValueQuest=new ArrayList<EditText>();
        ListGroup=new ArrayList<Data_GroupADMIN>();
        listnamegroup=new ArrayList<String>();


        listTypeQuest=new String[2];
        listTypeQuest[0]="TextField";
        listTypeQuest[1]="RadioButton";
        mArrayAdapterTypeQuest = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,listTypeQuest);
        mArrayAdapterTypeQuest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(mSpinnerQuest!=null) {
            mSpinnerQuest.setAdapter(mArrayAdapterTypeQuest);

        }

        next_Quest(0);


        mSpinnerQuest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        editLayoutQuestion(listTypeQuest[mSpinnerQuest.getSelectedItemPosition()],c);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


// List= Data_GroupADMIN.selectListGroup_user(getApplicationContext(),user.getUid());







        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("GroupAdmin");

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


//                            Toast.makeText(getApplicationContext(),"FB size "+List.size()+" size="+listnamegroup.size(),Toast.LENGTH_LONG).show();
                            String [] Array_namegroup=new String[listnamegroup.size()];
                            for(int i=0;i<listnamegroup.size();i++){
                                Array_namegroup[i]=listnamegroup.get(i);
                            }
                            mArrayAdapterGroup = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,Array_namegroup);
                            mArrayAdapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            if(mSpinner!=null) {
                                mSpinner.setAdapter(mArrayAdapterGroup);

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

                Bsubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (ListQuest.size() > 0) {
                            ListQuest.get(c).setQuestion(ValueQuest.getText().toString());
                            ListQuest.get(c).setGroup(EGroup.getText().toString());

                        } // la7ata lcurrent tt3adal

                        String KeyGroup=ListGroup.get(mSpinner.getSelectedItemPosition()).getKey_Group();
                        Data_forms.Insert_form(Ename.getText().toString(), Edesc.getText().toString(), user.getUid(), KeyGroup, ListQuest);

                        Intent intent = new Intent(getApplicationContext(), MainAdmin.class);
                        setResult(1, intent);
                        finish();


                    }
                });

        Bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ListQuest.size()>c+1) {
                     ListQuest.get(c).setQuestion(ValueQuest.getText().toString());
                    // ListQuest.get(c).setTypeQuestion("Text Field");
                     c++;
                        showLayoutQuestion(c,ListCurrentView);
  //                   ValueQuest.setText(ListQuest.get(c).getQuestion());
    //                 EGroup.setText(ListQuest.get(c).getGroup());

                }
                else{

                    ListQuest.get(c).setQuestion(ValueQuest.getText().toString());
                    ListQuest.get(c).setGroup(EGroup.getText().toString());

                    // hon case krmel spinner
                    c++;
                    next_Quest(c);

          //          ValueQuest.setText("");


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
                   showLayoutQuestion(c,ListCurrentView);
                    // ValueQuest.setText(ListQuest.get(c).getQuestion());
                    //EGroup.setText(ListQuest.get(c).getGroup());

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
        if(mSpinnerQuest.getSelectedItemPosition()>=0) {
            Toast.makeText(getApplicationContext(),"spinner="+mSpinnerQuest.getSelectedItemPosition(),Toast.LENGTH_LONG).show();
            D1.setTypeQuestion(listTypeQuest[mSpinnerQuest.getSelectedItemPosition()]);
               }
        ListQuest.add(D1);
        createLayoutQuestion(listTypeQuest[mSpinnerQuest.getSelectedItemPosition()]);

    }


 public void showLayoutQuestion(int index,ArrayList<LinearLayout> listlayout){

        ViewQuest.removeAllViews();
        ViewQuest.addView(ListCurrentView.get(index));
        ValueQuest=listDataValueQuest.get(index); // valueQuest ==field current

 }


 @SuppressLint("ResourceAsColor")
 public void createLayoutQuestion(String TypeQuestion){
     LinearLayout Lcurrent;
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
            listDataValueQuest.add(ValueQuest);
            ViewQuest.addView(Lcurrent);
            ListCurrentView.add(Lcurrent); // list
            break;
            case "RadioButton":
                ViewQuest.removeAllViews();
                Lcurrent = new LinearLayout(getApplicationContext());
                Lcurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Lcurrent.setOrientation(LinearLayout.VERTICAL);

                ValueQuest = new EditText(getApplicationContext());
                ValueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                ValueQuest.setTextColor(R.color.colorPrimaryDark);
                ValueQuest.setHint("Enter Text Question ");
                Lcurrent.addView(ValueQuest);


                LinearLayout LRadio=new LinearLayout(getApplicationContext());
                LRadio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LRadio.setOrientation(LinearLayout.HORIZONTAL);
                final EditText Eradio = new EditText(getApplicationContext());
                Eradio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Eradio.setTextColor(R.color.colorPrimaryDark);
                Eradio.setHint("Enter Text RadioButton");
                Eradio.setHintTextColor(R.color.design_default_color_primary_dark);
                Eradio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus) {
                            if(ListQuest.size()>=c+1){
                        //    ListQuest.get(c).setChoix(Eradio.getText().toString());
                            Toast.makeText(getApplicationContext(), "finish typing : "+c, Toast.LENGTH_LONG).show();
                             }
                        }
                    }
                });
                LRadio.addView(Eradio);

                Lcurrent.addView(LRadio);
                listDataValueQuest.add(ValueQuest);
                ViewQuest.addView(Lcurrent);

                ListCurrentView.add(Lcurrent); // list

                break;
            default:
                Toast.makeText(getApplicationContext(),"default",Toast.LENGTH_LONG).show();
                break;
        }

 }

 @SuppressLint("ResourceAsColor")
 public void editLayoutQuestion(String TypeQuestion, int index){

     //save the question
     ListQuest.get(c).setQuestion(ValueQuest.getText().toString());
     ListQuest.get(c).setGroup(EGroup.getText().toString());
     ListQuest.get(c).setTypeQuestion(TypeQuestion);

     LinearLayout Lcurrent;
     ViewQuest.removeAllViews();
     ListCurrentView.get(index).removeAllViews();
     switch (TypeQuestion) {
         case "TextField":
             Lcurrent = new LinearLayout(getApplicationContext());
             Lcurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
             Lcurrent.setOrientation(LinearLayout.VERTICAL);
             ValueQuest = new EditText(getApplicationContext());
             ValueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
             ValueQuest.setTextColor(R.color.colorPrimaryDark);
             ValueQuest.setText(ListQuest.get(c).getQuestion());
             Lcurrent.addView(ValueQuest); // valueQuest meme

             ViewQuest.addView(Lcurrent);
             ListCurrentView.add(Lcurrent); // list
             break;
         case "RadioButton":
             Lcurrent = new LinearLayout(getApplicationContext());
             Lcurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
             Lcurrent.setOrientation(LinearLayout.VERTICAL);
             ValueQuest = new EditText(getApplicationContext());
             ValueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
             ValueQuest.setTextColor(R.color.colorPrimaryDark);
             ValueQuest.setText(ListQuest.get(c).getQuestion());
             Lcurrent.addView(ValueQuest);// valueQuest meme

             LinearLayout LRadio=new LinearLayout(getApplicationContext());
             LRadio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
             LRadio.setOrientation(LinearLayout.HORIZONTAL);
             final EditText Eradio = new EditText(getApplicationContext());
             Eradio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
             Eradio.setTextColor(R.color.colorPrimaryDark);
             Eradio.setHint("Enter Text RadioButton");
             Eradio.setHintTextColor(R.color.design_default_color_primary_dark);
             Eradio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                 @Override
                 public void onFocusChange(View v, boolean hasFocus) {
                     if(!hasFocus) {
                      //           ListQuest.get(c).setChoix(Eradio.getText().toString());
                             Toast.makeText(getApplicationContext(), "finish typing : "+c, Toast.LENGTH_LONG).show();
                      }
                 }
             });
             LRadio.addView(Eradio);
             Lcurrent.addView(LRadio);

             listDataValueQuest.add(ValueQuest);
             ViewQuest.addView(Lcurrent);

             ListCurrentView.add(Lcurrent); // list

             break;
         default:
             Toast.makeText(getApplicationContext(),"default",Toast.LENGTH_LONG).show();
             break;
     }





 }
}

