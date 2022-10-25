package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.testing.ui.profile;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinCircleActivity extends AppCompatActivity {

    Pinview pinview;
    DatabaseReference reference,currentReference;
    FirebaseUser user;
    FirebaseAuth auth;
    String current_user_id,join_user_id;
    DatabaseReference circleReference;
    String current_user_name ,current_user_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);
        pinview=(Pinview) findViewById(R.id.pinview);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentReference= FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        current_user_id = user.getUid();


        currentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                current_user_name = snapshot.child(user.getUid()).child("name").getValue(String.class);
                current_user_email = snapshot.child(user.getUid()).child("email").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public  void submitButtonClick(View v)
    {

        Query query = reference.orderByChild("code").equalTo(pinview.getValue());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    CreateUser createUser = null;
                    for(DataSnapshot childDss : snapshot.getChildren())
                    {
                        createUser = childDss.getValue(CreateUser.class);
                        join_user_id = createUser.userid;

                            circleReference = FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(join_user_id).child("CircleMembers");



                       CircleJoin circleJoin = new CircleJoin(current_user_id);
                        //profile circleJoin = new profile(current_user_name,current_user_email);
                        CircleJoin circleJoin1 = new CircleJoin(join_user_id);

                        circleReference.child(user.getUid()).setValue(circleJoin)
                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            circleReference.child(current_user_id).child("Name").setValue(current_user_name).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        circleReference.child(current_user_id).child("Email:").setValue(current_user_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    Toast.makeText(getApplicationContext(),"User joined circle successfully",Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });
                                                    }

                                                }
                                            });
                                            //reference.child(user.getUid()).child("imageUrl").setValue(download_image_path)
                                            //Toast.makeText(getApplicationContext(),"User joined circle successfully",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Circle Code is invalid",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
