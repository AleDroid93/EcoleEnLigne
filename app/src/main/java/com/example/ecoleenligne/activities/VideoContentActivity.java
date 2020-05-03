package com.example.ecoleenligne.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.example.ecoleenligne.R;

import com.example.ecoleenligne.adapters.VideosAdapter;
import com.example.ecoleenligne.models.Video;

import java.util.ArrayList;

//TODO (1) - progress loop nei video
//TODO (2) - risolvere bug del media controller on scrolling
public class VideoContentActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mVideoRecyclerView;
    private VideosAdapter mVideosAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList<Video> videos = getIntent().getParcelableArrayListExtra("videos");
        videos = filterNonEmptyVideos(videos);

        mVideoRecyclerView = findViewById(R.id.video_recycler_view);
        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mVideoRecyclerView.setHasFixedSize(true);

        mVideosAdapter = new VideosAdapter(videos, VideoContentActivity.this);
        mVideoRecyclerView.setAdapter(mVideosAdapter);
    }

    private ArrayList<Video> filterNonEmptyVideos(ArrayList<Video> videos) {
        ArrayList<Video> filteredVideos = new ArrayList<>();
        for(Video video : videos)
            if(!video.isEmpty())
                filteredVideos.add(video);
        return filteredVideos;
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

    @Override
    public void onClick(View v) {

    }
}
