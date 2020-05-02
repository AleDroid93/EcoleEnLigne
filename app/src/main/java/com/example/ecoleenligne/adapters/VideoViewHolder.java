package com.example.ecoleenligne.adapters;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoleenligne.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class VideoViewHolder extends RecyclerView.ViewHolder {
    View view;
    SimpleExoPlayer exoPlayer;
    PlayerView playerView;


    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setVideo(final Application ctx, String title, String url){

        try{
            BandwidthMeter meter = new DefaultBandwidthMeter.Builder(ctx).build();
            TrackSelection.Factory selectionFactory = new AdaptiveTrackSelection.Factory();
            TrackSelector selector = new DefaultTrackSelector(ctx, selectionFactory);
            exoPlayer = (SimpleExoPlayer) new SimpleExoPlayer.Builder(ctx)
                    .setBandwidthMeter(meter)
                    .build();
            Uri video = Uri.parse(url);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(ctx,
                    Util.getUserAgent(ctx, "videos/cl0/cs0/ch0/ls0"));
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource =
                    new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(video);
            // Prepare the player with the source.
            playerView.setPlayer(exoPlayer);
            exoPlayer.prepare(videoSource);
            exoPlayer.setPlayWhenReady(false);
        }catch (Exception ex){
            Log.e("VideosAdapter", ex.getMessage());
        }
    }
}
