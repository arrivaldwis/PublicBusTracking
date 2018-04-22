package com.example.vikar.publicbustracking.model;

public class StationModel {
    int id_station;
    int latitude;
    int longitude;
    String name;

    public StationModel() {
    }

    public StationModel(int id_station, int latitude, int longitude, String name) {
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

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
