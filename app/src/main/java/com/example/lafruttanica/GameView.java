package com.example.lafruttanica;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.example.lafruttanica.gameobjects.Platform;
import com.example.lafruttanica.gameobjects.Player;

public class GameView extends SurfaceView implements Runnable, View.OnTouchListener {
    private Thread thread;
    private boolean isRunning; /* A variable indicating whether the game is running or not */
    private final Platform platform; /* The platform of the game */
    private final Player player; /* The player object in the game */
    private final int SCREEN_WIDTH; /* The width of the screen */
    private final int SCREEN_HEIGHT; /* The height of the screen */
    private static final int FPS = 60; /* The frame-rate in which the game runs */
    private static final double GRAVITY = 2.5; /* The gravity enacted on the player */
    private static final double FRICTION = 0.3; /* Friction that applies to the player when he isn't moving */

    private static final double SIDE_ACCELERATION = 1; /* The acceleration of the player */

    // To make the game compatible for every device, the movement of objects will be multiplied by this ratio:
    private final double screenRatioX;
    private final double screenRatioY;

    public GameView(Context context, final int SCREEN_WIDTH, final int SCREEN_HEIGHT) {
        super(context);

        // Saving the screen's width and height:
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;

        // Calculating the ratio:
        this.screenRatioX = 1080.0 / SCREEN_WIDTH;
        this.screenRatioY = 2186.0 / SCREEN_HEIGHT;
        Log.v("Ratio is one:", (screenRatioX == 1) + " " + (screenRatioY == 1));

        final Resources res = context.getResources();

        // Loading the platform object:
        this.platform = new Platform(SCREEN_WIDTH, SCREEN_HEIGHT, res);

        // Loading the player object:
        this.player = new Player(SCREEN_WIDTH, res);

        // Signaling to the surface view that touches are controlled through this class:
        this.setOnTouchListener(this);

    }

    @Override
    public void run() {
        while (this.isRunning) {
            this.update();
            this.draw();
            this.sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000 / FPS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw() {
        // Validating the surfaceView object:
        if (!this.getHolder().getSurface().isValid()) {
            return;
        }
        // Getting the canvas:
        final Canvas canvas = getHolder().lockCanvas();

        // Painting the canvas in the color of the background to remove the previous frame:
        canvas.drawColor(getContext().getColor(R.color.background));

        // Drawing the platform:
        this.platform.draw(canvas);

        // Drawing the player:
        this.player.draw(canvas);

        // Unlocking the canvas:
        getHolder().unlockCanvasAndPost(canvas);
    }

    private void update() {
        // Applying gravity to the player:
        this.player.setVelY(this.player.getVelY() + GRAVITY * screenRatioY);

        // Checking collision with the player and the platform:
        if (this.player.collides(this.platform)) {
            // Reset the player's velocity to 0:
            this.player.setVelY(0);
        }

        // Accelerating the player when needed:
        if (this.player.accelerateRight)
            this.player.accelerateX(SIDE_ACCELERATION * screenRatioX);
        else if (this.player.accelerateLeft)
            this.player.accelerateX(-SIDE_ACCELERATION * screenRatioX);
        // If the player is not accelerating, apply friction:
        else if (this.player.getVelX() != 0) {
            if (this.player.getVelX() > 0)
                this.player.setVelX(Math.max(0, this.player.getVelX() - FRICTION));
            else
                this.player.setVelX(Math.min(0, this.player.getVelX() + FRICTION));
        }


        // Check that the player is not outside the screen:
        if (0 > this.player.getX()) {
            this.player.setX(0);
            // Create a bounce effect:
            this.player.setVelX(-0.8 * this.player.getVelX());
        }
        else if (SCREEN_WIDTH < this.player.getX() + this.player.getWidth()) {
            this.player.setX(SCREEN_WIDTH - this.player.getWidth());
            // Create a bounce effect:
            this.player.setVelX(-0.8 * this.player.getVelX());
        }

        // Update the player:
        this.player.update();

        // Update the platform:
        this.platform.update();
    }

    public void resume() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            // If the user just pressed:
            case MotionEvent.ACTION_DOWN: {
                // Getting the position of the click:
                final float x = motionEvent.getX();

                // Signal whether the player should accelerate right or left:
                final boolean accelerate_right = x > SCREEN_WIDTH / 2.0;
                this.player.accelerateRight = accelerate_right;
                this.player.accelerateLeft = !accelerate_right;

                // Return true because the event was handled:
                return true;
            }
            // If the user stopped to press:
            case MotionEvent.ACTION_UP: {
                // Remove all acceleration:
                this.player.accelerateRight = false;
                this.player.accelerateLeft = false;

                // Return true because the event was handled:
                return true;
            }
            default:
                // Return false because the event wasn't handled:
                return false;
        }
    }
}
