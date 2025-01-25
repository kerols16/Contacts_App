package com.example.contects.model;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Contact.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static volatile Database INSTANCE;

    public abstract ContactDao contactDao();

    public static Database getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    Database.class, "contacts_database")
                            .fallbackToDestructiveMigration() // Recreates the database if there's a schema change
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
