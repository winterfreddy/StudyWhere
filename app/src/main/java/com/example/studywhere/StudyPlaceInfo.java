package com.example.studywhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StudyPlaceInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_place_info);

        Intent intent = getIntent();
        Bundle coordinates = intent.getExtras();
        String latitude = coordinates.getString("LATITUDE");
        String longitude = coordinates.getString("LONGITUDE");

        TextView lattv = findViewById(R.id.latTextRes);
        lattv.setText(latitude);

        TextView longtv = findViewById(R.id.longTextRes);
        longtv.setText(longitude);
    }

    //TODO
    //set cancel button to go back to previous screen

    //TODO
    //Save location name to firestore
    //Save coordinates to firestore as geopoint
    //Save crowdedness to firestore
    //Save noiseness to firestore

    //set save button to save all information and send to firestore
}
