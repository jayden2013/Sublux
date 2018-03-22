package com.sublux.jayden.sublux;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Date;

public class home extends AppCompatActivity {

    final int CAMERA_REQUEST = 1888;
    ImageView cameraView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button cameraRollButton = (Button) findViewById(R.id.cameraRollButton);
        Button takePhotoButton = (Button) findViewById(R.id.takePhotoButton);

        cameraRollButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(home.this, selectFromCameraRoll.class);
                startActivity(i);
            }
        });

        //THIS CODE FOR CAMERA SELECTION DOES NOT WORK.
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            ImageView cameraView = (ImageView) findViewById(R.id.cameraView);

            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

            protected void onActivityResult(int requestCode, int resultCode, Intent data){
                home.super.onActivityResult(requestCode, resultCode, data);
                if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    cameraView.setImageBitmap(photo);
                }

            }


        });

        //Checking the date for fun. Maybe to change the app theme based on the date/time.
        String date = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date()).substring(0,5);
        if (date.equals("1/1/1")){ //Check is weird, due to the way the string is substring'd. We'll still cash it tho.
            System.out.println("HAPPY NEW YEAR");
        } else if (date.equals("7/4/1")){
            System.out.println("HAPPY INDEPENDENCE DAY!");
        } else if (date.equals("10/31")) {
                System.out.println("SPOOKY");
        }
        else{
            System.out.println("NO HOLIDAYS :(");
        }
    }
}
