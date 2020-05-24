package com.example.ecoleenligne.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ecoleenligne.R;

public class SearchResultsActivity extends AppCompatActivity {
    public static final String TAG = "SearchResultsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"Sono qui");

        setContentView(R.layout.activity_search_results);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.e(TAG,"gestisco intent");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //TODO use the query to search your data somehow
            Log.e(TAG,"intent ricevuto! Far partre la ricerca dei corsi");
            finish();
        }else{
            Log.e(TAG,"non è arrivato l'intent");
        }
    }

}
