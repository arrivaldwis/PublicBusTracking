package com.example.vikar.publicbustracking.model;

/**
 * Created by vikar on 12-Mar-18.
 */

public class UserModel {
    private Long id_user;
    private String phone;
    private String email;
    private String name;
    private String role;
    private String image;
    private int id_bus;

    public UserModel(Long id_user, String email, String name, String role, String image, int id_bus,
                     String phone) {
        this.id_user = id_user;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.role = role;
        this.image = image;
        this.id_bus = id_bus;
    }

    public UserModel() {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId_bus() {
        return id_bus;
    }

    public void setId_bus(int id_bus) {
        this.id_bus = id_bus;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
