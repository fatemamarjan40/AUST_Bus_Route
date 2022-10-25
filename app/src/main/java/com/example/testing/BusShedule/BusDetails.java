package com.example.testing.BusShedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;


import com.example.testing.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BusDetails extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

    }
    public void onClick(View v) {
        if(R.id.rt1==v.getId())
        {
            Intent enterIntent = new Intent(BusDetails.this, firstRoute.class);
            startActivity(enterIntent);
        }
        if(R.id.rt2==v.getId())
        {
            Intent enterIntent = new Intent(BusDetails.this, secondRoute.class);
            startActivity(enterIntent);
        }

        if(R.id.rt3==v.getId())
        {
            Intent enterIntent = new Intent(BusDetails.this,thirdRoute.class);
            startActivity(enterIntent);
        }
        if(R.id.rt4==v.getId())
        {
            Intent enterIntent = new Intent(BusDetails.this, fourthRoute.class);
            startActivity(enterIntent);
        }
        if(R.id.rt5==v.getId())
        {
            Intent enterIntent = new Intent(BusDetails.this, fifthRoute.class);
            startActivity(enterIntent);
        }
        if(R.id.rt6==v.getId())
        {
            Intent enterIntent = new Intent(BusDetails.this,sixthRoute.class);
            startActivity(enterIntent);
        }

    }
}
