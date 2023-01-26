package com.example.lafruttanica.gameobjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.lafruttanica.R;

public class Player extends GameObject {
    public enum accelerate {
        LEFT,
        RIGHT,
        NONE
    }
    private accelerate acceleration; /* The direction of the vertical acceleration */
    private static final double FRICTION = 0.4; /* Friction that applies to the player when he isn't moving */
    private static final double SIDE_ACCELERATION = 1; /* The acceleration of the player */

    public Player(final int SCREEN_WIDTH, final Resources res, final double screenRatioX,
                  final double screenRatioY) {
        // Setting the acceleration to none:
        this.acceleration = accelerate.NONE;

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

        // Setting the ratio:
        this.screenRatioX = screenRatioX;
        this.screenRatioY = screenRatioY;
    }

    /**
     * Setting the direction of the player's acceleration.
     * @param acceleration The direction of acceleration: LEFT, RIGHT or NONE.
     */
    public void setAcceleration(accelerate acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public void update() {
        // Accelerating accordingly:
        switch (this.acceleration) {
            case LEFT: {
                this.velX -= SIDE_ACCELERATION * screenRatioX;
                break;
            }
            case RIGHT: {
                this.velX += SIDE_ACCELERATION * screenRatioX;
            }
            // Applying friction if there is no acceleration:
            case NONE: {
                // If the player is moving right:
                if (this.velX > 0)
                    this.velX = Math.max(0, this.velX - FRICTION * screenRatioX);
                // If the player is moving left:
                else
                    this.velX = Math.min(0, this.velX + FRICTION * screenRatioX);
            }
        }

        // Moving the object:
        this.move();
    }
}
