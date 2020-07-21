package com.example.rentingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.rentingapp.R;

public class CreateListingFragment extends Fragment implements SetTitleDialogFragment.EditTitleDialogListener {
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
    RelativeLayout layoutCurrentLocation;
    ImageView checkboxLocation;

    RelativeLayout layoutPrice;
    TextView tvPrice;
    ImageView checkboxPrice;

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

        // TODO: remember to reset padding once title has been added
        float scale = getResources().getDisplayMetrics().density;
        int paddingTop = (int) (30*scale + 0.5f);
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
        layoutCurrentLocation =  view.findViewById(R.id.layoutCurrentLocation);
        layoutCurrentLocation.setVisibility(View.GONE);
        checkboxLocation = view.findViewById(R.id.checkboxLocation);
        checkboxLocation.setVisibility(View.GONE);

        // Price
        layoutPrice = view.findViewById(R.id.layoutPrice);
        tvPrice =  view.findViewById(R.id.tvPrice);
        tvPrice.setVisibility(View.GONE);
        checkboxPrice = view.findViewById(R.id.checkboxPrice);
        checkboxPrice.setVisibility(View.GONE);

        layoutTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                SetTitleDialogFragment setTitleFragment = new SetTitleDialogFragment();
                setTitleFragment.setTargetFragment(CreateListingFragment.this, 300);
                setTitleFragment.show(fm, "fragment_edit_name");
            }
        });

    }

    @Override
    public void onFinishTitleDialog(String title) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        checkboxTitle.setVisibility(View.VISIBLE);
    }
}
