package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InviteCode extends AppCompatActivity {

    String name,email,password,date,issharing,code;
    Uri imageUri;
    ProgressDialog progressDialog;

    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    String userId;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        t1 = (TextView) findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        Intent myIntent = getIntent();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("User_images");
        if(myIntent!= null)
        {
            name= myIntent.getStringExtra("name");
            email= myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");
            code = myIntent.getStringExtra("code");
            issharing = myIntent.getStringExtra("isSharing");
            imageUri = myIntent.getParcelableExtra("imageUri");
        }
        t1.setText(code);
    }

    public void registerUser(View v)
    {
        progressDialog.setMessage("Please wait while we are creating an account for you");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //insert values in Real time database
                            user = auth.getCurrentUser();
                            userId =user.getUid();

                            CreateUser createUser = new CreateUser(name,email,password,"false","na","na",user.getUid());

                            //user = auth.getCurrentUser();

                            reference.child(userId).setValue(createUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                StorageReference sr = storageReference.child(user.getUid()+ ".jpg");
                                                sr.putFile(imageUri)
                                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                               if(task.isSuccessful())
                                                               {

                                                                   String download_image_path = task.getResult().getStorage().getDownloadUrl().toString();
                                                                   //String download_image_path =
                                                                   reference.child(user.getUid()).child("imageUrl").setValue(download_image_path)
                                                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                               @Override
                                                                               public void onComplete(@NonNull Task<Void> task) {
                                                                                   if(task.isSuccessful())
                                                                                   {
                                                                                       progressDialog.dismiss();
                                                                                       Toast.makeText(getApplicationContext(),"Email sent for verification. Check Email",Toast.LENGTH_SHORT).show();
                                                                                       sendVerificationEmail();
                                                                                       //finish();
                                                                                       Intent myIntent = new Intent(InviteCode.this,loginActivity.class);
                                                                                       startActivity(myIntent);

                                                                                   }
                                                                                   else
                                                                                   {
                                                                                       progressDialog.dismiss();
                                                                                       Toast.makeText(getApplicationContext(),"An error occured while creating account",Toast.LENGTH_SHORT).show();


                                                                                   }

                                                                               }
                                                                           });
                                                               }
                                                            }
                                                        });



                                            }
                                            else
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(),"Could not Register user.",Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });


                        }
                    }
                });






    }

    public void sendVerificationEmail()
    {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Email sent for Verification",Toast.LENGTH_SHORT).show();
                            finish();
                            auth.signOut();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Could not Sent Email.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
