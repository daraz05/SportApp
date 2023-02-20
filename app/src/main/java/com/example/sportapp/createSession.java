package com.example.sportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class createSession extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    Boolean isPersonal;
    Button createSession;
    EditText sessionName;
    EditText sessionLocation;
    Timestamp startTime;
    Timestamp endTime;
    Date dateStart;
    Date dateEnd;
    DatePicker datePicker;
    TimePicker startTimePicker;
    TimePicker endTimePicker;
    Calendar cal;
    FirebaseUser currentUser;
    Spinner dropdown;
    String sports = "first";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        createSession = (Button) findViewById(R.id.btnCreateSession);
        sessionName = (EditText) findViewById(R.id.tvSessionNameCreate);
        sessionLocation = (EditText) findViewById(R.id.tvSessionLocationCreate);
        datePicker = (DatePicker) findViewById(R.id.dpSessionDate);
        startTimePicker = (TimePicker) findViewById(R.id.tpStart);
        endTimePicker = (TimePicker) findViewById(R.id.tpEnd);
        createSession.setOnClickListener(this);
        cal = Calendar.getInstance();
        currentUser = mAuth.getCurrentUser();
        dropdown = (Spinner) findViewById(R.id.spinner2);
        DocumentReference docRef = db.collection("trainers").document(currentUser.getEmail());
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
                        Trainer trainer = document.toObject(Trainer.class);
                        sports = trainer.getExpertises();
                        String[] items = stringToArray(sports);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(createSession.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
                        dropdown.setAdapter(adapter);
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
        Log.d("KAKA4", sports);
    }

     public String[] stringToArray(String sports)
     {
         int count = 1;
         for (int i = 0; i < sports.length(); i++)
         {
             if(sports.charAt(i) == ',')
                 count++;
         }
         String[] items = new String[count];
         StringBuilder word = new StringBuilder();
         count = 0;
         for (int k = 0; k < sports.length(); k++)
         {
            if(sports.charAt(k) != ',' && sports.charAt(k) != ' ')
                word.append(sports.charAt(k));
            if(sports.charAt(k) == ',' || k == sports.length()-1)
            {
                items[count] = word.toString();
                count++;
                word = new StringBuilder();
            }
         }
         return items;
     }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_personal:
                if (checked)
                {
                    isPersonal = true;
                }
                    break;
            case R.id.radio_group:
                if (checked)
                {
                   isPersonal = false;
                }
                    break;
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View view)
    {
        if(view == createSession)
        {
            cal.set(Calendar.YEAR, datePicker.getYear());
            cal.set(Calendar.MONTH, datePicker.getMonth());
            cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth()-1);
            cal.set(Calendar.HOUR, startTimePicker.getHour());
            cal.set(Calendar.MINUTE, startTimePicker.getMinute());
            dateStart = cal.getTime();
            startTime = new Timestamp(dateStart);
            cal.set(Calendar.HOUR, endTimePicker.getHour());
            cal.set(Calendar.MINUTE, endTimePicker.getMinute());
            dateEnd = cal.getTime();
            endTime = new Timestamp(dateEnd);
            if(isPersonal)
            {
                PersonalSession personalSession = new PersonalSession(sessionName.getText().toString(), startTime, endTime, sessionLocation.getText().toString(), currentUser.getEmail(), dropdown.getSelectedItem().toString(), null);
                db.collection("personalSessions").document().set(personalSession);
            }
            else
            {
                GroupSession groupSession = new GroupSession(sessionName.getText().toString(), startTime, endTime, sessionLocation.getText().toString(), currentUser.getEmail(), dropdown.getSelectedItem().toString(), null);
                db.collection("groupSessions").document().set(groupSession);
            }
            finish();
        }
    }
}