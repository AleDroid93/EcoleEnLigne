package com.example.ecoleenligne;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ecoleenligne.models.UserInfo;

public class CourseMenu extends AppCompatActivity {
    private static final String TAG = "CourseMenu";
    private TextView courseMenuTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_menu);
        String name = getIntent().getStringExtra("course_name");
        courseMenuTitle = findViewById(R.id.tv_menu_title);
        courseMenuTitle.setText(name + " course here");
        UserInfo currentUser = getIntent().getParcelableExtra("user");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
