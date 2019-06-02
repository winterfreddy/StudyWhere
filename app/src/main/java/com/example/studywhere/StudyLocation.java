package com.example.studywhere;

import com.google.firebase.firestore.GeoPoint;

public class StudyLocation {

    public String locationName;
    public GeoPoint geo_point;
    public String crowdedness;
    public String noiseness;

    public StudyLocation(){
    }

    public StudyLocation(String locationName, GeoPoint geo_point, String crowdedness, String noiseness){
        this.locationName = locationName;
        this.geo_point = geo_point;
        this.crowdedness = crowdedness;
        this.noiseness = noiseness;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public GeoPoint getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(GeoPoint geo_point) {
        this.geo_point = geo_point;
    }

    public String getCrowdedness() {
        return crowdedness;
    }

    public void setCrowdedness(String crowdedness) {
        this.crowdedness = crowdedness;
    }

    public String getNoiseness() {
        return noiseness;
    }

    public void setNoiseness(String noiseness) {
        this.noiseness = noiseness;
    }
}
