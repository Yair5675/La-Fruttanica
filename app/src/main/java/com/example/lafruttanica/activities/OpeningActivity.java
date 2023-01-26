package com.example.lafruttanica.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.lafruttanica.R;
import com.example.lafruttanica.activities.GameActivity;

public class OpeningActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        ImageView platform = findViewById(R.id.platform);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Initializes the media player for playing music:
        mediaPlayer = MediaPlayer.create(this, R.raw.opening_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        /*
         * Credit to the music:
         * https://www.youtube.com/watch?v=Qq4_kxdP468&list=PL_Z-vyXvZD7gac7I8eRuXn0CKceSpSVcN&index=2&ab_channel=PupClub
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pausing the music:
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public void play(View view) {
        if (view.getId() == R.id.play_btn) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }
    }
}