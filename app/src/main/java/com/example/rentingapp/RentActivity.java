package com.example.rentingapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.rentingapp.fragments.DatePickerFragment;
import com.example.rentingapp.models.Listing;

import org.parceler.Parcels;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class RentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    float TAX_RATE = 0.0725f;
    int price;

    Listing listing;

    Calendar calStart = Calendar.getInstance();
    Calendar calEnd = Calendar.getInstance();
    Boolean startClicked;

    Button btnClose;
    TextView tvListingName;
    TextView tvListingPrice;
    ImageView ivListingImage;
    TextView tvStartDate;
    TextView tvEndDate;
    TextView tvPriceXdays;
    TextView tvPriceXdaysTotal;
    TextView tvTaxes;
    TextView tvGrandTotal;
    Button btnRent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        listing = Parcels.unwrap(getIntent().getParcelableExtra("listing"));
        price = listing.getPrice();

        calEnd.add(Calendar.DAY_OF_YEAR, 1);

        btnClose = findViewById(R.id.btnClose);
        tvListingName = findViewById(R.id.tvListingName);
        tvListingPrice = findViewById(R.id.tvListingPrice);
        ivListingImage = findViewById(R.id.ivListingImage);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvPriceXdays = findViewById(R.id.tvPriceXdays);
        tvPriceXdaysTotal = findViewById(R.id.tvPriceXdaysTotal);
        tvTaxes = findViewById(R.id.tvTaxes);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        btnRent = findViewById(R.id.btnRent);

        tvListingName.setText(listing.getTitle());
        tvListingPrice.setText(String.format("$%d / day", listing.getPrice()));

        Glide.with(RentActivity.this)
                .load(listing.getImage().getUrl())
                .transform(new CenterCrop())
//                .transform(new MultiTransformation(new CenterCrop(), new RoundedCornersTransformation(15, 5)))
                .into(ivListingImage);

        updateTotals(1);

        tvStartDate.setText(String.format("%s %d", getMonthName(calStart.get(Calendar.MONTH)), calStart.get(Calendar.DAY_OF_MONTH)));
        tvEndDate.setText(String.format("%s %d", getMonthName(calEnd.get(Calendar.MONTH)), calEnd.get(Calendar.DAY_OF_MONTH)));

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startClicked = true;
                DatePickerFragment newFragment = new DatePickerFragment(calStart);
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startClicked = false;
                DatePickerFragment newFragment = new DatePickerFragment(calEnd, (calStart.getTimeInMillis() + TimeUnit.DAYS.toMillis(1)));
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });

        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calStart.after(calEnd) || calStart.equals(calEnd)) {
                    Toast.makeText(RentActivity.this, "Start date must be before end date!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String monthName = getMonthName(month);

        if (startClicked) {
            tvStartDate.setText(String.format("%s %d", monthName, day));
            calStart.set(Calendar.YEAR, year);
            calStart.set(Calendar.MONTH, month);
            calStart.set(Calendar.DAY_OF_MONTH, day);

            if (calStart.after(calEnd) || calStart.equals(calEnd)) {
                tvEndDate.setTextColor(getResources().getColor(R.color.errorRed));
            }
        } else {
            tvEndDate.setText(String.format("%s %d", monthName, day));
            calEnd.set(Calendar.YEAR, year);
            calEnd.set(Calendar.MONTH, month);
            calEnd.set(Calendar.DAY_OF_MONTH, day);

            if (tvEndDate.getCurrentTextColor() == getResources().getColor(R.color.errorRed)) {
                tvEndDate.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }

        if (calStart.before(calEnd)) {
            long diff = calEnd.getTimeInMillis() - calStart.getTimeInMillis();
            int numDays = (int) TimeUnit.MILLISECONDS.toDays(diff);
            updateTotals(numDays);
        }
    }

    private void updateTotals(int numDays) {
        String priceXdays;
        if (numDays == 1) {
            priceXdays = String.format("$%d  x  1 day", price);
        } else {
            priceXdays = String.format("$%d  x  %d days", price, numDays);
        }
        tvPriceXdays.setText(priceXdays);

        int subTotal = price * numDays;
        String priceXdaysTotal = String.format("$%d", subTotal);
        tvPriceXdaysTotal.setText(priceXdaysTotal);

        int tax = Math.round((float) subTotal * TAX_RATE);
        tvTaxes.setText(String.format("$%d", tax));

        int total = subTotal + tax;
        tvGrandTotal.setText(String.format("$%d", total));
    }

    private String getMonthName(int month) {
        return new DateFormatSymbols().getShortMonths()[month];
    }
}