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
import com.example.ecoleenligne.adapters.NotificationAdapter;
import com.example.ecoleenligne.fragments.ChatFragment;
import com.example.ecoleenligne.fragments.ClassroomFragment;
import com.example.ecoleenligne.fragments.DashboardFragment;
import com.example.ecoleenligne.fragments.MessagesFragment;
import com.example.ecoleenligne.fragments.SavedItemsFragment;
import com.example.ecoleenligne.models.Notification;
import com.example.ecoleenligne.viewmodels.NotificationViewModel;
import com.example.ecoleenligne.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private UserInfo currentUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference notificationsRef;
    private View notificationBadge;
    private BottomNavigationView bottomNavigationView;
    private Observer<String> notificationObserver;
    private Observer<ArrayList<Notification>> notificationListObserver;
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
        database = FirebaseDatabase.getInstance();

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
        notificationListObserver = getNotificationListObserver();
        notificationViewModel.getMutableNotifications().observe(this, notificationListObserver);
        if(notificationViewModel.getMutableNotifications().getValue() == null || notificationViewModel.getMutableNotifications().getValue().isEmpty())
            initNotifications(currentUser.getUid());
    }


    public NotificationViewModel getNotificationViewModel() {
        return notificationViewModel;
    }

    public Observer<ArrayList<Notification>> getObserverNotificationsList(){return notificationListObserver;}

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


    private void initNotifications(String uid){
        String urlDb = "notifications/"+uid;
        notificationsRef = database.getReference(urlDb);
        ChildEventListener listener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Notification notificationItem = dataSnapshot.getValue(Notification.class);
                String key = dataSnapshot.getKey();
                notificationItem.setKey(key);
                notificationViewModel.updateNotifications(notificationItem);
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

        notificationsRef.orderByChild("datetime").addChildEventListener(listener);
    }

    private void setNotificationBadge(int pos, String num){
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(pos);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this).inflate(R.layout.badge_layout, itemView, true);
        TextView textView = itemView.findViewById(R.id.notifications_badge);
        if(textView.getVisibility() == View.GONE && Integer.parseInt(num) >0)
            textView.setVisibility(View.VISIBLE);
        else if(textView.getVisibility() == View.VISIBLE && Integer.parseInt(num) <=0)
            textView.setVisibility(View.GONE);
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
                        int count = notificationViewModel.getNotificationsToRead() +1;
                        notificationViewModel.setNotificationsToRead(count);
                        setNotificationBadge(2, String.valueOf(count));
                    } else {
                        Log.d("HomeActivity", "creationMessage: " + msg);
                    }
                }
            };

        return observerCreationMessage;
    }




    public Observer<ArrayList<Notification>> getNotificationListObserver() {
        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        Observer<ArrayList<Notification>> observer = new Observer<ArrayList<Notification>>() {
            @Override
            public void onChanged(ArrayList<Notification> notifications) {
                int count = notificationViewModel.getNotificationsToRead();
                setNotificationBadge(2, String.valueOf(count));

            }
        };
        return observer;
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


    public void markNotificationAsRead(Notification notification){
        notification.setRead(false);
        String key = notification.getKey();
        Map<String, Object> newValues = notification.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+key, newValues);
        notificationsRef.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(HomeActivity.this, "Marked as read", Toast.LENGTH_SHORT).show();
                int toRead = notificationViewModel.getNotificationsToRead()-1;
                notificationViewModel.setNotificationsToRead(toRead);
                setNotificationBadge(2, String.valueOf(toRead));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeActivity.this, "Error on updating", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
