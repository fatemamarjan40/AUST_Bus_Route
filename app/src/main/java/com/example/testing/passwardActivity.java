package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class passwardActivity extends AppCompatActivity {
    String email;
    EditText e3_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passward);
        e3_password=(EditText)findViewById(R.id.editText3);
        Intent myIntent = getIntent();
        if(myIntent!= null)
        {
            email = myIntent.getStringExtra("email");

        }
    }

    public void gotoNameActivity(View v)
    {
        String password = e3_password.getText().toString();
        if(!password.equals("") && password.length()>6)
        {
            Intent myIntent= new Intent(passwardActivity.this,nameActivity.class);
            myIntent.putExtra("email",email);
            myIntent.putExtra("password",e3_password.getText().toString());
            startActivity(myIntent);
            finish();

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Password length should be more than 6 character and not be empty!",Toast.LENGTH_LONG).show();
        }
    }
}

