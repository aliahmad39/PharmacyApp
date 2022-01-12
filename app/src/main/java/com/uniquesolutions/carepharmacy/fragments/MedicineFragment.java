package com.uniquesolutions.carepharmacy.fragments;


import android.content.Context;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.uniquesolutions.carepharmacy.Adapters.MedicinesList;

import com.uniquesolutions.carepharmacy.Models.Medicines;
import com.uniquesolutions.carepharmacy.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class MedicineFragment extends Fragment {
    View view;
    String category = "";
    ArrayList<Medicines> medicines = new ArrayList<>();
    FirebaseDatabase database;
    RecyclerView rec_medicine;
    MedicinesList Adapter;
    EditText searchdata;

    private ImageView iv_mic;
    private TextView tvNothing;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    public MedicineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_medicine, container, false);

        // category  = getArguments().getString("category" , null);
        Log.v("TAG" ,"Medicine Fragment :on CreateView()");
        searchdata = view.findViewById(R.id.et_search);


        searchdata.setText("");

        category = HomeFragment.getGotomedicine();
        database = FirebaseDatabase.getInstance();

        rec_medicine = view.findViewById(R.id.recycler_medicines_list);
        tvNothing = view.findViewById(R.id.tv_list);

        rec_medicine.setLayoutManager(new LinearLayoutManager(getContext()));



        if (category.isEmpty()) {
            Adapter = new MedicinesList(getContext(), medicines , tvNothing , rec_medicine);
            rec_medicine.setAdapter(Adapter);
            database.getReference().child("Medicines").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Medicines medicines2 = dataSnapshot.getValue(Medicines.class);
                            medicines.add(medicines2);
                        }
                           Adapter.updateMedicineList(medicines);
                        Adapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        else {
            Adapter = new MedicinesList(getContext(), medicines,tvNothing , rec_medicine);
            rec_medicine.setAdapter(Adapter);
            HomeFragment.setGotomedicine("");
            //    Toast.makeText(getContext(), category + " hello", Toast.LENGTH_SHORT).show();
            database.getReference().child("Medicines").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Medicines medicines2 = dataSnapshot.getValue(Medicines.class);
                            if (medicines2.getCategory().equals(category)) {
                                medicines.add(medicines2);
                            }

                        }

                        Adapter.updateMedicineList(medicines);
//                        rec_medicine.setAdapter(Adapter);
                        Adapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        searchdata.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            //    Adapter.updateMedicineList(medicines);
                if(s.length()>0)
                Adapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        iv_mic = view.findViewById(R.id.iv_mic);

        iv_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchdata.setText("");
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e) {
                    Toast.makeText(getContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        if(context instanceof InterfaceMedicineFrgment){
//            interfaceMedicineFrgment = (InterfaceMedicineFrgment) context;
//        }else{
//            throw new RuntimeException(
//                    context.toString() +
//                            "must impliment Home fragment listener"
//            );
//        }
        Log.v("TAG" ,"Medicine Fragment :on Attach()");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                searchdata.setText(Objects.requireNonNull(result).get(0));
                searchdata.setSelection(searchdata.getText().length());
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("TAG" ,"Medicine Fragment :on Create()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("TAG" ,"Medicine Fragment :on Activity Created()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("TAG" ,"Medicine Fragment :on Resume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("TAG" ,"Medicine Fragment :on Pause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("TAG" ,"Medicine Fragment :on Stop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("TAG" ,"Medicine Fragment :on Destroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v("TAG" ,"Medicine Fragment :on DestroyView()");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v("TAG" ,"Medicine Fragment :on Detach()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("TAG" ,"Medicine Fragment :on Start()");
    }

}