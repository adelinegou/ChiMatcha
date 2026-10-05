package com.example.chimatcha;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Bind layout views
        etUsername = findViewById(R.id.et_reg_username);
        etPassword = findViewById(R.id.et_reg_password);
        etConfirmPassword = findViewById(R.id.et_reg_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        tvLoginLink = findViewById(R.id.tv_login_link);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegistration();
            }
        });

        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }

    private void handleRegistration() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation: Username must be filled
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username must be filled");
            etUsername.requestFocus();
            return;
        }

        // Validation: Length of username must be greater than 6
        if (username.length() <= 6) {
            etUsername.setError("Username must be greater than 6 characters");
            etUsername.requestFocus();
            return;
        }

        // Validation: Password must be filled
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password must be filled");
            etPassword.requestFocus();
            return;
        }

        // Validation: Confirm Password must be filled
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirm Password must be filled");
            etConfirmPassword.requestFocus();
            return;
        }

        // Validation: Password and Confirm Password must be the same
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        // Success Message
        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        intent.putExtra("REGISTERED_USERNAME", username);

        startActivity(intent);

        finish();
    }
}