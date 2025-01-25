package com.example.contects.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ContactManagement {

    // Get all contacts from the database
    public static void getAllContacts(Context context, OnContactsLoadedListener listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Database db = Database.getInstance(context);
            List<Contact> contacts = db.contactDao().getAllContacts();
            if (listener != null) {
                listener.onContactsLoaded((contacts != null) ? contacts : new ArrayList<>());
            }
        });
    }


    public static void insertContact(Context context, Contact contact, OnOperationCompleteListener listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Database db = Database.getInstance(context);
            db.contactDao().insertContact(contact);
            if (listener != null) {
                listener.onOperationComplete();
            }
        });
    }

    public static void insertContacts(Context context, List<Contact> contacts, OnContactsInsertedListener listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Database db = Database.getInstance(context);
                db.contactDao().insertContacts(contacts);
                if (listener != null) {
                    listener.onContactsInserted(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onContactsInserted(false);
                }
            }
        });
    }



    public static void searchContacts(Context context, String query, OnContactsLoadedListener listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Database db = Database.getInstance(context);
            List<Contact> filteredContacts = db.contactDao().searchContacts("%" + query + "%");
            if (listener != null) {
                listener.onContactsLoaded((filteredContacts != null) ? filteredContacts : new ArrayList<>());
            }
        });
    }

    // Update an existing contact
    public static void updateContact(Context context, Contact contact, OnOperationCompleteListener listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Database db = Database.getInstance(context);
            db.contactDao().updateContact(contact);
            if (listener != null) {
                listener.onOperationComplete();
            }
        });
    }

    public static void deleteContact(Context context, Contact contact, OnContactDeletedListener listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Database db = Database.getInstance(context);
            db.contactDao().deleteContact(contact);
            if (listener != null) {
                listener.onContactDeleted(true);
            }
        });
    }

    public interface OnContactDeletedListener {
        void onContactDeleted(boolean success);
    }


    public interface OnContactsLoadedListener {
        void onContactsLoaded(List<Contact> contacts);

    }

    public interface OnOperationCompleteListener {
        void onOperationComplete();
    }

    public interface OnContactsInsertedListener {
        void onContactsInserted(boolean success);
    }
}
