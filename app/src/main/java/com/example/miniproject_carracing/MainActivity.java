package com.example.miniproject_carracing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Directly start GuideActivity without showing menu
        Intent intent = new Intent(MainActivity.this, GuideActivity.class);
        startActivity(intent);
        finish(); // Close MainActivity so user can't go back to it
    }
}