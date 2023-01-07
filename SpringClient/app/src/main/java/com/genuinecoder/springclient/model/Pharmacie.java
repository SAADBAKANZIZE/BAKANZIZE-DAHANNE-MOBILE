package com.genuinecoder.springclient.model;


import com.genuinecoder.springclient.model.Zone;

import java.util.Date;


public class Pharmacie {

    private int id;

    private String nom;
    private String adresse;
    private double longitude;
    private double latitude;


    private Zone zone;
    private User pharmacien;
    private Date dateCreation = new Date();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public User getPharmacien() {
        return pharmacien;
    }

    public void setPharmacien(User pharmacien) {
        this.pharmacien = pharmacien;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
}
