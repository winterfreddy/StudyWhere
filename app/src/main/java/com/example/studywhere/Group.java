package com.example.studywhere;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

@IgnoreExtraProperties
public class Group {

    private String group_name;
    private String school_name;
    private String subject;
    private String location;
    private String date; //intended meeting time for group (doesn't have to be a format of a date)
    private String group_size;
    private @ServerTimestamp Date timestamp; //when study group was added to the database

    public Group(String group_name, String school_name, String subject, String group_size, String location, String date, Date timestamp){
        this.group_name = group_name;
        this.school_name = school_name;
        this.subject = subject;
        this.location = location;
        this.date = date;
        this.group_size = group_size;
        this.timestamp = timestamp;
    }

    public Group(){

    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLocation(){ return location; }

    public void setLocation(String location){ this.location = location;}

    public String getDate(){ return date; }

    public void setDate(String date){ this.date = date; }

    public String getGroup_size() {
        return group_size;
    }

    public void setGroup_size(String group_size) {
        this.group_size = group_size;
    }

    public Date getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(Date timestamp){
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Group{" +
                "group_name='" + group_name + '\'' +
                ", school_name='" + school_name + '\'' +
                ", subject='" + subject + '\'' +
                ", group_size=" + group_size +
                ", location=" + location +
                ", date=" + date +
                ", timestamp=" + timestamp +
                '}';
    }

}
