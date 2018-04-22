package com.example.vikar.publicbustracking.model;

public class TrackModel {
    int id_bus;
    int id_user;
    double latitude;
    double longitude;

    public TrackModel() {

    }

    public TrackModel(int id_bus, int id_user, double latitude, double longitude) {
        this.id_bus = id_bus;
        this.id_user = id_user;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId_bus() {
        return id_bus;
    }

    public void setId_bus(int id_bus) {
        this.id_bus = id_bus;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
