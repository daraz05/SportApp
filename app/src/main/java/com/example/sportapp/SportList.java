package com.example.sportapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SportList extends BaseActivity implements SportListRVAdapter.ItemClickListener {
    private static final String TAG = "TAG";
    SportListRVAdapter adapter;
    Intent openTrainersList;
    String sportName;
    FirebaseFirestore db;
    PersonalSession ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_list);

//        db = FirebaseFirestore.getInstance();
//        ps = new PersonalSession("Yoga with ofek", null, null, "test", "razdar05@gmail.com", "raz");
////        db.collection("personalSessions").document("1").set(ps);
//        db.collection("groupSessions").document().set(ps);

        // data to populate the RecyclerView with
        ArrayList<String> sportsNames = new ArrayList<>();
        sportsNames.add("Soccer");
        sportsNames.add("Basketball");
        sportsNames.add("Tennis");
        sportsNames.add("Gym");
        sportsNames.add("Running");
        sportsNames.add("Swimming");

        RecyclerView recyclerView = findViewById(R.id.rvSports);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SportListRVAdapter(this, sportsNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        openTrainersList = new Intent(this, sportTrainersList.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.trainers);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                switch (item.getItemId()) {
                    case R.id.trainers:
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.sessions:
                        startActivity(new Intent(getApplicationContext(),SessionsList.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
//                        startActivity(new Intent(getApplicationContext(),SignedIn.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

    }


    @Override
    public void onItemClick(View view, int position)
    {
        sportName = adapter.getItem(position);
        openTrainersList.putExtra("SPORT_NAME", sportName);
        startActivity(openTrainersList);
    }
}