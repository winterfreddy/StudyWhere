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
    private ArrayList<Group> mGroups = new ArrayList<>();
    private GroupActivity mGroupActivity;
    private Context mContext;
    private int currGroupInd;

    public GroupAdapter(Context context, ArrayList<Group> groups){
        mGroups = groups;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_group_list_item, parent, false);

        holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder){
            ((ViewHolder)holder).group_name.setText(mGroups.get(position).getGroup_name());
            SimpleDateFormat spf = new SimpleDateFormat("MMM dd, yyyy");
            String date = spf.format(mGroups.get(position).getTimestamp());
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

        TextView group_name, timestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            group_name = itemView.findViewById(R.id.group_name);
            timestamp = itemView.findViewById(R.id.timestamp);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            currGroupInd = getAdapterPosition();
            mGroupActivity.onGroupSelected(mGroups.get(currGroupInd));
        }
    }
}
