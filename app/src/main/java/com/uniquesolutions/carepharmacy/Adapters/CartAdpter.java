package com.uniquesolutions.carepharmacy.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uniquesolutions.carepharmacy.Activities.CartActivity;
import com.uniquesolutions.carepharmacy.Activities.OrderActivity;
import com.uniquesolutions.carepharmacy.MainActivity;
import com.uniquesolutions.carepharmacy.Models.ManualOrders;
import com.uniquesolutions.carepharmacy.Models.Medicines;
import com.uniquesolutions.carepharmacy.Models.Users;
import com.uniquesolutions.carepharmacy.R;
import com.uniquesolutions.carepharmacy.databinding.SampleCartViewBinding;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CartAdpter extends RecyclerView.Adapter<CartAdpter.ViewHolder> {
    Context context;
    ArrayList<Medicines> list;
    View view;
    int qty = 1;
    int price;
    FirebaseDatabase database;
    Medicines medicines;
    Users user = new Users();
    ManualOrders manualOrders = new ManualOrders();

    public CartAdpter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
    }

    public CartAdpter(Context context, ArrayList<Medicines> list) {
        this.context = context;
        this.list = list;
        database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(context).inflate(R.layout.sample_cart_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        medicines = list.get(position);

        qty = Integer.parseInt(medicines.getQuantity());
        price = Integer.parseInt(medicines.getPrice());

        holder.binding.tvMedicineName.setText(medicines.getName());
        holder.binding.tvquantity.setText(medicines.getQuantity());
        holder.binding.productDetailPrice.setText("Rs " + medicines.getPrice());
        holder.binding.productDetail.setText(medicines.getDescription());
        holder.binding.tvMedicineStatus.setText(medicines.getStatus());


        holder.binding.ibadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddQuantity(holder);
            }
        });
        holder.binding.ibminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubQuantity(holder);
            }
        });


        holder.binding.btnConfirmCartOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialogue(position);
            }
        });

        holder.binding.btnCancelCartOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setIcon(R.drawable.ic_delete)
                        .setMessage("Are you sure to delete this?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("Cart").child(medicines.getId()).child(medicines.getCartId()).setValue(null);
                                Toast.makeText(context, "deleted Successfully...", Toast.LENGTH_SHORT).show();
                               CartActivity.removeCartItem(position);
                               // list.remove(position);
                              // notifyItemRemoved(position);
                             context.startActivity(new Intent(context , CartActivity.class));
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


    public void AddQuantity(ViewHolder holder) {
        qty += 1;

        holder.binding.tvquantity.setText(qty + "");
        holder.binding.productDetailPrice.setText("Rs:" + price * qty);
    }

    public void SubQuantity(ViewHolder holder) {
        if (qty > 1)
            qty -= 1;

        holder.binding.tvquantity.setText(qty + "");
        holder.binding.productDetailPrice.setText("Rs:" + price * qty);
    }

    public void updateCart(ArrayList<Medicines> data) {
        list.clear();
        list = data;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        SampleCartViewBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = SampleCartViewBinding.bind(itemView);
        }
    }

    private void CreateDialogue(int position) {
        final Dialog customdialog = new Dialog(context);

        customdialog.setContentView(R.layout.alert_dialoge_custom );

        EditText orderName = customdialog.findViewById(R.id.etUserNameDialog);
        EditText orderEmail = customdialog.findViewById(R.id.etEmailDialog);
        EditText orderPhn = customdialog.findViewById(R.id.etph_noDialog);
        EditText orderAddress = customdialog.findViewById(R.id.etaddressDialog);
        TextView confirmOrder = customdialog.findViewById(R.id.btnConfirmOrderDialog);
        TextView cancelOrder = customdialog.findViewById(R.id.btnDismissDialog);
        ImageView ivorder = customdialog.findViewById(R.id.ivOrder);


        database.getReference().child("Users").child(medicines.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(Users.class);
                orderName.setText(user.getUserName());
                orderPhn.setText(user.getPhnNo());
                orderEmail.setText(user.getEmail());
                orderAddress.setText(user.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Glide.with(context).load(medicines.getImage()).placeholder(R.drawable.amoxil).into(ivorder);



        Calendar calendar = Calendar.getInstance();


        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customdialog.dismiss();
            }
        });


        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     customdialog.dismiss();
                //    Toast.makeText(getContext(), "Order Placed Successfully...", Toast.LENGTH_LONG).show();
                if (orderName.getText().toString().isEmpty()) {
                    orderName.setError("Enter Email*");
                    return;
                }
                if (orderEmail.getText().toString().isEmpty()) {
                    orderEmail.setError("Enter Password*");
                    return;
                }
                if (orderPhn.getText().toString().isEmpty()) {
                    orderPhn.setError("Enter UserName*");
                    return;
                }
                if (orderAddress.getText().toString().isEmpty()) {
                    orderAddress.setError("Enter Phone Number*");
                    return;
                }


                manualOrders.setAddress(orderAddress.getText().toString());
                manualOrders.setEmail(orderEmail.getText().toString());
                manualOrders.setPhnNo(orderPhn.getText().toString());
                manualOrders.setUserName(orderName.getText().toString());
                manualOrders.setMedDsc(medicines.getDescription());
                manualOrders.setMedName(medicines.getName());
                manualOrders.setMedImage(medicines.getImage());
                manualOrders.setMedctgr(medicines.getCategory());
                manualOrders.setMedPrice(price * qty +"");
                manualOrders.setMedQty(qty+"");
                manualOrders.setMedDsc(medicines.getStatus());
                manualOrders.setOrdertime(new Date().getTime());


              String result = UploadOrder();

                if(!result.isEmpty()){
                    Toast.makeText(context, "Order Uploaded Successfully...", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("Cart").child(medicines.getId()).child(medicines.getCartId()).setValue(null);
                    CartActivity.removeCartItem(position);
                    context.startActivity(new Intent(context , CartActivity.class));

                }


                customdialog.dismiss();
            }
        });

        customdialog.show();
    }
    public String UploadOrder(){
        String result = "Successfull";
        database.getReference().child("Orders").child("ManualOrders")
                .child(medicines.getId()).push().setValue(manualOrders);
        return result;
    }
}
