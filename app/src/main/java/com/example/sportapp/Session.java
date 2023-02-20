package com.example.sportapp;

import android.location.Location;

import com.google.firebase.Timestamp;

import java.util.Date;


abstract public class Session
{
    private String name;
    private Timestamp startTime;
    private Timestamp endTime;
    private String location;
    private String trainer;
    private String sport;

    public Session()
    {

    }

    public Session(String name, Timestamp startTime, Timestamp endTime, String location, String trainer, String sport)
    {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.trainer = trainer;
        this.sport = sport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public void changeDate(Timestamp newStartTime, Timestamp newEndTime)
    {
        this.startTime = newStartTime;
        this.endTime = newEndTime;
    }

}
