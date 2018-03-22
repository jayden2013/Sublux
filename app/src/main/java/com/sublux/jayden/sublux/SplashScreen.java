package com.sublux.jayden.sublux;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final int TIME_OUT = 10; //MILLISECONDS

        new Thread(new Runnable() {
            @Override
            public void run() {
                increaseProgressBar(TIME_OUT);
                Intent homeIntent = new Intent(SplashScreen.this, home.class);
                startActivity(homeIntent);
                finish();
            }
        }).start();

    }

    public void increaseProgressBar(int timeout) {
        ProgressBar pb = findViewById(R.id.progressBar);
        for (int i = 0; i <= 100; i++){
        try {
            Thread.sleep(timeout);
            pb.setProgress(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         }
    }

}
