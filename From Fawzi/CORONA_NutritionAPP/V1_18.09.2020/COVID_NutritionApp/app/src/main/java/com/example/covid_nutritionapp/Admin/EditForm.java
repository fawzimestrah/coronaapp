package com.example.covid_nutritionapp.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private FirebaseUser user;
    private Spinner mSpinner;
    private  ArrayList<Data_GroupADMIN> ListGroup;
    private ArrayList<String> listnamegroup;

    private ArrayAdapter mArrayAdapterGroup;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_form);



        user = FirebaseAuth.getInstance().getCurrentUser();

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

        mSpinner=findViewById(R.id.IdSpinnernamegroup);
         ListGroup = new ArrayList<Data_GroupADMIN>();
        listnamegroup=new ArrayList<String>();


        extra = getIntent().getExtras();
        if(extra!=null) {
            KeyForm=extra.getString("keyForm");
        }


        database = FirebaseDatabase.getInstance();




        DatabaseReference myRefG = database.getReference("GroupAdmin");

        myRefG.addValueEventListener(new ValueEventListener() {

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
                String KeyGroup=ListGroup.get(mSpinner.getSelectedItemPosition()).getKey_Group();

                Data_forms.Update_form(CurrentForm.getKey_value(),Ename.getText().toString(),CurrentForm.getDateform(),Edesc.getText().toString(), user.getUid(), KeyGroup,ListQuest);

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

