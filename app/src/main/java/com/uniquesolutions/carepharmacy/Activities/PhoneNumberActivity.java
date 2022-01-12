package com.uniquesolutions.carepharmacy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.uniquesolutions.carepharmacy.MainActivity;
import com.uniquesolutions.carepharmacy.databinding.ActivityPhoneNumberBinding;


public class PhoneNumberActivity extends AppCompatActivity {
    ActivityPhoneNumberBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();


        binding.etPhoneNumber.requestFocus();
        auth= FirebaseAuth.getInstance();

        binding.ccp.registerCarrierNumberEditText(binding.etPhoneNumber);

        if(auth.getCurrentUser() != null)
        {
            Intent intent=new Intent(PhoneNumberActivity.this, MainActivity.class);
            startActivity(intent);
        }
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PhoneNumberActivity.this, OTPActivity.class);
                intent.putExtra("phoneNumber",binding.ccp.getFullNumberWithPlus().trim());
                startActivity(intent);
            }
        });
    }
}