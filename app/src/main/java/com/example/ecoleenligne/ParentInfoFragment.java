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
import android.widget.Toast;

import com.example.ecoleenligne.data.NetworkMessage;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.repositories.UserInfoRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParentInfoFragment extends Fragment implements View.OnClickListener {
    private NavController navController;
    private EditText mEdtName;
    private EditText mEdtSurname;
    private RadioButton genderButton;
    private UserInfo incomingUser;
    private FirebaseAuth mAuth;

    private UserInfoRepository userInfoRepository;


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
        view.findViewById(R.id.signup_btn).setOnClickListener(ParentInfoFragment.this);
        view.findViewById(R.id.back_btn).setOnClickListener(ParentInfoFragment.this);
        mEdtName = view.findViewById(R.id.edtName);
        mEdtSurname = view.findViewById(R.id.edtSurname);
        genderButton = null;
        mAuth = FirebaseAuth.getInstance();
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
                    // TODO - salvare dati su SQLite e su Firebase DB
                    createNewUser(incomingUser);
                    break;
                case R.id.back_btn:
                    FragmentActivity activity = getActivity();
                    if(activity != null)
                        activity.onBackPressed();
                    else
                        Log.w("ParentInfoFragment", "onClick: Attenzione, FragmentActivity null!");
                    break;
            }
        }
    }

    private void createNewUser(UserInfo user) {
        Log.d("ParentInfoFragment", "createAccount:" + user.getEmail());
        //showProgressBar();
        if(mAuth != null) {
            mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Log.d("ParentInfoFragment", "createUserWithEmail:success");
                                FirebaseUser fuser = mAuth.getCurrentUser();
                                user.setUid(fuser.getUid());
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("user", user);
                                userInfoRepository = UserInfoRepository.getInstance();
                                NetworkMessage message = userInfoRepository.createUser(fuser.getUid(), user);
                                if(message.getMessage().equals("success"))
                                    navController.navigate(R.id.action_personalInfoFragment_to_dashboardFragment, bundle);
                                else
                                    Toast.makeText(getActivity(), "User creation failed.", Toast.LENGTH_SHORT).show();
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
}
