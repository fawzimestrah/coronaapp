package com.example.appultreasures.maps;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.appultreasures.R;

import java.util.ArrayList;

public class DI_Adapter_DataSubmit extends ArrayAdapter<DataSubmit_Admin> {

        Context myContext;
        int resource;
    ArrayList<DataSubmit_Admin> myData=null;


        public DI_Adapter_DataSubmit(Context context, int resource, ArrayList<DataSubmit_Admin> myData) {

            super(context, resource, myData);
            this.myContext=context;
            this.resource=resource;
            this.myData=myData;
            Log.e("data11","contextnew ="+this.myContext+"  data"+this.myContext+" // "+this.resource+" // ");

            // Log.e("data11","contextnew ="+this.myContext+"  data"+this.myContext+" // "+this.resource+" // "+ this.myData);

        }

        @Override

        public DataSubmit_Admin getItem(int position)
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

          TextView Tname=row.findViewById(R.id.Idadapter_NameCountry);

           Tname.setText(myData.get(position).getImageName());
         //   Tname.setText("fawzii"+position);
            return row;


        }

}
