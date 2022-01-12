package com.uniquesolutions.carepharmacy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.uniquesolutions.carepharmacy.Activities.OrderActivity;
import com.uniquesolutions.carepharmacy.Adapters.AdapterCatg;
import com.uniquesolutions.carepharmacy.InterfaceSetTitle;
import com.uniquesolutions.carepharmacy.MainActivity;
import com.uniquesolutions.carepharmacy.Models.Devices;
import com.uniquesolutions.carepharmacy.Models.Medicines;
import com.uniquesolutions.carepharmacy.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    LinearLayout linearLayoutShop, linearLayoutProduct;
    ImageView imageItemShop;
    TextView tvItemNameShop;
    TextView prescription_order;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ArrayList<Medicines> list = new ArrayList<>();
    ArrayList<Devices> deviceList = new ArrayList<>();
    ShimmerRecyclerView rvCatg;
    ShimmerRecyclerView rvdevices;
    AdapterCatgry medicinesAdpater;
    AdapterDevices adapterDevices;

    private static String gotomedicine = "";


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageSlider imageSlider = view.findViewById(R.id.slider);
        List<SlideModel> slideModels = new ArrayList<>();


        slideModels.add(new SlideModel(R.drawable.med, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.med2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.med3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.med4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.cap, ScaleTypes.FIT));


        imageSlider.setImageList(slideModels);


        rvCatg = view.findViewById(R.id.rec_home_category);
        rvdevices = view.findViewById(R.id.rec_home_device);


        adapterDevices = new AdapterDevices(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);

        rvdevices.setLayoutManager(layoutManager);

        //   rvCatg.setAdapter(medicinesAdpater);

        rvdevices.showShimmerAdapter();

        //  linearLayoutProduct = view.findViewById(R.id.linear_layout_product);

        prescription_order = view.findViewById(R.id.prescription_order);


        prescription_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new PrescOrderFragment()).addToBackStack(null).commit();

            }
        });






        database = FirebaseDatabase.getInstance();

        database.getReference().child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Medicines medicines = snapshot1.getValue(Medicines.class);
                        list.add(medicines);

                    }

                    medicinesAdpater.updateMedicineList(list);


                    rvCatg.setAdapter(medicinesAdpater);

                    rvCatg.hideShimmerAdapter();

                    medicinesAdpater.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "empty", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("Devices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    deviceList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Devices devices= snapshot1.getValue(Devices.class);
                        deviceList.add(devices);
                    }
                    adapterDevices.updateMedicineList(deviceList);
                    rvdevices.setAdapter(adapterDevices);
                    rvdevices.hideShimmerAdapter();
                    adapterDevices.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "empty", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        loadMedicineCategory();


        return view;

    }



    private void loadMedicineCategory() {

        medicinesAdpater = new AdapterCatgry(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);


        rvCatg.setLayoutManager(layoutManager);

        //   rvCatg.setAdapter(medicinesAdpater);

        rvCatg.showShimmerAdapter();
    }


