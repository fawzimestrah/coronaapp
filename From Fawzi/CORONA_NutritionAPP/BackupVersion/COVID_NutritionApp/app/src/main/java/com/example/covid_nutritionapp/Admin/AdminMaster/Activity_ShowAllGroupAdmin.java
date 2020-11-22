package com.example.covid_nutritionapp.Admin.AdminMaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.covid_nutritionapp.Admin.Activity_EditForm;
import com.example.covid_nutritionapp.Admin.Data_GroupADMIN;
import com.example.covid_nutritionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_ShowAllGroupAdmin extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    GridView mListView;
    DI_Adapter_GroupAdmin mArrayAdapter;
    FirebaseUser user ;
    private ArrayList<Data_GroupADMIN> listGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_result_form_layout);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mListView=(GridView) findViewById(R.id.idLform);
        database = FirebaseDatabase.getInstance();
        listGroup=new ArrayList<Data_GroupADMIN>();
        DatabaseReference myRef_grp = database.getReference("GroupAdmin");
        myRef_grp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listGroup.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    Data_GroupADMIN D = snapshot.getValue(Data_GroupADMIN.class);
                    D.setKey_Group(snapshot.getKey());
                    listGroup.add(D);

                }
                mArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mArrayAdapter = new DI_Adapter_GroupAdmin(getApplicationContext(), R.layout.cust_adapt_groupadmin,listGroup);
        if(mListView !=null)
        {
            mListView.setAdapter(mArrayAdapter);

        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

             //   Intent intent = new Intent(Activity_ShowAllGroupAdmin.this, Activity_EditForm.class);
              // intent.putExtra("keyForm",listGroup.get(position).getKey_Group());
               // startActivityForResult(intent,2);

            }
        });
    }
}
