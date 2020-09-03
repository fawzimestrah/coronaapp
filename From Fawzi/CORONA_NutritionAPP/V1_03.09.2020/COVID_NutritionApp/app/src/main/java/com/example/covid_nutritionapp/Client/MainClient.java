package com.example.covid_nutritionapp.Client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Admin.EditForm;
import com.example.covid_nutritionapp.Admin.MainAdmin;
import com.example.covid_nutritionapp.DI_Adapter_Forms;
import com.example.covid_nutritionapp.Data_forms;
import com.example.covid_nutritionapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainClient extends AppCompatActivity {


    ListView mListView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<Data_forms> ListForm;
    DI_Adapter_Forms mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_client1);

        mListView=(ListView)findViewById(R.id.idLform);

        ListForm=new ArrayList<Data_forms>();

       // user = FirebaseAuth.getInstance().getCurrentUser();


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("FORMS");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ListForm.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    Data_forms D = snapshot.getValue(Data_forms.class);
                    D.setKey_value(snapshot.getKey());
                     if(true) { // conditions pour les clients
                         ListForm.add(D);
                         //    Toast.makeText(getApplicationContext(),"d="+D.getNameform(),Toast.LENGTH_LONG).show();
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

                Intent intent = new Intent(MainClient.this, ShowForm.class);

                intent.putExtra("keyForm",ListForm.get(position).getKey_value());
                startActivityForResult(intent,2);

            }
        });

    }








}
