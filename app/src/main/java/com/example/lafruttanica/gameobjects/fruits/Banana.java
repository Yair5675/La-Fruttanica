package com.example.lafruttanica.gameobjects.fruits;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.lafruttanica.R;
import com.example.lafruttanica.gamehandlers.GameLoop;
import com.example.lafruttanica.gamehandlers.GameManager;

public class Banana extends Fruit {
    private double movementX; /* The current x value in the banana's movement sin function */
    private double startX; /* The first x position the the banana spawned at */
    private static final int SCORE = 30;

    //Since the banana's movement is based on the sin(x) function, the movement unit is the delta of x:
    private static final double MOVEMENT_UNIT = Math.PI / GameLoop.getMaxUps();
    private static final double WAVE_LENGTH = 200; /* The amount of x pixels the banana will move in
                                                     half an interval of its sin function */

    public Banana(GameManager gameManager) {
        super(gameManager, SCORE);
        // Setting the movement x to 0:
        this.movementX = 0;

        // Setting and resizing the image:
        this.image = BitmapFactory.decodeResource(gameManager.getResources(), R.drawable.banana);

        final int BANANA_WIDTH = (int) (this.gameManager.getScreenWidth() * FRUIT_SCALE);
        final int BANANA_HEIGHT = this.image.getHeight() * BANANA_WIDTH / this.image.getWidth();

        this.image = Bitmap.createScaledBitmap(this.image, BANANA_WIDTH, BANANA_HEIGHT, false);

        // Spawning the apple at a random location:
        this.setSpawnCoordinates();

        // Saving the first x position as a constant:
        this.startX = this.x;
    }

    /**
     * The banana's signature movement, moves in a sin-function like movement.
     */
    @Override
    protected void move() {
        // Updating the x:
        this.x = this.startX + Math.sin(this.movementX) * WAVE_LENGTH * this.screenRatioX;

        // If the banana hits the walls, we use the equation -sin(x) = sin(-x), and "reverse" the
        // banana's velocity:
        final int X_SCREEN_LIM = this.gameManager.getScreenWidth() - this.image.getWidth();
        if (this.x < 0) {
            this.x *= -1;
            this.movementX = 1.5 * Math.PI;
            this.startX = WAVE_LENGTH * this.screenRatioX;
        }
        else if (this.x > X_SCREEN_LIM) {
            this.x = 2 * X_SCREEN_LIM - this.x;
            this.movementX = Math.PI / 2;
            this.startX = 2 * X_SCREEN_LIM - this.x - WAVE_LENGTH * this.screenRatioX;
        }

        // Moving the movement x forward:
        this.movementX += MOVEMENT_UNIT;

        // Making the banana fall at a constant velocity:
        this.y += FALL_VELOCITY * this.screenRatioY;
    }
}
