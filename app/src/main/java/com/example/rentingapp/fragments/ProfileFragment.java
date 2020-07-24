package com.example.rentingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rentingapp.LoginActivity;
import com.example.rentingapp.R;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {
    TextView tvCurrUserFullName;
    Button btnLogout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates the layout for the fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCurrUserFullName = view.findViewById(R.id.tvCurrUserFullName);
        btnLogout = view.findViewById(R.id.btnLogout);

        ParseUser currentUser = null;
        try {
            currentUser = ParseUser.getCurrentUser().fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String fullName = String.format("%s %s", currentUser.getString("firstName"), currentUser.getString("lastName"));
        tvCurrUserFullName.setText(fullName);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
