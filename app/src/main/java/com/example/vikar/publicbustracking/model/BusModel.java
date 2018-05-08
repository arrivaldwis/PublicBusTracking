package com.example.vikar.publicbustracking.model;

public class BusModel {
    public int id_bus;
    public int id_rute;

    public BusModel() {

    }

    public BusModel(int id_bus, int id_rute) {
        this.id_bus = id_bus;
        this.id_rute = id_rute;
    }

    public int getId_bus() {
        return id_bus;
    }

    public void setId_bus(int id_bus) {
        this.id_bus = id_bus;
    }

    public int getId_rute() {
        return id_rute;
    }

    public void setId_rute(int id_rute) {
        this.id_rute = id_rute;
    }
}
