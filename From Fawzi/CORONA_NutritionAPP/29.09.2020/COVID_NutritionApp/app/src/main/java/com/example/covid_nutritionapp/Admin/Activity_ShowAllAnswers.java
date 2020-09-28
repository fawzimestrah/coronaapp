package com.example.covid_nutritionapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.DI_Adapter_User;
import com.example.covid_nutritionapp.Data_UserAnswer;
import com.example.covid_nutritionapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_ShowAllAnswers extends AppCompatActivity {

    ListView mListView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Bundle extra;
    private String keyForm;
    ArrayList<Data_UserAnswer> listUser;
    DI_Adapter_User mArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_result_details_form_layout);

        mListView=(ListView)findViewById(R.id.idLRes_Detailsform);

        listUser =new ArrayList<Data_UserAnswer>();

        extra = getIntent().getExtras();
        if(extra!=null) {
            keyForm =extra.getString("keyForm");
        }

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("FORMS_Data").child(keyForm);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data_UserAnswer U = snapshot.getValue(Data_UserAnswer.class);
                    U.setUser(snapshot.getKey());
                    listUser.add(U);
                }
                mArrayAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mArrayAdapter = new DI_Adapter_User(getApplicationContext(), R.layout.cust_adapt_useranswer, listUser);

        mArrayAdapter.notifyDataSetChanged();
        if(mListView !=null)
        {
            mListView.setAdapter(mArrayAdapter);

        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                   Intent intent = new Intent(Activity_ShowAllAnswers.this, Activity_DetailsAnswers.class);

                intent.putExtra("keyForm", keyForm);
                intent.putExtra("keyId", listUser.get(position).getUser());

                startActivityForResult(intent,2);


            }
        });

    }

}


