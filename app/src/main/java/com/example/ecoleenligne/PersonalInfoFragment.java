package com.example.ecoleenligne;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.ecoleenligne.models.UserInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalInfoFragment extends Fragment implements View.OnClickListener {
    private NavController navController;
    private EditText mEdtName;
    private EditText mEdtSurname;
    private RadioButton genderButton;
    private UserInfo incomingUser;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_info, container, false);
    }

    public String onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        genderButton = (RadioButton) view;
        String gender = "";
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.opt_mother:
                if (checked)
                    Log.d("PersonalInfoFragment","onRadioButtonClicked: you are mother");
                    gender = "mother";
                    break;
            case R.id.opt_father:
                if (checked)
                    Log.d("PersonalInfoFragment","onRadioButtonClicked: you are father");
                    gender = "father";
                    break;
            case R.id.opt_other:
                if (checked)
                    Log.d("PersonalInfoFragment","onRadioButtonClicked: you are other");
                    gender = "other";
                    break;
        }
        return gender;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        incomingUser = getArguments().getParcelable("user");
        view.findViewById(R.id.signup_btn).setOnClickListener(PersonalInfoFragment.this);
        view.findViewById(R.id.back_btn).setOnClickListener(PersonalInfoFragment.this);
        mEdtName = view.findViewById(R.id.edtName);
        mEdtSurname = view.findViewById(R.id.edtSurname);
        genderButton = null;
    }


    @Override
    public void onClick(View v) {
        if(navController == null){
            Log.w("PersonalInfoFragment", "onClick: Attenzione, Navigation Controller null!");
            return;
        }
        if(v != null) {
            if(v instanceof RadioButton){
                String gender = onRadioButtonClicked(v);
                incomingUser.setGender(gender);
            }
            switch (v.getId()) {
                case R.id.signup_btn:
                    Log.d("PersonalInfoFragment", "onClick: next pressed!");
                    String name = mEdtName.getText().toString();
                    String surname = mEdtSurname.getText().toString();
                    Bundle bundle = new Bundle();
                    if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !name.equals(surname)) {
                        incomingUser.setName(name);
                        incomingUser.setSurname(surname);
                    }
                    bundle.putParcelable("user", incomingUser);
                    // TODO - salvare dati su SQLite e su Firebase DB
                    navController.navigate(R.id.action_personalInfoFragment_to_dashboardFragment, bundle);
                    break;
                case R.id.back_btn:
                    FragmentActivity activity = getActivity();
                    if(activity != null)
                        activity.onBackPressed();
                    else
                        Log.w("PersonalInfoFragment", "onClick: Attenzione, FragmentActivity null!");
                    break;
            }
        }
    }
}
