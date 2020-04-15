package com.example.mapstest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatProperty;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Windo_popUp extends Activity {

    Bundle extra;
boolean CheckHosp,CheckHome;
    EditText Ename;
    Spinner sp_family,sp_transf;
    RadioGroup RHopital,Rhome;
    RadioButton RBhome,RBtrans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_info_point);




        DisplayMetrics Dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Dm);

        int width = Dm.widthPixels;
        int height = Dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));


        extra =getIntent().getExtras();
         if(extra!=null) {

            TextView TLA=findViewById(R.id.TText1);
            TextView TlO=findViewById(R.id.TText2);
             Ename= findViewById(R.id.name);
             sp_family=findViewById(R.id.spfamily);
            sp_transf=findViewById(R.id.sp_how_trans);
            RHopital=findViewById(R.id.rg_Transhopital);
             Rhome=findViewById(R.id.rg_isHome);
             RBhome=findViewById(R.id.idRB_ishome_yes);
             RBtrans=findViewById(R.id.idRB_transhop_yes);

             TLA.setText(String.valueOf(extra.getDouble("Cliked_Latitude"))  );
            TlO.setText(String.valueOf(extra.getDouble("Cliked_Longitude")) );



    //        Toast.makeText(getApplicationContext(),extra.getString("Confirmed_No"),Toast.LENGTH_LONG).show();
//Confirmed/No





            RHopital.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    if(RBtrans.isChecked()){
                       CheckHosp=true;
                    }else{
                        CheckHosp=false;
                    }
                    Toast.makeText(getApplicationContext(),"Hopital true ="+RHopital.getCheckedRadioButtonId(),Toast.LENGTH_LONG ).show();

                }
            });

            Rhome.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (RBhome.isChecked()) {
                        Toast.makeText(getApplicationContext(),"Home true = "+checkedId,Toast.LENGTH_LONG ).show();

                        CheckHome = true;
                    } else {
                        CheckHome = false;

                    }
                    Toast.makeText(getApplicationContext(),"Home = "+Rhome.getCheckedRadioButtonId(),Toast.LENGTH_LONG ).show();

                }
            });

            Button Bsubmit=findViewById(R.id.idButtonSubmit);

            Bsubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DataSubmit Dpop_up=new DataSubmit();
                    Dpop_up.setLatitude(extra.getDouble("Cliked_Latitude"));
                    Dpop_up.setLongitude(extra.getDouble("Cliked_Longitude"));
                    Dpop_up.setFull_Name(Ename.getText().toString());
                    Dpop_up.setNb_family(Integer.valueOf(sp_family.getSelectedItem().toString()));
                    DataSubmit.Inser_Point(Dpop_up.getLatitude(),Dpop_up.getLongitude(),Dpop_up.getFull_Name(),Dpop_up.getNb_family(),CheckHosp,sp_transf.getSelectedItem().toString(),CheckHome);

                    Intent intent = new Intent(getApplicationContext(),Maps_test.class);
                    intent.putExtra("Submit_bool",true);
                    intent.putExtra("Cliked_Latitude",Dpop_up.getLatitude());
                    intent.putExtra("Cliked_Longitude",Dpop_up.getLongitude());

                    setResult(1,intent);
                    finish();
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
}