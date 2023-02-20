package com.example.sportapp;

import android.location.Location;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public class Trainer extends Trainee
{
    private String expertises;
    private String description;

    public Trainer(String fullName, String birthDate, String gender, String email, String phoneNumber, String city, String expertises, String description)
    {
        super(fullName, birthDate, gender, email, phoneNumber, city);
        this.expertises = expertises;
        this.description = description;

    }

    public Trainer()
    {
    }

    public String getExpertises() {
        return expertises;
    }

    public void setExpertises(String expertises) {
        this.expertises = expertises;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
