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
import com.google.android.gms.tasks.OnSuccessListener;
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
        D.setKey_value(KeyForm);
        Log.e("form123 insert",KeyForm);

        myRef.child(KeyForm).setValue(D);


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




    public static void  Update_form(final Activity activity, final String key_Form, final String Name, final String dateform, final String Desc, final HashMap<String,String> DescformLang, final String IdUser, final String GroupUser , final ArrayList<Data_Question> Questions){
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
        D.AddListe_Question(Questions);
        HashMap<String,Object> hashForm=new HashMap<String, Object>();
        HashMap<String,Object> hashkeyForm=new HashMap<String, Object>();
        hashForm.put("dateform",dateform);
        hashForm.put("nameform",Name);
        hashForm.put("descform",Desc);
        hashForm.put("descformLang",DescformLang);
        hashForm.put("groupCreator",GroupUser);
        hashForm.put("key_value",key_Form);
        hashForm.put("userCreator",IdUser);
        hashkeyForm.put(key_Form,hashForm);
 //       myRef.updateChildren(hashkeyForm);
        for (int i=0;i<Questions.size();i++){
            final int count=i;

            Questions.get(i).setKey_form(key_Form);
            if(Questions.get(i).getGroup()==null){
                Questions.get(i).setGroup("Default");
            }
            if(Questions.get(i).getKey_value()!=null) {
                HashMap hashQuest=new HashMap<String, Object>();

                Data_Question current= new Data_Question();
                HashMap hashkeyQuest=new HashMap<String, Object>();
                hashQuest.put("group",Questions.get(i).getGroup());
                hashQuest.put("num_Item",Questions.get(i).getNum_Item());
                hashQuest.put("question",Questions.get(i).getQuestion());
                hashQuest.put("shared",Questions.get(i).isShared());
                hashQuest.put("typeQuestion",Questions.get(i).getTypeQuestion());
                hashQuest.put("questions",Questions.get(i).getQuestions());
                hashQuest.put("key_form",Questions.get(i).getKey_form());
                hashQuest.put("key_value",Questions.get(i).getKey_value());
                hashkeyQuest.put(Questions.get(i).getKey_value(),hashQuest);
                if(Questions.get(i).getChoix().size()!=0){
                    hashQuest.put("choix",Questions.get(i).getChoix());
                }

                try {


                Log.e("testindex",D.getKey_value()+"// i="+i+"//"+Questions.get(i).getKey_value()+"  ///"+Questions.get(i).getKey_value());
                myRef.child(D.getKey_value()).child("QUESTIONS").child(Questions.get(i).getGroup()).updateChildren(hashkeyQuest)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if(count+1==Questions.size()) {
                                    View parentLayout = activity.findViewById(android.R.id.content);
                                    Snackbar.make(parentLayout, "Update !", Snackbar.LENGTH_SHORT).show();
                                    Intent intent = new Intent(activity.getApplicationContext(), Activity_MainAdmin.class);
                                    activity.setResult(2, intent);
                                    activity.finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        View parentLayout = activity.findViewById(android.R.id.content);
                        Snackbar.make(parentLayout, "Error . Can't update.", Snackbar.LENGTH_SHORT).show();
                        Toast.makeText(activity.getApplicationContext(),"Error . Can't update.\n please try again later ",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(activity.getApplicationContext(), Activity_MainAdmin.class);
                        activity.setResult(2, intent);
                        activity.finish();

                    }
                });
                }catch (Exception e){
                    View parentLayout = activity.findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Error . Can't update.", Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(activity.getApplicationContext(),"Error . Can't update.\n please try again later ",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity.getApplicationContext(), Activity_MainAdmin.class);
                    activity.setResult(2, intent);
                    activity.finish();

                }

            }else{
              String keyquestion= myRef.child(key_Form).child("QUESTIONS").child(Questions.get(i).getGroup()).push().getKey();
               Questions.get(i).setKey_value(keyquestion);
                try {

                    myRef.child(key_Form).child("QUESTIONS").child(Questions.get(i).getGroup()).child(Questions.get(i).getKey_value()).setValue(Questions.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (Questions.size() == count + 1) {
                                View parentLayout = activity.findViewById(android.R.id.content);
                                Snackbar.make(parentLayout, "Update !", Snackbar.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity.getApplicationContext(), Activity_MainAdmin.class);
                                activity.setResult(2, intent);
                                activity.finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            View parentLayout = activity.findViewById(android.R.id.content);
                            Snackbar.make(parentLayout, "Error . Can't update.", Snackbar.LENGTH_SHORT).show();

                        }
                    });

                }catch (Exception e){
                    View parentLayout = activity.findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Error . Can't update.", Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(activity.getApplicationContext(),"Error . Can't update.\n please try again later ",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity.getApplicationContext(), Activity_MainAdmin.class);
                    activity.setResult(2, intent);
                    activity.finish();

                }
            }
        }

        }











/*
    public static void  Update_form_test(final Activity activity, final String key_Form, String Name, String dateform, String Desc, HashMap<String,String> DescformLang, String IdUser, String GroupUser , final Data_Question Question){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FORMS");
        Data_forms D = new Data_forms();
        D.setNameform(Name);
        D.setDateform(dateform);
        D.setDescform(Desc);
        D.setDescformLang(DescformLang);
        D.setUserCreator(IdUser);
        D.setGroupCreator(GroupUser);
        D.setKey_value(key_Form);

//        Log.e("form123 insert",KeyForm);

//        myRef.child(key_Form).setValue(D);
        if (Question!=null ) {
                if(Question.getKey_value()==null){
                 String s =  myRef.child(key_Form).child("QUESTIONS").child(Question.getGroup()+"").push().getKey();
                    Question.setKey_value(s);
                }
                myRef.child(key_Form).child("QUESTIONS").child(Question.getGroup()+"").child(Question.getKey_value()).setValue(Question);

            }



Log.e("exception12345","updatetest size="+Question.getKey_value());
    }
*/







    public static void deleteForm(String keyForm){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FORMS");
        myRef.child(keyForm).setValue(null);


    }



}