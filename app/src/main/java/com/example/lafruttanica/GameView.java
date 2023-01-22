package com.example.lafruttanica;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.lafruttanica.gameobjects.Platform;
import com.example.lafruttanica.gameobjects.Player;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isRunning; /* A variable indicating whether the game is running or not */
    private final Platform platform; /* The platform of the game */
    private final Player player; /* The player object in the game */
    private final int SCREEN_WIDTH; /* The width of the screen */
    private final int SCREEN_HEIGHT; /* The height of the screen */
    private static final int FPS = 60; /* The frame-rate in which the game runs */

    // To make the game compatible for every device, the movement of objects will be multiplied by this ratio:
    private double screenRatioX;
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
        final double GRAVITY = 2.5;
        this.player.setVelY(this.player.getVelY() + GRAVITY * screenRatioY);

        // Checking collision with the player and the platform:
        if (this.player.collides(this.platform)) {
            // Reset the player's velocity to 0:
            this.player.setVelY(0);
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

    public int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }

    public int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }
}
