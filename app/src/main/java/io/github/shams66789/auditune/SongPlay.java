package io.github.shams66789.auditune;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class SongPlay extends AppCompatActivity {

    TextView textView;
    ImageView play;
    ImageView next;
    ImageView previous;
    ArrayList<File> songs;
    String textContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_play);
        textView = findViewById(R.id.textView2);
        play = findViewById(R.id.imageView5);
        next = findViewById(R.id.imageView6);
        previous = findViewById(R.id.imageView7);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("SongList");
        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);
    }
}