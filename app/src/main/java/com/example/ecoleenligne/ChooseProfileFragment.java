package com.example.ecoleenligne;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecoleenligne.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseProfileFragment extends Fragment {


    public ChooseProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_profile, container, false);
    }

}
