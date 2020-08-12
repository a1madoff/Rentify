package com.example.rentingapp.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.rentingapp.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.parse.ParseGeoPoint;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

//import com.seatgeek.placesautocomplete.model.Place;

public class SetLocationDialogFragment extends DialogFragment {
    public static final String TAG = "SetLocationDialogFragment";

    EditText autocompleteSearchbox;
    Button btnCurrentLocation;
    Button btnSetLocation;

    ParseGeoPoint geoPoint = new ParseGeoPoint();
    String fullAddress;
    String locality;

    public SetLocationDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public interface EditLocationDialogListener {
        void onFinishLocationDialog(ParseGeoPoint geoPoint, String fullAddress, String locality);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_set_location, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCurrentLocation = view.findViewById(R.id.btnCurrentLocation);
        btnSetLocation = view.findViewById(R.id.btnSetLocation);

        String apiKey = "AIzaSyBH2jMbw3_qbD-P70ZarP8lDeQI-OLQEEs";
        // Initialize the SDK
        Places.initialize(getContext(), apiKey);

        // Create a new PlacesClient instance
        final PlacesClient placesClient = Places.createClient(getContext());

        // Initializes the AutocompleteSupportFragment.
        final AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specifies the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG));
        autocompleteFragment.setCountries("US");

        // Search button is R.id.places_autocomplete_search_button
        autocompleteSearchbox = autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);

        // Sets up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                autocompleteSearchbox.setPadding(0, autocompleteSearchbox.getPaddingTop(), 0, autocompleteSearchbox.getPaddingBottom());

                LatLng latLng = place.getLatLng();
                geoPoint.setLatitude(latLng.latitude);
                geoPoint.setLongitude(latLng.longitude);

                fullAddress = place.getAddress();
                fullAddress = fullAddress.substring(0, fullAddress.length() - 5);

                locality = place.getAddressComponents().asList().get(2).getName();

                Log.i(TAG, "Place: " + place.getName() + ", " + place.getAddress());
            }


            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                FusedLocationProviderClient locationClient = getFusedLocationProviderClient(getContext());
                locationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // GPS location can be null if GPS is switched off
                                if (location != null) {
                                    double latitude = location.getLatitude();
                                    geoPoint.setLatitude(latitude);
                                    double longitude = location.getLongitude();
                                    geoPoint.setLongitude(longitude);

                                    Log.i(TAG, "latitude = " + latitude + ", longitude = " + longitude);

                                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                    List<Address> matches = null;
                                    try {
                                        matches = geocoder.getFromLocation(latitude, longitude, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    fullAddress = matches.get(0).getAddressLine(0);
                                    fullAddress = fullAddress.substring(0, fullAddress.length() - 5);
                                    locality = matches.get(0).getLocality();

                                    autocompleteSearchbox.setPadding(0, autocompleteSearchbox.getPaddingTop(), 0, autocompleteSearchbox.getPaddingBottom());
                                    String shortAddress = fullAddress.split(",")[0];
                                    autocompleteSearchbox.setText(shortAddress);

                                    Log.i(TAG, fullAddress);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("SetLocationDialogFragment", "Error trying to get last GPS location");
                                e.printStackTrace();
                            }
                        });
            }
        });

        btnSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullAddress != null) {
                    EditLocationDialogListener listener = (EditLocationDialogListener) getTargetFragment();
                    listener.onFinishLocationDialog(geoPoint, fullAddress, locality);
                    // Closes the dialog and returns back to the parent activity
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Location cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {

        FragmentManager fm = getFragmentManager();

        Fragment xmlFragment = fm.findFragmentById(R.id.autocomplete_fragment);
        if (xmlFragment != null) {
            fm.beginTransaction().remove(xmlFragment).commit();
        }

        super.onDestroyView();
    }
}
