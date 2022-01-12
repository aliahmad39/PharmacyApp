package com.uniquesolutions.carepharmacy.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uniquesolutions.carepharmacy.MainActivity;
import com.uniquesolutions.carepharmacy.Models.Medicines;
import com.uniquesolutions.carepharmacy.R;
import com.uniquesolutions.carepharmacy.fragments.HomeFragment;
import com.uniquesolutions.carepharmacy.fragments.MedicineFragment;

import java.util.ArrayList;

public class AdapterCatg extends RecyclerView.Adapter<AdapterCatg.ViewHolder> {
    Context context;
    ArrayList<Medicines> list;

    public AdapterCatg(Context context, ArrayList<Medicines> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false);

        return new ViewHolder(view);
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


                //.switchFragment(fragment , context , medicines.getCategory());


//Inflate the fragment
//                   getFragmentManager().beginTransaction().add(R.id.container, ldf).commit();
//
//                   context..beginTransaction().replace(R.id.nav_host_fragment, fragment)
//                            .addToBackStack(null).commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
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
