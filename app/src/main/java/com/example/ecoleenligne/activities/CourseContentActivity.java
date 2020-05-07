package com.example.ecoleenligne.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.ParagraphAdapter;
import com.example.ecoleenligne.adapters.VideosAdapter;
import com.example.ecoleenligne.data.NetworkMessage;
import com.example.ecoleenligne.fragments.StudentInfoFragment;
import com.example.ecoleenligne.models.Chapter;
import com.example.ecoleenligne.models.Child;
import com.example.ecoleenligne.models.Lesson;
import com.example.ecoleenligne.models.Notification;
import com.example.ecoleenligne.models.Paragraph;
import com.example.ecoleenligne.models.Video;
import com.example.ecoleenligne.repositories.NotificationRepository;
import com.example.ecoleenligne.viewmodels.NotificationViewModel;
import com.facebook.internal.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class CourseContentActivity extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    private TextView tvIntroContent;
    private TextView tvConclusionContent;
    private RecyclerView mParagraphRecyclerView;
    private ParagraphAdapter mParagraphAdapter;
    private NotificationRepository notificationRepository;
    private NotificationViewModel notificationViewModel;
    private Observer<String> observerNotification;
    private FirebaseAuth mAuth;

    private Lesson currentLesson;
    private NestedScrollView mScrollView;
    private double percScrolled;
    private Integer timeReadingEstimation;
    private long currentTimeRead;
    private long startTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);
        startTime = System.currentTimeMillis();
        percScrolled = 0.0;
        mScrollView = findViewById(R.id.nstScrollView);
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int scrollContentHeight = mScrollView.getChildAt(0).getHeight();
                percScrolled = ((((float) scrollY) / ((float) (scrollContentHeight - scrollY))));
            }
        });

        tvIntroContent = findViewById(R.id.tv_intro_content);
        tvConclusionContent = findViewById(R.id.tv_conclusion_content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvIntroContent.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            tvConclusionContent.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        currentLesson = getIntent().getParcelableExtra("lesson");
        String introContent = getIntent().getStringExtra("intro");
        String conclusionContent = getIntent().getStringExtra("conclusion");
        ArrayList<Paragraph> paragraphs = getIntent().getParcelableArrayListExtra("paragraphs");

        timeReadingEstimation = getTimeReadingEstimation(introContent, conclusionContent, paragraphs);

        tvIntroContent.setText(introContent);
        tvConclusionContent.setText(conclusionContent);

        mParagraphRecyclerView = findViewById(R.id.paragraphs_recycler_view);
        mParagraphRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mParagraphRecyclerView.setHasFixedSize(true);
        mParagraphRecyclerView.setNestedScrollingEnabled(false);

        mParagraphAdapter = new ParagraphAdapter(paragraphs,false, CourseContentActivity.this);
        mParagraphRecyclerView.setAdapter(mParagraphAdapter);

        mAuth = FirebaseAuth.getInstance();
    }

    private Integer getTimeReadingEstimation(String introContent, String conclusionContent, ArrayList<Paragraph> paragraphs) {
        int wordsPerMinute = 200;
        String fullLessonText = introContent + conclusionContent;
        for (Paragraph p : paragraphs)
            fullLessonText += p.getContent();
        int noOfWords = fullLessonText.split(" ").length;
        double minutes = noOfWords / wordsPerMinute;
        int readTime = new Double(Math.ceil(minutes)).intValue();
        return readTime;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                currentTimeRead = System.currentTimeMillis() - startTime;
                int actualMinutes = (int) ((currentTimeRead / (1000*60)) % 60);
                Double perc = new Double(percScrolled*100);
                int intPerc = perc.intValue();

                String uid = mAuth.getCurrentUser().getUid();
                // TODO - check if the same notification is present. If yes update
                //checkNotification(uid);

                Notification notification;
                if(intPerc >= 100 && (actualMinutes >= timeReadingEstimation -1 ))
                    notification= new Notification( currentLesson.getTitle(), "full_text", "You've completed this lesson", false);
                else if(intPerc < 100)
                    notification= new Notification( currentLesson.getTitle(), "full_text", "You've read the "+intPerc+"% of the content", false);
                else
                    notification= new Notification( currentLesson.getTitle(), "full_text", "You've read too fast this lesson!", false);
                observerNotification = getNotificationObserver();
                notificationViewModel.putNotification(uid, notification);
                LiveData<String> repo = notificationViewModel.getMutableNotificationMessage();
                repo.observe(this, observerNotification);
                onBackPressed();
                break;
            }
        }
        return true;
    }

    private void checkNotification(String uid) {
        String urlDb = "notifications/"+uid;
        reference = database.getReference(urlDb);
        boolean exists = false;
        ChildEventListener listener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        reference.orderByChild("topic").addChildEventListener(listener);
    }

    private Observer<String> getNotificationObserver() {
        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        final Observer<String> observerCreationMessage = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String notificationMessage) {
                String msg = notificationMessage;
                if(msg.equals("success")){
                    Log.d("HomeActivity", "notification added");
                }else{
                    Log.d("HomeActivity", "creationMessage: "+msg);
                }

            }
        };
        return observerCreationMessage;
    }

    public double getPercentageReading(){
        int offset = mParagraphRecyclerView.computeVerticalScrollOffset();
        int extent = mParagraphRecyclerView.computeVerticalScrollExtent();
        int range = mParagraphRecyclerView.computeVerticalScrollRange();

        return (100.0f * offset / (double)(range - extent));
    }
}
