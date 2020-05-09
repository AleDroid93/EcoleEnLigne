package com.example.ecoleenligne.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.activities.QuizActivity;
import com.example.ecoleenligne.models.QuizItem;
import com.example.ecoleenligne.views.QuizFragment;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class QuizSliderAdapter extends PagerAdapter {
    Fragment context;
    LayoutInflater inflater;
    ArrayList<QuizItem> questions;
    ArrayList<Integer> answers;

    public QuizSliderAdapter(Fragment context, ArrayList<QuizItem> items) {
        this.context = context;
        this.questions = items;
        this.answers  = new ArrayList<>(this.questions.size());
        for(int i = 0; i < questions.size(); i++)
            answers.add(0);
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getActivity().getSystemService(context.getActivity().LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.quiz_slide_layout, container, false);
        TextView tvQuestion = view.findViewById(R.id.question);
        Chip btnChoice1 = view.findViewById(R.id.chipChoice1);
        Chip btnChoice2 = view.findViewById(R.id.chipChoice2);
        Chip btnChoice3 = view.findViewById(R.id.chipChoice3);
        Chip btnChoice4 = view.findViewById(R.id.chipChoice4);
        btnChoice1.setOnCheckedChangeListener(getOnCheckedChangeListener());
        btnChoice2.setOnCheckedChangeListener(getOnCheckedChangeListener());
        btnChoice3.setOnCheckedChangeListener(getOnCheckedChangeListener());
        btnChoice4.setOnCheckedChangeListener(getOnCheckedChangeListener());

        QuizItem question = questions.get(position);

        tvQuestion.setText(question.getQuestion());
        btnChoice1.setText(question.getChoice1());
        btnChoice2.setText(question.getChoice2());
        btnChoice3.setText(question.getChoice3());
        btnChoice4.setText(question.getChoice4());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }


    public CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener(){
        return new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                Chip chip = (Chip) view;
                QuizFragment fragment = (QuizFragment) context;
                int currentQuestion = fragment.getCurrentQuestion();
                if(isChecked){
                    chip.setChipBackgroundColorResource(R.color.colorAccent2);
                    chip.setChipStrokeColorResource(R.color.colorPrimaryDark);
                    chip.setChipIconVisible(false);
                    int answerId = getNumberOfChip(chip.getId());
                    answers.set(currentQuestion, answerId);
                    chip.setTextColor(context.getResources().getColor(R.color.white));
                }else{
                    chip.setChipBackgroundColorResource(R.color.white);
                    chip.setChipIconVisible(true);
                    chip.setTextColor(context.getResources().getColor(R.color.text_color));
                    chip.setChipStrokeColorResource(R.color.text_color);
                }
            }
        };
    }

    public int getNumberOfChip(int id){
        switch(id){
            case R.id.chipChoice1:
                return 1;
            case R.id.chipChoice2:
                return 2;
            case R.id.chipChoice3:
                return 3;
            case R.id.chipChoice4:
                return 4;
        }
        return 0;
    }

    public int evaluateQuiz(){
        int result = 0;
        for(QuizItem question : questions){
            if(question.getAnswer() == answers.get(questions.indexOf(question)))
                result++;
        }
        return result;
    }
}
