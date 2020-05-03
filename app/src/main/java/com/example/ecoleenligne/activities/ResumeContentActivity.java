package com.example.ecoleenligne.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.ParagraphAdapter;
import com.example.ecoleenligne.models.Paragraph;

import java.util.ArrayList;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class ResumeContentActivity extends AppCompatActivity {
    private RecyclerView mResumesRecyclerView;
    private ParagraphAdapter mResumeAdapter;
    private TextView tvResumeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_content);
        tvResumeTitle = findViewById(R.id.tv_resume_title);

        String title = getIntent().getStringExtra("resumeTitle");
        tvResumeTitle.setText(getResources().getString(R.string.resume_title, title));
        ArrayList<Paragraph> paragraphs = getIntent().getParcelableArrayListExtra("resumes");

        mResumesRecyclerView= findViewById(R.id.resumes_recycler_view);
        mResumesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mResumesRecyclerView.setHasFixedSize(true);
        mResumesRecyclerView.setNestedScrollingEnabled(false);

        mResumeAdapter = new ParagraphAdapter(paragraphs,true, ResumeContentActivity.this);
        mResumesRecyclerView.setAdapter(mResumeAdapter);

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
