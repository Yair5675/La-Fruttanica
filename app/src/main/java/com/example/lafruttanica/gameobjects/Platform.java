package com.example.lafruttanica.gameobjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.lafruttanica.GameManager;
import com.example.lafruttanica.R;

public class Platform extends GameObject {

    private double friction; /* The friction that the player will experience */
    private final GameManager gameManager;
    private Bitmap platformImage;

    public Platform(double friction, GameManager gameManager) {
        // Setting the x and y coordinates to 0 for now:
        super(0, 0, 0, 0);
        // Setting the friction value:
        this.friction = friction;
        // Saving a variable pointing to the game manager:
        this.gameManager = gameManager;
        // Loading the image:
        this.loadImage();
        // Setting the x and y to the bottom of the screen:
        this.y = this.gameManager.getScreenHeight() - this.getHeight();
    }

    private void loadImage() {
        Bitmap platform = BitmapFactory.decodeResource(this.gameManager.getResources(),
                R.drawable.platform_scaled);

        // Resizing the platform:
        final float NEW_WIDTH = gameManager.getScreenWidth();
        final float NEW_HEIGHT = NEW_WIDTH * platform.getHeight() / platform.getWidth();
        final float scaleW = NEW_WIDTH / platform.getWidth();
        final float scaleY = NEW_HEIGHT / platform.getHeight();

        final Matrix matrix = new Matrix();
        matrix.postScale(scaleW, scaleY);

        platform = Bitmap.createBitmap(platform, 0, 0, platform.getWidth(), platform.getHeight(), matrix, false);
        this.platformImage = platform;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.platformImage, (float) this.x, (float) this.y, new Paint());
    }

    @Override
    public void update() {

    }

    public double getFriction() {
        return friction;
    }

    public int getWidth() {
        return this.platformImage.getWidth();
    }

    public int getHeight() {
        return this.platformImage.getHeight();
    }
}
