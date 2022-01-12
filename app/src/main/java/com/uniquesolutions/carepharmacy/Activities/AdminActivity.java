package com.uniquesolutions.carepharmacy.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uniquesolutions.carepharmacy.Models.Medicines;
import com.uniquesolutions.carepharmacy.R;
import com.uniquesolutions.carepharmacy.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {
ActivityAdminBinding binding;
StorageReference reference;
FirebaseStorage storage;
FirebaseDatabase database;
String catgName;
Medicines medicines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        medicines = new Medicines();

        reference = storage.getReference().child("Categories");

        binding.btnUploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catgName =binding.etCategoryName.getText().toString();
                if(catgName.isEmpty()){
                    binding.etCategoryName.setError("plz write Category name");
                    return;
                }

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent , 50);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 50) {
            if (data != null) {

                Uri filepath = data.getData();

                reference.child(catgName).putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   reference.child(catgName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           medicines.setCategory(catgName);
                           medicines.setImage(uri.toString());

                           String randomKey = database.getReference().push().getKey();
                           medicines.setId(randomKey);

                           database.getReference().child("Categories").child(randomKey).setValue(medicines);
                           binding.etCategoryName.setText("");
                           Toast.makeText(AdminActivity.this, "Operation Successfull...", Toast.LENGTH_SHORT).show();
                       }
                   });
                    }
                });


            }
        }
    }
}