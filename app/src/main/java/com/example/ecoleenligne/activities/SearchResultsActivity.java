package com.example.ecoleenligne.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.SearchResultsAdapter;
import com.example.ecoleenligne.models.Classroom;
import com.example.ecoleenligne.models.Course;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.repositories.UserInfoRepository;
import com.example.ecoleenligne.viewmodels.CourseViewModel;
import com.example.ecoleenligne.viewmodels.UserInfoViewModel;
import com.example.ecoleenligne.views.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private UserInfoViewModel userInfoViewModel;
    private UserInfoRepository userInfoRepository;
    private Observer<String> updateObserver;


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
                TextView tv = v.findViewById(R.id.tv_search_result_course_item);
                String courseName = tv.getText().toString();
                Course courseClicked = mSearchResultsAdapter.getCourseByName(courseName);
                if(courseClicked != null){
                    Classroom userClassroom = currentUser.getUserClass();
                    userClassroom.addCourse(courseClicked);
                    updateUser();
                    //finish();
                }else{
                    Toast.makeText(SearchResultsActivity.this, "You already follow this course", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mSearchResultRecyclerView.setAdapter(mSearchResultsAdapter);
        /*
        userInfoViewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
        updateObserver = getCourseUpdateObserver();
        */
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseObserver = getObserverCourses();
        courseViewModel.getCoursesLiveData().observe(this, courseObserver);
        handleIntent(getIntent());
    }

    private Observer<String> getCourseUpdateObserver() {
        Observer<String> observerCourseUpdate = new Observer<String>() {
            @Override
            public void onChanged(String updateMessage) {
                String msg = updateMessage;
                if (msg.equals("success")) {
                    Log.e(TAG, "courses updated");
                    Toast.makeText(SearchResultsActivity.this, "follow added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SearchResultsActivity.this, HomeActivity.class);
                    intent.putExtra("updateUser", currentUser);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "update Message: " + msg);
                    Toast.makeText(SearchResultsActivity.this, "update courses failed. Retry", Toast.LENGTH_SHORT).show();
                }
            }
        };
        return observerCourseUpdate;
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

    private void updateUser() {
        String urlExercises = "users/"+currentUser.getUid()+"/uclass/courses";
        DatabaseReference reference = database.getReference(urlExercises);
        String key = "courses";
        Map<String, Object> newValues = currentUser.getUclass().toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        reference.setValue(currentUser.getUclass().getCourses()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SearchResultsActivity.this, "follow success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SearchResultsActivity.this, "Error on updating", Toast.LENGTH_SHORT).show();
            }
        });

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
                        TextView tv = v.findViewById(R.id.tv_search_result_course_item);
                        String courseName = tv.getText().toString();
                        Course courseClicked = mSearchResultsAdapter.getCourseByName(courseName);
                        if(courseClicked != null){
                            Classroom userClassroom = currentUser.getUserClass();
                            userClassroom.addCourse(courseClicked);
                            updateUser();
                            //finish();
                        }else{
                            Toast.makeText(SearchResultsActivity.this, "You already follow this course", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                mSearchResultsAdapter.notifyDataSetChanged();
                mSearchResultRecyclerView.setAdapter(mSearchResultsAdapter);
            }
        };
    }

}
