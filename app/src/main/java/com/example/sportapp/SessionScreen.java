package com.example.sportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SessionScreen extends BaseActivity
{
    private static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    TextView sessionName;
    TextView sessionLocation;
    TextView sessionDate;
    TextView sessionStartTime;
    TextView sessionEndTime;
    TextView sessionTrainer;
    TextView sessionTrainees;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_screen);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        sessionName = (TextView) findViewById(R.id.tvSessionNameS);
        sessionLocation = (TextView) findViewById(R.id.tvSessionLocationS);
        sessionDate = (TextView) findViewById(R.id.tvSessionDateS);
        sessionStartTime = (TextView) findViewById(R.id.tvSessionStartTimeS);
        sessionEndTime = (TextView) findViewById(R.id.tvSessionEndTimeS);
        sessionTrainer = (TextView) findViewById(R.id.tvSessionTrainerS);
        sessionTrainees = (TextView) findViewById(R.id.tvSessionTraineesS);
//        if(trainer == yes)
//        {
//            sessionTrainees.setVisibility(View.VISIBLE);
//        }
        extras = getIntent().getExtras();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        SpannableString content = new SpannableString(extras.getString("sessionName"));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        sessionName.setText(content);
        sessionLocation.setText(extras.getString("sessionLocation"));
        sessionDate.setText(extras.getString("sessionDate"));
        sessionStartTime.setText(extras.getString("sessionStartTime"));
        sessionEndTime.setText(extras.getString("sessionEndTime"));
        DocumentReference docRef = db.collection("trainers").document(extras.getString("sessionTrainer"));
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
                        sessionTrainer.setText(trainer.getFullName());
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
        if(extras.getBoolean("isPersonal"))
        {
            sessionTrainees.setText(extras.getString("sessionTrainee"));
        }
        else
        {
            sessionTrainees.setText(extras.getString("sessionTrainees"));
        }

    }
}