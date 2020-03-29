package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView profile_name, profile_status;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    EditText messageBox;    ImageButton sendButton;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<Message> mMessage;
    String userId;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.userToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profile_image = findViewById(R.id.currentUserImage);
        profile_name = findViewById(R.id.currentUserEmail);
        profile_status = findViewById(R.id.userCurrentStatus);
        messageBox = findViewById(R.id.messageBox);
        sendButton = findViewById(R.id.sendButton);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.messageRecyclerView);

        intent = getIntent();
        userId = intent.getStringExtra("userId");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        assert userId != null;
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                if(TextUtils.equals(user.getImageURL(), "default")){
                    profile_image.setImageResource(R.drawable.ic_person_black_50dp);
                }else{
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }
                profile_name.setText(user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mMessage = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, mMessage);
        recyclerView.setAdapter(messageAdapter);

        readMessages();


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt = messageBox.getText().toString();
                DatabaseReference messageReference = FirebaseDatabase.getInstance().getReference().child("chats");

                if(!messageTxt.isEmpty()) {

                    HashMap<String, Object> message = new HashMap<>();
                    message.put("sender", currentUser.getUid());
                    message.put("receiver", userId);
                    message.put("message", messageTxt);
                    message.put("timestamp", ServerValue.TIMESTAMP);

                    messageReference.push().setValue(message);
                    messageBox.setText("");
                }


            }
        });

    }

    private void readMessages() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chats");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Message message = dataSnapshot.getValue(Message.class);

                if(message.getSender().equals(currentUser.getUid()) && message.getReceiver().equals(userId) ||
                        message.getSender().equals(userId) && message.getReceiver().equals(currentUser.getUid())) {
                    mMessage.add(message);
                    messageAdapter.notifyItemInserted(mMessage.size()-1);
                    recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
