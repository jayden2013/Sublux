package com.sublux.jayden.sublux;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class results extends AppCompatActivity {

    String imagePath = "";
    int width = 0, height = 0;
    Bitmap result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent i = getIntent();
        this.imagePath = i.getStringExtra("imagePath"); //Set Image Path
        analyzeImage();
    }

    /**
     * Method to Analyze an image, probably pixel by pixel. We shall see.
     */
    public void analyzeImage(){

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        System.out.println("Current Permissions: " + permissionCheck); //Print current permissions to log in Android Studio.

        if (permissionCheck != 0) { //If we don't have permissions, request permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
            //Access file
            Bitmap bmp = BitmapFactory.decodeFile(imagePath); //This is the image to be analyzed, now dynamic.
            final int OFFSET = 45; //Offset for RGB Threshold.
            width = bmp.getWidth(); //Set Width
            height = bmp.getWidth(); //Set Height
          //  height = 300; //For Testing
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
                    x++; //Increment column position.
                }
                y++; //Increment row position.
                x = 0; //New row, reset column position.
            }

      //      modifiedImageView.setImageBitmap(result); //Set the modifiedImageView to the resulting bitmap.
            result = cleanUp(result);
            modifiedImageView.setImageBitmap(result);
            System.out.println("displaying results");

            result = analyzeHead(result);
            System.out.println("analyzed head");
            modifiedImageView.setImageBitmap(result);

    }

    /**
     * Cleans up a bitmap, thinning out lines and shadows.
     * @param bmp
     * @return
     */
    public Bitmap cleanUp(Bitmap bmp){
        Bitmap cleaned = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        PixelObject pixel, nextPixel;
        boolean firstFound = false;
        int x = 0, y = 0;
        System.out.println("cleaning up");
        while(y < height){
            while (x < width){
                pixel = new PixelObject(Color.red(bmp.getPixel(x,y)), Color.green(bmp.getPixel(x,y)), Color.blue(bmp.getPixel(x,y)), bmp.getPixel(x,y)); //Create new pixel object using values.
                if (x < width - 1) {
                    nextPixel = new PixelObject(Color.red(bmp.getPixel(x + 1, y)), Color.green(bmp.getPixel(x + 1, y)), Color.blue(bmp.getPixel(x + 1, y)), bmp.getPixel(x + 1, y)); //Create new pixel object using values.
                }
                else{
                    nextPixel = pixel;
                }
                if (pixel.getBlue() != 255 && !firstFound){ //If the pixel is black and it's the first one found...
                    cleaned.setPixel(x, y, Color.BLACK);
                    firstFound = true;
                }
                else if (pixel.getBlue() != 255 && nextPixel.getBlue() == 255){ //If the pixel is black and the next pixel is blue...
                    cleaned.setPixel(x, y, Color.BLACK);
                }
                else{ //could remove this portion for more speed maybe.
                    cleaned.setPixel(x, y, Color.BLUE);
                }
                x++;
            }
            y++;
            x = 0;
            firstFound = false;
        }

        return cleaned;
    }

    /**
     * Analyze head posture.
     * @return
     */
    public Bitmap analyzeHead(Bitmap bmp){
        Bitmap headed = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        PixelObject pixel, nextPixel;
        boolean topHeadFound = false;
        int x = 0, y = 0;
        pixel = new PixelObject(Color.red(bmp.getPixel(x,y)), Color.green(bmp.getPixel(x,y)), Color.blue(bmp.getPixel(x,y)), bmp.getPixel(x,y)); //Create new pixel object using values.
        int topHeadX = 0, topHeadY = 0, centerMassX = 0;


        //Determine the coordinates of the top of the head. Also the center mass X coordinate.
        while (y < height){
            while (x < width){
                pixel = new PixelObject(Color.red(bmp.getPixel(x,y)), Color.green(bmp.getPixel(x,y)), Color.blue(bmp.getPixel(x,y)), bmp.getPixel(x,y)); //Create new pixel object using values.
                if (pixel.getBlue() != 255 && topHeadFound == false && y > 40 && x > width / 4) { //If the color is black, the top of the head hasn't been found yet, y is greater than 40 pixels, and x is greater than a quarter of the width...
                    topHeadFound = true;
                    topHeadX = x; //Get topHeadX value for analysis.
                    topHeadY = y; //Get topHeadY value for analysis.

                    while(x < width) { //This is all for testing.
                        headed.setPixel(x, y, Color.RED);
                        x++;
                    }
                }
                else if (pixel.getBlue() == 255){
                    headed.setPixel(x, y, Color.BLUE);
                }
                else{
                    headed.setPixel(x, y, Color.BLACK);
                }

                x++;
            }
            y++;
            x = 0;
        }
        centerMassX = topHeadX; //The center mass X coordinate and the top head X coordinate are the same. Using different variables for easier understanding.

        //Determine foot coordinate.

        y = height - 1;
        x = 0;

        boolean bottomYFound = false;
        int bottomY = 0;
        while (y > 0){
            while (x < width){
                pixel = new PixelObject(Color.red(bmp.getPixel(x,y)), Color.green(bmp.getPixel(x,y)), Color.blue(bmp.getPixel(x,y)), bmp.getPixel(x,y)); //Create new pixel object using values.
                if (pixel.getBlue() != 255 && bottomYFound == false) { //If the color is black, the top of the head hasn't been found yet, y is greater than 40 pixels, and x is greater than a quarter of the width...
                    bottomYFound = true;
                    bottomY = y;
                    System.out.println("Y coordinate:" + y);
                    while(x < width) { //This is all for testing.
                        headed.setPixel(x, y, Color.RED);
                        x++;
                    }
                }
                x++;
            }
            y--;
            x = 0;
        }


        //Determine center mass Y
        int centerMassY = (bottomY - topHeadY) /2;
        //This is all for testing:
//        System.out.println("CENTER MASS Y : " + centerMassY);
//        x = 0;
//        while (x < width){
//            headed.setPixel(x, centerMassY, Color.RED);
//            x++;
//        }

        //Find chin
        y = centerMassY;
        int bottomHeadY = 0;
        while (y > topHeadY){
            pixel = new PixelObject(Color.red(bmp.getPixel(centerMassX,y)), Color.green(bmp.getPixel(centerMassX,y)), Color.blue(bmp.getPixel(centerMassX,y)), bmp.getPixel(centerMassX,y)); //Create new pixel object using values.
            if (pixel.getBlue() != 255){
                bottomHeadY = y;
                //For Testing
//                System.out.println("Found Chin: " + y);
//                x = 0;
//                while (x < width){
//                headed.setPixel(x, bottomHeadY, Color.RED);
//                x++;
//                }
                break;
            }
            y--;
        }





        return headed; //Return an offset maybe of how offset the posture is?
    }

    /**
     * Displays the results of the image analysis with a toast.
     */
    public void displayResults(){
        //Temporary toast until full image analysis is complete.
        Toast resultToast = Toast.makeText(this, "oh noes bad posture", Toast.LENGTH_LONG);
        resultToast.show(); //Show Bubble Text
    }

}
