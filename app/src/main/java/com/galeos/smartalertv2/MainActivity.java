package com.galeos.smartalertv2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //A delay for the loading animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 2000);
    }
}