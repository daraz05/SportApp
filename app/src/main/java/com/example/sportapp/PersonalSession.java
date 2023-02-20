package com.example.sportapp;

import android.location.Location;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.Date;

public class PersonalSession extends Session {
    private String trainee;

    public PersonalSession()
    {

    }
    public PersonalSession(String name, Timestamp startTime, Timestamp endTime, String location, String trainer, String sport, String trainee)
    {
        super(name, startTime, endTime, location, trainer, sport);
        this.trainee = trainee;
    }

    public String getTrainee() {
        return trainee;
    }

    public void setTrainee(String trainee) {
        this.trainee = trainee;
    }
}
