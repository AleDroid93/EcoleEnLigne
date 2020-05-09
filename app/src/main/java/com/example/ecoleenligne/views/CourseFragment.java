package com.example.ecoleenligne.views;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.activities.CourseContentActivity;
import com.example.ecoleenligne.adapters.ParagraphAdapter;
import com.example.ecoleenligne.models.Lesson;
import com.example.ecoleenligne.models.Notification;
import com.example.ecoleenligne.models.Paragraph;
import com.example.ecoleenligne.repositories.NotificationRepository;
import com.example.ecoleenligne.viewmodels.NotificationViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {

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


    public CourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startTime = System.currentTimeMillis();
        percScrolled = 0.0;
        mScrollView = view.findViewById(R.id.nstScrollView);
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int scrollContentHeight = mScrollView.getChildAt(0).getHeight();
                percScrolled = ((((float) scrollY) / ((float) (scrollContentHeight - scrollY))));
            }
        });

        tvIntroContent = view.findViewById(R.id.tv_intro_content);
        tvConclusionContent = view.findViewById(R.id.tv_conclusion_content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvIntroContent.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            tvConclusionContent.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        currentLesson = getArguments().getParcelable("lesson");
        String introContent = getArguments().getString("intro");
        String conclusionContent = getArguments().getString("conclusion");
        ArrayList<Paragraph> paragraphs = getArguments().getParcelableArrayList("paragraphs");

        timeReadingEstimation = getTimeReadingEstimation(introContent, conclusionContent, paragraphs);

        tvIntroContent.setText(introContent);
        tvConclusionContent.setText(conclusionContent);

        mParagraphRecyclerView = view.findViewById(R.id.paragraphs_recycler_view);
        mParagraphRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mParagraphRecyclerView.setHasFixedSize(true);
        mParagraphRecyclerView.setNestedScrollingEnabled(false);

        mParagraphAdapter = new ParagraphAdapter(paragraphs,false, getActivity());
        mParagraphRecyclerView.setAdapter(mParagraphAdapter);

        mAuth = FirebaseAuth.getInstance();

        HomeActivity parentActivity = (HomeActivity) getActivity();
        notificationViewModel = parentActivity.getNotificationViewModel();
        observerNotification = parentActivity.getNotificationMessageObserver();
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


    public void computeReadingState(){
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

        LiveData<String> repo = notificationViewModel.getMutableNotificationMessage();
        notificationViewModel.putNotification(uid, notification);

        repo.observe(getActivity(), observerNotification);
    }


}
