package com.example.lafruttanica.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.lafruttanica.R;

/**
 * Handles the opening screen for the game.
 */
public class OpeningActivity extends AppCompatActivity {
    private boolean isMuted; /* Whether the player wants to mute the music or not */
    private ImageView mutingBtn; /* The view that allows the user to toggle the music */
    private MediaPlayer mediaPlayer; /* Media player for music */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        // Setting the muted variable to false:
        this.isMuted = false;

        // Loading and starting the media player:
        this.mediaPlayer = MediaPlayer.create(this, R.raw.opening_music);
        this.mediaPlayer.start();
        this.mediaPlayer.setLooping(true);

        // Loading the muting button:
        this.mutingBtn = findViewById(R.id.muting_btn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Initializes the media player for playing music if it isn't muted:
        if (!isMuted)
            this.mediaPlayer.start();
        /*
         * Credit to the music:
         * https://www.youtube.com/watch?v=Qq4_kxdP468&list=PL_Z-vyXvZD7gac7I8eRuXn0CKceSpSVcN&index=2&ab_channel=PupClub
        */
    }

    public void changeMuted(View view) {
        // Reversing the muted variable:
        this.isMuted = !this.isMuted;

        // Changing the image of the muting button:
        if (this.isMuted) {
            // Changing the image:
            this.mutingBtn.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.muted_icon));
            // Pausing the media player:
            this.mediaPlayer.pause();
        }
        else {
            // Changing the image:
            this.mutingBtn.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.unmuted_icon));
            // Restarting the music:
            this.mediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pausing the music if it isn't muted:
        try {
            if (!isMuted) {
                this.mediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(View view) {
        // If the user pressed the play button, open the main game activity:
        if (view.getId() == R.id.play_btn) {
            Intent intent = new Intent(this, GameActivity.class);

            // Telling the game activity if the music is muted or not:
            intent.putExtra("isMuted", this.isMuted);

            // Stopping the music:
            if (!this.isMuted) {
                this.mediaPlayer.stop();
                this.mediaPlayer.release();
            }

            // Starting the game activity:
            startActivity(intent);
        }
    }
}