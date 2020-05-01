package com.example.ecoleenligne.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.Course;

import java.util.ArrayList;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ClassroomViewHolder> {
    private ArrayList<Course> mCourses;
    private View.OnClickListener listener;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ClassroomViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public TextView textView;

        public ClassroomViewHolder(View v, View.OnClickListener listener) {
            super(v);
            cardView = v.findViewById(R.id.chapter_card_view);
            cardView.setOnClickListener(listener);
            textView = v.findViewById(R.id.tv_course_name);
        }
    }

    public void setmCourses(ArrayList<Course> mCourses) {
        this.mCourses = mCourses;
    }

    public ArrayList<Course> getmCourses() {
        return mCourses;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ClassroomAdapter(ArrayList<Course> courses, View.OnClickListener listener) {
        this.mCourses = courses;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ClassroomAdapter.ClassroomViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_card_view_item, parent, false);
        ClassroomViewHolder vh = new ClassroomViewHolder(view, listener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ClassroomViewHolder holder, int position) {
        // - get element from your dataset at this position
        Course course = mCourses.get(position);
        String name = course.getName();
        int color = Color.parseColor(course.getColor());
        // - replace the contents of the view with that element
        holder.cardView.setCardBackgroundColor(color);
        holder.textView.setText(name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCourses.size();
    }


}
