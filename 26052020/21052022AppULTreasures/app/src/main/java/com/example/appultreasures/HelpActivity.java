package com.example.appultreasures;

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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HelpActivity extends AppCompatActivity {

    private static final String TAG = "HelpActivity";
    ImageView bgapp, signoutimg;
    TextView signout;
    LinearLayout texthome, menus;
    Animation frombottom, alphaanim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        frombottom = AnimationUtils.loadAnimation(this, R.anim.animfrombottom);
        alphaanim = AnimationUtils.loadAnimation(this, R.anim.alphaanim);
        bgapp = (ImageView) findViewById(R.id.bgapp);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        signout = (TextView)findViewById(R.id.signouttext);
        signoutimg=(ImageView)findViewById(R.id.signoutimg);
        menus = (LinearLayout) findViewById(R.id.menus);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;
        int translationIndex = (int)(-2200 + (dpHeight-692)*(1.7)*(dpHeight/692));

        bgapp.animate().translationY(translationIndex).setDuration(800).setStartDelay(300);
        texthome.startAnimation(frombottom);
        signout.startAnimation(alphaanim);
        signoutimg.startAnimation(alphaanim);
        menus.startAnimation(frombottom);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HelpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signoutimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HelpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }



}