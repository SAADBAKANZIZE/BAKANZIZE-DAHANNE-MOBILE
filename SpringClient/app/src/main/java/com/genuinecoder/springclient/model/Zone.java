package com.genuinecoder.springclient.model;

import com.genuinecoder.springclient.model.Pharmacie;
import com.genuinecoder.springclient.model.Ville;

import java.util.Set;


public class Zone {

    private int id;

    private String nom;

    private Set<Pharmacie> pharmacies;


    private Ville ville;

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

    public Set<Pharmacie> getPharmacies() {
        return pharmacies;
    }

    public void setPharmacies(Set<Pharmacie> pharmacies) {
        this.pharmacies = pharmacies;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

}
