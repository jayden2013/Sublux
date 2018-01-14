package com.sublux.jayden.sublux;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;

public class results extends AppCompatActivity {

    String imageURI = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent i = getIntent();
        //System.out.println(i.getStringExtra("imageURI")); //Testing
        this.imageURI = i.getStringExtra("imageURI"); //Set Image URI

//        //Temporary Bubble Text
//        Toast toast = Toast.makeText(this, imageURI, Toast.LENGTH_LONG);
//        toast.show(); //Show Bubble Text
        analyzeImage();
    }


    /**
     * Method to Analyze an image, probably pixel by pixel. We shall see.
     */
    public void analyzeImage(){

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); //Directory is downloads, but will need to be changed in full implementation.

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        System.out.println("Current Permissions: " + permissionCheck); //Print current permissions to log in Android Studio.

        if (permissionCheck != 0) { //If we don't have permissions, request permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
            //Access file
            Bitmap bmp = BitmapFactory.decodeFile(path + "/1.jpeg");


            //Testing to see if we can display properties...
            Toast toastHeight = Toast.makeText(this, "" + bmp.getHeight(), Toast.LENGTH_LONG);
            toastHeight.show();
            //And yes, we can...


    }

}
