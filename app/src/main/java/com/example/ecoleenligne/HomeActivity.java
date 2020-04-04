package com.example.ecoleenligne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private TextView welcomeTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcomeTv = findViewById(R.id.welcome_text_view);
        String welcome = getString(R.string.welcome_text);
        String userEmail = currentUser.getEmail();
        if(currentUser != null){
            welcomeTv.setText(welcome + "\n" + userEmail);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(currentUser != null)
            FirebaseAuth.getInstance().signOut();
        finish();
    }
}
