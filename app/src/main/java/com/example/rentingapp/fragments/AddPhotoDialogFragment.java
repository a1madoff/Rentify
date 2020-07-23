package com.example.rentingapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.rentingapp.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class AddPhotoDialogFragment extends DialogFragment {
    public static final String TAG = "AddPhotoDialogFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public final static int PICK_PHOTO_CODE = 1046;

    Button btnSetPhoto;
    ImageView ivUploadImage;
    FloatingActionButton btnGallery;
    FloatingActionButton btnCamera;
    FloatingActionsMenu floatingMenu;

    private File photoFile;
    private String photoFileName = "photo.jpg";
    private byte[] bitmapdata;
    Bitmap photoBitmap;

    public AddPhotoDialogFragment() {
        // Required empty public constructor
    }

    public interface AddPhotoDialogListener {
        void onFinishPhotoDialog(Bitmap photoBitmap, ParseFile photoParseFile);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Inflates the layout for the fragment
        return inflater.inflate(R.layout.fragment_add_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSetPhoto = view.findViewById(R.id.btnSetPhoto);
        ivUploadImage = view.findViewById(R.id.ivUploadImage);
        btnGallery = view.findViewById(R.id.btnGallery);
        btnCamera = view.findViewById(R.id.btnCamera);
        floatingMenu = view.findViewById(R.id.floatingMenu);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPhoto();
            }
        });

        btnSetPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pbLoading.setVisibility(ProgressBar.VISIBLE);
                if ((photoFile == null && bitmapdata == null) || ivUploadImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                AddPhotoDialogListener listener = (AddPhotoDialogListener) getTargetFragment();

                ParseFile parseFile;
                if (bitmapdata == null) {
                    parseFile = new ParseFile(photoFile);
                } else {
                    parseFile = new ParseFile(bitmapdata);
                }

                listener.onFinishPhotoDialog(photoBitmap, parseFile);
                // Closes the dialog and returns back to the parent activity
                dismiss();
            }
        });
    }

    private void launchCamera() {
        // Creates Intent to take a picture and return control to the application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Creates File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // Wraps File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider.rentingapp", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Starts the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private void pickPhoto() {
        // Creates intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
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
                photoBitmap = takenImage;

                // Loads the taken image into the preview
                ivUploadImage.setImageTintList(null);
                ivUploadImage.setImageBitmap(takenImage);
                floatingMenu.collapseImmediately();
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();

            // Loads the image located at photoUri into selectedImage
            Bitmap selectedImage = loadFromUri(photoUri);
            photoBitmap = selectedImage;
            // Loads the taken image into the preview
            ivUploadImage.setImageTintList(null);
            ivUploadImage.setImageBitmap(selectedImage);
            floatingMenu.collapseImmediately();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            bitmapdata = bos.toByteArray();
        }
    }

    // Returns File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Gets safe storage directory for photos
        // Uses `getExternalFilesDir` on Context to access package-specific directories
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Creates the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Returns the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // Checks version of Android on device
            if (Build.VERSION.SDK_INT > 27) {
                // Uses the new decodeBitmap method for newer versions of Android
                ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // Supports older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
