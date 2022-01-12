package com.uniquesolutions.carepharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.uniquesolutions.carepharmacy.Activities.CartActivity;
import com.uniquesolutions.carepharmacy.Activities.ProfileActivity;
import com.uniquesolutions.carepharmacy.Activities.SignInActivity;
import com.uniquesolutions.carepharmacy.fragments.HomeFragment;
import com.uniquesolutions.carepharmacy.fragments.MedicineFragment;
import com.uniquesolutions.carepharmacy.fragments.MyOrdersFragment;
import com.uniquesolutions.carepharmacy.fragments.PrescOrderFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static int activityName = 0;
    DrawerLayout mNavDrawer;
    String title ="";
    String title2 ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(title.equals("")) {
            getSupportActionBar().setTitle("Home");
        }else{
            getSupportActionBar().setTitle(title);
        }
        Log.v("TAG" , "Main Activity : onCreate()");
        mNavDrawer = findViewById(R.id.drawer_layout);




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mNavDrawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mNavDrawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
            mNavDrawer.closeDrawer(GravityCompat.START);

            navigationView.setCheckedItem(R.id.homefragment);

        }

//switchTitle();

    }

    @Override
    public void onBackPressed() {
        if (mNavDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //    private void switchTitle() {
//        if(activityName == 0){
//            getSupportActionBar().setTitle("Home");
//            Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
//        }else if (activityName == 1){
//            getSupportActionBar().setTitle("Home");
//        }
//        else if (activityName == 2){
//            getSupportActionBar().setTitle("Medicine");
//            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
//        }else if (activityName == 3){
//            getSupportActionBar().setTitle("Order");
//        }else if (activityName == 4){
//            getSupportActionBar().setTitle("Upload Prescription");
//            Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.cart:
                Intent inten = new Intent(this, CartActivity.class);
                startActivity(inten);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    //
//    @Override
//    public void onBackPressed() {
//        if (mNavDrawer.isDrawerOpen(GravityCompat.START)) {
//            mNavDrawer.closeDrawer(GravityCompat.START);
//        } else {
//            if (getSupportActionBar().getTitle().toString() == "Home") {
//                super.onBackPressed();
//                finishAffinity();
//                return;
//            } else {
//
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//            }
//        }
//    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homefragment:
                getSupportActionBar().setTitle("Home");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).addToBackStack(null).commit();
                mNavDrawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.medicinefragment:
                getSupportActionBar().setTitle("Medicine");
                // switchTitle();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new MedicineFragment(), "Medicine").addToBackStack(null).commit();
                mNavDrawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.orderfragment:
                getSupportActionBar().setTitle("Orders");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new MyOrdersFragment()).addToBackStack(null).commit();
                mNavDrawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.prescripionorderfragment:
                getSupportActionBar().setTitle("Upload Prescription");
                // switchTitle();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new PrescOrderFragment()).addToBackStack(null).commit();
                mNavDrawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.Logout:
                mNavDrawer.closeDrawer(GravityCompat.START);
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);

                break;


        }
        mNavDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("TAG", "Main Activity : onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("TAG", "Main Activity : onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("TAG", "Main Activity : onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("TAG", "Main Activity : onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("TAG", "Main Activity : onDestroy()");
    }


}