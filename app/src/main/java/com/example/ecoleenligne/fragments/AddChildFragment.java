package com.example.ecoleenligne.fragments;


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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.Child;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.repositories.UserInfoRepository;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddChildFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private Spinner mSpinnerClass;
    private NavController navController;
    private EditText mEdtName;
    private EditText mEdtSurname;
    private RadioButton genderButton;
    private UserInfo incomingUser;
    private Child child;

    public AddChildFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_child, container, false);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        incomingUser = getArguments().getParcelable("user");
        view.findViewById(R.id.confirm_btn).setOnClickListener(AddChildFragment.this);
        mEdtName = view.findViewById(R.id.edtName);
        mEdtSurname = view.findViewById(R.id.edtSurname);
        genderButton = null;
        child = new Child();
    }


    @Override
    public void onClick(View v) {
        if(navController == null){
            Log.w("AddChildFragment", "onClick: Attenzione, Navigation Controller null!");
            return;
        }
        if(v != null) {
            if(v instanceof RadioButton){
                String gender = onRadioButtonClicked(v);
                child.setGender(gender);
            }
            switch (v.getId()) {
                case R.id.confirm_btn:
                    Log.d("AddChildFragment", "onClick: next pressed!");
                    String name = mEdtName.getText().toString();
                    String surname = mEdtSurname.getText().toString();

                    if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !name.equals(surname)) {
                        child.setName(name);
                        child.setSurname(surname);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("user", incomingUser);
                        bundle.putParcelable("child", child);
                        navController.navigate(R.id.action_addChildFragment_to_parentInfoFragment,bundle);
                    }
                    break;
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("AddChildFragment", "onItemSelected: hai scelto " +
                parent.getItemAtPosition(position).toString());
        String uclass = parent.getItemAtPosition(position).toString();
        child.setClassroom(uclass);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                    Log.d("AddChildFragment","onRadioButtonClicked: you are female");
                gender = "female";
                break;
            case R.id.opt_male:
                if (checked)
                    Log.d("AddChildFragment","onRadioButtonClicked: you are father");
                gender = "male";
                break;
            case R.id.opt_otherg:
                if (checked)
                    Log.d("AddChildFragment","onRadioButtonClicked: you are other");
                gender = "other";
                break;
        }
        return gender;
    }
}
