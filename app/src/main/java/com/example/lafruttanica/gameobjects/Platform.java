package com.example.lafruttanica.gameobjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.lafruttanica.GameManager;
import com.example.lafruttanica.R;

public class Platform extends GameObject {

    private double friction; /* The friction that the player will experience */
    private final GameManager gameManager;

    public Platform(double x, double y, double friction, GameManager gameManager) {
        // Setting the x and y coordinates:
        super(x, y, 0, 0);
        // Setting the friction value:
        this.friction = friction;
        // Saving a variable pointing to the game manager:
        this.gameManager = gameManager;

    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(this.gameManager.getResources(),
                R.drawable.platform_scaled);
        canvas.drawBitmap(bitmap, (float) x, (float) y, null);
    }

    @Override
    public void update() {

    }

    public double getFriction() {
        return friction;
    }
}
