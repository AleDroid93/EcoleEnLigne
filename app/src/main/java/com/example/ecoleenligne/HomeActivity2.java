package com.example.ecoleenligne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ecoleenligne.fragments.ChatFragment;
import com.example.ecoleenligne.fragments.ClassroomFragment;
import com.example.ecoleenligne.fragments.DashboardFragment;
import com.example.ecoleenligne.fragments.SavedItemsFragment;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.views.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity2 extends AppCompatActivity {
    private UserInfo currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_2);
        BottomNavigationView bottomNavigationView = findViewById(R.id.dashboard_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("user");
        mAuth = FirebaseAuth.getInstance();
        if(!mAuth.getCurrentUser().isEmailVerified()) {
            sendEmailVerification();
        }else{
            // TODO gestire attivazione account child
        }
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
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", currentUser);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };


    private void sendEmailVerification() {
        // Disable button
        //findViewById(R.id.verifyEmailButton).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        //findViewById(R.id.verifyEmailButton).setEnabled(true);

                        if (task.isSuccessful()) {
                            // TODO alert dialog che chiede di cliccare sul link di verifica email
                            Toast.makeText(HomeActivity2.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // TODO chiedere di cliccare da qualche parte per riprovare a farsi inviare email di attivazione
                            Log.e("HomeActivity2", "sendEmailVerification", task.getException());
                            Toast.makeText(HomeActivity2.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }


}
