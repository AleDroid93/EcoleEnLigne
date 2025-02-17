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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.OnboardingStudentAdapter;

import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardingFragment extends Fragment {
    private Button prevBtn;
    private Button nextBtn;
    private ImageView imgClose;
    private OnboardingStudentAdapter adapter;
    private ViewPager.OnPageChangeListener pageListener;
    private ViewPager onboardingPager;
    private LinearLayout dotsPager;
    private ArrayList<TextView> dots;
    private int currentSlide;


    public OnboardingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgClose = view.findViewById(R.id.img_close);
        imgClose.bringToFront();
        prevBtn = view.findViewById(R.id.prevBtn);
        nextBtn = view.findViewById(R.id.nextBtn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "prev clicked", Toast.LENGTH_SHORT).show();
                onboardingPager.setCurrentItem(currentSlide - 1);
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "next clicked", Toast.LENGTH_SHORT).show();
                if (((Button) v).getText().equals(getResources().getString(R.string.finish_hint))) {
                    HomeActivity parentActivity = (HomeActivity) getActivity();
                    parentActivity.getNavController().popBackStack();
                } else {
                    onboardingPager.setCurrentItem(currentSlide + 1);
                }
            }
        });
        currentSlide = 0;
        pageListener = getPageListener();

        onboardingPager = view.findViewById(R.id.onboarding_pager);
        dotsPager = view.findViewById(R.id.dots_onboarding_pager);

        HomeActivity parentActivity = (HomeActivity) getActivity();
        parentActivity.getSupportActionBar().hide();
        parentActivity.getBottomNavigationView().setVisibility(View.GONE);
        adapter = new OnboardingStudentAdapter(parentActivity);
        onboardingPager.setAdapter(adapter);
        addDotsIndicator(0);
        onboardingPager.addOnPageChangeListener(pageListener);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Ehi", "Close clicked!");
                HomeActivity parentActivity = (HomeActivity) getActivity();
                parentActivity.getNavController().popBackStack();
            }
        });
    }

    private void addDotsIndicator(int position) {
        dots = new ArrayList<>();
        dotsPager.removeAllViews();
        currentSlide = position;
        for (int i = 0; i < adapter.getCount(); i++) {
            TextView dot = new TextView(getActivity());
            dot.setText(Html.fromHtml("&#8226"));
            dot.setTextSize(35);
            if (i == position)
                dot.setTextColor(getResources().getColor(R.color.colorAccent2));
            else
                dot.setTextColor(getResources().getColor(R.color.grey));
            dots.add(dot);
            dotsPager.addView(dot);
        }
    }


    public ViewPager.OnPageChangeListener getPageListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
                if (position == 0) {
                    nextBtn.setEnabled(true);
                    prevBtn.setEnabled(false);
                    prevBtn.setVisibility(View.INVISIBLE);
                    prevBtn.setText("");
                } else if (position == adapter.getCount() - 1) {
                    nextBtn.setEnabled(true);
                    prevBtn.setEnabled(true);
                    prevBtn.setVisibility(View.VISIBLE);
                    nextBtn.setText(getResources().getString(R.string.finish_hint));
                    prevBtn.setText(getResources().getString(R.string.prev_hint));
                } else {
                    nextBtn.setEnabled(true);
                    prevBtn.setEnabled(true);
                    prevBtn.setVisibility(View.VISIBLE);
                    prevBtn.setText(getResources().getString(R.string.prev_hint));
                    nextBtn.setText(getResources().getString(R.string.next_hint));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        };
    }

    @Override
    public void onStop() {
        HomeActivity parentActivity = (HomeActivity) getActivity();
        parentActivity.getSupportActionBar().show();
        parentActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
        super.onStop();

    }
}
