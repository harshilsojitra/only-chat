package com.example.onch.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onch.Adapter.MessagesAdapter;
import com.example.onch.ModelClass.Messages;
import com.example.onch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String ReciverImage,ReciverUID,ReciverName,SenderUid;
    CircleImageView profileImage;
    TextView reciverName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String sImage;
    public static String rImage;

    CardView sendBtn;
    EditText edtMessage;

    String senderRoom,reciverRoom;

    RecyclerView messageAdater;
    ArrayList<Messages> messagesArrayList;

    MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();

        ReciverName=getIntent().getStringExtra("name");
        ReciverImage=getIntent().getStringExtra("ReciverImage");
        ReciverUID=getIntent().getStringExtra("uid");

        messagesArrayList=new ArrayList<>();

        profileImage  = findViewById(R.id.profile_image);
        reciverName = findViewById(R.id.reciverName);

        messageAdater=findViewById(R.id.messageAdater);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdater.setLayoutManager(linearLayoutManager);
        adapter=new MessagesAdapter(ChatActivity.this,messagesArrayList);
        messageAdater.setAdapter(adapter);

        ImageView btnbackchat=findViewById(R.id.btnbackmenu);
        btnbackchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sendBtn=findViewById(R.id.sendBtn);
        edtMessage=findViewById(R.id.edtMessage);

        Picasso.get().load("https://cdn-icons-png.flaticon.com/512/64/64572.png").into(profileImage);
        reciverName.setText(""+ReciverName);

        SenderUid=firebaseAuth.getUid();

        senderRoom=SenderUid+ReciverUID;
        reciverRoom=ReciverUID+SenderUid;



        DatabaseReference reference =database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatRefrece =database.getReference().child("chats").child(senderRoom).child("messages");


        chatRefrece.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesArrayList.clear();
                
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Messages messages=dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               sImage= snapshot.child("imageUri").getValue().toString();
               rImage=ReciverImage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=edtMessage.getText().toString();
                if(message.isEmpty())
                {
                    Toast.makeText(ChatActivity.this, "Please enter Valid Message", Toast.LENGTH_SHORT).show();
                    return;
                }
                edtMessage.setText("");
                Date date=new Date();

                Messages messages=new Messages(message,SenderUid,date.getTime());

                database=FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                         .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats")
                                        .child(reciverRoom)
                                        .child("messages")
                                        .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });
            }
        });


    }
}