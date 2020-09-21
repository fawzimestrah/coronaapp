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
    private String Class_Type;
    private String Data_Register;
    public Data_User() {
    }

    public Data_User(String user_id, String email, String class_Type) {
        this.user_id = user_id;
        this.email = email;
        Class_Type = class_Type;
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

    public String getClass_Type() {
        return Class_Type;
    }

    public void setClass_Type(String class_Type) {
        Class_Type = class_Type;
    }

    public String getData_Register() {
        return Data_Register;
    }

    public void setData_Register(String data_Register) {
        Data_Register = data_Register;
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
}
