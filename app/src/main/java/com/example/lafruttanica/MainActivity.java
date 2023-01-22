package com.example.lafruttanica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int WIDTH = displayMetrics.widthPixels;
        final int HEIGHT = displayMetrics.heightPixels;
        setContentView(new GameManager(this, WIDTH, HEIGHT));
    }
}