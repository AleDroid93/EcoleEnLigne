package com.example.ecoleenligne.fragments;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.HomeActivity2;
import com.example.ecoleenligne.R;
import com.example.ecoleenligne.data.NetworkMessage;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.repositories.UserInfoRepository;
import com.example.ecoleenligne.viewmodels.UserInfoViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentInfoFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private Spinner mSpinnerClass;
    private NavController navController;
    private EditText mEdtName;
    private EditText mEdtSurname;
    private RadioButton genderButton;
    private UserInfo incomingUser;
    private FirebaseAuth mAuth;
    private UserInfoRepository userInfoRepository;
    private UserInfoViewModel model;
    private Observer<NetworkMessage> observerCreationUser;
    private TextView tvChooseCourses;
    private ChipGroup coursesGroup;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();


    public StudentInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_info, container, false);
        // Inflate the layout for this fragment
        mSpinnerClass = view.findViewById(R.id.class_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.class_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinnerClass.setAdapter(adapter);
        mSpinnerClass.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("StudentInfoFragment", "onItemSelected: hai scelto " +
                parent.getItemAtPosition(position).toString());
        String uclass = parent.getItemAtPosition(position).toString();
        if(!(position == 0)){
            coursesGroup.setVisibility(View.VISIBLE);
            displayClassCourses(uclass);
            tvChooseCourses.setVisibility(View.VISIBLE);
        }else{
            coursesGroup.setVisibility(View.GONE);
            tvChooseCourses.setVisibility(View.GONE);
        }
        incomingUser.setUclass(uclass);
    }


    private void displayClassCourses(String uclass){
        HashMap<String,String> classesIds = new HashMap<>();
        classesIds.put("first", "cl0");
        classesIds.put("second","cl1");
        classesIds.put("third","cl2");
        String courseId = classesIds.get(uclass.toLowerCase());
        DatabaseReference reference = database.getReference("courses/"+courseId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, String> courses = (HashMap<String, String>) dataSnapshot.getValue();
                coursesGroup.removeAllViews();
                for(String course : courses.values()) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_layout, coursesGroup, false);
                    chip.setText(course);
                    chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                            Chip chip = (Chip) view;
                            if(isChecked){
                                chip.setChipBackgroundColorResource(R.color.lightColorAccent);
                                chip.setChipStrokeColorResource(R.color.colorAccent);
                                chip.setTextColor(getResources().getColor(R.color.colorAccent));
                            }else{
                                chip.setChipBackgroundColorResource(R.color.white);
                                chip.setTextColor(getResources().getColor(R.color.browser_actions_text_color));
                                chip.setChipStrokeColorResource(R.color.browser_actions_divider_color);
                            }
                        }
                    });
                    coursesGroup.addView(chip);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("StudentInfoFragment", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        incomingUser = getArguments().getParcelable("user");
        view.findViewById(R.id.signup_btn).setOnClickListener(StudentInfoFragment.this);
        view.findViewById(R.id.back_btn).setOnClickListener(StudentInfoFragment.this);
        mEdtName = view.findViewById(R.id.edtName);
        mEdtSurname = view.findViewById(R.id.edtSurname);
        tvChooseCourses = view.findViewById(R.id.tv_choose_courses);
        genderButton = null;
        coursesGroup = view.findViewById(R.id.course_chips);
        mAuth = FirebaseAuth.getInstance();

    }


    private Observer<NetworkMessage> getCreationUserObserver() {
        model = ViewModelProviders.of(StudentInfoFragment.this).get(UserInfoViewModel.class);
        final Observer<NetworkMessage> observerCreationMEssage = new Observer<NetworkMessage>() {
            @Override
            public void onChanged(@Nullable NetworkMessage creationMessage) {
                String msg = creationMessage.getMessage();
                if(msg.equals("success")){
                    Intent intent = new Intent(getActivity(), HomeActivity2.class);
                    intent.putExtra("user", incomingUser);
                    startActivity(intent);
                }else{
                    Log.d("ParentInfoFragment", "creationMessage: "+msg);
                }

            }
        };
        return observerCreationMEssage;
    }



    public String onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        genderButton = (RadioButton) view;
        String gender = "";
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.opt_female:
                if (checked)
                    Log.d("StudentInfoFragment","onRadioButtonClicked: you are female");
                gender = "female";
                break;
            case R.id.opt_male:
                if (checked)
                    Log.d("StudentInfoFragment","onRadioButtonClicked: you are father");
                gender = "male";
                break;
            case R.id.opt_otherg:
                if (checked)
                    Log.d("ParentInfoFragment","onRadioButtonClicked: you are other");
                gender = "other";
                break;
        }
        return gender;
    }

    @Override
    public void onClick(View v) {
        if(navController == null){
            Log.w("StudentInfoFragment", "onClick: Attenzione, Navigation Controller null!");
            return;
        }
        if(v != null) {
            if(v instanceof RadioButton){
                String gender = onRadioButtonClicked(v);
                incomingUser.setGender(gender);
            }
            switch (v.getId()) {
                case R.id.signup_btn:
                    Log.d("StudentInfoFragment", "onClick: next pressed!");
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
                        Log.w("StudentInfoFragment", "onClick: Attenzione, FragmentActivity null!");
                    break;
            }
        }
    }

    private void createNewUser(UserInfo user) {
        Log.d("StudentInfoFragment", "createAccount:" + user.getEmail());
        //showProgressBar();
        if(mAuth != null) {
            mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Log.d("StudentInfoFragment", "createUserWithEmail:success");
                                FirebaseUser fuser = mAuth.getCurrentUser();
                                user.setUid(fuser.getUid());
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("user", user);
                                observerCreationUser = getCreationUserObserver();
                                model.createUser(user.getUid(), user);
                                LiveData<NetworkMessage> repo = model.getCreationMessage();
                                repo.observe(StudentInfoFragment.this, observerCreationUser);
                            } else {
                                Log.w("StudentInfoFragment", "createUserWithEmail:failure", task.getException());
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
