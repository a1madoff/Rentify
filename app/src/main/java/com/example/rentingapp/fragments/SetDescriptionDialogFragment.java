package com.example.rentingapp.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.rentingapp.R;

public class SetDescriptionDialogFragment extends DialogFragment {
    EditText etDescription;
    Button btnSetDescription;

    public SetDescriptionDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public interface EditDescriptionDialogListener {
        void onFinishDescriptionDialog(String description);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_set_description, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDescription = view.findViewById(R.id.etDescription);
//         Shows soft keyboard automatically and requests focus to field
        etDescription.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnSetDescription = view.findViewById(R.id.btnSetDescription);
        btnSetDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDescriptionDialogListener listener = (EditDescriptionDialogListener) getTargetFragment();
                listener.onFinishDescriptionDialog(etDescription.getText().toString());
                // Closes the dialog and returns back to the parent activity
                dismiss();
            }
        });
    }
}