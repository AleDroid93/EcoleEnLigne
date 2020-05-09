package com.example.ecoleenligne.views;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.activities.CourseContentActivity;
import com.example.ecoleenligne.activities.QuizActivity;
import com.example.ecoleenligne.activities.ResumeContentActivity;
import com.example.ecoleenligne.activities.VideoContentActivity;
import com.example.ecoleenligne.models.Lesson;
import com.example.ecoleenligne.models.Paragraph;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.models.Video;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LessonMenuFragment extends Fragment {
    private TextView lessonMenuTitle;
    private Lesson lesson;
    private View.OnClickListener onClickListener;
    private NavController navController;

    public LessonMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lesson_menu, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        String lessonName = getArguments().getString("lessonName");
        if(getArguments().getParcelable("lesson") != null)
            lesson = getArguments().getParcelable("lesson");
        int bgColor = getArguments().getInt("bgColor", 0);
        UserInfo currentUser = ((HomeActivity) getActivity()).getCurrentUser();
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //view.findViewById(R.id.lesson_menu_layout).setBackgroundColor(bgColor);

        instantiateOnClickListener();

        view.findViewById(R.id.course_card_view_item).setOnClickListener(onClickListener);
        view.findViewById(R.id.video_card_view_item).setOnClickListener(onClickListener);
        view.findViewById(R.id.resume_card_view_item).setOnClickListener(onClickListener);
        view.findViewById(R.id.quiz_card_view_item).setOnClickListener(onClickListener);
        view.findViewById(R.id.exercise_card_view_item).setOnClickListener(onClickListener);
        lessonMenuTitle = view.findViewById(R.id.tv_lesson_menu_title);
        lessonMenuTitle.setText(lessonName + " lesson");

    }

    public void instantiateOnClickListener(){
        this.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                Bundle bundle = new Bundle();
                switch (v.getId()){
                    case R.id.video_card_view_item:
                        message = "Video Item clicked!";

                        ArrayList<Video> videos = lesson.getVideos();
                        bundle.putParcelableArrayList("videos", videos);
                        bundle.putString("urlDb", "video");
                        navController.navigate(R.id.action_lessonMenuFragment_to_videosFragment, bundle);
                        break;
                    case R.id.course_card_view_item:
                        message = "Course Item clicked!";

                        ArrayList<Paragraph> paragraphs = lesson.getParagraphs();
                        String intro = lesson.getIntroduction();
                        String conclusion = lesson.getConclusion();
                        bundle.putParcelable("lesson", lesson);
                        bundle.putString("intro", intro);
                        bundle.putString("conclusion", conclusion);
                        bundle.putParcelableArrayList("paragraphs", paragraphs);
                        navController.navigate(R.id.action_lessonMenuFragment_to_courseFragment, bundle);
                        break;
                    case R.id.resume_card_view_item:
                        message = "Resume Item clicked!";

                        String resumeTitle = lesson.getTitle();
                        ArrayList<Paragraph> resumedParagraphs = lesson.getParagraphs();
                        bundle.putParcelableArrayList("resumes", resumedParagraphs);
                        bundle.putString("resumeTitle", resumeTitle);
                        navController.navigate(R.id.action_lessonMenuFragment_to_resumeFragment, bundle);
                        break;
                    case R.id.quiz_card_view_item:
                        message = "QuizItem Item clicked!";
                        navController.navigate(R.id.action_lessonMenuFragment_to_quizFragment);
                        break;
                    case R.id.exercise_card_view_item:
                        message = "Exercise Item clicked!";
                        navController.navigate(R.id.action_lessonMenuFragment_to_exerciseFragment);
                        break;

                }
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
    }
}
