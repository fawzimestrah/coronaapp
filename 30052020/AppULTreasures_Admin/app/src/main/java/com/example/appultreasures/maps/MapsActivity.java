package com.example.appultreasures.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.appultreasures.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.appultreasures.maps.DataSubmit_Admin;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;
    ArrayList<LatLng> L_Marker_Confimed;
    ArrayList<DataSubmit_Admin> L_Data_Confimed;
    // point blue
    ArrayList<LatLng> L_Marker_NOConfimed;
    ArrayList<DataSubmit_Admin> L_Data_NOConfimed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }




    public void centreMapOnLocation(Location location, String title){


        L_Marker_Confimed=new ArrayList<LatLng>();
        L_Data_Confimed=new ArrayList<DataSubmit_Admin>();

        L_Marker_NOConfimed=new ArrayList<LatLng>();
        L_Data_NOConfimed=new ArrayList<DataSubmit_Admin>();

        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

        mMap.addMarker(new MarkerOptions().position(userLocation).title(title));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,18));


        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {

            @Override
            public void onCircleClick(Circle circle) {
                // Flip the r, g and b components of the circle's
                // stroke color.
                int strokeColor = circle.getStrokeColor() ^ 0x00ffffff;
                circle.setStrokeColor(strokeColor);
            }
        });



        FirebaseDatabase database ;
        DatabaseReference myRef ;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message1");
        myRef.child("Mobile").child("Cas_confirmes").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                // meshkle eza 7ateina l clear btsir we7de tm7e ltenye

            //    mMap.clear();//clear map to add new marker's
                L_Marker_Confimed.clear();
                L_Data_Confimed.clear();

                for (DataSnapshot  snapshot:dataSnapshot.getChildren()){
                    //snapshot.getKey();
                    DataSubmit_Admin DConf=snapshot.getValue(DataSubmit_Admin.class);
                    DConf.setKey_value(snapshot.getKey());
  //                  Toast.makeText(getApplicationContext(),"key= "+DConf.key_value_DataSubmit() ,Toast.LENGTH_LONG).show();

                    LatLng My_location_Database = new LatLng(DConf.getLatitude(), DConf.getLongitude());


                    // add into L_mark_confirmed
                    L_Marker_Confimed.add(My_location_Database);
                    L_Data_Confimed.add(DConf);

                }
                Add_ALLMarker_Maps();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





// Lal ADMIN seulement
        myRef.child("Mobile").child("Coordonne").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               L_Marker_Confimed.clear();
                L_Data_NOConfimed.clear();
                for (DataSnapshot  snapshot:dataSnapshot.getChildren()){

                    DataSubmit_Admin D=snapshot.getValue(DataSubmit_Admin.class);
                    D.setKey_value(snapshot.getKey());
    //                Toast.makeText(getApplicationContext(),"keyNoConf= "+D.key_value_DataSubmit() ,Toast.LENGTH_LONG).show();

                    LatLng My_location_Database = new LatLng(D.getLatitude(), D.getLongitude());


              // add into L_mark_confirmed
                    L_Marker_NOConfimed.add(My_location_Database);
                    L_Data_NOConfimed.add(D);

                }
                Add_ALLMarker_Maps();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });












    }









    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centreMapOnLocation(lastKnownLocation,"Your Location");

            }
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */






















    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));




        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                Intent intent = new Intent(getApplicationContext(),Windo_popUp_ADDpoint.class);
                intent.putExtra("Cliked_Latitude",latLng.latitude);
                intent.putExtra("Cliked_Longitude",latLng.longitude);

                startActivityForResult(intent,1);

            }
        });


// hon lezm n2sema 2 ya user 3ade fa bttl3 popup data
        //ya admin fa bttl3 lpopup li hala2 ana 3emela



        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

