package com.example.sportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    EditText editTextEmail;
    EditText editTextPassword;
    Button btnSignIn;
    Intent openSignUpScreen;
    Intent openSignedIn;
    Intent openForgotPassword;
    Intent openSportList;
    TextView textViewSignUp;
    TextView textViewForgotPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.emailLogIn);
        editTextPassword = (EditText) findViewById(R.id.passwordLogIn);
        btnSignIn = (Button) findViewById(R.id.signInBtn);
        textViewSignUp = (TextView) findViewById(R.id.txtSignUp);
        textViewForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        btnSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);

        openForgotPassword = new Intent(this, ForgotPassword.class);
        openSignUpScreen = new Intent(this, SignUpScreen.class);
        openSignedIn = new Intent(this, SignedIn.class);
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
        startActivity(openSportList);
    }

    private void updateUI(Object o)
    {
    }

    @Override
    public void onClick(View view)
    {
        if(view == textViewSignUp)
        {
            startActivity(openSignUpScreen);
        }
        if(view == textViewForgotPassword)
        {
            startActivity(openForgotPassword);
        }
        if(view == btnSignIn)
        {
            mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                startActivity(openSportList);
                                finish();
                            }
                            else
                            {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }




}