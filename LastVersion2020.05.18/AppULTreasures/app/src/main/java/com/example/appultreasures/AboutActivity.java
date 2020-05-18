package com.example.appultreasures;

import android.os.Bundle;
import android.util.Log;
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

        bgapp.animate().translationY(-2200).setDuration(800).setStartDelay(300);
        texthome.startAnimation(frombottom);
        //menus.startAnimation(frombottom);
    }



}