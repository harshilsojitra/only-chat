package com.example.onch.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onch.R;
import com.example.onch.Adapter.UserAdepter;
import com.example.onch.ModelClass.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainUserRecycerView;
    UserAdepter adepter;
    FirebaseDatabase  database;
    ArrayList<Users> usersArrayList;
    ImageView imgLogout;
    TextView Username;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Username = findViewById(R.id.Username);
        etSearch = findViewById(R.id.etsearch);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        usersArrayList = new ArrayList<>();

        SetData();

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(HomeActivity.this,R.style.Dialoge);

                dialog.setContentView(R.layout.dialog_layout);
                TextView yesBtn,noBtn;

                yesBtn=dialog.findViewById(R.id.yesBtn);
                noBtn=dialog.findViewById(R.id.noBtn);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        //startActivity(new Intent(HomeActivity.this,RegistrationActivity.class));
                    }

                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SearchData(s.toString());
            }
        });

    }

    private void SetData() {
        DatabaseReference reference = database.getReference().child("user");
        usersArrayList.clear();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Users users = dataSnapshot.getValue(Users.class);
                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid())) {
                        Username.setText(users.getName());
                    }
                    if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid()))
                    {
                        usersArrayList.add(users);
                    }
                }
                adepter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        imgLogout=findViewById(R.id.img_logout);
        mainUserRecycerView = findViewById(R.id.mainUserRecycerView);
        mainUserRecycerView.setLayoutManager(new LinearLayoutManager(this));
        adepter= new UserAdepter(HomeActivity.this,usersArrayList);
        mainUserRecycerView.setAdapter(adepter);
    }

    private void SearchData(String s) {
        DatabaseReference reference = database.getReference().child("user");
        usersArrayList.clear();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Users users = dataSnapshot.getValue(Users.class);
                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid())) {
                        Username.setText(users.getName());
                    }
                    if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid()))
                    {
                        if(users.getName().toLowerCase().contains(s.toLowerCase())){
                            usersArrayList.add(users);
                        }
                    }
                }
                adepter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        imgLogout=findViewById(R.id.img_logout);
        mainUserRecycerView = findViewById(R.id.mainUserRecycerView);
        mainUserRecycerView.setLayoutManager(new LinearLayoutManager(this));
        adepter= new UserAdepter(HomeActivity.this,usersArrayList);
        mainUserRecycerView.setAdapter(adepter);
    }
}