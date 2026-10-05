package com.example.chimatcha;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        finish();
    }
}

// n.b. activity_main.xml ga diedit karena disini sdh lgsg redirect ke login jd screen main ga dipakai