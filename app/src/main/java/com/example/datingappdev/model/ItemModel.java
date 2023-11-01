package com.example.datingappdev.model;

public class ItemModel {

    private int image;
    private String nama, usia, kota;
    private User user;

    public ItemModel() {
    }

    public ItemModel(int image, String nama, String usia, String kota) {
        this.image = image;
        this.nama = nama;
        this.usia = usia;
        this.kota = kota;
    }
    public ItemModel(int image, User user) {
        this.image = image;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public int getImage() {
        return image;
    }

    public String getNama() {
        return nama;
    }

    public String getUsia() {
        return usia;
    }

    public String getKota() {
        return kota;
    }
}
