package com.example.coronaapp_admin_maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Windo_popUp_Admin_check extends Activity {

    Bundle extra;
    boolean CheckHosp,CheckHome;
    EditText Ename,sp_family,sp_transf;
    RadioGroup RHopital,Rhome;
    Button BsubmitDone,BDelete;
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
        sp_family=findViewById(R.id.spfamily_admin);
        sp_transf=findViewById(R.id.sp_how_trans_admin);
        RHopital=findViewById(R.id.rg_Transhopital_admin);
        Rhome=findViewById(R.id.rg_isHome_admin);
        BsubmitDone=findViewById(R.id.idButtonSubmit_admin);
        BDelete=findViewById(R.id.IdButtonDelete_admin);


        if(extra!=null) {


                TLA.setText(String.valueOf(extra.getDouble("Cliked_Latitude"))  );
                TlO.setText(String.valueOf(extra.getDouble("Cliked_Longitude")) );
                Ename.setText(extra.getString("Name"));
                sp_family.setText(String.valueOf(extra.getInt("Nb_family")));
                sp_transf.setText(extra.getString("How_transferred"));

                if(extra.getBoolean("isIn_home")){
                    CheckHome=true;
                }else{
                    CheckHome=false;
                }

            if(extra.getBoolean("isTransferred_hospital")){
                CheckHosp=true;
            }else{
                CheckHosp=false;
            }



        }



        BsubmitDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSubmit_Admin Dpop_up=new DataSubmit_Admin();
                Dpop_up.setLatitude(extra.getDouble("Cliked_Latitude"));
                Dpop_up.setLongitude(extra.getDouble("Cliked_Longitude"));
                Dpop_up.setFull_Name(Ename.getText().toString());
                Dpop_up.setNb_family(Integer.valueOf(sp_family.getText().toString()));
                DataSubmit_Admin.Insert_Point_ADMIN(Dpop_up.getLatitude(),Dpop_up.getLongitude(),Dpop_up.getFull_Name(),Dpop_up.getNb_family(),CheckHosp,sp_transf.getText().toString(),CheckHome);

                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("Submit_bool",true);
                intent.putExtra("Cliked_Latitude",Dpop_up.getLatitude());
                intent.putExtra("Cliked_Longitude",Dpop_up.getLongitude());

                setResult(1,intent);
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
}

