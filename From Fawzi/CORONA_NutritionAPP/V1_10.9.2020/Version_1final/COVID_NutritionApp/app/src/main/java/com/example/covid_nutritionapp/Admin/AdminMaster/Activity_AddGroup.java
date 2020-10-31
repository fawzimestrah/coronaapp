package com.example.covid_nutritionapp.Admin.AdminMaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Admin.Data_GroupADMIN;
import com.example.covid_nutritionapp.R;

import static android.text.TextUtils.isEmpty;

public class Activity_AddGroup extends AppCompatActivity {

    private EditText Ename,Edes;
    private Button Bregister;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_group_layout);

        DisplayMetrics Dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Dm);

        int width = Dm.widthPixels;
        int height = Dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));

        Ename=findViewById(R.id.input_name);
        Edes=findViewById(R.id.input_desc);
        Bregister=findViewById(R.id.btn_register);

        Bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(Ename.getText().toString()) && !isEmpty(Edes.getText().toString())) {

                    Data_GroupADMIN D = new Data_GroupADMIN();
                    D.setNameGroup(Ename.getText().toString());
                    D.setDescription(Edes.getText().toString());
                    Data_GroupADMIN.addGroupAdmin(D);

                    Intent intent = new Intent(Activity_AddGroup.this, Activity_MainRegisterAdmin.class);
                    setResult(1,intent);
                    finish();
                }

            }
        });


    }
}
