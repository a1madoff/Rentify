package com.example.rentingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.rentingapp.models.Listing;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ListingDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    Listing listing;
    Context context;

    ImageView ivListingImage;
    TextView tvTitle;
    TextView tvLocation;
    TextView tvDescription;
    TextView tvPriceBottom;
    ImageView ivProfilePicture;
    TextView tvName;
    Button btnMessage;
    Button btnRent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.detailsMap);
        mapFragment.getMapAsync(this);

        context = this;
        listing = Parcels.unwrap(getIntent().getParcelableExtra("listing"));

        ivListingImage = findViewById(R.id.ivListingImage);
        tvTitle = findViewById(R.id.tvTitle);
        tvLocation = findViewById(R.id.tvLocation);
        tvDescription = findViewById(R.id.tvDescription);
        tvPriceBottom = findViewById(R.id.tvPriceBottom);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        tvName = findViewById(R.id.tvName);
        btnMessage = findViewById(R.id.btnMessage);
        btnRent = findViewById(R.id.btnRent);

        Glide.with(context)
                .load(listing.getImage().getUrl())
                .transform(new CenterCrop())
                .into(ivListingImage);

        tvTitle.setText(listing.getTitle());
        tvLocation.setText(listing.getLocality());
        tvDescription.setText(listing.getDescription());


        String boldText = String.format("$%d", listing.getPrice());
        String normalText = " / day";
        SpannableString str = new SpannableString(boldText + normalText);
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPriceBottom.setText(str);

        ParseFile profilePicture = listing.getSeller().getParseFile("profilePicture");
        if (profilePicture == null) {
            Glide.with(context)
                    .load(R.drawable.no_profile_picture)
                    .transform(new CircleCrop())
                    .into(ivProfilePicture);
        } else {
            Glide.with(context)
                    .load(profilePicture.getUrl())
                    .transform(new CircleCrop())
                    .into(ivProfilePicture);
        }

        tvName.setText(String.format("%s %s", listing.getSeller().getString("firstName"), listing.getSeller().getString("lastName")));

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("listing", Parcels.wrap(listing));
                context.startActivity(intent);
            }
        });

        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RentActivity.class);
                intent.putExtra("listing", Parcels.wrap(listing));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        IconGenerator iconGenerator = new IconGenerator(ListingDetailsActivity.this);
        iconGenerator.setStyle(IconGenerator.STYLE_WHITE);
        iconGenerator.setColor(ContextCompat.getColor(ListingDetailsActivity.this, R.color.colorAccent));
        Bitmap bitmap = iconGenerator.makeIcon("");
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
//        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);

        ParseGeoPoint geoPoint = listing.getCoordinates();
        LatLng latLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Marker in Sydney")
                .icon(icon));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}