package com.example.appultreasures_client.maps;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.appultreasures_client.R;

import java.io.InputStream;

public class Windo_popUp_Admin_check extends Activity {

    Bundle extra;
    private static final String TAG = "Info ";
    ImageView bgapp;
    LinearLayout texthome, menus;
    Animation frombottom;


    EditText Ename,Edescription,Ecity;
    ImageView Img;
    boolean downloadImage=false;
    Button BsubmitDone,Baff;
    String Url;
    ProgressBar mProgressBar;
    CardView card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_info);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;
        int translationIndex = (int)(-2300 + (dpHeight-692)*(2)*(dpHeight/692));

        bgapp = (ImageView) findViewById(R.id.bgapp);
        bgapp.animate().translationY(translationIndex).setDuration(0).setStartDelay(0);

        DisplayMetrics Dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Dm);

        int width = Dm.widthPixels;
        int height = Dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));



        Ename= findViewById(R.id.name);
        Edescription=findViewById(R.id.Description);
        Ecity=findViewById(R.id.city);
        Img=findViewById(R.id.ShowImageView);
        Baff=findViewById(R.id.idButtonshow);
        mProgressBar=findViewById(R.id.progressBar);
         card= findViewById(R.id.IdCardView);
        card.setMaxCardElevation(0);
        card.setRadius(15);




        showDialog();
        extra =getIntent().getExtras();
        Ename.setEnabled(false);
        Edescription.setEnabled(false);
        Ecity.setEnabled(false);
        if(extra!=null) {
                Ename.setText(extra.getString("Name"));
                Edescription.setText(extra.getString("Description"));
                Ecity.setText(extra.getString("City"));
                Url=extra.getString("URLimage");

                Toast.makeText(getApplicationContext(),"URL="+Url,Toast.LENGTH_LONG).show();
                hideDialog();
            // show Image
            Baff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!downloadImage) {
                        showDialog();
                        new DownloadImageTask(Img).execute(Url);
                        downloadImage=true;
                    }
                }
            });

        }


    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onPause() {

        super.onPause();


    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result)
        {
            bmImage.setImageBitmap(result);

                bmImage.setMinimumWidth(card.getWidth());
            bmImage.setMinimumHeight(card.getHeight());






            hideDialog();
        }
    }




    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }



}

