package com.example.sportapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;

public class SportTrainersLVAdapter extends ArrayAdapter<Trainer>
{
    private Context context;
    public SportTrainersLVAdapter(@NonNull Context context, @NonNull ArrayList<Trainer> trainers)
    {
        super(context, 0, trainers);
        this.context = context;
    }



    public int getAge(int year, int month, int dayOfMonth)
    {
        return Period.between(
                LocalDate.of(year, month, dayOfMonth),
                LocalDate.now()
        ).getYears();
    }

    private int getDay(String date)
    {
        int count = 0;
        String day = "";
        while(date.charAt(count) != '/')
        {
            day += date.charAt(count);
            count++;
        }
        return Integer.parseInt(day);
    }

    private int getMonth(String date)
    {
        int count = 0;
        String month = "";
        int slashCount = 0;
        boolean b = false;
        for(int i = 0; i < date.length(); i++)
        {

            if(date.charAt(i) == '/')
            {
                slashCount++;
                b = true;
            }

            if(slashCount == 1 && !b)
            {
                month += date.charAt(i);
            }
            b = false;
        }
        return Integer.parseInt(month);
    }

    private int getYear(String date)
    {
        int slashCount = 0;
        String year = "";
        for(int i = 0; i < date.length(); i++)
        {
            if(slashCount == 2)
            {
                year += date.charAt(i);
            }

            if(date.charAt(i) == '/')
                slashCount++;
        }
        return Integer.parseInt(year);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItemView = convertView;

        if (listItemView==null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.sport_trainers_item,parent,false);
        }

        Trainer trainer = getItem(position);

        TextView textViewFullName = (TextView) listItemView.findViewById(R.id.tvTrainerFullName);
        TextView textViewGender = (TextView) listItemView.findViewById(R.id.tvTrainerGender);
        TextView textViewAge = (TextView) listItemView.findViewById(R.id.tvTrainerAge);

        String age = String.valueOf(getAge(getYear(trainer.getBirthDate()), getMonth(trainer.getBirthDate()), getDay(trainer.getBirthDate())));

        textViewFullName.setText(String.valueOf(trainer.getFullName()));
        textViewGender.setText(String.valueOf(trainer.getGender()));
        textViewAge.setText(age);

        listItemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, TrainerListProfile.class);
                intent.putExtra("trainerEmail", trainer.getEmail());
                context.startActivity(intent);
            }
        });


        return listItemView;
    }




}
