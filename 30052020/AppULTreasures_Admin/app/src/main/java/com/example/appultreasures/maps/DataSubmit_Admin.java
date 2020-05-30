package com.example.appultreasures.maps;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataSubmit_Admin {


    private  String Key_value="";
    private  String User;
    public String imageName;
    public String imageURL;
    private double longitude;
    private  double latitude;
    private String Full_Name;
    private String City;
    private  String Date;
    public DataSubmit_Admin(){

    }
    public DataSubmit_Admin(String name, String url){

        this.imageName = name;
        this.imageURL= url;
    }

    public String getDate() {
        return Date;
    }
    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageURL() {
        return imageURL;
    }


    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public void setKey_value(String key_value) {
        this.Key_value = key_value;
    }

    public String key_value_DataSubmit(){
        return this.Key_value;
    }


    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
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




    public static void  Update_Point(double Latitude,double Longitude,String name,String URL,String Description,String Key,String City,String User){


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());


        FirebaseDatabase database ;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message1");
        DataSubmit_Admin D=new DataSubmit_Admin();

        D.setImageName(Description);
        D.setImageURL(URL);
        D.setLatitude(Latitude);
        D.setLongitude(Longitude);
        D.setFull_Name(name);
        D.setCity(City);
        D.setDate(formattedDate);
        D.setUser(User);

        myRef.child("Mobile").child("Cas_confirmes").child(Key).setValue(D);
        // Coordonne
        //Cas_confirmes

    }

    public static void Delete_Point_Coordonne(String Key, Context v){


        FirebaseDatabase database ;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message1");
        Toast.makeText(v.getApplicationContext(),"KeyDelete="+Key,Toast.LENGTH_LONG).show();
        myRef.child("Mobile").child("Coordonne").child(Key).setValue(null);
        // Coordonne
        //Cas_confirmes

    }


    public static void Delete_Point_Confirmee(String Key, Context v){


        FirebaseDatabase database ;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message1");
        Toast.makeText(v.getApplicationContext(),"KeyDelete="+Key,Toast.LENGTH_LONG).show();
        myRef.child("Mobile").child("Cas_confirmes").child(Key).setValue(null);
        // Coordonne
        //Cas_confirmes

    }












    public static void  Insert_Point_ADMIN(double Latitude,double Longitude,String name,String URL,String Description,String City,String User){


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        FirebaseDatabase database ;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message1");
        DataSubmit_Admin D=new DataSubmit_Admin();

        D.setImageName(Description);
        D.setImageURL(URL);
        D.setLatitude(Latitude);
        D.setLongitude(Longitude);
        D.setFull_Name(name);
        D.setCity(City);
        D.setUser(User);

        D.setDate(formattedDate);
        myRef.child("Mobile").child("Cas_confirmes").push().setValue(D);
        // Coordonne
        //Cas_confirmes

    }


}


