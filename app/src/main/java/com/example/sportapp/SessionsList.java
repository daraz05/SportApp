package com.example.sportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SessionsList extends BaseActivity
{
    private static final String TAG = "TAG";
    FirebaseFirestore db;
    ListView lvSessions;
    ArrayList<Session> alSessions;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FloatingActionButton fab;
    Intent openCreateSession;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions_list);
        db = FirebaseFirestore.getInstance();
        lvSessions = findViewById(R.id.lvSessions);
        alSessions = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        openCreateSession = new Intent(this, createSession.class);
        loadAllSessionsToListView();
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(openCreateSession);
            }
        });
    }


    private void loadAllSessionsToListView()
    {
        Task task1 = db.collection("personalSessions").get();
        Task task2 = db.collection("groupSessions").get();
        Task combinedTask = Tasks.whenAllSuccess(task1, task2);
        combinedTask.addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> querySnapshots) {
                QuerySnapshot personalSessionsSnapshot = (QuerySnapshot) querySnapshots.get(0);
                QuerySnapshot groupSessionsSnapshot = (QuerySnapshot) querySnapshots.get(1);
                List<DocumentSnapshot> personalSessions = personalSessionsSnapshot.getDocuments();
                List<DocumentSnapshot> groupSessions = groupSessionsSnapshot.getDocuments();
                for (DocumentSnapshot document : personalSessions) {
                    if(String.valueOf(document.get("trainer")).equals(currentUser.getEmail().toString()) || String.valueOf(document.get("trainee")).equals(currentUser.getEmail().toString())) {
                        PersonalSession currentSession = document.toObject(PersonalSession.class);
                        alSessions.add(currentSession);
                    }
                }
                for (DocumentSnapshot document : groupSessions) {
                    if(String.valueOf(document.get("trainer")).equals(currentUser.getEmail().toString())) {
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
                SessionsLVAdapter adapter = new SessionsLVAdapter(SessionsList.this, alSessions);
                lvSessions.setAdapter(adapter);
            }
        });
        combinedTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // we are displaying a toast message
                // when we get any error from Firebase.
                Toast.makeText(SessionsList.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
            }
        });
    }


}