package com.example.ecoleenligne.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.QuizItem;

import java.util.ArrayList;

public class QuizSliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<QuizItem> questions;

    public QuizSliderAdapter(Context context, ArrayList<QuizItem> items) {
        this.context = context;
        this.questions = items;
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
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.quiz_slide_layout, container, false);
        TextView tvQuestion = view.findViewById(R.id.question);
        Button btnChoice1 = view.findViewById(R.id.choice1);
        Button btnChoice2 = view.findViewById(R.id.choice2);
        Button btnChoice3 = view.findViewById(R.id.choice3);
        Button btnChoice4 = view.findViewById(R.id.choice4);

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
}
