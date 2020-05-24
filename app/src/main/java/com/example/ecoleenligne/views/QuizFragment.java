package com.example.ecoleenligne.views;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
import com.example.ecoleenligne.adapters.QuizSliderAdapter;
import com.example.ecoleenligne.models.Notification;
import com.example.ecoleenligne.models.Quiz;
import com.example.ecoleenligne.viewmodels.NotificationViewModel;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;

    private TextView tvQuestion;
    private Chip choice1;
    private Chip choice2;
    private Chip choice3;
    private Chip choice4;
    private Button prevBtn;
    private Button nextBtn;
    private ViewPager quizPager;
    private LinearLayout dotsPager;
    private QuizSliderAdapter adapter;
    private ArrayList<TextView> dots;
    private ViewPager.OnPageChangeListener pageListener;
    private NotificationViewModel notificationViewModel;
    private Observer<String> observerNotification;

    private int nQuiz;
    private int currentQuestion;
    private Quiz currentQuiz;



    public QuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        tvQuestion = view.findViewById(R.id.question);
        choice1 = view.findViewById(R.id.chipChoice1);
        choice2 = view.findViewById(R.id.chipChoice2);
        choice3 = view.findViewById(R.id.chipChoice3);
        choice4 = view.findViewById(R.id.chipChoice4);
        prevBtn = view.findViewById(R.id.prevBtn);
        nextBtn = view.findViewById(R.id.nextBtn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "prev clicked", Toast.LENGTH_SHORT).show();
                quizPager.setCurrentItem(currentQuestion - 1);
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "next clicked", Toast.LENGTH_SHORT).show();
                if(((Button)v).getText().equals(getResources().getString(R.string.finish_hint))) {
                    int result = adapter.evaluateQuiz();
                    int maxResult = adapter.getCount();
                    //TODO - aggiungere result alle statistiche, insieme a quizdone
                    Toast.makeText(getActivity(), "Quiz result: " + result + "/" + maxResult, Toast.LENGTH_SHORT).show();
                    String datetime = getCurrentLocalDateTimeStamp();
                    // TODO - farsi passare il quiz number, currentLesson e currentCourse
                    Notification notification = new Notification("X course Y title","quiz","You've completed a quiz of the lesson x",datetime, true);
                    LiveData<String> repo = notificationViewModel.getMutableNotificationMessage();
                    String uid = mAuth.getCurrentUser().getUid();
                    notificationViewModel.putNotification(uid, notification);
                    repo.observe(getActivity(), observerNotification);
                }else {
                    quizPager.setCurrentItem(currentQuestion + 1);
                }
            }
        });
        currentQuestion = 0;
        pageListener = getPageListener();
        spawnQuiz();

        quizPager = view.findViewById(R.id.quiz_pager);
        dotsPager = view.findViewById(R.id.dots_pager);

        HomeActivity parentActivity = (HomeActivity) getActivity();
        notificationViewModel = parentActivity.getNotificationViewModel();
        observerNotification = parentActivity.getNotificationMessageObserver();
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


    public void spawnQuiz(){
        String urlQuizzes = "quizzes";
        DatabaseReference reference = database.getReference(urlQuizzes);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double max =  dataSnapshot.getChildrenCount();
                nQuiz =  getRandomQuizNumber(0.0, max);
                DatabaseReference quizReference = database.getReference(urlQuizzes+"/"+nQuiz);
                quizReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        currentQuiz = dataSnapshot.getValue(Quiz.class);
                        adapter = new QuizSliderAdapter(QuizFragment.this, currentQuiz.getQuestions());

                        quizPager.setAdapter(adapter);
                        addDotsIndicator(0);
                        quizPager.addOnPageChangeListener(pageListener);
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

    public int getRandomQuizNumber(double min, double max){
        double x = Math.random()*(max);
        return (int) x;
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

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public void sendNotification(String uid) {
        String datetime = getCurrentLocalDateTimeStamp();
        // TODO - farsi passare il quiz number, currentLesson e currentCourse
        Notification notification = new Notification("X course Y title","quiz","You've completed a quiz of the lesson x",datetime, true);
        LiveData<String> repo = notificationViewModel.getMutableNotificationMessage();
        notificationViewModel.putNotification(uid, notification);
        repo.observe(getActivity(), observerNotification);
    }

    public String getCurrentLocalDateTimeStamp() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa",
                Locale.ENGLISH);
        String var = dateFormat.format(date);
        return var;
    }
}
