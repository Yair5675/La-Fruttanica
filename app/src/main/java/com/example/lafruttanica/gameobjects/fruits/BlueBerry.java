package com.example.lafruttanica.gameobjects.fruits;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.lafruttanica.R;
import com.example.lafruttanica.gamehandlers.GameManager;

public class BlueBerry extends Fruit {
    private static final int SCORE = 20;
    private static final int SIDE_VELOCITY = 25;

    private static final double HOP_VELOCITY = 50; /* When reaching this velocity, the blueberry will "hop" */

    public BlueBerry(GameManager gameManager) {
        super(gameManager, SCORE);

        // Setting the x velocity to the selected number:
        this.velX = SIDE_VELOCITY;

        // Setting and resizing the image:
        this.image = BitmapFactory.decodeResource(gameManager.getResources(), R.drawable.blueberry);
        final int BLUEBERRY_WIDTH = (int) (this.gameManager.getScreenWidth() * FRUIT_SCALE);
        final int BLUEBERRY_HEIGHT = this.image.getHeight() * BLUEBERRY_WIDTH / this.image.getWidth();

        this.image = Bitmap.createScaledBitmap(this.image, BLUEBERRY_WIDTH, BLUEBERRY_HEIGHT, false);

        // Spawning the apple at a random location:
        this.setSpawnCoordinates();
    }

    /**
     * The blueberry's signature movement is "hopping" its way down.
     */
    @Override
    protected void move() {
        super.move();

        // If the blueberry reached the HOP_VELOCITY, change its direction and make it hop:
        if (this.velY >= HOP_VELOCITY) {
            this.velY *= -0.6;
            this.velX *= -1;
        }
        // Change its direction if it got into a wall:
        final int X_SCREEN_LIM = this.gameManager.getScreenWidth() - this.image.getWidth();
        if (this.x < 0) {
            this.x = 0;
            this.velX *= -1;
        }

        else if (this.x > X_SCREEN_LIM) {
            this.x = X_SCREEN_LIM;
            this.velX *= -1;
        }
    }
}
