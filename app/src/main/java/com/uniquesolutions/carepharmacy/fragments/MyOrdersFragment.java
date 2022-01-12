package com.uniquesolutions.carepharmacy.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uniquesolutions.carepharmacy.InterfaceSetTitle;
import com.uniquesolutions.carepharmacy.Models.ManualOrders;
import com.uniquesolutions.carepharmacy.Models.Users;
import com.uniquesolutions.carepharmacy.R;
import com.uniquesolutions.carepharmacy.databinding.FragmentMyOrdersBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyOrdersFragment extends Fragment {
    FragmentMyOrdersBinding binding;
    DatabaseReference reference;
    DatabaseReference reference2;
    FirebaseDatabase database;
    int prescOrders = 0;
    int manualOrders = 0;
    int totalOrders = 0;
    String id;
    ProgressDialog dialog;
 //   InterfaceOrderFrgment interfaceOrderFrgment;


//    public interface InterfaceOrderFrgment {
//        void setOrderTitle(String title);
//    }


    public MyOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyOrdersBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        View view = binding.getRoot();

        Log.v("TAG", "MYOrder Fragment :on CreateView()");

   //     interfaceOrderFrgment.setOrderTitle("Orders");

        database = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        id = firebaseUser.getUid();

        //   Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait...");
        dialog.setTitle("Prescription Orders");
        binding.orderscount.setText(totalOrders + "");
        reference = database.getReference().child("Orders").child("PrescriptionOrders").child(id);
        reference2 = database.getReference().child("Orders").child("ManualOrders").child(id);

        binding.orderscount.setVisibility(View.GONE);
        binding.tvOrdersManualCount.setVisibility(View.GONE);

        binding.btnprescorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        dialog.dismiss();

                        //  totalOrders = String.valueOf(snapshot.getChildrenCount());
                        prescOrders = (int) snapshot.getChildrenCount();
                        binding.orderscount.setVisibility(View.VISIBLE);
                        binding.orderscount.setText(prescOrders + "");
                        binding.presclayout.removeAllViews();
                        Toast.makeText(getContext(), "Orders :" + prescOrders, Toast.LENGTH_SHORT).show();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                            Users users = snapshot1.getValue(Users.class);
                            View view = inflater.inflate(R.layout.sample_presc, binding.presclayout, false);

                            ImageView prescimage = view.findViewById(R.id.ivpresc);
                            TextView orderTime = view.findViewById(R.id.tvtime);

                            // String str = new String("2014-09-01 10:00:00.000");
                            //  String time = str.split("\\s")[1].split("\\.")[0];
                            //System.out.print(time);
                            // DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            // Date date = new Date();
                            // Timestamp ts=new Timestamp(messageModel.getTimestamp());
                            // Date date = new Date(messageModel.getTimestamp());
                            // String time = dateFormat.format(date);

                            // SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm a");

                            String dateString = formatter.format(users.getOrdertime());
                            orderTime.setText(dateString);

                            Glide.with(getContext()).load(users.getPrescImage())
                                    .placeholder(R.drawable.avatar)
                                    .into(prescimage);

                            binding.presclayout.addView(view);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        binding.btnmanualOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mannualOrders();
            }
        });

        mannualOrders();

        return view;
    }

    private void mannualOrders() {
        dialog.show();
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                dialog.dismiss();

                //  totalOrders = String.valueOf(snapshot.getChildrenCount());
                manualOrders = (int) snapshot.getChildrenCount();
                binding.tvOrdersManualCount.setVisibility(View.VISIBLE);
                binding.tvOrdersManualCount.setText(manualOrders + "");
                binding.presclayout.removeAllViews();
                Toast.makeText(getContext(), "Orders :" + manualOrders, Toast.LENGTH_SHORT).show();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    ManualOrders users = snapshot1.getValue(ManualOrders.class);
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.sample_manual_layout, binding.presclayout, false);

                    ImageView prescimage = view.findViewById(R.id.iv_order);
                    TextView orderTime = view.findViewById(R.id.tv_order_date);
                    TextView orderid = view.findViewById(R.id.tv_order_id);
                    TextView ordercatg = view.findViewById(R.id.tv_order_catg);
                    TextView ordername = view.findViewById(R.id.tv_order_name);
                    TextView orderqty = view.findViewById(R.id.tv_order_qty);
                    TextView orderprice = view.findViewById(R.id.tv_order_price);
                    TextView orderstatus = view.findViewById(R.id.tv_order_status);
                    TextView orderaddress = view.findViewById(R.id.tv_address);

                    // String str = new String("2014-09-01 10:00:00.000");
                    //  String time = str.split("\\s")[1].split("\\.")[0];
                    //System.out.print(time);
                    // DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    // Date date = new Date();
                    // Timestamp ts=new Timestamp(messageModel.getTimestamp());
                    // Date date = new Date(messageModel.getTimestamp());
                    // String time = dateFormat.format(date);

                    // SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm a");

                    String dateString = formatter.format(users.getOrdertime());

                    orderTime.setText(dateString);
                    orderid.setText(users.getMedId());
                    ordername.setText(users.getMedName());
                    ordercatg.setText(users.getMedctgr());
                    orderqty.setText(users.getMedQty());
                    orderprice.setText(users.getMedPrice());
                    orderstatus.setText(users.getMedStatus());
                    orderaddress.setText(users.getAddress());

                    Glide.with(getContext()).load(users.getMedImage())
                            .placeholder(R.drawable.avatar)
                            .into(prescimage);

                    binding.presclayout.addView(view);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (context instanceof InterfaceOrderFrgment) {
//            interfaceOrderFrgment = (InterfaceOrderFrgment) context;
//        } else {
//            throw new RuntimeException(
//                    context.toString() +
//                            "must impliment Home fragment listener"
//            );
//        }
//        Log.v("TAG", "MYOrder Fragment :on Attach()");
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("TAG", "MYOrder Fragment :on Create()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("TAG", "MYOrder Fragment :on Start()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("TAG", "MYOrder Fragment :on Activity Created()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("TAG", "MYOrder Fragment :on Resume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("TAG", "MYOrder Fragment :on Pause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("TAG", "MYOrder Fragment :on Stop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("TAG", "MYOrder Fragment :on Destroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.v("TAG", "MYOrder Fragment :on DestroyView()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v("TAG", "MYOrder Fragment :on Detach()");
    }
}