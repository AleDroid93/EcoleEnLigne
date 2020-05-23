package com.example.ecoleenligne.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.QuizItem;

import java.util.ArrayList;


public class OnboardingStudentAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;

    private int[] slideIcons = {R.drawable.search, R.drawable.sheet, R.drawable.save,
            R.drawable.video, R.drawable.exam, R.drawable.mark};

    private int[] slideHeadings = {R.string.headingSearch, R.string.headingStudy, R.string.headingSave,
            R.string.headingVideo, R.string.headingTest, R.string.headingMark};

    private int[] slideTexts = {R.string.descSearch, R.string.descStudy, R.string.descSave,
            R.string.descVideo, R.string.descTest, R.string.descMark};

    public OnboardingStudentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return slideHeadings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.onboard_slide_layout, container, false);
        TextView tvHeading = view.findViewById(R.id.onboarding_header_text);
        TextView tvBody = view.findViewById(R.id.onboarding_body_text);
        ImageView img = view.findViewById(R.id.onboarding_img);
        String headingText = context.getString(slideHeadings[position]);
        String bodyText = context.getString(slideTexts[position]);
        img.setImageResource(slideIcons[position]);
        tvHeading.setText(headingText);
        tvBody.setText(bodyText);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }



}
