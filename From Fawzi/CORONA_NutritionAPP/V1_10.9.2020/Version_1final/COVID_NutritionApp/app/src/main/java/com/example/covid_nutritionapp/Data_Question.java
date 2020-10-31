package com.example.covid_nutritionapp;

import java.util.ArrayList;
import java.util.HashMap;

public class Data_Question {
    private String Key_value=null ;
    private  String Key_form;
    private HashMap<String,String> Questions =  new HashMap<String,String>();
    private String Question;
    private  int num_Item;
    private String group;
    private String TypeQuestion;
    private ArrayList<String> choix=new ArrayList<String>();
    public Data_Question() {
    }

    public Data_Question(String question) {
        Question = question;
    }

    public Data_Question(String key_form, String question, int num_Item, String group) {
        Key_form = key_form;
        Question = question;
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

    public  void addLanguage_Question(String lang, String Question ){
        Questions.put(lang,Question);


    }
    public HashMap<String, String> getQuestions() {
        return Questions;
    }

    public void setQuestions(HashMap<String, String> questions) {
        Questions = questions;
    }
}
