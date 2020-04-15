package com.example.mapstest;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataSubmit {


    private  String Key_value;
    private double longitude;
    private  double latitude;
    private String Full_Name;
    private int nb_family;
    private  boolean transferred_hospital;
    private String how_transferred;
    private  boolean in_home;


    public void setKey_value(String key_value) {
        Key_value = key_value;
    }

    public String key_value_DataSubmit(){
        return this.Key_value;
    }
    public boolean isTransferred_hospital() {
        return transferred_hospital;
    }

    public void setTransferred_hospital(boolean transferred_hospital) {
        this.transferred_hospital = transferred_hospital;
    }

    public String getHow_transferred() {
        return how_transferred;
    }

    public void setHow_transferred(String how_transferred) {
        this.how_transferred = how_transferred;
    }

    public boolean isIn_home() {
        return in_home;
    }

    public void setIn_home(boolean in_home) {
        this.in_home = in_home;
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public int getNb_family() {
        return nb_family;
    }

    public void setNb_family(int nb_family) {
        this.nb_family = nb_family;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


public static void  Inser_Point(double Latitude,double Longitude,String name,int nb_family,boolean transferred_hospital,String how_transferred,boolean in_home){



    FirebaseDatabase database ;
    DatabaseReference myRef;

    database = FirebaseDatabase.getInstance();
    myRef = database.getReference("message1");
    DataSubmit D=new DataSubmit();

    D.setLatitude(Latitude);
    D.setLongitude(Longitude);
    D.setFull_Name(name);
    D.setNb_family(nb_family);
    D.setTransferred_hospital(transferred_hospital);
    D.setHow_transferred(how_transferred);
    D.setIn_home(in_home);//hon asdna eza 3ezel halo aw la2

    if(transferred_hospital){
    D.setHow_transferred(how_transferred);
    }else{
        D.setHow_transferred("XX");
    }

    myRef.child("Mobile").child("Coordonne").push().setValue(D);
   // Coordonne
    //Cas_confirmes

    }



// batalna nst3mla
public static  DataSubmit[] get_all_point(){
        DataSubmit D_all=null;

    FirebaseDatabase database ;
    DatabaseReference myRef ;

    database = FirebaseDatabase.getInstance();
    myRef = database.getReference("message1");

    myRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });





        return null;
}


    public static void  Insert_Point_ADMIN(double Latitude,double Longitude,String name,int nb_family,boolean transferred_hospital,String how_transferred,boolean in_home){

        FirebaseDatabase database ;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message1");
        DataSubmit D=new DataSubmit();

        D.setLatitude(Latitude);
        D.setLongitude(Longitude);
        D.setFull_Name(name);
        D.setNb_family(nb_family);
        D.setTransferred_hospital(transferred_hospital);
        D.setHow_transferred(how_transferred);
        D.setIn_home(in_home);//hon asdna eza 3ezel halo aw la2

        if(transferred_hospital){
            D.setHow_transferred(how_transferred);
        }else{
            D.setHow_transferred("XX");
        }

        myRef.child("Mobile").child("Cas_confirmes").push().setValue(D);
        // Coordonne
        //Cas_confirmes

    }





 }


