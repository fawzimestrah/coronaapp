package com.example.covid_nutritionapp;

import java.util.ArrayList;

public class Data_GroupItem {

    private int Completed;
    private String name_group;
    private ArrayList<Data_Question> allQuest=new ArrayList<Data_Question>();

    public Data_GroupItem() {

    }


    public int getCompleted() {
        return Completed;
    }

    public void setCompleted(int completed) {
        Completed = completed;
    }

    public String getName_group() {
        return name_group;
    }

    public void setName_group(String name_group) {
        this.name_group = name_group;
    }

    public ArrayList<Data_Question> getAllQuest() {
        return allQuest;
    }

    public void setAllQuest(ArrayList<Data_Question> allQuest) {
        this.allQuest = allQuest;
    }



}
