package com.example.ecoleenligne;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseClassFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private Spinner mSpinnerClass;
    private NavController navController;


    public ChooseClassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_class, container, false);
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
        Log.d("ChooseClassFragment", "onItemSelected: hai scelto " +
                parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.next_btn).setOnClickListener(ChooseClassFragment.this);
        view.findViewById(R.id.back_btn).setOnClickListener(ChooseClassFragment.this);
    }


    @Override
    public void onClick(View v) {
        if(navController == null){
            Log.w("ChooseClassFragment", "onClick: Attenzione, Navigation Controller null!");
            return;
        }
        if(v != null) {
            switch (v.getId()) {
                case R.id.next_btn:
                    Log.d("ChooseClassFragment", "onClick: next pressed!");
                    navController.navigate(R.id.action_chooseClassFragment_to_personalInfoFragment);
                    break;
                case R.id.back_btn:
                    FragmentActivity activity = getActivity();
                    if(activity != null)
                        activity.onBackPressed();
                    else
                        Log.w("ChooseClassFragment", "onClick: Attenzione, FragmentActivity null!");
                    break;
            }
        }
    }

}
