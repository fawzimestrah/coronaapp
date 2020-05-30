package com.example.appultreasures_client.maps;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataImage {

    private String key_value;
    public String imageName;
    public String imageURL;
    public String  city;

    public DataImage() {

    }

    public DataImage(String name, String url) {

        this.imageName = name;
        this.imageURL= url;
    }


    public String getKey_value() {
        return key_value;
    }

    public void setKey_value(String key_value) {
        this.key_value = key_value;
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

    public static void  Insert_Data_Image(String name, String url){

        FirebaseDatabase database ;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("DataImage");
        DataImage D= new DataImage(name,url);

            myRef.child("Data").child("Coordonne").push().setValue(D);

    }
}
