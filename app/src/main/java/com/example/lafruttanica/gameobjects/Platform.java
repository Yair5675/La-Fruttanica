package com.example.lafruttanica.gameobjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.example.lafruttanica.R;
import com.example.lafruttanica.gamehandlers.GameManager;

public class Platform extends GameObject {

    public Platform(final GameManager gameManager) {
        super(gameManager.getScreenWidth(), gameManager.getScreenHeight());

        final Resources res = gameManager.getResources();
        // Loading the platform's image:
        this.image = BitmapFactory.decodeResource(res, R.drawable.platform);

        // Calculating the new height for the platform using proportions:
        final int NEW_HEIGHT = this.image.getHeight() * SCREEN_WIDTH / this.image.getWidth();

        // Resizing the image (the new width is always the screen's width, and the height is
        // calculated accordingly):
        this.image = Bitmap.createScaledBitmap(this.image, SCREEN_WIDTH, NEW_HEIGHT, false);

        // Setting the coordinates to the bottom of the screen:
        this.x = 0;
        this.y = SCREEN_HEIGHT - this.getHeight();
    }

    @Override
    public void update() {
    }
}