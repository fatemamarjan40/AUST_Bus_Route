package com.example.testing.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.R;


import java.util.ArrayList;

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.MyViewHolder> {
    Context context;
    ArrayList<profile>profiles;

    public CircleAdapter(Context context, ArrayList<profile> profiles) {
        this.context = context;
        this.profiles = profiles;
    }

    @NonNull
    @Override


    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(profiles.get(position).getName());
        holder.email.setText(profiles.get(position).getEmail());
       // Picasso.get().load(profiles.get(position).getProfilePic()).into(holder.profilePic);

    }



    @Override
    public int getItemCount() {
        return profiles.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,email;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_name);
            email = (TextView) itemView.findViewById(R.id.item_email);
          //  profilePic = (ImageView) itemView.findViewById(R.id.profile_pic);
        }

    }
}
