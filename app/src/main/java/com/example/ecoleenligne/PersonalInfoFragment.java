package com.example.ecoleenligne;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalInfoFragment extends Fragment implements View.OnClickListener {


    public PersonalInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_info, container, false);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.opt_mother:
                if (checked)
                    Log.d("PersonalInfoFragment","onRadioButtonClicked: you are mother");
                    break;
            case R.id.opt_father:
                if (checked)
                    Log.d("PersonalInfoFragment","onRadioButtonClicked: you are father");
                    break;
            case R.id.opt_other:
                if (checked)
                    Log.d("PersonalInfoFragment","onRadioButtonClicked: you are other");
                    break;
        }
    }


    @Override
    public void onClick(View v) {
        if(v instanceof RadioButton){
           onRadioButtonClicked(v);
        }
    }
}
