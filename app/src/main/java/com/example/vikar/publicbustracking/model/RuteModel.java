package com.example.vikar.publicbustracking.model;

public class RuteModel {
    String destination;
    int id_rute;
    String id_station;
    String origin;

    public RuteModel() {
    }

    public RuteModel(String destination, int id_rute, String id_station, String origin) {
        this.destination = destination;
        this.id_rute = id_rute;
        this.id_station = id_station;
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getId_rute() {
        return id_rute;
    }

    public void setId_rute(int id_rute) {
        this.id_rute = id_rute;
    }

    public String getId_station() {
        return id_station;
    }

    public void setId_station(String id_station) {
        this.id_station = id_station;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
