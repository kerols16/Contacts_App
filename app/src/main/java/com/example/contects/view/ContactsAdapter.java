package com.example.contects.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contects.R;
import com.example.contects.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private Context context;
    private List<Contact> contactsList;

    // Constructor to initialize context and contact list
    public ContactsAdapter(Context context, List<Contact> contactsList) {
        this.context = context;
        this.contactsList = contactsList != null ? contactsList : new ArrayList<>();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_contact layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        // Get the current contact at the position
        Contact contact = contactsList.get(position);

        // Set contact name and phone number in the views
        holder.contactName.setText(contact.getName());
        holder.contactNumber.setText(contact.getPhoneNumber());

        // Handle click on contact card to navigate to ContactDetailsActivity
        holder.contactCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.example.contects.controller.ContactDetailsActivity.class);
            intent.putExtra("contact", contact);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactNumber;
        CardView contactCard;

        public ContactViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            contactNumber = itemView.findViewById(R.id.contact_number);
            contactCard = itemView.findViewById(R.id.contact_card);
        }
    }

    // Method to update the contact list when new data is available
    @SuppressLint("NotifyDataSetChanged")
    public void setContacts(List<Contact> contacts) {
        this.contactsList = contacts != null ? contacts : new ArrayList<>();
        notifyDataSetChanged(); // Notify the adapter to refresh the RecyclerView
    }
}
