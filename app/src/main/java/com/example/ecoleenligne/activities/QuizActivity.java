package com.example.ecoleenligne.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.QuizSliderAdapter;
import com.example.ecoleenligne.models.Quiz;
import com.example.ecoleenligne.models.QuizItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextView tvQuestion;
    private Button choice1;
    private Button choice2;
    private Button choice3;
    private Button choice4;

    private ViewPager quizPager;
    private LinearLayout dotsPager;
    private QuizSliderAdapter adapter;
    private int nQuiz;
    private int currentQuestion;
    private Quiz currentQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        tvQuestion = findViewById(R.id.question);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);
        choice4 = findViewById(R.id.choice4);
        currentQuestion = 0;
        spawnQuiz();

        quizPager = findViewById(R.id.quiz_pager);
        dotsPager = findViewById(R.id.dots_pager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return true;
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
                        adapter = new QuizSliderAdapter(QuizActivity.this, currentQuiz.getQuestions());
                        quizPager.setAdapter(adapter);
                        //nextQuestion(currentQuiz);
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

    public void nextQuestion(Quiz quiz){
        QuizItem quizItem = new QuizItem();
        if(currentQuestion < quiz.getQuestions().size()) {
            quizItem = quiz.getQuestions().get(currentQuestion);

            choice1.setText(quizItem.getChoice1());
            choice2.setText(quizItem.getChoice2());
            choice3.setText(quizItem.getChoice3());
            choice4.setText(quizItem.getChoice4());
            tvQuestion.setText(quizItem.getQuestion());
            currentQuestion++;
        }else{
            Toast.makeText(this, "Quiz ended", Toast.LENGTH_SHORT).show();
        }
    }

    public int getRandomQuizNumber(double min, double max){
        double x = Math.random()*(max);
        return (int) x;
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        switch (v.getId()){
            case R.id.choice1:
                if(button.getText().equals(currentQuiz.getQuestions().get(currentQuestion).getAnswerTextByIndex()))
                    Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                break;
            case R.id.choice2:
                break;
            case R.id.choice3:
                break;
            case R.id.choice4:
                break;
        }
    }
}
