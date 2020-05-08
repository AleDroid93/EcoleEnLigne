package com.example.ecoleenligne.views;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.activities.VideoContentActivity;
import com.example.ecoleenligne.adapters.VideosAdapter;
import com.example.ecoleenligne.models.Video;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideosFragment extends Fragment {

    private RecyclerView mVideoRecyclerView;
    private VideosAdapter mVideosAdapter;


    public VideosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_videos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeActivity parentActivity = (HomeActivity) getActivity();
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList<Video> videos = getArguments().getParcelableArrayList("videos");
        videos = filterNonEmptyVideos(videos);

        mVideoRecyclerView = view.findViewById(R.id.video_recycler_view);
        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        mVideoRecyclerView.setHasFixedSize(true);

        mVideosAdapter = new VideosAdapter(videos, parentActivity);
        mVideoRecyclerView.setAdapter(mVideosAdapter);
    }

    private ArrayList<Video> filterNonEmptyVideos(ArrayList<Video> videos) {
        ArrayList<Video> filteredVideos = new ArrayList<>();
        for(Video video : videos)
            if(!video.isEmpty())
                filteredVideos.add(video);
        return filteredVideos;
    }
}
