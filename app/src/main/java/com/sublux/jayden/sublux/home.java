package com.sublux.jayden.sublux;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button cameraRollButton = (Button) findViewById(R.id.cameraRollButton);
        cameraRollButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(home.this, selectFromCameraRoll.class);
                startActivity(i);
            }
        });


    }
}
