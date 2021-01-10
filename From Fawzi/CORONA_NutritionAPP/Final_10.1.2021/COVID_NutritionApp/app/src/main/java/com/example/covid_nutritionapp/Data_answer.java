package com.example.covid_nutritionapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Data_answer {

    private  String Key_form;
    private String Key_value=null ;
    private String Key_value_Quest=null ;
    private String User;
    private String Question;
    private String Answer="";
    private ArrayList<String> grpAnswers=new ArrayList<String>();
    private  int num_Item;
    private String group;

    public Data_answer() {
    }
    public Data_answer(String Quest,String key_Quest) {
    this.setQuestion(Quest);
    this.setKey_value(key_Quest);
    }

    public String getKey_value() {
        return Key_value;
    }

    public void setKey_value(String key_value) {
        Key_value = key_value;
    }

    public String getKey_value_Quest() {
        return Key_value_Quest;
    }

    public void setKey_value_Quest(String key_value_Quest) {
        Key_value_Quest = key_value_Quest;
    }

    public String getKey_form() {
        return Key_form;
    }

    public void setKey_form(String key_form) {
        Key_form = key_form;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public int getNum_Item() {
        return num_Item;
    }

    public void setNum_Item(int num_Item) {
        this.num_Item = num_Item;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public ArrayList<String> getGrpAnswers() {
        return grpAnswers;
    }

    public void setGrpAnswers(ArrayList<String> grpAnswers) {
        this.grpAnswers = grpAnswers;
    }

    public void addGrpAnswers(String Answer) {
        this.grpAnswers.add(Answer);
    }

    public  static  void insert_answer(String keyform, String keyvalue, String quest, String answer, String user , int nbItem, String group , int Comp,ArrayList<String> choix ){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FORMS_Data").child(keyform).child(user).child(group);


        Data_answer D = new Data_answer();

        D.setUser(user);
        D.setKey_form(keyform);
        D.setQuestion(quest);
        D.setAnswer(answer);
        D.setNum_Item(nbItem);
        D.setGroup(group);
        if(choix!=null){
            D.setGrpAnswers(choix);
        }
        Data_GroupItem G = new Data_GroupItem();
        if(Comp==0) {
            G.setCompleted(0);

        }else{
            G.setCompleted(1);
        }
     //   myRef.setValue(G);

        if(keyvalue!=null){ // new answer
            myRef.child(keyvalue).setValue(D); // fine shila hl2
                                                                    // bas lbde a3mlo eno ma hot flag complet =>bl activity admin nshouf 3adad kl group wbsir b3ml test
                                                                        // if nb == nbtotal ==> be5eda w eza la ma b5da
        }
        else {
            myRef.push().setValue(D);   //.child("Data")..push....
        }



    }





}
