package com.example.testing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.testing.Adapters.MessageAdapters;
import com.example.testing.Models.AllMethods;
import com.example.testing.Models.Message;
import com.example.testing.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatForum extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messagedb;
    MessageAdapters messageAdapter;
    User u;
    List<Message> messages;

    RecyclerView rvMessage;
    EditText etMessage;
    Button imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_forum);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();
    }
   // @Override
   /* public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {

            this.finish();

        }

        return super.onOptionsItemSelected(item);

    }*/

    private void init() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        u = new User();

        rvMessage = (RecyclerView) findViewById(R.id.rvmessage);
        etMessage = (EditText) findViewById(R.id.etMessage);
        imageButton = (Button) findViewById(R.id.btnSend);
        imageButton.setOnClickListener((View.OnClickListener) this);
        messages = new ArrayList<>();
    }

    public void onClick(View view) {

        if (!TextUtils.isEmpty(etMessage.getText().toString())) {
            Message message = new Message(etMessage.getText().toString(), u.getName());
            etMessage.setText("");
            messagedb.push().setValue(message);
        } else {
            Toast.makeText(getApplicationContext(), "You can not blank message", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();
        u.setUid(currentUser.getUid());
        u.setEmail(currentUser.getEmail());


        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                u = snapshot.getValue(User.class);
                u.setUid(currentUser.getUid());
                AllMethods.name = u.getName();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagedb = database.getReference("message");
        messagedb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(message.getKey());
                messages.add(message);
                displayMessages(messages);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());

                List<Message> newMessages = new ArrayList<>();

                for(Message m: messages)
                {
                    if(m.getKey().equals(message.getKey()))
                    {
                        newMessages.add(message);
                    }
                    else
                    {
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessages(messages);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Message message = snapshot.getValue(Message.class);

                message.setKey(snapshot.getKey());

                List<Message> newMessages = new ArrayList<Message>();

                for(Message m: messages)
                {
                    if(!m.getKey().equals(message.getKey()))
                    {
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessages(messages);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<>();

    }

    private void displayMessages(List<Message> messages) {
        rvMessage.setLayoutManager(new LinearLayoutManager(ChatForum.this));
        messageAdapter = new MessageAdapters(ChatForum.this,messages,messagedb);
        rvMessage.setAdapter(messageAdapter);

    }
}


