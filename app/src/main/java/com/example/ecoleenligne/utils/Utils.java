package com.example.ecoleenligne.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoleenligne.adapters.ClassroomAdapter;
import com.example.ecoleenligne.models.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static List<Course> DataCache = new ArrayList<>();

    public static void show(Context c, String message){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }

    public static void search(final AppCompatActivity a, DatabaseReference db,
                              //final ProgressBar pb,
                              ClassroomAdapter adapter) {

        //Utils.showProgressBar(pb);

        Query firebaseSearchQuery = db.child("courses").orderByChild("name");

        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                //DataCache.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //Now get Scientist Objects and populate our arraylist.
                        Course course = ds.getValue(Course.class);
                        if (course != null) {
                            course.setId(ds.getKey());
                        }
                        DataCache.add(course);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.show(a,"No item found");
                }
                //Utils.hideProgressBar(pb);

            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                Log.d("FIREBASE CRUD", databaseError.getMessage());
                //Utils.hideProgressBar(pb);
                Utils.show(a,databaseError.getMessage());
            }
        });
    }
}