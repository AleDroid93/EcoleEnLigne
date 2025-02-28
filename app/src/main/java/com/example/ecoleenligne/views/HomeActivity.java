package com.example.ecoleenligne.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.activities.MainActivity;
import com.example.ecoleenligne.activities.RootActivity;
import com.example.ecoleenligne.activities.SearchResultsActivity;
import com.example.ecoleenligne.fragments.ClassroomFragment;
import com.example.ecoleenligne.models.Child;
import com.example.ecoleenligne.models.Classroom;
import com.example.ecoleenligne.models.Notification;
import com.example.ecoleenligne.utils.Utils;
import com.example.ecoleenligne.viewmodels.ExerciseViewModel;
import com.example.ecoleenligne.viewmodels.NotificationViewModel;
import com.example.ecoleenligne.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
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

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private UserInfo currentUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference notificationsRef;
    private View notificationBadge;
    private BottomNavigationView bottomNavigationView;

    private Observer<String> notificationObserver;
    private Observer<String> exerciseSubmissionObserver;
    private Observer<ArrayList<Notification>> notificationListObserver;

    private NotificationViewModel notificationViewModel;
    private ExerciseViewModel exerciseSubmissionViewModel;

    private NavController navController;

    private int counter;
    private String currentDestinationLabel;
    private int idLastSelected;

    public UserInfo getCurrentUser() {
        return currentUser;
    }

    public void updateToolbarTitle(String title){
        this.mToolbar.setTitle(title);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        mToolbar = findViewById(R.id.home_activity_toolbar);
        mToolbar.setTitle("EcoleEnLigne");
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.logout_item:
                        // TODO - handle logout
                        Log.e(TAG, "Logout selected");
                        menuItem.setChecked(true);
                        mAuth.signOut();
                        Intent intentLogout = new Intent(HomeActivity.this, RootActivity.class);
                        intentLogout.putExtra("logout", "logout");
                        startActivity(intentLogout);
                        finish();
                        break;

                    default:
                        Log.e(TAG, "hai cliccato "+ menuItem.getTitle().toString());
                        Bundle bundle = new Bundle();
                        bundle.putInt("studentSwap", (menuItem.getOrder()-1));
                        navController.navigate(R.id.action_nav_home_self, bundle);
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });


        setSupportActionBar(mToolbar);
        /*
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

         */
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        currentUser = intent.getParcelableExtra("user");

        if(!mAuth.getCurrentUser().isEmailVerified())
            setParentUid();

        String notificationToken = currentUser.getNotificaionToken();
        String appToken = Utils.token;


        if(!appToken.isEmpty() && (notificationToken == null || notificationToken.isEmpty())){
            pushDeviceToken();
        }
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        if(currentUser.getRole().equalsIgnoreCase("student")){
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.bottom_nav_student);
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_stud_fragment);
            navController = navHostFragment.getNavController();
            AppBarConfiguration appBarConfiguration =
                    new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_class, R.id.nav_messages)
                            .setOpenableLayout(mDrawerLayout).build();

            NavigationUI.setupWithNavController(bottomNavigationView, navController);
            NavigationUI.setupWithNavController(mToolbar, navController, appBarConfiguration);


        }else{
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation);
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_stud_fragment);
            navController = navHostFragment.getNavController();
            navController.setGraph(R.navigation.navigation_parent);
            AppBarConfiguration appBarConfiguration =
                    new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_messages)
                            .setOpenableLayout(mDrawerLayout).build();

            NavigationUI.setupWithNavController(bottomNavigationView, navController);
            NavigationUI.setupWithNavController(mToolbar, navController, appBarConfiguration);
            final Menu menu = mNavigationView.getMenu();
            for(int i = 0; i <currentUser.getChildren().size(); i++){
                menu.add(Menu.NONE, 100, (i+1), currentUser.getChildren().get(i).getName());

            }
        }

        if(!mAuth.getCurrentUser().isEmailVerified()) {
            sendEmailVerification();
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
        exerciseSubmissionObserver = getExerciseSubmissionObserver();
        exerciseSubmissionViewModel.getMutableExSubmissionMessage().observe(this, exerciseSubmissionObserver);
        if(notificationViewModel.getMutableNotifications().getValue() == null || notificationViewModel.getMutableNotifications().getValue().isEmpty())
            initNotifications(currentUser.getUid());
    }

    private void setParentUid() {

        for(Child child : currentUser.getChildren()){
            String studetUid = child.getUid();
            String urlStudentParentUid = "users/"+studetUid;
            DatabaseReference reference = database.getReference(urlStudentParentUid);
            String key = "parentUid";
            String newValue = currentUser.getUid();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/"+key, newValue);
            reference.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e(TAG, "parentUid refreshed");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "parentUid error refreshed");
                }
            });
        }
    }

    private void pushDeviceToken() {
        String urlNotificationToken = "users/"+currentUser.getUid();
        DatabaseReference reference = database.getReference(urlNotificationToken);
        String key = "notificationToken";
        String newValue = Utils.token;
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+key, newValue);
        reference.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(HomeActivity.this, "token refreshed", Toast.LENGTH_SHORT).show();
                currentUser.setNotificationToken(Utils.token);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeActivity.this, "Error on updating token", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public NotificationViewModel getNotificationViewModel() {
        return notificationViewModel;
    }

    public ExerciseViewModel getExerciseSubmissionViewModel() { return exerciseSubmissionViewModel; }

    public Observer<ArrayList<Notification>> getObserverNotificationsList(){return notificationListObserver;}

    private void sendEmailVerification() {
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
                        int count = notificationViewModel.getNotificationsToRead();
                        notificationViewModel.setNotificationsToRead(count);
                        if(currentUser.getRole().equalsIgnoreCase("student")) {
                            setNotificationBadge(2, String.valueOf(count));
                        }else{
                            setNotificationBadge(1, String.valueOf(count));
                        }
                    } else {
                        Log.d("HomeActivity", "creationMessage: " + msg);
                    }
                    navController.popBackStack();
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
                if(currentUser.getRole().equalsIgnoreCase("student")){
                    setNotificationBadge(2, String.valueOf(count));

                }else{
                    setNotificationBadge(1, String.valueOf(count));
                }
            }
        };
        return observer;
    }


    public Observer<String> getExerciseSubmissionObserver() {
        exerciseSubmissionViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        Observer<String> observerExerciseSubmission = new Observer<String>() {
            @Override
            public void onChanged(String submissionMessage) {
                String msg = submissionMessage;
                if (msg.equals("success")) {
                    Log.e(TAG, "exercise submission added");
                    Toast.makeText(HomeActivity.this, "exercise submitted", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "exercise submission Message: " + msg);
                    Toast.makeText(HomeActivity.this, "exercise submission failed. Retry", Toast.LENGTH_SHORT).show();
                }
                navController.popBackStack();
            }
        };
        return observerExerciseSubmission;
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG,"gestisco intent");
        if (intent.getParcelableExtra("updateUser") != null) {
            UserInfo updatedUser = intent.getParcelableExtra("updateUser");
            currentUser = updatedUser;
            Log.e(TAG,"intent ricevuto! Verificare aggiornamenti");
            //finish();
        }else{
            Log.e(TAG,"non è arrivato l'intent");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e(TAG, "creatione options menu");
        MenuInflater inflater = getMenuInflater();
        String fragLabel = navController.getCurrentDestination().getLabel().toString();
        inflater.inflate(R.menu.toolbar_menu, menu);

        return true;
    }




    public void changeMenu(int menuId){
        Menu menu = mToolbar.getMenu();
        mToolbar.setTitle("EcoleEnLigne");
        menu.clear();
        getMenuInflater().inflate(menuId, menu);
        if(menuId == R.menu.search_menu){
            Log.e(TAG,"carico search menu");
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", currentUser);
            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));
            searchView.setIconifiedByDefault(true);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                    intent.putExtra(SearchManager.APP_DATA, currentUser);
                    intent.putExtra(SearchManager.QUERY, s);
                    intent.setAction(Intent.ACTION_SEARCH);
                    startActivity(intent);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:

                Log.e(TAG, "back pressed! "+ navController.getCurrentDestination().getLabel().toString());
                String fragLabel = navController.getCurrentDestination().getLabel().toString();
                if(fragLabel.equalsIgnoreCase("coursefragment")){

                    CourseFragment courseFragment = (CourseFragment) getForegroundFragment();
                    courseFragment.computeReadingState();
                }else if(fragLabel.equalsIgnoreCase("quizfragment")){
                    QuizFragment quizFragment = (QuizFragment) getForegroundFragment();
                    String uid = mAuth.getCurrentUser().getUid();
                    quizFragment.sendNotification(uid);
                }else if(fragLabel.equalsIgnoreCase("exercisefragment")){
                    ExerciseFragment exerciseFragment = (ExerciseFragment) getForegroundFragment();
                    String uid = mAuth.getCurrentUser().getUid();
                    exerciseFragment.sendNotification(uid);
                }
                if(!fragLabel.equalsIgnoreCase("nav_home") &&
                        !fragLabel.equalsIgnoreCase("nav_class") &&
                        !fragLabel.equalsIgnoreCase("nav_messages") &&
                        !fragLabel.equalsIgnoreCase("nav_saved"))
                    navController.popBackStack();
                else
                    mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.info_app:
                navController.navigate(R.id.action_nav_home_to_onboardingFragment);
                Log.e(TAG, "infor pressed!");
                break;
            case R.id.search:
                break;
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
                if(currentUser.getRole().equalsIgnoreCase("student")) {
                    setNotificationBadge(2, String.valueOf(toRead));
                }else{
                    setNotificationBadge(1, String.valueOf(toRead));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeActivity.this, "Error on updating", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
