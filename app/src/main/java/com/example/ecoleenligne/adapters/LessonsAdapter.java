package com.example.ecoleenligne.adapters;

import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.Chapter;
import com.example.ecoleenligne.models.Course;
import com.example.ecoleenligne.models.Lesson;
import com.example.ecoleenligne.views.HomeActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.LessonViewHolder>{
    private ArrayList<Lesson> mLessons;
    private int color;
    private Course currentCourse;
    private Chapter currentChapter;
    private Context context;


    public LessonsAdapter(ArrayList<Lesson> mLessons, int color, Course currentCourse, Chapter currentChapter, Context context) {
        this.mLessons = mLessons;
        this.color = color;
        this.currentChapter = currentChapter;
        this.currentCourse = currentCourse;
        this.context = context;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_item_card_view, parent, false);
        LessonViewHolder vh = new LessonViewHolder(view, context, color, currentCourse, currentChapter);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = mLessons.get(position);
        String title = lesson.getTitle();
        holder.lsTitle.setText(title);
        holder.addLesson(lesson);
    }

    @Override
    public int getItemCount() {
        return mLessons.size();
    }

    /**
     * ViewHolder Inner Class
     */
    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        public MaterialCardView lessonItem;
        public TextView lsTitle;
        public Context ctx;
        public int color;
        public Chapter chapter;
        public Course course;
        public ArrayList<Lesson> lessons;



        public LessonViewHolder(@NonNull View itemView, Context ctx, int color, Course currentCourse, Chapter currentChapter) {
            super(itemView);
            this.lessons = new ArrayList<>();
            this.lessonItem = itemView.findViewById(R.id.lesson_item_card_view);
            this.lsTitle = itemView.findViewById(R.id.tv_lesson_title);
            this.ctx = ctx;
            this.course = currentCourse;
            this.chapter = currentChapter;
            this.color = color;

            lessonItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("LessonsAdapter", "chiamo il menu di scelta risorse");
                    HomeActivity parentActivity = (HomeActivity) ctx;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("user", parentActivity.getCurrentUser());

                    Lesson lesson = getLessonByTitle(lsTitle.getText().toString());
                    if(lesson != null)
                        bundle.putParcelable("lesson", lesson);
                    bundle.putParcelable("course", course);
                    bundle.putParcelable("chapter", chapter);
                    bundle.putInt("bgColor", color);
                    bundle.putString("lessonName", lsTitle.getText().toString());
                    parentActivity.getNavController().navigate(R.id.action_courseDetailFragment_to_lessonMenuFragment, bundle);
                }
            });
            lessonItem.setCardBackgroundColor(color);
        }

        public void addLesson(Lesson lesson){
            this.lessons.add(lesson);
        }

        public Lesson getLessonByTitle(String title){
            for(Lesson l : lessons){
                String lTitle = l.getTitle();
                if( lTitle.equals(title))
                    return l;
            }
            return null;
        }
    }
}
