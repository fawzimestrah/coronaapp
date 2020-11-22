package com.example.covid_nutritionapp.Admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.DI_Adapter_User;
import com.example.covid_nutritionapp.Data_UserAnswer;
import com.example.covid_nutritionapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Activity_ShowAllAnswers extends AppCompatActivity {

    ListView mListView;
    ImageView imgToExcel;
    FirebaseDatabase database;
    TextView textNodata;
    DatabaseReference myRef;
    Bundle extra;
    private String keyForm;
    ArrayList<Data_UserAnswer> listUser;
    DI_Adapter_User mArrayAdapter;
    ProgressBar mProgressBar;
    Data_Backup Backup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_result_details_form_layout);

        mListView=(ListView)findViewById(R.id.idLRes_Detailsform);
        imgToExcel=findViewById(R.id.idToExcel);
        mProgressBar = findViewById(R.id.progressBar);
        textNodata=(TextView)findViewById(R.id.idtextnodata);

        listUser =new ArrayList<Data_UserAnswer>();

        extra = getIntent().getExtras();
        if(extra!=null) {
            keyForm =extra.getString("keyForm");
        }

        imgToExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listUser.size()!=0) {
                    showDialog();
                   Backup = new Data_Backup();
                    Backup.checkPermission(getApplicationContext(), Activity_ShowAllAnswers.this);
                    Backup.saveExcelData(Activity_ShowAllAnswers.this, keyForm);
                    imgToExcel.setEnabled(false);
                }
                else {
                    View parentLayout =findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "No Answers !", Snackbar.LENGTH_LONG).show();

                }
            }
        });

        showDialog();
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
                if(listUser.size()==0){
                    textNodata.setVisibility(View.VISIBLE);

                }
                else{
                    textNodata.setVisibility(View.INVISIBLE);
                }
                mArrayAdapter.notifyDataSetChanged();
                hideDialog();
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


     void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public static  void finishExportdone(Activity activity) throws InterruptedException {
        Toast.makeText(activity.getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();
        View parentLayout = activity.findViewById(android.R.id.content);
        Snackbar.make(parentLayout, "saved ", Snackbar.LENGTH_LONG).show();
        activity.finish();
        activity.startActivity(activity.getIntent());
    }
    public  void  ExportError(String e){
        Toast.makeText(getApplicationContext(),"Error "+e,Toast.LENGTH_LONG).show();

    }
}


