package com.uniquesolutions.carepharmacy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uniquesolutions.carepharmacy.Adapters.CartAdpter;
import com.uniquesolutions.carepharmacy.MainActivity;
import com.uniquesolutions.carepharmacy.Models.ManualOrders;
import com.uniquesolutions.carepharmacy.Models.Medicines;
import com.uniquesolutions.carepharmacy.Models.Users;
import com.uniquesolutions.carepharmacy.R;
import com.uniquesolutions.carepharmacy.databinding.ActivityCartBinding;
import com.uniquesolutions.carepharmacy.databinding.ActivityOrderBinding;
import com.uniquesolutions.carepharmacy.fragments.MedicineFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.uniquesolutions.carepharmacy.fragments.PrescOrderFragment.users;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    StorageReference reference;
    FirebaseStorage storage;
    FirebaseDatabase database;
    public Medicines medicines;
    String name, category, status, desc, image, price, qty;
    String id;
    public static CartAdpter cartAdpter;
    public static ArrayList<Medicines> list;
    ImageView productimage, ivdelete;
    FloatingActionButton confirmOrder;
    TextView tvname, tvprice, tvqty, tvstatus, tvcatg;

    FirebaseAuth auth;
    DatabaseReference userReference;




    String checkOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Shopping Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();


        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        id = firebaseUser.getUid();

       cartAdpter = new CartAdpter(CartActivity.this);
        binding.cartrecyclerview.setLayoutManager(new LinearLayoutManager(this));



        database.getReference().child("Cart").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        Medicines medicines = snapshot1.getValue(Medicines.class);
                        list.add(medicines);
                    }

                    cartAdpter.updateCart(list);
                    binding.cartrecyclerview.setAdapter(cartAdpter);
                    cartAdpter.notifyDataSetChanged();

                }else{
                    Toast.makeText(CartActivity.this, "Cart is Empty!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public static void removeCartItem(int i ){
        list.remove(i);
        cartAdpter.notifyItemRemoved(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();

        return super.onSupportNavigateUp();
    }

//    public void loadCart() {
//        //   Toast.makeText(getContext(), "load shop", Toast.LENGTH_SHORT).show();
//        LayoutInflater inflater = LayoutInflater.from(this);
//
//        int i;
//        for (i = 0; i < list.size(); i++) {
//            View view = inflater.inflate(R.layout.item_cart_product, binding.cartlayout, false);
//            productimage = view.findViewById(R.id.iv_cart);
//            tvname = view.findViewById(R.id.tv_cartname);
//            tvstatus = view.findViewById(R.id.tv_cartstatus);
//            tvqty = view.findViewById(R.id.tvcartqty);
//            tvprice = view.findViewById(R.id.tvcartprice);
//            tvcatg = view.findViewById(R.id.tv_cart_catg);
//            ivdelete = view.findViewById(R.id.ivcartdelete);
//            confirmOrder = view.findViewById(R.id.fab_cart);
//
//
//            medicines = list.get(i);
//
//            Glide.with(this).load(medicines.getImage()).placeholder(R.drawable.amoxil).into(productimage);
//
//            tvname.setText(medicines.getName());
//            tvstatus.setText(medicines.getStatus());
//            tvqty.setText(medicines.getQuantity());
//            tvprice.setText(medicines.getPrice());
//            tvcatg.setText(medicines.getCategory());
//
//            final int aux = i;
//            ivdelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new AlertDialog.Builder(CartActivity.this)
//                            .setTitle("Delete")
//                            .setIcon(R.drawable.ic_delete)
//                            .setMessage("Are you sure to delete this?")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    database.getReference().child("Cart").child(id).child(medicines.getCartId()).setValue(null);
//                                    Toast.makeText(CartActivity.this, "deleted Successfully...", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(CartActivity.this, MainActivity.class));
//                                    finish();
//                                }
//                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).show();
//
//
//                }
//            });
//
//            confirmOrder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    //    Toast.makeText(CartActivity.this, users.getUserName().toString(), Toast.LENGTH_SHORT).show();
//                    CreateDialogue();
//                }
//            });
//
//            binding.cartlayout.addView(view);
//        }
//    }

//    public void CreateDialogue() {
//        Toast.makeText(this, "hello how", Toast.LENGTH_SHORT).show();
//        final Dialog customdialog = new Dialog(CartActivity.this);
//        customdialog.setContentView(R.layout.alert_dialoge_custom);
//        EditText orderName = customdialog.findViewById(R.id.etUserNameDialog);
//        EditText orderEmail = customdialog.findViewById(R.id.etEmailDialog);
//        EditText orderPhn = customdialog.findViewById(R.id.etph_noDialog);
//        EditText orderAddress = customdialog.findViewById(R.id.etaddressDialog);
//        Button confirmOrder = customdialog.findViewById(R.id.btnConfirmOrderDialog);
//        Button cancelOrder = customdialog.findViewById(R.id.btnDismissDialog);
//        ImageView ivorder = customdialog.findViewById(R.id.ivOrder);
//        Calendar calendar = Calendar.getInstance();
//
//
//        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                   Users data = snapshot.getValue(Users.class);
//                    orderName.setText(data.getUserName() + "");
//                    orderPhn.setText(data.getPhnNo() + "");
//                    orderEmail.setText(data.getEmail() + "");
//                    orderAddress.setText(data.getAddress() + "");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        Glide.with(CartActivity.this).load(medicines.getImage()).placeholder(R.drawable.amoxil)
//                .into(ivorder);
//
//
//        cancelOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customdialog.dismiss();
//            }
//        });
//
//
//        confirmOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //     customdialog.dismiss();
//                //    Toast.makeText(getContext(), "Order Placed Successfully...", Toast.LENGTH_LONG).show();
//                if (orderName.getText().toString().isEmpty()) {
//                    orderName.setError("Enter Email*");
//                    return;
//                }
//                if (orderEmail.getText().toString().isEmpty()) {
//                    orderEmail.setError("Enter Password*");
//                    return;
//                }
//                if (orderPhn.getText().toString().isEmpty()) {
//                    orderPhn.setError("Enter UserName*");
//                    return;
//                }
//                if (orderAddress.getText().toString().isEmpty()) {
//                    orderAddress.setError("Enter Phone Number*");
//                    return;
//                }
//
//
//
//                ManualOrders manualOrders = new ManualOrders(orderName.getText().toString(),
//                        orderEmail.getText().toString(),
//                        orderPhn.getText().toString(),
//                        orderAddress.getText().toString(),
//                        medicines.getId(),
//                        medicines.getCategory(),
//                        medicines.getName(),
//                        medicines.getPrice(),
//                        medicines.getDescription(),
//                        medicines.getQuantity(),
//                        medicines.getImage(),
//                        medicines.getStatus(),
//                        new Date().getTime()
//
//                );
//
//                Toast.makeText(CartActivity.this, id, Toast.LENGTH_SHORT).show();
//                database.getReference().child("Orders").child("ManualOrders").child(id)
//                        .push().setValue(manualOrders);
//
//                customdialog.dismiss();
//
//                Toast.makeText(CartActivity.this, "Order Upload Successfully", Toast.LENGTH_SHORT).show();
//
//            }
//
//        });
//
//        customdialog.show();
//    }
}