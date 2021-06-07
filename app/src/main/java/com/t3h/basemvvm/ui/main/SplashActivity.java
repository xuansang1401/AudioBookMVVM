package com.t3h.basemvvm.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.t3h.basemvvm.R;
import com.t3h.basemvvm.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent= new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },
                500);
    }

}