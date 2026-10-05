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

public class ItemActivity extends AppCompatActivity {

    private LinearLayout llProductsContainer;
    private TextView btnCatAll, btnCatClassic, btnCatCoffee, btnCatFruity;

    private ImageView btnTopMenu;
    private View dropdownOverlay;
    private CardView cvDropdownMenu;
    private LinearLayout dropHome, dropItems, dropBranch, dropLogout;

    private JSONArray itemsArray;
    private String selectedCategoryId = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        llProductsContainer = findViewById(R.id.ll_products_container);
        btnCatAll = findViewById(R.id.btn_cat_all);
        btnCatClassic = findViewById(R.id.btn_cat_classic);
        btnCatCoffee = findViewById(R.id.btn_cat_coffee);
        btnCatFruity = findViewById(R.id.btn_cat_fruity);

        btnTopMenu = findViewById(R.id.btn_top_menu);
        dropdownOverlay = findViewById(R.id.dropdown_overlay);
        cvDropdownMenu = findViewById(R.id.cv_dropdown_menu);
        dropHome = findViewById(R.id.drop_home);
        dropItems = findViewById(R.id.drop_items);
        dropBranch = findViewById(R.id.drop_branch);
        dropLogout = findViewById(R.id.drop_logout);

        loadJSONData();
        setupDropdownMenu();
        setupCategoryButtons();
        displayProducts();
    }

    private void loadJSONData() {
        try {
            InputStream is = getAssets().open("database.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            itemsArray = jsonObject.getJSONArray("Items");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupDropdownMenu() {
        btnTopMenu.setOnClickListener(v -> {
            cvDropdownMenu.setVisibility(View.VISIBLE);
            dropdownOverlay.setVisibility(View.VISIBLE);
        });

        dropdownOverlay.setOnClickListener(v -> hideDropdown());

        dropHome.setOnClickListener(v -> {
            hideDropdown();
            Intent intent = new Intent(ItemActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        dropItems.setOnClickListener(v -> hideDropdown()); // Already on Item page

        dropBranch.setOnClickListener(v -> {
            hideDropdown();
            Intent intent = new Intent(ItemActivity.this, BranchActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        dropLogout.setOnClickListener(v -> {
            hideDropdown();
            Intent intent = new Intent(ItemActivity.this, LoginActivity.class);
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

    private void setupCategoryButtons() {
        View.OnClickListener catClickListener = v -> {
            btnCatAll.setBackgroundResource(R.drawable.bg_chip_unselected);
            btnCatAll.setTextColor(getColor(R.color.black));
            btnCatClassic.setBackgroundResource(R.drawable.bg_chip_unselected);
            btnCatClassic.setTextColor(getColor(R.color.black));
            btnCatCoffee.setBackgroundResource(R.drawable.bg_chip_unselected);
            btnCatCoffee.setTextColor(getColor(R.color.black));
            btnCatFruity.setBackgroundResource(R.drawable.bg_chip_unselected);
            btnCatFruity.setTextColor(getColor(R.color.black));

            TextView clickedButton = (TextView) v;
            clickedButton.setBackgroundResource(R.drawable.bg_chip_selected);
            clickedButton.setTextColor(getColor(R.color.white));

            if (clickedButton == btnCatAll) selectedCategoryId = "All";
            else if (clickedButton == btnCatClassic) selectedCategoryId = "0";
            else if (clickedButton == btnCatCoffee) selectedCategoryId = "1";
            else if (clickedButton == btnCatFruity) selectedCategoryId = "2";

            displayProducts();
        };

        btnCatAll.setOnClickListener(catClickListener);
        btnCatClassic.setOnClickListener(catClickListener);
        btnCatCoffee.setOnClickListener(catClickListener);
        btnCatFruity.setOnClickListener(catClickListener);
    }

    private void displayProducts() {
        llProductsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        LinearLayout currentRow = null;
        int visibleCount = 0;

        int marginPx = (int) (8 * getResources().getDisplayMetrics().density);

        try {
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObj = itemsArray.getJSONObject(i);
                String itemCategoryId = itemObj.getString("categoryId");

                if (selectedCategoryId.equals("All") || itemCategoryId.equals(selectedCategoryId)) {

                    if (visibleCount % 2 == 0) {
                        currentRow = new LinearLayout(this);
                        currentRow.setOrientation(LinearLayout.HORIZONTAL);
                        llProductsContainer.addView(currentRow);
                    }

                    View card = inflater.inflate(R.layout.item_grid_card, currentRow, false);

                    TextView title = card.findViewById(R.id.tv_product_title);
                    TextView extra = card.findViewById(R.id.tv_product_extra);
                    TextView price = card.findViewById(R.id.tv_product_price);
                    ImageView image = card.findViewById(R.id.iv_product_image);

                    String productTitle = itemObj.getString("title");
                    String productExtra = itemObj.getString("extra");
                    int productPrice = itemObj.getInt("price");
                    String imageName = itemObj.getJSONArray("picUrl").getString(0);

                    title.setText(productTitle);
                    extra.setText(productExtra);
                    price.setText("Rp " + String.format("%,d", productPrice));

                    int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    if (resId != 0) {
                        image.setImageResource(resId);
                    }

                    card.setOnClickListener(view -> {
                        Intent intent = new Intent(ItemActivity.this, ItemDetailActivity.class);
                        intent.putExtra("PRODUCT_TITLE", productTitle);
                        intent.putExtra("PRODUCT_EXTRA", productExtra);
                        intent.putExtra("PRODUCT_PRICE", productPrice);
                        intent.putExtra("PRODUCT_IMAGE_NAME", imageName);
                        try {
                            intent.putExtra("PRODUCT_DESC", itemObj.getString("description"));
                        } catch (Exception ignored) {}

                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    });

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                    lp.setMargins(marginPx, marginPx, marginPx, marginPx);

                    card.setLayoutParams(lp);

                    if (currentRow != null) {
                        currentRow.addView(card);
                    }

                    visibleCount++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}