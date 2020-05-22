package com.example.appultreasures.maps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.appultreasures.R;

import java.io.InputStream;

public class Windo_popup_infoEdit extends Activity {

    Bundle extra;
    EditText Ename,EDescription,Ecity;
    String Url,KEY;
    Button BsubmitDone,BDelete;
    ImageView Img;
    ImageView bgapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_info_edit);

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




        extra =getIntent().getExtras();

        Ename= findViewById(R.id.name_admin);
        EDescription= findViewById(R.id.Description_admin);
        Img=findViewById(R.id.ShowImageView);
        BsubmitDone=findViewById(R.id.idButtonSubmit_admin);
        BDelete=findViewById(R.id.IdButtonDelete_admin);
        Ecity=findViewById(R.id.city);

        if(extra!=null) {


            Ename.setText(extra.getString("Name"));
            EDescription.setText(extra.getString("Description"));
            Url=extra.getString("URLimage");
            KEY=extra.getString("Key");
            Ecity.setText(extra.getString("City"));
            // show Image
            new DownloadImageTask(Img)
                    .execute(Url);



        }



        BDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSubmit_Admin.Delete_Point_Confirmee(KEY,getApplication());

                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("Submit_bool",true);

                setResult(3,intent);
                finish();
            }
        });
        BsubmitDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Ename.getText().length()>3 &&EDescription.getText().length()>2) {

                    DataSubmit_Admin Dpop_up = new DataSubmit_Admin();
                    Dpop_up.setLatitude(extra.getDouble("Cliked_Latitude"));
                    Dpop_up.setLongitude(extra.getDouble("Cliked_Longitude"));
                    Dpop_up.setFull_Name(Ename.getText().toString());
                    Dpop_up.setImageName(EDescription.getText().toString());
                    Dpop_up.setImageURL(Url);
                    Dpop_up.setCity(Ecity.getText().toString());
                    DataSubmit_Admin.Update_Point(Dpop_up.getLatitude(), Dpop_up.getLongitude(), Dpop_up.getFull_Name(), Url, Dpop_up.getImageName(), KEY, Dpop_up.getCity(),extra.getString("User"));

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("Submit_bool", true);
                    intent.putExtra("Cliked_Latitude", Dpop_up.getLatitude());
                    intent.putExtra("Cliked_Longitude", Dpop_up.getLongitude());

                    setResult(3, intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Error in full name OR in Description",Toast.LENGTH_LONG).show();

                }
            }
        });


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

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }






}
