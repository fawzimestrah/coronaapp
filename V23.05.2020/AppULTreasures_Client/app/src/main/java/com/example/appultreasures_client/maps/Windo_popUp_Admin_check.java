package com.example.appultreasures_client.maps;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.appultreasures_client.R;

import java.io.InputStream;

public class Windo_popUp_Admin_check extends Activity {

    Bundle extra;
    EditText Ename,Edescription,Ecity;
    ImageView Img;
    Button BsubmitDone,Baff;
    String Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_info);




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

            // show Image
            Baff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new DownloadImageTask(Img).execute(Url);

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

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }







}

