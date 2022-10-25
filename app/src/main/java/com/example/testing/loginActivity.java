package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextInputEditText email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null)
        {
            Intent i = new Intent(loginActivity.this,dash_board.class);
            startActivity(i);
        }
        else {

            setContentView(R.layout.activity_login);
            email=(TextInputEditText) findViewById(R.id.emailLogin);
            pass=(TextInputEditText) findViewById(R.id.passwordLogin);
        }

    }

    public void gotoRegister(View v)
    {
        Intent myIntent= new Intent(loginActivity.this,register.class);
        startActivity(myIntent);

    }
   /* public void ok(View v)
    {
        Intent myIntent= new Intent(loginActivity.this,dash_board.class);
        startActivity(myIntent);
    }*/


    public void LoginUser(View v)
    {
        String textEmail = email.getText().toString();
        String password = pass.getText().toString();
        if(!textEmail.equals("") && !password.equals(""))
        {
            auth.signInWithEmailAndPassword(textEmail,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                //Toast.makeText(getApplicationContext(),"User logged successfully",Toast.LENGTH_LONG).show();
                                FirebaseUser user = auth.getCurrentUser();
                                if(user.isEmailVerified())
                                {
                                    Intent intent = new Intent(loginActivity.this,dash_board.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {

                                    finish();
                                    Toast.makeText(getApplicationContext(),"Email is not Verified yet.",Toast.LENGTH_LONG).show();
                                }


                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Wrong Email or Password",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Enter something ! ",Toast.LENGTH_LONG).show();

        }



    }


}
