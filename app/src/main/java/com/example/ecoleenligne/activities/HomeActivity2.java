package com.example.ecoleenligne.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.data.NetworkMessage;
import com.example.ecoleenligne.fragments.ChatFragment;
import com.example.ecoleenligne.fragments.ClassroomFragment;
import com.example.ecoleenligne.fragments.DashboardFragment;
import com.example.ecoleenligne.fragments.MessagesFragment;
import com.example.ecoleenligne.fragments.SavedItemsFragment;
import com.example.ecoleenligne.fragments.StudentInfoFragment;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.viewmodels.NotificationViewModel;
import com.example.ecoleenligne.viewmodels.UserInfoViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity2 extends AppCompatActivity implements View.OnClickListener {
    private UserInfo currentUser;
    private FirebaseAuth mAuth;
    private View notificationBadge;
    private BottomNavigationView bottomNavigationView;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_2);
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("user");
        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.dashboard_menu);
        // TODO - gestire anche i sottocasi (offline mode e tchat)
        if(currentUser.getRole().equalsIgnoreCase("student")){
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.bottom_nav_student);
        }else{
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation);
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        if(!mAuth.getCurrentUser().isEmailVerified()) {
            sendEmailVerification();
        }else{
            // TODO gestire attivazione account child
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", currentUser);
        Fragment fragment = new DashboardFragment();
        fragment.setArguments(bundle);
        int index = getFragmentManager().getBackStackEntryCount() - 1;
        if(index > 0){
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
            String tag = backEntry.getName();
            fragment = getSupportFragmentManager().findFragmentByTag(tag);
        }
        Log.d("HomeActivity2","index "+index);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        setNotificationBadge(2, "1");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", currentUser);
            switch (menuItem.getItemId()){
                // creation of the fragment
                case R.id.nav_dashboard:
                    Log.d("HomeActivity2", "onClick: dashboard pressed");

                    selectedFragment = new DashboardFragment();
                    selectedFragment.setArguments(bundle);
                    break;
                case R.id.nav_home:
                    Log.d("HomeActivity2", "onClick: home pressed");
                    // TODO replace DasbhboardFragment with HomeStudentFragment
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
                    selectedFragment.setArguments(bundle);
                    break;
                case R.id.nav_messages:
                    Log.d("HomeActivity", "onClick: messages pressed");
                    selectedFragment = new MessagesFragment();
                    //selectedFragment.setArguments(bundle);
                case R.id.nav_saved:
                    Log.d("HomeActivity2", "onClick: saved items pressed");
                    selectedFragment = new SavedItemsFragment();
                    break;
            }

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


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.chapter_card_view:
                Log.d("HomeActivity2", "course clicked!");
                Intent intent = new Intent(this, CourseMenu.class);

                intent.putExtra("course_name", ((TextView)v.findViewById(R.id.tv_course_name)).getText());
                intent.putExtra("user", currentUser);
                currentUser.getUclass().getId();

                startActivity(intent);
                break;
        }
    }

    private void setNotificationBadge(int pos, String num){
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(pos);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        View badge = LayoutInflater.from(this).inflate(R.layout.badge_layout, itemView, true);
        ((TextView) badge).setText(num);
    }


}
