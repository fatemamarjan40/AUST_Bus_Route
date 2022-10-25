package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class register extends AppCompatActivity {


    EditText e4_email;
    FirebaseAuth auth;
    ProgressDialog dialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e4_email = (EditText) findViewById(R.id.editText4);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
    }
    public  void gotoPasswordActivity(View v)
    {
        dialog.setMessage("Checking email address");
        dialog.show();
        String email = e4_email.getText().toString();
        if(!email.equals(""))
        {
            auth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                            if(task.isSuccessful())
                            {
                                dialog.dismiss();
                                boolean check= !task.getResult().getSignInMethods().isEmpty();
                                if(!check)
                                {
                                    Intent myIntent = new Intent(register.this,passwardActivity.class);
                                    myIntent.putExtra("email",e4_email.getText().toString());
                                    startActivity(myIntent);
                                    finish();
                                }
                                else
                                {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"This email is already registered",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

        }
        else
        {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(),"Please Enter your Email",Toast.LENGTH_LONG).show();
        }

    }
}

