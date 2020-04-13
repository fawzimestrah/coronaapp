package com.example.mapstest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TestDatabaseCloud extends AppCompatActivity {
    FirebaseDatabase database ;
    DatabaseReference myRef;
            EditText Esave;
    TextView Tread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acc_db_cloud);


        // Write a message to the database
  database = FirebaseDatabase.getInstance();
  myRef = database.getReference("message1");


        Esave=findViewById(R.id.IdEditTosave);

        Button Save=findViewById(R.id.IdButtonSave);
        // Write a message to the database
       Save.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {

                                     DataSubmit D=new DataSubmit();
                                     D.setLatitude(55);
                                     D.setLongitude(40);
                                    myRef.child("Mobile").child("Coordonne").push().setValue(D);
    //                                           myRef.setValue(Esave.getText().toString());
                                   }
                               }
       );


    Tread=findViewById(R.id.IdReader);
    Button Bread=findViewById(R.id.IdButtonRead);

    Bread.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                  //  String value = dataSnapshot.getValue(String.class);
                    myRef.child("Mobile").child("Coordonne").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           // Tread.setText(dataSnapshot.getValue(String.class));
                                    Tread.setText("");
                                     for (DataSnapshot  snapshot:dataSnapshot.getChildren()){
                                         DataSubmit D=snapshot.getValue(DataSubmit.class);
                                         Tread.setText(Tread.getText().toString()+"\n"+D.getLatitude()+" "+D.getLongitude());

                                         Toast.makeText(getApplicationContext()," done Read ",Toast.LENGTH_LONG).show();
                                     }

                            
                               myRef.child("Coordonne");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                 }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
        }
    });




    class Database{



        
        }





    }









}
