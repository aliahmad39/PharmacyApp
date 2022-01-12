package com.uniquesolutions.carepharmacy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uniquesolutions.carepharmacy.Activities.OrderActivity;
import com.uniquesolutions.carepharmacy.Models.Medicines;
import com.uniquesolutions.carepharmacy.R;
import com.uniquesolutions.carepharmacy.databinding.ItemMedicinesListBinding;


import java.util.ArrayList;
import java.util.List;

public class MedicinesList extends RecyclerView.Adapter<MedicinesList.MedicinesViewHolder> implements Filterable {
    Context context;

    ArrayList<Medicines> filteredData = new ArrayList<>();
    List<Medicines> backup = new ArrayList<>();
    ArrayList<Medicines> list = new ArrayList<>();
    View view;
    MedicinesViewHolder medicinesViewHolder;
    TextView tvNothing;
    RecyclerView rec;

    public MedicinesList(Context context, ArrayList<Medicines> list, TextView tvNothing , RecyclerView rec) {
        this.context = context;
         this.list.addAll(list);
        this.tvNothing = tvNothing;
        this.rec = rec;
    }

    @NonNull
    @Override
    public MedicinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(context).inflate(R.layout.item_medicines_list, parent, false);
        medicinesViewHolder = new MedicinesViewHolder(view);

        return medicinesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicinesViewHolder holder, int position) {
        Medicines medicines = list.get(position);

        Glide.with(context).load(medicines.getImage()).into(holder.binding.imageMedicineShop);
        // holder.binding.tvstatus.setText(medicines.);
        holder.binding.tvprice2.setText("Rs:" + medicines.getPrice());
        holder.binding.tvproductname.setText(medicines.getName() + "");
        holder.binding.tvstatus.setText(medicines.getStatus() + "");


        holder.binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Put the value
//                OrderFragment fragment = new OrderFragment();
                Bundle bundle = new Bundle();
                bundle.putString("category", medicines.getCategory());
                bundle.putString("medicineid", medicines.getId() + "");
                bundle.putString("medicinename", medicines.getName() + "");
                bundle.putString("medicineprice", medicines.getPrice() + "");
                bundle.putString("medicinedescription", medicines.getDescription() + "");
                bundle.putString("medicineimage", medicines.getImage() + "");
                bundle.putString("status", medicines.getStatus() + "");


//Inflate the fragment
                // .beginTransaction().re(R.id.container, ldf).commit();
//                ParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
//                       .addToBackStack(null).commit();
//
                Intent intent = new Intent(context, OrderActivity.class);
                intent.putExtra("Bundledata", bundle);
                intent.putExtra("message", "medicines");


                context.startActivity(intent);


            }


        });


    }

    public void updateMedicineList(ArrayList<Medicines> data) {
        backup = new ArrayList<>(data);
        list = new ArrayList<>(data);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MedicinesViewHolder extends RecyclerView.ViewHolder {
        ItemMedicinesListBinding binding;

        public MedicinesViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemMedicinesListBinding.bind(itemView);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    //Anonymous inner class
    Filter filter = new Filter() {
        @Override  //background thread
        protected FilterResults performFiltering(CharSequence keyword) {
            filteredData.clear();

            if (keyword.toString().isEmpty()) {
                filteredData.addAll(backup);
            } else {

                for (Medicines obj : backup) {
                    if (obj.getName().toLowerCase().contains(keyword.toString().toLowerCase())) {
                        filteredData.add(obj);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredData;
            return results;
        }

        @Override  //main UI thread
        protected void publishResults(CharSequence constraint, FilterResults results) {

            ArrayList<Medicines> filteredResult = new ArrayList<>((ArrayList<Medicines>) results.values);
            if(filteredResult.size() > 0){
                Toast.makeText(context, "filter size :"+filteredResult.size(), Toast.LENGTH_SHORT).show();
            rec.setVisibility(View.VISIBLE);
            tvNothing.setVisibility(View.GONE);
            list.clear();
            list.addAll(filteredResult);
            notifyDataSetChanged();
            }else{
                Toast.makeText(context, "filter else size :"+filteredResult.size(), Toast.LENGTH_SHORT).show();
                rec.setVisibility(View.GONE);
                tvNothing.setVisibility(View.VISIBLE);
            }
        }
    };








//    //Anonymous inner class
//    Filter filter = new Filter() {
//        @Override  //background thread
//        protected FilterResults performFiltering(CharSequence keyword) {
//            filteredData.clear();
//
//            if (keyword.toString().isEmpty()) {
//                filteredData.addAll(backup);
//            } else {
//
//                for (Medicines obj : backup) {
//                    if (obj.getName().toLowerCase().contains(keyword.toString().toLowerCase())) {
//                        filteredData.add(obj);
//                    }
//                }
//
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredData;
//            return results;
//        }
//
//        @Override  //main UI thread
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//
//            list.clear();
//            list.addAll((ArrayList<Medicines>) results.values);
//            notifyDataSetChanged();
//        }
//    };

//
//    //Anonymous inner class
//    Filter filter = new Filter() {
//        @Override  //background thread
//        protected FilterResults performFiltering(CharSequence keyword) {
//            filtereddata.clear();
//
//            if (keyword.length() == 0) {
//                highlightItems.clear();
//                //   filtereddata.addAll(Arrays.asList(MainActivity.list));
//                //  filtereddata.addAll(MainActivity.medicines);
//                filtereddata.addAll(backup);
//            } else {
//
//                for (Medicines obj : backup) {
//                    if (obj.getName().toLowerCase().contains(keyword.toString().toLowerCase())) {
//                        filtereddata.add(obj);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filtereddata;
//            return results;
//        }
//
//        @Override  //main UI thread
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            list.clear();
//            list.addAll((ArrayList<Medicines>) results.values);
//            notifyDataSetChanged();
//        }
//    };

}
