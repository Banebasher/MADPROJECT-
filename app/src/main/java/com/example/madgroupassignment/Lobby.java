package com.example.madgroupassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.madgroupassignment.databinding.ActivityLobbyBinding;


public class Lobby extends DrawerBaseAcitivity {
ActivityLobbyBinding activityLobbyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityLobbyBinding = ActivityLobbyBinding.inflate(getLayoutInflater());
        allocateActivityTitle("LOBBY");
        super.setContentView(activityLobbyBinding.getRoot());





    }
}