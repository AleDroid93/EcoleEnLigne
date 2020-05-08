package com.example.ecoleenligne.fragments;


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

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.views.HomeActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private UserInfo currentUser;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(currentUser == null) {
            currentUser = ((HomeActivity)getActivity()).getCurrentUser();
        }
        TextView tv = view.findViewById(R.id.welcome_text_view);
        if(currentUser != null)
            tv.setText("Hello\n"+ currentUser.toString());
    }

}
