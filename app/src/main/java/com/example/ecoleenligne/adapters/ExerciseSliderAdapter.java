package com.example.ecoleenligne.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.ExerciseItem;


import java.util.ArrayList;

public class ExerciseSliderAdapter extends PagerAdapter {
    Fragment context;
    LayoutInflater inflater;
    String title;
    ArrayList<ExerciseItem> exercises;
    ArrayList<String> answers;

    public ExerciseSliderAdapter(Fragment context, String title,ArrayList<ExerciseItem> exercises) {
        this.context = context;
        this.title = title;
        this.exercises = exercises;
        this.answers  = new ArrayList<>(this.exercises.size());
    }

    public ArrayList<ExerciseItem> getExercises() {
        return exercises;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    @Override
    public int getCount() {
        return exercises.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    public void addAnswer(String answer, int pos){
        answers.add(pos, answer);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getActivity().getSystemService(context.getActivity().LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exercise_slide_layout, container, false);
        TextView tvQuestion = view.findViewById(R.id.tv_exercise_question);
        TextView title = view.findViewById(R.id.tv_exercise_title);
        EditText edtAnswer = view.findViewById(R.id.edt_exercise_answer);
        title.setText(this.title);
        ExerciseItem exercise = exercises.get(position);
        tvQuestion.setText(exercise.getQuestion());
        container.addView(view);
        return view;
    }

    public int evaluateExercise(){
        // TODO - restituire le risposte in ogni esercizio per effettuare nel fragment una chiamata POST al db
        Toast.makeText(context.getActivity(),"Exercise evaluated" ,Toast.LENGTH_SHORT).show();
        return 1;
    }


}
