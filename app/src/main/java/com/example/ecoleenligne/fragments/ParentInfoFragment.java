package com.example.ecoleenligne.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.data.NetworkMessage;
import com.example.ecoleenligne.models.Child;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.repositories.UserInfoRepository;
import com.example.ecoleenligne.viewmodels.UserInfoViewModel;
import com.example.ecoleenligne.views.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParentInfoFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "ParentInfoFragment";
    private NavController navController;
    private EditText mEdtName;
    private EditText mEdtSurname;
    private RadioButton genderButton;
    private UserInfo incomingUser;
    private Switch switchLearningOffline;
    private FirebaseAuth mAuth;
    private UserInfo incomingChild;
    private UserInfoRepository userInfoRepository;
    private UserInfoViewModel model;

    private Observer<NetworkMessage> observerCreationUser;
    private Observer<String> observerChildrenCredentials;

    public ParentInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_parent_info, container, false);
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
                    Log.d("ParentInfoFragment","onRadioButtonClicked: you are mother");
                    gender = "mother";
                    break;
            case R.id.opt_father:
                if (checked)
                    Log.d("ParentInfoFragment","onRadioButtonClicked: you are father");
                    gender = "father";
                    break;
            case R.id.opt_other:
                if (checked)
                    Log.d("ParentInfoFragment","onRadioButtonClicked: you are other");
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
        incomingChild = getArguments().getParcelable("child");

        if(incomingChild != null) {
            // TODO risolvere bug: L'utente arriva con attributi vuoti
            Child child = new Child(incomingChild.getUid(), incomingChild.getName(), incomingChild.getSurname(),
                   incomingChild.getUserClass().getId(), incomingChild.getGender(), incomingChild.getEmail(), incomingChild.getPassword());
            incomingUser.addChild(child);
            mEdtName = (EditText)view.findViewById(R.id.edtName);
            mEdtName.setText(incomingUser.getName());
            mEdtSurname = (EditText)view.findViewById(R.id.edtSurname);
            mEdtSurname.setText(incomingUser.getSurname());
            String role = incomingUser.getGender();
            RadioButton btn;
            if(role.equalsIgnoreCase("mother")){
                btn = view.findViewById(R.id.opt_mother);
            }else if(role.equalsIgnoreCase("father")){
                btn = view.findViewById(R.id.opt_father);
            }else{
                btn = view.findViewById(R.id.opt_other);
            }

            btn.toggle();
            switchLearningOffline = view.findViewById(R.id.switchLearning);
            switchLearningOffline.setChecked(incomingUser.getOfflineLearning());
        }
        mEdtName = view.findViewById(R.id.edtName);
        mEdtSurname = view.findViewById(R.id.edtSurname);

        view.findViewById(R.id.signup_btn).setOnClickListener(ParentInfoFragment.this);
        view.findViewById(R.id.back_btn).setOnClickListener(ParentInfoFragment.this);
        view.findViewById(R.id.fab_add_child).setOnClickListener(ParentInfoFragment.this);
        switchLearningOffline = view.findViewById(R.id.switchLearning);
        switchLearningOffline.setOnCheckedChangeListener(ParentInfoFragment.this);

        genderButton = null;
        mAuth = FirebaseAuth.getInstance();

    }


    private Observer<NetworkMessage> getCreationUserObserver() {
        model = ViewModelProviders.of(ParentInfoFragment.this).get(UserInfoViewModel.class);
        final Observer<NetworkMessage> observerCreationMEssage = new Observer<NetworkMessage>() {
            @Override
            public void onChanged(@Nullable NetworkMessage creationMessage) {
                String msg = creationMessage.getMessage();
                if(msg.equals("success")){
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra("user", incomingUser);
                    startActivity(intent);
                }else{
                    Log.d("ParentInfoFragment", "creationMessage: "+msg);
                }

            }
        };
        return observerCreationMEssage;
    }

    private Observer<String> getChildrenCredentialsObserver() {
        model = ViewModelProviders.of(ParentInfoFragment.this).get(UserInfoViewModel.class);
        final Observer<String> observerChildrenCredentials = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String childrenCredentialsMessage) {
                String msg = childrenCredentialsMessage;
                if(msg.equals("success")){
                    Log.d("ParentInfoFragment", "childrenCredentialsMessage: "+msg);
                }else{
                    Log.d("ParentInfoFragment", "childrenCredentialsMessage: "+msg);
                }

            }
        };
        return observerChildrenCredentials;
    }

    @Override
    public void onClick(View v) {
        if(navController == null){
            Log.w("ParentInfoFragment", "onClick: Attenzione, Navigation Controller null!");
            return;
        }
        if(v != null) {
            if(v instanceof RadioButton){
                String gender = onRadioButtonClicked(v);
                incomingUser.setGender(gender);
            }
            switch (v.getId()) {
                case R.id.signup_btn:
                    Log.d("ParentInfoFragment", "onClick: next pressed!");
                    String name = mEdtName.getText().toString();
                    String surname = mEdtSurname.getText().toString();

                    if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !name.equals(surname)) {
                        incomingUser.setName(name);
                        incomingUser.setSurname(surname);
                    }
                    createNewUser(incomingUser);
                    break;
                case R.id.back_btn:
                    FragmentActivity activity = getActivity();
                    if(activity != null)
                        activity.onBackPressed();
                    else
                        Log.w("ParentInfoFragment", "onClick: Attenzione, FragmentActivity null!");
                    break;
                case R.id.fab_add_child:
                    String nameProv= mEdtName.getText().toString();
                    String surnameProv = mEdtSurname.getText().toString();
                    incomingUser.setName(nameProv);
                    incomingUser.setSurname(surnameProv);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fromParent", true);
                    bundle.putParcelable("user", incomingUser);
                    navController.navigate(R.id.action_parentInfoFragment_to_studentInfoFragment, bundle);
                    break;
            }
        }
    }

    private void createNewUser(UserInfo user) {
        Log.d(TAG, "createAccount:" + user.getEmail());
        //showProgressBar();
        if(mAuth != null) {
            mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser fuser = mAuth.getCurrentUser();
                                user.setUid(fuser.getUid());
                                ArrayList<Child> children = user.getChildren();


                                Bundle bundle = new Bundle();
                                bundle.putParcelable("user", user);
                                //userInfoRepository = UserInfoRepository.getInstance();
                                observerCreationUser = getCreationUserObserver();
                                observerChildrenCredentials = getChildrenCredentialsObserver();

                                model.sendChildrenCredentials(user.getChildren(), user.getEmail());
                                LiveData<String> repo = model.getCredentialsEmailMessage();
                                repo.observe(ParentInfoFragment.this, observerChildrenCredentials);

                                model.createUser(user.getUid(), user);
                                LiveData<NetworkMessage> repoUserCreation = model.getCreationMessage();
                                repoUserCreation.observe(ParentInfoFragment.this, observerCreationUser);

                            } else {

                                Log.w("ParentInfoFragment", "createUserWithEmail:failure", task.getException());
                                String message = task.getException().getMessage();
                                Toast.makeText(getActivity(), "Registration failed. "+ message,
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                            //hideProgressBar();
                        }
                    });
        }
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d("ParentInfoFragment", "onCheckedChanged: "+ isChecked);
        if(isChecked){
            incomingUser.setOfflineLearning(true);
        }else{
            incomingUser.setOfflineLearning(false);
        }
    }
}
