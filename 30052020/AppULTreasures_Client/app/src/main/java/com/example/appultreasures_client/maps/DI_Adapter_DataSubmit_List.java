package com.example.appultreasures_client.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.appultreasures_client.R;

import java.io.InputStream;
import java.util.ArrayList;

public class DI_Adapter_DataSubmit_List extends ArrayAdapter<DataSubmit> {

       static ArrayList<Bitmap> Array_Bimg;
    static ArrayList<String> Array_UrlImg;

    Context myContext;
        int resource;
    ArrayList<DataSubmit> myData=null;


        public DI_Adapter_DataSubmit_List(Context context, int resource, ArrayList<DataSubmit> myData) {

            super(context, resource, myData);
            this.myContext=context;
            this.resource=resource;
            this.myData=myData;
            Log.e("data11","contextnew ="+this.myContext+"  data"+this.myContext+" // "+this.resource+" // ");

            // Log.e("data11","contextnew ="+this.myContext+"  data"+this.myContext+" // "+this.resource+" // "+ this.myData);

        }

        @Override

        public DataSubmit getItem(int position)
        {
            Log.e("data1","Faswzi");

            return super.getItem(position);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row=convertView;
            Log.e("data3    ","Faswzi");
            LayoutInflater inflater= LayoutInflater.from(myContext);
            Log.e("data5   ","Faswzi");

            row= inflater.inflate(resource, parent,false);

            ImageView IMG=row.findViewById(R.id.IdImagePostCust);
         TextView Tname=row.findViewById(R.id.IdTextLCust);
            TextView Tdesc=row.findViewById(R.id.IdTextMCust);
            ProgressBar mProgressBar=row.findViewById(R.id.pBarImg);


                 Tname.setText(myData.get(position).getFull_Name());
            Tdesc.setText(myData.get(position).getImageName());


            // create  if not exists
            if(Array_UrlImg==null){
                Array_UrlImg=new ArrayList<String>();
            }
            if(Array_Bimg==null){
                Array_Bimg=new ArrayList<Bitmap>();
            }


            mProgressBar.setVisibility(View.VISIBLE);

            if(Array_UrlImg.contains(myData.get(position).getImageURL()) && Array_Bimg.size()>Array_UrlImg.indexOf(myData.get(position).getImageURL())){

             Tname.setText(Tname.getText()+"////"+Array_UrlImg.indexOf(myData.get(position).getImageURL()));

                  IMG.setImageBitmap(Array_Bimg.get(Array_UrlImg.indexOf(myData.get(position).getImageURL())));


            }else {
                Array_UrlImg.add(myData.get(position).getImageURL());
                new DownloadImageTask(IMG).execute(myData.get(position).getImageURL());
                if(mProgressBar.getVisibility() == View.VISIBLE) {
                    mProgressBar.setVisibility(View.INVISIBLE);


                }
            }
            return row;


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

              //  Toast.makeText(getContext(),"else",Toast.LENGTH_LONG).show();
                // add to Array list
                bmImage.setImageBitmap(result);
            Array_Bimg.add(result);

            }
    }


}
