package com.example.studywhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SuggestNewPlaces extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_new_places);

        final Button addByMap = (Button) findViewById(R.id.addMap);
        addByMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent("studywhere.SuggestNewPlaces");
                startActivity(intent);
            }
        });

        final Button addByText = (Button) findViewById(R.id.addText);
        addByText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent("studywhere.StudyPlaceInfo");
                startActivity(intent);
            }
        });
    }
}
