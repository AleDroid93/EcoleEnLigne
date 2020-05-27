package com.example.ecoleenligne.views;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.ChaptersAdapter;
import com.example.ecoleenligne.models.Chapter;
import com.example.ecoleenligne.models.Course;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.viewmodels.ChapterViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseDetailFragment extends Fragment {
    private static final String TAG = "CourseDetailFragment";
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    ChildEventListener listener;
    private TextView courseMenuTitle;
    private RecyclerView mChaptersRecyclerView;
    private ChaptersAdapter mChaptersAdapter;
    private RecyclerView.Adapter mLessonsAdapter;
    private LinearLayoutManager mLayoutChaptersManager;
    private ChapterViewModel chaptersViewModel;
    private Observer<ArrayList<Chapter>> observerChapter;
    UserInfo currentUser;
    private int courseColor;
    private int lightColor;
    private String urlDb;
    private Course currentCourse;
    private NavController navController;


    public CourseDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getActivity().onBackPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        if(getArguments().getParcelable("course") != null)
            currentCourse = getArguments().getParcelable("course");
        String courseName = getArguments().getString("course_name");
        UserInfo currentUser = getArguments().getParcelable("user");
        HomeActivity parentActivity = (HomeActivity) getActivity();
        parentActivity.changeMenu(R.menu.toolbar_menu);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseMenuTitle = view.findViewById(R.id.tv_menu_title);
        courseMenuTitle.setText(courseName + ":");
        courseColor = currentUser.findCourseColorByName(courseName);
        lightColor = currentUser.findCourseLightColorByName(courseName);

        String clId = currentUser.getUclass().getId();
        String csId = currentUser.findCourseIdByName(courseName);

        mChaptersRecyclerView = view.findViewById(R.id.chapters_recycler_view);
        mLayoutChaptersManager = new LinearLayoutManager(parentActivity);
        mChaptersRecyclerView.setLayoutManager(mLayoutChaptersManager);

        chaptersViewModel = ViewModelProviders.of(parentActivity).get(ChapterViewModel.class);
        observerChapter = getObserverChapter();
        chaptersViewModel.getChaptersLiveData().observe(parentActivity, observerChapter);

        if (chaptersViewModel.getChaptersLiveData().getValue() == null) {
            displayChapters(clId, csId);
            mChaptersAdapter = new ChaptersAdapter(parentActivity, courseColor, lightColor, currentCourse, new ArrayList<>());
            mChaptersRecyclerView.setAdapter(mChaptersAdapter);
        } else if (chaptersViewModel.getChaptersLiveData().getValue().isEmpty()){
            displayChapters(clId, csId);
        }else{
            mChaptersAdapter = new ChaptersAdapter(getActivity(), courseColor, lightColor, currentCourse, chaptersViewModel.getChaptersLiveData().getValue());
            mChaptersAdapter.notifyDataSetChanged();
            mChaptersRecyclerView.setAdapter(mChaptersAdapter);
        }
    }



    public Observer<ArrayList<Chapter>> getObserverChapter(){
        return new Observer<ArrayList<Chapter>>() {
            @Override
            public void onChanged(ArrayList<Chapter> chapters) {
                mChaptersAdapter = new ChaptersAdapter(getActivity(), courseColor, lightColor, currentCourse, chapters);
                mChaptersAdapter.notifyDataSetChanged();
                mChaptersRecyclerView.setAdapter(mChaptersAdapter);
            }
        };
    }



    private void displayChapters(String clId, String csId) {
        urlDb = "courses/"+clId+"/"+csId+"/chapters";
        reference = database.getReference(urlDb);

        listener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chapter chapterItem = dataSnapshot.getValue(Chapter.class);
                chaptersViewModel.addChapter(chapterItem);
                //lessonsViewModel.addAll(chapterItem.getLessons());
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

    @Override
    public void onStop() {
        Log.e(TAG, "onStop");
        mChaptersAdapter.clear();
        mChaptersRecyclerView.setAdapter(mChaptersAdapter);
        super.onStop();
    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mChaptersAdapter = new ChaptersAdapter(getActivity(), getResources().getColor(R.color.white), lightColor, currentCourse,new ArrayList<>());
                mChaptersAdapter.notifyDataSetChanged();
                mChaptersRecyclerView.setAdapter(mChaptersAdapter);
                reference.removeEventListener(listener);
                navController.navigate(R.id.action_courseDetailFragment_to_classroomFragment);
                break;
            }
        }
        return true;
    }

    public UserInfo getCurrentUser(){
        return this.currentUser;
    }


}
