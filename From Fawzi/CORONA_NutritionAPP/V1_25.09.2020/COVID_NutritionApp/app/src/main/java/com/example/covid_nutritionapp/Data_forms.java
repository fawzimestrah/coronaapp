package com.example.covid_nutritionapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Data_forms {

    private String Key_value;
    private String userCreator;  // userAdmin
    private String groupCreator;  // userAdmin
    private ArrayList<Data_Question> Liste_Question;
    private String nameform = "None";
    private String Dateform = "None";
    private String Descform = "None";


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


    public void setDescform(String descform) {
        Descform = descform;
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


    public static void Insert_form(String Name, String Desc,String IdUser,String GroupUser ,ArrayList<Data_Question> Questions) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FORMS");

        Data_forms D = new Data_forms();
        D.setNameform(Name);
        D.setDateform(formattedDate);
        D.setDescform(Desc);
        D.setUserCreator(IdUser);
        D.setGroupCreator(GroupUser);
        String KeyForm=myRef.push().getKey(); // jbna lkey li 5l2neha
        myRef.child(KeyForm).setValue(D);

        D.setKey_value(KeyForm);


        if (Questions!=null && Questions.size() != 0) {
            D.AddListe_Question(Questions);
            for (int i = 0; i < D.getListe_Question().size(); i++) {
                Questions.get(i).setKey_form(KeyForm);
                myRef.child(KeyForm).child("QUESTIONS").child(Questions.get(i).getGroup()+"").push().setValue(Questions.get(i));

            }
        }
    }

    public static void  Update_form(String key_Form,String Name, String dateform,String Desc,String IdUser,String GroupUser , ArrayList<Data_Question> Questions){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FORMS");

        Data_forms D = new Data_forms();
        D.setNameform(Name);
        D.setDateform(dateform);
        D.setDescform(Desc);

        D.setUserCreator(IdUser);
        D.setGroupCreator(GroupUser);
        myRef.child(key_Form).setValue(D);

        if (Questions!=null && Questions.size() != 0) {
            D.AddListe_Question(Questions);
            for (int i = 0; i < D.getListe_Question().size(); i++) {
                if(Questions.get(i).getKey_value()!=null){
                    String group="default";
                    if(Questions.get(i).getGroup()!=null){
                        group=Questions.get(i).getGroup();
                    }
                    myRef.child(key_Form).child("QUESTIONS").child(group).child(Questions.get(i).getKey_value()).setValue(Questions.get(i));
                }

                else {
                    Questions.get(i).setKey_form(key_Form);
                    myRef.child(key_Form).child("QUESTIONS").child(Questions.get(i).getGroup() + "").push().setValue(Questions.get(i));
                }
            }
        }

    }

}