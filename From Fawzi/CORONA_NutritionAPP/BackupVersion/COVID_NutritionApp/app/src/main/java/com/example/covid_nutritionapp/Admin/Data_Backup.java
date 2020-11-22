package com.example.covid_nutritionapp.Admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.covid_nutritionapp.Data_GroupItem;
import com.example.covid_nutritionapp.Data_Question;
import com.example.covid_nutritionapp.Data_User;
import com.example.covid_nutritionapp.Data_UserAnswer;
import com.example.covid_nutritionapp.Data_answer;
import com.example.covid_nutritionapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.net.InternetDomainName;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class Data_Backup {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    ValueEventListener myValueEventListner;
    int count=0;
    boolean startAfterQuest=true;
    boolean startAfterReponse=false;

    String keyForm="";
    public static void BackupExecel(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            return;
        }

    }

    public void saveExcelData(final Activity activity, final String keyForm) {
        this.keyForm=keyForm;

        final ArrayList<Data_Question> listQuest=new ArrayList<Data_Question>();
        final ArrayList<ArrayList<Data_answer>>allListReponse=new ArrayList<ArrayList<Data_answer>>();
        final ArrayList<String> listUserString=new ArrayList<String>();
        myRef = database.getReference("FORMS");
        final DatabaseReference myRef_details = database.getReference("FORMS");

        myValueEventListner= myRef.child(keyForm).child("QUESTIONS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listQuest.clear();
                allListReponse.clear();
                listUserString.clear();
                final long nbgroupstart=dataSnapshot.getChildrenCount();
                int start=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    start++;
                    final Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());
                    myRef_details.child(keyForm).child("QUESTIONS").child(D.getName_group()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Data_Question Dq = snapshot.getValue(Data_Question.class);
                                Dq.setKey_value(snapshot.getKey());
                                listQuest.add(Dq);
                                Log.e("indexquestionsize1=",Dq.getQuestion()+"");
                                Log.e("indexquestionsize=",listQuest.size()+"");
                            }
                            //start
                            if(database==null){

                            }else if (startAfterQuest){
                                startAfterQuest=false;
                                Log.e("hhhhuu","aa");
                                myRef = database.getReference("FORMS_Data").child(keyForm);
                                 myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final long mySnapcountUser=dataSnapshot.getChildrenCount();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        // final ArrayList<ArrayList<Data_answer>>allReponseuser=new ArrayList<ArrayList<Data_answer>>();
                                        final Data_UserAnswer U = snapshot.getValue(Data_UserAnswer.class);
                                        U.setUser(snapshot.getKey());
                                        final Data_UserAnswer User=new Data_UserAnswer(U.getUser());
                                        listUserString.add(User.getUser());
                                        if(database!=null) {
                                            DatabaseReference myRef_Reponse = database.getReference("FORMS_Data");
                                            final DatabaseReference myRef_Reponse_details = database.getReference("FORMS_Data");
                                            myRef_Reponse.child(keyForm).child(User.getUser()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    //chaque User
                                                    final ArrayList<Data_answer> listReponse = new ArrayList<Data_answer>();
                                                    count++;
                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        //chaque group
                                                        Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                                                        D.setName_group(snapshot.getKey());
                                                        if (mySnapcountUser == count) {
                                                            Log.e("www yes", count + " //" + mySnapcountUser);
                                                            startAfterReponse = true;
                                                        } else {
                                                            Log.e("www", count + " //" + mySnapcountUser);
                                                        }
                                                        myRef_Reponse_details.child(keyForm).child(User.getUser()).child(D.getName_group()).addValueEventListener(new ValueEventListener() {
                                                            @SuppressLint("NewApi")
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                // chaque question
                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                    Data_answer Dq = snapshot.getValue(Data_answer.class);
                                                                    Dq.setKey_value(snapshot.getKey());
                                                                    listReponse.add(Dq);
                                                                }
                                                                Log.e("reponsecurrent", "size rep" + listReponse.size());

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });

                                                    }
                                                    allListReponse.add(listReponse);
                                                    if (mySnapcountUser == count) {
                                                        if (startAfterReponse) {
                                                            getAllUser(activity, listUserString, listQuest, allListReponse);
                                                            Log.e("reponse", count + "size rep" + allListReponse.size());
                                                        }
                                                    } else {
                                                        Log.e("reponse", "else size rep" + allListReponse.size());

                                                    }


                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    }

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }







    public void checkPermission(Context context,Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }
    }


    public void getAllUser(final Activity activity, final ArrayList<String> listAllUserString, final ArrayList<Data_Question>listQuest, final ArrayList<ArrayList<Data_answer>> allListReponse){
        final ArrayList<Data_User> listUser= new ArrayList<Data_User>();
        myRef = database.getReference("USERS");
        listUser.clear();
        for( final String userKey : listAllUserString){
            Log.e("data",userKey);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getKey().equals(userKey)){
                        Data_User U = snapshot.getValue(Data_User.class);
                        listUser.add(U);
                       Log.e("nbuserloop",U.getEmail() +"// size="+listUser.size());
                        }
                    }

                    if(listAllUserString.indexOf(userKey)==listAllUserString.size()-1){ // last index ==> finish
                        Log.e("nbuserloop1",listUser.size()+"");
                        myRef.onDisconnect().cancel();
                        saveDatalaXlsx(activity,listUser,listQuest,allListReponse);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    View parentLayout = activity.findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Error "+databaseError.getMessage(), Snackbar.LENGTH_LONG).show();

                }
            });
    }

    }

    public void saveDatalaXlsx(Activity activity,ArrayList<Data_User> listAllUser,ArrayList<Data_Question>listQuestion,ArrayList<ArrayList<Data_answer>> allListReponse){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName="CDNutrition-"+timeStamp+".csv";

        boolean success = false;
        //New Workbook
        Workbook wb = new HSSFWorkbook();
        Cell c = null;
        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        CellStyle csRed = wb.createCellStyle();
        csRed.setFillForegroundColor(HSSFColor.RED.index);
        csRed.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        CellStyle csNothing = wb.createCellStyle();
        csNothing.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        csNothing.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet

        Sheet sheet1 = null;
        sheet1 = wb.createSheet("myForm");

        // Generate column headings (Questions)
        Row row = sheet1.createRow(0);
        Cell cr=row.createCell(0);

        cr.setCellValue(activity.getResources().getString(R.string.email));
        cr=row.createCell(1);
        cr.setCellValue(activity.getResources().getString(R.string.register));
        cr=row.createCell(2);
        cr.setCellValue(activity.getResources().getString(R.string.date_naissance_EN));
        cr=row.createCell(3);
        cr.setCellValue(activity.getResources().getString(R.string.sexe_EN));
        cr=row.createCell(4);
        cr.setCellValue(activity.getResources().getString(R.string.poids_actuel_EN));
        cr=row.createCell(5);
        cr.setCellValue(activity.getResources().getString(R.string.taille_actuelle_EN));
        cr=row.createCell(6);
        cr.setCellValue(activity.getResources().getString(R.string.niveau_edu_EN));
        cr=row.createCell(7);
        cr.setCellValue(activity.getResources().getString(R.string.lieu_residance_EN));
        cr=row.createCell(8);
        cr.setCellValue(activity.getResources().getString(R.string.gpscity));
        cr=row.createCell(9);
        cr.setCellValue(activity.getResources().getString(R.string.situation_sociale_EN));
        cr=row.createCell(10);
        cr.setCellValue(activity.getResources().getString(R.string.nb_prs_famille_EN));
        cr=row.createCell(11);
        cr.setCellValue(activity.getResources().getString(R.string.nb_fils_EN));
        cr=row.createCell(12);
        cr.setCellValue(activity.getResources().getString(R.string.nb_fils_prs_EN));
        cr=row.createCell(13);
        cr.setCellValue(activity.getResources().getString(R.string.nb_chambres_EN));
        cr=row.createCell(14);
        cr.setCellValue(activity.getResources().getString(R.string.cas_professionnel_EN));
        cr=row.createCell(15);
        cr.setCellValue(activity.getResources().getString(R.string.spec_sante_EN));
        cr=row.createCell(16);
        cr.setCellValue(activity.getResources().getString(R.string.assurance_EN));
        cr=row.createCell(17);
        cr.setCellValue(activity.getResources().getString(R.string.quest16_EN));
        cr=row.createCell(18);
        cr.setCellValue(activity.getResources().getString(R.string.quest17_EN));
        cr=row.createCell(19);
        cr.setCellValue(activity.getResources().getString(R.string.quest18_EN));

       ArrayList<Integer> notInclude=new ArrayList<Integer>();
        for(int i=0;i<listQuestion.size();i++){
                Log.e("indexi",i+" //"+listQuestion.size());
                c = row.createCell(i+20); // question register
                if(listQuestion.get(i).getQuestion_lang("EN")!=null && !listQuestion.get(i).getQuestion_lang("EN").equals("")&& !listQuestion.get(i).getQuestion_lang("EN").equals(" ") ){
                      c.setCellValue(listQuestion.get(i).getQuestion_lang("EN"));
                }else{
                    c.setCellValue(listQuestion.get(i).getQuestion_lang("AR"));
                }
            if( !listQuestion.get(i).isShared()){
                notInclude.add(i);
                Log.e("notinclude",i+"");
                c.setCellStyle(csRed);
            }else{
                c.setCellStyle(cs);
            }

            sheet1.setColumnWidth(i, (15 * 500));

        }


        Log.e("sizes123","size useres="+listAllUser.size()+" // size answers"+allListReponse.size());
        // register user + answers
        for(int j=0;j<allListReponse.size();j++) {
            if (listAllUser.size() > j) {

                Row rowReponse = sheet1.createRow(j + 1); // index =0 :question form
                Cell c1 = rowReponse.createCell(0);
                c1.setCellValue(listAllUser.get(j).getEmail());
                c1 = rowReponse.createCell(1);
                c1.setCellValue(listAllUser.get(j).getData_Register());
                c1 = rowReponse.createCell(2);
                c1.setCellValue(listAllUser.get(j).getDate_naissance());
                c1 = rowReponse.createCell(3);
                c1.setCellValue(listAllUser.get(j).getSexe());
                c1 = rowReponse.createCell(4);
                c1.setCellValue(listAllUser.get(j).getPoids());
                c1 = rowReponse.createCell(5);
                c1.setCellValue(listAllUser.get(j).getTaille());
                c1 = rowReponse.createCell(6);
                c1.setCellValue(listAllUser.get(j).getNiveau_Education());
                c1 = rowReponse.createCell(7);
                c1.setCellValue(listAllUser.get(j).getLieu_Resistance());
                c1 = rowReponse.createCell(8);
                c1.setCellValue(listAllUser.get(j).getCity());
                c1 = rowReponse.createCell(9);
                c1.setCellValue(listAllUser.get(j).getSituation_Sociale());
                c1 = rowReponse.createCell(10);
                c1.setCellValue(listAllUser.get(j).getNb_prs_famille());
                c1 = rowReponse.createCell(11);
                c1.setCellValue(listAllUser.get(j).getNb_fils());
                c1 = rowReponse.createCell(12);
                c1.setCellValue(listAllUser.get(j).getNb_fils_prs());
                c1 = rowReponse.createCell(13);
                c1.setCellValue(listAllUser.get(j).getNb_chambres());
                c1 = rowReponse.createCell(14);
                c1.setCellValue(listAllUser.get(j).getCas_professionnel());
                c1 = rowReponse.createCell(15);
                c1.setCellValue(listAllUser.get(j).getSpecialiste_sante());
                c1 = rowReponse.createCell(16);
                c1.setCellValue(listAllUser.get(j).getAssurance());
                c1 = rowReponse.createCell(17);
                c1.setCellValue(listAllUser.get(j).getAlcool());
                c1 = rowReponse.createCell(18);
                c1.setCellValue(listAllUser.get(j).getNb_ciguarette());
                c1 = rowReponse.createCell(19);
                c1.setCellValue(listAllUser.get(j).getNb_Narguile());


                for (int k = 0; k < allListReponse.get(j).size(); k++) {
                    ArrayList<Integer> notInc = new ArrayList<Integer>();
                    notInc.addAll(notInclude);
                    if (!notInclude.contains(k)) {
                        c = rowReponse.createCell(k + 20);
                        c.setCellValue(allListReponse.get(j).get(k).getAnswer());
                        //           c.setCellStyle(cs);
                        sheet1.setColumnWidth(k, (15 * 500));
                        Log.e("counter1", "j= " + j + "// k=" + k + "//" + allListReponse.get(j).size() + " // " + listAllUser.get(j).getEmail());
                    } else {
                        if (allListReponse.get(j).get(k).getNum_Item() == k + 1) {
                            c = rowReponse.createCell(k + 20);
                            c.setCellValue(allListReponse.get(j).get(k).getAnswer());
                            //            c.setCellStyle(cs);
                            sheet1.setColumnWidth(k, (15 * 500));
                            c.setCellStyle(csRed);
                        } else {
                            c = rowReponse.createCell((k) + 20);
                            for (int z = 0; z < notInc.size(); z++) {
                                int temp = notInc.get(z);
                                notInc.remove(z);
                                notInc.add(z, temp + 1);
                                Log.e("ss1", notInclude.get(z) + "");
                                Log.e("ss2", notInc.get(z) + "");
                            }
                            allListReponse.get(j).add(k, null);
                            c.setCellValue(" ");
                            sheet1.setColumnWidth(k, (15 * 500));
                            c.setCellStyle(csNothing);
//                        Log.e("taggg",k+"//"+allListReponse.get(j).get(k).getNum_Item()+"// "+allListReponse.get(j).get(k).getAnswer());

                        }
                    }

                }
            }
        }
        File root = new File(Environment.getExternalStorageDirectory(), "CovidNutrition");
        if (!root.exists()) {
            root.mkdirs();
        }

        File file = new File(root, fileName);
        FileOutputStream os = null;
        Writer out = null;
            try {
                os = new FileOutputStream(file);
                wb.write(os);
                Log.e("FileUtils", "Writing file" + file);
                success = true;
                Activity_ShowAllAnswers.finishExportdone(activity);
                //                System.exit(100);
            } catch (IOException e) {
                Log.e("FileUtils", "Error writing " + file, e);
                Toast.makeText(activity.getApplicationContext(),"Error "+e,Toast.LENGTH_LONG).show();
                database=null;

            } catch (Exception e) {
                Log.e("FileUtils", "Failed to save file", e);
                Toast.makeText(activity.getApplicationContext(),"Error "+e,Toast.LENGTH_LONG).show();
                database=null;

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                try {
                    if (null != os)
                        os.close();
                    database=null;
                } catch (Exception ex) {
                    Toast.makeText(activity.getApplicationContext(),"Error "+ex,Toast.LENGTH_LONG).show();
                    database=null;

                }
            }
//            return success;
        }


}
