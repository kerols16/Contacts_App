package com.example.contects.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.contects.R;
import com.example.contects.model.Contact;
import com.example.contects.model.ContactManagement;

public class ContactDetailsActivity extends AppCompatActivity {

    private TextView contactNameTextView, contactNumberTextView;
    private Button editContactButton, deleteContactButton, callContactButton;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        contactNameTextView = findViewById(R.id.contact_name);
        contactNumberTextView = findViewById(R.id.contact_number);
        editContactButton = findViewById(R.id.edit_contact_button);
        deleteContactButton = findViewById(R.id.delete_contact_button);
        callContactButton = findViewById(R.id.call_contact_button);
        Intent intent = getIntent();
        contact = (Contact) intent.getSerializableExtra("contact");

        if (contact != null) {
            contactNameTextView.setText(contact.getName());
            contactNumberTextView.setText(contact.getPhoneNumber());
        }


        editContactButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(ContactDetailsActivity.this, AddEditContactActivity.class);
            editIntent.putExtra("contact", contact);
            startActivityForResult(editIntent, 1);
            finish();
        });


        deleteContactButton.setOnClickListener(v -> {
            if (contact != null) {
                ContactManagement.deleteContact(ContactDetailsActivity.this, contact, new ContactManagement.OnContactDeletedListener() {
                    @Override
                    public void onContactDeleted(boolean success) {
                        if (success) {
                            Toast.makeText(ContactDetailsActivity.this, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ContactDetailsActivity.this, "Failed to delete contact", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        callContactButton.setOnClickListener(v -> {
            String phoneNumber = contact.getPhoneNumber();

            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                // Check for CALL_PHONE permission
                if (ContextCompat.checkSelfPermission(ContactDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ContactDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {

                    makePhoneCall(phoneNumber);
                }
            } else {
                Toast.makeText(ContactDetailsActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, make the call
                String phoneNumber = contact.getPhoneNumber();
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    makePhoneCall(phoneNumber);
                }
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(ContactDetailsActivity.this, "Permission denied. Cannot make the call", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to make a phone call
    private void makePhoneCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber)); // URI format for phone calls
        startActivity(callIntent);
    }
}
