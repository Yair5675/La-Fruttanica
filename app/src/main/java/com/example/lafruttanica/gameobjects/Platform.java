package com.example.lafruttanica.gameobjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.lafruttanica.R;

public class Platform extends GameObject {
    public Platform(final int SCREEN_WIDTH, final int SCREEN_HEIGHT, final Resources res) {
        // Loading the platform's image:
        this.image = BitmapFactory.decodeResource(res, R.drawable.new_platform_scaled);
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