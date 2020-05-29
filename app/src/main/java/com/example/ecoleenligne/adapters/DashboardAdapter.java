package com.example.ecoleenligne.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.Statistics;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>{
    private ArrayList<Statistics> mStats;
    private Context context;


    public DashboardAdapter(ArrayList<Statistics> mStats) {
        this.mStats = mStats;
    }

    public DashboardAdapter(Context ctx, ArrayList<Statistics> mStats) {
        this.mStats = mStats;
        this.context = ctx;
    }

    public ArrayList<Statistics> getmStats() {
        return mStats;
    }

    public void setmStats(ArrayList<Statistics> mStats) {
        this.mStats = mStats;
    }

    public void clear(){
        mStats.clear();
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stats_course_view, parent, false);
        DashboardViewHolder vh = new DashboardViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        Statistics stats = mStats.get(position);
        String courseName = stats.getCourseName() +" stats:";
        holder.tvCourseName.setText(courseName);
        if(stats.getNumQuizzes() > 0)
            holder.tvNumQuizzes.setText(String.valueOf(stats.getNumQuizzes()));
        if(stats.getAvgMark() > 0.0)
            holder.tvAvg.setText(String.valueOf(stats.getAvgMark()));
        if(stats.getNumExercises() > 0)
            holder.tvNumEx.setText(String.valueOf(stats.getNumExercises()));
        holder.tvCourseName.setTextColor(Color.parseColor(stats.getColor()));

    }

    @Override
    public int getItemCount() {
        return mStats.size();
    }


    /**
     * ViewHolder Inner Classroom
     */
    public static class DashboardViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout statsLayout;
        public TextView tvCourseName;
        public TextView tvNumEx;
        public TextView tvNumQuizzes;
        public TextView tvAvg;


        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            statsLayout = itemView.findViewById(R.id.stats_course_layout);
            tvCourseName = itemView.findViewById(R.id.tv_stats_course_name);
            tvNumEx = itemView.findViewById(R.id.tv_numEx);
            tvNumQuizzes = itemView.findViewById(R.id.tv_numQuiz);
            tvAvg = itemView.findViewById(R.id.tv_mark);

        }

    }
}
