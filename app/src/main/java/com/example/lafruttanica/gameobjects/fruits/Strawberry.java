package com.example.lafruttanica.gameobjects.fruits;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.example.lafruttanica.R;
import com.example.lafruttanica.gamehandlers.GameManager;

public class Strawberry extends Fruit {
    private boolean isDiagonal; /* Whether the strawberry is in its diagonal phase or not */
    private static final int SCORE = 30;
    private static final double SIDE_VELOCITY = 30;
    private static final double DIAGONAL_HEIGHT = 1000; /* If the strawberry reaches this height, it
                                                           will stop and randomly diagonally */

    public Strawberry(GameManager gameManager) {
        super(gameManager, SCORE);

        // Setting and resizing the image:
        this.image = BitmapFactory.decodeResource(gameManager.getResources(), R.drawable.strawberry);
        final int STRAWBERRY_WIDTH = (int) (this.gameManager.getScreenWidth() * FRUIT_SCALE);
        final int STRAWBERRY_HEIGHT = this.image.getHeight() * STRAWBERRY_WIDTH / this.image.getWidth();

        this.image = Bitmap.createScaledBitmap(this.image, STRAWBERRY_WIDTH, STRAWBERRY_HEIGHT, false);

        // Spawning the apple at a random location:
        this.setSpawnCoordinates();

        isDiagonal = false;
    }

    /**
     * The strawberry's movement is similar to the apple's, but at a certain height it moves
     * diagonally to a random direction.
     */
    @Override
    protected void move() {
        // If the strawberry reached the diagonal height:
        if (!this.isDiagonal && this.y >= DIAGONAL_HEIGHT * screenRatioY) {
            // Signaling the diagonal side began:
            this.isDiagonal = true;
            // Applying side velocity:
            this.velX = SIDE_VELOCITY * screenRatioX;
            // Deciding whether the strawberry moves left or right:
            final boolean right = random.nextBoolean();
            this.velX *= right ? 1 : -1;
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

        this.x += this.velX;
        this.y += FALL_VELOCITY * screenRatioY;
    }
}
