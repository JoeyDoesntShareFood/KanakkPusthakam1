package com.example.jyothisp.kanakkpusthakam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TripHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        setTitle(R.string.title_trip_history);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TripHistoryFragment()).commit();
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}
