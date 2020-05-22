package com.example.appultreasures_client.maps;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appultreasures_client.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class List_Information extends AppCompatActivity {
    private static final String TAG = "ListDataActivity";
    ImageView bgapp;
    LinearLayout texthome, menus;
    Animation frombottom;
    ArrayList<DataSubmit> ListD_Current = null;
    DI_Adapter_DataSubmit_List mArrayAdapter_DataSubmit;
    FirebaseDatabase database ;
    DatabaseReference myRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_information);
/*
        frombottom = AnimationUtils.loadAnimation(this, R.anim.animfrombottom);
        bgapp = (ImageView) findViewById(R.id.bgapp);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        //menus = (LinearLayout) findViewById(R.id.menus);
        bgapp.animate().translationY(-2200).setDuration(800).setStartDelay(300);
        texthome.startAnimation(frombottom);
        //menus.startAnimation(frombottom);
*/
 ListD_Current =new ArrayList<DataSubmit>();
        final ListView mListView =(ListView)findViewById(R.id.IdListView);




        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message1");
        myRef.child("Mobile").child("Cas_confirmes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListD_Current.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    DataSubmit D = snapshot.getValue(DataSubmit.class);
                    D.setKey_value(snapshot.getKey());
                    ListD_Current.add(D);

//                    Toast.makeText(getApplicationContext(),"done"+D.key_value_DataSubmit(),Toast.LENGTH_LONG).show();
                }
                //refresh
                mArrayAdapter_DataSubmit.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







         mArrayAdapter_DataSubmit = new DI_Adapter_DataSubmit_List(getApplicationContext(), R.layout.cust_adapter_datasubmit_list, ListD_Current);

        if(mListView !=null)
        {
            mListView.setAdapter(mArrayAdapter_DataSubmit);

            Log.e("fm11",""+mListView);
            // Toast.makeText(getActivity().getApplicationContext(),"eascfsd",Toast.LENGTH_LONG).show();

        }







    }
}
