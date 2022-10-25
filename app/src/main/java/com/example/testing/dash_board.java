package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.testing.BusShedule.BusDetails;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class dash_board extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    TextView t1_currentName, t2_currentEmail;
    ImageView i1;

    String current_user_name;
    String current_user_email;
    String current_user_imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        toolbar = findViewById(R.id.tool_bar);
        navigationView =(NavigationView)findViewById(R.id.navigation_menu);
        View header = navigationView.getHeaderView(0);

        t1_currentName = header.findViewById(R.id.title_text);
        t2_currentEmail = header.findViewById(R.id.email_text);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id =menuItem.getItemId();
                if(id == R.id.nav_signOut)
                {
                    FirebaseUser user = auth.getCurrentUser();
                    if(user !=null)
                    {
                        auth.signOut();
                        finish();
                        Intent intent = new Intent(dash_board.this,loginActivity.class);
                        startActivity(intent);


                    }

                }
                if (id == R.id.nav_myCircle) {
                    Intent myIntent = new Intent(dash_board.this, MyCircleActivity.class);
                    startActivity(myIntent);

                }

                return true;
            }
        });
        setUpToolbar();

        header = navigationView.getHeaderView(0);
        t1_currentName = header.findViewById(R.id.title_text);
        t2_currentEmail = header.findViewById(R.id.email_text);
        //View navView =navigationView.inflateHeaderView(R.layout.nav_header_user_location_main);
        i1 = header.findViewById(R.id.imageView);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                current_user_name = snapshot.child(user.getUid()).child("name").getValue(String.class);
                current_user_email = snapshot.child(user.getUid()).child("email").getValue(String.class);
                current_user_imageUrl = snapshot.child(user.getUid()).child("imageUrl").getValue(String.class);
                //String image = snapshot.child(user.getUid()).child("imageUrl").getValue().toString();
                t1_currentName.setText(current_user_name);
                t2_currentEmail.setText(current_user_email);
                //Picasso.get().load(current_user_imageUrl).placeholder(R.drawable.logo2).into(i1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setUpToolbar(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_toolbar);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch(item.getItemId())
       {
           case android.R.id.home:
               drawerLayout.openDrawer(GravityCompat.START);
               return true;
       }
       return true;
    }

    public void busShedule(View view) {
        Intent myIntent= new Intent(dash_board.this, BusDetails.class);
        startActivity(myIntent);
    }

    public void userLocation(View view) {
        Intent myIntent= new Intent(dash_board.this, UserLocationMainActivity.class);
        startActivity(myIntent);
    }

    public void ChatForum(View view) {
        Intent myIntent= new Intent(dash_board.this, ChatForum.class);
        startActivity(myIntent);
    }

    public void aboutUs(View view) {
        Intent myIntent= new Intent(dash_board.this, AboutUs.class);
        startActivity(myIntent);
    }
}
