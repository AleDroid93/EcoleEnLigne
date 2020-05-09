package com.example.ecoleenligne.views;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.NotificationAdapter;
import com.example.ecoleenligne.models.Chapter;
import com.example.ecoleenligne.models.Notification;
import com.example.ecoleenligne.viewmodels.ChapterViewModel;
import com.example.ecoleenligne.viewmodels.NotificationViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {
    private static final String TAG = "NotificationsFragment";
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    ChildEventListener listener;
    private TextView notificationFragmentTitle;
    private RecyclerView mNotificationRecyclerView;
    private NotificationAdapter mNotificationAdapter;
    private LinearLayoutManager mLayoutChaptersManager;
    private NotificationViewModel notificationViewModel;
    private Observer<ArrayList<Notification>> observerNotifications;
    private String urlDb;
    private NavController navController;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeActivity parentActivity = (HomeActivity) getActivity();
        notificationFragmentTitle = view.findViewById(R.id.notification_title);
        mNotificationRecyclerView = view.findViewById(R.id.notification_recycler_view);
        String uid = parentActivity.getCurrentUser().getUid();

        mLayoutChaptersManager = new LinearLayoutManager(parentActivity);
        mNotificationRecyclerView.setLayoutManager(mLayoutChaptersManager);

        notificationViewModel = ViewModelProviders.of(parentActivity).get(NotificationViewModel.class);
        observerNotifications = parentActivity.getObserverNotificationsList();
        notificationViewModel.getMutableNotifications().observe(parentActivity, observerNotifications);


        if (notificationViewModel.getMutableNotifications().getValue() == null) {
            displayNotifications(uid);
            mNotificationAdapter = new NotificationAdapter(parentActivity, new ArrayList<>());
            mNotificationRecyclerView.setAdapter(mNotificationAdapter);
        } else if (notificationViewModel.getMutableNotifications().getValue().isEmpty()){
            displayNotifications(uid);
        }else{
            mNotificationAdapter = new NotificationAdapter(getActivity(), notificationViewModel.getMutableNotifications().getValue());
            mNotificationAdapter.notifyDataSetChanged();
            mNotificationRecyclerView.setAdapter(mNotificationAdapter);
        }

    }

    public void displayNotifications(String uid) {
        urlDb = "notifications/"+uid;
        reference = database.getReference(urlDb);

        listener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Notification notificationItem = dataSnapshot.getValue(Notification.class);
                notificationItem.setKey(dataSnapshot.getKey());
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
        reference.orderByChild("name").addChildEventListener(listener);
    }


    public void updateRecyclerView(NotificationAdapter mNotificationAdapter) {
        this.mNotificationAdapter = mNotificationAdapter;
        this.mNotificationAdapter.notifyDataSetChanged();
        this.mNotificationRecyclerView.setAdapter(this.mNotificationAdapter);
    }


}
