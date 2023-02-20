package com.example.sportapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;

public class TraineesLVAdapter extends ArrayAdapter<Trainee>
{
    public TraineesLVAdapter(@NonNull Context context, @NonNull ArrayList<Trainee> trainees)
    {
        super(context, 0, trainees);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItemView = convertView;

        if (listItemView==null)
        {
            if(getItem(position).getGender().equals("Male"))
            {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.male_users_layout,parent,false);
            }
            else if(getItem(position).getGender().equals("Female"))
            {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.female_users_layout,parent,false);
            }
            else
            {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.users_layout,parent,false);
            }
        }

        Trainee trainee = getItem(position);

        TextView textViewFullName = (TextView) listItemView.findViewById(R.id.tvFullName);
        TextView textViewEmail = (TextView) listItemView.findViewById(R.id.tvEmail);
        TextView textViewGender = (TextView) listItemView.findViewById(R.id.tvGender);
        TextView textViewPhoneNumber = (TextView) listItemView.findViewById(R.id.tvPhone);


        textViewFullName.setText(trainee.getFullName());
        textViewEmail.setText(trainee.getEmail());
        textViewGender.setText(String.valueOf(trainee.getGender()));
        textViewPhoneNumber.setText(String.valueOf(trainee.getPhoneNumber()));

        listItemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title);
                builder.setPositiveButton("Open", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Full Name: " + trainee.getFullName() + " Email: " + trainee.getEmail() + " Gender: " + trainee.getGender() + "Phone Number: " + trainee.getPhoneNumber(), Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        return listItemView;
    }




}
