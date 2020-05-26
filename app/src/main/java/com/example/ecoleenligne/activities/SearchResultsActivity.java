package com.example.ecoleenligne.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.SearchResultsAdapter;
import com.example.ecoleenligne.models.Classroom;
import com.example.ecoleenligne.models.Course;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.viewmodels.CourseViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {
    public static final String TAG = "SearchResultsActivity";
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private RecyclerView mSearchResultRecyclerView;
    private SearchResultsAdapter mSearchResultsAdapter;
    private LinearLayoutManager layoutManager;
    private UserInfo currentUser;
    private CourseViewModel courseViewModel;
    private Observer<ArrayList<Course>> courseObserver;
    private ImageView imgClose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"Sono qui");
        setContentView(R.layout.activity_search_results);
        imgClose = findViewById(R.id.img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Ehi", "Close clicked!");
                finish();
            }
        });
        mSearchResultRecyclerView = (RecyclerView) findViewById(R.id.search_results_recycler_view);
        mSearchResultRecyclerView.setHasFixedSize(true);
        mSearchResultRecyclerView.setNestedScrollingEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        mSearchResultRecyclerView.setLayoutManager(layoutManager);
        mSearchResultsAdapter = new SearchResultsAdapter(new ArrayList<>(), this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "course clicked");
            }
        });
        mSearchResultRecyclerView.setAdapter(mSearchResultsAdapter);

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseObserver = getObserverCourses();
        courseViewModel.getCoursesLiveData().observe(this, courseObserver);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.e(TAG,"gestisco intent");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            currentUser = intent.getParcelableExtra(SearchManager.APP_DATA);
            Log.e(TAG,"intent ricevuto! Far partre la ricerca dei corsi");
            displaySearchResults();
            //finish();
        }else{
            Log.e(TAG,"non è arrivato l'intent");
        }
    }


    private void displaySearchResults(){
        DatabaseReference reference = database.getReference("courses/"+currentUser.getUclass().getId());
        ChildEventListener listener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Course courseItem = dataSnapshot.getValue(Course.class);
                Classroom userClassroom = currentUser.getUclass();
                if(!userClassroom.isPresent(courseItem))
                    courseViewModel.addCourse(courseItem);
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
        };
        reference.orderByChild("name").addChildEventListener(listener);
    }

    public Observer<ArrayList<Course>> getObserverCourses(){
        return new Observer<ArrayList<Course>>() {
            @Override
            public void onChanged(ArrayList<Course> courses) {

                mSearchResultsAdapter = new SearchResultsAdapter(courses, SearchResultsActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "course clicked");
                    }
                });
                mSearchResultsAdapter.notifyDataSetChanged();
                mSearchResultRecyclerView.setAdapter(mSearchResultsAdapter);
            }
        };
    }

}
