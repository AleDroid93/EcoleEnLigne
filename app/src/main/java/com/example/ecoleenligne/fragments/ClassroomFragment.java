package com.example.ecoleenligne.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.ClassroomAdapter;
import com.example.ecoleenligne.models.Course;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.viewmodels.CourseViewModel;
import com.example.ecoleenligne.views.HomeActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClassroomFragment extends Fragment {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private RecyclerView classroomRecyclerView;
    private ClassroomAdapter mClassroomAdapter;
    private GridLayoutManager layoutClassroomManager;
    private UserInfo currentUser;
    private NavController navController;
    private CourseViewModel courseViewModel;
    private Observer<ArrayList<Course>> courseObserver;


    public ClassroomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.fragment_class, container, false);
        classroomRecyclerView = (RecyclerView) fragmentLayout.findViewById(R.id.classroom_recycler_view);
        layoutClassroomManager = new GridLayoutManager(getActivity(),2);
        classroomRecyclerView.setLayoutManager(layoutClassroomManager);
        return fragmentLayout;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        String courseId = "";
        DatabaseReference reference = database.getReference("courses/" + courseId);
        HomeActivity parentActivity = (HomeActivity) getActivity();
        parentActivity.changeMenu(R.menu.search_menu);
        parentActivity.getCurrentFocus();
        currentUser = parentActivity.getCurrentUser();
        courseViewModel = ViewModelProviders.of(parentActivity).get(CourseViewModel.class);
        courseObserver = getObserverCourses();
        courseViewModel.getCoursesLiveData().observe(parentActivity, courseObserver);


        View.OnClickListener listener = getOnClickListener();
        /*
        mClassroomAdapter = new ClassroomAdapter(new ArrayList<>(), listener);
        classroomRecyclerView.setAdapter(mClassroomAdapter);
        */
        if (courseViewModel.getCoursesLiveData().getValue() == null){
            displayClassCourses(currentUser.getUid());
        }else if (courseViewModel.isEmpty()) {
            displayClassCourses(currentUser.getUid());
        }else{
            mClassroomAdapter = new ClassroomAdapter(courseViewModel.getCoursesLiveData().getValue(), getOnClickListener());
            mClassroomAdapter.notifyDataSetChanged();
            classroomRecyclerView.setAdapter(mClassroomAdapter);
        }

    }

    // TODO - utilizzare approccio ViewModel come per i chapters
    private void displayClassCourses(String uid){
        DatabaseReference reference = database.getReference("users/"+uid+"/uclass/courses");
        // TODO se c'è qualcosa sul db locale, prendila da li, altrimenti scaricala dal web e sincronizza
        // TODO spostare reperimento dei corsi nella HomeActivity e gestire solo la RecyclerView qui
        ChildEventListener listener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Course courseItem = new Course();
                try {
                    courseItem = dataSnapshot.getValue(Course.class);
                }catch (DatabaseException dbex){
                    ArrayList<Course> courses = dataSnapshot.getValue(ArrayList.class);
                    courseItem = courses.get(courses.size()-1);
                }
                courseViewModel.addCourse(courseItem);
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


    private View.OnClickListener getOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.course_card_view:
                        Log.d("ClassroomFragment", "course clicked!");
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("user", currentUser);
                        String courseName = ((TextView)v.findViewById(R.id.tv_course_name)).getText().toString();
                        Course courseClicked = courseViewModel.getCourse(courseName);
                        bundle.putParcelable("course", courseClicked);
                        bundle.putString("course_name",  courseName);
                        courseViewModel.clear();
                        mClassroomAdapter = new ClassroomAdapter(new ArrayList<>(), null);
                        mClassroomAdapter.notifyDataSetChanged();
                        navController.navigate(R.id.action_classroomFragment_to_courseDetailFragment, bundle);
                        break;
                }
            }
        };
    }


    public Observer<ArrayList<Course>> getObserverCourses(){
        return new Observer<ArrayList<Course>>() {
            @Override
            public void onChanged(ArrayList<Course> courses) {
                if(mClassroomAdapter == null) {
                    mClassroomAdapter = new ClassroomAdapter(courses, getOnClickListener());
                    mClassroomAdapter.notifyDataSetChanged();
                    classroomRecyclerView.setAdapter(mClassroomAdapter);
                }else if(mClassroomAdapter.getItemCount() <= courses.size()){
                    mClassroomAdapter = new ClassroomAdapter(courses, getOnClickListener());
                    mClassroomAdapter.notifyDataSetChanged();
                    classroomRecyclerView.setAdapter(mClassroomAdapter);
                }
            }
        };
    }

    public void refreshClassroom(ArrayList<Course> courses){
        mClassroomAdapter.clear();
        mClassroomAdapter = new ClassroomAdapter(courses, getOnClickListener());
        mClassroomAdapter.notifyDataSetChanged();
        classroomRecyclerView.setAdapter(mClassroomAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();
        //mClassroomAdapter.clear();
    }
}
