package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class shareInviteCode extends AppCompatActivity {

    TextView showCode;
    String code;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    FirebaseUser user;
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_invite_code);
        bt = (Button) findViewById(R.id.share);
        auth = FirebaseAuth.getInstance();
        showCode=(TextView)findViewById(R.id.showCode);
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                code=snapshot.child(user.getUid()).child("code").getValue(String.class);
                showCode.setText(code);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void share(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"share code");
        String shareMassege ="This My Circle code:  " +code +" \n\n ";
        intent.putExtra(Intent.EXTRA_TEXT,shareMassege);
        startActivity(Intent.createChooser(intent,"share by"));
    }
}
