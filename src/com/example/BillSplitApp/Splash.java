package com.example.BillSplitApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.main);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(Splash.this, Menu.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}