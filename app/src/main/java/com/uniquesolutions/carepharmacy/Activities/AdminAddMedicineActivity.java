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
import com.uniquesolutions.carepharmacy.databinding.ActivityAdminAddMedicineBinding;
import com.uniquesolutions.carepharmacy.databinding.ActivityAdminBinding;

import java.util.Calendar;

public class AdminAddMedicineActivity extends AppCompatActivity {
ActivityAdminAddMedicineBinding binding;
    StorageReference reference;
    FirebaseStorage storage;
    FirebaseDatabase database;
    Medicines medicines;
    String name, category , status, price , desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminAddMedicineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        medicines = new Medicines();





        binding.btnaddmedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          name = binding.etmedicineName.getText().toString();
          category = binding.etmedicinecategory.getText().toString();
         status = binding.etmedicinestatus.getText().toString();
          price= binding.etmedicineprice.getText().toString();
         desc = binding.etmedicinedescription.getText().toString();

             if(name.isEmpty()){
                 binding.etmedicineName.setError("Name Required");
                 return;
             }
                if(category.isEmpty()){
                    binding.etmedicinecategory.setError("Category Required");
                    return;
                }
                if(status.isEmpty()){
                    binding.etmedicinestatus.setError("Status Required");
                    return;
                }
                if(price.isEmpty()){
                    binding.etmedicineprice.setError("Price Required");
                    return;
                }
                if(desc.isEmpty()){
                    binding.etmedicinedescription.setError("Description Required");
                    return;
                }

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent , 42);


            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 42) {
            if (data != null) {
                Calendar calendar = Calendar.getInstance();
                Uri filepath = data.getData();
                reference = storage.getReference().child("ProductImage").child(category).child(name).child(calendar.getTimeInMillis() + "");
                reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                medicines.setCategory(category);
                                medicines.setImage(uri.toString());
                               medicines.setName(name);
                               medicines.setDescription(desc);
                               medicines.setStatus(status);
                               medicines.setPrice(price);
                               medicines.setQuantity("2");

                                String randomKey = database.getReference().push().getKey();
                                medicines.setId(randomKey);

                                database.getReference().child("Medicines").child(randomKey).setValue(medicines);
                                binding.etmedicinedescription.setText("");
                                binding.etmedicineprice.setText("");
                                binding.etmedicinestatus.setText("");
                                binding.etmedicinecategory.setText("");
                                binding.etmedicineName.setText("");
                                Toast.makeText(AdminAddMedicineActivity.this, "Operation Successfull...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


            }
        }
    }
}