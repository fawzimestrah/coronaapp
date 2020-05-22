package com.example.appultreasures_client.maps;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.appultreasures_client.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Windo_popUp extends Activity {

    Bundle extra;

boolean CheckHosp,CheckHome;
    EditText Ename,Edescription;
    Spinner Spinner_City;
    Button   Bload, Bsubmit;
    int i_city=0;
    private String[] Array_city;
    ArrayAdapter mArrayAdapter_city;

    ArrayList<String>ListCity;
    private ImageView Img;
    StorageReference mStorageRef ;
    StorageTask UploadTask;
    private Uri imgUri;
    String URI_Saved; // mn3abiha bl fileUpload wmnsta3mla bl button submit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_info_point);



        mStorageRef= FirebaseStorage.getInstance().getReferenceFromUrl("gs://p-ul4-zahle-app1.appspot.com");


        DisplayMetrics Dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Dm);

        int width = Dm.widthPixels;
        int height = Dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));


        extra =getIntent().getExtras();
         if(extra!=null) {
              Ename= findViewById(R.id.name);
             Edescription=findViewById(R.id.Description);
             Spinner_City=findViewById(R.id.city);
            Bload=findViewById(R.id.idButtonload);
             Img=findViewById(R.id.ShowImageView);
           Bsubmit=findViewById(R.id.idButtonSubmit);


             // spinner
             // select city from database
             FirebaseDatabase database ;
             DatabaseReference myRef ;
             ListCity=new ArrayList<String>();

             Array_city=new String[13];
             Array_city[0]="";
             Array_city[1]="";
             Array_city[2]="";
             Array_city[3]="";
             Array_city[4]="";
             Array_city[5]="";
             Array_city[6]="";
             Array_city[7]="";
             Array_city[8]="";
             Array_city[9]="";
             Array_city[10]="";
             Array_city[11]="";
             Array_city[12]="";

             database = FirebaseDatabase.getInstance();
             myRef = database.getReference("region");
             myRef.addValueEventListener(new ValueEventListener() {


                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                     ListCity.clear();

                     for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                         //snapshot.getKey();
                         Data_Region D = snapshot.getValue(Data_Region.class);

                         ListCity.add(D.getNom());
                          i_city++;
//                    Toast.makeText(getApplicationContext(),"city="+ListCity.get(i_city-1),Toast.LENGTH_LONG).show();

                     }

                     for (int k=0;k<i_city;k++) {
                         Array_city[k] = ListCity.get(k);
                     }

                     BaseAdapter adapter = (BaseAdapter) Spinner_City.getAdapter();
                     adapter.notifyDataSetChanged();

                     // spinner 3ala awal index
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });


mArrayAdapter_city = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,Array_city);
             mArrayAdapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
             if(Spinner_City!=null) {
                 Spinner_City.setAdapter(mArrayAdapter_city);

             }


             Img.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
      //               Img.setBackgroundResource(R.drawable.digitallovesaktid);
                 }
             });


             Bload.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     Fileshooser();

                 }
             });




            Bsubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(UploadTask !=null && UploadTask.isInProgress()){ // eza kabasa kaza mara abl ma ttl3 toast Success..
                        Toast.makeText(getApplicationContext(),"Upload in progress ",Toast.LENGTH_LONG).show();
                    }else {

                     FileUploader();

                     }

                }
            });






        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onPause() {

        super.onPause();


    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }





    private void FileUploader(){

        final StorageReference Ref =mStorageRef.child("immg").child(System.currentTimeMillis()+"."+getExtension(imgUri));

        UploadTask=  Ref.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(com.google.firebase.storage.UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //  Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        //                            taskSnapshot.getUploadSessionUri().toString()
                        Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(getApplicationContext(),"Image Uploaded succesFully"+UploadTask.toString(),Toast.LENGTH_LONG).show();
                             //   DataImage.Insert_Data_Image("EditText",uri.toString());


                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                URI_Saved=uri.toString();

                                DataSubmit Dpop_up=new DataSubmit();
                                Dpop_up.setLatitude(extra.getDouble("Cliked_Latitude"));
                                Dpop_up.setLongitude(extra.getDouble("Cliked_Longitude"));
                                Dpop_up.setFull_Name(Ename.getText().toString());
                                Dpop_up.setImageURL(URI_Saved);
                                Dpop_up.setImageName(Edescription.getText().toString());
                                Dpop_up.setCity(Spinner_City.getSelectedItem().toString());

                                DataSubmit.Inser_Point(Dpop_up.getLatitude(),Dpop_up.getLongitude(),Dpop_up.getFull_Name(),Dpop_up.getImageURL(),Dpop_up.getImageName(),Dpop_up.getCity(),  user.getEmail());


                                Intent intent = new Intent(getApplicationContext(),Maps_test.class);
                                intent.putExtra("Submit_bool",true);
                                intent.putExtra("Cliked_Latitude",Dpop_up.getLatitude());
                                intent.putExtra("Cliked_Longitude",Dpop_up.getLongitude());

                                setResult(1,intent);
                                finish();

                            }
                        });


                        //UploadTask.toString()
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(getApplicationContext(),"Failed Exeption: "+exception.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });


    }


    private String getExtension(Uri uri ){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap =MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));


    }




    private  void Fileshooser(){
       /* Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
*/

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Windo_popUp.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {

                    if (checkPermissionCamera()) {
                        if( isStoragePermissionGranted()) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);
                        }else{
                            requestPermissionStrorage();
                        }

                    } else {
                        requestPermissionCamera();

                    }



                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);

                    // nafsa mwjoude bl onPermession bas ya3te lprermission
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    boolean  checkPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermissionCamera() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                77);

    }
    private void  requestPermissionStrorage(){

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                78);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 77: //camera
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissionCamera();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
            case 78: // storage
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.v("onRequest","Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Windo_popUp.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission","Permission is granted");
                return true;
            } else {

                Log.v("permission","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission","Permission is granted");
            return true;
        }
    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null ){
            imgUri=data.getData();
            Img.setImageURI(imgUri);


        }else if (requestCode==0 ){
            Toast.makeText(getApplicationContext(),"done IMage onActivityResulty Code=0 " ,Toast.LENGTH_LONG).show();
/*

&& resultCode==RESULT_OK  && data!=null && data.getData()!=null
            imgUri=data.getData();
            Img.setImageURI(imgUri);
          Img.setImageURI(imgUri);
            Toast.makeText(getApplicationContext(),"done IMage onActivityResulty Code=0 "+imgUri ,Toast.LENGTH_LONG).show();
// 3ala2et krmel lstorage /// badak txid permission lstorage!!!!


 */
            if (resultCode == RESULT_OK) {
                Bitmap imageBM = (Bitmap) data.getExtras().get("data");
                Img.setImageBitmap(imageBM);
                // hala2 bdna nle2e method tsayev l image bitmap krmel na3mla uplad mn trajet m3ayan
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/saved_imagesLoaction");
                myDir.mkdirs();

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fname = "UL4"+ timeStamp +".jpg";

                File file = new File(myDir, fname);
                if (file.exists()) file.delete ();
                try {

                    FileOutputStream out = new FileOutputStream(file);
                    imageBM.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Finaly bade jib l url lal shi li sayato
                imgUri=(Uri.fromFile(file));

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "CANCELED ", Toast.LENGTH_LONG).show();
            }
        }



    }






}