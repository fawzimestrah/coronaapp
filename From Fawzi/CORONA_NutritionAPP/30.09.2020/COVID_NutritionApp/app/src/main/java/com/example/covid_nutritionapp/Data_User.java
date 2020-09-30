package com.example.covid_nutritionapp;
import com.example.covid_nutritionapp.Admin.Data_GroupADMIN;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Data_User {
    private String user_id;
    private String email;
    private String class_Type;
    private String date_Register;


    private String date_naissance;
    private String sexe;
    private String poids;
    private String taille;
    private String niveau_Education;
    private String  lieu_Resistance;
    private String situation_Sociale;
    private String nb_prs_famille;
    private String nb_fils;
    private String nb_fils_prs;
    private String nb_chambres;
    private String cas_professionnel;
    private String specialiste_sante;
    private String assurance;
    private String suivi_recommandations;
    private String alcool;
    private String bolciguarette;
    private String nb_ciguarette;
    private String bolnarguile;
    private String nb_Narguile;

    public Data_User() {
    }

    public Data_User(String user_id, String email, String class_Type) {
        this.user_id = user_id;
        this.email = email;
        this.class_Type = class_Type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getData_Register() {
        return date_Register;
    }



    public String getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(String date_naissance) {
        this.date_naissance = date_naissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getPoids() {
        return poids;
    }

    public void setPoids(String poids) {
        this.poids = poids;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getNiveau_Education() {
        return niveau_Education;
    }

    public void setNiveau_Education(String niveau_Education) {
        this.niveau_Education = niveau_Education;
    }

    public String getLieu_Resistance() {
        return lieu_Resistance;
    }

    public void setLieu_Resistance(String lieu_Resistance) {
        this.lieu_Resistance = lieu_Resistance;
    }

    public String getSituation_Sociale() {
        return situation_Sociale;
    }

    public void setSituation_Sociale(String situation_Sociale) {
        this.situation_Sociale = situation_Sociale;
    }

    public String getNb_prs_famille() {
        return nb_prs_famille;
    }

    public void setNb_prs_famille(String nb_prs_famille) {
        this.nb_prs_famille = nb_prs_famille;
    }

    public String getNb_fils() {
        return nb_fils;
    }

    public void setNb_fils(String nb_fils) {
        this.nb_fils = nb_fils;
    }

    public String getNb_fils_prs() {
        return nb_fils_prs;
    }

    public void setNb_fils_prs(String nb_fils_prs) {
        this.nb_fils_prs = nb_fils_prs;
    }

    public String getNb_chambres() {
        return nb_chambres;
    }

    public void setNb_chambres(String nb_chambres) {
        this.nb_chambres = nb_chambres;
    }

    public String getCas_professionnel() {
        return cas_professionnel;
    }

    public void setCas_professionnel(String cas_professionnel) {
        this.cas_professionnel = cas_professionnel;
    }

    public String getSpecialiste_sante() {
        return specialiste_sante;
    }

    public void setSpecialiste_sante(String specialiste_sante) {
        this.specialiste_sante = specialiste_sante;
    }

    public String getAssurance() {
        return assurance;
    }

    public void setAssurance(String assurance) {
        this.assurance = assurance;
    }

    public String getSuivi_recommandations() {
        return suivi_recommandations;
    }

    public void setSuivi_recommandations(String suivi_recommandations) {
        this.suivi_recommandations = suivi_recommandations;
    }

    public String getAlcool() {
        return alcool;
    }

    public void setAlcool(String alcool) {
        this.alcool = alcool;
    }

    public String getNb_ciguarette() {
        return nb_ciguarette;
    }

    public void setNb_ciguarette(String nb_ciguarette) {
        this.nb_ciguarette = nb_ciguarette;
    }

    public String getNb_Narguile() {
        return nb_Narguile;
    }

    public void setNb_Narguile(String nb_Narguile) {
        this.nb_Narguile = nb_Narguile;
    }

    public void setData_Register(String data_Register) {
        date_Register = data_Register;
    }

    public String getBolciguarette() {
        return bolciguarette;
    }

    public void setBolciguarette(String bolciguarette) {
        this.bolciguarette = bolciguarette;
    }

    public String getBolnarguile() {
        return bolnarguile;
    }

    public void setBolnarguile(String bolnarguile) {
        this.bolnarguile = bolnarguile;
    }

    public static void Insert_User(Data_User user){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        user.setData_Register(formattedDate);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("USERS");
        myRef.child(user.getUser_id()).setValue(user);
    }

    public static void insert_User_Admin(Data_User user, ArrayList<Data_GroupADMIN> ListGroup){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        user.setData_Register(formattedDate);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("USERS");
        myRef.child(user.getUser_id()).setValue(user);
        for(int i=0;i<ListGroup.size();i++) {
            if(ListGroup.get(i).isChecked()){
                myRef.child(user.getUser_id()).child("Groups").push().setValue(ListGroup.get(i).getKey_Group());
            }
        }
        Data_GroupADMIN.insertMemberGroup(ListGroup,user.getUser_id());
    }

    public String getClass_Type() {
        return class_Type;
    }

    public void setClass_Type(String class_Type) {
        this.class_Type = class_Type;
    }
}
