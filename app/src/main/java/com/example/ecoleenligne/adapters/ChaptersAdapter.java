package com.example.ecoleenligne.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.Chapter;
import com.example.ecoleenligne.models.Course;

import java.util.ArrayList;

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.ChapterViewHolder>{
    private ArrayList<Chapter> mChapters;
    private int color;
    private Context context;
    private View.OnClickListener listener;

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout chHeader;
        public TextView chNumber;
        public TextView chTitle;
        public ImageView chArrow;
        public Context context;
        public int color;
        public View.OnClickListener listener;

        public ChapterViewHolder(@NonNull View itemView, Context ctx, int color, View.OnClickListener listener) {
            super(itemView);
            chHeader = itemView.findViewById(R.id.header_layout);
            chNumber = itemView.findViewById(R.id.tv_chapter_number);
            chTitle = itemView.findViewById(R.id.tv_chapter_title);
            chArrow = itemView.findViewById(R.id.arrowBtn);
            chArrow.setOnClickListener(listener);
            listener = listener;
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(ctx, R.drawable.ic_down);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, color);
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
        ChapterViewHolder vh = new ChapterViewHolder(view, context, color, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = mChapters.get(position);
        String chapterNumber = "Chapter "+ chapter.getNumber();
        String title = chapter.getTitle();
        holder.chTitle.setText(title);
        holder.chTitle.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.chNumber.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.chNumber.setText(chapterNumber);
    }

    @Override
    public int getItemCount() {
        return this.mChapters.size();
    }

    public ChaptersAdapter(ArrayList<Chapter> mChapters) {
        this.mChapters = mChapters;
    }

    public ChaptersAdapter(Context ctx, int color, View.OnClickListener listener, ArrayList<Chapter> mChapters) {
        this.mChapters = mChapters;
        this.color = color;
        this.context = ctx;
        this.listener = listener;
    }
}
