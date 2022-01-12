package com.uniquesolutions.carepharmacy.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.uniquesolutions.carepharmacy.InterfaceSetTitle;
import com.uniquesolutions.carepharmacy.MainActivity;
import com.uniquesolutions.carepharmacy.Models.Users;
import com.uniquesolutions.carepharmacy.R;
import com.uniquesolutions.carepharmacy.databinding.FragmentPrescOrderBinding;


import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class PrescOrderFragment extends Fragment {
    FragmentPrescOrderBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String id;
    DatabaseReference prescRef;
    FirebaseStorage storage;
    public static Users users;
    InterfaceSetTitle interfaceSetTitle;
    //  EditText orderName, orderEmail, orderPhn, orderAddress;
  //  InterfacePrescFragment interfacePrescFragment;


//    public interface InterfacePrescFragment {
//        void prescSetTitle(String title);
//    }

    public PrescOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPrescOrderBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        View view = binding.getRoot();
        Log.v("TAG", "Presc Order Fragment : onCreateView()");
       // interfacePrescFragment.prescSetTitle("Upload Prescription");


        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Uploading image...");
        dialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = auth.getCurrentUser();
        id = firebaseUser.getUid();

        storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance();

        prescRef = database.getReference().child("Orders").child("PrescriptionOrders").child(id);

        database.getReference().child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    users = snapshot.getValue(Users.class);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.ivgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 48);

            }
        });
        binding.ivcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "camera clicked", Toast.LENGTH_SHORT).show();
                Dexter.withContext(getContext())
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 101);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Calendar calendar = Calendar.getInstance();
        if (requestCode == 48) {
            if (data != null) {
                CreateDialogue(getContext(), data.getData(), 12, null);
//                if (data.getData() != null) {
//
//                    Uri selectedImage = data.getData();
//
//                    final StorageReference reference = storage.getReference().child("PrescriptionOrders").child(calendar.getTimeInMillis() + "");
//
//                    dialog.show();
//                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                            dialog.dismiss();
//                            if (task.isSuccessful()) {
//                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        String filePath = uri.toString();

//                                        users.setPrescImage(filePath);
//
//                                        prescRef.push().setValue(users);
//
//                                        Toast.makeText(getContext(), "Order Upload Successfully", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(getContext(), MainActivity.class));
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(getContext(), "failed " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//                            }
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getContext(), "Error " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
            }
        } else if (requestCode == 101 && resultCode == RESULT_OK) {
            CreateDialogue(getContext(), null, 40, data);
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//
//              Uri uri=Uri.parse(String.valueOf(bitmap));
//
//            byte bb[] = bytes.toByteArray();
//
//            //  StorageReference reference = mstorage.child("cameraImage").child(selectedImage.getLastPathSegment());
//            StorageReference reference = storage.getReference().child("PrescriptionOrders").child(calendar.getTimeInMillis() + "");
//            dialog.show();
//            reference.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    dialog.dismiss();
//
//                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            String filePath = uri.toString();
//                            users.setPrescImage(filePath);
//
//                            prescRef.push().setValue(users);
//
//                            Toast.makeText(getContext(), "Order Upload Successfully", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getContext(), MainActivity.class));
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getContext(), "failed " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getContext(), "Error :" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });


        }
    }

    private void CreateDialogue(Context context, Uri galleryImage, int number, Intent datacamera) {
        final Dialog customdialog = new Dialog(context);
        customdialog.setContentView(R.layout.alert_dialoge_custom);
        EditText orderName = customdialog.findViewById(R.id.etUserNameDialog);
        EditText orderEmail = customdialog.findViewById(R.id.etEmailDialog);
        EditText orderPhn = customdialog.findViewById(R.id.etph_noDialog);
        EditText orderAddress = customdialog.findViewById(R.id.etaddressDialog);
        TextView confirmOrder = customdialog.findViewById(R.id.btnConfirmOrderDialog);
        TextView cancelOrder = customdialog.findViewById(R.id.btnDismissDialog);
        ImageView ivorder = customdialog.findViewById(R.id.ivOrder);


        String name, phn, email, addr;
        final Users users1 = new Users();
        name = users.getUserName();
        phn = users.getPhnNo();
        email = users.getEmail();
        addr = users.getAddress();

        orderName.setText(name);
        orderPhn.setText(phn);
        orderEmail.setText(email);
        orderAddress.setText(addr);

        Calendar calendar = Calendar.getInstance();

//       String url = (String) datacamera.getExtras().get("data");

        if (number == 12) {
            ivorder.setImageURI(galleryImage);
        } else {
            Bitmap bitma = (Bitmap) datacamera.getExtras().get("data");
            ivorder.setImageBitmap(bitma);
        }


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

                users1.setUserName(orderName.getText().toString());
                users1.setEmail(orderEmail.getText().toString());
                users1.setPhnNo(orderPhn.getText().toString());
                users1.setAddress(orderAddress.getText().toString());
                //  users1.setOrdertime(new Date().getTime());
                users1.setOrdertime(new Date().getTime());

                if (number == 12) {
                    final StorageReference reference = storage.getReference().child("PrescriptionOrders").child(calendar.getTimeInMillis() + "");

                    dialog.show();
                    reference.putFile(galleryImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        ivorder.setImageURI(uri);
                                        String filePath = uri.toString();
                                        users1.setPrescImage(filePath);

                                        prescRef.push().setValue(users1);

                                        Toast.makeText(getContext(), "Order Upload Successfully", Toast.LENGTH_SHORT).show();
                                        customdialog.dismiss();
                                        //   startActivity(new Intent(getContext(), MainActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "failed " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                } else {

                    Bitmap bitmap = (Bitmap) datacamera.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    byte bb[] = bytes.toByteArray();

                    //  StorageReference reference = mstorage.child("cameraImage").child(selectedImage.getLastPathSegment());
                    StorageReference reference = storage.getReference().child("PrescriptionOrders").child(calendar.getTimeInMillis() + "");
                    dialog.show();
                    reference.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String filePath = uri.toString();
                                    users1.setPrescImage(filePath);

                                    prescRef.push().setValue(users1);

                                    Toast.makeText(getContext(), "Order Upload Successfully", Toast.LENGTH_SHORT).show();
                                    customdialog.dismiss();
                                    //   startActivity(new Intent(getContext(), MainActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "failed " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error :" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        customdialog.show();
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (context instanceof InterfacePrescFragment) {
//            interfacePrescFragment = (InterfacePrescFragment) context;
//        } else {
//            throw new RuntimeException(
//                    context.toString() +
//                            "must impliment Home fragment listener"
//            );
//        }
//        Log.v("TAG", "Presc Order Fragment :on Attach()");
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("TAG", "Presc Order Fragment :on Create()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("TAG", "Presc Order Fragment :on Activity Created()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("TAG", "Presc Order Fragment :on Resume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("TAG", "Presc Order Fragment :on Pause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("TAG", "Presc Order Fragment :on Stop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("TAG", "Presc Order Fragment :on Destroy()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("TAG", "Presc Order Fragment :on Start()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.v("TAG", "Presc Order Fragment :on DestroyView()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v("TAG", "Presc Order Fragment :on Detach()");
    }
}