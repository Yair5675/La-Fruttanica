package com.example.lafruttanica.gameobjects.fruits;

import com.example.lafruttanica.gamehandlers.GameManager;
import com.example.lafruttanica.gameobjects.GameObject;

import java.util.Random;

public abstract class Fruit extends GameObject {
    protected final int SCORE; /* The score that the player gets upon catching the fruit */
    protected final GameManager gameManager;
    protected static final Random random = new Random();
    protected static final double FALL_VELOCITY = 30; /* The constant y velocity of the fruit */

    // All the fruits' images must have a width of tenth of the screen:
    protected static final double FRUIT_SCALE = 0.1;

    // The limits to the fruit's spawning position (in height percentage):
    private static final short UPPER_SPAWN_LIMIT = 15;
    private static final short LOWER_SPAWN_LIMIT = 23;

    public enum Type {
        APPLE,
        BANANA,
        BLUEBERRY,
        PEACH,
        STRAWBERRY
    }

    public Fruit(final GameManager gameManager, final int SCORE) {
        super(gameManager.getScreenWidth(), gameManager.getScreenHeight());

        this.SCORE = SCORE;
        this.gameManager = gameManager;
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


    public static Fruit createFruit(final Type type, final GameManager gameManager) {
        switch (type) {
            case APPLE: return new Apple(gameManager);
            case BANANA: return new Banana(gameManager);
            case BLUEBERRY: return new BlueBerry(gameManager);
            case PEACH: return new Peach(gameManager);
            case STRAWBERRY: return new Strawberry(gameManager);
            default: return null;
        }
    }
}
