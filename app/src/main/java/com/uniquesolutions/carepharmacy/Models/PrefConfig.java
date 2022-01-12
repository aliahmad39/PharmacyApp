package com.uniquesolutions.carepharmacy.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.uniquesolutions.carepharmacy.R;

public class PrefConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status){
     SharedPreferences.Editor editor= sharedPreferences.edit();
     editor.putBoolean(context.getString(R.string.pref_login_status) , status);
     editor.commit();
    }
    public boolean readLoginStatus(){
      return sharedPreferences.getBoolean(context.getString(R.string.pref_login_status),false);
    }
    public void writeRememberme(boolean status){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_rememberme_status) , status);
        editor.commit();
    }
    public boolean readRemberme(){
        return sharedPreferences.getBoolean(context.getString(R.string.pref_rememberme_status),false);
    }
    public void wrireName(String name){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_name),name);
        editor.commit();

    }
    public String readName(){
        return sharedPreferences.getString(context.getString(R.string.pref_user_name),"User");
    }
    public void wrirePassword(String password){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_password),password);
        editor.commit();

    }
    public String readPassword(){
        return sharedPreferences.getString(context.getString(R.string.pref_user_password),"");
    }


    public void displayToast(String message){

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
