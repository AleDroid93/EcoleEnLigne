package com.example.ecoleenligne;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntryFragment extends Fragment implements View.OnClickListener{
    private NavController navController;

    public EntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get the graph navigation reference from the starting point
        navController = Navigation.findNavController(view);

        // Binding click handler on fragments' buttons
        view.findViewById(R.id.signin_btn).setOnClickListener(this);
        view.findViewById(R.id.signup_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(navController == null){
            Log.w("EntryFragment", "onClick: Attenzione, Navigation Controller null!");
            return;
        }
        if(v != null){
            switch(v.getId()){
                case R.id.signup_btn:
                    navController.navigate(R.id.action_entryFragment_to_signUpFragment);
                    break;
                case R.id.signin_btn:
                    navController.navigate(R.id.action_entryFragment_to_loginActivity);
                    break;
            }
        }
    }
}
