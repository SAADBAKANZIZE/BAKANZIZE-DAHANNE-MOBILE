package com.genuinecoder.springclient.adapter;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genuinecoder.springclient.DetailsActivity;
import com.genuinecoder.springclient.MapsActivity;
import com.genuinecoder.springclient.R;
import com.genuinecoder.springclient.model.Employee;
import com.genuinecoder.springclient.model.Pharmacie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PharmacieAdapter extends RecyclerView.Adapter<PharmacieHolder> {

    private List<Pharmacie> pharmacieList;

    public PharmacieAdapter(List<Pharmacie> pharmacieList) {
        this.pharmacieList = pharmacieList;
    }

    @NonNull
    @Override
    public PharmacieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pharmacie_item, parent, false);
        return new PharmacieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacieHolder holder, int position) {
        Pharmacie pharmacie = pharmacieList.get(position);
        holder.name.setText(pharmacie.getNom());
        holder.location.setText(pharmacie.getAdresse());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                final ProgressDialog dialog = new ProgressDialog(view.getContext());
                dialog.setMessage("Loading Delete Data");
                final CharSequence[] dialogitem = {"Go to Map", "View details"};
                builder.setTitle(pharmacie.getNom());
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                System.out.println(pharmacie.getLatitude());
                                System.out.println(pharmacie.getLongitude());

                                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                                intent.putExtra("lat", pharmacie.getLatitude());
                                intent.putExtra("long", pharmacie.getLongitude());
                                intent.putExtra("name", pharmacie.getNom());

                                view.getContext().startActivity(intent);
                                break;
                            case 1 :
                                Intent intentDetails = new Intent(view.getContext(), DetailsActivity.class);
                                view.getContext().startActivity(intentDetails);
                                break;

                        }
                    }
                });

                builder.create().show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return pharmacieList.size();
    }
}
