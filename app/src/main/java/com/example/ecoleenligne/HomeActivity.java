package com.example.ecoleenligne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    com.google.firebase.auth.FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
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

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://ecoleenligne-1015c.firebaseio.com/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        FirebaseClient client = retrofit.create(FirebaseClient.class);

        Call<Map<String, FirebaseUser>> call = client.getUsers();

        call.enqueue(new Callback<Map<String, FirebaseUser>>() {
            @Override
            public void onResponse(Call<Map<String, FirebaseUser>> call, Response<Map<String, FirebaseUser>> response) {
                if(response == null)
                    Log.d("HomeActivity", "response: null");
                else
                    Log.d("HomeActivity", "response: "+ response.toString());


                Map<String, FirebaseUser> users = response.body();
                if(users != null)
                    Log.d("HomeActivity", "users: "+ users.isEmpty());
                    
                else
                    Log.d("HomeActivity", "users: null");

                Log.d("HomeActivity","onCreate call success: ");
            }

            @Override
            public void onFailure(Call<Map<String, FirebaseUser>> call, Throwable t) {
                String callInfo = call.request().url().toString();
                Log.d("HomeActivity","onCreate: Network failure on url "+ callInfo);
                Toast.makeText(HomeActivity.this, "Network failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(currentUser != null)
            FirebaseAuth.getInstance().signOut();
        finish();
    }
}
