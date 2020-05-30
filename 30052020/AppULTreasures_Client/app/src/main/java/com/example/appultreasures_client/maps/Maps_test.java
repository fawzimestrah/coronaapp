package com.example.appultreasures_client.maps;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.appultreasures_client.R;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Maps_test extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener{

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;
    ArrayList<LatLng> L_Marker_Confimed;
    ArrayList<DataSubmit> L_Data_Confimed;
// point blue
    ArrayList<LatLng> L_Marker_NOConfimed;
    ArrayList<DataSubmit> L_Data_NOConfimed;
    private String[] Array_city;

    FirebaseUser user;
    int i=0;

    public void centreMapOnLocation(Location location, String title){

Log.e("Debut","centreMapOnLocation");
      //  DataSubmit.getnb_of_Data_Region(getApplicationContext());
        L_Marker_Confimed=new ArrayList<LatLng>();
        L_Data_Confimed=new ArrayList<DataSubmit>();

        L_Marker_NOConfimed=new ArrayList<LatLng>();
        L_Data_NOConfimed=new ArrayList<DataSubmit>();

        //  LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

        mMap.clear();

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

                L_Marker_Confimed.clear();
                L_Data_Confimed.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    //snapshot.getKey();
                    DataSubmit D=snapshot.getValue(DataSubmit.class);
                    D.setKey_value(snapshot.getKey());
                    //Toast.makeText(getApplicationContext(),"key= "+D.key_value_DataSubmit() ,Toast.LENGTH_LONG).show();

                    //   LatLng My_location2 = new LatLng(D.getLatitude(), D.getLongitude());
                    LatLng My_location_Database = new LatLng(D.getLatitude(), D.getLongitude());


                    // add into L_mark_confirmed
                    L_Marker_Confimed.add(My_location_Database);
                    L_Data_Confimed.add(D);

                }

                Add_ALLMarker_Maps();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



// Data li houwe deja mda5ela abel
        myRef.child("Mobile").child("Coordonne").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                L_Data_NOConfimed.clear();
                for (DataSnapshot  snapshot:dataSnapshot.getChildren()){

                    DataSubmit D=snapshot.getValue(DataSubmit.class);
                    D.setKey_value(snapshot.getKey());

                    LatLng My_location_Database = new LatLng(D.getLatitude(), D.getLongitude());

                    if( D.getUser().equals(user.getEmail())) {
                        // add into L_mark_confirmed
                        L_Marker_NOConfimed.add(My_location_Database);
                        L_Data_NOConfimed.add(D);
                    }
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

        Log.e("Debut","onRequestPermissionsResult");
        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centreMapOnLocation(lastKnownLocation,"Your Location");

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Log.e("Debut","onCREATE");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        user= FirebaseAuth.getInstance().getCurrentUser();





        }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.e("Debut","onMapReady");
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                //Toast.makeText(getApplicationContext(),"Marktitle :"+marker.getTitle(),Toast.LENGTH_LONG).show();

                return false;
            }
        });





// popup data



        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

// popUp

                if(L_Marker_Confimed.contains(marker.getPosition())){
                    //Toast.makeText(getApplicationContext(),"Confirmee",Toast.LENGTH_LONG).show();
                        //L_Data_Confimed

                    int INDEX= L_Marker_Confimed.indexOf(marker.getPosition());
                    //Toast.makeText(getApplicationContext(),"onselect index="+INDEX+"  position="+marker.getPosition(),Toast.LENGTH_LONG).show();

                    DataSubmit D_check=L_Data_Confimed.get(INDEX);
                //    Toast.makeText(getApplicationContext(),"index="+INDEX+"   CLICK SUR  fullname="+D_check.getFull_Name(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Windo_popUp_Admin_check.class);

                    intent.putExtra("Cliked_Latitude",marker.getPosition().latitude);
                    intent.putExtra("Cliked_Longitude",marker.getPosition().longitude);
                    intent.putExtra("Name",D_check.getFull_Name());
                    intent.putExtra("Description",D_check.getImageName());
                    intent.putExtra("URLimage",D_check.getImageURL());
                    intent.putExtra("City",D_check.getCity());

                    startActivityForResult(intent,2);
//                finish();


                }else if(L_Marker_NOConfimed.contains(marker.getPosition())){
                    int INDEX= L_Marker_NOConfimed.indexOf(marker.getPosition());

                    DataSubmit D_check=L_Data_NOConfimed.get(INDEX);
                    Intent intent = new Intent(getApplicationContext(), Windo_popup_infoEdit.class);
                    intent.putExtra("Cliked_Latitude",marker.getPosition().latitude);
                    intent.putExtra("Cliked_Longitude",marker.getPosition().longitude);
                    intent.putExtra("Name",D_check.getFull_Name());
                    intent.putExtra("Description",D_check.getImageName());
                    intent.putExtra("URLimage",D_check.getImageURL());
                    intent.putExtra("City",D_check.getCity());
                    intent.putExtra("User",D_check.getUser());
                    intent.putExtra("Key",D_check.key_value_DataSubmit());

                    startActivityForResult(intent,2);
                }



                return null;
            }
        });











       mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
           @Override
           public void onMapLongClick(LatLng latLng) {
               Intent intent = new Intent(getApplicationContext(),Windo_popUp.class);
               intent.putExtra("Cliked_Latitude",latLng.latitude);
               intent.putExtra("Cliked_Longitude",latLng.longitude);

               startActivityForResult(intent,1);
//                finish();



           }
       });


        Intent intent = getIntent();
        if (intent.getIntExtra("Place Number",0) == 0 ){

            // Zoom into users location
            Log.e("Lmanager","before locationManager L 296");

            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            Log.e("Lmanager","LocationManager "+locationListener);

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
                Log.e("testLastL","LastKnwonLocation L459 "+lastKnownLocation);

            } else {
                Log.e("Debut","ELSE OnmapReady");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }


    }


    @Override
    public void onPolylineClick(Polyline polyline) {


    }



    // rah ta3ml close bs ntl3 mna la eno fi onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if(requestCode==1 && data!=null){


        Bundle extra =data.getExtras();
        if(extra!=null) {

            if(extra.getBoolean("Submit_bool")) {
                Double Lal = extra.getDouble("Cliked_Latitude");
                Double Long = extra.getDouble("Cliked_Longitude");

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
                    .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        }



        LatLng My_location_cuurent = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(My_location_cuurent).title("Marker in your  new database location FM"));


    }

}
