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

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegisterLink = findViewById(R.id.tv_register_link);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLoginValidation();
            }
        });

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }

    private void handleLoginValidation() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation: Username field cannot be empty
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username cannot be empty");
            etUsername.requestFocus();
            return;
        }

        // Validation: Username must be greater than 6 characters
        if (username.length() <= 6) {
            etUsername.setError("Username must be greater than 6 characters");
            etUsername.requestFocus();
            return;
        }

        // Validation: Password field cannot be empty
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return;
        }

        // Success Message
        Toast.makeText(LoginActivity.this, "Welcome to Chi Matcha, " + username + "!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("REGISTERED_USERNAME", username);
        startActivity(intent);

        finish();
    }
}