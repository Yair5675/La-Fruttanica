package com.example.lafruttanica.gameobjects;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.lafruttanica.R;

public class Player extends GameObject {

    public Player(final int SCREEN_WIDTH, final Resources res) {
        // Loading the image:
        this.image = BitmapFactory.decodeResource(res, R.drawable.player_basket_scaled);
        // The image width should be a fourth of the total width. Calculate the height accordingly:
        final int PLAYER_WIDTH = SCREEN_WIDTH / 4;
        final int PLAYER_HEIGHT = this.image.getHeight() * PLAYER_WIDTH / this.image.getWidth();
        // Resizing the image:
        this.image = Bitmap.createScaledBitmap(this.image, PLAYER_WIDTH, PLAYER_HEIGHT, false);

        // Setting the x to the middle of the screen and the y to the top, so the player will fall:
        this.x = (SCREEN_WIDTH - this.getWidth()) / 2.0;
        this.y = 0;
    }

    @Override
    public void update() {
        this.move();
    }

    public void accelerateX(final double acceleration) {
        this.velX += acceleration;
    }
}
