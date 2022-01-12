package com.uniquesolutions.carepharmacy.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uniquesolutions.carepharmacy.Models.Medicines;
import com.uniquesolutions.carepharmacy.R;
import com.uniquesolutions.carepharmacy.databinding.ActivityAdminDevicesBinding;

import java.util.Calendar;


public class AdminDevicesActivity extends AppCompatActivity {
    ActivityAdminDevicesBinding binding;
    StorageReference reference;
    FirebaseStorage storage;
    FirebaseDatabase database;
    Medicines medicines;
    String name, category, status, price, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDevicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        medicines = new Medicines();


        binding.btnaddmedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.etDeviceName.getText().toString();
                status = binding.etmedicinestatus.getText().toString();
                price = binding.etmedicineprice.getText().toString();


                if (name.isEmpty()) {
                    binding.etDeviceName.setError("Name Required");
                    return;
                }
                if (status.isEmpty()) {
                    binding.etmedicinestatus.setError("Status Required");
                    return;
                }
                if (price.isEmpty()) {
                    binding.etmedicineprice.setError("Price Required");
                    return;
                }

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 45) {
            if (data != null) {
                Calendar calendar = Calendar.getInstance();
                Uri filepath = data.getData();
                reference = storage.getReference().child("ProductDevice").child(name).child(calendar.getTimeInMillis() + "");
                reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                medicines.setImage(uri.toString());
                                medicines.setName(name);
                                medicines.setStatus(status);
                                medicines.setPrice(price);

                                String randomKey = database.getReference().push().getKey();
                                medicines.setId(randomKey);

                                database.getReference().child("Devices").child(randomKey).setValue(medicines);
                                binding.etmedicineprice.setText("");
                                binding.etmedicinestatus.setText("");
                                binding.etDeviceName.setText("");
                                Toast.makeText(AdminDevicesActivity.this, "Operation Successfull...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        }
    }
}