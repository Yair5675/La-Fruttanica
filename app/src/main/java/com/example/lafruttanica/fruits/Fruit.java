package com.example.lafruttanica.fruits;

import com.example.lafruttanica.gamehandlers.GameView;
import com.example.lafruttanica.gameobjects.GameObject;

import java.util.Random;

public abstract class Fruit extends GameObject {
    protected final int SCORE; /* The score that the player gets upon catching the fruit */
    private final GameView gameManager;
    private static final Random random = new Random();

    // The limits to the fruit's spawning position (in height percentage):
    private static final short UPPER_SPAWN_LIMIT = 15;
    private static final short LOWER_SPAWN_LIMIT = 23;

    public Fruit(final GameView gameManager, final int SCORE) {
        this.SCORE = SCORE;
        this.gameManager = gameManager;

        this.screenRatioX = gameManager.getScreenRatioX();
        this.screenRatioY = gameManager.getScreenRatioY();
    }

    protected void setSpawnCoordinates() {
        // Setting the x coordinates:
        this.x = random.nextInt(this.gameManager.getScreenWidth() - this.image.getWidth());

        // Setting the y coordinates:
        final short HEIGHT_AREA = LOWER_SPAWN_LIMIT - UPPER_SPAWN_LIMIT;
        this.y = this.gameManager.getScreenHeight() *
                (UPPER_SPAWN_LIMIT + random.nextInt(HEIGHT_AREA)) / 100.0;
    }

    @Override
    public void update() {
        // Each fruit has a special "move" method:
        this.move();
    }

    public int getScore() {
        return SCORE;
    }
}
