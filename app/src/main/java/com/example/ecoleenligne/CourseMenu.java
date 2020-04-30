package com.example.ecoleenligne;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ecoleenligne.adapters.ChaptersAdapter;
import com.example.ecoleenligne.adapters.ClassroomAdapter;
import com.example.ecoleenligne.models.Chapter;
import com.example.ecoleenligne.models.Course;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.viewmodels.ChapterViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseMenu extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CourseMenu";
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextView courseMenuTitle;
    private RecyclerView mChaptersRecyclerView;
    private RecyclerView.Adapter mChaptersAdapter;
    private LinearLayoutManager mLayoutChaptersManager;
    private ChapterViewModel chaptersViewModel;
    private Observer<ArrayList<Chapter>> observerChapter;
    private UserInfo currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_menu);
        chaptersViewModel = ViewModelProviders.of(CourseMenu.this).get(ChapterViewModel.class);
        observerChapter = getObserverChapter();
        chaptersViewModel.getChaptersLiveData().observe(CourseMenu.this, observerChapter);
        String courseName = getIntent().getStringExtra("course_name");
        courseMenuTitle = findViewById(R.id.tv_menu_title);
        courseMenuTitle.setText(courseName + " course here");
        mChaptersRecyclerView = findViewById(R.id.chapters_recycler_view);
        mLayoutChaptersManager = new LinearLayoutManager(CourseMenu.this);
        mChaptersRecyclerView.setLayoutManager(mLayoutChaptersManager);
        mChaptersAdapter = new ChaptersAdapter(new ArrayList<>());
        mChaptersRecyclerView.setAdapter(mChaptersAdapter);
        UserInfo currentUser = getIntent().getParcelableExtra("user");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String clId = currentUser.getUclass().getId();
        String csId = currentUser.findCourseIdByName(courseName);
        displayChapters(clId, csId);
    }

    public Observer<ArrayList<Chapter>> getObserverChapter(){
        return new Observer<ArrayList<Chapter>>() {
            @Override
            public void onChanged(ArrayList<Chapter> chapters) {
                mChaptersAdapter = new ChaptersAdapter(chapters);
                mChaptersRecyclerView.setAdapter(mChaptersAdapter);
            }
        };
    }

    // TODO gestire la nested RecyclerView per le lezioni
    private void displayChapters(String clId, String csId) {
        DatabaseReference reference = database.getReference("courses/"+clId+"/"+csId+"/chapters");


        reference.orderByChild("name").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chapter chapterItem = dataSnapshot.getValue(Chapter.class);
                chaptersViewModel.addChapter(chapterItem);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i(TAG, "popping backstack");
            fm.popBackStack();
        } else {
            Log.i(TAG, "nothing on backstack, calling super");
            super.onBackPressed();
        }
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

    }
}
