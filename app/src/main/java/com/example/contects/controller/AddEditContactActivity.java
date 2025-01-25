package com.example.contects.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contects.R;
import com.example.contects.model.Contact;
import com.example.contects.model.ContactManagement;

public class AddEditContactActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText;
    private Button saveButton;
    private TextView titletxt;
    private Contact  currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        nameEditText = findViewById(R.id.edit_contact_name);
        phoneEditText = findViewById(R.id.edit_contact_number);
        saveButton = findViewById(R.id.save_contact_button);
        titletxt = findViewById(R.id.edit_contact_title);

        // Check intent for contact data
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("contact")) {
            // Editing an existing contact
            currentContact = (Contact) intent.getSerializableExtra("contact");
            setTitle("Edit Contact");
            titletxt.setText("Edit Contact");

            // Pre-fill fields with existing contact data
            if (currentContact != null) {
                nameEditText.setText(currentContact.getName());
                phoneEditText.setText(currentContact.getPhoneNumber());
            }
        } else {
            // Adding a new contact
            setTitle("Add New Contact");
            titletxt.setText("Add New Contact");
        }

        // Set up Save button click listener
        saveButton.setOnClickListener(v -> saveContact());
    }

    private void saveContact() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Check if name is empty first
        if (name.isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate name length
        if (name.length() > 15) {
            Toast.makeText(this, "Name must be between 1 and 15 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate phone number format
        if (phone.length() != 11 || !phone.matches("\\d+") ) {
            Toast.makeText(this, "Phone number must be exactly 11 or 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }


        if (currentContact != null) {
            currentContact.setName(name);
            currentContact.setPhoneNumber(phone);
            ContactManagement.updateContact(this, currentContact, () -> {
                Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show();
            });
        } else {

            Contact newContact = new Contact(name, phone);
            ContactManagement.insertContact(this, newContact, () -> {
                Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();
            });
        }


    }

}
