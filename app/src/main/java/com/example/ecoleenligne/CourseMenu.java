package com.example.ecoleenligne;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecoleenligne.adapters.ChaptersAdapter;
import com.example.ecoleenligne.adapters.ClassroomAdapter;
import com.example.ecoleenligne.listener.CardViewListener;
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
    private LinearLayout expandableView;
    private RecyclerView mChaptersRecyclerView;
    private RecyclerView.Adapter mChaptersAdapter;
    private LinearLayoutManager mLayoutChaptersManager;
    private ChapterViewModel chaptersViewModel;
    private Observer<ArrayList<Chapter>> observerChapter;
    private UserInfo currentUser;
    private int courseColor;

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
        mChaptersRecyclerView.addOnItemTouchListener(new CardViewListener(CourseMenu.this, mChaptersRecyclerView, new CardViewListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "arrow clicked");
                LinearLayout headerLayout = view.findViewById(R.id.header_layout);
                TextView chTitle = headerLayout.findViewById(R.id.tv_chapter_title);
                TextView chNumber = headerLayout.findViewById(R.id.tv_chapter_number);
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(CourseMenu.this, R.drawable.chapter_item_box);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                ImageView imView = (ImageView) headerLayout.findViewById(R.id.arrowBtn);
                expandableView = view.findViewById(R.id.expandableView);
                if(expandableView.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(view.findViewById(R.id.course_card_view), new AutoTransition());
                    expandableView.setVisibility(View.VISIBLE);
                    imView.setImageResource(R.drawable.ic_up);
                    DrawableCompat.setTint(wrappedDrawable, courseColor);
                    chTitle.setTextColor(getResources().getColor(R.color.white));
                    chNumber.setTextColor(getResources().getColor(R.color.white));
                }else {
                    TransitionManager.beginDelayedTransition(view.findViewById(R.id.course_card_view), new AutoTransition());
                    expandableView.setVisibility(View.GONE);
                    imView.setImageResource(R.drawable.ic_down);
                    DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.white));
                    chTitle.setTextColor(getResources().getColor(R.color.text_color));
                    chNumber.setTextColor(getResources().getColor(R.color.text_color));
                }
                headerLayout.setBackground(wrappedDrawable);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        mLayoutChaptersManager = new LinearLayoutManager(CourseMenu.this);
        mChaptersRecyclerView.setLayoutManager(mLayoutChaptersManager);
        mChaptersAdapter = new ChaptersAdapter(CourseMenu.this, courseColor,CourseMenu.this, new ArrayList<>());
        mChaptersRecyclerView.setAdapter(mChaptersAdapter);
        UserInfo currentUser = getIntent().getParcelableExtra("user");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        courseColor = currentUser.findCourseColorByName(courseName);
        String clId = currentUser.getUclass().getId();
        String csId = currentUser.findCourseIdByName(courseName);

        displayChapters(clId, csId);
    }

    public Observer<ArrayList<Chapter>> getObserverChapter(){
        return new Observer<ArrayList<Chapter>>() {
            @Override
            public void onChanged(ArrayList<Chapter> chapters) {
                mChaptersAdapter = new ChaptersAdapter(CourseMenu.this, courseColor,CourseMenu.this, chapters);
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
        switch(v.getId()){
            case R.id.arrowBtn:

                break;
        }
    }
}
