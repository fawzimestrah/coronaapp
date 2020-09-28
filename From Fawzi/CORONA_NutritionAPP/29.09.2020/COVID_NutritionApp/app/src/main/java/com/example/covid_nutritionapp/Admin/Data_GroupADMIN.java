package com.example.covid_nutritionapp.Admin;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Data_GroupADMIN {
    private String key_Group =null;
    private String nameGroup=null;
    private String description =null;
    private String keyUser =null;
    public boolean checked=false;
    public ArrayList<String> listMembers =new ArrayList<String>();
    public Data_GroupADMIN() {
    }


    public Data_GroupADMIN(String key_Group, String key_user) {
        this.key_Group = key_Group;
        keyUser = key_user;
    }


    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    public  void addMembers(String user){
        this.listMembers.add(user);
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey_Group() {
        return key_Group;
    }

    public void setKey_Group(String key_Group) {
        this.key_Group = key_Group;
    }

    public String getKey_user() {
        return keyUser;
    }

    public void setKey_user(String key_user) {
        keyUser = key_user;
    }

    public boolean isChecked() {
        return checked;
    }


    public static void addGroupAdmin(Data_GroupADMIN Group){
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String formattedDate = df.format(c.getTime());



    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("GroupAdmin");
     myRef.push().setValue(Group);

    // String KeyForm=myRef.push().getKey(); // jbna lkey li 5l2neha
//    myRef.child(user.getUser_id()).setValue(user); // Keyform == iduser /\



}

public static  void insertMemberGroup(ArrayList<Data_GroupADMIN> listGroup, String IdUser){
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("GroupAdmin");

    for(int i=0;i< listGroup.size();i++){
        if(listGroup.get(i).isChecked()){
        myRef.child(listGroup.get(i).getKey_Group()).child("Members").push().setValue(IdUser);
        }
    }

}

public static ArrayList<Data_GroupADMIN> selectListGroup_user(final Context context, final String IdUser){
        final ArrayList<Data_GroupADMIN> List=new ArrayList<Data_GroupADMIN>();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("GroupAdmin");

    myRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                //snapshot.getKey();
                final Data_GroupADMIN D = snapshot.getValue(Data_GroupADMIN.class);
                D.setKey_Group(snapshot.getKey());
                DatabaseReference myRefUser = database.getReference("GroupAdmin").child(snapshot.getKey()).child("Members");
                myRefUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //snapshot.getKey();
                                String Dkey = snapshot.getValue(String.class);
                                if(Dkey.equals(IdUser)){
                                    List.add(D);

                                }
                            }
                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });




    return List;
}


}
