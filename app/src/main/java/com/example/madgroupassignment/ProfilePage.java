package com.example.madgroupassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.madgroupassignment.databinding.ActivityLobbyBinding;
import com.example.madgroupassignment.databinding.ActivityProfilePageBinding;

public class ProfilePage extends DrawerBaseAcitivity {

    ActivityProfilePageBinding activityProfilePageBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityProfilePageBinding = ActivityProfilePageBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        allocateActivityTitle("Profile");
        setContentView(activityProfilePageBinding.getRoot());
    }
}