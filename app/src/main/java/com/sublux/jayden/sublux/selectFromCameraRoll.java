package com.sublux.jayden.sublux;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class selectFromCameraRoll extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_from_camera_roll);
        //Image View Button
        Button imageSelectButton = (Button) findViewById(R.id.imageSelectButton);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        //Image Analysis Button
        final Button analysisButton = (Button) findViewById(R.id.analysisButton);
        analysisButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //do stuff
                analysisButton.setText("Analyzing...");
            }
        });



    }


    private void analyzeImage(){

    }


    //Open a Gallery View
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

}

