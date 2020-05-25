package com.example.appultreasures.maps;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.appultreasures.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Confirm_dataCountry extends AppCompatActivity {


    private static final String TAG = "Cities";
    ImageView bgapp;
    LinearLayout texthome, menus;
    Animation frombottom;
    TextView TextNot;


    Spinner Spinner_City;
    ArrayAdapter mArrayAdapter;
    DI_Adapter_DataSubmit mArrayAdapter_DataSubmit;
    ArrayList<String> ListCity;
    String[] Array_city;
    ArrayList<Data_Region_Admin> ListD_region;
    ArrayList<DataSubmit_Admin> ListDataSubmit_noConf,ListD_Current;
    DataSubmit_Admin [] myData=null;
    int i_city =0,i_Data=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_data_contry);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;
        int translationIndex = (int)(-2200 + (dpHeight-692)*(1.7)*(dpHeight/692));

        frombottom = AnimationUtils.loadAnimation(this, R.anim.animfrombottom);
        bgapp = (ImageView) findViewById(R.id.bgapp);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);
        bgapp.animate().translationY(translationIndex).setDuration(800).setStartDelay(300);
        texthome.startAnimation(frombottom);

        menus.startAnimation(frombottom);




        Spinner_City=findViewById(R.id.Id_Spinner_city);

        ListCity=new ArrayList<String>();
        ListD_region=new ArrayList<Data_Region_Admin>();
        ListDataSubmit_noConf=new ArrayList<DataSubmit_Admin>();
        ListD_Current=new ArrayList<DataSubmit_Admin>();
        TextNot=findViewById(R.id.IdTextNotification);

        Array_city=new String[14];
        Array_city[0]="";
        Array_city[1]="";
        Array_city[2]="";
        Array_city[3]="";
        Array_city[4]="";
        Array_city[5]="";
        Array_city[6]="";
        Array_city[7]="";
        Array_city[8]="";
        Array_city[9]="";
        Array_city[10]="";
        Array_city[11]="";
        Array_city[12]="";
        Array_city[13]="";


        // select city from database
        FirebaseDatabase database ;
        DatabaseReference myRef ;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("region");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ListCity.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    Data_Region_Admin D = snapshot.getValue(Data_Region_Admin.class);

                    ListCity.add(D.getNom());
                    ListD_region.add(D);
                    i_city++;
//                    Toast.makeText(getApplicationContext(),"city="+ListCity.get(i_city-1),Toast.LENGTH_LONG).show();

                }




                Array_city[0] ="ALL";

                for (int k=0;k<i_city;k++) {
                    Array_city[k+1] = ListCity.get(k);
                }

                BaseAdapter adapter = (BaseAdapter) Spinner_City.getAdapter();
                adapter.notifyDataSetChanged();

                // spinner 3ala awal index
                refresh_Spinner_Data();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });












        mArrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,Array_city);
        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(Spinner_City!=null) {
            Spinner_City.setAdapter(mArrayAdapter);
            Toast.makeText(getApplicationContext(),"done", Toast.LENGTH_LONG).show();

        }

        Spinner_City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                refresh_Spinner_Data();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        final DatabaseReference myRef_data = database.getReference("message1");
        myRef_data.child("Mobile").child("Coordonne").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListDataSubmit_noConf.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    DataSubmit_Admin D = snapshot.getValue(DataSubmit_Admin.class);
                    D.setKey_value(snapshot.getKey());
                    i_Data++;
                    ListDataSubmit_noConf.add(D);
                }

                //refresh
                refresh_Spinner_Data();
                //        createNotification();
          }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        final ListView mListView =(ListView)findViewById(R.id.IdListData);

        mArrayAdapter_DataSubmit = new DI_Adapter_DataSubmit(getApplicationContext(),R.layout.cust_adapter_datasubmit_notif, ListD_Current);

        if(mListView !=null)
        {
            mListView.setAdapter(mArrayAdapter_DataSubmit);

            Log.e("fm11",""+mListView);
            // Toast.makeText(getActivity().getApplicationContext(),"eascfsd",Toast.LENGTH_LONG).show();

        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataSubmit_Admin D_check=ListD_Current.get(position);
                Toast.makeText(getApplicationContext(),"index="+position+"   CLICK SUR "+D_check.key_value_DataSubmit(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Windo_popUp_Admin_check.class);

                intent.putExtra("Cliked_Latitude",D_check.getLatitude());
                intent.putExtra("Cliked_Longitude",D_check.getLongitude());

                intent.putExtra("Name",D_check.getFull_Name());

                intent.putExtra("Key",D_check.key_value_DataSubmit());
                intent.putExtra("Description",D_check.getImageName());
                intent.putExtra("URLimage",D_check.getImageURL());
                intent.putExtra("City",D_check.getCity());

                startActivityForResult(intent,2);

            }
        });

    }

    void refresh_Spinner_Data(){

        if(ListCity.size()!=0) {

            TextNot.setVisibility(View.INVISIBLE);
            if(Spinner_City.getSelectedItem().toString().equals("ALL")){
                ListD_Current.clear();

               ListD_Current.addAll(ListDataSubmit_noConf);

            }else{
                ListD_Current.clear();

                for (int h=0;h<ListDataSubmit_noConf.size();h++) {

                    if (ListDataSubmit_noConf.get(h).getCity().equals(Spinner_City.getSelectedItem().toString())) {

                        ListD_Current.add(ListDataSubmit_noConf.get(h));

                    }
                }
            }
        }
        if(ListD_Current.size()==0){

            TextNot.setVisibility(View.VISIBLE);
        }
        mArrayAdapter_DataSubmit.notifyDataSetChanged();

    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){

            Toast.makeText(getApplicationContext(),"onActRes", Toast.LENGTH_LONG).show();
            refresh_Spinner_Data();


        }
    }


    static String CHANNEL_ID = "default_channel_id";

    public void createNotification11() {
        // Create the NotificationChannel, but only on API 26+ because // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "my_channel";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }


    public void createNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

                .setContentTitle("Notifications Example")
                .setContentText("This is a test notification");
        Intent notificationIntent = new Intent(this,Confirm_dataCountry.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}
