package com.sublux.jayden.sublux;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class results extends AppCompatActivity {

    String imageURI = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent i = getIntent();
        //System.out.println(i.getStringExtra("imageURI")); //Testing
        this.imageURI = i.getStringExtra("imageURI"); //Set Image URI

        //Temporary Bubble Text
        Toast toast = Toast.makeText(this, imageURI, Toast.LENGTH_LONG);
        toast.show(); //Show Bubble Text
    }


    /**
     * Method to Analyze an image, probably pixel by pixel. We shall see.
     */
    public void analyzeImage(){


    }





}
