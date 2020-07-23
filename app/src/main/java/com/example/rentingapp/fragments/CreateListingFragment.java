package com.example.rentingapp.fragments;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.rentingapp.R;
import com.example.rentingapp.models.Listing;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class CreateListingFragment extends Fragment implements
        SetTitleDialogFragment.EditTitleDialogListener, SetDescriptionDialogFragment.EditDescriptionDialogListener,
        AddPhotoDialogFragment.AddPhotoDialogListener, SetLocationDialogFragment.EditLocationDialogListener,
        SetPriceDialogFragment.EditPriceDialogListener {
    public static final String TAG = "CreateListingFragment";

    RelativeLayout layoutTitle;
    TextView tvTitle;
    ImageView checkboxTitle;

    RelativeLayout layoutDescription;
    TextView tvDescription;
    ImageView checkboxDescription;

    RelativeLayout layoutPhotos;
    ImageView ivListingPhoto;
    ImageView checkboxPhotos;

    RelativeLayout layoutLocation;
    TextView tvLocation;
    ImageView checkboxLocation;

    RelativeLayout layoutPrice;
    TextView tvPrice;
    ImageView checkboxPrice;

    Button btnCreate;

    // Listing fields
    int mPrice;
    ParseFile mImage;
    ParseGeoPoint mCoordinates;
    String mTitle;
    String mDescription;
    String mFullAddress;
    String mLocality;

    public CreateListingFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflates the layout for the fragment
        return inflater.inflate(R.layout.fragment_create_listing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: reset padding once title has been added
        // TODO: reopening set text box fills with already inputted text
        float scale = getResources().getDisplayMetrics().density;
        int paddingTop = (int) (40*scale + 0.5f);
        int paddingBottom = (int) (20*scale + 0.5f);

        // Title
        layoutTitle = view.findViewById(R.id.layoutTitle);
        layoutTitle.setPadding(0, paddingTop, 0, paddingBottom);
        tvTitle =  view.findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.GONE);
        checkboxTitle = view.findViewById(R.id.checkboxTitle);
        checkboxTitle.setVisibility(View.GONE);

        // Description
        layoutDescription = view.findViewById(R.id.layoutDescription);
        tvDescription =  view.findViewById(R.id.tvDescription);
        tvDescription.setVisibility(View.GONE);
        checkboxDescription = view.findViewById(R.id.checkboxDescription);
        checkboxDescription.setVisibility(View.GONE);

        // Photos
        layoutPhotos = view.findViewById(R.id.layoutPhotos);
        ivListingPhoto =  view.findViewById(R.id.ivListingPhoto);
        ivListingPhoto.setVisibility(View.GONE);
        checkboxPhotos = view.findViewById(R.id.checkboxPhotos);
        checkboxPhotos.setVisibility(View.GONE);

        // Location
        layoutLocation = view.findViewById(R.id.layoutLocation);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvLocation.setVisibility(View.GONE);
        checkboxLocation = view.findViewById(R.id.checkboxLocation);
        checkboxLocation.setVisibility(View.GONE);

        // Price
        layoutPrice = view.findViewById(R.id.layoutPrice);
        tvPrice =  view.findViewById(R.id.tvPrice);
        tvPrice.setVisibility(View.GONE);
        checkboxPrice = view.findViewById(R.id.checkboxPrice);
        checkboxPrice.setVisibility(View.GONE);

        btnCreate = view.findViewById(R.id.btnCreate);

        layoutTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                SetTitleDialogFragment setTitleFragment = new SetTitleDialogFragment();
                setTitleFragment.setTargetFragment(CreateListingFragment.this, 300);
                setTitleFragment.show(fm, "fragment_edit_name");
            }
        });

        layoutDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                SetDescriptionDialogFragment setDescriptionDialogFragment = new SetDescriptionDialogFragment();
                setDescriptionDialogFragment.setTargetFragment(CreateListingFragment.this, 300);
                setDescriptionDialogFragment.show(fm, "fragment_edit_name");
            }
        });

        layoutPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                AddPhotoDialogFragment addPhotoDialogFragment = new AddPhotoDialogFragment();
                addPhotoDialogFragment.setTargetFragment(CreateListingFragment.this, 300);
                addPhotoDialogFragment.show(fm, "fragment_edit_name");
            }
        });

        layoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                SetLocationDialogFragment setLocationDialogFragment = new SetLocationDialogFragment();
                setLocationDialogFragment.setTargetFragment(CreateListingFragment.this, 300);
                setLocationDialogFragment.show(fm, "fragment_edit_name");
            }
        });

        layoutPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                SetPriceDialogFragment setPriceDialogFragment = new SetPriceDialogFragment();
                setPriceDialogFragment.setTargetFragment(CreateListingFragment.this, 300);
                setPriceDialogFragment.show(fm, "fragment_edit_name");
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvTitle.getVisibility() == View.GONE || tvDescription.getVisibility() == View.GONE ||
                        ivListingPhoto.getVisibility() == View.GONE || tvLocation.getVisibility() == View.GONE ||
                        tvLocation.getVisibility() == View.GONE) {
                    Toast.makeText(getContext(), "All fields must filled!", Toast.LENGTH_SHORT).show();
                } else {
                    saveListing();
                }
            }
        });
    }

    @Override
    public void onFinishTitleDialog(String title) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        checkboxTitle.setVisibility(View.VISIBLE);

        mTitle = title;
    }

    @Override
    public void onFinishDescriptionDialog(String description) {
        tvDescription.setVisibility(View.VISIBLE);
        tvDescription.setText(description);
        checkboxDescription.setVisibility(View.VISIBLE);

        mDescription = description;
    }

    @Override
    public void onFinishPhotoDialog(Bitmap photoBitmap, ParseFile photoParseFile) {
        // TODO: photoParseFile will be used when saving listing
        ivListingPhoto.setVisibility(View.VISIBLE);
        ivListingPhoto.setImageBitmap(photoBitmap);
        checkboxPhotos.setVisibility(View.VISIBLE);

        mImage = photoParseFile;
    }

    @Override
    public void onFinishLocationDialog(ParseGeoPoint geoPoint, String fullAddress, String locality) {
        // TODO: geoPoint/locality will be used when saving listing
        tvLocation.setVisibility(View.VISIBLE);
        tvLocation.setText(fullAddress);
        checkboxLocation.setVisibility(View.VISIBLE);

        mCoordinates = geoPoint;
        mFullAddress = fullAddress;
        mLocality = locality;
    }

    @Override
    public void onFinishPriceDialog(String dollarSignedPrice) {
        tvPrice.setVisibility(View.VISIBLE);
        checkboxPrice.setVisibility(View.VISIBLE);

        String boldText = dollarSignedPrice;
        String normalText = " / day";
        SpannableString str = new SpannableString(boldText + normalText);
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPrice.setText(str);

        mPrice = Integer.parseInt(dollarSignedPrice.substring(1));
    }

    private void saveListing() {
        Listing listing = new Listing();

        listing.setPrice(mPrice);
        listing.setImage(mImage);
        listing.setCoordinates(mCoordinates);
        listing.setTitle(mTitle);
        listing.setDescription(mDescription);
        listing.setfullAddress(mFullAddress);
        listing.setLocality(mLocality);

        listing.setSeller(ParseUser.getCurrentUser());

        listing.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }

                Log.i(TAG, "Listing was saved successfully!");

                // TODO: return to view my listings page, also clear fields from fragment?
                // optional: add progress bar
            }
        });
    }
}