//
//    public void loadProduct() {
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        int i;
//        for (i = 0; i < imagesProduct.length; i++) {
//            View view = inflater.inflate(R.layout.item_product, linearLayoutProduct, false);
//            ImageView imageProduct = view.findViewById(R.id.image_product);
//            TextView tvName = view.findViewById(R.id.tv_name_product);
//            TextView tvpils = view.findViewById(R.id.tv_pills_product);
//            TextView tvFinalPrice = view.findViewById(R.id.tv_final_price);
//            TextView tvprice = view.findViewById(R.id.tv_price_product);
//
//            imageProduct.setImageResource(imagesProduct[i]);
//            tvName.setText(namesItemsProduct[i]);
//            tvpils.setText(pilsItemsProduct[i]);
//            tvFinalPrice.setText(finalPriceProduct[i]);
//            tvprice.setText(PriceProduct[i]);
//
//
//            linearLayoutProduct.addView(view);
//        }
//
//    }
//
//
//    public void loadShop() {
//     //   Toast.makeText(getContext(), "load shop", Toast.LENGTH_SHORT).show();
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//
//        int i;
//        for (i = 0; i < list.size(); i++) {
//            View view = inflater.inflate(R.layout.item_shop, linearLayoutShop, false);
//            imageItemShop = view.findViewById(R.id.image_item_shop);
//            tvItemNameShop = view.findViewById(R.id.tv_item_name_shop);
//
//            Medicines medicines = list.get(i);
//
//            Glide.with(getContext()).load(medicines.getImage()).placeholder(R.drawable.amoxil).into(imageItemShop);
//
//            tvItemNameShop.setText(medicines.getCategory());
//
//            final int aux = i;
//            imageItemShop.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String itemSeleccinoda = medicines.getCategory();
//                    Toast.makeText(getContext(), itemSeleccinoda, Toast.LENGTH_SHORT).show();
//                    //Put the value
//                    MedicineFragment fragment = new MedicineFragment();
//                    Bundle args = new Bundle();
//                    args.putString("category", medicines.getCategory());
//                    fragment.setArguments(args);
//                    gotomedicine =medicines.getCategory();
//
//
////Inflate the fragment
//                 //   getFragmentManager().beginTransaction().add(R.id.container, ldf).commit();
//
//                    getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
//                            .addToBackStack(null).commit();
//                }
//            });
//
//            linearLayoutShop.addView(view);
//        }
//    }

    public static String getGotomedicine() {
        return gotomedicine;
    }

    public static void setGotomedicine(String gotomedicine) {
        HomeFragment.gotomedicine = gotomedicine;
    }


    public class AdapterCatgry extends RecyclerView.Adapter<AdapterCatgry.ViewHolder> {
        Context context;
        ArrayList<Medicines> list;

        public AdapterCatgry(Context context, ArrayList<Medicines> list) {
            this.context = context;
            this.list = list;
        }

        public AdapterCatgry(Context context) {
            this.context = context;
            list = new ArrayList<>();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            Log.v("TAG" ,"Home Fragment :on Create View()");

            return viewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Medicines medicines = list.get(position);


            Glide.with(context).load(medicines.getImage()).placeholder(R.drawable.amoxil).into(holder.catgimage);
            holder.catgname.setText(medicines.getCategory());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String itemSeleccinoda = medicines.getCategory();
                    Toast.makeText(context, itemSeleccinoda, Toast.LENGTH_SHORT).show();
                    //Put the value
                    MedicineFragment fragment = new MedicineFragment();
                    Bundle args = new Bundle();
                    args.putString("category", medicines.getCategory());
                    fragment.setArguments(args);
                    HomeFragment.setGotomedicine(medicines.getCategory());
                    getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack(null).commit();
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void updateMedicineList(ArrayList<Medicines> data) {
            list.clear();
            list = data;

        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView catgimage;
            TextView catgname;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                catgimage = itemView.findViewById(R.id.image_item_shop);
                catgname = itemView.findViewById(R.id.tv_item_name_shop);


            }
        }


    }


    public class AdapterDevices extends RecyclerView.Adapter<AdapterDevices.ViewHolder> {
        Context context;
        ArrayList<Devices> list;

        public AdapterDevices(Context context, ArrayList<Devices> list) {
            this.context = context;
            this.list = list;
        }

        public AdapterDevices(Context context) {
            this.context = context;
            list = new ArrayList<>();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);


            return viewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Devices medicines = list.get(position);


            Glide.with(context).load(medicines.getImage()).placeholder(R.drawable.stetoscope).into(holder.deviceimage);
            holder.devicename.setText(medicines.getName());
            holder.devicestatus.setText(medicines.getStatus());
            holder.deviceprice.setText("Rs :"+medicines.getPrice());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundled = new Bundle();
                    bundled.putString("deviceid", medicines.getId());
                    bundled.putString("devicename", medicines.getName());
                    bundled.putString("deviceprice", medicines.getPrice());
                    bundled.putString("deviceimage", medicines.getImage());
                    bundled.putString("devicestatus", medicines.getStatus());


                    Intent intent = new Intent(context, OrderActivity.class);
                    intent.putExtra("BundleDevicedata", bundled);
                    intent.putExtra("message" , "Devices");


                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void updateMedicineList(ArrayList<Devices> data) {
            list.clear();
            list = data;

        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView deviceimage;
            TextView devicename, devicestatus, deviceprice;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                deviceimage = itemView.findViewById(R.id.image_product);
                devicename = itemView.findViewById(R.id.tv_name_product);
                devicestatus = itemView.findViewById(R.id.tv_device_status);
                deviceprice = itemView.findViewById(R.id.tv_device_price);


            }
        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.v("TAG" ,"Home Fragment :on Attach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("TAG" ,"Home Fragment :on Create()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("TAG" ,"Home Fragment :on Activity Created()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("TAG" ,"Home Fragment :on start()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("TAG" ,"Home Fragment :on Resume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("TAG" ,"Home Fragment :on Pause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("TAG" ,"Home Fragment :on Stop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("TAG" ,"Home Fragment :on Destroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v("TAG" ,"Home Fragment :on DestroyView()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v("TAG" ,"Home Fragment :on Detach()");
    }
}