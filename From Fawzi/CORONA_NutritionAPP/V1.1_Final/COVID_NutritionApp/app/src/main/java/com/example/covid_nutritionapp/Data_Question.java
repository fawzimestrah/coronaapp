package com.example.covid_nutritionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Data_Question  {
    private String Key_value=null ;
    private  String Key_form=null;
    private HashMap<String,String> Questions =  new HashMap<String,String>();
    private String Question="";
    private  int num_Item=0;
    private String group="Default";
    private String TypeQuestion="";
    private boolean isShared=true;
    private ArrayList<String> choix=new ArrayList<String>();
    public Data_Question() {
    }

    public Data_Question(String lang,String question) {

        Question = question;
        addLanguage_Question(lang,question);
    }

    public Data_Question(String key_form, String lang,String question, int num_Item, String group) {
        Key_form = key_form;
        Question = question;
        addLanguage_Question(lang,question);
        this.num_Item = num_Item;
        this.group = group;
    }

    public String getKey_value() {
        return Key_value;
    }

    public void setKey_value(String key_value) {
        Key_value = key_value;
    }


    public String getQuestion() {
        return Question;
    }

    public String getQuestion_lang(String lang) {
        return Questions.get(lang);
    }


    public  void addLanguage_Question(String lang, String Question ){
        Questions.put(lang,Question);

    }
    public HashMap<String, String> getQuestions() {
        return Questions;
    }


    public void setQuestions(HashMap<String, String> questions) {
        Questions = questions;
    }


    // fct  firebase
    public void setQuestion(String question) {
        Question = question;
    }
    // fct code
    public void setQuestion(String lang,String question) {
        addLanguage_Question(lang,question);
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

    public String getKey_form() {
        return Key_form;
    }

    public void setKey_form(String key_form) {
        Key_form = key_form;
    }


    public String getTypeQuestion() {
        return TypeQuestion;
    }

    public void setTypeQuestion(String typeQuestion) {
        TypeQuestion = typeQuestion;
    }

    public ArrayList<String> getChoix() {
        return choix;
    }

    public void AddChoix(String choix) {
        this.choix.add(choix);
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }


    public static void addQuestion(Data_Question Question){

    }

    public static void  updateQuestion(){

    }


    public static void deleteQuestion(Data_Question Question){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FORMS");
        Question.setShared(false);

        myRef.child(Question.getKey_form()).child("QUESTIONS").child(Question.getGroup()).child(Question.getKey_value()).setValue(Question);
        //myRef.child(Question.getKey_form()).child(Question.getKey_value()).setValue(null);


    }


    
}
