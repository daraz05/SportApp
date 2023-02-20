package com.example.sportapp;

import android.location.Location;


import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;

public class GroupSession extends Session
{
    private ArrayList<String> trainees;

    public GroupSession()
    {

    }

    public GroupSession(String name, Timestamp startTime, Timestamp endTime, String location, String trainer, String sport, ArrayList<String> trainees)
    {
        super(name, startTime, endTime, location, trainer, sport);
        this.trainees = trainees;
    }

    public ArrayList<String> getTrainees() {
        return trainees;
    }

    public void setTrainees(ArrayList<String> trainees) {
        this.trainees = trainees;
    }

    public void removeTrainee(String removeTrainee)
    {
        this.trainees.remove(removeTrainee);
    }

    public void addTrainee(String addTrainee)
    {
        this.trainees.add(addTrainee);
    }


}
