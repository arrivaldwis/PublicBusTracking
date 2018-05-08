package com.example.vikar.publicbustracking.model;

public class StationModel {
    int id_station;
    double latitude;
    double longitude;
    String name;

    public StationModel() {
    }

    public StationModel(int id_station, double latitude, double longitude, String name) {
        this.id_station = id_station;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public int getId_station() {
        return id_station;
    }

    public void setId_station(int id_station) {
        this.id_station = id_station;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
