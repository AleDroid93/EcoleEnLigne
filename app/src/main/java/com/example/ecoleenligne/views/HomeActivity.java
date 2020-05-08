package com.example.ecoleenligne.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.fragments.ChatFragment;
import com.example.ecoleenligne.fragments.ClassroomFragment;
import com.example.ecoleenligne.fragments.DashboardFragment;
import com.example.ecoleenligne.fragments.MessagesFragment;
import com.example.ecoleenligne.fragments.SavedItemsFragment;
import com.example.ecoleenligne.viewmodels.NotificationViewModel;
import com.example.ecoleenligne.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private UserInfo currentUser;
    private FirebaseAuth mAuth;
    private View notificationBadge;
    private BottomNavigationView bottomNavigationView;
    private Observer<String> notificationObserver;
    private NotificationViewModel notificationViewModel;
    private NavController navController;

    private int counter;
    private String currentDestinationLabel;
    private int idLastSelected;

    public UserInfo getCurrentUser() {
        return currentUser;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("user");
        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // TODO - gestire anche i sottocasi (offline mode e tchat)
        if(currentUser.getRole().equalsIgnoreCase("student")){
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.bottom_nav_student);
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_stud_fragment);

            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

        }else{
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation);
        }

        if(!mAuth.getCurrentUser().isEmailVerified()) {
            sendEmailVerification();
        }else{
            // TODO gestire attivazione account child
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", currentUser);
        counter = 1;
        idLastSelected = -1;
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                Log.e("HomeActivity", "riselezionato. non fare nulla");
            }
        });

        notificationObserver = getNotificationObserver();
        // TODO lanciare la richiesta delle notifiche già presenti sul database
    }


    public NotificationViewModel getNotificationViewModel() {
        return notificationViewModel;
    }

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
                            Toast.makeText(HomeActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // TODO chiedere di cliccare da qualche parte per riprovare a farsi inviare email di attivazione
                            Log.e("HomeActivity2", "sendEmailVerification", task.getException());
                            Toast.makeText(HomeActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }



    private void setNotificationBadge(int pos, String num){
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(pos);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this).inflate(R.layout.badge_layout, itemView, true);
        TextView textView = itemView.findViewById(R.id.notifications_badge);
        if(textView.getVisibility() == View.GONE)
            textView.setVisibility(View.VISIBLE);
        textView.setText(num);
    }

    public Observer<String> getNotificationMessageObserver(){
        return notificationObserver;
    }

    public Observer<String> getNotificationObserver() {

        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        Observer<String> observerCreationMessage = new Observer<String>() {

                @Override
                public void onChanged(@Nullable String notificationMessage) {
                    String msg = notificationMessage;
                    if (msg.equals("success")) {
                        Log.d("HomeActivity", "notification added");
                        setNotificationBadge(2, String.valueOf(counter++));
                    } else {
                        Log.d("HomeActivity", "creationMessage: " + msg);
                    }
                }
            };

        return observerCreationMessage;
    }


    public NavController getNavController() {
        return navController;
    }

    public void setNavController(NavController navController) {
        this.navController = navController;
    }

    public Fragment getForegroundFragment(){
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_stud_fragment);
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }


    @Override
    public void onBackPressed() {
        NavBackStackEntry entry = navController.getCurrentBackStackEntry();
        if (entry == null) {
            super.onBackPressed();
            //additional code
        } else {
            navController.popBackStack();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Log.e(TAG, "back pressed! "+ navController.getCurrentDestination().getLabel().toString());
                String fragLabel = navController.getCurrentDestination().getLabel().toString();
                if(fragLabel.equalsIgnoreCase("coursefragment")){

                    CourseFragment courseFragment = (CourseFragment) getForegroundFragment();
                    courseFragment.computeReadingState();
                }
                navController.popBackStack();
                break;
            }
        }
        return true;
    }
}
