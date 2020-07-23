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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.rentingapp.R;

public class SetTitleDialogFragment extends DialogFragment {
    EditText etTitle;
    Button btnSetTitle;

    public SetTitleDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public interface EditTitleDialogListener {
        void onFinishTitleDialog(String title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_set_title, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTitle = view.findViewById(R.id.etTitle);
//         Shows soft keyboard automatically and requests focus to field
        etTitle.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnSetTitle = view.findViewById(R.id.btnSetTitle);
        btnSetTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etTitle.getText().toString();
                if (!text.isEmpty()) {
                    EditTitleDialogListener listener = (EditTitleDialogListener) getTargetFragment();
                    listener.onFinishTitleDialog(text);
                    // Closes the dialog and returns back to the parent activity
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Title cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}