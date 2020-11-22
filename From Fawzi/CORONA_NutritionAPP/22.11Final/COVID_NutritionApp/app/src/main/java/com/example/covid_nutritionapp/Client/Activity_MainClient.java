package com.example.covid_nutritionapp.Client;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class Activity_MainClient extends AppCompatActivity {

    private int REQUEST_MLIST =2;

    TextView textNodata;
    ListView mListView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<Data_forms> listForm;
    DI_Adapter_Forms mArrayAdapter;
    ImageView logout;
    FirebaseUser user ;
    ProgressBar mProgressBar;
    boolean isDoubleClicked=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_client1_layout);



        logout=(ImageView) findViewById(R.id.idLogoutC);
        logout.setAlpha(0); // transparent

        mListView=(ListView)findViewById(R.id.idLform);
        textNodata=(TextView)findViewById(R.id.idtextnodata);
        listForm =new ArrayList<Data_forms>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mProgressBar = findViewById(R.id.progressBar);
        showDialog();



        final Handler handler=new Handler();
        final Runnable r=new Runnable(){
            @Override
            public void run(){
                //Actions when Single Clicked
                isDoubleClicked=false;
            }
        };


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDoubleClicked){
                    //Actions when double Clicked
                    isDoubleClicked=false;
                    //remove callbacks for Handlers
                    handler.removeCallbacks(r);
                    Log.e("logoutClick","double Click");
                    SingOUT();
                    Intent intent = new Intent(Activity_MainClient.this, Activity_LoginActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Log.e("logoutClick","one Click");

                    isDoubleClicked=true;
                    handler.postDelayed(r,500);

                }


            }
        });

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("FORMS");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listForm.clear();
                showDialog();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    Data_forms D = snapshot.getValue(Data_forms.class);
                    D.setKey_value(snapshot.getKey());
                     if(true) { // conditions pour les clients
                         listForm.add(D);
                         //    Toast.makeText(getApplicationContext(),"d="+D.getNameform(),Toast.LENGTH_LONG).show();
                     }
                     }

                mArrayAdapter.notifyDataSetChanged();
                if(listForm.size()==0){
                    textNodata.setVisibility(View.VISIBLE);
                }else{
                    textNodata.setVisibility(View.INVISIBLE);

                }
                hideDialog();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mArrayAdapter = new DI_Adapter_Forms(getApplicationContext(), R.layout.cust_adapt_forms, listForm);

        mArrayAdapter.notifyDataSetChanged();
        if(mListView !=null)
        {
            mListView.setAdapter(mArrayAdapter);

        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog();
                Intent intent = new Intent(Activity_MainClient.this, Activity_ShowFormDescriptionClient.class);
                intent.putExtra("keyForm", listForm.get(position).getKey_value());
                startActivityForResult(intent, REQUEST_MLIST);
                hideDialog();
            }
        });

    }



 private void SingOUT(){
        showDialog();
        FirebaseAuth.getInstance()
                .signOut();

    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== REQUEST_MLIST){

        }
    }
}
