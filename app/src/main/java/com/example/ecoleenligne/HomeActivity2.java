package com.example.ecoleenligne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.ecoleenligne.fragments.ChatFragment;
import com.example.ecoleenligne.fragments.ClassroomFragment;
import com.example.ecoleenligne.fragments.DashboardFragment;
import com.example.ecoleenligne.fragments.SavedItemsFragment;
import com.example.ecoleenligne.models.UserInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity2 extends AppCompatActivity {
    private UserInfo currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_2);
        BottomNavigationView bottomNavigationView = findViewById(R.id.dashboard_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("user");
        //currentUser = getArguments().getParcelable("user");
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", currentUser);
        DashboardFragment dashboardFragment = new DashboardFragment();
        dashboardFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dashboardFragment).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()){
                // creation of the fragment
                case R.id.nav_dashboard:
                    Log.d("HomeActivity2", "onClick: dashboard pressed");
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("user", currentUser);
                    selectedFragment = new DashboardFragment();
                    selectedFragment.setArguments(bundle);
                    break;
                case R.id.nav_chat:
                    Log.d("HomeActivity2", "onClick: chat pressed");
                    selectedFragment = new ChatFragment();
                    break;
                case R.id.nav_class:
                    Log.d("HomeActivity2", "onClick: classroom pressed");
                    selectedFragment = new ClassroomFragment();
                    break;
                case R.id.nav_saved:
                    Log.d("HomeActivity2", "onClick: saved items pressed");
                    selectedFragment = new SavedItemsFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };
}
