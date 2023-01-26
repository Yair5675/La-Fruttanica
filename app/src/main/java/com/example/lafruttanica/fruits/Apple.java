package com.example.lafruttanica.fruits;


import android.graphics.BitmapFactory;

import com.example.lafruttanica.R;
import com.example.lafruttanica.gamehandlers.GameView;

public class Apple extends Fruit {
    private static final int SCORE = 10;

    public Apple(GameView gameManager) {
        super(gameManager, SCORE);

        // Setting the image:
        this.image = BitmapFactory.decodeResource(gameManager.getResources(), R.drawable.apple_scaled);

        // Spawning the apple at a random location:
        this.setSpawnCoordinates();
    }
}
