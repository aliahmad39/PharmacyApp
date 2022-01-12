package com.uniquesolutions.carepharmacy.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uniquesolutions.carepharmacy.MainActivity;
import com.uniquesolutions.carepharmacy.Models.Medicines;
import com.uniquesolutions.carepharmacy.R;
import com.uniquesolutions.carepharmacy.databinding.ActivityOrderBinding;

public class OrderActivity extends AppCompatActivity {
        ActivityOrderBinding binding;
        StorageReference reference;
        FirebaseStorage storage;
        FirebaseDatabase database;
        Medicines medicines;
        String name, category, status, desc , image;
        String id;
        String id2;
        int qty = 1;
        int price , price2;
        AlertDialog dialog;
        public static String cart ="";
    String randomkey;
    String result=new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Upload Order");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        id2 = firebaseUser.getUid();

        database = FirebaseDatabase.getInstance();

        binding.tvquantity.setText(qty + "");
        // price = price * qty;

        Intent intent = getIntent();
        result = intent.getStringExtra("message");


        if (result.equals("Devices")) {
            Bundle bundle1 = getIntent().getBundleExtra("BundleDevicedata");
            name = bundle1.getString("devicename");
            status = bundle1.getString("devicestatus");
            price = Integer.parseInt(bundle1.getString("deviceprice"));
            id = bundle1.getString("deviceid");
            image = bundle1.getString("deviceimage");

            Toast.makeText(this, "i m in devices", Toast.LENGTH_LONG).show();


            Glide.with(OrderActivity.this).load(image).placeholder(R.drawable.stetoscope)
                    .into(binding.ivproduct);
            // Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

            binding.tvMedicineName.setText(name);
            binding.productDetail.setText(status);
            binding.detail.setText("Product Status");
            binding.productDetailPrice.setText("Rs:" + price);

            binding.btnAddtoCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    price2 = price * qty;


                    randomkey = database.getReference().push().getKey();

                    medicines = new Medicines(id2,  name, price2 + "",  qty + "", image, status, randomkey);


                    database.getReference().child("Cart").child(id2).child(randomkey).setValue(medicines);


                    Intent intent = new Intent(OrderActivity.this, CartActivity.class);

                    startActivity(intent);
                    finish();
                    Toast.makeText(OrderActivity.this, "Successfully Add to basket", Toast.LENGTH_SHORT).show();
                }
            });

            return;
        }
        else if(result.equals("medicines")){
            Bundle bundle = intent.getBundleExtra("Bundledata");

            name = bundle.getString("medicinename");
            category = bundle.getString("category");
            status = bundle.getString("status");
            price = Integer.parseInt(bundle.getString("medicineprice"));
            desc = bundle.getString("medicinedescription");
            id = bundle.getString("medicineid");
            image = bundle.getString("medicineimage");


            Glide.with(OrderActivity.this).load(image).placeholder(R.drawable.vitamin)
                    .into(binding.ivproduct);
            // Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

            binding.tvMedicineName.setText(name);
            binding.productDetail.setText(desc);
            binding.detail.setText("Product Detail");
            binding.productDetailPrice.setText("Rs:" + price);

            binding.btnAddtoCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    price2 = price * qty;

//                Bundle bundle1 = new Bundle();
//                bundle1.putString("category", category);
//                bundle1.putString("medicineid", id);
//                bundle1.putString("medicinename", name);
//                bundle1.putInt("medicineprice", price2);
//                bundle1.putInt("medicineqty", qty);
//                bundle1.putString("medicinedescription",desc );
//                bundle1.putString("medicineimage", image);
//                bundle1.putString("status", status);
                    randomkey = database.getReference().push().getKey();

                    medicines = new Medicines(id2, category, name, price2 + "", desc, qty + "", image, status, randomkey);


                    database.getReference().child("Cart").child(id2).child(randomkey).setValue(medicines);

//                storage = FirebaseStorage.getInstance();
//                reference = storage.getReference().child("Cart").child(category).child(id);
//
//                reference.putFile()


                    Intent intent = new Intent(OrderActivity.this, CartActivity.class);
                    // intent.putExtra("CartData", bundle1);
                    //  cart = "passdata";
                    startActivity(intent);
                    finish();
                    Toast.makeText(OrderActivity.this, "Successfully Add to basket", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    public void AddQuantity(View view) {

        qty += 1;


        binding.tvquantity.setText(qty + "");
        binding.productDetailPrice.setText("Rs:" + price * qty);
    }

    public void SubQuantity(View view) {
        if (qty > 1)
            qty -= 1;

        binding.tvquantity.setText(qty + "");
        binding.productDetailPrice.setText("Rs:" + price * qty);
    }

    @Override
    public boolean onSupportNavigateUp() {

        startActivity(new Intent(OrderActivity.this, MainActivity.class));
        finishAffinity();
        return super.onSupportNavigateUp();
    }
}