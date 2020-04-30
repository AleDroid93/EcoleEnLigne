package com.example.ecoleenligne.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.Chapter;
import com.example.ecoleenligne.models.Course;

import java.util.ArrayList;

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.ChapterViewHolder>{
    private ArrayList<Chapter> mChapters;

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout chHeader;
        public TextView chNumber;
        public TextView chTitle;
        public ImageView chArrow;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chHeader = itemView.findViewById(R.id.header_layout);
            chNumber = itemView.findViewById(R.id.tv_chapter_number);
            chTitle = itemView.findViewById(R.id.tv_chapter_title);
        }
    }

    public ArrayList<Chapter> getmChapters() {
        return mChapters;
    }

    public void setmChapters(ArrayList<Chapter> mChapters) {
        this.mChapters = mChapters;
    }

    public ChaptersAdapter() {
        this.mChapters = new ArrayList<>();
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter_card_view, parent, false);
        ChaptersAdapter.ChapterViewHolder vh = new ChaptersAdapter.ChapterViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = mChapters.get(position);
        String chapterNumber = "Chapter "+ chapter.getNumber();
        String title = chapter.getTitle();
        // TODO settare anche il colore della freccia
        holder.chTitle.setText(title);
        holder.chNumber.setText(chapterNumber);
    }

    @Override
    public int getItemCount() {
        return this.mChapters.size();
    }

    public ChaptersAdapter(ArrayList<Chapter> mChapters) {
        this.mChapters = mChapters;
    }
}
