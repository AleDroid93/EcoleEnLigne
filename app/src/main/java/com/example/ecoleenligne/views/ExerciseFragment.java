package com.example.ecoleenligne.views;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.ExerciseSliderAdapter;
import com.example.ecoleenligne.models.Exercise;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseFragment extends Fragment {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextView tvQuestion;
    private Button prevBtn;
    private Button nextBtn;
    private ViewPager exercisePager;
    private LinearLayout dotsPager;
    private ExerciseSliderAdapter adapter;
    private ArrayList<TextView> dots;
    private ViewPager.OnPageChangeListener pageListener;

    private int nExercises;
    private int currentQuestion;
    private Exercise currentExercise;

    public ExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvQuestion = view.findViewById(R.id.tv_exercise_question);
        prevBtn = view.findViewById(R.id.prevBtn);
        nextBtn = view.findViewById(R.id.nextBtn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "prev clicked", Toast.LENGTH_SHORT).show();
                exercisePager.setCurrentItem(currentQuestion - 1);
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "next clicked", Toast.LENGTH_SHORT).show();
                if(((Button)v).getText().equals(getResources().getString(R.string.finish_hint))) {
                    int result = adapter.evaluateExercise();
                    int maxResult = adapter.getCount();
                    //TODO - aggiungere result alle statistiche, insieme a quizdone
                    Toast.makeText(getActivity(), "Quiz result: " + result + "/" + maxResult, Toast.LENGTH_SHORT).show();
                }else {
                    exercisePager.setCurrentItem(currentQuestion + 1);
                }
            }
        });
        currentQuestion = 0;
        pageListener = getPageListener();
        spawnExercise();

        exercisePager = view.findViewById(R.id.quiz_pager);
        dotsPager = view.findViewById(R.id.dots_pager);
    }
    public void spawnExercise(){

    }



    public ViewPager.OnPageChangeListener getPageListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
                if(position == 0){
                    nextBtn.setEnabled(true);
                    prevBtn.setEnabled(false);
                    prevBtn.setVisibility(View.INVISIBLE);
                    prevBtn.setText("");
                }else if(position  == adapter.getCount()-1){
                    nextBtn.setEnabled(true);
                    prevBtn.setEnabled(true);
                    prevBtn.setVisibility(View.VISIBLE);
                    nextBtn.setText(getResources().getString(R.string.finish_hint));
                    prevBtn.setText(getResources().getString(R.string.prev_hint));
                }else{
                    nextBtn.setEnabled(true);
                    prevBtn.setEnabled(true);
                    prevBtn.setVisibility(View.VISIBLE);
                    prevBtn.setText(getResources().getString(R.string.prev_hint));
                    nextBtn.setText(getResources().getString(R.string.next_hint));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }


    private void addDotsIndicator(int position) {
        dots = new ArrayList<>();
        dotsPager.removeAllViews();
        currentQuestion = position;
        for (int i=0; i < adapter.getCount(); i++){
            TextView dot = new TextView(getActivity());
            dot.setText(Html.fromHtml("&#8226"));
            dot.setTextSize(35);
            if(i == position)
                dot.setTextColor(getResources().getColor(R.color.colorAccent2));
            else
                dot.setTextColor(getResources().getColor(R.color.grey));
            dots.add(dot);
            dotsPager.addView(dot);
        }
    }
}
