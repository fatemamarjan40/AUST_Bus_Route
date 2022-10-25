package com.example.testing;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class UserLocationMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    FirebaseAuth auth;
    GoogleMap mMap;
    GoogleApiClient client;
    FirebaseUser user;
    DatabaseReference databaseReference;
    LocationRequest request;
    LatLng latLng;

    TextView t1_currentName, t2_currentEmail;
    ImageView i1;


    String current_user_name;
    String current_user_email;
    String current_user_imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        setSupportActionBar(toolbar);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        // image = (CircleImageView) findViewById(R.id.imageView);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
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
               // Picasso.get().load(current_user_imageUrl).placeholder(R.drawable.logo2).into(i1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_location_main, menu);
        return true;
    }

    public boolean OnOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            this.finish();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

      /*  if(id== R.id.nav_joinCircle)
        {
            Intent myIntent = new Intent (UserLocationMainActivity.this,JoinCircleActivity.class);
            startActivity(myIntent);
        }
        else*/
        if (id == R.id.nav_signOut) {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                auth.signOut();
                finish();
                Intent intent = new Intent(UserLocationMainActivity.this, loginActivity.class);
                startActivity(intent);


            }

        } else if (id == R.id.nav_shareloc) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            //i.putExtra(Intent.EXTRA_TEXT,"My location is : "+"https//play.google.com/store/apps/details?id= "+BuildConfig.APPLICATION_ID+"\n\n"+latLng.latitude+","+latLng.longitude+",17z");
            i.putExtra(Intent.EXTRA_TEXT, "My location is : " + "https//www.google.com/maps/@" + latLng.latitude + "," + latLng.longitude + ",17z");
            startActivity(i.createChooser(i, "Share using: "));


        }
        /*else if(id == R.id.nav_inviteMembers)
        {
            Intent myIntent= new Intent(UserLocationMainActivity.this,shareInviteCode.class);
            startActivity(myIntent);

        }
        else*/
        if (id == R.id.nav_myCircle) {
            Intent myIntent = new Intent(UserLocationMainActivity.this, MyCircleActivity.class);
            startActivity(myIntent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById((R.id.drawer_layout));
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        client.connect();

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location == null) {
            Toast.makeText(getApplicationContext(), "Could not get Location", Toast.LENGTH_SHORT).show();

        } else {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions options = new MarkerOptions();
            //options.(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            options.position(latLng);
            options.title("Current Location");
            mMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14F));
            databaseReference.child(user.getUid()).child("lat").setValue(latLng.latitude);
            databaseReference.child(user.getUid()).child("lng").setValue(latLng.longitude);
            databaseReference.child(user.getUid()).child("isSharing").setValue("true");
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(3000);
        //LocationServices.FusedLocationApi.requestLocationUpdates(client,request,this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
