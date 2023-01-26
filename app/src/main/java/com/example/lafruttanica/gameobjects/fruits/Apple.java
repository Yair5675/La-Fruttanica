package com.example.lafruttanica.gameobjects.fruits;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.lafruttanica.R;
import com.example.lafruttanica.gamehandlers.GameManager;

public class Apple extends Fruit {
    private static final int SCORE = 10;

    public Apple(GameManager gameManager) {
        super(gameManager, SCORE);

        // Setting and resizing the image:
        this.image = BitmapFactory.decodeResource(gameManager.getResources(), R.drawable.apple_scaled);
        final int APPLE_WIDTH = (int) (this.gameManager.getScreenWidth() * FRUIT_SCALE);
        final int APPLE_HEIGHT = this.image.getHeight() * APPLE_WIDTH / this.image.getWidth();

        this.image = Bitmap.createScaledBitmap(this.image, APPLE_WIDTH, APPLE_HEIGHT, false);

        // Spawning the apple at a random location:
        this.setSpawnCoordinates();
    }

    /**
     * The apple's movement is a normal one: just fall down at a constant speed.
     */
    @Override
    protected void move() {
        this.y += FALL_VELOCITY * this.screenRatioY;
    }
}
