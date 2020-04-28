package com.example.ecoleenligne.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ecoleenligne.HomeActivity2;
import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.ClassroomAdapter;
import com.example.ecoleenligne.models.Course;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassroomFragment extends Fragment {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private RecyclerView classroomRecyclerView;
    private RecyclerView.Adapter mClassroomAdapter;
    private RecyclerView.LayoutManager layoutClassroomManager;

    public ClassroomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.fragment_class, container, false);
        classroomRecyclerView = (RecyclerView) fragmentLayout.findViewById(R.id.classroom_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //classroomRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutClassroomManager = new LinearLayoutManager(getContext());
        classroomRecyclerView.setLayoutManager(layoutClassroomManager);


        return fragmentLayout;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String courseId = "";
        DatabaseReference reference = database.getReference("courses/"+courseId);
        HomeActivity2 parentActivity = (HomeActivity2) getActivity();
        parentActivity.getCurrentFocus();
        displayClassCourses("");



    }


    private void displayClassCourses(String uclass){
        // TODO get classesIds from the database
        HashMap<String,String> classesIds = new HashMap<>();
        classesIds.put("first", "cl0");
        classesIds.put("second","cl1");
        classesIds.put("third","cl2");
        String courseId = classesIds.get(uclass.toLowerCase());
        DatabaseReference reference = database.getReference("courses/"+courseId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Course> classroom = (HashMap<String, Course>) dataSnapshot.getValue();
                ArrayList<Course> courses = (ArrayList<Course>) classroom.values();
                // specify an adapter (see also next example)
                // TODO (3) spostare in onCreate
                // TODO (1) create a MaterialCardView for each course
                // TODO (2) display cards in a RecyclerView
                mClassroomAdapter = new ClassroomAdapter(courses);
                classroomRecyclerView.setAdapter(mClassroomAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("StudentInfoFragment", databaseError.getMessage());
            }
        });
    }
}
