package com.example.appultreasures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.appultreasures.maps.Confirm_dataCountry;
import com.example.appultreasures.maps.MapsActivity;
import com.example.appultreasures.maps.SelectData_cities;

public class MainActivity extends AppCompatActivity {

    ImageView bgapp, about, help, maps,cities,notification;
    LinearLayout texthome, menus;
    Animation frombottom;

    public static final int ERROR_DIALOG_REQUEST = 9001;
    //start variables added to test permission
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;
    private boolean mLocationPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get DP
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;
        int translationIndex = (int)(-2200 + (dpHeight-692)*(1.7)*(dpHeight/692));
        Log.e("testwh", "" + dpWidth + " h:" + dpHeight + "dpt:"+ translationIndex);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.animfrombottom);
        bgapp = (ImageView) findViewById(R.id.bgapp);
        about = (ImageView) findViewById(R.id.about);
        help = (ImageView) findViewById(R.id.help);
        maps = (ImageView) findViewById(R.id.map);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);
        cities=(ImageView)findViewById(R.id.cities);
        notification=(ImageView)findViewById(R.id.notifications);
        //bgapp.animate().translationY(-2200).setDuration(800).setStartDelay(300);
        bgapp.animate().translationY(translationIndex).setDuration(800).setStartDelay(300);
        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        cities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectData_cities.class);
                startActivity(intent);

            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Confirm_dataCountry.class);
                startActivity(intent);

            }
        });

    }



}
