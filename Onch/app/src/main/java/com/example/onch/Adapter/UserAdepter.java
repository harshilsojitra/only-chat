package com.example.onch.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onch.Activity.ChatActivity;
import com.example.onch.ModelClass.Users;
import com.example.onch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdepter extends RecyclerView.Adapter<UserAdepter.Viewholder> {
    Context HomeActivity ;
    ArrayList<Users> usersArrayList;
    public UserAdepter(com.example.onch.Activity.HomeActivity homeActivity, ArrayList<Users> usersArrayList) {
        this.HomeActivity= homeActivity;
        this.usersArrayList=usersArrayList;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(HomeActivity).inflate(R.layout.item_user_drow,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
            Users users = usersArrayList.get(position);

            holder.user_name.setText(users.name);
            holder.user_status.setText(users.status);
//        Picasso.get().load(users.imageUri).into(holder.user_profile);

        Picasso.get().load("https://cdn-icons-png.flaticon.com/512/64/64572.png").into(holder.user_profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity, ChatActivity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("ReciverImage",users.getImageUri());
                intent.putExtra("uid",users.getUid());
                HomeActivity.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }
    class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView user_profile;
        TextView user_name;
        TextView user_status;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            user_profile = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            user_status = itemView.findViewById(R.id.user_status);
        }
    }
}
