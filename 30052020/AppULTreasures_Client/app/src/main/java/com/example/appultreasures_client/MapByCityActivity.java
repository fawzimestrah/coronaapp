package com.example.appultreasures_client;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import com.example.appultreasures_client.maps.DataSubmit;
import com.example.appultreasures_client.maps.Data_Region;
import com.example.appultreasures_client.maps.Windo_popUp_Admin_check;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapByCityActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView bgapp, signoutimg;
    TextView signout;
    LinearLayout texthome, menus;
    Animation frombottom, alphaanim;

    Spinner Spinner_City;
    ArrayAdapter mArrayAdapter;
    FragmentActivity FMaps;
    int i = 0;
    GoogleMap mMap;
    boolean continue1 = false;
    ArrayList<String> ListCity;
    String[] Array_city;
    ArrayList<Data_Region> ListD_region;
    CircleOptions circle = null;
    Circle CurrentCircle = null;
    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;
    ArrayList<LatLng> L_Marker_Confimed;
    ArrayList<DataSubmit> L_Data_Confimed;
    // point blue
    ArrayList<LatLng> L_Marker_NOConfimed;
    ArrayList<DataSubmit> L_Data_NOConfimed;
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_spinner_city);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //get DP
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth = outMetrics.widthPixels / density;
        int translationIndex = (int) (-2200 + (dpHeight - 692) * (1.7) * (dpHeight / 692));
        //Log.e("testwh", "" + dpWidth + " h:" + dpHeight + "dpt:" + translationIndex);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.animfrombottom);
        alphaanim = AnimationUtils.loadAnimation(this, R.anim.alphaanim);
        bgapp = (ImageView) findViewById(R.id.bgapp);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);
        signout = (TextView)findViewById(R.id.signouttext);
        signoutimg=(ImageView)findViewById(R.id.signoutimg);

        bgapp.animate().translationY(translationIndex).setDuration(800).setStartDelay(300);
        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);
        signout.startAnimation(alphaanim);
        signoutimg.startAnimation(alphaanim);

        ListCity = new ArrayList<String>();
        ListD_region = new ArrayList<Data_Region>();

        L_Marker_Confimed = new ArrayList<LatLng>();
        L_Data_Confimed = new ArrayList<DataSubmit>();

        L_Marker_NOConfimed = new ArrayList<LatLng>();
        L_Data_NOConfimed = new ArrayList<DataSubmit>();

        Array_city = new String[13];
        Array_city[0] = "";
        Array_city[1] = "";
        Array_city[2] = "";
        Array_city[3] = "";
        Array_city[4] = "";
        Array_city[5] = "";
        Array_city[6] = "";
        Array_city[7] = "";
        Array_city[8] = "";
        Array_city[9] = "";
        Array_city[10] = "";
        Array_city[11] = "";
        Array_city[12] = "";

        Spinner_City = findViewById(R.id.Id_Spinner_city);

        // select city from database

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("region");

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MapByCityActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signoutimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MapByCityActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ListCity.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    Data_Region D = snapshot.getValue(Data_Region.class);

                    ListCity.add(D.getNom());
                    ListD_region.add(D);
                    i++;
                    //Toast.makeText(getApplicationContext(), "city=" + ListCity.get(i - 1), Toast.LENGTH_LONG).show();

                    LatLng My_location_Database = new LatLng(D.getLatitude(), D.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(My_location_Database).title(D.getNom())).setIcon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                }

                for (int k = 0; k < i; k++) {
                    Array_city[k] = ListCity.get(k);
                }

                BaseAdapter adapter = (BaseAdapter) Spinner_City.getAdapter();
                adapter.notifyDataSetChanged();

                // spinner 3ala awal index
                SelectCity(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mArrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, Array_city);
        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (Spinner_City != null) {
            Spinner_City.setAdapter(mArrayAdapter);
            //Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();

        }

        Spinner_City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ListCity.size() != 0) {

                    //Toast.makeText(getApplicationContext(), " select=" + ListCity.get(position), Toast.LENGTH_LONG).show();

                    SelectCity(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //Toast.makeText(getApplicationContext(), "click sur " + latLng.toString(), Toast.LENGTH_LONG).show();
            }
        });


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message1");
        myRef.child("Mobile").child("Cas_confirmes").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // meshkle eza 7ateina l clear btsir we7de tm7e ltenye
                //               mMap.clear();
                // ma b2aser bhl class la eno l update btsir hasab lspinner
                L_Marker_Confimed.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataSubmit D = snapshot.getValue(DataSubmit.class);
                    D.setKey_value(snapshot.getKey());
                    //Toast.makeText(getApplicationContext(), "key= " + D.key_value_DataSubmit(), Toast.LENGTH_LONG).show();
                    LatLng My_location_Database = new LatLng(D.getLatitude(), D.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(My_location_Database).title("Petite Description")).setIcon(BitmapDescriptorFactory
                            //.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                            .fromResource(R.mipmap.ul_launcher));
                    //create a cercle
                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(My_location_Database)
                            .radius(10)
                            .strokeWidth(100)
                            .strokeColor(Color.RED)
                            .fillColor(Color.argb(128, 255, 0, 0))
                            .clickable(true));

                    // add into L_mark_confirmed
                    L_Marker_Confimed.add(My_location_Database);
                    L_Data_Confimed.add(D);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

                if (L_Marker_Confimed.contains(marker.getPosition())) {
                    //Toast.makeText(getApplicationContext(), "Confirmee", Toast.LENGTH_LONG).show();
                    //L_Data_Confimed

                    //Toast.makeText(getApplicationContext(),"FAWZI"+ marker.getId()+"\n marker= "+L_Marker_Confimed.contains(marker.getPosition()),Toast.LENGTH_LONG).show();
                    int INDEX = L_Marker_Confimed.indexOf(marker.getPosition());
                    DataSubmit D_check = L_Data_Confimed.get(INDEX);
                    //Toast.makeText(getApplicationContext(), "index=" + INDEX + "   CLICK SUR  fullname=" + D_check.getFull_Name(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Windo_popUp_Admin_check.class);

                    intent.putExtra("Cliked_Latitude", marker.getPosition().latitude);
                    intent.putExtra("Cliked_Longitude", marker.getPosition().longitude);
                    intent.putExtra("Name", D_check.getFull_Name());
                    intent.putExtra("Description", D_check.getImageName());
                    intent.putExtra("URLimage", D_check.getImageURL());
                    intent.putExtra("City", D_check.getCity());

                    startActivityForResult(intent, 2);
//                finish();


                } else if (L_Marker_NOConfimed.contains(marker.getPosition())) {

                }


                return null;
            }
        });


    }


    public void centreMapOnLocation(Location location, String title) {
        //  mMap.clear();

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(userLocation).title(title));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
        mMap.setMinZoomPreference(14);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (circle != null) {
                    checkBounds(cameraPosition);
                }
            }
        });

    }

    public void checkBounds(CameraPosition cameraPosition) {

        LatLng actualCenter = cameraPosition.target;

    /*    LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(33.306, 35.366));
        builder.include(new LatLng(34.416, 36.022));
        builder.include(new LatLng(33.406, 35.366));
        builder.include(new LatLng(33.306, 36.022));

        LatLngBounds bounds = builder.build();
*/

        float[] distance = new float[2];
        Location.distanceBetween(actualCenter.latitude, actualCenter.longitude,
                circle.getCenter().latitude, circle.getCenter().longitude, distance);

        if (distance[0] > circle.getRadius()) {
            //Toast.makeText(getBaseContext(), "Outside", Toast.LENGTH_LONG).show();

        } else {
            //Toast.makeText(getBaseContext(), "Inside", Toast.LENGTH_LONG).show();
        }

        /*
        if (bounds.contains(actualCenter)) {
  //          Toast.makeText(getApplicationContext(),"camerachange= "+cameraPosition.target,Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_LONG).show();

        }*/


    }

    private void SelectCity(int position) {
        LatLng CenterC = new LatLng(ListD_region.get(Spinner_City.getSelectedItemPosition()).getLatitude(), ListD_region.get(Spinner_City.getSelectedItemPosition()).getLongitude());

        circle = drawCircle(CenterC);

        Location lastKnownLocation = new Location(ListD_region.get(position).getNom());
        lastKnownLocation.setLatitude(ListD_region.get(position).getLatitude());
        lastKnownLocation.setLongitude(ListD_region.get(position).getLongitude());

        centreMapOnLocation(lastKnownLocation, "Your Location Fm1");

    }

    private CircleOptions drawCircle(LatLng point) {
        // remove old Cercle
        if (CurrentCircle != null) {
            CurrentCircle.remove();
        }

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(point);
        circleOptions.radius(10000);
        circleOptions.strokeColor(Color.BLACK);
        circleOptions.strokeWidth(3);
        // add currentCercle
        CurrentCircle = mMap.addCircle(circleOptions);

        return circleOptions;
    }

}