package com.example.ecoleenligne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ecoleenligne.adapters.ChaptersAdapter;
import com.example.ecoleenligne.models.UserInfo;

import java.util.ArrayList;

public class LessonMenuActivity extends AppCompatActivity {
    private TextView lessonMenuTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_menu);

        String lessonName = getIntent().getStringExtra("lessonName");
        UserInfo currentUser = getIntent().getParcelableExtra("user");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

}
