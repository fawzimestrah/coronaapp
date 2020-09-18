package com.example.covid_nutritionapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Client.MainClient;
import com.example.covid_nutritionapp.Client.ShowForm;
import com.example.covid_nutritionapp.DI_Adapter_Forms;
import com.example.covid_nutritionapp.DI_Adapter_User;
import com.example.covid_nutritionapp.Data_UserAnswer;
import com.example.covid_nutritionapp.Data_forms;
import com.example.covid_nutritionapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Details_Res_Form extends AppCompatActivity {

    ListView mListView;
    FirebaseDatabase database;
    DatabaseReference myRef;

    Bundle extra;
    private String KeyForm;
    ArrayList<Data_UserAnswer> ListUser;

    DI_Adapter_User mArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_result_details_form);

        mListView=(ListView)findViewById(R.id.idLRes_Detailsform);

        ListUser=new ArrayList<Data_UserAnswer>();

        extra = getIntent().getExtras();
        if(extra!=null) {
            KeyForm=extra.getString("keyForm");
        }

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("FORMS_Data").child(KeyForm);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data_UserAnswer U = snapshot.getValue(Data_UserAnswer.class);
                    U.setUser(snapshot.getKey());
                    //userString= snapshot.getKey();
                    ListUser.add(U);
//                    Toast.makeText(getApplicationContext(),"user="+ListUser.get(0).getUser(),Toast.LENGTH_LONG).show();

                }
                mArrayAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mArrayAdapter = new DI_Adapter_User(getApplicationContext(), R.layout.cust_adapt_useranswer,ListUser);

        mArrayAdapter.notifyDataSetChanged();
        if(mListView !=null)
        {
            mListView.setAdapter(mArrayAdapter);

        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                   Intent intent = new Intent(Details_Res_Form.this, Details_ResAnswers.class);

                intent.putExtra("keyForm",KeyForm);
                intent.putExtra("keyId",ListUser.get(position).getUser());

                startActivityForResult(intent,2);


            }
        });

    }







}


