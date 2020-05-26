package com.example.ecoleenligne.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.Video;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder>{
    private ArrayList<Video> mVideos;
    private Context context;

    public VideosAdapter() {
        this.mVideos = new ArrayList<>();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_card_view_item, parent, false);
        VideosAdapter.VideoViewHolder vh = new VideosAdapter.VideoViewHolder(view, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = mVideos.get(position);
        String videoTitle = video.getTitle();
        holder.tvVideoTitle.setText(videoTitle);
        String url = video.getUrl();
        Uri videoUrl = Uri.parse(url);
        holder.videoPlayer.setVideoURI(videoUrl);
    }

    @Override
    public int getItemCount() {
        return this.mVideos.size();
    }


    public VideosAdapter(ArrayList<Video> mVideos, Context context) {
        this.mVideos = mVideos;
        this.context = context;
    }

    public ArrayList<Video> getmVideos() {
        return mVideos;
    }

    public void setmVideos(ArrayList<Video> mVideos) {
        this.mVideos = mVideos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * ViewHolder Inner Classroom
     */
    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout videoLayoutHeader;
        public TextView tvVideoTitle;
        public ImageButton btnVideoPlay;
        public VideoView videoPlayer;
        public MediaController mediaController;
        public Context ctx;


        public VideoViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            videoLayoutHeader = itemView.findViewById(R.id.header_layout);
            tvVideoTitle = itemView.findViewById(R.id.tv_video_item_title);
            mediaController = new MediaController(ctx);
            ConstraintSet constraints = new ConstraintSet();
            constraints.clone(videoLayoutHeader);
            constraints.connect(mediaController.getId(), ConstraintSet.END, videoLayoutHeader.getId(), ConstraintSet.END, 0);
            constraints.connect(mediaController.getId(), ConstraintSet.START, videoLayoutHeader.getId(), ConstraintSet.START, 0);
            constraints.connect(mediaController.getId(), ConstraintSet.BOTTOM, videoLayoutHeader.getId(), ConstraintSet.TOP, 0);
            constraints.applyTo(videoLayoutHeader);
            videoPlayer = itemView.findViewById(R.id.video_player);
            videoPlayer.setMediaController(mediaController);
            mediaController.setAnchorView(videoPlayer);
            btnVideoPlay = itemView.findViewById(R.id.play_btn);
            this.ctx = ctx;

            btnVideoPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mediaController.isShowing()) {
                        v.setVisibility(View.GONE);
                        videoPlayer.start();
                    }
                }
            });
            videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.d("VideoContentActivity", "fideo finished!");
                    Toast.makeText(ctx,"Video Finshed!", Toast.LENGTH_SHORT).show();
                    btnVideoPlay.setVisibility(View.VISIBLE);
                }
            });
        }


    }
}
