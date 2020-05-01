package com.example.ecoleenligne.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.ecoleenligne.models.UserInfo;
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
    private GridLayoutManager layoutClassroomManager;
    private UserInfo currentUser;

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
        layoutClassroomManager = new GridLayoutManager(getActivity(),2);
        classroomRecyclerView.setLayoutManager(layoutClassroomManager);
        mClassroomAdapter = new ClassroomAdapter(new ArrayList<>(), (View.OnClickListener) getActivity());
        classroomRecyclerView.setAdapter(mClassroomAdapter);
        return fragmentLayout;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String courseId = "";
        DatabaseReference reference = database.getReference("courses/"+courseId);
        HomeActivity2 parentActivity = (HomeActivity2) getActivity();
        parentActivity.getCurrentFocus();
        currentUser = getArguments().getParcelable("user");
        displayClassCourses(currentUser.getUid());
    }

    // TODO - utilizzare approccio ViewModel come per i chapters
    private void displayClassCourses(String uid){
        DatabaseReference reference = database.getReference("users/"+uid+"/uclass/courses");
        // TODO se c'è qualcosa sul db locale, prendila da li, altrimenti scaricala dal web e sincronizza
        // TODO spostare reperimento dei corsi nella HomeActivity e gestire solo la RecyclerView qui
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<HashMap<String, String>> classroom = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                ArrayList<Course> courses = new ArrayList<>();
                for(HashMap<String, String> h : classroom)
                    courses.add(new Course(h.get("id"),h.get("name"), h.get("color"), h.get("lightColor")));

                // specify an adapter (see also next example)
                mClassroomAdapter = new ClassroomAdapter(courses, (View.OnClickListener) getActivity());
                mClassroomAdapter.notifyDataSetChanged();
                classroomRecyclerView.setAdapter(mClassroomAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("StudentInfoFragment", databaseError.getMessage());
            }
        });
    }
}
