package com.example.lafruttanica.gamehandlers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.lafruttanica.R;
import com.example.lafruttanica.gameobjects.fruits.Fruit;
import com.example.lafruttanica.gameobjects.Platform;
import com.example.lafruttanica.gameobjects.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * Handles the game's objects, updates and draws them. Also handles touch events and score.
 */
@SuppressLint("ViewConstructor")
public class GameManager extends SurfaceView implements View.OnTouchListener, SurfaceHolder.Callback {
    private GameLoop gameLoop; /* Object handling the run of the game */
    private final Player player; /* The player object in the game */
    private final Platform platform; /* The platform of the game */
    private final ArrayList<Fruit> fruits; /* The fruits that are on the screen */
    private final int SCREEN_WIDTH; /* The width of the screen */
    private final int SCREEN_HEIGHT; /* The height of the screen */
    private final Bitmap BACKGROUND; /* The background image of the game */
    private final SurfaceHolder surfaceHolder; /* The surface object of the game */
    private long time = System.currentTimeMillis(); /* Variable to record time for the spawn rate of fruits */
    private static final Random random = new Random(); /* A variable to get random values */
    private static final short MAX_FRUITS = 4; /* The maximum amount of fruits allowed to be on the screen */
    private static final boolean SHOW_UPS = false; /* Whether the ups and fps will be shown in the screen */

    // To make the game compatible for every device, the movement of objects will be multiplied by this ratio:
    private final double screenRatioX;
    private final double screenRatioY;


    public GameManager(Context context, final int SCREEN_WIDTH, final int SCREEN_HEIGHT) {
        super(context);

        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);

        // Setting the game-loop:
        this.initLoop();
        setFocusable(true);

        // Saving the screen's width and height:
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;

        this.fruits = new ArrayList<>();

        // Calculating the ratio:
        this.screenRatioX = 1080.0 / SCREEN_WIDTH;
        this.screenRatioY = 2186.0 / SCREEN_HEIGHT;

        // Setting the background:
        final Resources res = context.getResources();
        Bitmap tempBackground = BitmapFactory.decodeResource(res, R.drawable.background_tree);
        BACKGROUND = Bitmap.createScaledBitmap(tempBackground, SCREEN_WIDTH, SCREEN_HEIGHT, false);

        // Loading the platform object:
        this.platform = new Platform(this);

        // Loading the player object:
        this.player = new Player(this);

        // Signaling to the surface view that touches are controlled through this class:
        this.setOnTouchListener(this);
    }

    private void initLoop() {
        this.gameLoop = new GameLoop(this, this.surfaceHolder);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Drawing the background:
        canvas.drawBitmap(BACKGROUND, 0, 0, new Paint());

        // Drawing the player's score:
        drawScore(canvas);

        // Displaying the updates and frames per second if needed:
        if (SHOW_UPS) {
            drawFPS(canvas);
            this.drawUPS(canvas);
        }

        // Drawing the platform:
         this.platform.draw(canvas);

        // Drawing the player:
        this.player.draw(canvas);

        // Drawing the fruits:
        for (Fruit fruit : this.fruits) {
            // Making sure the fruit isn't null:
            if (fruit == null) continue;

            // Drawing the fruit:
            fruit.draw(canvas);
        }
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

    @SuppressLint("DefaultLocale")
    private void drawScore(Canvas canvas) {
        // Setting the size of the text:
        Paint paint = new Paint();
        paint.setTextSize((float) (70 * screenRatioX));
        // Setting the font:
        final Typeface font = ResourcesCompat.getFont(this.getContext(), R.font.score_font);
        paint.setTypeface(font);

        // Centering the text at the top-middle of the screen:
        final String score = String.format("Score: %d", this.player.score);
        Rect bounds = new Rect();
        paint.getTextBounds(score, 0, score.length(), bounds);
        final int textX = (canvas.getWidth() - bounds.width()) / 2,
                  textY = (int) (150 * screenRatioY);
        canvas.drawText(score, textX, textY, paint);
    }

    public void update() {
        // Update the player:
        this.player.update();

        // Checking if the player hit the platform:
        if (this.player.getVelY() > 0 && this.player.collides(this.platform)) {
            // Reset the player's velocity to 0:
            this.player.setVelY(0);
            // Setting the player's y to just below the screen:
            this.player.setY(this.platform.getY() + 70 * screenRatioY - this.player.getHeight());
        }

        // Check that the player is not outside the screen horizontally:
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

        // Updating the fruits:
        Fruit fruit;
        for (int i = 0; i < this.fruits.size(); i++) {
            fruit = this.fruits.get(i);
            // Making sure the fruit isn't null:
            if (fruit == null) continue;

            // Updating the fruit:
            fruit.update();

            // If the fruit touched the player's bag, update the score and remove the fruit:
            if (this.player.collides(fruit)) {
                this.player.score += fruit.getScore();
                this.fruits.set(i, null);
            }
            // If the fruit reached the platform, take points off the player and remove the fruit:
            else if (this.platform.collides(fruit)) {
                this.player.score = Math.max(0, this.player.score - 20);
                this.fruits.set(i, null);
            }
        }

        // Removing the fruits that should be out of the game:
        this.fruits.removeIf(Objects::isNull);

        // Generating two fruits every two seconds:
        if (System.currentTimeMillis() - time >= 2000) {
            // Adding fruits only if there is room for more:
            final int fruitNum = Math.min(2, Math.max(0, MAX_FRUITS - this.fruits.size()));
            this.addFruit(fruitNum);
            time = System.currentTimeMillis();
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return false;
    }

    /**
     * Adds two random fruits to the game.
     */
    private void addFruit(final int fruitNum) {
        Fruit.Type type;
        final Fruit.Type[] FRUIT_TYPES = Fruit.Type.values();
        for (int i = 0; i < fruitNum; i++) {
            // Selecting a random type:
            type = FRUIT_TYPES[random.nextInt(FRUIT_TYPES.length)];
            // Adding the random fruit to the arraylist:
            this.fruits.add(Fruit.createFruit(type, this));
        }
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

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        // If the thread has been terminated, starting it again will throw an exception. To solve
        // that, we will make sure to reset the loop if the thread is indeed terminated:
        if (this.gameLoop.getState().equals(Thread.State.TERMINATED)) {
            this.initLoop();
        }

        // Starting the loop once the surface is created:
        this.gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
    }

    public void pause() {
        this.gameLoop.stopLoop();
    }
}
