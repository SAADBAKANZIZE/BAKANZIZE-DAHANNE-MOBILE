package com.genuinecoder.springclient.adapter;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.genuinecoder.springclient.R;
import com.genuinecoder.springclient.model.Pharmacie;

public class PharmacieHolder extends RecyclerView.ViewHolder {

    TextView name, location, branch;

    public PharmacieHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.pharmacieListItem_name);
        location = itemView.findViewById(R.id.pharmacieListItem_location);
    }
}
