package com.jasoncareter.ipop.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasoncareter.ipop.R;

public class ContactsRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView name ;
    public ImageView header ;
    public View view;

    public ContactsRecyclerViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        name = itemView.findViewById(R.id.contacts_name);
        header = itemView.findViewById(R.id.contacts_header);
    }
}
