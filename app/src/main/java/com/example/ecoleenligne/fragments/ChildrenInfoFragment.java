package com.example.ecoleenligne.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecoleenligne.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChildrenInfoFragment extends Fragment {


    public ChildrenInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_students, container, false);
    }

}
