package com.example.studywhere;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewGroups extends AppCompatActivity implements View.OnClickListener, GroupActivity{

    private static final String TAG = "ViewGroups";
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mParentLayout;
    private ArrayList<Group> mGroups = new ArrayList<>();
    private GroupAdapter mGroupAdapter;
    private DocumentSnapshot mLastQueriedDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_groups);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Enter the info", Snackbar.LENGTH_LONG)
                        .setAction("Something happened", null).show();
            }
        });
    }

    @Override
    public void onGroupSelected(Group group){
        //this is for editing
    }

    private void getGroups(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference groupsCollectionRef = db.collection("study groups");

        Query groupsQuery = null;
        if (mLastQueriedDoc != null){
            groupsQuery = groupsCollectionRef.orderBy("timestamp", Query.Direction.DESCENDING).startAfter(mLastQueriedDoc);
        }
        else{
            groupsQuery = groupsCollectionRef.orderBy("timestamp", Query.Direction.DESCENDING);
        }
        groupsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot document: task.getResult()){
                        Group group = document.toObject(Group.class);
                        mGroups.add(group);
                    }

                    if(task.getResult().size() != 0){
                        mLastQueriedDoc = task.getResult().getDocuments().get(task.getResult().size() -1);
                    }

                    mGroupAdapter.notifyDataSetChanged();
                }
                else{
                    showSnackBarMessage("Query failed");
                }
            }
        });
    }

    private void initRecyclerView(){
        if(mGroupAdapter == null){
            mGroupAdapter = new GroupAdapter(this, mGroups);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mGroupAdapter);
    }

    @Override
    public void createNewGroup(String group_name, String school_name, String subject, String location, String date, String group_size) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newGroupRef = db.collection("study groups").document();
        Group studyGroup = new Group();
        studyGroup.setGroup_name(group_name);
        studyGroup.setSchool_name(school_name);
        studyGroup.setSubject(subject);
        studyGroup.setLocation(location);
        studyGroup.setDate(date);
        studyGroup.setGroup_size(group_size);

        newGroupRef.set(studyGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ViewGroups.this, "Study group added", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(ViewGroups.this, "Failed to add group", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void showSnackBarMessage(String message){
        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:{
                newGroupDialog dialog = new newGroupDialog();
                dialog.show(getSupportFragmentManager(), "New Group Dialog");
                break;
            }
        }
    }

}
