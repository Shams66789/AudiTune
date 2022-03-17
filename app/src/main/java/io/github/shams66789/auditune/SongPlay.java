package io.github.shams66789.auditune;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class SongPlay extends AppCompatActivity {

    TextView textView;
    ImageView play , next, previous;
    SeekBar seekBar;
    ArrayList<File> songs;
    String textContent;
    MediaPlayer mediaPlayer;
    int position;
    Thread updateSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_play);
        textView = findViewById(R.id.textView2);
        play = findViewById(R.id.imageView5);
        next = findViewById(R.id.imageView6);
        previous = findViewById(R.id.imageView7);
        seekBar = findViewById(R.id.seekBar2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("SongList");
        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateSeek = new Thread() {
            @Override
            public void run() {
                int currentPos = 0;
                try {
                    while (currentPos < mediaPlayer.getDuration()) {
                        currentPos = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPos);
                        sleep(800);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();

        play.setOnClickListener(v -> {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
            }else if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                play.setImageResource(R.drawable.play);
            }
        });

        next.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (position != songs.size() - 1) {
                position = position + 1;
            } else {
               position = 0;
            }
            Uri uri1 = Uri.parse(songs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);
            mediaPlayer.start();
            play.setImageResource(R.drawable.pause);
            seekBar.setMax(mediaPlayer.getDuration());
            textContent = songs.get(position).getName().toString();
            textView.setText(textContent);
        });

        previous.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (position != 0) {
                position = position - 1;
            } else {
                position = songs.size() - 1;
            }
            Uri uri1 = Uri.parse(songs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);
            mediaPlayer.start();
            play.setImageResource(R.drawable.pause);
            seekBar.setMax(mediaPlayer.getDuration());
            textContent = songs.get(position).getName().toString();
            textView.setText(textContent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }
}