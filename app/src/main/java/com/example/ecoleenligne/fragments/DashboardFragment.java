package com.example.ecoleenligne.fragments;


import android.os.Bundle;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.DashboardAdapter;
import com.example.ecoleenligne.models.Child;
import com.example.ecoleenligne.models.Statistics;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.viewmodels.StatsViewModel;
import com.example.ecoleenligne.views.HomeActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    ChildEventListener listener;

    private UserInfo currentUser;
    private Child currentChild;
    private RecyclerView mDashboardRecyclerView;
    private LinearLayoutManager layoutManager;
    private DashboardAdapter mDashboardAdapter;
    private StatsViewModel mViewModel;
    private Observer<ArrayList<Statistics>> observerStats;
    private NavController navController;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (currentUser == null) {
            currentUser = ((HomeActivity) getActivity()).getCurrentUser();
        }
        TextView tv = view.findViewById(R.id.tv_dashboard_title);
        if (currentUser != null) {

            ArrayList<Child> children = currentUser.getChildren();
            if (!children.isEmpty()) {
                currentChild = children.get(0);
                tv.setText(children.get(0).getName() + " dashboard:");
                getStudentCourses(currentChild.getUid(), currentChild.getName());
            } else {
                tv.setText("Your Dashboard:");
                getStudentCourses(currentUser.getUid(), currentUser.getName());
            }
        }
        HomeActivity parentActivity = (HomeActivity) getActivity();
        parentActivity.changeMenu(R.menu.toolbar_menu);

        navController = Navigation.findNavController(view);

        mDashboardRecyclerView = view.findViewById(R.id.dashboard_recycler_view);
        layoutManager = new LinearLayoutManager(parentActivity);
        mDashboardRecyclerView.setLayoutManager(layoutManager);

        mViewModel = ViewModelProviders.of(parentActivity).get(StatsViewModel.class);
        observerStats = getObserverStats();
        mViewModel.getStatsLiveData().observe(parentActivity, observerStats);

        /*
        courseColor = currentUser.findCourseColorByName(courseName);
        lightColor = currentUser.findCourseLightColorByName(courseName);

        String clId = currentUser.getUclass().getId();
        String csId = currentUser.findCourseIdByName(courseName);



        chaptersViewModel = ViewModelProviders.of(parentActivity).get(ChapterViewModel.class);
        observerChapter = getObserverChapter();
        chaptersViewModel.getChaptersLiveData().observe(parentActivity, observerChapter);

        if (chaptersViewModel.getChaptersLiveData().getValue() == null) {
            mChaptersAdapter = new ChaptersAdapter(parentActivity, courseColor, lightColor, currentCourse, new ArrayList<>());
            mChaptersRecyclerView.setAdapter(mChaptersAdapter);
            displayChapters(clId, csId);
        } else if (chaptersViewModel.getChaptersLiveData().getValue().isEmpty()){
            displayChapters(clId, csId);
        }else{
            mChaptersAdapter = new ChaptersAdapter(getActivity(), courseColor, lightColor, currentCourse, chaptersViewModel.getChaptersLiveData().getValue());
            mChaptersAdapter.notifyDataSetChanged();
            mChaptersRecyclerView.setAdapter(mChaptersAdapter);
        }

         */
    }

    private void getStudentCourses(String uid, String studName) {
        String url = "users/" + uid + "/uclass/courses";
        DatabaseReference reference = database.getReference(url);

        listener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, String> courseItem = (Map<String, String>) dataSnapshot.getValue();
                findStats(uid, courseItem.get("id"), courseItem.get("name"), courseItem.get("color"), studName);
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

    private void findStats(String uid, String courseId, String courseName, String courseColor, String studName) {
        String url = "statistics/"+ uid;
        DatabaseReference reference = database.getReference(url);
        reference.child(courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.getChildren()!=null &&
                        dataSnapshot.getChildren().iterator().hasNext()){
                    Log.e(TAG,"Stats trovate!");
                    Statistics stat = dataSnapshot.getValue(Statistics.class);
                    stat.setCourseName(courseName);
                    stat.setStudentName(studName);
                    stat.setColor(courseColor);
                    mViewModel.addStats(stat);
                    Log.e(TAG, stat.toString());
                }else {
                    Log.e(TAG, "Nessuna stats per questo corso");
                    Statistics stat = new Statistics();
                    stat.setCourseName(courseName);
                    stat.setStudentName(studName);
                    stat.setColor(courseColor);
                    mViewModel.addStats(stat);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "There was an error with the database");
            }
        });
    }

    public Observer<ArrayList<Statistics>> getObserverStats(){
        return new Observer<ArrayList<Statistics>>() {
            @Override
            public void onChanged(ArrayList<Statistics> stats) {
                for(Statistics s : stats)
                    Log.e(TAG, s.toString());
                mDashboardAdapter = new DashboardAdapter(getActivity(), stats);
                //mChaptersAdapter.notifyDataSetChanged();
                mDashboardRecyclerView.setAdapter(mDashboardAdapter);
            }
        };
    }


}
