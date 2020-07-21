package com.example.rentingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class AddImageActivity extends AppCompatActivity {
    public static final String TAG = "AddImageActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public final static int PICK_PHOTO_CODE = 1046;

    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private Button btnLibrary;
    private ProgressBar pbLoading;

    private File photoFile;
    private String photoFileName = "photo.jpg";
    private byte[] bitmapdata;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        context = this;

        etDescription = findViewById(R.id.etDescription);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        ivPostImage = findViewById(R.id.ivPostImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnLibrary = findViewById(R.id.btnLibrary);
        pbLoading = findViewById(R.id.pbLoading);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        btnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPhoto();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbLoading.setVisibility(ProgressBar.VISIBLE);
                String description = etDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(context, "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ((photoFile == null && bitmapdata == null) || ivPostImage.getDrawable() == null) {
                    Toast.makeText(context, "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);
            }
        });
    }

    private void launchCamera() {
        // Creates Intent to take a picture and return control to the application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Creates File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // Wraps File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(context, "com.codepath.fileprovider.rentingapp", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            // Starts the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private void pickPhoto() {
        // Creates intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            // Brings up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Camera photo is on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // Loads the taken image into the preview
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(context, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();

            // Loads the image located at photoUri into selectedImage
            Bitmap selectedImage = loadFromUri(photoUri);
            // Loads the taken image into the preview
            ivPostImage.setImageBitmap(selectedImage);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            bitmapdata = bos.toByteArray();
        }
    }

    // Returns File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Gets safe storage directory for photos
        // Uses `getExternalFilesDir` on Context to access package-specific directories
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Creates the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Returns the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // Checks version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // Uses the new decodeBitmap method for newer versions of Android
                ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // Supports older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
//        Post post = new Post();
//        post.setDescription(description);
//        if (bitmapdata == null) {
//            post.setImage(new ParseFile(photoFile));
//        } else {
//            post.setImage(new ParseFile(bitmapdata));
//        }
//        post.setUser(currentUser);
//        post.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "Error while saving", e);
//                    Toast.makeText(context, "Error while saving!", Toast.LENGTH_SHORT).show();
//                }
//
//                Log.i(TAG, "Post save was successful!");
//                etDescription.setText("");
//                ivPostImage.setImageResource(0);
//                pbLoading.setVisibility(ProgressBar.INVISIBLE);
//            }
//        });
    }
}