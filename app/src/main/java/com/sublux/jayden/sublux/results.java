package com.sublux.jayden.sublux;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class results extends AppCompatActivity {

    String imagePath = "";
    int width = 0, height = 0;
    Bitmap result;
    StringBuilder resultsString = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent i = getIntent();
        this.imagePath = i.getStringExtra("imagePath"); //Set Image Path
        analyzeImage();
        displayResults();
    }

    /**
     * Method to Analyze an image, probably pixel by pixel. We shall see.
     */
    public void analyzeImage(){

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != 0) { //If we don't have permissions, request permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        //Access file
        Bitmap bmp = BitmapFactory.decodeFile(imagePath); //This is the image to be analyzed, now dynamic.
        final int OFFSET = 45; //Offset for RGB Threshold. 50 doesn't seem to work for ears, but 45 does. 40 crashes.
        width = bmp.getWidth(); //Set Width
        height = bmp.getWidth(); //Set Height
        //  height = 300; //For Testing
        int x = 0, y = 0; //Initialize X + Y
        int totalPixels = width * height; //Calculate Total Pixel
        PixelObject pixel; //Pixel object for storing values.
        ArrayList<PixelObject> pixelArray = new ArrayList<PixelObject>(); //ArrayList for storing Pixels
        int backgroundColor = bmp.getPixel(0,0); //Set the background color to be the first pixel for analysis.
        result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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

        result = cleanUp(result);
        result = analyzeHead(result);

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
        final int SHOULDER_THRESHOLD = 10; //The acceptable threshold for shoulder comparisons. Anything outside of this will be considered bad posture.
        int shoulder_posture_value = 0;
        boolean leftShoulderHigher = false;
        int SHOULDER_TO_EAR_THRESHOLD = 15; //The acceptable threshold for shoulder to ear comparisons. Anything outside of this will be considered bad posture. Checks for tilted heads.
        int HIP_WIDTH_THRESHOLD = 25; //The acceptable threshold for centered hip comparisons.

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
        x = 0;
        while (x < width){
            headed.setPixel(x, centerMassY, Color.RED);
            x++;
        }

        //Find chin
        y = centerMassY;
        int bottomHeadY = 0;
        while (y > topHeadY){
            pixel = new PixelObject(Color.red(bmp.getPixel(centerMassX,y)), Color.green(bmp.getPixel(centerMassX,y)), Color.blue(bmp.getPixel(centerMassX,y)), bmp.getPixel(centerMassX,y)); //Create new pixel object using values.
            if (pixel.getBlue() != 255){
                bottomHeadY = y;
                //For Testing
                x = 0;
                while (x < width){
                    headed.setPixel(x, bottomHeadY, Color.RED);
                    x++;
                }
                break;
            }
            y--;
        }

        //Analyze Left Shoulder
        int shoulderLine = 0;
        shoulderLine = centerMassY - bottomHeadY;
        x = 0;
        while (x < width){
            headed.setPixel(x, shoulderLine, Color.WHITE);
            x++;
        }

        int shoulderWhereAbouts = bottomHeadY - shoulderLine;

        y = bottomHeadY;
        //Set x to x/4 of centerMassX. Can't do it in one operation for some reason.
        x = centerMassX / 4;
        x = centerMassX - x;

        Point leftShoulderPoint = new Point();
        while(y < shoulderLine){
            pixel = new PixelObject(Color.red(bmp.getPixel(x,y)), Color.green(bmp.getPixel(x,y)), Color.blue(bmp.getPixel(x,y)), bmp.getPixel(x,y)); //Create new pixel object using values.
            if (pixel.getBlue() != 255){ //If pixel is black
                break;
            }
            y++;
            if (y == shoulderLine){ //If we made it to the shoulder line without finding a suspected shoulder, restart with x - 1;
                y = bottomHeadY;
                x -= 1;
            }
        }

        leftShoulderPoint.set(x, y);
        headed.setPixel(leftShoulderPoint.x, leftShoulderPoint.y, Color.GREEN);

        //Find the right shoulder
        x = centerMassX / 4;
        x = centerMassX + x;

        Point rightShoulderPoint = new Point();
        while(y < shoulderLine){
            pixel = new PixelObject(Color.red(bmp.getPixel(x,y)), Color.green(bmp.getPixel(x,y)), Color.blue(bmp.getPixel(x,y)), bmp.getPixel(x,y)); //Create new pixel object using values.
            if (pixel.getBlue() != 255){ //If pixel is black
                break;
            }
            y++;
            if (y == shoulderLine){
                y = bottomHeadY;
                x += 1;
            }
        }

        rightShoulderPoint.set(x, y);

        headed.setPixel(rightShoulderPoint.x, rightShoulderPoint.y, Color.GREEN);

        String shoulderResultText = "No posture information available.";
        //Get shoulder posture value.
        shoulder_posture_value = Math.abs(leftShoulderPoint.y - rightShoulderPoint.y);
        //Check shoulder posture value against shoulder threshold.
        if (shoulder_posture_value > SHOULDER_THRESHOLD){
            leftShoulderHigher = leftShoulderPoint.y > rightShoulderPoint.y;
            shoulderResultText = "Shoulder posture is outside the range of acceptable values. ";
            if (leftShoulderHigher) {
                shoulderResultText += "Your left shoulder is higher than your right shoulder.\n";
            }
            else{
                shoulderResultText += "Your left shoulder is lower than your right shoulder.\n";
            }
        }
        else{
            shoulderResultText = "Your shoulder posture is looking great!\n";
        }

        resultsString.append(shoulderResultText);

        //Analyze head for tilt.
        //Get distance between shoulder points.
        int shoulderDistance = rightShoulderPoint.x - leftShoulderPoint.x;

        //Get ear Y
        int midHeadY = bottomHeadY - topHeadY;

        //Get Right Ear
        x = centerMassX;
        y = midHeadY;
        while (x < width) {
            pixel = new PixelObject(Color.red(bmp.getPixel(x, y)), Color.green(bmp.getPixel(x, y)), Color.blue(bmp.getPixel(x, y)), bmp.getPixel(x, y)); //Create new pixel object using values.
            if (pixel.getBlue() != 255){ //If pixel color is black.
                break;
            }
            x++;
            if (x == width){
                x = 0;
                y -= 1;
            }
        }
        Point rightEarPoint = new Point();
        rightEarPoint.set(x,y);

        //Get Left Ear
        x = leftShoulderPoint.x;
        y = midHeadY;
        while (x < centerMassX) {
            pixel = new PixelObject(Color.red(bmp.getPixel(x, y)), Color.green(bmp.getPixel(x, y)), Color.blue(bmp.getPixel(x, y)), bmp.getPixel(x, y)); //Create new pixel object using values.
            if (pixel.getBlue() != 255){ //If pixel color is black.
                break;
            }
            x++;
            if (x == centerMassX){
                x = leftShoulderPoint.x;
                y -= 1;
            }
        }

        Point leftEarPoint = new Point();
        leftEarPoint.set(x,y);

        headed.setPixel(rightEarPoint.x, rightEarPoint.y, Color.GREEN);
        headed.setPixel(leftEarPoint.x, leftEarPoint.y, Color.GREEN);

        //Check for tilted head.
        int leftShoulderToEar = leftEarPoint.x - leftShoulderPoint.x;
        int rightShoulderToEar = rightShoulderPoint.x - rightEarPoint.x;
        boolean head_tilted_left = false;
        String headTiltText = "No Posture Information Available.";
        if (Math.abs(leftShoulderToEar - rightShoulderToEar) > SHOULDER_TO_EAR_THRESHOLD){
            head_tilted_left = leftShoulderToEar < rightShoulderToEar;
            headTiltText = "Head tilt is outside the range of acceptable values. ";
            if (head_tilted_left) {
                headTiltText += "Your head is tilted to the left.\n";
            }
            else{
                headTiltText += "Your head is tilted to the right.\n";
            }
        }
        else{
            headTiltText = "Your head and neck are looking excellent!\n";
        }
        resultsString.append(headTiltText);

        //Analyze Waist.

        //Left Hip
        y = centerMassY;
        //Set x to x/4 of centerMassX. Can't do it in one operation for some reason.
        x = centerMassX / 4;
        x = centerMassX - x;

        while(x < centerMassX){
            pixel = new PixelObject(Color.red(bmp.getPixel(x, y)), Color.green(bmp.getPixel(x, y)), Color.blue(bmp.getPixel(x, y)), bmp.getPixel(x, y)); //Create new pixel object using values.

            if (pixel.getBlue() != 255){ //If the pixel is black.
                break;
            }

            if (x == 0){
                //Set x to x/4 of centerMassX. Can't do it in one operation for some reason.
                x = centerMassX / 4;
                x = centerMassX - x;
                y++; //Try again at new Y coordinate.
            }
            x++;
        }
        Point leftHipPoint = new Point();
        leftHipPoint.set(x,y);

        //Testing.
        headed.setPixel(leftHipPoint.x, leftHipPoint.y, Color.YELLOW);

        //Right hip
        y = centerMassY;
        x = centerMassX / 4;
        x = centerMassX + x;

        while (x > centerMassX){
            pixel = new PixelObject(Color.red(bmp.getPixel(x, y)), Color.green(bmp.getPixel(x, y)), Color.blue(bmp.getPixel(x, y)), bmp.getPixel(x, y)); //Create new pixel object using values.

            if (pixel.getBlue() != 255){ //If pixel is black.
                break;
            }

            if (x == 0){
                x = centerMassX / 4;
                x = centerMassX + x;
                y++;
            }
            x--;
        }

        Point rightHipPoint = new Point();
        rightHipPoint.set(x,y);

        //Testing
        headed.setPixel(rightHipPoint.x, rightHipPoint.y, Color.YELLOW);

        String hipText;
        //Check Hip Values
        if (Math.abs((rightHipPoint.x - centerMassX) - (centerMassX - leftHipPoint.x)) > HIP_WIDTH_THRESHOLD){
            hipText = "Hip values are outside of threshold.\n";
        }
        else{
            hipText = "Your hip posture is looking excellent!\n";
        }
        resultsString.append(hipText);

        return headed; //Return an offset maybe of how offset the posture is?
    }

    /**
     * Displays the results of the image analysis.
     */
    public void displayResults(){
        final ImageView modifiedImageView = (ImageView) findViewById(R.id.resultView);
        modifiedImageView.setImageBitmap(result);
        TextView resultsView = (TextView) findViewById(R.id.resultsView);
        resultsView.setText(resultsString.toString());
    }

}