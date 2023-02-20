package com.example.sportapp;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Date;
import java.time.Month;


public class SignUpScreen extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    Spinner dropdown;
    EditText editTextEmailSignUp;
    EditText editTextPasswordSignUp;
    EditText editTextFullName;
    EditText editTextPhoneNumber;
    EditText editTextCity;
    Button buttonSignUp;
    DatePicker datePickerBirthDate;
    Intent openSignedIn;
    Intent openSportList;
    Intent openExpertisesSelect;
    Switch switchIsTrainer;
    String birthDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        dropdown = (Spinner) findViewById(R.id.spinner1);
        String[] items = new String[]{"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        datePickerBirthDate = (DatePicker) findViewById(R.id.dpBirthDate);
        buttonSignUp = (Button) findViewById(R.id.SignUpBtn);
        editTextEmailSignUp = (EditText) findViewById(R.id.emailSignUp);
        editTextPasswordSignUp = (EditText) findViewById(R.id.passwordSignUp);
        editTextFullName = (EditText) findViewById(R.id.fullNameSignUp);
        editTextPhoneNumber = (EditText) findViewById(R.id.phoneNumberSignUp);
        editTextCity = (EditText) findViewById(R.id.citySignUp);
        switchIsTrainer = (Switch) findViewById(R.id.swIsTrainer);


        buttonSignUp.setOnClickListener(this);

        openSignedIn = new Intent(this, SignedIn.class);
        openExpertisesSelect = new Intent(this, ExpertisesSelect.class);
        openSportList = new Intent(this, SportList.class);






    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            reload();
        }
    }
    private void reload()
    {
    }

    private void updateUI(Object o)
    {
    }

    @Override
    public void onClick(View view)
    {
        if(view == buttonSignUp)
        {
            birthDate = datePickerBirthDate.getDayOfMonth()+"/"+(datePickerBirthDate.getMonth()+1)+"/"+datePickerBirthDate.getYear();
            Trainer trainer = new Trainer(editTextFullName.getText().toString(), birthDate, dropdown.getSelectedItem().toString(), editTextEmailSignUp.getText().toString(), editTextPhoneNumber.getText().toString(), editTextCity.getText().toString(), "" , "");
            Trainee trainee = new Trainee(editTextFullName.getText().toString(), birthDate, dropdown.getSelectedItem().toString(), editTextEmailSignUp.getText().toString(), editTextPhoneNumber.getText().toString(), editTextCity.getText().toString());
            mAuth.createUserWithEmailAndPassword(editTextEmailSignUp.getText().toString(), editTextPasswordSignUp.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(switchIsTrainer.isChecked())
                                {
                                    db.collection("trainers").document(user.getEmail()).set(trainer);
                                    updateUI(user);
                                    startActivity(openExpertisesSelect);
                                    finish();
                                }
                                else
                                {
                                    db.collection("trainees").document(user.getEmail()).set(trainee);
                                    updateUI(user);
                                    startActivity(openSportList);
                                    finish();
                                }
                            }
                            else
                            {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpScreen.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }
}

