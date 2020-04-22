package com.example.ecoleenligne.fragments;

import com.example.ecoleenligne.models.UserInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ecoleenligne.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner mSpinnerProfile;
    private NavController navController;
    private UserInfo incomingUser;

    public ChooseProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_profile, container, false);
        // Inflate the layout for this fragment
        mSpinnerProfile = view.findViewById(R.id.profile_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.profile_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinnerProfile.setAdapter(adapter);
        mSpinnerProfile.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("ChooseProfileFragment", "onItemSelected: hai scelto " +
                parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.next_btn).setOnClickListener(ChooseProfileFragment.this);
        view.findViewById(R.id.back_btn).setOnClickListener(ChooseProfileFragment.this);
        incomingUser = getArguments().getParcelable("user");
    }


    @Override
    public void onClick(View v) {
        if(navController == null){
            Log.w("ChooseProfileFragment", "onClick: Attenzione, Navigation Controller null!");
            return;
        }
        if(v != null) {
            switch (v.getId()) {
                case R.id.next_btn:
                    Log.d("ChooseProfileFragment", "onClick: next pressed!");
                    String profile = mSpinnerProfile.getSelectedItem().toString();
                    incomingUser.setRole(profile);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("user",incomingUser);
                    if(profile.equalsIgnoreCase("student"))
                        navController.navigate(R.id.action_chooseProfileFragment_to_chooseClassFragment, bundle);
                    else
                        navController.navigate(R.id.action_chooseProfileFragment_to_personalInfoFragment, bundle);
                    break;
                case R.id.back_btn:
                    FragmentActivity activity = getActivity();
                    if(activity != null)
                        activity.onBackPressed();
                    else
                        Log.w("ChooseProfileFragment", "onClick: Attenzione, FragmentActivity null!");
                    break;
            }
        }
    }
}
