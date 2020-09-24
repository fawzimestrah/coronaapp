package com.example.covid_nutritionapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.DI_Adapter_Forms;
import com.example.covid_nutritionapp.Data_forms;
import com.example.covid_nutritionapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_ShowForms extends AppCompatActivity {
    ListView mListView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<Data_forms> listForm;
    DI_Adapter_Forms mArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_result_form);
        mListView=(ListView)findViewById(R.id.idLResform);
        listForm =new ArrayList<Data_forms>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("FORMS");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                listForm.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Data_forms D = snapshot.getValue(Data_forms.class);
                    D.setKey_value(snapshot.getKey());
                    listForm.add(D);
                }
                mArrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        mArrayAdapter = new DI_Adapter_Forms(getApplicationContext(), R.layout.cust_adapt_forms, listForm);
        mArrayAdapter.notifyDataSetChanged();
        if(mListView !=null){
            mListView.setAdapter(mArrayAdapter);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Activity_ShowForms.this, Activity_ShowAllAnswers.class);
                intent.putExtra("keyForm", listForm.get(position).getKey_value());
                startActivity(intent);

            }
        });
    }
}

