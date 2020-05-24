package com.example.ecoleenligne.views;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.adapters.ParagraphAdapter;
import com.example.ecoleenligne.models.Paragraph;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResumeFragment extends Fragment {

    private RecyclerView mResumesRecyclerView;
    private ParagraphAdapter mResumeAdapter;
    private TextView tvResumeTitle;


    public ResumeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resume, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvResumeTitle = view.findViewById(R.id.tv_resume_title);

        String title =getArguments().getString("resumeTitle");
        tvResumeTitle.setText(getResources().getString(R.string.resume_title, title));
        ArrayList<Paragraph> paragraphs = getArguments().getParcelableArrayList("resumes");

        mResumesRecyclerView= view.findViewById(R.id.resumes_recycler_view);
        mResumesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mResumesRecyclerView.setHasFixedSize(true);
        mResumesRecyclerView.setNestedScrollingEnabled(false);

        mResumeAdapter = new ParagraphAdapter(paragraphs,true, getActivity());
        mResumesRecyclerView.setAdapter(mResumeAdapter);
    }
}
