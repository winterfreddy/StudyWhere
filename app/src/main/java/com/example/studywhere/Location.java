package com.example.studywhere;

import com.google.firebase.firestore.GeoPoint;

public class Location {
    private String crowdedness;
    private GeoPoint geo_point;
    private String noiseness;

    public Location(String crowdedness, GeoPoint geo_point, String noiseness) {
        this.crowdedness = crowdedness;
        this.geo_point = geo_point;
        this.noiseness = noiseness;
    }

    public Location(){

    }

    public String getCrowdedness() {
        return crowdedness;
    }

    public void setCrowdedness(String crowdedness) {
        this.crowdedness = crowdedness;
    }

    public GeoPoint getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(GeoPoint geo_point) {
        this.geo_point = geo_point;
    }

    public String getNoiseness() {
        return noiseness;
    }

    public void setNoiseness(String noiseness) {
        this.noiseness = noiseness;
    }

    @Override
    public String toString() {
        return "Location{" +
                " crowdedness='" + crowdedness + '\'' +
                ", geo_point=" + geo_point +
                ", noiseness='" + noiseness + '\'' +
                '}';
    }
}
