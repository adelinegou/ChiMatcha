package com.example.chimatcha;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class HomeActivity extends AppCompatActivity {

    private TextView tvGreeting, tvViewAllItems, tvViewAllBranches;
    private ViewFlipper viewFlipper;
    private ImageButton btnPrev, btnNext;
    private LinearLayout llFeaturedItems, llFeaturedBranches;

    // Dropdown Variables
    private ImageView btnTopMenu;
    private View dropdownOverlay;
    private CardView cvDropdownMenu;
    private LinearLayout dropHome, dropItems, dropBranch, dropLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvGreeting = findViewById(R.id.tv_greeting);
        viewFlipper = findViewById(R.id.view_flipper);
        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);
        tvViewAllItems = findViewById(R.id.tv_view_all_items);
        tvViewAllBranches = findViewById(R.id.tv_view_all_branches);
        llFeaturedItems = findViewById(R.id.ll_featured_items);
        llFeaturedBranches = findViewById(R.id.ll_featured_branches);

        btnTopMenu = findViewById(R.id.btn_top_menu);
        dropdownOverlay = findViewById(R.id.dropdown_overlay);
        cvDropdownMenu = findViewById(R.id.cv_dropdown_menu);
        dropHome = findViewById(R.id.drop_home);
        dropItems = findViewById(R.id.drop_items);
        dropBranch = findViewById(R.id.drop_branch);
        dropLogout = findViewById(R.id.drop_logout);

        // Dynamic Global Username
        android.content.SharedPreferences globalVar = getSharedPreferences("ChiMatchGlobal", MODE_PRIVATE);
        String passedUsername = getIntent().getStringExtra("REGISTERED_USERNAME");

        if (passedUsername != null && !passedUsername.trim().isEmpty()) {
            globalVar.edit().putString("SAVED_USERNAME", passedUsername).apply();
            tvGreeting.setText(passedUsername);
        } else {
            String savedName = globalVar.getString("SAVED_USERNAME", "Matcha Lover");
            tvGreeting.setText(savedName);
        }

        setupDropdownMenu();
        setupCarousel();
        loadDatabaseData();
        setupViewAllButtons();
    }

    private void setupDropdownMenu() {
        btnTopMenu.setOnClickListener(v -> {
            cvDropdownMenu.setVisibility(View.VISIBLE);
            dropdownOverlay.setVisibility(View.VISIBLE);
        });

        dropdownOverlay.setOnClickListener(v -> hideDropdown());

        dropHome.setOnClickListener(v -> hideDropdown()); // Already on Home

        dropItems.setOnClickListener(v -> {
            hideDropdown();
            Intent intent = new Intent(HomeActivity.this, ItemActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        dropBranch.setOnClickListener(v -> {
            hideDropdown();
            Intent intent = new Intent(HomeActivity.this, BranchActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        dropLogout.setOnClickListener(v -> {
            hideDropdown();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }

    private void hideDropdown() {
        cvDropdownMenu.setVisibility(View.GONE);
        dropdownOverlay.setVisibility(View.GONE);
    }

    private void setupCarousel() {
        viewFlipper.setInAnimation(this, android.R.anim.fade_in);
        viewFlipper.setOutAnimation(this, android.R.anim.fade_out);

        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        btnNext.setOnClickListener(v -> {
            viewFlipper.showNext();
            viewFlipper.startFlipping();
        });

        btnPrev.setOnClickListener(v -> {
            viewFlipper.showPrevious();
            viewFlipper.startFlipping();
        });
    }

    private void loadDatabaseData() {
        try {
            InputStream is = getAssets().open("database.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray popularArray = jsonObject.getJSONArray("Popular");
            JSONArray branchesArray = jsonObject.getJSONArray("branches");

            LayoutInflater inflater = LayoutInflater.from(this);

            // "Our Signature"
            for (int i = 0; i < Math.min(3, popularArray.length()); i++) {
                JSONObject itemObj = popularArray.getJSONObject(i);
                View itemCard = inflater.inflate(R.layout.item_preview_card, llFeaturedItems, false);

                TextView title = itemCard.findViewById(R.id.item_title);
                ImageView image = itemCard.findViewById(R.id.item_image);
                title.setText(itemObj.getString("title"));

                JSONArray picUrlArray = itemObj.getJSONArray("picUrl");
                String imageName = picUrlArray.getString(0);

                int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                if (imageResId != 0) {
                    image.setImageResource(imageResId);
                }

                itemCard.setOnClickListener(v -> {
                    Intent intent = new Intent(HomeActivity.this, ItemDetailActivity.class);
                    try {
                        intent.putExtra("PRODUCT_TITLE", itemObj.getString("title"));
                        intent.putExtra("PRODUCT_EXTRA", itemObj.getString("extra"));
                        intent.putExtra("PRODUCT_PRICE", itemObj.getInt("price"));
                        intent.putExtra("PRODUCT_IMAGE_NAME", itemObj.getJSONArray("picUrl").getString(0));
                        intent.putExtra("PRODUCT_DESC", itemObj.getString("description"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                });

                llFeaturedItems.addView(itemCard);
            }

            // "Visit Us"
            for (int i = 0; i < Math.min(2, branchesArray.length()); i++) {
                JSONObject branchObj = branchesArray.getJSONObject(i);
                View branchCard = inflater.inflate(R.layout.branch_preview_card, llFeaturedBranches, false);

                TextView title = branchCard.findViewById(R.id.branch_title);
                TextView location = branchCard.findViewById(R.id.branch_location);
                ImageView image = branchCard.findViewById(R.id.branch_image);

                title.setText(branchObj.getString("name"));
                location.setText(branchObj.getString("location"));

                String imageName = branchObj.getString("image");
                int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                if (imageResId != 0) {
                    image.setImageResource(imageResId);
                }

                branchCard.setOnClickListener(v -> {
                    Intent intent = new Intent(HomeActivity.this, BranchActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                });

                llFeaturedBranches.addView(branchCard);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupViewAllButtons() {
        tvViewAllItems.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ItemActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        tvViewAllBranches.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, BranchActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}