package com.example.lafruttanica.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.lafruttanica.R;
import com.example.lafruttanica.gamehandlers.GameManager;

/**
 * An activity class for the game, hands the control over to the GameManager class.
 */
public class GameActivity extends AppCompatActivity {
    private boolean isMuted; /* Whether the player wants to mute the music or not */
    private MediaPlayer mediaPlayer; /* Media player for music */
    private GameManager gameManager; /* The game manager object that actually handles the game */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Getting the width and height of the screen:
        final Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        // Creating the game manager:
        gameManager = new GameManager(this, point.x, point.y); // Point.x is the screen's width, Point.y is the screen's height

        setContentView(gameManager);

        // Getting the state of the music from the previous activity:
        this.isMuted = getIntent().getBooleanExtra("isMuted", false);

        // Loading the media player:
        this.mediaPlayer = MediaPlayer.create(this, R.raw.game_music);
        this.mediaPlayer.setLooping(true);

        // Starting the media player only if it isn't muted:
        if (!isMuted)
            this.mediaPlayer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Resuming the music from the beginning:
        if (!isMuted)
            mediaPlayer.start();
        /*
         * Credit to the music:
         * https://www.youtube.com/watch?v=n3uQ2aVzPbk&list=PLFM-Uv4841gxZDGVzkVCDf-kzYNsRpxKK&ab_channel=Larachma
        */
    }

    @Override
    protected void onPause() {
        // Pausing the game:
        this.gameManager.pause();

        super.onPause();

        // Pausing the music:
        if (!isMuted)
            this.mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!isMuted) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
        }
    }
}
