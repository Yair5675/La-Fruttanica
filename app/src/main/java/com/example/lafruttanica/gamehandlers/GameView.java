package com.example.lafruttanica.gamehandlers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.lafruttanica.R;
import com.example.lafruttanica.gameobjects.Platform;
import com.example.lafruttanica.gameobjects.Player;

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements View.OnTouchListener, SurfaceHolder.Callback {
    private final Platform platform; /* The platform of the game */
    private final Player player; /* The player object in the game */
    private final int SCREEN_WIDTH; /* The width of the screen */
    private final int SCREEN_HEIGHT; /* The height of the screen */
    private final Bitmap BACKGROUND; /* The background image of the game */
    private final GameLoop gameLoop; /* Object handling the run of the game */

    // To make the game compatible for every device, the movement of objects will be multiplied by this ratio:
    private final double screenRatioX;
    private final double screenRatioY;

    public GameView(Context context, final int SCREEN_WIDTH, final int SCREEN_HEIGHT) {
        super(context);

        getHolder().addCallback(this);

        // Setting the game-loop:
        this.gameLoop = new GameLoop(this, getHolder());
        setFocusable(true);

        // Saving the screen's width and height:
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;

        // Calculating the ratio:
        this.screenRatioX = 1080.0 / SCREEN_WIDTH;
        this.screenRatioY = 2186.0 / SCREEN_HEIGHT;
        Log.v("Ratio is one:", (screenRatioX == 1) + " " + (screenRatioY == 1));

        final Resources res = context.getResources();

        // Setting the background:
        Bitmap tempBackground = BitmapFactory.decodeResource(res, R.drawable.background_tree_scaled);
        BACKGROUND = Bitmap.createScaledBitmap(tempBackground, SCREEN_WIDTH, SCREEN_HEIGHT, false);

        // Loading the platform object:
        this.platform = new Platform(SCREEN_WIDTH, SCREEN_HEIGHT, res);

        // Loading the player object:
        this.player = new Player(SCREEN_WIDTH, res, screenRatioX, screenRatioY);

        // Signaling to the surface view that touches are controlled through this class:
        this.setOnTouchListener(this);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Drawing the background:
        canvas.drawBitmap(BACKGROUND, 0, 0, new Paint());

        // Displaying the updates and frames per second:
        drawFPS(canvas);
        this.drawUPS(canvas);

        // Drawing the platform:
        this.platform.draw(canvas);

        // Drawing the player:
        this.player.draw(canvas);
    }

    private void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(this.gameLoop.getAverageUPS());
        final int color = ContextCompat.getColor(getContext(), R.color.magenta);
        final Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 0, 100, paint);
    }

    private void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(this.gameLoop.getAverageFPS());
        final int color = ContextCompat.getColor(getContext(), R.color.magenta);
        final Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 0, 60, paint);
    }

    public void update() {
        // Update the player:
        this.player.update();

        // Checking collision with the player and the platform:
        if (this.player.collides(this.platform)) {
            // Reset the player's velocity to 0:
            this.player.setVelY(0);
            // Setting the player's y to just below the start of the platform:
            this.player.setY(this.platform.getY() + 70 * screenRatioY - this.player.getHeight());
        }

        // Check that the player is not outside the screen:
        if (0 > this.player.getX()) {
            this.player.setX(0);
            // Create a bounce effect:
            this.player.setVelX(-0.8 * screenRatioX * this.player.getVelX());
        }
        else if (SCREEN_WIDTH < this.player.getX() + this.player.getWidth()) {
            this.player.setX(SCREEN_WIDTH - this.player.getWidth());
            // Create a bounce effect:
            this.player.setVelX(-0.8 * screenRatioX * this.player.getVelX());
        }

        // Update the platform:
        this.platform.update();

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
                if (accelerate_right)
                    this.player.setAcceleration(Player.accelerate.RIGHT);
                else
                    this.player.setAcceleration(Player.accelerate.LEFT);

                // Return true because the event was handled:
                return true;
            }
            // If the user stopped to press:
            case MotionEvent.ACTION_UP: {
                // Remove all acceleration:
                this.player.setAcceleration(Player.accelerate.NONE);

                // Return true because the event was handled:
                return true;
            }
            default: {
                // Return false because the event wasn't handled:
                return false;
            }
        }
    }

    public int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public double getScreenRatioX() {
        return screenRatioX;
    }

    public double getScreenRatioY() {
        return screenRatioY;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        // Starting the loop once the surface is created:
        this.gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        this.gameLoop.stopLoop();
    }
}
