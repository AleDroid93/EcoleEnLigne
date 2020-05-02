package com.example.ecoleenligne.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.VideoViewHolder;
import com.example.ecoleenligne.adapters.VideosAdapter;
import com.example.ecoleenligne.models.Video;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class VideoContentActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private String urlDb;
    private VideoView videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList<Video> videos = getIntent().getParcelableArrayListExtra("videos");
        String urlDb = getIntent().getStringExtra("urlDb");
        this.urlDb = "videos/cl0/cs0/ch0/ls0";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        MediaController mediaController = new MediaController(this);
        videoPlayer= findViewById(R.id.video_player);
        videoPlayer.setMediaController(mediaController);
        mediaController.setAnchorView(videoPlayer);
        String token = "https://firebasestorage.googleapis.com/v0/b/ecoleenligne-1015c.appspot.com/o/video_4.mp4?alt=media&token=be3f10a9-1840-40ec-8ead-37f97b6f33f2";
        Uri uri = Uri.parse(token);
        videoPlayer.setVideoURI(uri);
        videoPlayer.start();
        //database = FirebaseDatabase.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return true;
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
