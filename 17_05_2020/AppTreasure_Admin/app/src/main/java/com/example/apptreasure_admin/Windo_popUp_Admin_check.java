package com.example.apptreasure_admin;

import android.app.Activity;
import android.content.Intent;
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

import java.io.InputStream;

public class Windo_popUp_Admin_check extends Activity {

    Bundle extra;
    EditText Ename,EDescription,Ecity;
    String Url;
    Button BsubmitDone,BDelete;
    ImageView Img;
    String Key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_info);




        DisplayMetrics Dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Dm);

        int width = Dm.widthPixels;
        int height = Dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));




        extra =getIntent().getExtras();

        TextView TLA=findViewById(R.id.TText1);
        TextView TlO=findViewById(R.id.TText2);
        Ename= findViewById(R.id.name_admin);
        EDescription= findViewById(R.id.Description_admin);
        Img=findViewById(R.id.ShowImageView);
       BsubmitDone=findViewById(R.id.idButtonSubmit_admin);
        BDelete=findViewById(R.id.IdButtonDelete_admin);
        Ecity=findViewById(R.id.city);

        if(extra!=null) {


                TLA.setText(String.valueOf(extra.getDouble("Cliked_Latitude"))  );
                TlO.setText(String.valueOf(extra.getDouble("Cliked_Longitude")) );
                Ename.setText(extra.getString("Name"));
                EDescription.setText(extra.getString("Description"));
                Url=extra.getString("URLimage");
                Key=extra.getString("Key");
                Ecity.setText(extra.getString("City"));
                Toast.makeText(getApplicationContext(),"KeyKey="+Key,Toast.LENGTH_LONG).show();
            // show Image
            new DownloadImageTask(Img)
                    .execute(Url);



        }


BDelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Toast.makeText(getApplicationContext(),"onclickkeySubmit="+Key,Toast.LENGTH_LONG).show();
        DataSubmit_Admin.Delete_Point_Coordonne(Key,getApplication());
        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);

        intent.putExtra("Submit_bool",true);

        setResult(2,intent);
        finish();

    }
});


        BsubmitDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSubmit_Admin Dpop_up=new DataSubmit_Admin();
                Dpop_up.setLatitude(extra.getDouble("Cliked_Latitude"));
                Dpop_up.setLongitude(extra.getDouble("Cliked_Longitude"));
                Dpop_up.setFull_Name(Ename.getText().toString());
                Dpop_up.setImageName(EDescription.getText().toString());
                Dpop_up.setImageURL(Url);
                Dpop_up.setCity(Ecity.getText().toString());
                DataSubmit_Admin.Insert_Point_ADMIN(Dpop_up.getLatitude(),Dpop_up.getLongitude(),Dpop_up.getFull_Name(),Url,Dpop_up.getImageName(),Dpop_up.getCity());
                Toast.makeText(getApplicationContext(),"onclickkeySubmit="+Key,Toast.LENGTH_LONG).show();
                DataSubmit_Admin.Delete_Point_Coordonne(Key,getApplication());
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("Submit_bool",true);
                intent.putExtra("Cliked_Latitude",Dpop_up.getLatitude());
                intent.putExtra("Cliked_Longitude",Dpop_up.getLongitude());

                setResult(2,intent);
                finish();

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


