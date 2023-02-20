package com.example.sportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Date;
import java.util.Calendar;
import java.util.Random;

public class SignedIn extends BaseActivity implements View.OnClickListener
{
    private static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    TextView textViewUser;
    EditText editTextPhone;
    Button buttonChangePhone;
    Button buttonUsersDataList;
    Button buttonUsersData1;
    Button buttonUsersData2;
    Button buttonCreateAlarm;
    Button buttonStartService;
    FirebaseFirestore db;
    Intent openQuery1;
    Intent openQuery2;
    Intent openShowUsers;
    Intent setAlarm;
    Intent testService;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_signed_in);
        mAuth = FirebaseAuth.getInstance();
        textViewUser = (TextView) findViewById(R.id.txtUser);
        editTextPhone = (EditText) findViewById(R.id.changePhone);
        buttonChangePhone = (Button) findViewById(R.id.btnChangePhone);
        buttonUsersDataList = (Button) findViewById(R.id.btnShowUsers);
        buttonUsersData1 = (Button) findViewById(R.id.btnUsersData1);
        buttonUsersData2 = (Button) findViewById(R.id.btnUsersData2);
        buttonCreateAlarm = (Button) findViewById(R.id.btnCreateAlarm);
        buttonStartService = (Button) findViewById(R.id.btnStartService);
        buttonUsersDataList.setOnClickListener(this);
        buttonUsersData1.setOnClickListener(this);
        buttonUsersData2.setOnClickListener(this);
        buttonChangePhone.setOnClickListener(this);
        buttonCreateAlarm.setOnClickListener(this);
        buttonStartService.setOnClickListener(this);
        openQuery1 = new Intent(this, QueryActivity1.class);
        openQuery2 = new Intent(this, QueryActivity2.class);
        openShowUsers = new Intent(this, ShowUsers.class);
        setAlarm = new Intent(AlarmClock.ACTION_SET_ALARM);
        testService = new Intent(this, TestService.class);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        textViewUser.setText(currentUser.getEmail());
    }


    @Override
    public void onClick (View view)
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(view == buttonCreateAlarm)
        {
            int hour = new Random().nextInt(25);
            int minute = new Random().nextInt(60);
            String hourS = Integer.toString(hour);
            String minuteS = Integer.toString(minute);
            setAlarm.putExtra(AlarmClock.EXTRA_HOUR, hour);
            setAlarm.putExtra(AlarmClock.EXTRA_MINUTES, minute);
            if(minute < 10)
            {
                setAlarm.putExtra(AlarmClock.EXTRA_MESSAGE, "You have a session at "+hourS+":"+"0"+minuteS);
            }
            else
            {
                setAlarm.putExtra(AlarmClock.EXTRA_MESSAGE, "You have a session at "+hourS+":"+minuteS);
            }
            startActivity(setAlarm);
        }
        if(view == buttonUsersDataList)
        {
            startActivity(openShowUsers);
        }
        if(view == buttonUsersData1)
        {
            startActivity(openQuery1);
        }
        if(view == buttonUsersData2)
        {
            startActivity(openQuery2);
        }
        if(view == buttonStartService)
        {
            startService(testService);
        }
        if(view == buttonChangePhone)
        {
            DocumentReference docRef = db.collection("trainees").document(currentUser.getEmail().toString());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists())
                        {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            Trainee trainee = document.toObject(Trainee.class);
                            trainee.setPhoneNumber(editTextPhone.getText().toString());
                            db.collection("trainees").document(currentUser.getEmail()).set(trainee);
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
}