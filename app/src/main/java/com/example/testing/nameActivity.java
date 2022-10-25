package com.example.testing;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;


public class nameActivity extends AppCompatActivity {

    //String email, password;
    EditText e5_name;
    //CircleImageView circleImage;
    Uri resultUrl;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    String userId;
    ProgressDialog progressDialog;
    String name, email, password, date, issharing, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        e5_name = (EditText) findViewById(R.id.editText5);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        Intent myIntent = getIntent();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        //circleImage = (CircleImageView) findViewById(R.id.circleImage);
        if (myIntent != null) {
           // name = myIntent.getStringExtra("name");
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");
            //code = myIntent.getStringExtra("code");
            issharing = myIntent.getStringExtra("isSharing");
            // imageUri = myIntent.getParcelableExtra("imageUri");
        }

    /*    if (myIntent != null) {
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");

        }*/
    }

    public void registration(View view) {
        final String userName = e5_name.getText().toString();

        if(userName.equals("") && resultUrl== null)
        {
            Toast.makeText(getApplicationContext(),"Please Enter something ",Toast.LENGTH_LONG).show();
        }
        else
        {
            progressDialog.setMessage("Please wait while we are creating an account for you");
            progressDialog.show();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    user = auth.getCurrentUser();
                    userId = user.getUid();
                    // CreateUser createUser = new CreateUser(name, email, password, code, "false", "na", "na", user.getUid());
                    CreateUser createUser = new CreateUser(userName,email,password,"false","na","na",userId);
                    reference.child(user.getUid()).setValue(createUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Email sent for verification. Check Email", Toast.LENGTH_SHORT).show();
                                        sendVerificationEmail();
                                        finish();
                                        //Intent myIntent = new Intent(nameActivity.this, loginActivity.class);
                                       // startActivity(myIntent);

                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "An error occured while creating account", Toast.LENGTH_SHORT).show();


                                    }

                                }
                            });
                }
            });

        }

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
                            Intent myIntent = new Intent(nameActivity.this, loginActivity.class);
                             startActivity(myIntent);


                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Could not Sent Email.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}

  /*  public void generateCode(View v) {
        Date myDate = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
        String date = format1.format((myDate));
        Random r = new Random();

        int n = 100000 + r.nextInt(900000);
        String code = String.valueOf(n);

        String userName = e5_name.getText().toString();
        if (userName.equals("") && resultUrl == null) {
            Toast.makeText(getApplicationContext(), "Please Enter something ", Toast.LENGTH_LONG).show();
        } else {
            Intent myIntent = new Intent(nameActivity.this, InviteCode.class);
            myIntent.putExtra("name", e5_name.getText().toString());
            myIntent.putExtra("email", email);
            myIntent.putExtra("password", password);
            myIntent.putExtra("date", date);
            myIntent.putExtra("isSharing", "true");
            myIntent.putExtra("code", code);
            // myIntent.putExtra("imageUri",resultUrl);


            startActivity(myIntent);
            finish();

        }


    }
}

    /*public void selectImage(View v)
    {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i,12);
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 12 && resultCode == RESULT_OK && data != null)
        {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUrl = result.getUri();
                circleImage.setImageURI(resultUrl);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }*/


