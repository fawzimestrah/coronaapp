package com.example.covid_nutritionapp.Admin.AdminMaster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.covid_nutritionapp.Admin.Data_GroupADMIN;
import com.example.covid_nutritionapp.Data_forms;
import com.example.covid_nutritionapp.R;

import java.util.ArrayList;
import java.util.List;

public class DI_Adapter_Checkbox extends ArrayAdapter<Data_GroupADMIN> {

    Context myContext;
    int resource;
    ArrayList<Data_GroupADMIN> myData = null;

    public DI_Adapter_Checkbox(@NonNull Context context, int resource, List<Data_GroupADMIN> myData) {


        super(context, resource, myData);
        this.myContext = context;
        this.resource = resource;
        this.myData = (ArrayList<Data_GroupADMIN>) myData;


    }

    @Nullable
    @Override
    public Data_GroupADMIN getItem(int position) {
        return super.getItem(position);
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = LayoutInflater.from(myContext);
        row = inflater.inflate(resource, parent, false);

        CheckBox chb=row.findViewById(R.id.IdChecbox);
        chb.setText(myData.get(position).getNameGroup());
        chb.setTextColor(R.color.colorPrimaryDark);

        chb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myData.get(position).checked=true;
                }else{

                    myData.get(position).checked=false;
                }
            }
        });
        return row;
    }

}
