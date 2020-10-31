package com.example.covid_nutritionapp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.covid_nutritionapp.Admin.Activity_MainAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Data_forms {

    private String Key_value;
    private String userCreator;  // userAdmin
    private String groupCreator;  // userAdmin
    private ArrayList<Data_Question> Liste_Question;
    private String nameform = "None";
    private String Dateform = "None";

    private String Descform = "None";
   private HashMap<String,String> DescformLang = new HashMap<String,String>();


    public Data_forms(String testname, String date) {
        //    Key_value = key_value;
        //   this.user = user;
        this.nameform = testname;
        this.setDateform(date);
    }

    public Data_forms() {

    }

    public String getGroupCreator() {
        return groupCreator;
    }

    public void setGroupCreator(String groupCreator) {
        this.groupCreator = groupCreator;
    }

    public String getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(String userCreator) {
        this.userCreator = userCreator;
    }

    public String getNameform() {
        return nameform;
    }

    public void setNameform(String nameform) {
        this.nameform = nameform;
    }

    public String getDateform() {
        return Dateform;
    }

    public void setDateform(String dateform) {
        Dateform = dateform;
    }


    public void setKey_value(String key_value) {
        Key_value = key_value;
    }

    public String getKey_value() {
        return Key_value;
    }

    public String getDescform() {
        return Descform;
    }
    // fct  firebase
    public void setDescform(String descform) {
        Descform = descform;
    }
    // fct code
    public void setDescform(String lang,String desc) {
        addDescLang(lang,desc);
        Descform = desc;
    }

    public String getDescForm_lang(String lang) {
        return DescformLang.get(lang);
    }




    // hashmap


    public HashMap<String, String> getDescformLang() {
        return DescformLang;
    }

    public void setDescformLang(HashMap<String, String> descformLang) {
        DescformLang = descformLang;
    }

    public  void addDescLang(String lang, String description ){
        DescformLang.put(lang,description);

    }




    public ArrayList<Data_Question> getListe_Question() {
        return Liste_Question;
    }

    public void AddListe_Question(ArrayList<Data_Question> Question) {
        if (Liste_Question == null) {
            Liste_Question = new ArrayList<Data_Question>();
        }
        for (int i = 0; i < Question.size(); i++) {
            Liste_Question.add(Question.get(i));
        }
    }


    public static String  Insert_form(String Name, String Desc,HashMap<String,String> DescformLang ,String IdUser,String GroupUser ,ArrayList<Data_Question> Questions) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FORMS");
        Data_forms D = new Data_forms();
        D.setNameform(Name);
        D.setDateform(formattedDate);
        D.setDescform(Desc);
        D.setDescformLang(DescformLang);
        D.setUserCreator(IdUser);
        D.setGroupCreator(GroupUser);
        String KeyForm=myRef.push().getKey(); // jbna lkey li 5l2neha

        Log.e("form123 insert",KeyForm);

        myRef.child(KeyForm).setValue(D);

        D.setKey_value(KeyForm);


        if (Questions!=null && Questions.size() != 0) {
            D.AddListe_Question(Questions);
            for (int i = 0; i < D.getListe_Question().size(); i++) {
                Questions.get(i).setKey_form(KeyForm);
                String c1=myRef.child(KeyForm).child("QUESTIONS").child(Questions.get(i).getGroup()+"").push().getKey();
                Questions.get(i).setKey_value(c1);
                myRef.child(KeyForm).child("QUESTIONS").child(Questions.get(i).getGroup()+"").child(c1).setValue(Questions.get(i));

            }
        }
    return KeyForm;
    }




    public static void  Update_form(final Activity activity, final String key_Form, String Name, String dateform, String Desc, HashMap<String,String> DescformLang, String IdUser, String GroupUser , final ArrayList<Data_Question> Questions){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("FORMS");

        Data_forms D = new Data_forms();
        D.setNameform(Name);
        D.setDateform(dateform);
        D.setDescform(Desc);

//            Log.e("lang123AR",DescformLang.get("AR"));
  //          Log.e("lang123EN",DescformLang.get("EN"));

        D.setDescformLang(DescformLang);
        D.setUserCreator(IdUser);
        D.setGroupCreator(GroupUser);
        D.setKey_value(key_Form);
        Log.e("form123 update",key_Form);
       myRef.child(key_Form).setValue(D);

//        myRef.child(key_Form).child("QUESTIONS").removeValue();
        if (Questions!=null && Questions.size() != 0) {
            D.AddListe_Question(Questions);

            Log.e("size=",""+Questions.size());

            for (int i = 0; i < D.getListe_Question().size(); i++) {
                if(Questions.get(i).getKey_value()!=null){
                    String group="Default";
                    if(Questions.get(i).getGroup()!=null){
                        group=Questions.get(i).getGroup();
                    }
                    try
                    {
                        Log.e("text1",i+" :keyf="+key_Form+"  group:"+group+"  keyvalue:"+Questions.get(i).getKey_value()+ "");
                        myRef.child(key_Form).child("QUESTIONS").child(group).child(Questions.get(i).getKey_value()).setValue(Questions.get(i))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                View parentLayout = activity.findViewById(android.R.id.content);
                                Snackbar.make(parentLayout, "Update !", Snackbar.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity.getApplicationContext(), Activity_MainAdmin.class);
                                activity.setResult(2,intent);
                                activity.finish();

                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                View parentLayout = activity.findViewById(android.R.id.content);
                                Snackbar.make(parentLayout, "Error . Can't update.", Snackbar.LENGTH_SHORT).show();

                            }
                        });

                    }
                    catch (Exception e)
                    {
                    Log.e("exception1234",e.getMessage());

                    }
                     }
                else {
                    Questions.get(i).setKey_form(key_Form);
                    myRef.child(key_Form).child("QUESTIONS").child(Questions.get(i).getGroup() +"").push().setValue(Questions.get(i));
                }
            }
        }

    }



    public static void  Update_form_test(final Activity activity, final String key_Form, String Name, String dateform, String Desc, HashMap<String,String> DescformLang, String IdUser, String GroupUser , final ArrayList<Data_Question> Questions){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("FORMS");

        Data_forms D = new Data_forms();
        D.setNameform(Name);
        D.setDateform(dateform);
        D.setDescform(Desc);

        D.setDescformLang(DescformLang);
        D.setUserCreator(IdUser);
        D.setGroupCreator(GroupUser);
        D.setKey_value(key_Form);
        Log.e("form123 update",key_Form);
        myRef.child(key_Form).setValue(D);

        myRef.child(key_Form).child("QUESTIONS").removeValue();
        if (Questions!=null && Questions.size() != 0) {
            D.AddListe_Question(Questions);

            for (int i = 0; i < D.getListe_Question().size(); i++) {
                if(Questions.get(i).getKey_value()!=null){
                    String group="Default";
                    if(Questions.get(i).getGroup()!=null){
                        group=Questions.get(i).getGroup();
                    }
                    try
                    {
                        Log.e("text1",i+" :keyf="+key_Form+"  group:"+group+"  keyvalue:"+Questions.get(i).getKey_value()+ "");
                        myRef.child(key_Form).child("QUESTIONS").child(group).child(Questions.get(i).getKey_value()).setValue(Questions.get(i))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        View parentLayout = activity.findViewById(android.R.id.content);
                                        Snackbar.make(parentLayout, "Update !", Snackbar.LENGTH_SHORT).show();
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                View parentLayout = activity.findViewById(android.R.id.content);
                                Snackbar.make(parentLayout, "Error . Can't update.", Snackbar.LENGTH_SHORT).show();

                            }
                        });

                    }
                    catch (Exception e)
                    {
                        Log.e("exception1234",e.getMessage());

                    }
                }
                else {
                    Questions.get(i).setKey_form(key_Form);
                    myRef.child(key_Form).child("QUESTIONS").child(Questions.get(i).getGroup() +"").push().setValue(Questions.get(i));
                }
            }
        }

    }








    public static void deleteForm(String keyForm){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FORMS");
        myRef.child(keyForm).setValue(null);


    }



}