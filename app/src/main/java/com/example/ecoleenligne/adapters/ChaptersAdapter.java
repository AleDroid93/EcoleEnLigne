package com.example.ecoleenligne.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.Chapter;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.ChapterViewHolder>{
    private ArrayList<Chapter> mChapters;
    private int color;
    private int lightColor;
    private Context context;


    public ChaptersAdapter(ArrayList<Chapter> mChapters) {
        this.mChapters = mChapters;
    }

    public ChaptersAdapter(Context ctx, int color, int lightColor, ArrayList<Chapter> mChapters) {
        this.mChapters = mChapters;
        this.color = color;
        this.lightColor = lightColor;
        this.context = ctx;
    }

    @Override
    public int getItemCount() {
        return this.mChapters.size();
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
        ChapterViewHolder vh = new ChapterViewHolder(view, context, color);
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
        holder.lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.lessonsRecyclerView.setAdapter(new LessonsAdapter(chapter.getLessons(), lightColor, context));

    }


    /**
     * ViewHolder Inner Class
     */
    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout chHeader;
        public TextView chNumber;
        public TextView chTitle;
        public ImageView chArrow;
        public RecyclerView lessonsRecyclerView;
        public Context ctx;
        public int color;


        public ChapterViewHolder(@NonNull View itemView, Context ctx, int color) {
            super(itemView);
            chHeader = itemView.findViewById(R.id.header_layout);
            chNumber = itemView.findViewById(R.id.tv_chapter_number);
            chTitle = itemView.findViewById(R.id.tv_chapter_title);
            chArrow = itemView.findViewById(R.id.arrowBtn);
            lessonsRecyclerView = itemView.findViewById(R.id.lessons_recycler_view);
            chHeader.setOnClickListener(getChapterClickListener(ctx, color));
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(ctx, R.drawable.ic_down);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, color);
            Drawable chapterBoxDrawable = AppCompatResources.getDrawable(ctx, R.drawable.chapter_item_box);
            Drawable wrapChapterBoxDrawable = DrawableCompat.wrap(chapterBoxDrawable);
            DrawableCompat.setTint(wrapChapterBoxDrawable,ctx.getResources().getColor(R.color.white) );
            chHeader.setBackground(wrapChapterBoxDrawable);
        }


        private View.OnClickListener getChapterClickListener(Context ctx, int chColor){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ChaptersAdapter", "arrow clicked");
                    TextView chTitle = v.findViewById(R.id.tv_chapter_title);
                    TextView chNumber = v.findViewById(R.id.tv_chapter_number);
                    Drawable unwrappedDrawable = AppCompatResources.getDrawable(ctx, R.drawable.chapter_item_box);
                    Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                    ImageView imView = (ImageView) v.findViewById(R.id.arrowBtn);
                    View expandableView = ((ViewGroup)v.getParent()).findViewById(R.id.expandableView);
                    MaterialCardView chapterCard = (MaterialCardView) v.getParent().getParent();
                    if(expandableView.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(chapterCard, new AutoTransition());
                        expandableView.setVisibility(View.VISIBLE);
                        imView.setImageResource(R.drawable.ic_up);
                        DrawableCompat.setTint(wrappedDrawable, chColor);
                        chTitle.setTextColor(ctx.getResources().getColor(R.color.white));
                        chNumber.setTextColor(ctx.getResources().getColor(R.color.white));
                    }else {
                        TransitionManager.beginDelayedTransition(chapterCard, new AutoTransition());
                        expandableView.setVisibility(View.GONE);
                        imView.setImageResource(R.drawable.ic_down);
                        DrawableCompat.setTint(wrappedDrawable, ctx.getResources().getColor(R.color.white));
                        chTitle.setTextColor(ctx.getResources().getColor(R.color.text_color));
                        chNumber.setTextColor(ctx.getResources().getColor(R.color.text_color));
                    }
                    v.setBackground(wrappedDrawable);
                }
            };
        }
    }

}
