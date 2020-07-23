package com.example.rentingapp.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
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

import com.example.rentingapp.R;

public class SetPriceDialogFragment extends DialogFragment {
    EditText etPrice;
    Button btnSetPrice;

    public SetPriceDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public interface EditPriceDialogListener {
        void onFinishPriceDialog(String dollarSignedPrice);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_set_price, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etPrice = view.findViewById(R.id.etPrice);
        etPrice.setText("$");
        Selection.setSelection(etPrice.getText(), etPrice.getText().length());
        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().startsWith("$")){
                    etPrice.setText("$");
                    Selection.setSelection(etPrice.getText(), etPrice.getText().length());

                }
            }
        });

//         Shows soft keyboard automatically and requests focus to field
        etPrice.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnSetPrice = view.findViewById(R.id.btnSetPrice);
        btnSetPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price = etPrice.getText().toString();
                if (!price.equals("$")) {
                    EditPriceDialogListener listener = (EditPriceDialogListener) getTargetFragment();
                    listener.onFinishPriceDialog(price);
                    // Closes the dialog and returns back to the parent activity
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Price cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
