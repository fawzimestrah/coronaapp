package com.example.covid_nutritionapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DI_Adapter_Forms extends ArrayAdapter<Data_forms> {

    Context myContext;
    int resource;
    ArrayList<Data_forms> myData=null;

    public DI_Adapter_Forms(@NonNull Context context, int resource, List<Data_forms> myData) {
        super(context, resource, myData);
        this.myContext=context;
        this.resource=resource;
        this.myData= (ArrayList<Data_forms>) myData;
    }

    @Nullable
    @Override
    public Data_forms getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater= LayoutInflater.from(myContext);
        row= inflater.inflate(resource, parent,false);
        TextView Tname=row.findViewById(R.id.idCustName);
        TextView TDate=row.findViewById(R.id.idCustDate);
        Tname.setText(myData.get(position).getNameform());
        TDate.setText(myData.get(position).getDateform());
        return row;
    }
}
