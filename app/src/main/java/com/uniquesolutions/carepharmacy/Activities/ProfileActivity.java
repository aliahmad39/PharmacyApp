package com.uniquesolutions.carepharmacy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uniquesolutions.carepharmacy.MainActivity;
import com.uniquesolutions.carepharmacy.Models.Users;
import com.uniquesolutions.carepharmacy.R;
import com.uniquesolutions.carepharmacy.databinding.ActivityProfileBinding;


import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth auth;
    String id;
    ActivityProfileBinding binding;
    AlertDialog dialog;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        database = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        id = currentUser.getUid();

        DatabaseReference userReference = database.getReference().child("Users").child(id);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Users users = snapshot.getValue(Users.class);
                    binding.etEmail.setText(users.getEmail());
                    binding.etphNo.setText(users.getPhnNo());
                    binding.etUserName.setText(users.getUserName());
                    binding.etPassword.setText(users.getPassword());
                    binding.ethomeAddr.setText(users.getAddress());
                    binding.tvUserName.setText(users.getUserName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Update Profile")
                        .setIcon(R.drawable.ic_update)
                        .setMessage("Are you sure to update profile")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                binding.btnSignUp.setVisibility(View.GONE);
                                binding.progressBar.setVisibility(View.VISIBLE);
                                String name = binding.etUserName.getText().toString();
                                String email = binding.etEmail.getText().toString();
                                String phn = binding.etphNo.getText().toString();
                                String pass = binding.etPassword.getText().toString();
                                String Addre = binding.ethomeAddr.getText().toString();

                                HashMap<String, Object> obj = new HashMap<>();
                                obj.put("userName", name);
                                obj.put("email", email);
                                obj.put("password", pass);
                                obj.put("PhnNo", phn);
                                obj.put("address", Addre);

                                userReference.updateChildren(obj);

//                                UserProfileChangeRequest request=new UserProfileChangeRequest.Builder()
//                                        .setDisplayName(name)
//                                        .build();

                               currentUser.updateEmail(email+"").addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {
                                    //   binding.progressBar.setVisibility(View.GONE);
                                    //   binding.btnSignUp.setVisibility(View.VISIBLE);
                                    //   Toast.makeText(ProfileActivity.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                                   }
                               });
                                //  Toast.makeText(ProfileActivity.this, "email updated", Toast.LENGTH_SHORT).show();

                               currentUser.updatePassword(pass+"").addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {
                                       Toast.makeText(ProfileActivity.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                                       binding.progressBar.setVisibility(View.GONE);
                                       binding.btnSignUp.setVisibility(View.VISIBLE);

                                   }

                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(ProfileActivity.this, "error :"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                       binding.progressBar.setVisibility(View.GONE);
                                       binding.btnSignUp.setVisibility(View.VISIBLE);

                                   }
                               });



                         // finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();

        return super.onSupportNavigateUp();
    }

}