// popUp

                if(L_Marker_Confimed.contains(marker.getPosition())){

                    int INDEX= L_Marker_Confimed.indexOf(marker.getPosition());
                    DataSubmit_Admin D_check=L_Data_Confimed.get(INDEX);
                    Intent intent = new Intent(getApplicationContext(), Windo_popup_infoEdit.class);

                    intent.putExtra("Cliked_Latitude",marker.getPosition().latitude);
                    intent.putExtra("Cliked_Longitude",marker.getPosition().longitude);
                    intent.putExtra("Key",D_check.key_value_DataSubmit());
                    intent.putExtra("Name",D_check.getFull_Name());
                    intent.putExtra("Description",D_check.getImageName());
                    intent.putExtra("URLimage",D_check.getImageURL());
                    intent.putExtra("City",D_check.getCity());
                    intent.putExtra("User",D_check.getUser());


                    startActivityForResult(intent,3);


                }else if(L_Marker_NOConfimed.contains(marker.getPosition())){

                    //Toast.makeText(getApplicationContext(),"FAWZI"+ marker.getId()+"\n marker= "+L_Marker_Confimed.contains(marker.getPosition()),Toast.LENGTH_LONG).show();
                    int INDEX= L_Marker_NOConfimed.indexOf(marker.getPosition());
                    DataSubmit_Admin D_check=L_Data_NOConfimed.get(INDEX);
                    Intent intent = new Intent(getApplicationContext(), Windo_popUp_Admin_check.class);

                    intent.putExtra("Cliked_Latitude",marker.getPosition().latitude);
                    intent.putExtra("Cliked_Longitude",marker.getPosition().longitude);
                    intent.putExtra("Name",D_check.getFull_Name());

                    intent.putExtra("Key",D_check.key_value_DataSubmit());
                    intent.putExtra("Description",D_check.getImageName());
                    intent.putExtra("URLimage",D_check.getImageURL());
                    intent.putExtra("City",D_check.getCity());
                    intent.putExtra("User",D_check.getUser());

                    startActivityForResult(intent,2);
//                finish();

                }



                return null;
            }
        });














        Intent intent = getIntent();
        if (intent.getIntExtra("Place Number",0) == 0 ){

            // Zoom into users location
            Log.e("Fawzi","before locationManager L 296");

            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            Log.e("Fawzi","LocationManager "+locationListener);

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //    centreMapOnLocation(location,"Your Location");

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                // bta3ml focus 3l location bl gps bs nhna ma bdna yeha ela hon awl mara
                centreMapOnLocation(lastKnownLocation,"Your Location Fm1");
                Log.e("Fawzi","LastKnwonLocation L330 "+lastKnownLocation);

            } else {

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }









    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2 && data!=null){ // result from Windo_popUp_Admin_check ( Confirme || Delete)
            Bundle extra =data.getExtras();
            if(extra!=null) {

                if(extra.getBoolean("Submit_bool")) {

                }
            }



        }else
        if(requestCode==3 && data!=null){ // result from Windo_popup_edit (Edit || Delete)


            Bundle extra =data.getExtras();
            if(extra!=null) {

                if(extra.getBoolean("Submit_bool")) {

                }
            }



        }
    }


    private void Add_ALLMarker_Maps(){
        // nzid lbe2e
        mMap.clear();
        for(int y=0;y<L_Data_Confimed.size();y++) {

            LatLng My_location_Database = new LatLng(L_Data_Confimed.get(y).getLatitude(), L_Data_Confimed.get(y).getLongitude());

            mMap.addMarker(new MarkerOptions().position(My_location_Database).title("NoConfirmed")).setIcon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

        }

        for(int y=0;y<L_Data_NOConfimed.size();y++) {

            LatLng My_location_Database = new LatLng(L_Data_NOConfimed.get(y).getLatitude(), L_Data_NOConfimed.get(y).getLongitude());


            mMap.addMarker(new MarkerOptions().position(My_location_Database).title("Marker in your  new database location FM")).setIcon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }



        LatLng My_location_cuurent = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(My_location_cuurent).title("Marker in your  new database location FM"));


    }
}
