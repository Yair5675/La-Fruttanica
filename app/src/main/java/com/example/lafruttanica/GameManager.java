package com.example.lafruttanica;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.lafruttanica.gameobjects.Platform;


/**
 * This class manages all objects and states in the game, and is responsible to update and render
 * those objects on the screen.
 */
public class GameManager extends SurfaceView implements SurfaceHolder.Callback {
    private final GameLoop gameLoop; /* A gameLoop instance to control the frame-rate of the game */
    private final Platform platform; /* The platform object the player is standing on */

    public enum GameState {
        OPEN_SCREEN,
        IN_GAME,
        PAUSE
    }

    public GameManager(Context context) {
        super(context);

        // Get surface holder and add callback:
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // Setting up the game loop:
        this.gameLoop = new GameLoop(this, surfaceHolder);

        setFocusable(true);

        // Setting up the platform:
        this.platform = new Platform(0, 500, 1, this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // Drawing the updates per seconds and frames per second:
        drawUPS(canvas);
        drawFPS(canvas);
        this.platform.draw(canvas);
    }

    private void drawUPS(Canvas canvas) {
        final String averageUPS = Double.toString(gameLoop.getAverageUPS());
        final Paint paint = new Paint();
        final int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 50, paint);
    }

    private void drawFPS(Canvas canvas) {
        final String averageFPS = Double.toString(gameLoop.getAverageFPS());
        final Paint paint = new Paint();
        final int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 100, paint);
    }

    /**
     * Updates the game state.
     */
    public void update() {

    }
}
