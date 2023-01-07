package com.genuinecoder.springclient.model;

import com.genuinecoder.springclient.model.Pharmacie;

import java.util.Set;


public class User {

    private int id;

    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String username;
    private String role;




    private Set<Pharmacie> pharmacies;

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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Set<Pharmacie> getPharmacies() {
        return pharmacies;
    }


    public void setPharmacies(Set<Pharmacie> pharmacies) {
        this.pharmacies = pharmacies;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
