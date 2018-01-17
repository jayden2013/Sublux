package com.sublux.jayden.sublux;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
    }
}
