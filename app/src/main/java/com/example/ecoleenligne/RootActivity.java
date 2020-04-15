package com.example.ecoleenligne;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment navFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        FragmentManager fragmentManager = navFragment.getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment f : fragments){
            if(f instanceof SignInFragment){
                f.onActivityResult(requestCode, resultCode, data);
            }
        }

    }
}
