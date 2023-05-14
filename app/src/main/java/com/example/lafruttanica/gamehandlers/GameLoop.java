package com.example.lafruttanica.gamehandlers;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * This class handles the frame rate and updates per second of the game, allowing it to run
 * consistently at a constant rate.
 */
public class GameLoop extends Thread {
    private double averageUPS; /* The average updates per second */
    private double averageFPS; /* The average frames per second */
    private boolean isRunning; /* Whether the gameLoop is running or not */
    private final SurfaceHolder surfaceHolder; /* Surface holder to manage the screen */
    private final GameManager gameManager; /* Game object for rendering and updating game objects */

    private final static double MAX_UPS = 30; /* The maximum updates per second */
    private final static double UPS_FREQ = 1e3 / MAX_UPS; /* The actual UPS frequency */

    public GameLoop(final GameManager gameManager, final SurfaceHolder surfaceHolder) {
        this.isRunning = false;
        this.averageUPS = 0;
        this.averageFPS = 0;
        this.surfaceHolder = surfaceHolder;
        this.gameManager = gameManager;
    }

    public double getAverageUPS() {
        return this.averageUPS;
    }

    public double getAverageFPS() {
        return this.averageFPS;
    }

    public void startLoop() {
        Log.d("GameLoop.java", "startLoop() called");
        this.isRunning = true;
        this.start();
    }

    public void stopLoop() {
        Log.d("GameLoop.java", "stopLoop()");
        this.isRunning = false;
        try {
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();

        // A canvas to write on:
        Canvas canvas = null;

        // Variables to count the updates per second and frames per second:
        int updatesCount = 0;
        int framesCount = 0;

        // Variables to hold the time that has passed (in milliseconds):
        long startTime;
        long elapsedTime;
        long sleepTime;

        // While the game loop is running:
        startTime = System.currentTimeMillis();
        while (this.isRunning) {

            // Update and render objects in the game:
            try {
                // Locking the canvas:
                canvas = this.surfaceHolder.lockCanvas();

                // Preventing multiple threads from calling the 'update' and 'draw' methods:
                synchronized (this.surfaceHolder) {
                    // Drawing the objects in the game:
                    this.gameManager.draw(canvas);

                    // Updating the game:
                    this.gameManager.update();
                    updatesCount++;
                }

            } catch (IllegalArgumentException e) {
                e.printStackTrace();

            } finally {
                // If no error occurred and the canvas is not null, unlock the canvas:
                if (canvas != null) {
                    try {
                        this.surfaceHolder.unlockCanvasAndPost(canvas);
                        framesCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // Calculating the elapsed and sleep time:
            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updatesCount * UPS_FREQ - elapsedTime);

            // Pause for stable and consistent updates per second:
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // If the updating/rendering is taking too much time, skip frames:
            while (sleepTime < 0 && updatesCount < MAX_UPS - 1) {
                // Updating the game again so the updates per second match their rate:
                this.gameManager.update();
                updatesCount++;

                // Recalculating the sleep time:
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updatesCount * UPS_FREQ - elapsedTime);
            }

            // Calculate average UPS and FPS:
            elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= 1000) { // If at least 1 second has passed:
                // Calculating the averages:
                this.averageUPS = updatesCount / (1e-3 * elapsedTime);
                this.averageFPS = framesCount / (1e-3 * elapsedTime);

                Log.i("FPS and UPS: ", averageFPS + " " + averageUPS);

                // Resetting the time and counters:
                startTime = System.currentTimeMillis();
                updatesCount = 0;
                framesCount = 0;
            }
        }
    }

    public static double getMaxUps() {
        return MAX_UPS;
    }
}
