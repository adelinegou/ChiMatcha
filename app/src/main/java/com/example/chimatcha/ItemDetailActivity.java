package com.example.chimatcha;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class ItemDetailActivity extends AppCompatActivity {

    private ImageView ivDetailImage, btnBack;
    private TextView tvDetailTitle, tvDetailPrice, tvDetailDesc;
    private AutoCompleteTextView spinnerIce, spinnerSweet;
    private TextInputEditText etQuantity, etNotes;
    private MaterialButton btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        btnBack = findViewById(R.id.btn_back);
        ivDetailImage = findViewById(R.id.iv_detail_image);
        tvDetailTitle = findViewById(R.id.tv_detail_title);
        tvDetailPrice = findViewById(R.id.tv_detail_price);
        tvDetailDesc = findViewById(R.id.tv_detail_desc);
        spinnerIce = findViewById(R.id.spinner_ice);
        spinnerSweet = findViewById(R.id.spinner_sweet);
        etQuantity = findViewById(R.id.et_quantity);
        etNotes = findViewById(R.id.et_notes);
        btnPlaceOrder = findViewById(R.id.btn_place_order);

        Intent incomingIntent = getIntent();
        String title = incomingIntent.getStringExtra("PRODUCT_TITLE");
        String extraInfo = incomingIntent.getStringExtra("PRODUCT_EXTRA");
        int price = incomingIntent.getIntExtra("PRODUCT_PRICE", 0);
        String imageName = incomingIntent.getStringExtra("PRODUCT_IMAGE_NAME");
        String fullDescription = incomingIntent.getStringExtra("PRODUCT_DESC");

        tvDetailTitle.setText(title != null ? title : "Matcha Drink");
        tvDetailPrice.setText("Rp " + String.format("%,d", price));
        tvDetailDesc.setText(fullDescription != null && !fullDescription.isEmpty() ? fullDescription : extraInfo);

        if (imageName != null) {
            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (resId != 0) {
                ivDetailImage.setImageResource(resId);
            }
        }

        setupDropdownMenus();

        btnBack.setOnClickListener(v -> handleBackNavigation());
        btnPlaceOrder.setOnClickListener(v -> processFormValidation());
    }

    private void setupDropdownMenus() {
        String[] iceLevels = {"No Ice", "Less Ice", "Normal Ice", "Extra Ice"};
        ArrayAdapter<String> iceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, iceLevels);
        spinnerIce.setAdapter(iceAdapter);

        String[] sweetLevels = {"No Sugar", "Less Sweet", "Normal Sweet", "Extra Sweet"};
        ArrayAdapter<String> sweetAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, sweetLevels);
        spinnerSweet.setAdapter(sweetAdapter);
    }

    private void processFormValidation() {
        String quantityRaw = etQuantity.getText().toString().trim();
        int quantity = 0;

        try {
            if (!quantityRaw.isEmpty()) {
                quantity = Integer.parseInt(quantityRaw);
            }
        } catch (NumberFormatException ignored) {}

        // Validation: Qty cannot be empty or 0
        if (quantityRaw.isEmpty() || quantity <= 0) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Invalid Quantity")
                    .setMessage("Please enter a valid amount. Quantity must be at least 1 or greater to place an order.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
            return;
        }

        // Success Message
        new MaterialAlertDialogBuilder(this)
                .setTitle("Order Confirmed")
                .setMessage("Thank you for your purchase! A verification and confirmation overview receipt email has been dispatched directly to your inbox.")
                .setCancelable(false)
                .setPositiveButton("Return to Menu", (dialog, which) -> {
                    dialog.dismiss();
                    handleBackNavigation();
                })
                .show();
    }

    private void handleBackNavigation() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        handleBackNavigation();
    }
}