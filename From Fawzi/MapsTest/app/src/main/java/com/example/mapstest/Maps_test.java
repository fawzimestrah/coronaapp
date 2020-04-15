package com.example.mapstest;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Maps_test extends FragmentActivity implements OnMapReadyCallback ,GoogleMap.OnPolylineClickListener{

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;
    ArrayList<LatLng> L_Marker_Confimed;
    ArrayList<DataSubmit> L_Data_Confimed;
// point blue
    ArrayList<LatLng> L_Marker_NOConfimed;
    ArrayList<DataSubmit> L_Data_NOConfimed;



    public void centreMapOnLocation(Location location, String title){


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

                mMap.clear();//clear map to add new marker's
                L_Marker_Confimed.clear();
                for (DataSnapshot  snapshot:dataSnapshot.getChildren()){
                    //snapshot.getKey();
                    DataSubmit D=snapshot.getValue(DataSubmit.class);
                    D.setKey_value(snapshot.getKey());
                    Toast.makeText(getApplicationContext(),"key= "+D.key_value_DataSubmit() ,Toast.LENGTH_LONG).show();

                    //   LatLng My_location2 = new LatLng(D.getLatitude(), D.getLongitude());
                    LatLng My_location_Database = new LatLng(D.getLatitude(), D.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(My_location_Database).title("Marker in your  new database location FM")).setIcon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                   //create a cercle
                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(My_location_Database)
                            .radius(10)
                            .strokeWidth(100)
                            .strokeColor(Color.RED)
                            .fillColor(Color.argb(128, 255, 0, 0))
                            .clickable(true));

                    /*Polygon polygon = mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng(30, 35), new LatLng(31, 30), new LatLng(33, 35))
                            .strokeColor(Color.RED)
                            .fillColor(Color.BLUE));
*/

                    // add into L_mark_confirmed
                    L_Marker_Confimed.add(My_location_Database);
                    L_Data_Confimed.add(D);
              //      Toast.makeText(getApplicationContext(),"3m tfout"+D.getLongitude(),Toast.LENGTH_LONG).show();

                }


                LatLng My_location_cuurent = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(My_location_cuurent).title("Marker in your  new database location FM"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




/*
// Lal ADMIN seulement
        myRef.child("Mobile").child("Coordonne").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // mMap.clear();//clear map to add new marker's
                L_Marker_Confimed.clear();
                for (DataSnapshot  snapshot:dataSnapshot.getChildren()){
                    DataSubmit D=snapshot.getValue(DataSubmit.class);
                    //   LatLng My_location2 = new LatLng(D.getLatitude(), D.getLongitude());
                    LatLng My_location_Database = new LatLng(D.getLatitude(), D.getLongitude());

                       mMap.addMarker(new MarkerOptions().position(My_location_Database).title("Marker in your  new database location FM")).setIcon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    // add into L_mark_confirmed
                    L_Marker_NOConfimed.add(My_location_Database);
                    L_Data_NOConfimed.add(D);
                    //      Toast.makeText(getApplicationContext(),"3m tfout"+D.getLongitude(),Toast.LENGTH_LONG).show();

                }


                LatLng My_location_cuurent = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(My_location_cuurent).title("Marker in your  new database location FM"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

*/










// pour les region



        DatabaseReference myRef_region ;
        myRef_region = database.getReference("region");
        myRef_region.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                Data_Region[] D= new Data_Region[8];
                for (DataSnapshot  snapshot:dataSnapshot.getChildren()) {

                    D[i] = snapshot.getValue(Data_Region.class);

                    LatLng My_location_Database = new LatLng(D[i].getLatitude(), D[i].getLongitude());

                   // Toast.makeText(getApplicationContext(),"yellow "+D[i].getLongitude(),Toast.LENGTH_LONG).show();

                    mMap.addMarker(new MarkerOptions().position(My_location_Database).title(D[i].getNom())).setIcon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

                    i++;

                }


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);






        }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Toast.makeText(getApplicationContext(),"Marktitle :"+marker.getTitle(),Toast.LENGTH_LONG).show();
                return false;
            }
        });




/*
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
                    Toast.makeText(getApplicationContext(),"Confirmee",Toast.LENGTH_LONG).show();
                        //L_Data_Confimed
                }else if(L_Marker_NOConfimed.contains(marker.getPosition())){

                    //Toast.makeText(getApplicationContext(),"FAWZI"+ marker.getId()+"\n marker= "+L_Marker_Confimed.contains(marker.getPosition()),Toast.LENGTH_LONG).show();
                    int INDEX= L_Marker_NOConfimed.indexOf(marker.getPosition());
                    DataSubmit D_check=L_Data_NOConfimed.get(INDEX);
                    Toast.makeText(getApplicationContext(),"index="+INDEX+"   CLICK SUR "+D_check.getFull_Name(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Windo_popUp_Admin_check.class);

                    intent.putExtra("Cliked_Latitude",marker.getPosition().latitude);
                    intent.putExtra("Cliked_Longitude",marker.getPosition().longitude);
                    intent.putExtra("Name",D_check.getFull_Name());
                    intent.putExtra("How_transferred",D_check.getHow_transferred());
                    intent.putExtra("Nb_family",D_check.getNb_family());
                    intent.putExtra("isIn_home",D_check.isIn_home());
                    intent.putExtra("isTransferred_hospital",D_check.isTransferred_hospital());

                    startActivityForResult(intent,2);
//                finish();

                }



                return null;
            }
        });


*/









/*
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                LatLng My_location2 = new LatLng(latLng.latitude, latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(My_location2).title("Marker in your  new location FM"));

                Toast.makeText(getApplicationContext(),
                        "Lat : " + latLng.latitude + " , "
                                + "Long : " + latLng.longitude,
                        Toast.LENGTH_LONG).show();

            }
        });
        */


       mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
           @Override
           public void onMapLongClick(LatLng latLng) {

       //       View v = getLayoutInflater().inflate(R.layout.popup_info, null);

               //LatLng clickMarkerLatLng = marker.getPosition();

               //           TextView title = (TextView) v.findViewById(R.id.TText1);
               //         title.setText(marker.getTitle());

               Intent intent = new Intent(getApplicationContext(), Windo_popUp.class);
               intent.putExtra("Cliked_Latitude",latLng.latitude);
               intent.putExtra("Cliked_Longitude",latLng.longitude);

            /*   if(L_Marker_Confimed.contains(clickMarkerLatLng)){
                   intent.putExtra("Confirmed_No","il est confirmee");

               }else{
                   intent.putExtra("Confirmed_No","NO  confirmed");

               }
*/
               startActivityForResult(intent,1);
//                finish();



            //   DataSubmit.Inser_Point(latLng.latitude,latLng.longitude);
              /* LatLng My_location2 = new LatLng(latLng.latitude, latLng.longitude);
               mMap.addMarker(new MarkerOptions().position(My_location2).title("Marker in your  new location FM")).setIcon(BitmapDescriptorFactory
                       .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
*/
               Toast.makeText(getApplicationContext(),
                       "Lat : " + latLng.latitude + " , "
                               + "Long : " + latLng.longitude,
                       Toast.LENGTH_LONG).show();

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


                LatLng My_location2 = new LatLng(Lal, Long);
                mMap.addMarker(new MarkerOptions().position(My_location2).title("Marker in your  new location FM")).setIcon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
            }
        }



        }

    }



}
