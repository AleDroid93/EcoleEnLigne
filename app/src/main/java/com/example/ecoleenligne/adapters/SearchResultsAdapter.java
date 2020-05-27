package com.example.ecoleenligne.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.Course;

import java.util.ArrayList;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder> {
    private ArrayList<Course> mCourses;
    private Context ctx;
    private View.OnClickListener listener;

    @NonNull
    @Override
    public SearchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_card_view_item, parent, false);
        SearchResultsAdapter.SearchResultsViewHolder vh = new SearchResultsAdapter.SearchResultsViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsViewHolder holder, int position) {
        Course course = mCourses.get(position);
        String name = course.getName();
        int color = Color.parseColor(course.getColor());
        // - replace the contents of the view with that element
        Drawable chapterBoxDrawable = AppCompatResources.getDrawable(ctx, R.drawable.search_result_item_box);
        Drawable wrapChapterBoxDrawable = DrawableCompat.wrap(chapterBoxDrawable);
        DrawableCompat.setTint(wrapChapterBoxDrawable, color);
        holder.linearLayout.setBackground(wrapChapterBoxDrawable);
        holder.textView.setText(name);
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class SearchResultsViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public TextView textView;

        public SearchResultsViewHolder(@NonNull View v) {
            super(v);
            linearLayout = v.findViewById(R.id.header_course_layout);
            linearLayout.setOnClickListener(listener);
            textView = v.findViewById(R.id.tv_search_result_course_item);
        }
    }

    public SearchResultsAdapter(ArrayList<Course> courses, Context context, View.OnClickListener listener) {
        this.mCourses = courses;
        this.ctx = context;
        this.listener = listener;
    }

    public Course getCourseByName(String name){
        for(Course c : mCourses){
            if(c.getName().equalsIgnoreCase(name))
                return c;
        }
        return null;
    }
}
