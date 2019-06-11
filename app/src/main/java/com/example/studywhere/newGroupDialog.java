package com.example.studywhere;

import android.content.Context;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatDialogFragment;

public class newGroupDialog extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "newGroupDialog";
    private EditText mGroupName, mSchoolName, mSubject, mLocation, mDate, mGroupSize;
    private TextView mSubmit, mCancel;
    private GroupActivity mGroupActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL;
        int theme = android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen;
        setStyle(style, theme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_group, container, false);
        mGroupName = view.findViewById(R.id.group_name);
        mSchoolName = view.findViewById(R.id.school_name);
        mSubject = view.findViewById(R.id.subject_name);
        mLocation = view.findViewById(R.id.location);
        mDate = view.findViewById(R.id.date);
        mGroupSize = view.findViewById(R.id.group_size);
        mSubmit = view.findViewById(R.id.submit);
        mCancel = view.findViewById(R.id.cancel);
        mSubmit.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        getDialog().setTitle("Add your study group.");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mGroupActivity = (GroupActivity) getActivity(); //instantiates the GroupActivity interface
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:{
                String group_name = mGroupName.getText().toString();
                String school_name = mSchoolName.getText().toString();
                String subject = mSubject.getText().toString();
                String location = mLocation.getText().toString();
                String date = mDate.getText().toString();
                String group_size = mGroupSize.getText().toString();
                if ((!group_name.equals("")) && (!school_name.equals("")) && (!subject.equals("")) && (!location.equals("")) && (!date.equals(""))){
                    mGroupActivity.addNewGroup(group_name, school_name, subject, location, date, group_size);
                    getDialog().dismiss();
                }
                else{
                    Toast.makeText(getActivity(), "Required info is incomplete", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.cancel:{
                getDialog().dismiss();
                break;
            }
        }

    }
}
