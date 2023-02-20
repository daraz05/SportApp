package com.example.sportapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

public class ExpertisesSelect extends AppCompatActivity {

    // initialize variables
    TextView textView;
    EditText editTextDescription;
    boolean[] selectedSport;
    ArrayList<Integer> langList = new ArrayList<>();
    StringBuilder stringBuilder;
    String[] sportArray = {"Soccer", "Basketball", "Tennis", "Gym", "Running", "Swimming"};
    Button buttonFinish;
    Intent openSportList;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expertises_select);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // assign variable
        textView = findViewById(R.id.tvSelectExpertises);
        editTextDescription = (EditText) findViewById(R.id.descriptionSignUp);
        buttonFinish = (Button) findViewById(R.id.btnFinish);
        openSportList = new Intent(this, SportList.class);

        // initialize selected language array
        selectedSport = new boolean[sportArray.length];

        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ExpertisesSelect.this);

                // set title
                builder.setTitle("Select Expertises");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(sportArray, selectedSport, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position in lang list
                            langList.add(i);
                            // Sort array list
                            Collections.sort(langList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(sportArray[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        textView.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedSport.length; j++) {
                            // remove all selection
                            selectedSport[j] = false;
                            // clear language list
                            langList.clear();
                            // clear text view value
                            textView.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
            }
        });


        buttonFinish.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                FirebaseUser user = mAuth.getCurrentUser();
                db.collection("trainers").document(user.getEmail()).update("expertises", stringBuilder.toString());
                db.collection("trainers").document(user.getEmail()).update("description", editTextDescription.getText().toString());
                startActivity(openSportList);
                finish();

            }
        });


    }

    @Override
    public void onBackPressed()
    {

    }
}
