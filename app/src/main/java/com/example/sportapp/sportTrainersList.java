package com.example.sportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class  sportTrainersList extends BaseActivity
{
    private static final String TAG = "TAG";
    FirebaseFirestore db;
    ListView lvTrainers;
    ArrayList<Trainer> alTrainers;
    String newString;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_trainers_list);
        db = FirebaseFirestore.getInstance();
        lvTrainers = findViewById(R.id.lvSportTrainers);
        alTrainers = new ArrayList<>();
        loadUsersToListView();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("SPORT_NAME");
            }
        } else {
            newString = (String) savedInstanceState.getSerializable("SPORT_NAME");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.trainers);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                switch (item.getItemId()) {
                    case R.id.trainers:
                        startActivity(new Intent(getApplicationContext(), SportList.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.sessions:
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



    private boolean isSportInExpertises(String expertises, String sport)
    {
        ArrayList<String> arr =  new ArrayList<String>();;
        String word = "";
        for(int i = 0; i < expertises.length(); i++)
        {
            if(expertises.charAt(i) == ',')
            {
                arr.add(word);
                word = "";
            }

            if(expertises.charAt(i) != ',' && expertises.charAt(i) != ' ')
                word += expertises.charAt(i);
        }
        arr.add(word);
        if(arr.contains(sport))
            return true;
        else
            return false;
    }

    private void loadUsersToListView()
    {
        db.collection("trainers").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                    {
                        if (!queryDocumentSnapshots.isEmpty())
                        {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            Log.d(TAG, "List size: " + list.size());
                            for (DocumentSnapshot document : list)
                            {
                                Log.d(TAG, newString);
                                if(isSportInExpertises(String.valueOf(document.get("expertises")), newString))
                                {
                                    Trainer trainer = document.toObject(Trainer.class);
                                    Log.d(TAG, document.getData().toString());
                                    alTrainers.add(trainer);
                                }
                            }
                            // after that we are passing our array list to our adapter class.
                            SportTrainersLVAdapter adapter = new SportTrainersLVAdapter(sportTrainersList.this, alTrainers);

                            // after passing this array list to our adapter
                            // class we are setting our adapter to our list view.
                            lvTrainers.setAdapter(adapter);
                        }
                        else
                        {
                            Log.d(TAG,"No data found in query");
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(sportTrainersList.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // we are displaying a toast message
                        // when we get any error from Firebase.
                        Toast.makeText(sportTrainersList.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}