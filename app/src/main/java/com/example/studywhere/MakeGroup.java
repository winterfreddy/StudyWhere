package com.example.studywhere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;

public class MakeGroup extends AppCompatActivity implements
        View.OnClickListener
{
    private static final String TAG = "MakeGroup";

    //widgets
    private EditText mGroupName, mSchoolName, mSubject, mGroupSize;
    private Button submitButton;

    //vars
    private GroupActivity mGroupActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group);
    }

    /*@Override
    public void createNewGroup(String group_name, String school_name, String subject, int group_size) {

    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submitButton:{
                String group_name = mGroupName.getText().toString();
                String school_name = mSchoolName.getText().toString();
                String subject = mSubject.getText().toString();
                String value = mGroupSize.getText().toString();
                int group_size = Integer.parseInt(value);

                break;
            }
        }

    }


}

