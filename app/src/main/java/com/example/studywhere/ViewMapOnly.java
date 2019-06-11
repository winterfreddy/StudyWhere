package com.example.studywhere;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.studywhere.Constants.ERROR_DIALOG_REQUEST;
import static com.example.studywhere.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.studywhere.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class ViewMapOnly extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseFirestore mDb;
    private Location mLocation;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    private static final String TAG = "ViewMapOnly";
    private static final int DEFAULT_ZOOM = 17;
    private static final int UPDATE_LOCATION_TIME = 5000; //5 seconds


    //default location if anything else fails
    private final LatLng mDefaultLocation = new LatLng(34.41223825619469, -119.84494138509037);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map_only);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button mviewgroups = findViewById(R.id.viewstudygroupsbutton);
        mviewgroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("studywhere.ViewGroups");
                startActivity(intent);
            }
        });

        final Button mhelpbutton = findViewById(R.id.helpbutton);
        mhelpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("studywhere.HelpScreen");
                startActivity(intent);
            }
        });

        mDb = FirebaseFirestore.getInstance();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation: called");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    Log.d(TAG, "onComplete: latitude: " + geoPoint.getLatitude() + " longitude: " + geoPoint.getLongitude());
                }
            }
        });
    }

    private boolean checkMapServices() {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS is required for StudyWhere, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            getLastKnownLocation();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK() {

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ViewMapOnly.this);

        if (available == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(ViewMapOnly.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "Map requests are not accepted", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private void getCurrentDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Can't acquire current location. Showing default UCSB location");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {
                    getLastKnownLocation();
                }
                else {
                    getLocationPermission();
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStudyLocations();
        if (checkMapServices()) {
            if (mLocationPermissionGranted) {
                getLastKnownLocation();
            }
            else {
                getLocationPermission();
            }
        }
    }

    private void updateStudyLocations(){
        Log.d(TAG, "updateStudyLocations running.");
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                getStudyLocations();
                mHandler.postDelayed(mRunnable, UPDATE_LOCATION_TIME);
            }
        }, UPDATE_LOCATION_TIME);
    }

    @Override
    protected void onPause(){
        super.onPause();
        stopUpdatingStudyLocations();
    }

    private void stopUpdatingStudyLocations(){
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        CollectionReference mCReference = mDb.collection("study locations");
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(this);

        getCurrentDeviceLocation();

        mCReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());

                        GeoPoint geoPoint = documentSnapshot.getGeoPoint("geo_point");
                        Double lat_double = geoPoint.getLatitude();
                        Double long_double = geoPoint.getLongitude();
                        String lat_str = lat_double.toString();
                        String long_str = long_double.toString();
                        Log.d(TAG, "lat_str is " + lat_str);
                        Log.d(TAG, "long_str is " + long_str);
                        LatLng studyLocation = new LatLng(lat_double, long_double);

                        String noiselevel = documentSnapshot.get("noiseness").toString();
                        String crowdlevel = documentSnapshot.get("crowdedness").toString();
                        Log.d(TAG, "noise level is " + noiselevel);
                        Log.d(TAG, "crowd level is " + crowdlevel);

                        mMap.addMarker(new MarkerOptions()
                                .position(studyLocation)
                                .title(documentSnapshot.getId())
                                .snippet("Noise Level: " + noiselevel + "\n" + "Crowd Level: " + crowdlevel)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        );
                        //Allows the snippet to become multi-lined
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                Context mContext = getApplicationContext();
                                LinearLayout information = new LinearLayout(mContext);
                                information.setOrientation(LinearLayout.VERTICAL);

                                TextView infoTitle = new TextView(mContext);
                                infoTitle.setTextColor(Color.BLACK);
                                infoTitle.setGravity(Gravity.CENTER);
                                infoTitle.setTypeface(null, Typeface.BOLD);
                                infoTitle.setText(marker.getTitle());

                                TextView infoSnippet = new TextView(mContext);
                                infoSnippet.setTextColor(Color.GRAY);
                                infoSnippet.setText(marker.getSnippet());

                                information.addView(infoTitle);
                                information.addView(infoSnippet);

                                return information;
                            }
                        });
                    }
                }
                else {
                    Log.d(TAG, "Error getting documents from document snapshot in onMapReady: ", task.getException());
                }
            }
        });

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        Double longitude = latLng.longitude;
        Double latitude = latLng.latitude;
        String strlongitude = longitude.toString();
        String strlatitude = latitude.toString();

        Intent intent = new Intent("studywhere.StudyPlaceInfo");
        Bundle coordinates = new Bundle();
        coordinates.putString("LONGITUDE", strlongitude);
        coordinates.putString("LATITUDE", strlatitude);
        intent.putExtras(coordinates);
        startActivity(intent);

    }

    private void getStudyLocations(){
        Log.d(TAG, "getStudyLocations: getting latest study locations from firebase");

        try{
            CollectionReference mCReference = mDb.collection("study locations");
            mCReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());

                            GeoPoint geoPoint = documentSnapshot.getGeoPoint("geo_point");
                            Double lat_double = geoPoint.getLatitude();
                            Double long_double = geoPoint.getLongitude();
                            String lat_str = lat_double.toString();
                            String long_str = long_double.toString();
                            Log.d(TAG, "lat_str is " + lat_str);
                            Log.d(TAG, "long_str is " + long_str);
                            LatLng studyLocation = new LatLng(lat_double, long_double);

                            String noiselevel = documentSnapshot.get("noiseness").toString();
                            String crowdlevel = documentSnapshot.get("crowdedness").toString();
                            Log.d(TAG, "noise level is " + noiselevel);
                            Log.d(TAG, "crowd level is " + crowdlevel);

                            mMap.addMarker(new MarkerOptions()
                                    .position(studyLocation)
                                    .title(documentSnapshot.getId())
                                    .snippet("Noise Level: " + noiselevel + "\n" + "Crowd Level: " + crowdlevel)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            );
                            //Allows the snippet to become multi-lined
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                @Override
                                public View getInfoWindow(Marker marker) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {
                                    Context mContext = getApplicationContext();
                                    LinearLayout information = new LinearLayout(mContext);
                                    information.setOrientation(LinearLayout.VERTICAL);

                                    TextView infoTitle = new TextView(mContext);
                                    infoTitle.setTextColor(Color.BLACK);
                                    infoTitle.setGravity(Gravity.CENTER);
                                    infoTitle.setTypeface(null, Typeface.BOLD);
                                    infoTitle.setText(marker.getTitle());

                                    TextView infoSnippet = new TextView(mContext);
                                    infoSnippet.setTextColor(Color.GRAY);
                                    infoSnippet.setText(marker.getSnippet());

                                    information.addView(infoTitle);
                                    information.addView(infoSnippet);

                                    return information;
                                }
                            });
                        }
                    }
                    else {
                        Log.d(TAG, "Error getting documents from document snapshot in getStudyLocations: ", task.getException());
                    }
                }
            });
        }
        catch (IllegalStateException e){
            Log.e(TAG, "getStudyLocations Error: " + e.getMessage() );
        }
    }
}
