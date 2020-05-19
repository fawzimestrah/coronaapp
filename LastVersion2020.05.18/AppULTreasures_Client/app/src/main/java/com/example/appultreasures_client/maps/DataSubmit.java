package com.example.appultreasures_client.maps;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataSubmit {


    private  String Key_value="";
    public String imageName;
    public String imageURL;
    private double longitude;
    private  double latitude;
    private String Full_Name;
    private  String city;

    public DataSubmit(){

    }
    public DataSubmit(String name, String url){

        this.imageName = name;
        this.imageURL= url;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
        Key_value = key_value;
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


public static void  Inser_Point(double Latitude,double Longitude,String name,String URL,String Description,String City){



    FirebaseDatabase database ;
    DatabaseReference myRef;

    database = FirebaseDatabase.getInstance();
    myRef = database.getReference("message1");
    DataSubmit D=new DataSubmit();

    D.setImageName(Description);
    D.setImageURL(URL);
    D.setLatitude(Latitude);
    D.setLongitude(Longitude);
    D.setFull_Name(name);
    D.setCity(City);

    myRef.child("Mobile").child("Coordonne").push().setValue(D);
   // Coordonne
    //Cas_confirmes

    }




    public static void  Insert_Point_ADMIN(double Latitude,double Longitude,String name){

        FirebaseDatabase database ;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message1");
        DataSubmit D=new DataSubmit();

        D.setLatitude(Latitude);
        D.setLongitude(Longitude);
        D.setFull_Name(name);
        myRef.child("Mobile").child("Cas_confirmes").push().setValue(D);
        // Coordonne
        //Cas_confirmes

    }





 }


