package com.example.sportapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SessionsLVAdapter extends ArrayAdapter<Session>
{

    private Context context;
    public SessionsLVAdapter(@NonNull Context context, @NonNull ArrayList<Session> sessions)
    {
        super(context, 0, sessions);
        this.context = context;
    }

    public String convertTimestampToStringDate(com.google.firebase.Timestamp timestamp)
    {
        Date date = timestamp.toDate();
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    public String convertTimestampToStringTime(com.google.firebase.Timestamp timestamp)
    {
        Date date = timestamp.toDate();
        return new SimpleDateFormat("HH:mm").format(date);

    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItemView = convertView;

        if (listItemView==null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.session_item,parent,false);
        }

        Session session = getItem(position);

        TextView textSessionName = (TextView) listItemView.findViewById(R.id.tvSessionName);
        TextView textDate = (TextView) listItemView.findViewById(R.id.tvDate);
        TextView textStartTime = (TextView) listItemView.findViewById(R.id.tvStartTime);
        TextView textEndTime = (TextView) listItemView.findViewById(R.id.tvEndTime);
        TextView textLocation = (TextView) listItemView.findViewById(R.id.tvLocation);
        TextView textTrainerName = (TextView) listItemView.findViewById(R.id.tvTrainerName);


        textSessionName.setText(String.valueOf(session.getName()));
        textLocation.setText(String.valueOf(session.getLocation()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").document(session.getTrainer()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        Trainer trainer = document.toObject(Trainer.class);
                        textTrainerName.setText(trainer.getFullName());
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                    } else
                    {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
        textDate.setText(convertTimestampToStringDate(session.getStartTime()));
        textStartTime.setText(convertTimestampToStringTime(session.getStartTime()));
        textEndTime.setText(convertTimestampToStringTime(session.getEndTime()));

        listItemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, SessionScreen.class);
                intent.putExtra("sessionName", session.getName());
                intent.putExtra("sessionLocation", session.getLocation());
                intent.putExtra("sessionDate", convertTimestampToStringDate(session.getStartTime()));
                intent.putExtra("sessionStartTime", convertTimestampToStringTime(session.getStartTime()));
                intent.putExtra("sessionEndTime", convertTimestampToStringTime(session.getEndTime()));
                intent.putExtra("sessionTrainer", session.getTrainer());
                if(session instanceof PersonalSession)
                {
                   PersonalSession ps =  (PersonalSession) session;
                   intent.putExtra("sessionTrainee", ps.getTrainee());
                   intent.putExtra("isPersonal", true);
                }
                else
                {
                    GroupSession gs =  (GroupSession) session;
                    intent.putExtra("sessionTrainees", gs.getTrainees());
                    intent.putExtra("isPersonal", false);
                }
                context.startActivity(intent);
            }
        });


        return listItemView;
    }




}
