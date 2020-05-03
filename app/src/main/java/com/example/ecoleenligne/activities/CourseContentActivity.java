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
import com.example.ecoleenligne.adapters.VideosAdapter;
import com.example.ecoleenligne.models.Paragraph;
import com.example.ecoleenligne.models.Video;

import java.util.ArrayList;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class CourseContentActivity extends AppCompatActivity {
    private TextView tvIntroContent;
    private TextView tvConclusionContent;
    private RecyclerView mParagraphRecyclerView;
    private ParagraphAdapter mParagraphAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);
        tvIntroContent = findViewById(R.id.tv_intro_content);
        tvConclusionContent = findViewById(R.id.tv_conclusion_content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvIntroContent.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            tvConclusionContent.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
        String introContent = getIntent().getStringExtra("intro");
        String conclusionContent = getIntent().getStringExtra("conclusion");
        ArrayList<Paragraph> paragraphs = getIntent().getParcelableArrayListExtra("paragraphs");

        tvIntroContent.setText(introContent);
        tvConclusionContent.setText(conclusionContent);

        mParagraphRecyclerView = findViewById(R.id.paragraphs_recycler_view);
        mParagraphRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mParagraphRecyclerView.setHasFixedSize(true);
        mParagraphRecyclerView.setNestedScrollingEnabled(false);

        mParagraphAdapter = new ParagraphAdapter(paragraphs,false, CourseContentActivity.this);
        mParagraphRecyclerView.setAdapter(mParagraphAdapter);
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
