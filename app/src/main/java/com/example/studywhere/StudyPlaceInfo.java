package com.example.studywhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class StudyPlaceInfo extends AppCompatActivity {

    //private DatabaseReference mDatabase;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_place_info);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        Bundle coordinates = intent.getExtras();
        final String latitude = coordinates.getString("LATITUDE");
        final String longitude = coordinates.getString("LONGITUDE");

        final TextView lattv = findViewById(R.id.latTextRes);
        lattv.setText(latitude);

        TextView longtv = findViewById(R.id.longTextRes);
        longtv.setText(longitude);

        final Button saveButton = findViewById(R.id.savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double latd = new Double(latitude);
                Double longd = new Double(longitude);
                saveLocation(latd, longd);
                finish();
            }
        });

        final Button cancelButton = findViewById(R.id.cancelbutton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void saveLocation(Double c1_lat, Double c2_long){

        GeoPoint geoPoint = new GeoPoint(c1_lat, c2_long);

        EditText nameLocation = findViewById(R.id.locationName);
        String mNameLocation = nameLocation.getText().toString();

        EditText rateCrowdedness = findViewById(R.id.CrowdinessRate);
        String mRateCrowdedness = rateCrowdedness.getText().toString();

        EditText rateNoise = findViewById(R.id.NoisenessRate);
        String mRateNoise = rateNoise.getText().toString();

        Location location = new Location(mRateCrowdedness, geoPoint, mRateNoise);

        db.collection("study locations").document(mNameLocation).set(location);


    }

}
