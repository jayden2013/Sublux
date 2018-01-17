package com.sublux.jayden.sublux;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class results extends AppCompatActivity {

    String imageURI = "";
    int width = 0, height = 0;
    Bitmap result;

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
        System.out.println(path);
            Bitmap bmp = BitmapFactory.decodeFile(path + "/2.jpg"); //hardcoded the filepath until i get image analysis perfected.
            final int OFFSET = 45;
            width = bmp.getWidth(); //Set Width;
            height = bmp.getWidth(); //Set Height;
            int x = 0, y = 0; //Initialize X + Y
            int totalPixels = width * height; //Calculate Total Pixel
            PixelObject pixel; //Pixel object for storing values.
            ArrayList<PixelObject> pixelArray = new ArrayList<PixelObject>(); //ArrayList for storing Pixels
            int backgroundColor = bmp.getPixel(0,0); //Set the background color to be the first pixel for analysis.
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            ImageView modifiedImageView = (ImageView) findViewById(R.id.resultView);
            PixelObject bgPixel = new PixelObject(Color.red(bmp.getPixel(x,y)), Color.green(bmp.getPixel(x,y)), Color.blue(bmp.getPixel(x,y)), bmp.getPixel(x,y));

            //Analyze pixel by pixel
            while (y < height) { //Change height value when testing to something more reasonable, so that we don't run out of memory.
                while (x < width) {
                   pixel = new PixelObject(Color.red(bmp.getPixel(x,y)), Color.green(bmp.getPixel(x,y)), Color.blue(bmp.getPixel(x,y)), bmp.getPixel(x,y)); //Create new pixel object using values.

                    if (pixel.getRed() > bgPixel.getRed() - OFFSET && pixel.getRed() < bgPixel.getRed() + OFFSET){
                        result.setPixel(x,y, Color.BLACK);
                    }
                    else if (pixel.getGreen() > bgPixel.getGreen() - OFFSET && pixel.getGreen() < bgPixel.getGreen() + OFFSET){
                        result.setPixel(x,y, Color.BLACK);
                    }
                    else if (pixel.getBlue() > bgPixel.getBlue() - OFFSET && pixel.getBlue() < bgPixel.getBlue() + OFFSET){
                        result.setPixel(x,y, Color.BLACK);
                    }
                    else{
                        result.setPixel(x,y, Color.BLUE);
                    }

                    x++;
                }
                y++;
                x = 0;
            }

            modifiedImageView.setImageBitmap(result); //Set the modifiedImageView to the resulting bitmap.
            analyzeHead();

    }

    /**
     * Analyze head posture.
     * @return
     */
    public int analyzeHead(){

        //do stuff

        return 0; //Return an offset maybe of how offset the posture is?
    }


}
