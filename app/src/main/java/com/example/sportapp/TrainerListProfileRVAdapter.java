package com.example.sportapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TrainerListProfileRVAdapter extends RecyclerView.Adapter<TrainerListProfileRVAdapter.SessionViewHolder>
{
    private ArrayList<Session> alSessions;

    public TrainerListProfileRVAdapter(ArrayList<Session> alSessions)
    {
        this.alSessions = alSessions;
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
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View sessionView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sessions_trainer_profile_item, parent, false);
        return new SessionViewHolder(sessionView);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Session currentSession = alSessions.get(position);
        holder.textSessionNameRv.setText(currentSession.getName());
        holder.textDateRv.setText(convertTimestampToStringDate(currentSession.getStartTime()));
        holder.textStartTimeRv.setText(convertTimestampToStringTime(currentSession.getStartTime()));
        holder.textEndTimeRv.setText(convertTimestampToStringTime(currentSession.getEndTime()));
        holder.textLocationRv.setText(currentSession.getLocation());
        holder.textSportRv.setText(currentSession.getSport());

    }

    @Override
    public int getItemCount() {
        return alSessions.size();
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {

        public TextView textSessionNameRv;
        public TextView textDateRv;
        public TextView textStartTimeRv;
        public TextView textEndTimeRv;
        public TextView textLocationRv;
        public TextView textSportRv;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);

            textSessionNameRv = (TextView) itemView.findViewById(R.id.tvSessionNameRv);
            textDateRv = (TextView) itemView.findViewById(R.id.tvDateRv);
            textStartTimeRv = (TextView) itemView.findViewById(R.id.tvStartTimeRv);
            textEndTimeRv = (TextView) itemView.findViewById(R.id.tvEndTimeRv);
            textLocationRv = (TextView) itemView.findViewById(R.id.tvLocationRv);
            textSportRv = (TextView) itemView.findViewById(R.id.tvSportRv);
        }
    }
}
