package com.example.chimatcha;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;

public class BranchActivity extends AppCompatActivity {

    private LinearLayout llBranchesContainer;

    // Dropdown Variables
    private ImageView btnTopMenu;
    private View dropdownOverlay;
    private CardView cvDropdownMenu;
    private LinearLayout dropHome, dropItems, dropBranch, dropLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);

        llBranchesContainer = findViewById(R.id.ll_branches_container);

        btnTopMenu = findViewById(R.id.btn_top_menu);
        dropdownOverlay = findViewById(R.id.dropdown_overlay);
        cvDropdownMenu = findViewById(R.id.cv_dropdown_menu);
        dropHome = findViewById(R.id.drop_home);
        dropItems = findViewById(R.id.drop_items);
        dropBranch = findViewById(R.id.drop_branch);
        dropLogout = findViewById(R.id.drop_logout);

        setupDropdownMenu();
        loadBranchesData();
    }

    private void setupDropdownMenu() {
        btnTopMenu.setOnClickListener(v -> {
            cvDropdownMenu.setVisibility(View.VISIBLE);
            dropdownOverlay.setVisibility(View.VISIBLE);
        });

        dropdownOverlay.setOnClickListener(v -> hideDropdown());

        dropHome.setOnClickListener(v -> {
            hideDropdown();
            Intent intent = new Intent(BranchActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        dropItems.setOnClickListener(v -> {
            hideDropdown();
            Intent intent = new Intent(BranchActivity.this, ItemActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        dropBranch.setOnClickListener(v -> hideDropdown()); // Already on Branch page

        dropLogout.setOnClickListener(v -> {
            hideDropdown();
            Intent intent = new Intent(BranchActivity.this, LoginActivity.class);
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

    private void loadBranchesData() {
        llBranchesContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        try {
            InputStream is = getAssets().open("database.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray branchesArray = jsonObject.getJSONArray("branches");

            for (int i = 0; i < branchesArray.length(); i++) {
                JSONObject branchObj = branchesArray.getJSONObject(i);
                View card = inflater.inflate(R.layout.branch_grid_card, llBranchesContainer, false);

                TextView tvName = card.findViewById(R.id.tv_branch_name);
                TextView tvRoad = card.findViewById(R.id.tv_branch_road);
                TextView tvHours = card.findViewById(R.id.tv_branch_hours);
                ImageView ivImage = card.findViewById(R.id.iv_branch_image);

                tvName.setText(branchObj.getString("name"));
                tvRoad.setText(branchObj.getString("road"));
                tvHours.setText(branchObj.getString("opening_hours"));

                String imageName = branchObj.getString("image");
                int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                if (imageResId != 0) {
                    ivImage.setImageResource(imageResId);
                }

                llBranchesContainer.addView(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}