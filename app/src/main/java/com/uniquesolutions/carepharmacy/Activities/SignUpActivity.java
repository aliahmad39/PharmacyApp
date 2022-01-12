package com.uniquesolutions.carepharmacy.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.uniquesolutions.carepharmacy.Models.Users;
import com.uniquesolutions.carepharmacy.R;
import com.uniquesolutions.carepharmacy.databinding.ActivitySignUpBinding;


public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;

    FirebaseDatabase database;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;

    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        signup = findViewById(R.id.btnSignUp);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("SignUp in progress");


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.etUserName.getText().toString().isEmpty())
                    binding.etUserName.setError("Enter UserName*");

                if (binding.etEmail.getText().toString().isEmpty())
                    binding.etEmail.setError("Enter Email*");

                if (binding.etphNo.getText().toString().isEmpty())
                    binding.etphNo.setError("Enter Phone Number*");

                if (binding.etaddress.getText().toString().isEmpty())
                    binding.etaddress.setError("Enter your shipping address*");

                if (binding.etPassword.getText().toString().isEmpty()) {
                    binding.etPassword.setError("Enter Password*");
                    return;
                } else {
                    progressDialog.show();
                    createUser();
                }

            }
        });

        binding.tvaha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createUser() {
        auth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(), binding.etPassword.getText().toString()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Users user = new Users(binding.etUserName.getText().toString(), binding.etEmail.getText().toString(),
                                    binding.etPassword.getText().toString(), binding.etphNo.getText().toString(), binding.etaddress.getText().toString());

                            String id = task.getResult().getUser().getUid();
                            user.setUserId(id);
                            database.getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(SignUpActivity.this, "Account Created Succesfully...", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return super.onSupportNavigateUp();

    }
}