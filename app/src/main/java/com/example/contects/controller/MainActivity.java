package com.example.contects.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contects.R;
import com.example.contects.model.Contact;
import com.example.contects.model.ContactManagement;
import com.example.contects.view.ContactsAdapter;
import com.example.contects.view.SearchFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements SearchFragment.OnSearchListener {

    private ContactsAdapter contactAdapter;
    private FrameLayout fragmentContainer;
    private static final int PERMISSION_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.contacts_recycler_view);
        Button addContactButton = findViewById(R.id.add_contact_button);
        Button searchButton = findViewById(R.id.search_button);
        fragmentContainer = findViewById(R.id.fragment_container);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactsAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(contactAdapter);

        loadContacts();

        addContactButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditContactActivity.class);
            startActivity(intent);
        });

            // Sync Contacts check permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {
                syncContacts();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACTS);
            }


        // Show Search View
        searchButton.setOnClickListener(v -> {
            if (fragmentContainer.getVisibility() == View.GONE) {
                showSearchFragment();
            } else {
                hideSearchFragment();
            }
        });
    }

    private void showSearchFragment() {
        fragmentContainer.setVisibility(View.VISIBLE);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setOnSearchListener(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit();
    }

    private void hideSearchFragment() {
        fragmentContainer.setVisibility(View.GONE);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    private void loadContacts() {
        ContactManagement.getAllContacts(this, contacts -> runOnUiThread(() -> {
            if (contacts != null) {
                contactAdapter.setContacts(contacts);
            } else {
                Toast.makeText(MainActivity.this, "Failed to load contacts", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void syncContacts() {
        boolean isSynced = getSharedPreferences("AppPrefs", MODE_PRIVATE).getBoolean("isSynced", false);
        if (isSynced) {
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Contact> contactsList = new ArrayList<>();
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(new Contact(name, phone));
                }
                cursor.close();
            }

            ContactManagement.insertContacts(this, contactsList, success -> {
                runOnUiThread(() -> {
                    if (success) {
                        getSharedPreferences("AppPrefs", MODE_PRIVATE)
                                .edit()
                                .putBoolean("isSynced", true)
                                .apply();

                        Toast.makeText(this, "Contacts synced successfully", Toast.LENGTH_SHORT).show();
                        loadContacts();
                    } else {
                        Toast.makeText(this, "Failed to sync contacts", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                syncContacts();
            } else {
                Toast.makeText(this, "Permission denied to read contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
    }

    @Override
    public void onSearch(String query) {
        ContactManagement.getAllContacts(this, contacts -> runOnUiThread(() -> {
            if (contacts != null) {
                List<Contact> filteredContacts = new ArrayList<>();
                for (Contact contact : contacts) {
                    if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
                        filteredContacts.add(contact);
                    }
                }
                contactAdapter.setContacts(filteredContacts);
            } else {
                Toast.makeText(MainActivity.this, "Failed to load contacts", Toast.LENGTH_SHORT).show();
            }
        }));
    }
}