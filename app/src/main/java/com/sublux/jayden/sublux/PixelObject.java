package com.sublux.jayden.sublux;

/**
 * A Pixel Object to store information about individual pixels.
 * @author Jayden Weaver
 */

public class PixelObject {

    int red = 0, green = 0, blue = 0, color = 0; //Initialize variables.

    public PixelObject(int r, int g, int b, int c) {
        //Set RGB values.
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    //Returns the color value
    public int getColor(){
        return this.color;
    }

    //Returns Red Value
    public int getRed(){
        return this.red;
    }
    //Returns Green Value
    public int getGreen(){
        return this.green;
    }

    //Returns Blue Value
    public int getBlue(){
        return this.blue;
    }

    //Sets the red value, returns if value was successfully set.
    public boolean setRed(int r){
        this.red = r;
        return this.red == r;
    }

    //Sets the green value, returns if value was successfully set.
    public boolean setGreen(int g){
        this.green = g;
        return this.green == g;
    }

    //Sets the blue value, returns if value was successfully set.
    public boolean setBlue(int b){
        this.blue = b;
        return this.blue == b;
    }

    //Sets the color value, returns if value was successfully set.
    public boolean setColor(int c){
        this.color = c;
        return this.color == c;
    }
}
