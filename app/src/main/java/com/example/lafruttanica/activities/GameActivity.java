package com.example.lafruttanica.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.lafruttanica.R;
import com.example.lafruttanica.gamehandlers.GameManager;

public class GameActivity extends AppCompatActivity {
    private GameManager gameManager;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Getting the width and height of the screen:
        final Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        // Creating the game manager:
        gameManager = new GameManager(this, point.x, point.y); // Point.x is the screen's width, Point.y is the screen's height

        setContentView(gameManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resuming the gameManager:
        this.gameManager.resume();
        // Resuming the music from the beginning:
        mediaPlayer = MediaPlayer.create(this, R.raw.game_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        /*
         * Credit to the music:
         * https://www.youtube.com/watch?v=n3uQ2aVzPbk&list=PLFM-Uv4841gxZDGVzkVCDf-kzYNsRpxKK&ab_channel=Larachma
         */
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pausing the game:
        this.gameManager.pause();
        // Pausing the music:
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}