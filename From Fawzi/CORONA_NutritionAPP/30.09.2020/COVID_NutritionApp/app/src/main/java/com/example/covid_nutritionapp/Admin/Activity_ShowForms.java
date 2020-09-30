package com.example.covid_nutritionapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Activity_LoginActivity;
import com.example.covid_nutritionapp.DI_Adapter_Forms;
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

public class Activity_ShowForms extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<Data_forms> listForm;
    GridView mListView;
    ArrayList<Data_forms> ListForm;
    DI_Adapter_Forms mArrayAdapter;
    FirebaseUser user ;
    private ArrayList<String> ListGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_result_form_layout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mListView=(GridView) findViewById(R.id.idLform);

        ListGroup=new ArrayList<String>();
        ListForm=new ArrayList<Data_forms>();

        database = FirebaseDatabase.getInstance();

        DatabaseReference myRef_grp = database.getReference("USERS").child(user.getUid()).child("Groups");

        myRef_grp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListGroup.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    String D = snapshot.getValue(String.class);
                    ListGroup.add(D);
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

                ListForm.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    Data_forms D = snapshot.getValue(Data_forms.class);
                    D.setKey_value(snapshot.getKey());
                    if(ListGroup.contains(D.getGroupCreator()) ){
                        ListForm.add(D);
                    }

                }

                mArrayAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mArrayAdapter = new DI_Adapter_Forms(getApplicationContext(), R.layout.cust_adapt_forms,ListForm);

        mArrayAdapter.notifyDataSetChanged();
        if(mListView !=null)
        {
            mListView.setAdapter(mArrayAdapter);

        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Activity_ShowForms.this, Activity_EditForm.class);
                intent.putExtra("keyForm",ListForm.get(position).getKey_value());
                startActivity(intent);

            }
        });


    }



}

