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
import java.util.Collection;
import java.util.List;

public class DI_Adapter_User extends ArrayAdapter<Data_UserAnswer> {
    Context myContext;
    int resource;
    ArrayList<Data_UserAnswer> myData=null;
    public DI_Adapter_User(Context context, int resource,  List<Data_UserAnswer> myData) {
        super(context, resource, myData);
        this.myContext=context;
        this.resource=resource;
        this.myData= (ArrayList<Data_UserAnswer>) myData;
    }

    @Nullable
    @Override
    public Data_UserAnswer getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater= LayoutInflater.from(myContext);
        row= inflater.inflate(resource, parent,false);
        TextView Tname=row.findViewById(R.id.idCustName);
        Tname.setText("App Numb : "+(position+1));
        return row;
    }
}

