package com.example.sportapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TrainerListProfile extends BaseActivity
{
    private static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    TextView trainerName;
    TextView trainerCity;
    TextView trainerDescription;
    TextView trainerPhoneNumber;
    String trainerEmail;
    SharedPreferences mPrefs;
    ArrayList<Session> alSessions;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_list_profile);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user  = mAuth.getCurrentUser();
        mPrefs = getPreferences(MODE_PRIVATE);
        trainerName = (TextView) findViewById(R.id.tvTrainerProfileName);
        trainerCity = (TextView) findViewById(R.id.tvTrainerProfileCity);
        trainerDescription = (TextView) findViewById(R.id.tvTrainerProfileDescription);
        trainerPhoneNumber = (TextView) findViewById(R.id.tvTrainerProfilePhoneNumber);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        alSessions = new ArrayList<>();
        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if(extras == null)
            {
                trainerEmail = null;
            }
            else
            {
                trainerEmail = extras.getString("trainerEmail");
            }
        }
        else
        {
            trainerEmail = (String) savedInstanceState.getSerializable("trainerEmail");
        }
        Task task1 = db.collection("personalSessions").get();
        Task task2 = db.collection("groupSessions").get();
        Task combinedTask = Tasks.whenAllSuccess(task1, task2);
        combinedTask.addOnSuccessListener(new OnSuccessListener<List<Object>>()
        {
            @Override
            public void onSuccess(List<Object> querySnapshots) {
                QuerySnapshot personalSessionsSnapshot = (QuerySnapshot) querySnapshots.get(0);
                QuerySnapshot groupSessionsSnapshot = (QuerySnapshot) querySnapshots.get(1);
                List<DocumentSnapshot> personalSessions = personalSessionsSnapshot.getDocuments();
                List<DocumentSnapshot> groupSessions = groupSessionsSnapshot.getDocuments();
                for (DocumentSnapshot document : personalSessions) {
                    if(String.valueOf(document.get("trainer")).equals(trainerEmail)) {
                        PersonalSession currentSession = document.toObject(PersonalSession.class);
                        alSessions.add(currentSession);
                    }
                }
                for (DocumentSnapshot document : groupSessions) {
                    if(String.valueOf(document.get("trainer")).equals(trainerEmail)) {
                        PersonalSession currentSession = document.toObject(PersonalSession.class);
                        Log.d(TAG, currentSession.getEndTime().toString());
                        alSessions.add(currentSession);
                    }
                }
                //sort the ArrayList here
                Collections.sort(alSessions, new Comparator<Session>() {
                    @Override
                    public int compare(Session o1, Session o2) {
                        return o1.getEndTime().compareTo(o2.getEndTime());
                    }
                });
                // update the ListView
                TrainerListProfileRVAdapter adapter = new TrainerListProfileRVAdapter(alSessions);
                recyclerView.setAdapter(adapter);
            }
        });
        combinedTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // we are displaying a toast message
                // when we get any error from Firebase.
                Toast.makeText(TrainerListProfile.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.trainers);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.trainers:
                        startActivity(new Intent(getApplicationContext(),SportList.class));
                        finishAffinity();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.sessions:
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
//                        startActivity(new Intent(getApplicationContext(),SignedIn.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        DocumentReference docRef = db.collection("trainers").document(trainerEmail);
        Log.d(TAG, "connectted docref");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {

                Log.d(TAG, "complete");
                if (task.isSuccessful())
                {
                    Log.d(TAG, "succesful");
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        Log.d(TAG, "checker"+"DocumentSnapshot data: " + document.getData());
                        Trainer trainer = document.toObject(Trainer.class);
                        trainerName.setText(trainer.getFullName());
                        trainerPhoneNumber.setText(trainer.getPhoneNumber());
                        trainerDescription.setText(trainer.getDescription());
                        trainerCity.setText(trainer.getCity());
                    }
                    else
                    {
                        Log.d(TAG, "No such document");
                    }
                }
                else
                {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}