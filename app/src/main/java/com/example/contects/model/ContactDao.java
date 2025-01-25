package com.example.contects.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert
    void insertContact(Contact contact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContacts(List<Contact> contacts);

    @Query("SELECT * FROM contacts")
    List<Contact> getAllContacts();

    @Query("SELECT * FROM contacts WHERE name LIKE :query")
    List<Contact> searchContacts(String query);

    @Update
    void updateContact(Contact contact);

    @Delete
    void deleteContact(Contact contact);
}
