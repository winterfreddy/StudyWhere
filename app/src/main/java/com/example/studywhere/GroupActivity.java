package com.example.studywhere;

public interface GroupActivity {

    void createNewGroup(String group_name, String school_name, String subject, String location, String date, String group_size);
    void onGroupSelected(Group group);
}
