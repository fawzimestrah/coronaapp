package com.example.appultreasures;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "AboutActivity";
    ImageView bgapp;
    LinearLayout texthome, menus;
    Animation frombottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        frombottom = AnimationUtils.loadAnimation(this, R.anim.animfrombottom);
        bgapp = (ImageView) findViewById(R.id.bgapp);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        //menus = (LinearLayout) findViewById(R.id.menus);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;
        int translationIndex = (int)(-2200 + (dpHeight-692)*(1.7)*(dpHeight/692));

        bgapp.animate().translationY(translationIndex).setDuration(800).setStartDelay(300);
        texthome.startAnimation(frombottom);
        //menus.startAnimation(frombottom);
    }



}