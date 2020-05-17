package com.example.ecoleenligne.views;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.ExerciseSliderAdapter;
import com.example.ecoleenligne.models.Exercise;
import com.example.ecoleenligne.models.ExerciseItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseFragment extends Fragment {
    private final static String TAG = "ExerciseFragment";
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextView tvQuestion;
    private Button prevBtn;
    private Button nextBtn;
    private Button submitBtn;
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
        submitBtn = view.findViewById(R.id.btn_submit_exercise);
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
                View view = exercisePager.getFocusedChild();
                EditText edt = view.findViewById(R.id.edt_exercise_answer);
                String currentAnswer = edt.getText().toString();
                adapter.addAnswer(currentAnswer, currentQuestion);
                exercisePager.setCurrentItem(++currentQuestion);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int result = adapter.evaluateExercise();
                //TODO - aggiungere result alle statistiche, insieme a quiz done
            }
        });

        currentQuestion = 0;
        pageListener = getPageListener();

        exercisePager = view.findViewById(R.id.exercise_pager);
        dotsPager = view.findViewById(R.id.dots_pager);
        spawnExercise();
    }


    public void spawnExercise(){
        String urlExercises = "exercises/cl0/cs0/ch0/ls0";
        DatabaseReference reference = database.getReference(urlExercises);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double max =  dataSnapshot.getChildrenCount();
                int exerciseIndex =  (int) max;
                DatabaseReference exercisesRef = database.getReference(urlExercises);
                exercisesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        currentExercise = dataSnapshot.getValue(Exercise.class);
                        Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> it = iterable.iterator();
                        while(it.hasNext()){
                            DataSnapshot data = it.next();
                            Log.e(TAG, data.getValue().toString());
                            Iterable<DataSnapshot> iter = data.getChildren();
                            Iterator<DataSnapshot> itQuestions = iter.iterator();
                            ArrayList<ExerciseItem> questions = new ArrayList<>();
                            while(itQuestions.hasNext()){
                                ExerciseItem question = itQuestions.next().getValue(ExerciseItem.class);
                                questions.add(question);
                            }
                            data = it.next();
                            Log.e(TAG, data.getValue().toString());
                            String title = data.getValue(String.class);
                            currentExercise = new Exercise(title, questions);
                        }
                        if (currentExercise != null) {
                            adapter = new ExerciseSliderAdapter(ExerciseFragment.this, currentExercise.getTitle(), currentExercise.getExercises());
                        }
                        exercisePager.setAdapter(adapter);
                        addDotsIndicator(0);
                        exercisePager.addOnPageChangeListener(pageListener);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                    submitBtn.setEnabled(false);
                    submitBtn.setVisibility(View.INVISIBLE);
                    prevBtn.setEnabled(false);
                    prevBtn.setVisibility(View.INVISIBLE);
                    prevBtn.setText("");
                }else if(position  == adapter.getCount()-1){
                    nextBtn.setEnabled(false);
                    prevBtn.setEnabled(true);
                    submitBtn.setEnabled(true);
                    submitBtn.setVisibility(View.VISIBLE);
                    prevBtn.setVisibility(View.VISIBLE);
                    nextBtn.setVisibility(View.INVISIBLE);
                    prevBtn.setText(getResources().getString(R.string.prev_hint));
                }else{
                    prevBtn.setEnabled(true);
                    nextBtn.setEnabled(true);
                    submitBtn.setEnabled(false);
                    submitBtn.setVisibility(View.INVISIBLE);
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
