package com.example.mapstest;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Data_Region {

    private double Longitude;
    private  double Latitude;
    private String nom;
    private int cas_confirmes;
    private int nb_decedes;
    private int nb_recuperees;

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        this.Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        this.Latitude = latitude;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCas_confirmes() {
        return cas_confirmes;
    }

    public void setCas_confirmes(int cas_confirmes) {
        this.cas_confirmes = cas_confirmes;
    }

    public int getNb_decedes() {
        return nb_decedes;
    }

    public void setNb_decedes(int nb_decedes) {
        this.nb_decedes = nb_decedes;
    }

    public int getNb_recuperees() {
        return nb_recuperees;
    }

    public void setNb_recuperees(int nb_recuperees) {
        this.nb_recuperees = nb_recuperees;
    }








    static  Data_Region [] getnb_of_Data_Region(final Context context){

        FirebaseDatabase database ;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("region");




        return null;

    }








}
