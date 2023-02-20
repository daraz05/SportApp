package com.example.sportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowUsers extends BaseActivity
{
    private static final String TAG = "TAG";
    FirebaseFirestore db;
    ListView lvTrainees;
    ArrayList<Trainee> alTrainees;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);
        db = FirebaseFirestore.getInstance();
        lvTrainees = findViewById(R.id.lvTrainees);
        alTrainees = new ArrayList<>();
        loadUsersToListView();

    }

    private void loadUsersToListView()
    {
        db.collection("trainees").get()
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
                                Trainee trainee = document.toObject(Trainee.class);
                                Log.d(TAG, document.getData().toString());
                                alTrainees.add(trainee);
                            }
                            // after that we are passing our array list to our adapter class.
                            TraineesLVAdapter adapter = new TraineesLVAdapter(ShowUsers.this, alTrainees);

                            // after passing this array list to our adapter
                            // class we are setting our adapter to our list view.
                            lvTrainees.setAdapter(adapter);
                        }
                        else
                        {
                            Log.d(TAG,"No data found in query");
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(ShowUsers.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // we are displaying a toast message
                        // when we get any error from Firebase.
                        Toast.makeText(ShowUsers.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}