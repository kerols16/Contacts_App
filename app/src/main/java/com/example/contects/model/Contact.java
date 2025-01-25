package com.example.contects.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
//data class contact
@Entity(tableName = "contacts")
public class Contact implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String phoneNumber;


    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
