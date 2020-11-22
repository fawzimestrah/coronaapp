package com.example.covid_nutritionapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Admin.Activity_MainAdmin;

import java.util.Locale;

public class Activity_About extends AppCompatActivity {
    WebView textView1;
    Button next;
    Spinner mSpinnerLanguage;
    private ArrayAdapter mArrayAdapterLanguage;
    View viewNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);

        textView1=findViewById(R.id.textView4);
        mSpinnerLanguage=(Spinner)findViewById(R.id.IdSpinnerlanguage);
        next=findViewById(R.id.idNext);
        viewNext = findViewById(R.id.idviewNext);
        Bundle extra = getIntent().getExtras();
        if(extra!=null) {
            Boolean firstOpen =extra.getBoolean("firstOpen");
            if(firstOpen){
                viewNext.setVisibility(View.VISIBLE);
            }else{
                viewNext.setVisibility(View.INVISIBLE);

            }
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("com.CovidNutrition.application.prefs", getApplication().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("firstOpen", false);
                editor.commit();
                Intent intent = new Intent(Activity_About.this,Activity_LoginActivity.class);
                startActivityForResult(intent,1);
                finish();
            }
        });
        String [] listLanguage=getResources().getStringArray(R.array.languages);

        mArrayAdapterLanguage=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,listLanguage);
        if(mSpinnerLanguage!=null) {
            mSpinnerLanguage.setAdapter(mArrayAdapterLanguage);
        }


        mSpinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NewApi")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String  language = mSpinnerLanguage.getSelectedItem().toString();
                getStringResourceByLanguage(language);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    private void getStringResourceByLanguage(String language) {
        textView1.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        switch (language) {
            case "EN":

                textView1.loadData("<html align=\"justify\">\n" +
                        "        <head>\n" +
                        "        </head>\n" +
                        "        <body>\n" +
                        " <p >" +
                        "<b>Nutritional Assessment System (NAS):</b> " +
                        "<br/><b>The new horizon of nutritional assessment during emergencies through mobile technologies in the Middle Eastern Countries </b>" +
                        "<br/>This mobile-application that was funded by the Regional Office of the Eastern Mediterranean-World Health Organization and designed by professionals from Faculties of Public Health and Sciences (Computer science department) at the Lebanese University (Lebanon) is a systematized approach to provide critical, " +
                        "reliable information for decision-making, and to establish shared systems and resources for host government partners and humanitarian organizations. " +
                        "It is an improved survey method that balances simplicity (for rapid assessment of acute emergencies) and technical soundness in the Middle Eastern Regions. " +
                        "<br/>The world is currently experiencing the pandemic of COVID-19, and there is a plethora of published recommendations on nutrition, emotional eating, eating patterns, physical activity and food security. " +
                        "Since quarantine is associated to the interruption of the work routine, this could be associated with a greater energy intake, as well as the consumption of higher quantities of fats, " +
                        "carbohydrates, and proteins.This unhealthy nutritional pattern could increase the risk of developing obesity that is, with sedentary lifestyle, " +
                        "are two risk factors for developing non-communicable diseases, which prevalences s are already high in the Eastern Mediterranean Region. " +
                        " </p> " +
                        " </body> " +
                        " </html>","text/html","utf-8");
                break;
            case "AR":
                textView1.loadData("  <html dir=\"rtl\" lang=\"ar\">\n" +
                        "        <head>\n" +
                        "        </head>\n" +
                        "        <body>\n" +
                        "        <p align=\"justify\"  >" +
                        "              <b>النظام الصحي للتقييم الغذائي:<br/></b>" +
                        "                <b>الأفق الجديد للتقييم الغذائي أثناء حالات الطوارئ من خلال تقنيات الهاتف الجوال في دول الشرق الأوسط.</b> <br/>" +
                        "                هذا التطبيق للهواتف الجوالة الذي تم تمويله من منظمة الصحة العالمية -المكتب الإقليمي لشرق المتوسط واشرف على تصميمه خبراء من كلية الصحة العامة و كلية العلوم (قسم علوم الحواسب) في الجامعة اللبنانية (لبنان) ، هو نهج منظم لتوفير معلومات مهمة وموثوقة لاتخاذ القرار ، وإنشاء أنظمة وموارد مشتركة من أجل شركاء الحكومة المضيفة والمنظمات الإنسانية. إنها طريقة مسح محسّنة توازن بين البساطة (للتقييم السريع لحالات الطوارئ الحادة) والسلامة الفنية في مناطق الشرق الأوسط.<br/>" +
                        "                يشهد العالم حاليًا جائحة COVID-19 ، وهناك عدد كبير من التوصيات المنشورة حول التغذية والأكل العاطفي وأنماط الأكل والنشاط البدني والأمن الغذائي. نظرًا لأن الحجر الصحي مرتبط بانقطاع روتين العمل ، فقد يرتبط ذلك بزيادة استهلاك الطاقة ، فضلاً عن استهلاك كميات أكبر من الدهون والنشويات والبروتينات. يمكن أن يؤدي هذا النمط الغذائي غير الصحي إلى زيادة خطر الإصابة بالسمنة ، ومع نمط الحياة المستقرة قد يؤدي الى خطر الاصابة بالأمراض الغير السارية المنتشرة بكثرة في إقليم شرق المتوسط.\n" +
                        "" +
                        "        </p>" +
                        "        </body>" +
                        "        </html>" +
                        "    ", "text/html; charset=UTF-8", "utf-8");
                break;
        }
        textView1.setBackgroundColor(Color.TRANSPARENT);
        }


}
