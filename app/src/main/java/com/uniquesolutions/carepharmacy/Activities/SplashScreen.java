package com.uniquesolutions.carepharmacy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.uniquesolutions.carepharmacy.R;

import static java.lang.Thread.sleep;

public class SplashScreen extends AppCompatActivity {
    ProgressBar pb;
    int i = 0;
    Handler hdlr=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pb = findViewById(R.id.pb);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                i = pb.getProgress();
                while (i < 100) {
                    i += 1;
                    hdlr.post(new Runnable() {
                        @Override
                        public void run() {
                            pb.setProgress(i);
                        }
                    });
                    try {
                        sleep(20);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (i == 100) {
                            Intent intent = new Intent(SplashScreen.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        });
        thread.start();
    }
}