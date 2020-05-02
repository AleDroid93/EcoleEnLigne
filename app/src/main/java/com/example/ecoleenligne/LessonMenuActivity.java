package com.example.ecoleenligne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.adapters.ChaptersAdapter;
import com.example.ecoleenligne.models.UserInfo;

import java.util.ArrayList;

public class LessonMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView lessonMenuTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_menu);

        String lessonName = getIntent().getStringExtra("lessonName");
        UserInfo currentUser = getIntent().getParcelableExtra("user");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.course_card_view_item).setOnClickListener(this);
        findViewById(R.id.video_card_view_item).setOnClickListener(this);
        findViewById(R.id.resume_card_view_item).setOnClickListener(this);
        findViewById(R.id.quiz_card_view_item).setOnClickListener(this);
        findViewById(R.id.exercise_card_view_item).setOnClickListener(this);
        lessonMenuTitle = findViewById(R.id.tv_lesson_menu_title);
        lessonMenuTitle.setText(lessonName + " lesson");
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

    @Override
    public void onClick(View v) {
        String message = "";
        switch (v.getId()){
            case R.id.video_card_view_item:
                message = "Video Item clicked!";
                break;
            case R.id.course_card_view_item:
                message = "Course Item clicked!";
                break;
            case R.id.resume_card_view_item:
                message = "Resume Item clicked!";
                break;
            case R.id.quiz_card_view_item:
                message = "Quiz Item clicked!";
                break;
            case R.id.exercise_card_view_item:
                message = "Exercise Item clicked!";
                break;

        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
