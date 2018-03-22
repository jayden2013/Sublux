package com.sublux.jayden.sublux;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
                if (imageUri != null){
                    //do stuff
                    analysisButton.setText("Analyzing...");
                    displayResults();
                } //If there isn't an image selected, then prevent the app from crashing and change the text color to alert the user.
                else{
                    TextView txt = (TextView) findViewById(R.id.instructionText);
                    if (txt.getCurrentTextColor() == Color.parseColor("#E1315B")){
                        txt.setTextColor(Color.parseColor("#FFEC5C"));
                    }
                    else{
                        txt.setTextColor(Color.parseColor("#E1315B"));
                    }

                }

            }
        });
    }

    private void displayResults(){
        Intent results = new Intent(selectFromCameraRoll.this, results.class);
        results.putExtra("imagePath", getImagePath(imageUri)); //Put Extra to access selected image on following screen.
        startActivity(results);
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
            imageUri = data.getData(); //Get imageUri
            imageView.setImageURI(imageUri); //Set imageView to selected image.
        }
    }

    /**
     * https://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework
     * @param uri
     * @return
     */
//    public String getImagePath(Uri uri){
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
//        cursor.close();
//
//        cursor = getContentResolver().query(
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//
//        return path;
//    }


    /**
     * https://stackoverflow.com/questions/20324155/get-filepath-and-filename-of-selected-gallery-image-in-android
     * @param uri
     * @return
     */
    public String getImagePath(Uri uri){
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}

