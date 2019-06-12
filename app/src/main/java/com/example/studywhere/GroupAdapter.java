package com.example.studywhere;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "GroupAdapter";
    private GroupActivity mGroupActivity;
    private Context mContext;
    private ArrayList<Group> mGroups = new ArrayList<>();

    public GroupAdapter(Context context, ArrayList<Group> groups){
        mContext = context;
        mGroups = groups;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_group_list_item, parent, false);
        vHolder = new ViewHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        if(holder instanceof ViewHolder){
            ((ViewHolder)holder).group_name.setText(mGroups.get(pos).getGroup_name());
            ((ViewHolder)holder).school_name.setText(mGroups.get(pos).getSchool_name());
            ((ViewHolder)holder).subject_name.setText(mGroups.get(pos).getSubject());
            ((ViewHolder)holder).location.setText(mGroups.get(pos).getLocation());
            ((ViewHolder)holder).date.setText(mGroups.get(pos).getDate());
            ((ViewHolder)holder).group_size.setText(mGroups.get(pos).getGroup_size());
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            String date = sdf.format(mGroups.get(pos).getTimestamp());
            ((ViewHolder)holder).timestamp.setText(date);
        }
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mGroupActivity = (GroupActivity) mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView group_name, school_name, subject_name, location, date, group_size, timestamp;

        @Override
        public void onClick(View v) {
            //nothing here
        }

        public ViewHolder(View itemView) {
            super(itemView);
            group_name = itemView.findViewById(R.id.group_name);
            school_name = itemView.findViewById(R.id.school_name);
            subject_name = itemView.findViewById(R.id.subject_name);
            location = itemView.findViewById(R.id.location);
            date = itemView.findViewById(R.id.date);
            group_size = itemView.findViewById(R.id.group_size);
            timestamp = itemView.findViewById(R.id.timestamp);
            itemView.setOnClickListener(this);
        }

    }
}
