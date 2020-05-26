package com.example.appultreasures_client.maps;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appultreasures_client.LoginActivity;
import com.example.appultreasures_client.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class Select_CityMain extends AppCompatActivity {


    private static final String TAG = "Cities";
    ImageView bgapp, signoutimg;
    TextView signout;
    LinearLayout texthome, menus;
    Animation frombottom, alphaanim;
    TextView TextNot;
    private ProgressBar mProgressBar;

    Spinner Spinner_City;
    ArrayAdapter mArrayAdapter;
    DI_Adapter_DataSubmit mArrayAdapter_DataSubmit;

    ArrayList<String> ListCity;
    String[] Array_city;
    ArrayList<Data_Region> ListD_region;
    ArrayList<DataSubmit> ListDataSubmit_Conf,ListD_Current;
    DataSubmit [] myData=null;
    int i_city =0,i_Data=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_data_city);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;
        int translationIndex = (int)(-2200 + (dpHeight-692)*(1.7)*(dpHeight/692));


        frombottom = AnimationUtils.loadAnimation(this, R.anim.animfrombottom);
        alphaanim = AnimationUtils.loadAnimation(this, R.anim.alphaanim);
        bgapp = (ImageView) findViewById(R.id.bgapp);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);
        signout = (TextView)findViewById(R.id.signouttext);
        signoutimg=(ImageView)findViewById(R.id.signoutimg);
        bgapp.animate().translationY(translationIndex).setDuration(800).setStartDelay(300);
        texthome.startAnimation(frombottom);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        menus.startAnimation(frombottom);
        signout.startAnimation(alphaanim);
        signoutimg.startAnimation(alphaanim);

        showDialog();



        Spinner_City=findViewById(R.id.Id_Spinner_city);

        ListCity=new ArrayList<String>();
        ListD_region=new ArrayList<Data_Region>();
        ListDataSubmit_Conf=new ArrayList<DataSubmit>();
        ListD_Current=new ArrayList<DataSubmit>();
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
                showDialog();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    Data_Region D = snapshot.getValue(Data_Region.class);

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

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Select_CityMain.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signoutimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Select_CityMain.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mArrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,Array_city);
        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(Spinner_City!=null) {
            Spinner_City.setAdapter(mArrayAdapter);
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
        myRef_data.child("Mobile").child("Cas_confirmes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showDialog();
                ListDataSubmit_Conf.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    DataSubmit D = snapshot.getValue(DataSubmit.class);
                    D.setKey_value(snapshot.getKey());
                    i_Data++;
                    ListDataSubmit_Conf.add(D);
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

        mArrayAdapter_DataSubmit = new DI_Adapter_DataSubmit(getApplicationContext(),R.layout.cust_adapter_datasubmit, ListD_Current);

        if(mListView !=null)
        {
            mListView.setAdapter(mArrayAdapter_DataSubmit);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataSubmit D_check=ListD_Current.get(position);
                //Toast.makeText(getApplicationContext(),"index="+position+"   CLICK SUR "+D_check.key_value_DataSubmit(), Toast.LENGTH_LONG).show();
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
        showDialog();
        if(ListCity.size()!=0) {

            TextNot.setVisibility(View.INVISIBLE);

            if(Spinner_City.getSelectedItem().toString().equals("ALL")){

                ListD_Current.clear();
                ListD_Current.addAll(ListDataSubmit_Conf);
                //    Toast.makeText(getApplicationContext(), " All"+ListD_Current.size(), Toast.LENGTH_LONG).show();

            }else{

                ListD_Current.clear();

                for (int h=0;h<ListDataSubmit_Conf.size();h++) {

                    if (ListDataSubmit_Conf.get(h).getCity().equals(Spinner_City.getSelectedItem().toString())) {

                        ListD_Current.add(ListDataSubmit_Conf.get(h));

                    }
                }

            }
        }
        if(ListD_Current.size()==0){

            TextNot.setVisibility(View.VISIBLE);
        }

        mArrayAdapter_DataSubmit.notifyDataSetChanged();
        hideDialog();
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
        if(requestCode==2){

            //  Toast.makeText(getApplicationContext(),"onActRes", Toast.LENGTH_LONG).show();
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

}
